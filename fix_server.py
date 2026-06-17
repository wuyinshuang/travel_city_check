import paramiko
import time

host = "47.101.153.130"
port = 22
username = "root"
password = "wuyinshuang@11"

client = paramiko.SSHClient()
client.set_missing_host_key_policy(paramiko.AutoAddPolicy())
client.connect(host, port, username, password)

# 检查assets目录
print("检查assets目录...")
cmd = "ls -la /usr/local/tomcat11/travelcity/ROOT/assets/"
stdin, stdout, stderr = client.exec_command(cmd)
print(stdout.read().decode('utf-8'))

# 测试API（使用完整URL）
print("\n测试API...")
cmd2 = "curl -s --max-time 10 'http://localhost:8080/travel-city-checkin/api/v1/provinces/geojson' 2>&1 | head -c 300"
stdin, stdout, stderr = client.exec_command(cmd2)
result = stdout.read().decode('utf-8')
error = stderr.read().decode('utf-8')
if result:
    print(f"API正常: {result[:150]}...")
else:
    print(f"API错误或超时: {error}")

# 重启Tomcat
print("\n重启Tomcat...")
cmd3 = "/usr/local/tomcat11/bin/shutdown.sh"
stdin, stdout, stderr = client.exec_command(cmd3)
time.sleep(3)

cmd4 = "/usr/local/tomcat11/bin/startup.sh"
stdin, stdout, stderr = client.exec_command(cmd4)
print(stdout.read().decode('utf-8'))

# 等待Tomcat启动
time.sleep(5)

# 再次测试
print("\n再次测试API...")
cmd5 = "curl -s --max-time 15 'http://localhost:8080/travel-city-checkin/api/v1/provinces/geojson' | head -c 200"
stdin, stdout, stderr = client.exec_command(cmd5)
result2 = stdout.read().decode('utf-8')
if result2 and len(result2) > 10:
    print(f"API恢复: {result2[:100]}...")
else:
    print(f"API仍无响应")

client.close()
