package dd.trafficinfo.directions;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.maps.GeoPoint;

import java.util.List;

/**
 * Created by DejanD on 18.9.2014.
 */
public class Route {

    String copyright;
    OverviewPolyline overview_polyline;
    List<LatLng> listPoints;

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public OverviewPolyline getOverview_polyline() {
        return overview_polyline;
    }

    public void setOverview_polyline(OverviewPolyline overview_polyline) {
        this.overview_polyline = overview_polyline;
    }


    public void calculateGeoPoints() {

        if (overview_polyline == null) return;

        if (overview_polyline.getPoints() == null) return;

        listPoints = Directions.decodePoly(overview_polyline.getPoints());

    }

    public List<LatLng> getListPoints() {
        return listPoints;
    }

    public void setListPoints(List<LatLng> listPoints) {
        this.listPoints = listPoints;
    }
}
