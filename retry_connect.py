import paramiko
import time

host = "47.101.153.130"
port = 22
username = "root"
password = "wuyinshuang@11"

# 重试连接3次
for attempt in range(3):
    try:
        print(f"第{attempt + 1}次尝试连接服务器...")
        client = paramiko.SSHClient()
        client.set_missing_host_key_policy(paramiko.AutoAddPolicy())
        client.connect(host, port, username, password, timeout=15)
        print("连接成功！")
        
        # 检查Tomcat状态
        print("\n检查Tomcat...")
        cmd = "ps aux | grep java | grep tomcat | wc -l"
        stdin, stdout, stderr = client.exec_command(cmd)
        result = stdout.read().decode('utf-8').strip()
        
        if result == '0':
            print("Tomcat未运行，正在启动...")
            cmd2 = "/usr/local/tomcat11/bin/startup.sh"
            stdin, stdout, stderr = client.exec_command(cmd2)
            time.sleep(5)
            print("Tomcat已启动")
        else:
            print(f"Tomcat运行中 (进程数: {result})")
        
        # 测试API
        print("\n测试API响应...")
        cmd3 = "curl -s -m 10 'http://localhost:8080/travel-city-checkin/api/v1/provinces/geojson' | head -c 100"
        stdin, stdout, stderr = client.exec_command(cmd3)
        api_result = stdout.read().decode('utf-8')
        if api_result:
            print(f"API正常: {api_result[:50]}...")
        else:
            print("API无响应")
        
        client.close()
        break
        
    except Exception as e:
        print(f"连接失败: {e}")
        if attempt < 2:
            print("等待5秒后重试...")
            time.sleep(5)
