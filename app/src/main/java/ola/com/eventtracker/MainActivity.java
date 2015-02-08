package ola.com.eventtracker;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import ola.com.eventtracker.event.EventAdapter;
import ola.com.eventtracker.event.EventHandler;
import ola.com.eventtracker.event.EventModel;

import static ola.com.eventtracker.utils.NetUtils.showToast;


public class MainActivity extends Activity implements SlidingUpPanelLayout.PanelSlideListener {

    private ListView mListView;
    private SlidingUpPanelLayout mSlidingUpPanelLayout;

    private View mTransparentHeaderView;
    private View mTransparentView;
    private View mSpaceView;

    private MapFragment mMapFragment;

    private GoogleMap mMap;


    EventHandler eventHandler;
    private Handler handler = new Handler();

    String calTimezone = TimeZone.getDefault().getDisplayName();
    Calendar start;
    Calendar end;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.list);
        mListView.setOverScrollMode(ListView.OVER_SCROLL_NEVER);

        mSlidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.slidingLayout);
        mSlidingUpPanelLayout.setEnableDragViewTouchEvents(true);

        int mapHeight = getResources().getDimensionPixelSize(R.dimen.map_height);
        mSlidingUpPanelLayout.setPanelHeight(mapHeight); // you can use different height here
        mSlidingUpPanelLayout.setScrollableView(mListView, mapHeight);

        mSlidingUpPanelLayout.setPanelSlideListener(this);

        // transparent view at the top of ListView
        mTransparentView = findViewById(R.id.transparentView);

        // init header view for ListView
        mTransparentHeaderView = LayoutInflater.from(this).inflate(R.layout.transparent_header_view, null, false);
        mSpaceView = mTransparentHeaderView.findViewById(R.id.space);

       /* ArrayList<String> testData = new ArrayList<String>(100);
        for (int i = 0; i < 100; i++) {
            testData.add("Item " + i);
        }

        mListView.setAdapter(new ArrayAdapter<String>(this, R.layout.simple_list_item, testData));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSlidingUpPanelLayout.collapsePane();
            }
        });*/
        final List<EventModel> values = new ArrayList<EventModel>();

        eventHandler = new EventHandler(getApplicationContext());

        start = Calendar.getInstance();
        start.set(Calendar.YEAR,2015);
        start.set(Calendar.MONTH,Calendar.JANUARY);
        start.set(Calendar.HOUR, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        end = Calendar.getInstance();
        end.set(Calendar.YEAR,2015);
        end.set(Calendar.MONTH,Calendar.DECEMBER);
        end.set(Calendar.HOUR, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);

        List<Map<String,String>> events = eventHandler.selectEvents(calTimezone, start, end);
        if(events==null)
            showToast(MainActivity.this,"No events found!");
        else {
            for(Map<String,String> event:events) {
                EventModel model = new EventModel(event);
                if(model.getLocation()!=null&&!model.getLocation().equals("")&&model.getLocation().contains(","))
                     values.add(model);
            }

            showToast(MainActivity.this,values.size() + " events found");

            EventAdapter adapter = new EventAdapter(getApplicationContext(),values);

            mListView.addHeaderView(mTransparentHeaderView);

            mListView.setAdapter(adapter);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // startNewActivity(getApplicationContext(),"com.olacabs.customer");
                   String[] eventData = values.get(0).getLocation().split(",");
                    LatLng latLng = new LatLng(Double.parseDouble(eventData[0].trim()),Double.parseDouble(eventData[1].trim()));
                    mMap.addMarker(new MarkerOptions()
                            .title(values.get(0).getTitle()).position(latLng)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_inverse))
                           );

                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            showDeal();
                            return false;
                        }
                    });

                    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                        @Override
                        public View getInfoWindow(Marker marker) {

                            return null;
                        }

                        @Override
                        public View getInfoContents(Marker marker) {
                            return null;
                        }
                    });
                    CameraUpdateFactory.newLatLngZoom(
                           latLng, 16);

                    mSlidingUpPanelLayout.collapsePane();
                }
            });
        }
        collapseMap();

        mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mapContainer, mMapFragment, "map");

        fragmentTransaction.commit();

      //  setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded(CameraUpdate location) {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = mMapFragment.getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setCompassEnabled(false);
                mMap.getUiSettings().setZoomControlsEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                CameraUpdate update = location;
                if (update != null) {
                    mMap.moveCamera(update);
                }
            }

            showRecommendation();
            showDeal();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // In case Google Play services has since become available.
        setUpMapIfNeeded(getLastKnownLocation());


    }

    public void startNewActivity(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
        /* We found the activity now start the activity */
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
        /* Bring user to the market or let them choose an app? */
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("market://details?id=" + packageName));
            context.startActivity(intent);
        }
    }

    private CameraUpdate getLastKnownLocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        String provider = lm.getBestProvider(criteria, true);
        if (provider == null) {
            return null;
        }
        Location loc = lm.getLastKnownLocation(provider);
        if (loc != null) {
            return CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()), 14.0f));
        }
        return null;
    }

    private void collapseMap() {
        mSpaceView.setVisibility(View.VISIBLE);
        mTransparentView.setVisibility(View.GONE);
    }

    private void expandMap() {
        mSpaceView.setVisibility(View.GONE);
        mTransparentView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPanelSlide(View view, float v) {
    }

    @Override
    public void onPanelCollapsed(View view) {
        expandMap();
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14f), 1000, null);
    }

    @Override
    public void onPanelExpanded(View view) {
        collapseMap();
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11f), 1000, null);
    }

    @Override
    public void onPanelAnchored(View view) {

    }

    public void showDeal() {

        Runnable playAudioSongRunnable = new Runnable() {


            public static final int NOTIFICATION_ID = 1;

            @Override
            public void run() {


                showToast(MainActivity.this,"Sample deal notification");
                generateNotification(MainActivity.this,"Get 30% discount on your next trip!","#‎ChaloNiklo‬, come join the ride with Ola");

            }

            private void generateNotification(Context context,String title, String message) {

                NotificationCompat.Builder builder =  new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_ola)
                        .setContentTitle(title)
                        .setSubText(message)
                        .setContentText(message)
                        .setStyle(new NotificationCompat.InboxStyle());

                // Add as notification
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(NOTIFICATION_ID, builder.build());
            }

            private  void removeNotification(Context context){
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancelAll();

            }
        };

        handler.post(playAudioSongRunnable);


    }

    public void showRecommendation() {

        Runnable playAudioSongRunnable = new Runnable() {


            public static final int NOTIFICATION_ID = 1;

            @Override
            public void run() {


                showToast(MainActivity.this,"Sample recommendation notification");
                generateNotification(MainActivity.this,"Hey we found someone, with whom you can share your cab!","This message is sent from push notifications.");

            }

            private void generateNotification(Context context,String title, String message) {

                NotificationCompat.Builder builder =  new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(title)
                        .setSubText(message)
                        .setContentText(message)
                        .setStyle(new NotificationCompat.InboxStyle());

                // Add as notification
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(NOTIFICATION_ID, builder.build());
            }

            private  void removeNotification(Context context){
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancelAll();

            }
        };

        handler.post(playAudioSongRunnable);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.ic_action_add_person) {
            showRecommendation();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
