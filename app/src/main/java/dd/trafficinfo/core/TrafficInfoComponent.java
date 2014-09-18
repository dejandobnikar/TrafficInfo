package dd.trafficinfo.core;

import com.fasterxml.jackson.databind.ObjectMapper;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.lang.ref.WeakReference;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;

import dd.trafficinfo.TrafficApplication;
import de.greenrobot.event.EventBus;

/**
 * Created by DejanD on 18.9.2014.
 */
public class TrafficInfoComponent {

    private static final int MSG_DATA_OK = 1;
    private static final int MSG_DATA_FAILED = 2;

    private static final String TAG = "TrafficInfoComponent";

    private static final String URL_DATA = "http://opendata.si/promet/events/";

    private static final long DATA_RELOAD_TIME = 1000 * 60 * 15;

    private static MyUpdateHandler mHandler;
    private TrafficApplication app;
    private List<Dogodek> listDogodki;
    private long dataTimestamp;

    public TrafficInfoComponent(TrafficApplication app) {

        this.app = app;
        mHandler = new MyUpdateHandler(this);
    }



    public List<Dogodek> getTrafficData() {
        return listDogodki;
    }



    public void loadTrafficData() {

        if (listDogodki != null && listDogodki.size() > 0) {

            // reload every 15 minutes
            if (System.currentTimeMillis()-dataTimestamp < DATA_RELOAD_TIME) return;
        }

        ConnectivityManager cm = (ConnectivityManager)app.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();


        if (!isConnected) {
            mHandler.obtainMessage(MSG_DATA_FAILED).sendToTarget();
            return;
        }


        new Thread(new Runnable() {
            @Override
            public void run() {


                InputStream is = null;

                try {

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

                    BaseObject obj = mapper.readValue(is, BaseObject.class);

                    if (obj != null) {
                        mHandler.obtainMessage(MSG_DATA_OK,obj).sendToTarget();
                    }
                    else {
                        mHandler.obtainMessage(MSG_DATA_FAILED).sendToTarget();
                    }

                }
                catch (Exception e) {
                    e.printStackTrace();
                    mHandler.obtainMessage(MSG_DATA_FAILED).sendToTarget();
                }
                finally {
                    if (is !=null) {
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
        EventBus bus = EventBus.getDefault();
        bus.post(new DataEvent(null));
    }



    private void onDataUpdated(BaseObject obj) {

        Dogodki d = obj.getDogodki();

        if (d == null ) return;

        listDogodki = d.getDogodek();

        if (listDogodki != null && listDogodki.size() > 0) {
            dataTimestamp = System.currentTimeMillis();
        }

        EventBus bus = EventBus.getDefault();
        bus.post(new DataEvent(listDogodki));

    }



    static class MyUpdateHandler extends Handler {
        private final WeakReference<TrafficInfoComponent> mTarget;
        public MyUpdateHandler(TrafficInfoComponent target) {     // V konstruktorju objekt, do katerega želimo imeti referenco
            mTarget = new WeakReference<TrafficInfoComponent>(target);
        }

        @Override
        public void handleMessage(Message msg) {

            TrafficInfoComponent target = mTarget.get();

            if (target == null) return;

            if (msg.what == MSG_DATA_OK) {
               BaseObject obj = (BaseObject) msg.obj;
               target.onDataUpdated(obj);
            }

            else if (msg.what == MSG_DATA_FAILED) {
                target.onDataUpdateFailed();
            }
        }
    }

}
