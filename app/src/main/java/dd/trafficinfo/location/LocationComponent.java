package dd.trafficinfo.location;

import android.location.Location;

import dd.trafficinfo.TrafficApplication;
import de.greenrobot.event.EventBus;

/**
 * Created by DejanD on 18.9.2014.
 */
public class LocationComponent {

    private TrafficApplication app;
    private MyLocation myLocation;
    private MyLocation.LocationResult locationResult;
    private Location lastKnownLocation;

    public LocationComponent(TrafficApplication app) {

        this.app = app;
    }


    public Location getLastKnownLocation() {
        return lastKnownLocation;
    }


    public void findLocation() {

        if (myLocation == null) {
            myLocation = new MyLocation();
        }


        if (locationResult == null) {
            locationResult = new MyLocation.LocationResult() {
                @Override
                public void gotLocation(Location location) {
                    lastKnownLocation = location;
                    EventBus bus = EventBus.getDefault();
                    bus.post(location);
                }
            };
        }

        myLocation.getLocation(app, locationResult);

    }


    public void clean() {
        if (myLocation != null) {
            myLocation.clean();
        }
    }

}
