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
    
    # 省份adcode列表
    province_adcodes = {
        1: "110000",  # 北京市
        2: "120000",  # 天津市
        3: "130000",  # 河北省
        # ... 可以添加更多
    }
    
    # 创建更新脚本
    update_script = '''import json
import subprocess
import requests

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

updated_total = 0

for province_id, adcode in province_adcodes.items():
    print(f"\\\\n处理省份 {province_id} ({adcode})...")
    
    # 下载省份的城市GeoJSON
    url = f"https://geo.datav.aliyun.com/areas_v3/bound/{adcode}_full.json"
    try:
        resp = requests.get(url, timeout=30)
        if resp.status_code != 200:
            print(f"  下载失败: {resp.status_code}")
            continue
        
        data = resp.json()
        features = data.get("features", [])
        
        updated = 0
        for feature in features:
            props = feature.get("properties", {})
            city_adcode = str(props.get("adcode", ""))
            city_name = props.get("name", "")
            
            # 跳过省份本身和特殊区域
            if not city_adcode or city_adcode == adcode or city_adcode.endswith("00"):
                continue
            
            # 构建城市GeoJSON
            city_geojson = json.dumps(feature, ensure_ascii=False)
            city_geojson_escaped = city_geojson.replace("'", "''")
            
            # 更新数据库
            sql = f"UPDATE cities SET geojson = \\'{city_geojson_escaped}\\' WHERE code = \\'{city_adcode}\\';"
            
            with open("/tmp/update_city.sql", "w") as f:
                f.write(sql)
            
            cmd = "mysql -u travel_city -p'travel_city@123' travel_city < /tmp/update_city.sql"
            result = subprocess.run(cmd, shell=True, capture_output=True, text=True)
            
            if result.returncode == 0:
                updated += 1
                print(f"  已更新: {city_name} ({city_adcode})")
        
        updated_total += updated
        print(f"  省份 {province_id} 更新了 {updated} 个城市")
        
    except Exception as e:
        print(f"  处理省份 {province_id} 出错: {e}")

print(f"\\\\n总共更新了 {updated_total} 个城市")
'''
    
    # 上传脚本
    sftp = client.open_sftp()
    with sftp.open('/tmp/update_city_geojson.py', 'w') as f:
        f.write(update_script)
    sftp.close()
    
    # 执行脚本
    print("\n正在更新城市GeoJSON数据...")
    stdin, stdout, stderr = client.exec_command('python3 /tmp/update_city_geojson.py')
    print(stdout.read().decode('utf-8'))
    
    error = stderr.read().decode('utf-8')
    if error:
        print("错误:", error)
    
except Exception as e:
    print(f"错误: {e}")
finally:
    client.close()
    print("\n连接已关闭")
