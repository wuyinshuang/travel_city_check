import paramiko

host = "47.101.153.130"
port = 22
username = "root"
password = "wuyinshuang@11"

client = paramiko.SSHClient()
client.set_missing_host_key_policy(paramiko.AutoAddPolicy())
client.connect(host, port, username, password)

# 检查前端文件
print("检查前端文件...")
cmd = "ls -la /usr/local/tomcat11/travelcity/ROOT/"
stdin, stdout, stderr = client.exec_command(cmd)
print(stdout.read().decode('utf-8'))

# 检查API
print("\n测试GeoJSON API...")
cmd2 = "curl -s 'http://localhost:8080/travel-city-checkin/api/v1/provinces/geojson' | head -c 200"
stdin, stdout, stderr = client.exec_command(cmd2)
result = stdout.read().decode('utf-8')
if result:
    print(f"API响应: {result[:100]}...")
else:
    print("API无响应")

# 检查Tomcat日志是否有错误
print("\n检查Tomcat最新日志...")
cmd3 = "tail -20 /usr/local/tomcat11/logs/catalina.out 2>/dev/null || echo '无日志文件'"
stdin, stdout, stderr = client.exec_command(cmd3)
print(stdout.read().decode('utf-8'))

# 重启Tomcat
print("\n重启Tomcat...")
cmd4 = "/usr/local/tomcat11/bin/shutdown.sh && sleep 2 && /usr/local/tomcat11/bin/startup.sh"
stdin, stdout, stderr = client.exec_command(cmd4)
time.sleep(5)
print("Tomcat已重启")

# 再次测试API
print("\n再次测试API...")
cmd5 = "curl -s -o /dev/null -w '%{http_code}' 'http://localhost:8080/travel-city-checkin/api/v1/provinces/geojson'"
stdin, stdout, stderr = client.exec_command(cmd5)
status = stdout.read().decode('utf-8')
print(f"HTTP状态码: {status}")

client.close()
