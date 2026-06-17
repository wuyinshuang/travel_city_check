import paramiko

host = "47.101.153.130"
port = 22
username = "root"
password = "wuyinshuang@11"

client = paramiko.SSHClient()
client.set_missing_host_key_policy(paramiko.AutoAddPolicy())
client.connect(host, port, username, password)

# 检查服务器上的JS文件内容
cmd = "grep -o 'borderColor.*000' /usr/local/tomcat11/travelcity/ROOT/assets/*.js | head -5"
stdin, stdout, stderr = client.exec_command(cmd)
print("服务器JS文件中的borderColor配置:")
print(stdout.read().decode('utf-8'))

# 检查是否有黑色边框配置
cmd2 = "grep -o 'itemStyle.*borderColor' /usr/local/tomcat11/travelcity/ROOT/assets/*.js | head -3"
stdin, stdout, stderr = client.exec_command(cmd2)
print("\nitemStyle配置:")
print(stdout.read().decode('utf-8'))

client.close()
