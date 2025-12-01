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

# 数据文件路径
USER_FILE_PATH = "user.json"      # 用户文件

BATCH_SIZE = 1000
# -------------------------------------------------------------


def connect_mysql():
    """建立数据库连接"""
    try:
        conn = pymysql.connect(**DB_CONFIG)
        cursor = conn.cursor()
        print("✅ 数据库连接成功！")
        # 预查询所有已导入的用户ID，存入集合（快速查询）
        cursor.execute("SELECT user_id FROM reviews;")
        valid_user_ids = {row[0] for row in cursor.fetchall()}
        print(f"✅ 已加载{len(valid_user_ids)}个有效用户ID")
        
        return conn, cursor, valid_user_ids
    except OperationalError as e:
        print(f"❌ 数据库连接失败：{e}")
        raise SystemExit(1)





def import_filtered_users():
    """只导入在review中存在的用户（基于去重后的user_id）"""
    conn, cursor,valid_user_ids = connect_mysql()
    valid_user_ids=set(valid_user_ids)
    batch_data = []
    try:
        with open(USER_FILE_PATH, "r", encoding="utf8") as f:
            total = 0
            imported = 0
            skipped = 0

            for line_num, line in enumerate(f, start=1):
                try:
                    data = json.loads(line.strip())
                    total += 1

                    # 核心过滤：只保留review中存在的user_id
                    uid = data.get("user_id")
                    if not uid or uid not in valid_user_ids:
                        skipped += 1
                        continue

                    # 处理用户字段（对应你的user表结构）
                    name = data.get("name")
                    password = None  # yelp数据无密码，可留空或设默认值
                    email = None     # yelp数据无邮箱，可留空
                    gender = None    # yelp数据无性别
                    age = None       # yelp数据无年龄
                    city = data.get("city")
                    review_count = data.get("review_count") or 0
                    cool = data.get("cool") or 0
                    useful = data.get("useful") or 0
                    elite = data.get("elite")  # 格式如"2019,2020"
                    yelping_since = data.get("yelping_since")  # 格式如"2010-01-01"
                    funny = data.get("funny") or 0
                    friends = ",".join(data.get("friends", []))  # 列表转字符串

                    batch_data.append((
                        uid, name, password, email, gender, age, city,
                        review_count, cool, useful, elite, yelping_since, funny, friends
                    ))

                    # 批量插入
                    if len(batch_data) >= BATCH_SIZE:
                        batch_insert(cursor, batch_data)
                        imported += len(batch_data)
                        batch_data = []
                        print(f"处理{total}条用户数据，导入{imported}条，跳过{skipped}条...")

                except json.JSONDecodeError as e:
                    print(f"⚠️ 第{line_num}行JSON解析失败：{e}，跳过")
                    skipped += 1
                    continue
                except Exception as e:
                    print(f"⚠️ 第{line_num}行处理异常：{e}，跳过")
                    skipped += 1
                    continue

            # 处理剩余数据
            if batch_data:
                batch_insert(cursor, batch_data)
                imported += len(batch_data)

            conn.commit()
            print("\n" + "="*50)
            print(f"总用户数据：{total}条")
            print(f"成功导入（review中存在的用户）：{imported}条")
            print(f"跳过（不在review中或无效）：{skipped}条")
            print("="*50)

    except Exception as e:
        conn.rollback()
        print(f"❌ 导入失败，事务回滚：{e}")
    finally:
        cursor.close()
        conn.close()
        print("🔌 数据库连接已关闭")


def batch_insert(cursor, batch_data):
    """批量插入用户数据"""
    insert_sql = """
    INSERT INTO `user` (
        uid, name, password, email, gender, age, city,
        review_count, cool, useful, elite, yelping_since, funny, friends
    ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s);
    """
    try:
        cursor.executemany(insert_sql, batch_data)
    except IntegrityError as e:
        print(f"⚠️ 主键重复冲突：{e}，跳过重复数据")
    except ProgrammingError as e:
        print(f"⚠️ SQL语法错误：{e}")
        raise


if __name__ == "__main__":
    
    import_filtered_users()