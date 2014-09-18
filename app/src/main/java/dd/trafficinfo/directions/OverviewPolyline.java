package dd.trafficinfo.directions;

import com.google.android.maps.GeoPoint;

import java.util.List;

/**
 * Created by DejanD on 18.9.2014.
 */
public class OverviewPolyline {

    String points;



    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;

       // listPoints = Directions.decodePoly(points);
    }
}
