import paramiko

host = "47.101.153.130"
port = 22
username = "root"
password = "wuyinshuang@11"

client = paramiko.SSHClient()
client.set_missing_host_key_policy(paramiko.AutoAddPolicy())
client.connect(host, port, username, password)

# 检查Tomcat配置
print("检查Tomcat server.xml配置...")
cmd = "cat /usr/local/tomcat11/conf/server.xml | grep -A5 -B5 'Context\\|Host'"
stdin, stdout, stderr = client.exec_command(cmd)
print(stdout.read().decode('utf-8'))

# 检查war包解压后的前端文件
print("\n检查war包解压后的前端文件...")
cmd2 = "ls -la /usr/local/tomcat11/webapps/travel-city-checkin/"
stdin, stdout, stderr = client.exec_command(cmd2)
print(stdout.read().decode('utf-8'))

# 检查war包中的前端文件
print("\n检查war包中的前端文件...")
cmd3 = "ls -la /usr/local/tomcat11/webapps/travel-city-checkin/assets/ 2>/dev/null || echo '无assets目录'"
stdin, stdout, stderr = client.exec_command(cmd3)
print(stdout.read().decode('utf-8'))

# 检查访问路径
print("\n测试访问路径...")
cmd4 = "curl -s -o /dev/null -w '%{http_code}' 'http://localhost:8080/travel-city-checkin/'"
stdin, stdout, stderr = client.exec_command(cmd4)
code1 = stdout.read().decode('utf-8')
print(f"  /travel-city-checkin/ -> HTTP {code1}")

cmd5 = "curl -s -o /dev/null -w '%{http_code}' 'http://localhost:8080/'"
stdin, stdout, stderr = client.exec_command(cmd5)
code2 = stdout.read().decode('utf-8')
print(f"  / -> HTTP {code2}")

client.close()
