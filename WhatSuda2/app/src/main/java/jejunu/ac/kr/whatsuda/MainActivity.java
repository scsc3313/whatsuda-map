package jejunu.ac.kr.whatsuda;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private GoogleMap map;
    private GoogleApiClient googleApiClient;
    private Location lastLocation;

    private MapFragment mapFragment;

    private Map<Integer, LandMark> landMarkMap;

    private ListView listView;
    private CustomAdapter adapter;
    private LocationRequest locationRequest;

    private static final String NOTIFICATION_MSG = "NOTIFICATION MSG";

    public static Intent makeNotificationIntent(Context context, String msg) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(NOTIFICATION_MSG, msg);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");

        landMarkMap = new HashMap<>();

        landMarkMap.put(1, new LandMark(new LatLng(33.4875853, 126.8058729), "비자림", 2040579));
        landMarkMap.put(2, new LandMark(new LatLng(33.4580731, 126.9337452), "성산일출봉", 7022724));
        landMarkMap.put(3, new LandMark(new LatLng(33.432798, 126.9257214), "한화 아쿠아플라넷", 2413119));
        landMarkMap.put(4, new LandMark(new LatLng(33.3225155, 126.8396793), "제주 민속촌박물관", 439899));

        landMarkMap.put(5, new LandMark(new LatLng(33.5231275, 126.5856429), "삼양 선사유적지", 145425));
        landMarkMap.put(6, new LandMark(new LatLng(33.5415984, 126.6407762), "제주 항일기념관", 460041));
        landMarkMap.put(7, new LandMark(new LatLng(33.433172, 126.6723419), "미니미니랜드", 207247));
        landMarkMap.put(8, new LandMark(new LatLng(33.4369715, 126.6259744), "절물자연휴양림", 2306373));
        landMarkMap.put(9, new LandMark(new LatLng(33.4445527, 126.5470649), "별빛누리공원", 304356));
        landMarkMap.put(10, new LandMark(new LatLng(33.4536071, 126.4874461), "도립미술관", 229522));
        landMarkMap.put(11, new LandMark(new LatLng(33.4523551, 126.4056057), "항몽유적지", 365434));

        landMarkMap.put(12, new LandMark(new LatLng(33.3895265, 126.2370672), "한림공원", 767381));

        landMarkMap.put(13, new LandMark(new LatLng(33.2502734, 126.2761716), "제주 추사관", 284676));
        landMarkMap.put(14, new LandMark(new LatLng(33.2536825, 126.3200347), "제주 조각공원", 62433));
        landMarkMap.put(15, new LandMark(new LatLng(33.2438005, 126.4135443), "퍼시픽랜드", 536790));
        landMarkMap.put(16, new LandMark(new LatLng(33.2526652, 126.4124578), "여미지 식물원", 806631));
        landMarkMap.put(17, new LandMark(new LatLng(33.244826, 126.5490414), "기당미술관", 38171));
        landMarkMap.put(18, new LandMark(new LatLng(33.2458539, 126.5627624), "이중섭미술관", 783154));
        landMarkMap.put(19, new LandMark(new LatLng(33.2448566, 126.5697145), "정방폭포", 3214280));


        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listview);

        // initialize GoogleMaps
        initGMaps();

        // create GoogleApiClient
        createGoogleApi();

        getSupportActionBar().setTitle("경로 추천");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady()");
        map = googleMap;
        map.setOnMapClickListener(this);
        map.setOnMarkerClickListener(this);
        if (map != null) {
            float zoom = 10f;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(33.2881107, 126.4661638), zoom);
            map.animateCamera(cameraUpdate);
        }
        if (checkPermission())
            map.setMyLocationEnabled(true);

        addPath(1, 4, 0xFF0080FF);
        addPath(5, 11, 0xFF0080FF);
        addPath(13, 19, 0xFF0080FF);
    }

    private void addPath(int start, int end, int color) {

        PolylineOptions options = new PolylineOptions();
        BitmapDescriptor descriptor;
        for (int i = start; i <= end; i++) {
            options.add(landMarkMap.get(i).getLatLng());
            if (i == start) {
                descriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_map_start);
            } else if (i == end) {
                descriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_map_end);
            } else {
                descriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);
            }
            markerForGeofence(landMarkMap.get(i), start, descriptor);
            drawGeofence(landMarkMap.get(i).getLatLng());
        }

        options.color(color);
        map.addPolyline(options);
    }

    // Create GoogleApiClient instance
    private void createGoogleApi() {
        Log.d(TAG, "createGoogleApi()");
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    private final int REQ_PERMISSION = 999;

    private boolean checkPermission() {
        Log.d(TAG, "checkPermission()");
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    private void askPermission() {
        Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQ_PERMISSION
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult()");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastKnownLocation();
                } else {
                    permissionsDenied();
                }
                break;
            }
        }
    }

    private void permissionsDenied() {
        Log.w(TAG, "permissionsDenied()");
        Toast.makeText(MainActivity.this, "권한을 승낙하셔야 합니다.", Toast.LENGTH_SHORT).show();
        // TODO close app and warn user
    }

    private void initGMaps() {
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapClick(LatLng latLng) {
        Log.d(TAG, "onMapClick(" + latLng + ")");
        listView.setVisibility(View.GONE);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        int start, end;
        switch ((int) marker.getTag()) {
            case 1:
                start = 1;
                end = 4;
                break;
            case 5:
                start = 5;
                end = 11;
                break;
            case 13:
                start = 13;
                end = 19;
                break;
            default:
                start = 0;
                end = 0;
                break;
        }
        adapter = new CustomAdapter(this, landMarkMap, start, end);
        listView.setVisibility(View.VISIBLE);
        listView.setAdapter(adapter);
        return false;
    }


    // This number in extremely low, and should be used only for debug
    private final int UPDATE_INTERVAL = 1000;
    private final int FASTEST_INTERVAL = 900;
    // Start location Updates

    private void startLocationUpdates() {
        Log.i(TAG, "startLocationUpdates()");
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        if (checkPermission())
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged [" + location + "]");
        lastLocation = location;

        for (Map.Entry<Integer, LandMark> landMark : landMarkMap.entrySet()) {
            Location landMarkLocation = new Location(landMark.getValue().getName());
            landMarkLocation.setLatitude(landMark.getValue().getLatLng().latitude);
            landMarkLocation.setLongitude(landMark.getValue().getLatLng().longitude);

            if (GEOFENCE_RADIUS > landMarkLocation.distanceTo(lastLocation)) {
                //서버에 이 사용자가 있다는 것을 전송!
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected()");
        getLastKnownLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.w(TAG, "onConnectionSuspended()");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.w(TAG, "onConnectionFailed()");
    }

    private void getLastKnownLocation() {
        Log.d(TAG, "getLastKnownLocation()");
        if (checkPermission()) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation != null) {
                Log.i(TAG, "LasKnown location. " +
                        "Long: " + lastLocation.getLongitude() +
                        " | Lat: " + lastLocation.getLatitude());
                startLocationUpdates();
            } else {
                Log.w(TAG, "No location retrieved yet");
                startLocationUpdates();
            }
        } else askPermission();
    }

    private void markerForGeofence(LandMark landMark, int tag, BitmapDescriptor descriptor) {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(landMark.getLatLng())
                .icon(descriptor)
                .title(landMark.getName());
        if (map != null) {
            map.addMarker(markerOptions).setTag(tag);
            drawGeofence(markerOptions.getPosition());
        }
    }

    private static final float GEOFENCE_RADIUS = 500.0f; // in meters

    private void drawGeofence(LatLng latLng) {
        Log.d(TAG, "drawGeofence()");
        CircleOptions circleOptions = new CircleOptions()
                .center(new LatLng(latLng.latitude, latLng.longitude))
                .strokeColor(Color.argb(50, 70, 70, 70))
                .fillColor(Color.argb(100, 150, 150, 150))
                .radius(GEOFENCE_RADIUS);
        map.addCircle(circleOptions);
    }
}
