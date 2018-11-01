package dongkyul.pospot.view.main;

import java.util.List;

import lombok.Data;

public class ResponseContainer {
    Response response;
    public class Response {
        Body body;
        Header header;

        public class Header {
            int resultCode;
            String resultMsg;
        }
        public class Body {

            Items items;

            public class Items {
                List<Item> item;

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
                    public int sigungucode;
                    public String tel;
                }
            }
        }
    }
}
