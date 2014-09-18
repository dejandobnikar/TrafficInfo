package dd.trafficinfo.core;

import java.util.List;

/**
 * Created by DejanD on 18.9.2014.
 */
public class DataEvent {

    private List<Dogodek> dogodki;

    public DataEvent(List<Dogodek> listDogodki) {
        this.dogodki = listDogodki;
    }


    public List<Dogodek> getDogodki() {
        return dogodki;
    }

    public void setDogodki(List<Dogodek> dogodki) {
        this.dogodki = dogodki;
    }
}
