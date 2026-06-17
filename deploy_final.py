import paramiko
import os

host = "47.101.153.130"
port = 22
username = "root"
password = "wuyinshuang@11"

client = paramiko.SSHClient()
client.set_missing_host_key_policy(paramiko.AutoAddPolicy())

try:
    print("正在连接服务器...")
    client.connect(host, port, username, password)
    print("连接成功！")
    
    # 清理旧文件
    print("\n清理旧文件...")
    cmd = "rm -rf /usr/local/tomcat11/travelcity/ROOT/assets/*"
    stdin, stdout, stderr = client.exec_command(cmd)
    stdout.read()
    
    # 上传新文件
    print("\n上传新文件...")
    sftp = client.open_sftp()
    
    # 上传index.html
    local_index = r"d:\trae\tra_projects\travel_city_check\frontend\dist\index.html"
    remote_index = "/usr/local/tomcat11/travelcity/ROOT/index.html"
    sftp.put(local_index, remote_index)
    print("上传: index.html")
    
    # 创建assets目录
    try:
        sftp.mkdir("/usr/local/tomcat11/travelcity/ROOT/assets")
    except:
        pass
    
    # 上传assets文件
    assets_dir = r"d:\trae\tra_projects\travel_city_check\frontend\dist\assets"
    for filename in os.listdir(assets_dir):
        local_path = os.path.join(assets_dir, filename)
        remote_path = f"/usr/local/tomcat11/travelcity/ROOT/assets/{filename}"
        sftp.put(local_path, remote_path)
        print(f"上传: assets/{filename}")
    
    sftp.close()
    
    # 验证
    print("\n验证文件...")
    stdin, stdout, stderr = client.exec_command("ls -la /usr/local/tomcat11/travelcity/ROOT/assets/")
    print(stdout.read().decode('utf-8'))
    
    print("\n部署完成！")
    
except Exception as e:
    print(f"错误: {e}")
finally:
    client.close()
