import paramiko
import time

host = "47.101.153.130"
port = 22
username = "root"
password = "wuyinshuang@11"

client = paramiko.SSHClient()
client.set_missing_host_key_policy(paramiko.AutoAddPolicy())

try:
    print("正在连接服务器...")
    client.connect(host, port, username, password, timeout=30)
    print("连接成功！")
    
    # 检查Tomcat状态
    print("\n检查Tomcat状态...")
    cmd = "ps aux | grep tomcat | grep -v grep | head -1"
    stdin, stdout, stderr = client.exec_command(cmd)
    result = stdout.read().decode('utf-8')
    if result:
        print(f"Tomcat运行中: {result.strip()[:100]}")
    else:
        print("Tomcat未运行！正在重启...")
        cmd2 = "/usr/local/tomcat11/bin/startup.sh"
        stdin, stdout, stderr = client.exec_command(cmd2)
        time.sleep(3)
    
    # 测试API
    print("\n测试API...")
    cmd3 = "curl -s -o /dev/null -w '%{http_code}' 'http://localhost:8080/travel-city-checkin/api/v1/provinces/geojson'"
    stdin, stdout, stderr = client.exec_command(cmd3)
    status_code = stdout.read().decode('utf-8')
    print(f"API状态码: {status_code}")
    
    # 检查前端文件
    print("\n检查前端文件...")
    cmd4 = "ls -la /usr/local/tomcat11/travelcity/ROOT/"
    stdin, stdout, stderr = client.exec_command(cmd4)
    print(stdout.read().decode('utf-8'))
    
except Exception as e:
    print(f"错误: {e}")
finally:
    client.close()
