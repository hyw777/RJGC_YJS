import json
import pymysql
from pymysql.err import OperationalError, IntegrityError, ProgrammingError
from datetime import datetime

# -------------------------- 配置项 --------------------------
DB_CONFIG = {
    "host": "localhost",
    "port": 3306,
    "user": "root",
    "password": "hyw499",
    "database": "food_rec",
    "charset": "utf8mb4"
}

REVIEW_FILE_PATH = "review.json"  # 你的review.json路径
BATCH_SIZE = 500  # 调小批量大小，减少冲突时的重试成本
# -------------------------------------------------------------

def connect_mysql():
    """建立数据库连接，并预加载所有有效的business_id（餐饮商户）"""
    try:
        conn = pymysql.connect(**DB_CONFIG)
        cursor = conn.cursor()
        print("✅ 数据库连接成功！")
        
        # 预查询所有已导入的餐饮商户business_id，存入集合（O(1)查询效率）
        cursor.execute("SELECT business_id FROM business;")
        valid_business_ids = {row[0] for row in cursor.fetchall()}
        print(f"✅ 已加载{len(valid_business_ids)}个有效餐饮商户ID")
        
        return conn, cursor, valid_business_ids
    except OperationalError as e:
        print(f"❌ 数据库连接失败：{e}")
        raise SystemExit(1)

def process_review_data():
    conn, cursor, valid_business_ids = connect_mysql()
    batch_data = []
    total_count = 0
    import_count = 0
    skip_count = 0
    invalid_business_count = 0  # 统计无效business_id的评论数

    try:
        with open(REVIEW_FILE_PATH, "r", encoding="utf8") as f:
            for line_num, line in enumerate(f, start=1):
                try:
                    data = json.loads(line.strip())
                    total_count += 1

                    # -------------------------- 核心过滤：只保留有效business_id的评论 --------------------------
                    business_id = data.get("business_id")
                    if not business_id or business_id not in valid_business_ids:
                        invalid_business_count += 1
                        skip_count += 1
                        continue  # 跳过非餐饮商户的评论


                    user_id = data.get("user_id") or "Unknown"
                    stars = data.get("stars") or 0.0
                    useful = data.get("useful") or 0
                    funny = data.get("funny") or 0
                    cool = data.get("cool") or 0
                    text = data.get("text") or ""
                    
                    # 处理日期格式
                    date_str = data.get("date")
                    date = None
                    if date_str:
                        try:
                            date = datetime.strptime(date_str, "%Y-%m-%d %H:%M:%S").date()
                        except ValueError:
                            date = None

                    batch_data.append((
                        user_id, business_id, stars,
                        useful, funny, cool, text, date
                    ))

                    # 达到批量大小则插入
                    if len(batch_data) >= BATCH_SIZE:
                        batch_insert_review(cursor, batch_data)
                        import_count += len(batch_data)
                        batch_data = []
                        print(f"🔄 已处理{total_count}条，导入{import_count}条，跳过{skip_count}条（无效商户评论：{invalid_business_count}条）...")

                except json.JSONDecodeError as e:
                    print(f"⚠️ 第{line_num}行JSON解析失败：{e}")
                    skip_count += 1
                    continue
                except Exception as e:
                    print(f"⚠️ 第{line_num}行处理异常：{e}")
                    skip_count += 1
                    continue

            # 处理剩余数据
            if batch_data:
                batch_insert_review(cursor, batch_data)
                import_count += len(batch_data)

        conn.commit()
        print("\n" + "="*60)
        print(f"📊 Review导入完成！")
        print(f"总数据条数：{total_count}")
        print(f"成功导入条数：{import_count}")
        print(f"跳过条数：{skip_count}")
        print(f"其中无效商户评论（business_id不存在）：{invalid_business_count}条")
        print("="*60)

    except Exception as e:
        conn.rollback()
        print(f"❌ 批量导入失败，事务回滚：{e}")
    finally:
        cursor.close()
        conn.close()
        print("🔌 数据库连接已关闭")

def batch_insert_review(cursor, batch_data):
    insert_sql = """
    INSERT INTO `review` (
        user_id, business_id, stars, useful, funny, cool, text, date
    ) VALUES ( %s, %s, %s, %s, %s, %s, %s, %s);
    """
    try:
        cursor.executemany(insert_sql, batch_data)
    except IntegrityError as e:
        # 若仍有冲突，可能是批量中混入了无效ID，改为单条插入并过滤
        print(f"⚠️ 批量插入冲突，改为单条过滤：{e}")
        for data in batch_data:
            try:
                cursor.execute(insert_sql, data)
            except IntegrityError:
                continue  # 跳过单个冲突数据

if __name__ == "__main__":
    process_review_data()