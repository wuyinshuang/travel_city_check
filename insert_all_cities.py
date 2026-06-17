import paramiko
import json

host = "47.101.153.130"
port = 22
username = "root"
password = "wuyinshuang@11"

client = paramiko.SSHClient()
client.set_missing_host_key_policy(paramiko.AutoAddPolicy())

try:
    print("正在连接服务器...")
    client.connect(host, port, username, password)
    print("连接成功！")
    
    # 创建插入脚本
    insert_script = '''import json
import subprocess
import requests
import time

# 省份ID到adcode的映射
province_adcodes = {
    1: "110000", 2: "120000", 3: "130000", 4: "140000", 5: "150000",
    6: "210000", 7: "220000", 8: "230000", 9: "310000", 10: "320000",
    11: "330000", 12: "340000", 13: "350000", 14: "360000", 15: "370000",
    16: "410000", 17: "420000", 18: "430000", 19: "440000", 20: "450000",
    21: "460000", 22: "500000", 23: "510000", 24: "520000", 25: "530000",
    26: "540000", 27: "610000", 28: "620000", 29: "630000", 30: "640000",
    31: "650000", 32: "710000", 33: "810000", 34: "820000"
}

total_inserted = 0

for province_id, adcode in province_adcodes.items():
    print(f"\\\\n处理省份 {province_id} ({adcode})...")
    
    url = f"https://geo.datav.aliyun.com/areas_v3/bound/{adcode}_full.json"
    
    try:
        time.sleep(0.5)  # 避免请求过快
        resp = requests.get(url, timeout=30)
        if resp.status_code != 200:
            print(f"  下载失败: {resp.status_code}")
            continue
        
        data = resp.json()
        features = data.get("features", [])
        
        inserted = 0
        for feature in features:
            props = feature.get("properties", {})
            city_adcode = str(props.get("adcode", ""))
            city_name = props.get("name", "")
            
            # 跳过省份本身和特殊区域
            if not city_adcode or city_adcode == adcode:
                continue
            
            # 构建城市GeoJSON
            city_geojson = json.dumps(feature, ensure_ascii=False)
            city_geojson_escaped = city_geojson.replace("'", "''")
            
            # 检查是否已存在
            check_sql = f"SELECT id FROM cities WHERE code = \\'{city_adcode}\\';"
            with open("/tmp/check_city.sql", "w") as f:
                f.write(check_sql)
            
            cmd = "mysql -u travel_city -p'travel_city@123' travel_city < /tmp/check_city.sql -N"
            result = subprocess.run(cmd, shell=True, capture_output=True, text=True)
            
            if result.stdout.strip():
                # 已存在，更新
                sql = f"UPDATE cities SET geojson = \\'{city_geojson_escaped}\\', name = \\'{city_name}\\' WHERE code = \\'{city_adcode}\\';"
            else:
                # 不存在，插入
                sql = f"INSERT INTO cities (name, code, province_id, geojson) VALUES (\\'{city_name}\\', \\'{city_adcode}\\', {province_id}, \\'{city_geojson_escaped}\\');"
            
            with open("/tmp/insert_city.sql", "w") as f:
                f.write(sql)
            
            cmd = "mysql -u travel_city -p'travel_city@123' travel_city < /tmp/insert_city.sql"
            result = subprocess.run(cmd, shell=True, capture_output=True, text=True)
            
            if result.returncode == 0:
                inserted += 1
        
        total_inserted += inserted
        print(f"  处理了 {inserted} 个城市")
        
    except Exception as e:
        print(f"  处理省份 {province_id} 出错: {e}")

print(f"\\\\n总共处理了 {total_inserted} 个城市")
'''
    
    # 上传脚本
    sftp = client.open_sftp()
    with sftp.open('/tmp/insert_all_cities.py', 'w') as f:
        f.write(insert_script)
    sftp.close()
    
    # 执行脚本
    print("\n正在插入所有省份城市数据（这可能需要几分钟）...")
    stdin, stdout, stderr = client.exec_command('python3 /tmp/insert_all_cities.py', timeout=600)
    print(stdout.read().decode('utf-8'))
    
    error = stderr.read().decode('utf-8')
    if error:
        print("错误:", error)
    
    # 验证
    print("\n验证结果...")
    stdin, stdout, stderr = client.exec_command("mysql -u travel_city -p'travel_city@123' travel_city -e \"SELECT COUNT(*) as total FROM cities;\"")
    print(stdout.read().decode('utf-8'))
    
except Exception as e:
    print(f"错误: {e}")
finally:
    client.close()
    print("\n连接已关闭")
