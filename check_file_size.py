import paramiko

host = "47.101.153.130"
port = 22
username = "root"
password = "wuyinshuang@11"

client = paramiko.SSHClient()
client.set_missing_host_key_policy(paramiko.AutoAddPolicy())
client.connect(host, port, username, password)

# 检查文件大小
print("检查服务器文件大小...")
cmd = "ls -la /usr/local/tomcat11/travelcity/ROOT/assets/"
stdin, stdout, stderr = client.exec_command(cmd)
print(stdout.read().decode('utf-8'))

# 检查本地文件大小
import os
local_file = r"d:\trae\tra_projects\travel_city_check\frontend\dist\assets\index-D_W0_w9U.js"
print(f"\n本地文件大小: {os.path.getsize(local_file)} bytes")

client.close()
