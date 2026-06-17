import paramiko
import time

host = "47.101.153.130"
port = 22
username = "root"
password = "wuyinshuang@11"

client = paramiko.SSHClient()
client.set_missing_host_key_policy(paramiko.AutoAddPolicy())
client.connect(host, port, username, password)

# 等待Tomcat完全启动
time.sleep(3)

# 测试API
print("测试API...")
cmd = "curl -s --max-time 15 'http://localhost:8080/travel-city-checkin/api/v1/provinces/geojson' 2>&1 | head -c 200"
stdin, stdout, stderr = client.exec_command(cmd)
result = stdout.read().decode('utf-8', errors='ignore')

if result and len(result) > 10:
    print(f"✓ API正常: {result[:80]}...")
else:
    print(f"× API响应: {result}")

# 检查前端JS文件内容（确认包含修复代码）
print("\n检查前端JS文件...")
cmd2 = "grep -o 'noteId.*checkRes' /usr/local/tomcat11/travelcity/ROOT/assets/*.js | head -1"
stdin, stdout, stderr = client.exec_command(cmd2)
js_check = stdout.read().decode('utf-8', errors='ignore')
if js_check:
    print(f"✓ 包含备注重复保存修复: {js_check[:50]}")
else:
    print("× 未找到修复代码")

cmd3 = "grep -o 'pendingImages' /usr/local/tomcat11/travelcity/ROOT/assets/*.js | head -1"
stdin, stdout, stderr = client.exec_command(cmd3)
upload_check = stdout.read().decode('utf-8', errors='ignore')
if upload_check:
    print(f"✓ 包含多图上传功能: {upload_check}")
else:
    print("× 未找到多图上传代码")

client.close()

print("\n" + "="*50)
print("请强制刷新浏览器：Ctrl+Shift+R (或 Ctrl+F5)")
print("="*50)
