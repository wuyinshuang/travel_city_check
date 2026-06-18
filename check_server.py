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

    # 检查Tomcat进程
    print("\n检查Tomcat进程...")
    cmd = "ps aux | grep tomcat | grep -v grep"
    stdin, stdout, stderr = client.exec_command(cmd)
    result = stdout.read().decode('utf-8', errors='ignore')
    if result:
        print(result)
    else:
        print("⚠ Tomcat进程未运行！")

    # 检查端口
    print("\n检查8080端口...")
    cmd = "netstat -tlnp | grep 8080"
    stdin, stdout, stderr = client.exec_command(cmd)
    port_result = stdout.read().decode('utf-8', errors='ignore')
    print(port_result if port_result else "端口8080未监听")

    # 检查Tomcat日志最后30行
    print("\n检查Tomcat日志（最后30行）...")
    cmd = "tail -30 /usr/local/tomcat11/logs/catalina.out"
    stdin, stdout, stderr = client.exec_command(cmd)
    logs = stdout.read().decode('utf-8', errors='ignore')
    print(logs)

except Exception as e:
    print(f"错误: {e}")
finally:
    client.close()
