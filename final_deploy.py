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
    
    # 清理旧文件
    print("清理旧文件...")
    cmd = "rm -rf /usr/local/tomcat11/travelcity/ROOT/*"
    stdin, stdout, stderr = client.exec_command(cmd)
    stdout.read()
    
    # 上传新文件
    print("上传新文件...")
    sftp = client.open_sftp()
    
    try:
        sftp.mkdir("/usr/local/tomcat11/travelcity/ROOT/assets")
    except:
        pass
    
    # 上传index.html
    local_index = r"d:\trae\tra_projects\travel_city_check\frontend\dist\index.html"
    remote_index = "/usr/local/tomcat11/travelcity/ROOT/index.html"
    sftp.put(local_index, remote_index)
    print("   ✓ index.html")
    
    # 上传assets
    assets_dir = r"d:\trae\tra_projects\travel_city_check\frontend\dist\assets"
    for filename in os.listdir(assets_dir):
        local_path = os.path.join(assets_dir, filename)
        remote_path = f"/usr/local/tomcat11/travelcity/ROOT/assets/{filename}"
        sftp.put(local_path, remote_path)
        print(f"   ✓ assets/{filename}")
    
    sftp.close()
    
    # 启动Tomcat
    print("\n启动Tomcat...")
    cmd = "/usr/local/tomcat11/bin/startup.sh"
    stdin, stdout, stderr = client.exec_command(cmd)
    stdout.read()
    
    # 等待启动
    print("等待Tomcat启动...")
    time.sleep(8)
    
    # 验证
    print("\n验证部署...")
    cmd = "ls -la /usr/local/tomcat11/travelcity/ROOT/assets/"
    stdin, stdout, stderr = client.exec_command(cmd)
    files = stdout.read().decode('utf-8', errors='ignore')
    print(files)
    
    cmd2 = "curl -s --max-time 10 'http://localhost:8080/travel-city-checkin/api/v1/provinces/geojson' | head -c 50"
    stdin, stdout, stderr = client.exec_command(cmd2)
    api_result = stdout.read().decode('utf-8', errors='ignore')
    if api_result:
        print(f"\n✓ API正常: {api_result[:30]}...")
    
    print("\n" + "="*60)
    print("部署完成！")
    print("="*60)
    print("\n请执行以下操作：")
    print("1. 按 Ctrl+Shift+R (或 Ctrl+F5) 强制刷新浏览器")
    print("2. 如果还是显示旧页面，请清除浏览器缓存后刷新")
    print("="*60)
    
except Exception as e:
    print(f"错误: {e}")
finally:
    client.close()
