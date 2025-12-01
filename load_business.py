import json
import pymysql
from pymysql.err import OperationalError, IntegrityError, ProgrammingError

# -------------------------- 配置项 --------------------------
DB_CONFIG = {
    "host": "localhost",
    "port": 3306,
    "user": "root",
    "password": "hyw499",
    "database": "niche_ reviews",  # 注意数据库名是否与表结构一致
    "charset": "utf8mb4"
}

JSON_FILE_PATH = "business.json"

FOOD_DRINK_KEYWORDS = {
    "Restaurants", "Food", "Coffee & Tea", "Bars", "Bakeries", "Desserts",
    "Fast Food", "Pizza", "Chinese", "Italian", "Japanese", "Cafes",
    "Ice Cream & Frozen Yogurt", "Juice Bars & Smoothies", "Beer", "Wine & Spirits"
}

BATCH_SIZE = 1000
# -------------------------------------------------------------


def connect_mysql():
    """建立MySQL数据库连接"""
    try:
        conn = pymysql.connect(**DB_CONFIG)
        cursor = conn.cursor()
        print("✅ 数据库连接成功！")
        return conn, cursor
    except OperationalError as e:
        print(f"❌ 数据库连接失败：{e}")
        raise SystemExit(1)


def process_business_data():
    """处理Yelp数据并导入MySQL"""
    conn, cursor = connect_mysql()
    batch_data = []  # 批量存储待插入数据

    try:
        with open(JSON_FILE_PATH, "r", encoding="utf8") as f:
            total_count = 0
            import_count = 0
            skip_count = 0

            for line_num, line in enumerate(f, start=1):
                try:
                    data = json.loads(line.strip())
                    total_count += 1

                    # 筛选餐饮类别商铺
                    categories = data.get("categories")
                    if not categories:
                        skip_count += 1
                        continue

                    category_list = [cat.strip().lower() for cat in categories.split(",")]
                    food_keywords_lower = {keyword.lower() for keyword in FOOD_DRINK_KEYWORDS}
                    if not any(keyword in category_list for keyword in food_keywords_lower):
                        skip_count += 1
                        continue

                    # 处理字段（核心修改：将原business_id存入bid字段）
                    bid = data.get("business_id")  # 原数据中的字符串ID
                    if not bid:
                        skip_count += 1
                        continue

                    # 必选字段
                    name = data.get("name") or "Unknown"
                    address = data.get("address") or "Unknown"
                    city = data.get("city") or "Unknown"
                    state = data.get("state") or "Unknown"
                    stars = data.get("stars") or 0.0
                    review_count = data.get("review_count") or 0

                    # 可选字段
                    postal_code = data.get("postal_code")
                    latitude = data.get("latitude")
                    longitude = data.get("longitude")
                    is_open = data.get("is_open") if data.get("is_open") is not None else 1

                    # JSON字段
                    attributes = json.dumps(data.get("attributes", {})) if data.get("attributes") else None
                    hours = json.dumps(data.get("hours", {})) if data.get("hours") else None

                    # 收集批量数据（不再包含自增的business_id，新增bid字段）
                    batch_data.append((
                        name, address, city, state, postal_code, latitude, longitude,
                        stars, review_count, is_open, categories, attributes, hours, bid
                    ))

                    if len(batch_data) >= BATCH_SIZE:
                        batch_insert(cursor, batch_data)
                        import_count += len(batch_data)
                        batch_data = []
                        print(f"🔄 已处理{total_count}条，导入{import_count}条，跳过{skip_count}条...")

                except json.JSONDecodeError as e:
                    print(f"⚠️ 第{line_num}行JSON解析失败：{e}，跳过该行")
                    skip_count += 1
                    continue
                except Exception as e:
                    print(f"⚠️ 第{line_num}行处理异常：{e}，跳过该行")
                    skip_count += 1
                    continue

            if batch_data:
                batch_insert(cursor, batch_data)
                import_count += len(batch_data)

            conn.commit()
            print("\n" + "="*50)
            print(f"📊 处理完成！")
            print(f"总数据条数：{total_count}")
            print(f"成功导入条数：{import_count}")
            print(f"跳过条数：{skip_count}")
            print("="*50)

    except Exception as e:
        conn.rollback()
        print(f"❌ 批量导入失败，事务回滚：{e}")
    finally:
        cursor.close()
        conn.close()
        print("🔌 数据库连接已关闭")


def batch_insert(cursor, batch_data):
    """批量插入数据（修正SQL字段，匹配表结构）"""
    insert_sql = """
    INSERT INTO `business` (
        name, address, city, state, postal_code, latitude, longitude,
        stars, review_count, is_open, categories, attributes, hours, bid
    ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
    ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP;
    """
    try:
        cursor.executemany(insert_sql, batch_data)
    except IntegrityError as e:
        print(f"⚠️ 批量插入冲突（主键重复）：{e}，跳过重复数据")
    except ProgrammingError as e:
        print(f"⚠️ SQL语法错误：{e}")
        raise


if __name__ == "__main__":
    process_business_data()