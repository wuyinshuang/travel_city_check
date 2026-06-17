package com.travelcity.config;

import com.travelcity.entity.City;
import com.travelcity.entity.Province;
import com.travelcity.entity.User;
import com.travelcity.repository.CityRepository;
import com.travelcity.repository.ProvinceRepository;
import com.travelcity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (provinceRepository.count() == 0) {
            insertProvinces();
            insertCities();
        }

        ensureAdminUser();
    }

    private void insertProvinces() {
        String[][] provinces = {
            {"北京市", "110000", "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"name\":\"北京市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[115.7,39.4],[117.4,39.4],[117.4,41.0],[115.7,41.0],[115.7,39.4]]]}}]}"},
            {"天津市", "120000", "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"name\":\"天津市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[116.4,38.5],[118.0,38.5],[118.0,40.2],[116.4,40.2],[116.4,38.5]]]}}]}"},
            {"河北省", "130000", "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"name\":\"河北省\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[113.4,36.0],[119.5,36.0],[119.5,42.6],[113.4,42.6],[113.4,36.0]]]}}]}"},
            {"山西省", "140000", "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"name\":\"山西省\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[110.3,34.5],[114.8,34.5],[114.8,40.5],[110.3,40.5],[110.3,34.5]]]}}]}"},
            {"内蒙古自治区", "150000", "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"name\":\"内蒙古自治区\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[97.0,37.0],[126.0,37.0],[126.0,53.0],[97.0,53.0],[97.0,37.0]]]}}]}"},
            {"辽宁省", "210000", "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"name\":\"辽宁省\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[118.5,38.7],[125.2,38.7],[125.2,43.5],[118.5,43.5],[118.5,38.7]]]}}]}"},
            {"吉林省", "220000", "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"name\":\"吉林省\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[121.8,40.5],[131.2,40.5],[131.2,46.5],[121.8,46.5],[121.8,40.5]]]}}]}"},
            {"黑龙江省", "230000", "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"name\":\"黑龙江省\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[121.1,43.2],[135.1,43.2],[135.1,53.5],[121.1,53.5],[121.1,43.2]]]}}]}"},
            {"上海市", "310000", "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"name\":\"上海市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[120.8,30.6],[122.0,30.6],[122.0,31.8],[120.8,31.8],[120.8,30.6]]]}}]}"},
            {"江苏省", "320000", "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"name\":\"江苏省\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[116.4,30.5],[121.9,30.5],[121.9,35.0],[116.4,35.0],[116.4,30.5]]]}}]}"},
            {"浙江省", "330000", "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"name\":\"浙江省\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[118.0,27.2],[123.5,27.2],[123.5,31.3],[118.0,31.3],[118.0,27.2]]]}}]}"},
            {"安徽省", "340000", "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"name\":\"安徽省\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[114.8,29.4],[119.8,29.4],[119.8,34.6],[114.8,34.6],[114.8,29.4]]]}}]}"},
            {"福建省", "350000", "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"name\":\"福建省\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[115.0,23.3],[120.0,23.3],[120.0,28.3],[115.0,28.3],[115.0,23.3]]]}}]}"},
            {"江西省", "360000", "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"name\":\"江西省\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[113.9,24.7],[118.4,24.7],[118.4,30.0],[113.9,30.0],[113.9,24.7]]]}}]}"},
            {"山东省", "370000", "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"name\":\"山东省\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[114.8,34.3],[122.0,34.3],[122.0,38.5],[114.8,38.5],[114.8,34.3]]]}}]}"},
            {"河南省", "410000", "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"name\":\"河南省\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[110.4,31.3],[116.6,31.3],[116.6,36.3],[110.4,36.3],[110.4,31.3]]]}}]}"},
            {"湖北省", "420000", "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"name\":\"湖北省\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[108.3,29.0],[116.0,29.0],[116.0,33.5],[108.3,33.5],[108.3,29.0]]]}}]}"},
            {"湖南省", "430000", "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"name\":\"湖南省\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[108.5,24.8],[114.5,24.8],[114.5,30.0],[108.5,30.0],[108.5,24.8]]]}}]}"},
            {"广东省", "440000", "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"name\":\"广东省\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[109.6,20.1],[117.2,20.1],[117.2,25.5],[109.6,25.5],[109.6,20.1]]]}}]}"},
            {"广西壮族自治区", "450000", "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"name\":\"广西壮族自治区\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[104.8,20.8],[112.4,20.8],[112.4,26.3],[104.8,26.3],[104.8,20.8]]]}}]}"},
            {"海南省", "460000", "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"name\":\"海南省\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[108.5,18.0],[111.5,18.0],[111.5,20.2],[108.5,20.2],[108.5,18.0]]]}}]}"},
            {"重庆市", "500000", "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"name\":\"重庆市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[105.8,28.1],[110.3,28.1],[110.3,32.2],[105.8,32.2],[105.8,28.1]]]}}]}"},
            {"四川省", "510000", "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"name\":\"四川省\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[97.2,26.0],[108.6,26.0],[108.6,34.3],[97.2,34.3],[97.2,26.0]]]}}]}"},
            {"贵州省", "520000", "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"name\":\"贵州省\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[103.3,24.7],[109.4,24.7],[109.4,29.3],[103.3,29.3],[103.3,24.7]]]}}]}"},
            {"云南省", "530000", "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"name\":\"云南省\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[97.3,21.1],[106.2,21.1],[106.2,29.2],[97.3,29.2],[97.3,21.1]]]}}]}"},
            {"西藏自治区", "540000", "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"name\":\"西藏自治区\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[78.2,26.5],[99.3,26.5],[99.3,36.0],[78.2,36.0],[78.2,26.5]]]}}]}"},
            {"陕西省", "610000", "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"name\":\"陕西省\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[105.4,31.8],[111.2,31.8],[111.2,39.6],[105.4,39.6],[105.4,31.8]]]}}]}"},
            {"甘肃省", "620000", "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"name\":\"甘肃省\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[92.0,32.3],[108.0,32.3],[108.0,42.7],[92.0,42.7],[92.0,32.3]]]}}]}"},
            {"青海省", "630000", "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"name\":\"青海省\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[89.4,31.3],[103.0,31.3],[103.0,39.2],[89.4,39.2],[89.4,31.3]]]}}]}"},
            {"宁夏回族自治区", "640000", "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"name\":\"宁夏回族自治区\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[104.9,35.1],[107.4,35.1],[107.4,39.3],[104.9,39.3],[104.9,35.1]]]}}]}"},
            {"新疆维吾尔自治区", "650000", "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"name\":\"新疆维吾尔自治区\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[73.4,34.5],[96.2,34.5],[96.2,49.1],[73.4,49.1],[73.4,34.5]]]}}]}"},
            {"香港特别行政区", "810000", "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"name\":\"香港特别行政区\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[113.8,22.2],[114.3,22.2],[114.3,22.5],[113.8,22.5],[113.8,22.2]]]}}]}"},
            {"澳门特别行政区", "820000", "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"name\":\"澳门特别行政区\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[113.5,22.1],[113.6,22.1],[113.6,22.2],[113.5,22.2],[113.5,22.1]]]}}]}"},
            {"台湾省", "710000", "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"name\":\"台湾省\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[120.5,21.9],[122.0,21.9],[122.0,25.3],[120.5,25.3],[120.5,21.9]]]}}]}"}
        };

        for (String[] p : provinces) {
            Province province = new Province();
            province.setName(p[0]);
            province.setCode(p[1]);
            province.setGeojson(p[2]);
            provinceRepository.save(province);
        }
    }

    private void insertCities() {
        String[][] cities = {
            {"东城区", "1", "110101", "{\"type\":\"Feature\",\"properties\":{\"name\":\"东城区\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[116.4,39.9],[116.5,39.9],[116.5,39.95],[116.4,39.95],[116.4,39.9]]]}}"},
            {"西城区", "1", "110102", "{\"type\":\"Feature\",\"properties\":{\"name\":\"西城区\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[116.3,39.9],[116.4,39.9],[116.4,39.95],[116.3,39.95],[116.3,39.9]]]}}"},
            {"朝阳区", "1", "110105", "{\"type\":\"Feature\",\"properties\":{\"name\":\"朝阳区\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[116.4,39.8],[116.6,39.8],[116.6,40.0],[116.4,40.0],[116.4,39.8]]]}}"},
            {"海淀区", "1", "110108", "{\"type\":\"Feature\",\"properties\":{\"name\":\"海淀区\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[116.2,39.9],[116.35,39.9],[116.35,40.1],[116.2,40.1],[116.2,39.9]]]}}"},
            {"黄浦区", "9", "310101", "{\"type\":\"Feature\",\"properties\":{\"name\":\"黄浦区\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[121.47,31.23],[121.49,31.23],[121.49,31.24],[121.47,31.24],[121.47,31.23]]]}}"},
            {"徐汇区", "9", "310104", "{\"type\":\"Feature\",\"properties\":{\"name\":\"徐汇区\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[121.43,31.18],[121.47,31.18],[121.47,31.23],[121.43,31.23],[121.43,31.18]]]}}"},
            {"浦东新区", "9", "310115", "{\"type\":\"Feature\",\"properties\":{\"name\":\"浦东新区\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[121.5,31.15],[121.8,31.15],[121.8,31.35],[121.5,31.35],[121.5,31.15]]]}}"},
            {"广州市", "19", "440100", "{\"type\":\"Feature\",\"properties\":{\"name\":\"广州市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[113.2,23.0],[113.5,23.0],[113.5,23.3],[113.2,23.3],[113.2,23.0]]]}}"},
            {"深圳市", "19", "440300", "{\"type\":\"Feature\",\"properties\":{\"name\":\"深圳市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[113.9,22.4],[114.3,22.4],[114.3,22.7],[113.9,22.7],[113.9,22.4]]]}}"},
            {"珠海市", "19", "440400", "{\"type\":\"Feature\",\"properties\":{\"name\":\"珠海市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[113.3,22.1],[113.5,22.1],[113.5,22.3],[113.3,22.3],[113.3,22.1]]]}}"},
            {"南京市", "10", "320100", "{\"type\":\"Feature\",\"properties\":{\"name\":\"南京市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[118.7,32.0],[118.9,32.0],[118.9,32.15],[118.7,32.15],[118.7,32.0]]]}}"},
            {"苏州市", "10", "320500", "{\"type\":\"Feature\",\"properties\":{\"name\":\"苏州市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[120.5,31.3],[120.8,31.3],[120.8,31.5],[120.5,31.5],[120.5,31.3]]]}}"},
            {"无锡市", "10", "320200", "{\"type\":\"Feature\",\"properties\":{\"name\":\"无锡市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[120.2,31.5],[120.4,31.5],[120.4,31.65],[120.2,31.65],[120.2,31.5]]]}}"},
            {"杭州市", "11", "330100", "{\"type\":\"Feature\",\"properties\":{\"name\":\"杭州市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[120.1,30.2],[120.3,30.2],[120.3,30.4],[120.1,30.4],[120.1,30.2]]]}}"},
            {"宁波市", "11", "330200", "{\"type\":\"Feature\",\"properties\":{\"name\":\"宁波市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[121.5,29.8],[121.8,29.8],[121.8,30.0],[121.5,30.0],[121.5,29.8]]]}}"},
            {"温州市", "11", "330300", "{\"type\":\"Feature\",\"properties\":{\"name\":\"温州市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[120.6,28.0],[120.8,28.0],[120.8,28.2],[120.6,28.2],[120.6,28.0]]]}}"},
            {"长沙市", "18", "430100", "{\"type\":\"Feature\",\"properties\":{\"name\":\"长沙市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[112.9,28.2],[113.1,28.2],[113.1,28.35],[112.9,28.35],[112.9,28.2]]]}}"},
            {"株洲市", "18", "430200", "{\"type\":\"Feature\",\"properties\":{\"name\":\"株洲市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[113.1,27.8],[113.3,27.8],[113.3,28.0],[113.1,28.0],[113.1,27.8]]]}}"},
            {"湘潭市", "18", "430300", "{\"type\":\"Feature\",\"properties\":{\"name\":\"湘潭市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[112.8,27.8],[113.0,27.8],[113.0,28.0],[112.8,28.0],[112.8,27.8]]]}}"},
            {"成都市", "23", "510100", "{\"type\":\"Feature\",\"properties\":{\"name\":\"成都市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[103.8,30.6],[104.1,30.6],[104.1,30.8],[103.8,30.8],[103.8,30.6]]]}}"},
            {"绵阳市", "23", "510700", "{\"type\":\"Feature\",\"properties\":{\"name\":\"绵阳市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[104.6,31.4],[104.8,31.4],[104.8,31.6],[104.6,31.6],[104.6,31.4]]]}}"},
            {"德阳市", "23", "510600", "{\"type\":\"Feature\",\"properties\":{\"name\":\"德阳市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[104.2,31.0],[104.4,31.0],[104.4,31.2],[104.2,31.2],[104.2,31.0]]]}}"},
            {"西安市", "27", "610100", "{\"type\":\"Feature\",\"properties\":{\"name\":\"西安市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[108.9,34.2],[109.1,34.2],[109.1,34.35],[108.9,34.35],[108.9,34.2]]]}}"},
            {"宝鸡市", "27", "610300", "{\"type\":\"Feature\",\"properties\":{\"name\":\"宝鸡市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[107.1,34.3],[107.3,34.3],[107.3,34.5],[107.1,34.5],[107.1,34.3]]]}}"},
            {"咸阳市", "27", "610400", "{\"type\":\"Feature\",\"properties\":{\"name\":\"咸阳市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[108.7,34.3],[108.9,34.3],[108.9,34.5],[108.7,34.5],[108.7,34.3]]]}}"},
            {"武汉市", "17", "420100", "{\"type\":\"Feature\",\"properties\":{\"name\":\"武汉市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[114.2,30.5],[114.5,30.5],[114.5,30.7],[114.2,30.7],[114.2,30.5]]]}}"},
            {"宜昌市", "17", "420500", "{\"type\":\"Feature\",\"properties\":{\"name\":\"宜昌市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[111.2,30.6],[111.4,30.6],[111.4,30.8],[111.2,30.8],[111.2,30.6]]]}}"},
            {"襄阳市", "17", "420600", "{\"type\":\"Feature\",\"properties\":{\"name\":\"襄阳市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[112.0,32.0],[112.2,32.0],[112.2,32.2],[112.0,32.2],[112.0,32.0]]]}}"},
            {"郑州市", "16", "410100", "{\"type\":\"Feature\",\"properties\":{\"name\":\"郑州市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[113.6,34.7],[113.8,34.7],[113.8,34.85],[113.6,34.85],[113.6,34.7]]]}}"},
            {"洛阳市", "16", "410300", "{\"type\":\"Feature\",\"properties\":{\"name\":\"洛阳市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[112.4,34.6],[112.6,34.6],[112.6,34.8],[112.4,34.8],[112.4,34.6]]]}}"},
            {"开封市", "16", "410200", "{\"type\":\"Feature\",\"properties\":{\"name\":\"开封市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[114.3,34.8],[114.5,34.8],[114.5,35.0],[114.3,35.0],[114.3,34.8]]]}}"},
            {"济南市", "15", "370100", "{\"type\":\"Feature\",\"properties\":{\"name\":\"济南市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[117.0,36.6],[117.2,36.6],[117.2,36.8],[117.0,36.8],[117.0,36.6]]]}}"},
            {"青岛市", "15", "370200", "{\"type\":\"Feature\",\"properties\":{\"name\":\"青岛市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[120.3,36.0],[120.5,36.0],[120.5,36.2],[120.3,36.2],[120.3,36.0]]]}}"},
            {"烟台市", "15", "370600", "{\"type\":\"Feature\",\"properties\":{\"name\":\"烟台市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[121.3,37.5],[121.5,37.5],[121.5,37.7],[121.3,37.7],[121.3,37.5]]]}}"},
            {"沈阳市", "6", "210100", "{\"type\":\"Feature\",\"properties\":{\"name\":\"沈阳市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[123.3,41.8],[123.5,41.8],[123.5,42.0],[123.3,42.0],[123.3,41.8]]]}}"},
            {"大连市", "6", "210200", "{\"type\":\"Feature\",\"properties\":{\"name\":\"大连市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[121.5,38.9],[121.7,38.9],[121.7,39.1],[121.5,39.1],[121.5,38.9]]]}}"},
            {"鞍山市", "6", "210300", "{\"type\":\"Feature\",\"properties\":{\"name\":\"鞍山市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[122.8,41.1],[123.0,41.1],[123.0,41.3],[122.8,41.3],[122.8,41.1]]]}}"},
            {"哈尔滨市", "8", "230100", "{\"type\":\"Feature\",\"properties\":{\"name\":\"哈尔滨市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[126.5,45.8],[126.7,45.8],[126.7,46.0],[126.5,46.0],[126.5,45.8]]]}}"},
            {"齐齐哈尔市", "8", "230200", "{\"type\":\"Feature\",\"properties\":{\"name\":\"齐齐哈尔市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[123.9,47.3],[124.1,47.3],[124.1,47.5],[123.9,47.5],[123.9,47.3]]]}}"},
            {"大庆市", "8", "230600", "{\"type\":\"Feature\",\"properties\":{\"name\":\"大庆市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[124.8,46.5],[125.0,46.5],[125.0,46.7],[124.8,46.7],[124.8,46.5]]]}}"},
            {"长春市", "7", "220100", "{\"type\":\"Feature\",\"properties\":{\"name\":\"长春市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[125.2,43.8],[125.4,43.8],[125.4,44.0],[125.2,44.0],[125.2,43.8]]]}}"},
            {"吉林市", "7", "220200", "{\"type\":\"Feature\",\"properties\":{\"name\":\"吉林市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[126.5,43.8],[126.7,43.8],[126.7,44.0],[126.5,44.0],[126.5,43.8]]]}}"},
            {"合肥市", "12", "340100", "{\"type\":\"Feature\",\"properties\":{\"name\":\"合肥市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[117.2,31.8],[117.4,31.8],[117.4,32.0],[117.2,32.0],[117.2,31.8]]]}}"},
            {"芜湖市", "12", "340200", "{\"type\":\"Feature\",\"properties\":{\"name\":\"芜湖市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[118.2,31.3],[118.4,31.3],[118.4,31.5],[118.2,31.5],[118.2,31.3]]]}}"},
            {"福州市", "13", "350100", "{\"type\":\"Feature\",\"properties\":{\"name\":\"福州市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[119.2,26.0],[119.4,26.0],[119.4,26.2],[119.2,26.2],[119.2,26.0]]]}}"},
            {"厦门市", "13", "350200", "{\"type\":\"Feature\",\"properties\":{\"name\":\"厦门市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[118.0,24.5],[118.2,24.5],[118.2,24.7],[118.0,24.7],[118.0,24.5]]]}}"},
            {"南昌市", "14", "360100", "{\"type\":\"Feature\",\"properties\":{\"name\":\"南昌市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[115.8,28.6],[116.0,28.6],[116.0,28.8],[115.8,28.8],[115.8,28.6]]]}}"},
            {"九江市", "14", "360400", "{\"type\":\"Feature\",\"properties\":{\"name\":\"九江市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[115.6,29.6],[115.8,29.6],[115.8,29.8],[115.6,29.8],[115.6,29.6]]]}}"},
            {"南宁市", "20", "450100", "{\"type\":\"Feature\",\"properties\":{\"name\":\"南宁市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[108.3,22.8],[108.5,22.8],[108.5,23.0],[108.3,23.0],[108.3,22.8]]]}}"},
            {"桂林市", "20", "450300", "{\"type\":\"Feature\",\"properties\":{\"name\":\"桂林市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[110.2,25.2],[110.4,25.2],[110.4,25.4],[110.2,25.4],[110.2,25.2]]]}}"},
            {"渝中区", "22", "500103", "{\"type\":\"Feature\",\"properties\":{\"name\":\"渝中区\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[106.5,29.5],[106.6,29.5],[106.6,29.6],[106.5,29.6],[106.5,29.5]]]}}"},
            {"江北区", "22", "500105", "{\"type\":\"Feature\",\"properties\":{\"name\":\"江北区\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[106.5,29.6],[106.7,29.6],[106.7,29.8],[106.5,29.8],[106.5,29.6]]]}}"},
            {"昆明市", "25", "530100", "{\"type\":\"Feature\",\"properties\":{\"name\":\"昆明市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[102.7,25.0],[102.9,25.0],[102.9,25.2],[102.7,25.2],[102.7,25.0]]]}}"},
            {"大理市", "25", "532900", "{\"type\":\"Feature\",\"properties\":{\"name\":\"大理市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[100.2,25.7],[100.4,25.7],[100.4,25.9],[100.2,25.9],[100.2,25.7]]]}}"},
            {"贵阳市", "24", "520100", "{\"type\":\"Feature\",\"properties\":{\"name\":\"贵阳市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[106.6,26.5],[106.8,26.5],[106.8,26.7],[106.6,26.7],[106.6,26.5]]]}}"},
            {"遵义市", "24", "520300", "{\"type\":\"Feature\",\"properties\":{\"name\":\"遵义市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[106.9,27.7],[107.1,27.7],[107.1,27.9],[106.9,27.9],[106.9,27.7]]]}}"},
            {"石家庄市", "3", "130100", "{\"type\":\"Feature\",\"properties\":{\"name\":\"石家庄市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[114.4,38.0],[114.6,38.0],[114.6,38.2],[114.4,38.2],[114.4,38.0]]]}}"},
            {"唐山市", "3", "130200", "{\"type\":\"Feature\",\"properties\":{\"name\":\"唐山市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[118.0,39.6],[118.2,39.6],[118.2,39.8],[118.0,39.8],[118.0,39.6]]]}}"},
            {"太原市", "4", "140100", "{\"type\":\"Feature\",\"properties\":{\"name\":\"太原市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[112.5,37.8],[112.7,37.8],[112.7,38.0],[112.5,38.0],[112.5,37.8]]]}}"},
            {"大同市", "4", "140200", "{\"type\":\"Feature\",\"properties\":{\"name\":\"大同市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[113.2,40.1],[113.4,40.1],[113.4,40.3],[113.2,40.3],[113.2,40.1]]]}}"},
            {"和平区", "2", "120101", "{\"type\":\"Feature\",\"properties\":{\"name\":\"和平区\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[117.2,39.1],[117.3,39.1],[117.3,39.2],[117.2,39.2],[117.2,39.1]]]}}"},
            {"河西区", "2", "120103", "{\"type\":\"Feature\",\"properties\":{\"name\":\"河西区\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[117.1,39.0],[117.2,39.0],[117.2,39.1],[117.1,39.1],[117.1,39.0]]]}}"},
            {"呼和浩特市", "5", "150100", "{\"type\":\"Feature\",\"properties\":{\"name\":\"呼和浩特市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[111.6,40.8],[111.8,40.8],[111.8,41.0],[111.6,41.0],[111.6,40.8]]]}}"},
            {"包头市", "5", "150200", "{\"type\":\"Feature\",\"properties\":{\"name\":\"包头市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[109.8,40.6],[110.0,40.6],[110.0,40.8],[109.8,40.8],[109.8,40.6]]]}}"},
            {"兰州市", "28", "620100", "{\"type\":\"Feature\",\"properties\":{\"name\":\"兰州市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[103.8,36.0],[104.0,36.0],[104.0,36.2],[103.8,36.2],[103.8,36.0]]]}}"},
            {"嘉峪关市", "28", "620200", "{\"type\":\"Feature\",\"properties\":{\"name\":\"嘉峪关市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[98.2,39.8],[98.4,39.8],[98.4,40.0],[98.2,40.0],[98.2,39.8]]]}}"},
            {"西宁市", "29", "630100", "{\"type\":\"Feature\",\"properties\":{\"name\":\"西宁市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[101.7,36.6],[101.9,36.6],[101.9,36.8],[101.7,36.8],[101.7,36.6]]]}}"},
            {"银川市", "30", "640100", "{\"type\":\"Feature\",\"properties\":{\"name\":\"银川市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[106.2,38.4],[106.4,38.4],[106.4,38.6],[106.2,38.6],[106.2,38.4]]]}}"},
            {"乌鲁木齐市", "31", "650100", "{\"type\":\"Feature\",\"properties\":{\"name\":\"乌鲁木齐市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[87.6,43.8],[87.8,43.8],[87.8,44.0],[87.6,44.0],[87.6,43.8]]]}}"},
            {"喀什市", "31", "653100", "{\"type\":\"Feature\",\"properties\":{\"name\":\"喀什市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[75.9,39.4],[76.1,39.4],[76.1,39.6],[75.9,39.6],[75.9,39.4]]]}}"},
            {"海口市", "21", "460100", "{\"type\":\"Feature\",\"properties\":{\"name\":\"海口市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[110.3,20.0],[110.5,20.0],[110.5,20.2],[110.3,20.2],[110.3,20.0]]]}}"},
            {"三亚市", "21", "460200", "{\"type\":\"Feature\",\"properties\":{\"name\":\"三亚市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[109.5,18.2],[109.7,18.2],[109.7,18.4],[109.5,18.4],[109.5,18.2]]]}}"},
            {"拉萨市", "26", "540100", "{\"type\":\"Feature\",\"properties\":{\"name\":\"拉萨市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[91.1,29.6],[91.3,29.6],[91.3,29.8],[91.1,29.8],[91.1,29.6]]]}}"},
            {"香港岛", "32", "810101", "{\"type\":\"Feature\",\"properties\":{\"name\":\"香港岛\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[114.1,22.25],[114.2,22.25],[114.2,22.35],[114.1,22.35],[114.1,22.25]]]}}"},
            {"澳门半岛", "33", "820101", "{\"type\":\"Feature\",\"properties\":{\"name\":\"澳门半岛\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[113.55,22.15],[113.58,22.15],[113.58,22.18],[113.55,22.18],[113.55,22.15]]]}}"},
            {"台北市", "34", "710101", "{\"type\":\"Feature\",\"properties\":{\"name\":\"台北市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[121.5,25.0],[121.6,25.0],[121.6,25.15],[121.5,25.15],[121.5,25.0]]]}}"},
            {"高雄市", "34", "710201", "{\"type\":\"Feature\",\"properties\":{\"name\":\"高雄市\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[120.3,22.6],[120.4,22.6],[120.4,22.75],[120.3,22.75],[120.3,22.6]]]}}"}
        };

        for (String[] c : cities) {
            City city = new City();
            city.setName(c[0]);
            city.setProvinceId(Long.parseLong(c[1]));
            city.setCode(c[2]);
            city.setGeojson(c[3]);
            cityRepository.save(city);
        }
    }

    private void ensureAdminUser() {
        String rawPassword = "admin123";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        userRepository.findByUsername("admin").ifPresentOrElse(admin -> {
            if (!passwordEncoder.matches(rawPassword, admin.getPassword())) {
                admin.setPassword(encodedPassword);
                userRepository.save(admin);
                System.out.println("Admin password re-encoded with BCrypt");
            }
        }, () -> {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(encodedPassword);
            userRepository.save(admin);
            System.out.println("Admin user created");
        });
    }
}
