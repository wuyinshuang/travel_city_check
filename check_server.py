import paramiko

host = "47.101.153.130"
port = 22
username = "root"
password = "wuyinshuang@11"

client = paramiko.SSHClient()
client.set_missing_host_key_policy(paramiko.AutoAddPolicy())
client.connect(host, port, username, password)

# 检查Tomcat状态
print("检查Tomcat状态...")
cmd = "ps aux | grep tomcat | grep -v grep"
stdin, stdout, stderr = client.exec_command(cmd)
print(stdout.read().decode('utf-8'))

# 检查端口
print("\n检查8080端口...")
cmd2 = "netstat -tlnp | grep 8080"
stdin, stdout, stderr = client.exec_command(cmd2)
print(stdout.read().decode('utf-8'))

# 测试API
print("\n测试API...")
cmd3 = "curl -s 'http://localhost:8080/travel-city-checkin/api/v1/provinces/geojson' | head -c 200"
stdin, stdout, stderr = client.exec_command(cmd3)
result = stdout.read().decode('utf-8')
print(result if result else "无响应")

# 检查文件
print("\n检查前端文件...")
cmd4 = "ls -la /usr/local/tomcat11/travelcity/ROOT/"
stdin, stdout, stderr = client.exec_command(cmd4)
print(stdout.read().decode('utf-8'))

client.close()
