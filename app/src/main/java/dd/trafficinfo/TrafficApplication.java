package dd.trafficinfo;

import android.app.Application;

import dd.trafficinfo.core.DownloadManagerComponent;
import dd.trafficinfo.core.TrafficInfoComponent;
import dd.trafficinfo.directions.DirectionsComponent;
import dd.trafficinfo.location.LocationComponent;

/**
 * Created by DejanD on 18.9.2014.
 */
public class TrafficApplication extends Application {

    private TrafficInfoComponent ti;
    private DownloadManagerComponent dmc;
    private LocationComponent locationComponent;
    private DirectionsComponent dirComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        ti = new TrafficInfoComponent(this);
        dmc = new DownloadManagerComponent(this);
        locationComponent = new LocationComponent(this);
        dirComponent = new DirectionsComponent(this);
    }


    public DirectionsComponent getDirectionsComponent() {
        return  dirComponent;
    }

    public LocationComponent getLocationComponent() {
        return locationComponent;
    }


    public TrafficInfoComponent getTrafficInfoComponent() {
        return ti;
    }

    public DownloadManagerComponent getDownloadManagerComponent() {
        return dmc;
    }
}