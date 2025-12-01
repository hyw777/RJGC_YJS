import json
import pymysql
from pymysql.err import OperationalError, IntegrityError, ProgrammingError

# -------------------------- 配置项 --------------------------
# MySQL数据库连接信息
DB_CONFIG = {
    "host": "localhost",       # 数据库地址
    "port": 3306,              # 端口
    "user": "root",   # 用户名
    "password": "hyw499",# 密码
    "database": "food_rec",# 数据库名
    "charset": "utf8mb4"       # 字符集（必须utf8mb4兼容特殊字符）
}

# Yelp数据文件路径（替换为你的business.json路径）
JSON_FILE_PATH = "business.json"

# 餐饮/饮品相关关键词（可根据需求扩展）
FOOD_DRINK_KEYWORDS = {
    "Restaurants", "Food", "Coffee & Tea", "Bars", "Bakeries", "Desserts",
    "Fast Food", "Pizza", "Chinese", "Italian", "Japanese", "Cafes",
    "Ice Cream & Frozen Yogurt", "Juice Bars & Smoothies", "Beer", "Wine & Spirits"
}

# 批量插入数量（每1000条提交一次，提高效率）
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
        # 1. 读取JSON文件
        with open(JSON_FILE_PATH, "r", encoding="utf8") as f:
            total_count = 0    # 总处理条数
            import_count = 0   # 成功导入条数
            skip_count = 0     # 跳过条数（非餐饮/无效数据）

            for line_num, line in enumerate(f, start=1):
                try:
                    # 解析单行JSON
                    data = json.loads(line.strip())
                    total_count += 1

                    # -------------------------- 2. 筛选餐饮类别商铺 --------------------------
                    categories = data.get("categories")
                    if not categories:
                        skip_count += 1
                        continue  # 无类别字段，跳过

                    # 拆分类别并匹配关键词（不区分大小写）
                    category_list = [cat.strip().lower() for cat in categories.split(",")]
                    food_keywords_lower = {keyword.lower() for keyword in FOOD_DRINK_KEYWORDS}
                    if not any(keyword in category_list for keyword in food_keywords_lower):
                        skip_count += 1
                        continue  # 非餐饮类，跳过

                    # -------------------------- 3. 处理字段空值 --------------------------
                    business_id = data.get("business_id")
                    if not business_id:  # 主键为空，跳过
                        skip_count += 1
                        continue

                    # 必选字段（NOT NULL）：空值填充默认值
                    name = data.get("name") or "Unknown"
                    address = data.get("address") or "Unknown"
                    city = data.get("city") or "Unknown"
                    state = data.get("state") or "Unknown"
                    stars = data.get("stars") or 0.0
                    review_count = data.get("review_count") or 0

                    # 可选字段：允许NULL
                    postal_code = data.get("postal_code")
                    latitude = data.get("latitude")
                    longitude = data.get("longitude")
                    is_open = data.get("is_open") if data.get("is_open") is not None else 1

                    # JSON字段：序列化为JSON字符串（空则存NULL）
                    attributes = json.dumps(data.get("attributes", {})) if data.get("attributes") else None
                    hours = json.dumps(data.get("hours", {})) if data.get("hours") else None

                    # -------------------------- 4. 收集批量数据 --------------------------
                    batch_data.append((
                        business_id, name, address, city, state,
                        postal_code, latitude, longitude, stars,
                        review_count, is_open, categories, attributes, hours
                    ))

                    # 达到批量大小则插入
                    if len(batch_data) >= BATCH_SIZE:
                        batch_insert(cursor, batch_data)
                        import_count += len(batch_data)
                        batch_data = []  # 清空批量列表
                        print(f"🔄 已处理{total_count}条，导入{import_count}条，跳过{skip_count}条...")

                except json.JSONDecodeError as e:
                    print(f"⚠️ 第{line_num}行JSON解析失败：{e}，跳过该行")
                    skip_count += 1
                    continue
                except Exception as e:
                    print(f"⚠️ 第{line_num}行处理异常：{e}，跳过该行")
                    skip_count += 1
                    continue

            # 处理剩余数据（不足BATCH_SIZE的部分）
            if batch_data:
                batch_insert(cursor, batch_data)
                import_count += len(batch_data)

            # 提交事务
            conn.commit()
            print("\n" + "="*50)
            print(f"📊 处理完成！")
            print(f"总数据条数：{total_count}")
            print(f"成功导入条数：{import_count}")
            print(f"跳过条数：{skip_count}")
            print("="*50)

    except Exception as e:
        conn.rollback()  # 异常回滚
        print(f"❌ 批量导入失败，事务回滚：{e}")
    finally:
        # 关闭游标和连接
        cursor.close()
        conn.close()
        print("🔌 数据库连接已关闭")


def batch_insert(cursor, batch_data):
    """批量插入数据到MySQL（参数化查询防注入）"""
    insert_sql = """
    INSERT INTO `business` (
        business_id, name, address, city, state,
        postal_code, latitude, longitude, stars,
        review_count, is_open, categories, attributes, hours
    ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
    ON DUPLICATE KEY UPDATE updated_at = CURRENT_TIMESTAMP;  # 主键重复时更新时间戳（可选）
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