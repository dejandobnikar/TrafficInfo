package dd.trafficinfo;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dd.trafficinfo.core.DataEvent;
import dd.trafficinfo.core.Dogodek;
import de.greenrobot.event.EventBus;

/**
 * Created by DejanD on 18.9.2014.
 */
public class InfoListFragment extends ListFragment {


    private InfoArrayAdapter adapter;
    private TrafficApplication app;
    private Location lastKnownLocation;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_info, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        app = (TrafficApplication)getActivity().getApplication();
        adapter = new InfoArrayAdapter(app, app.getTrafficInfoComponent().getTrafficData());
        setListAdapter(adapter);
    }


    @Override
    public void onResume() {
        super.onResume();

        lastKnownLocation = app.getLocationComponent().getLastKnownLocation();

        displayData();

        EventBus bus = EventBus.getDefault();
        bus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus bus = EventBus.getDefault();
        bus.unregister(this);

    }



    public void onEventMainThread(Location location) {

        // re-sort on location found...
        lastKnownLocation = location;
    }



    public void onEventMainThread(DataEvent event) {

        if (event.getDogodki() == null ) {
            return;
        }

        displayData(event.getDogodki());
    }



    private void displayData() {

        displayData(app.getTrafficInfoComponent().getTrafficData());
    }



    private void displayData(List<Dogodek> dogodki) {

        if (dogodki == null ) return;


        adapter.setData(getSortedData(dogodki));

    }



    private List<Dogodek> getSortedData(List<Dogodek> list) {

        if (lastKnownLocation == null ) return list;

        ArrayList<Dogodek> ret = new ArrayList<Dogodek>(list);
        Collections.sort(ret, new DistanceComparator(lastKnownLocation));

        return ret;
    }



    static class InfoArrayAdapter extends BaseAdapter {

        private List<Dogodek> listDogodki;
        private TrafficApplication app;
        private LayoutInflater inflater;

        public InfoArrayAdapter(TrafficApplication app, List<Dogodek> listDogodki ) {

            this.listDogodki = listDogodki;
            this.app = app;
            this.inflater = (LayoutInflater) app.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            if (listDogodki == null) return 0;

            return  listDogodki.size();
        }


        public void setData(List<Dogodek> list) {
            this.listDogodki = list;
            notifyDataSetChanged();
        }

        @Override
        public Dogodek getItem(int position) {
            return listDogodki.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Dogodek dogodek = getItem(position);
            Location location = app.getLocationComponent().getLastKnownLocation();

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_event, parent, false);

                holder = new ViewHolder();
                holder.iv = (NetworkImageView) convertView.findViewById(R.id.nivIcon);
                holder.tvRoad = (TextView) convertView.findViewById(R.id.tvRoad);
                holder.tvCause = (TextView) convertView.findViewById(R.id.tvCause);
                holder.tvDistance = (TextView) convertView.findViewById(R.id.tvDistance);

                convertView.setTag(holder);
            }


            holder = (ViewHolder) convertView.getTag();
            holder.tvRoad.setText(dogodek.getKategorija());
            holder.tvCause.setText(dogodek.getVzrok());
            holder.iv.setImageUrl(dogodek.getIcon(), app.getDownloadManagerComponent().getImageLoader());

            if (location != null) {
                float [] dist = new float[1];

                Location.distanceBetween(
                        location.getLatitude(),
                        location.getLongitude(),
                        dogodek.getY_wgs(),
                        dogodek.getX_wgs(),
                        dist
                );

                float distanceMeters = dist[0];

                if (distanceMeters != 0) {
                    holder.tvDistance.setText(String.format("%.1f km", distanceMeters/1000f) );
                }
            }

            return convertView;
        }



        public class ViewHolder{
            public TextView tvRoad;
            public TextView tvCause;
            public TextView tvDistance;
            public NetworkImageView iv;
        }
    }



    public class DistanceComparator implements Comparator<Dogodek> {

        private Location location;

        public DistanceComparator(Location loc) {
            location = loc;
        }


        @Override
        public int compare(Dogodek d1, Dogodek d2) {

            if (location == null ) return 0;

            float [] dist = new float[1];

            Location.distanceBetween(
                    location.getLatitude(),
                    location.getLongitude(),
                    d1.getY_wgs(),
                    d1.getX_wgs(),
                    dist
            );

            float distanceD1 = dist[0];


            Location.distanceBetween(
                    location.getLatitude(),
                    location.getLongitude(),
                    d2.getY_wgs(),
                    d2.getX_wgs(),
                    dist
            );


            float distanceD2 = dist[0];

            if (distanceD1 < distanceD2) return -1;
            else if (distanceD1 > distanceD2) return 1;
            else return 0;


        }
    }
}
