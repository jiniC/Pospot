package dongkyul.pospot.view.main;

import android.graphics.Bitmap;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

// 포토마커 객체 배열 { 타이틀, lat, lon, 키값은 마커아이디?, 대표이미지, [이미지 리스트] }

public class PhotoMarkerDB extends RealmObject {
//    @PrimaryKey
    private String title;
    private double lat;
    private double lon;
//    private RealmList<byte[]> photoList;

    @Override
    public String toString() {
        return "PhotoMarker {" +
                "title='" + title + '\'' +
                ", lat='" + lat + '\'' +
                ", lon=" + lon +
                '}';
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
//
//    public RealmList<byte[]>getPhotoList() {
//        return photoList;
//    }
//    public void setPhotoList(RealmList<byte[]> photoList) {
//        this.photoList = photoList;
//    }
}

