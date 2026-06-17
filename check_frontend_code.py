import paramiko

host = "47.101.153.130"
port = 22
username = "root"
password = "wuyinshuang@11"

client = paramiko.SSHClient()
client.set_missing_host_key_policy(paramiko.AutoAddPolicy())
client.connect(host, port, username, password)

# 检查前端JS文件中是否包含noteId.value = res.data.data.id
print("检查前端代码中的saveNote方法...")
cmd = "grep -o 'noteId.*res.data.data.id' /usr/local/tomcat11/travelcity/ROOT/assets/*.js"
stdin, stdout, stderr = client.exec_command(cmd)
result = stdout.read().decode('utf-8')
print(result if result else "未找到修复代码")

# 检查完整的saveNote方法
print("\n检查saveNote方法...")
cmd2 = "grep -A 5 'saveNote' /usr/local/tomcat11/travelcity/ROOT/assets/*.js | head -20"
stdin, stdout, stderr = client.exec_command(cmd2)
print(stdout.read().decode('utf-8'))

client.close()
