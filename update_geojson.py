import json
import sys

def generate_update_sql():
    """生成更新 SQL 语句"""
    print("正在生成 SQL 更新脚本...")
    
    with open("china_geojson.json", "r", encoding="utf-8") as f:
        data = json.load(f)
    
    features = data.get('features', [])
    
    with open("update_geojson.sql", "w", encoding="utf-8") as f:
        for feature in features:
            properties = feature.get('properties', {})
            adcode = str(properties.get('adcode', ''))
            name = properties.get('name', '')
            
            if not adcode:
                continue
            
            # 构建 GeoJSON 字符串
            feature_collection = {
                "type": "FeatureCollection",
                "features": [feature]
            }
            geojson_str = json.dumps(feature_collection, ensure_ascii=False)
            
            # 转义单引号
            geojson_str = geojson_str.replace("'", "''")
            
            # 生成 SQL
            sql = f"UPDATE provinces SET geojson = '{geojson_str}' WHERE code = '{adcode}';\n"
            f.write(sql)
            print(f"已生成: {name} ({adcode})")
    
    print(f"\nSQL 脚本已保存到 update_geojson.sql，共 {len(features)} 条语句")

if __name__ == "__main__":
    generate_update_sql()
