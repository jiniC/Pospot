package dongkyul.pospot.view.main;

import io.realm.RealmList;
import io.realm.RealmObject;

// 포토마커 객체 배열 { 타이틀, lat, lon, [이미지 리스트], 대표이미지, 마커아이디? }

public class PhotoMarkerDB extends RealmObject {
//    @PrimaryKey
//    @Required
    private String title;
    private double lat;
    private double lon;
    private RealmList<byte[]> photoList;
    private int titleIndex = 5;

    @Override
    public String toString() {
        String photoByteArray="[";
        for(byte[] photoByte:photoList){ //photolist에서 photo의 byte array를 하나씩 가져옴
            photoByteArray+=photoByte; //photo 각각의 byte array를 붙임
            photoByteArray+=",";
        }
        photoByteArray+="]";

        return "PhotoMarkerActivity {" +
                "title='" + title + '\'' +
                ", lat='" + lat + '\'' +
                ", lon=" + lon + '\'' +
                ", photoList=" + photoByteArray +
                '}'; //photomarker에 속한 모든 photo들이 phtolist안에 bytearray로 들어있는 photo marker string 생성

    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public double getLat() {
        return lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }
    public void setLon(double lon) {
        this.lon = lon;
    }

    public RealmList<byte[]> getPhotoList() {
        return photoList;
    }
    public void setPhotoList(RealmList<byte[]> photoList) {
        this.photoList = photoList;
    }

    public int getTitleIndex() {
        return titleIndex;
    }
    public void setTitleIndex(int titleIndex) {
        this.titleIndex = titleIndex;
    }
}

