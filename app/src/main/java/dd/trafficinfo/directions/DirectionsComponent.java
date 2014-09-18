package dd.trafficinfo.directions;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import dd.trafficinfo.core.BaseObject;
import dd.trafficinfo.core.DataEvent;
import dd.trafficinfo.core.Dogodki;
import de.greenrobot.event.EventBus;

/**
 * Created by DejanD on 18.9.2014.
 */
public class DirectionsComponent {



    private static final int MSG_DATA_OK = 1;
    private static final int MSG_DATA_FAILED = 2;

    private static MyUpdateHandler mHandler;

    private boolean isActive;

    //http://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&key=API_KEY


    public DirectionsComponent() {

        mHandler = new MyUpdateHandler(this);
    }


    public boolean isActive() {
        return isActive;
    }

    public void getDirections(final String from, final String to) {
/*
        ConnectivityManager cm = (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();


        if (!isConnected) {
            mHandler.obtainMessage(MSG_DATA_FAILED).sendToTarget();
            return;
        }
*/

        isActive = true;

        new Thread(new Runnable() {
            @Override
            public void run() {

                InputStream is = null;

                try {

                    //String URL_DATA = "http://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&key=" + "AIzaSyBRAcMdiPoYloA6FELwkkufZhglcQXYu2Y";

                    String URL_DATA = "https://maps.googleapis.com/maps/api/directions/json?origin=Ljubljana"
                        +from
                        +"&destination="
                        +to
                        +"&sensor=false";

                    URL url = new URL(URL_DATA);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("GET");
                    conn.connect();

                    is = conn.getInputStream();


                    ObjectMapper mapper = new ObjectMapper();
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                    Directions obj = mapper.readValue(is, Directions.class);

                    if (obj != null) {

                        if (obj.getRoutes() != null) {
                            for (Route r : obj.getRoutes()) {
                                if (r.getOverview_polyline() != null) {
                                    r.calculateGeoPoints();
                                }
                            }
                        }



                        mHandler.obtainMessage(MSG_DATA_OK,obj).sendToTarget();
                    }
                    else {
                        mHandler.obtainMessage(MSG_DATA_FAILED).sendToTarget();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    mHandler.obtainMessage(MSG_DATA_FAILED).sendToTarget();
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }


            }
        }).start();

    }



    private void onDataUpdateFailed() {

        isActive = false;

        EventBus bus = EventBus.getDefault();
        bus.post(new Directions());
    }

    private void onDataUpdated(Directions dir) {

        isActive = false;

        if (dir == null) return;
        if (dir.getRoutes() == null || dir.getRoutes().size() == 0) return;

        EventBus bus = EventBus.getDefault();
        bus.post(dir);

    }


    static class MyUpdateHandler extends Handler {
        private final WeakReference<DirectionsComponent> mTarget;
        public MyUpdateHandler(DirectionsComponent target) {     // V konstruktorju objekt, do katerega želimo imeti referenco
            mTarget = new WeakReference<DirectionsComponent>(target);
        }

        @Override
        public void handleMessage(Message msg) {

            DirectionsComponent target = mTarget.get();

            if (target == null) return;

            if (msg.what == MSG_DATA_OK) {
                Directions dir = (Directions) msg.obj;
                target.onDataUpdated(dir);
            }

            else if (msg.what == MSG_DATA_FAILED) {
                target.onDataUpdateFailed();
            }



        }
    }
}
