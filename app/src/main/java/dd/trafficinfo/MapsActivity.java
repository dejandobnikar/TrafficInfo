package dd.trafficinfo;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import dd.trafficinfo.core.DataEvent;
import dd.trafficinfo.core.Dogodek;
import dd.trafficinfo.directions.Directions;
import dd.trafficinfo.directions.Route;
import de.greenrobot.event.EventBus;

public class MapsActivity extends FragmentActivity implements View.OnClickListener {

    private static final String TAG_LIST = "info_listfragment";
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LatLng latLngSlo = new LatLng(46.2328254,14.8761473);
    private LatLng latLngNE = new LatLng(45.4753096,15.7495604);
    private LatLng latLngSW = new LatLng(45.516724,14.3686138);

    private LatLngBounds bounds = new LatLngBounds(latLngNE, latLngSW);
    private Button btnShowList;
    private ImageButton btnSearch;
    private EditText editFrom;
    private EditText editTo;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        editFrom = (EditText) findViewById(R.id.editFrom);
        editTo = (EditText) findViewById(R.id.editTo);
        btnSearch = (ImageButton) findViewById(R.id.btnSearchDirections);
        btnShowList =(Button) findViewById(R.id.btnShowList);
        progressBar = (ProgressBar ) findViewById(R.id.progressBar);

        btnShowList.setOnClickListener(this);
        btnSearch.setOnClickListener(this);

        TrafficApplication app = (TrafficApplication) getApplication();
        app.getTrafficInfoComponent().loadTrafficData();
        app.getLocationComponent().findLocation();


        Fragment mapFragment = getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.setRetainInstance(true);
    }



    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();

        setUpGui();

        displayData();

        EventBus bus = EventBus.getDefault();
        bus.register(this);
    }



    @Override
    protected void onPause() {
        super.onPause();
        EventBus bus = EventBus.getDefault();
        bus.unregister(this);

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        TrafficApplication app = (TrafficApplication) getApplication();
        app.getLocationComponent().clean();
    }



    private void setUpGui() {

        TrafficApplication app = (TrafficApplication) getApplication();
        if (app.getDirectionsComponent().isActive() ) {

            progressBar.setVisibility(View.VISIBLE);
            btnSearch.setVisibility(View.INVISIBLE);
        }
        else {
            progressBar.setVisibility(View.INVISIBLE);
            btnSearch.setVisibility(View.VISIBLE);
        }
    }



    public void onEventMainThread(DataEvent event) {

        if (event.getDogodki() == null ) {
            Toast.makeText(this, R.string.msg_data_load_failed, Toast.LENGTH_SHORT).show();
            return;
        }

        displayData(event.getDogodki());
    }



    public void onEventMainThread(Directions dir) {

        progressBar.setVisibility(View.INVISIBLE);
        btnSearch.setVisibility(View.VISIBLE);

        if (mMap == null) return;

        if (dir.getRoutes() == null ) {
            mMap.clear();
            displayData();
            return;
        }

        if (dir.getRoutes().size() == 0) {
            mMap.clear();
            displayData();
            return;
        }

        mMap.clear();
        for (Route r : dir.getRoutes()) {
            drawLines(r.getListPoints());
        }

        displayData();

    }



    private void drawLines(List<LatLng> list) {

        if (mMap != null) {

            PolylineOptions opts = new PolylineOptions();
            opts.color(Color.RED);
            mMap.addPolyline(opts.addAll(list));

        }
    }



    private void displayData() {
        TrafficApplication app = (TrafficApplication) getApplication();
        displayData(app.getTrafficInfoComponent().getTrafficData());
    }



    private void displayData(List<Dogodek> dogodki) {

        if (dogodki == null ) return;
        if (mMap == null) return;

        for (Dogodek d : dogodki) {

           mMap.addMarker(new MarkerOptions().position(new LatLng(d.getY_wgs(), d.getX_wgs())).title(d.getKategorija()));

        }
    }



    private void showInfoListFragment() {

        InfoListFragment f = (InfoListFragment) getSupportFragmentManager().findFragmentByTag(TAG_LIST);
        if (f == null) f = new InfoListFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, f, TAG_LIST).addToBackStack(null).commit();
    }



    private void searchForDirections() {
        String from = editFrom.getText().toString();
        String to = editTo.getText().toString();

        if (from.length() == 0 || to.length() == 0) {
            Toast.makeText(this, R.string.add_data, Toast.LENGTH_SHORT).show();
            return;
        }

        TrafficApplication app = (TrafficApplication) getApplication();

        progressBar.setVisibility(View.VISIBLE);
        btnSearch.setVisibility(View.INVISIBLE);

        app.getDirectionsComponent().getDirections(from, to);
    }



    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLngSlo, 7);

        mMap.moveCamera(update);

        displayData();
    }

    @Override
    public void onClick(View v) {
        if (v.equals(btnShowList)) {
            showInfoListFragment();
        }

        else if (v.equals(btnSearch)) {
            searchForDirections();
        }
    }
}
