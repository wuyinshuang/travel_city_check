import paramiko

host = "47.101.153.130"
port = 22
username = "root"
password = "wuyinshuang@11"

client = paramiko.SSHClient()
client.set_missing_host_key_policy(paramiko.AutoAddPolicy())
client.connect(host, port, username, password)

# 检查服务器上的前端文件
print("检查服务器前端文件...")
cmd = "ls -la /usr/local/tomcat11/travelcity/ROOT/"
stdin, stdout, stderr = client.exec_command(cmd)
print(stdout.read().decode('utf-8'))

print("\n检查assets目录...")
cmd2 = "ls -la /usr/local/tomcat11/travelcity/ROOT/assets/"
stdin, stdout, stderr = client.exec_command(cmd2)
print(stdout.read().decode('utf-8'))

# 检查war包
print("\n检查后端war包...")
cmd3 = "ls -la /usr/local/tomcat11/travelcity/*.war 2>/dev/null || ls -la /usr/local/tomcat11/webapps/*.war 2>/dev/null || echo '未找到war包'"
stdin, stdout, stderr = client.exec_command(cmd3)
print(stdout.read().decode('utf-8'))

# 检查Tomcat webapps目录
print("\n检查Tomcat webapps目录...")
cmd4 = "ls -la /usr/local/tomcat11/webapps/"
stdin, stdout, stderr = client.exec_command(cmd4)
print(stdout.read().decode('utf-8'))

client.close()
