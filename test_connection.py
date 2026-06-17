import paramiko
import socket

host = "47.101.153.130"
port = 22
username = "root"
password = "wuyinshuang@11"

# 先测试端口连通性
print("测试SSH端口连通性...")
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
sock.settimeout(10)
try:
    result = sock.connect_ex((host, port))
    if result == 0:
        print(f"端口{port}可达")
    else:
        print(f"端口{port}不可达 (错误码: {result})")
except Exception as e:
    print(f"连接失败: {e}")
finally:
    sock.close()

# 尝试SSH连接
print("\n尝试SSH连接...")
try:
    client = paramiko.SSHClient()
    client.set_missing_host_key_policy(paramiko.AutoAddPolicy())
    client.connect(host, port, username, password, timeout=20)
    print("连接成功！")
    
    # 检查Tomcat
    cmd = "systemctl status tomcat11 2>/dev/null || /usr/local/tomcat11/bin/version.sh 2>/dev/null || echo 'checking process'; ps aux | grep java | grep -v grep | head -1"
    stdin, stdout, stderr = client.exec_command(cmd)
    print("\nTomcat状态:")
    print(stdout.read().decode('utf-8')[:200])
    
    client.close()
except Exception as e:
    print(f"SSH连接失败: {e}")
