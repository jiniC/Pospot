package dongkyul.pospot.view.main;

import java.util.List;

import lombok.Data;

public class ResponseContainer {
    Items items;

    public class Items {
        List<Item> itemList;

        @Data
        public class Item {
            public int contenttypeid;
            public double mapx;
            public double mapy;
            public String title;



            public String addr1;
            public String addr2;
            public int areacode;

            public int booktour;

            public String cat1;
            public String cat2;
            public String cat3;
            public int contentid;
            public int createdtime;
            public int dist;
            public String firstimage;
            public String firstimage2;
            public int mlevel;
            public int modifiedtime;
            public int readcount;
            public String tel;





//"addr1":"서울특별시 중구 명동8가길 32",
//        "addr2": "(충무로2가)",
//        "areacode": 1,
//        "cat1": "A04",
//        "cat2": "A0401",
//        "cat3": "A04010600",
//        "contentid": 984586,
//        "contenttypeid": 38,
//        "createdtime": 20100324124214,
//        "dist": 884,
//        "firstimage": "http:\/\/tong.visitkorea.or.kr\/cms\/resource\/36\/1009936_image2_1.jpg",
//        "firstimage2": "http:\/\/tong.visitkorea.or.kr\/cms\/resource\/36\/1009936_image3_1.jpg",
//        "mapx": 126.9868259553,
//        "mapy": 37.5616910698,
//        "mlevel": 6,
//        "modifiedtime": 20180302170438,
//        "readcount": 11106,
//        "sigungucode": 24,
//        "tel": "02-753-6372",
//        "title": "가나 안경원 (명
        }
    }
}
