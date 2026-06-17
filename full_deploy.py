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
    
    # 1. 停止Tomcat
    print("\n1. 停止Tomcat...")
    cmd = "/usr/local/tomcat11/bin/shutdown.sh"
    stdin, stdout, stderr = client.exec_command(cmd)
    time.sleep(3)
    print("Tomcat已停止")
    
    # 2. 彻底清理旧文件
    print("\n2. 清理旧文件...")
    cmd = "rm -rf /usr/local/tomcat11/travelcity/ROOT/*"
    stdin, stdout, stderr = client.exec_command(cmd)
    stdout.read()
    print("旧文件已清理")
    
    # 3. 上传新文件
    print("\n3. 上传新文件...")
    sftp = client.open_sftp()
    
    # 创建目录
    try:
        sftp.mkdir("/usr/local/tomcat11/travelcity/ROOT/assets")
    except:
        pass
    
    # 上传index.html
    local_index = r"d:\trae\tra_projects\travel_city_check\frontend\dist\index.html"
    remote_index = "/usr/local/tomcat11/travelcity/ROOT/index.html"
    sftp.put(local_index, remote_index)
    print("   - index.html ✓")
    
    # 上传assets
    assets_dir = r"d:\trae\tra_projects\travel_city_check\frontend\dist\assets"
    for filename in os.listdir(assets_dir):
        local_path = os.path.join(assets_dir, filename)
        remote_path = f"/usr/local/tomcat11/travelcity/ROOT/assets/{filename}"
        sftp.put(local_path, remote_path)
        print(f"   - assets/{filename} ✓")
    
    sftp.close()
    print("文件上传完成")
    
    # 4. 启动Tomcat
    print("\n4. 启动Tomcat...")
    cmd = "/usr/local/tomcat11/bin/startup.sh"
    stdin, stdout, stderr = client.exec_command(cmd)
    result = stdout.read().decode('utf-8')
    print(result)
    
    # 等待启动
    print("\n5. 等待Tomcat启动...")
    time.sleep(8)
    
    # 6. 验证部署
    print("\n6. 验证部署...")
    cmd = "ls -la /usr/local/tomcat11/travelcity/ROOT/"
    stdin, stdout, stderr = client.exec_command(cmd)
    print(stdout.read().decode('utf-8'))
    
    cmd2 = "curl -s --max-time 10 'http://localhost:8080/travel-city-checkin/api/v1/provinces/geojson' | head -c 100"
    stdin, stdout, stderr = client.exec_command(cmd2)
    api_result = stdout.read().decode('utf-8')
    if api_result and len(api_result) > 10:
        print(f"\nAPI测试通过: {api_result[:50]}...")
    else:
        print("\nAPI可能还在启动中，请稍后刷新页面")
    
    print("\n" + "="*50)
    print("部署完成！请强制刷新浏览器（Ctrl+Shift+R）")
    print("="*50)
    
except Exception as e:
    print(f"错误: {e}")
finally:
    client.close()
