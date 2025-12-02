import json
import pymysql
from pymysql.err import OperationalError, IntegrityError, ProgrammingError

# -------------------------- 配置项 --------------------------
DB_CONFIG = {
    "host": "localhost",
    "port": 3306,
    "user": "root",
    "password": "hyw499",
    "database": "niche_ reviews",
    "charset": "utf8mb4"
}

PHOTO_FILE_PATH = "photoData/photos.json"  # 你的photo.json路径
BATCH_SIZE = 1000
# -------------------------------------------------------------


def connect_mysql():
    """建立数据库连接，并获取所有已存在的商户ID（business表的bid）"""
    try:
        conn = pymysql.connect(**DB_CONFIG)
        cursor = conn.cursor()
        
        # 查询business表中所有有效的bid（Yelp原生business_id）
        cursor.execute("SELECT bid FROM business;")
        valid_business_ids = {row[0] for row in cursor.fetchall()}
        print(f"✅ 数据库连接成功，已加载{len(valid_business_ids)}个有效商户ID")
        
        return conn, cursor, valid_business_ids
    except OperationalError as e:
        print(f"❌ 数据库连接失败：{e}")
        raise SystemExit(1)


def process_photo_data():
    conn, cursor, valid_business_ids = connect_mysql()
    batch_data = []
    total_count = 0
    import_count = 0
    skip_count = 0
    invalid_business_count = 0

    try:
        with open(PHOTO_FILE_PATH, "r", encoding="utf8") as f:
            for line_num, line in enumerate(f, start=1):
                try:
                    data = json.loads(line.strip())
                    total_count += 1

                    # -------------------------- 核心过滤：仅保留有效商户的图片 --------------------------
                    business_id = data.get("business_id")
                    if not business_id or business_id not in valid_business_ids:
                        invalid_business_count += 1
                        skip_count += 1
                        continue

                    # 提取图片字段
                    photo_id = data.get("photo_id")
                    if not photo_id:  # 主键为空则跳过
                        skip_count += 1
                        continue

                    caption = data.get("caption")  # 可能为空
                    label = data.get("label")      # 图片分类标签（如food、drink等）

                    # 收集批量数据
                    batch_data.append((
                        photo_id, business_id, caption, label
                    ))

                    # 批量插入
                    if len(batch_data) >= BATCH_SIZE:
                        batch_insert_photo(cursor, batch_data)
                        import_count += len(batch_data)
                        batch_data = []
                        print(f"🔄 已处理{total_count}条，导入{import_count}条，跳过{skip_count}条（无效商户：{invalid_business_count}条）...")

                except json.JSONDecodeError as e:
                    print(f"⚠️ 第{line_num}行JSON解析失败：{e}，跳过")
                    skip_count += 1
                    continue
                except Exception as e:
                    print(f"⚠️ 第{line_num}行处理异常：{e}，跳过")
                    skip_count += 1
                    continue

            # 处理剩余数据
            if batch_data:
                batch_insert_photo(cursor, batch_data)
                import_count += len(batch_data)

        conn.commit()
        print("\n" + "="*60)
        print(f"📊 Photo数据导入完成！")
        print(f"总数据条数：{total_count}")
        print(f"成功导入条数：{import_count}")
        print(f"跳过条数：{skip_count}")
        print(f"其中无效商户图片：{invalid_business_count}条")
        print("="*60)

    except Exception as e:
        conn.rollback()
        print(f"❌ 批量导入失败，事务回滚：{e}")
    finally:
        cursor.close()
        conn.close()
        print("🔌 数据库连接已关闭")


def batch_insert_photo(cursor, batch_data):
    """批量插入图片数据"""
    insert_sql = """
    INSERT INTO `photo` (
        photo_id, business_id, caption, label
    ) VALUES (%s, %s, %s, %s)
    ON DUPLICATE KEY UPDATE 
        caption = VALUES(caption), 
        label = VALUES(label);
    """
    try:
        cursor.executemany(insert_sql, batch_data)
    except IntegrityError as e:
        print(f"⚠️ 主键重复冲突：{e}，跳过重复数据")
    except ProgrammingError as e:
        print(f"⚠️ SQL语法错误：{e}")
        raise


if __name__ == "__main__":
    process_photo_data()