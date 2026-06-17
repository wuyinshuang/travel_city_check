import paramiko
import os
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

    # 停止Tomcat
    print("\n停止Tomcat...")
    cmd = "/usr/local/tomcat11/bin/shutdown.sh"
    stdin, stdout, stderr = client.exec_command(cmd)
    time.sleep(3)

    # 备份旧WAR包
    print("备份旧WAR包...")
    cmd = "mv /usr/local/tomcat11/travelcity/travel-city-checkin.war /usr/local/tomcat11/travelcity/travel-city-checkin.war.bak 2>/dev/null || echo 'No old war'"
    stdin, stdout, stderr = client.exec_command(cmd)
    stdout.read()

    # 上传新WAR包
    print("上传新WAR包...")
    sftp = client.open_sftp()

    local_war = r"d:\trae\tra_projects\travel_city_check\backend\target\travel-city-checkin.war"
    remote_war = "/usr/local/tomcat11/travelcity/travel-city-checkin.war"

    print(f"   本地文件: {local_war}")
    print(f"   远程路径: {remote_war}")

    # 获取本地文件大小
    local_size = os.path.getsize(local_war)
    print(f"   文件大小: {local_size / 1024 / 1024:.2f} MB")

    sftp.put(local_war, remote_war)
    print("   ✓ WAR包上传完成")

    sftp.close()

    # 启动Tomcat
    print("\n启动Tomcat...")
    cmd = "/usr/local/tomcat11/bin/startup.sh"
    stdin, stdout, stderr = client.exec_command(cmd)
    stdout.read()

    # 等待启动
    print("等待Tomcat启动（约15秒）...")
    time.sleep(15)

    # 验证
    print("\n验证部署...")

    # 检查WAR包
    cmd = "ls -lh /usr/local/tomcat11/travelcity/travel-city-checkin.war"
    stdin, stdout, stderr = client.exec_command(cmd)
    print("WAR包信息:")
    print(stdout.read().decode('utf-8', errors='ignore'))

    # 检查解压目录
    cmd = "ls -la /usr/local/tomcat11/travelcity/travel-city-checkin/ 2>/dev/null | head -10"
    stdin, stdout, stderr = client.exec_command(cmd)
    print("\n解压目录:")
    print(stdout.read().decode('utf-8', errors='ignore'))

    # 测试API
    cmd = "curl -s --max-time 10 'http://localhost:8080/travel-city-checkin/api/v1/provinces' | head -c 100"
    stdin, stdout, stderr = client.exec_command(cmd)
    api_result = stdout.read().decode('utf-8', errors='ignore')
    if api_result:
        print(f"\n✓ API正常: {api_result[:50]}...")
    else:
        print("\n⚠ API可能未就绪，请稍后重试")

    print("\n" + "="*60)
    print("部署完成！")
    print("="*60)
    print("\n访问地址: http://47.101.153.130:8080/travel-city-checkin/")
    print("\n请执行以下操作：")
    print("1. 按 Ctrl+Shift+R (或 Ctrl+F5) 强制刷新浏览器")
    print("2. 如果还是显示旧页面，请清除浏览器缓存后刷新")
    print("="*60)

except Exception as e:
    print(f"错误: {e}")
    import traceback
    traceback.print_exc()
finally:
    client.close()
