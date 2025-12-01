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

TIPS_FILE_PATH = "tip.json"  # 你的tip.json路径
BATCH_SIZE = 500  # 调小批量大小，提升稳定性
# -------------------------------------------------------------

def connect_mysql():
    """建立数据库连接，并预加载所有有效的餐饮商户business_id"""
    try:
        conn = pymysql.connect(**DB_CONFIG)
        cursor = conn.cursor()
        print("✅ 数据库连接成功！")
        
        # 预查询所有已导入的餐饮商户ID，存入集合（快速查询）
        cursor.execute("SELECT business_id FROM business;")
        valid_business_ids = {row[0] for row in cursor.fetchall()}
        print(f"✅ 已加载{len(valid_business_ids)}个有效餐饮商户ID")
        
        return conn, cursor, valid_business_ids
    except OperationalError as e:
        print(f"❌ 数据库连接失败：{e}")
        raise SystemExit(1)

def process_tips_data():
    conn, cursor, valid_business_ids = connect_mysql()
    batch_data = []
    total_count = 0
    import_count = 0
    skip_count = 0
    invalid_business_count = 0  # 统计无效商户的贴士数

    try:
        with open(TIPS_FILE_PATH, "r", encoding="utf8") as f:
            for line_num, line in enumerate(f, start=1):
                try:
                    data = json.loads(line.strip())
                    total_count += 1

                    # -------------------------- 过滤无效商户的贴士 --------------------------
                    business_id = data.get("business_id")
                    if not business_id or business_id not in valid_business_ids:
                        invalid_business_count += 1
                        skip_count += 1
                        continue

                    # 提取字段并处理空值
                    user_id = data.get("user_id") or "Unknown"
                    text = data.get("text") or ""
                    compliment_count = data.get("compliment_count") or 0
                    
                    # 处理日期格式
                    date_str = data.get("date")
                    date = None
                    if date_str:
                        try:
                            date = datetime.strptime(date_str, "%Y-%m-%d %H:%M:%S").date()
                        except ValueError:
                            date = None

                    batch_data.append((
                        user_id, business_id, text, date, compliment_count
                    ))

                    # 达到批量大小则插入
                    if len(batch_data) >= BATCH_SIZE:
                        batch_insert_tips(cursor, batch_data)
                        import_count += len(batch_data)
                        batch_data = []
                        print(f"🔄 已处理{total_count}条，导入{import_count}条，跳过{skip_count}条（无效商户贴士：{invalid_business_count}条）...")

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
                batch_insert_tips(cursor, batch_data)
                import_count += len(batch_data)

        conn.commit()
        print("\n" + "="*60)
        print(f"📊 Tips导入完成！")
        print(f"总数据条数：{total_count}")
        print(f"成功导入条数：{import_count}")
        print(f"跳过条数：{skip_count}")
        print(f"其中无效商户贴士（business_id不存在）：{invalid_business_count}条")
        print("="*60)

    except Exception as e:
        conn.rollback()
        print(f"❌ 批量导入失败，事务回滚：{e}")
    finally:
        cursor.close()
        conn.close()
        print("🔌 数据库连接已关闭")

def batch_insert_tips(cursor, batch_data):
    insert_sql = """
    INSERT INTO `tips` (
        user_id, business_id, text, date, compliment_count
    ) VALUES (%s, %s, %s, %s, %s)
    ON DUPLICATE KEY UPDATE 
        compliment_count = VALUES(compliment_count);
    """
    try:
        cursor.executemany(insert_sql, batch_data)
    except IntegrityError as e:
        # 批量冲突时改为单条插入，跳过重复数据
        print(f"⚠️ 批量插入冲突，改为单条过滤：{e}")
        for data in batch_data:
            try:
                cursor.execute(insert_sql, data)
            except IntegrityError:
                continue

if __name__ == "__main__":
    process_tips_data()