import paramiko
import os

host = "47.101.153.130"
port = 22
username = "root"
password = "wuyinshuang@11"

local_dist = "d:\\trae\\tra_projects\\travel_city_check\\frontend\\dist"
remote_path = "/usr/local/tomcat11/travelcity/ROOT"

client = paramiko.SSHClient()
client.set_missing_host_key_policy(paramiko.AutoAddPolicy())

try:
    print("正在连接服务器...")
    client.connect(host, port, username, password)
    print("连接成功！")
    
    sftp = client.open_sftp()
    
    def upload_directory(local_dir, remote_dir):
        try:
            sftp.stat(remote_dir)
        except FileNotFoundError:
            sftp.mkdir(remote_dir)
        
        for item in os.listdir(local_dir):
            local_path = os.path.join(local_dir, item)
            remote_item_path = os.path.join(remote_dir, item).replace("\\", "/")
            
            if os.path.isfile(local_path):
                print(f"上传文件: {item}")
                sftp.put(local_path, remote_item_path)
            elif os.path.isdir(local_path):
                print(f"创建目录: {item}")
                upload_directory(local_path, remote_item_path)
    
    print("\n开始上传前端文件...")
    upload_directory(local_dist, remote_path)
    
    print("\n部署完成！")
    sftp.close()
    
except Exception as e:
    print(f"错误: {e}")
finally:
    client.close()
    print("\n连接已关闭")
