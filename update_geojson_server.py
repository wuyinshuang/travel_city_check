import paramiko
import json

# 服务器信息
host = "47.101.153.130"
port = 22
username = "root"
password = "wuyinshuang@11"

# 创建 SSH 客户端
client = paramiko.SSHClient()
client.set_missing_host_key_policy(paramiko.AutoAddPolicy())

try:
    print("正在连接服务器...")
    client.connect(host, port, username, password)
    print("连接成功！")
    
    # 在服务器上下载 GeoJSON 数据
    print("正在下载中国地图 GeoJSON 数据...")
    stdin, stdout, stderr = client.exec_command('curl -s "https://geo.datav.aliyun.com/areas_v3/bound/100000_full.json" -o /tmp/china_geojson.json')
    stdout.channel.recv_exit_status()
    print("下载完成！")
    
    # 创建 Python 脚本在服务器上执行
    update_script = """import json
import subprocess
import sys

# 读取 GeoJSON 数据
with open('/tmp/china_geojson.json', 'r', encoding='utf-8') as f:
    data = json.load(f)

features = data.get('features', [])
updated = 0

for feature in features:
    properties = feature.get('properties', {})
    adcode = str(properties.get('adcode', ''))
    name = properties.get('name', '')
    
    if not adcode or adcode == '100000_JD':
        continue
    
    # 构建 GeoJSON 字符串
    feature_collection = {
        "type": "FeatureCollection",
        "features": [feature]
    }
    geojson_str = json.dumps(feature_collection, ensure_ascii=False)
    
    # 转义单引号
    geojson_str = geojson_str.replace("'", "''")
    
    # 生成 SQL
    sql = "UPDATE provinces SET geojson = '" + geojson_str + "' WHERE code = '" + adcode + "';"
    
    # 执行 SQL
    cmd = "mysql -u travel_city -p'travel_city@123' travel_city -e " + chr(34) + sql + chr(34)
    result = subprocess.run(cmd, shell=True, capture_output=True, text=True)
    
    if result.returncode == 0:
        updated += 1
        print(f"已更新: {name} ({adcode})")
    else:
        print(f"更新 {name} 失败: {result.stderr}")

print(f"\\n更新完成，共更新 {updated} 个省份")
"""
    
    # 上传脚本
    sftp = client.open_sftp()
    with sftp.open('/tmp/update_geojson.py', 'w') as f:
        f.write(update_script)
    sftp.close()
    
    # 执行脚本
    print("\n正在更新数据库...")
    stdin, stdout, stderr = client.exec_command('python3 /tmp/update_geojson.py')
    print(stdout.read().decode('utf-8'))
    
    error = stderr.read().decode('utf-8')
    if error:
        print("错误:", error)
    
except Exception as e:
    print(f"错误: {e}")
finally:
    client.close()
    print("\n连接已关闭")
