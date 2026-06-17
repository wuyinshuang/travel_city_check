import requests
import json
import sys

def download_china_geojson():
    """下载中国地图 GeoJSON 数据"""
    print("正在下载中国地图 GeoJSON 数据...")
    url = "https://geo.datav.aliyun.com/areas_v3/bound/100000_full.json"
    
    try:
        response = requests.get(url, timeout=30)
        response.raise_for_status()
        data = response.json()
        print(f"下载成功，共 {len(data.get('features', []))} 个省份")
        
        # 保存到文件
        with open("china_geojson.json", "w", encoding="utf-8") as f:
            json.dump(data, f, ensure_ascii=False, indent=2)
        print("已保存到 china_geojson.json")
        
        return data
    except Exception as e:
        print(f"下载失败: {e}")
        return None

if __name__ == "__main__":
    download_china_geojson()
