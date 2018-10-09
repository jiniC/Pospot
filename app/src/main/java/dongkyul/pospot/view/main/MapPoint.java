package dongkyul.pospot.view.main;

public class MapPoint {
    private String Name;
    private int type;
    private double latitude;
    private double longitude;

    public MapPoint() {
        super();
    }

    public MapPoint(String Name, int type, double latitude, double longitude) {
        this.Name = Name;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
