package com.example.bookitdriver.ui.fragmen;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.bookitdriver.R;
import com.firebase.geofire.GeoFire;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class HomeFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private Location lostLocation;
    private LocationRequest locationRequest;
    private LocationCallback mLocationCallback;
    private FusedLocationProviderClient mFusedLocationClient;
   private ToggleButton toggle;

   private static  final int MY_PERMITION_REQUEST_CODE=7000;
   private static  final int PLAY_SERVICE_RES_REQUEST=7001;
   private static  final int UPDATE_INTERVAL=5000;
   private static  final int FATEST_INTERVAL=3000;
   private static  final int DISPLACEMENT=10;
   private DatabaseReference driverReference;
   private GeoFire geoFire;



    private String  TAG="error";
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //init view
        toggle=root.findViewById(R.id.location_switch);

        //toggle scaleAnimation
        scaleAnimation();
        return root;

    }
    void scaleAnimation()
    {

        final ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
        scaleAnimation.setDuration(500);
        BounceInterpolator bounceInterpolator = new BounceInterpolator();
        scaleAnimation.setInterpolator(bounceInterpolator);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                //animation
                compoundButton.startAnimation(scaleAnimation);
            }



        });

    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,
                locationRequest,this);


}

    @Override
    public void onConnectionSuspended(int i) {

    }




    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.getErrorCode() == ConnectionResult.API_UNAVAILABLE) {
            // The Wearable API is unavailable
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        lostLocation=location;
        LatLng latLng= new LatLng(location.getLatitude(),location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

        String userId = FirebaseAuth.getInstance().getUid();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("DriversAvailable");
//        GeoFire geoFire= new GeoFire(reference);
//        geoFire.setLocation(userId,new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {
//            @Override
//            public void onComplete(String key, DatabaseError error) {
//                if (error!=null)
//                {
//
//                    Log.e("error","Can't go Active");
//                }
//
//                Log.e("error","You are Active");
//            }
//        });


}

    @Override
    public void onStop() {
        super.onStop();

//
//        String userId = FirebaseAuth.getInstance().getUid();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("DriversAvailable");
//        GeoFire geoFire= new GeoFire(reference);
//        if (userId!=null)
//        geoFire.removeLocation(userId);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        buildGoogleApiClient();


        boolean success = googleMap.setMapStyle(new MapStyleOptions(getResources()
                .getString(R.string.style_json)));

        if (!success) {
            Log.e(TAG, "Style parsing failed.");
        }
        // Position the map's camera

        mMap.setMyLocationEnabled(true);

        // Add a marker in Sydney and move the camera

    }




    protected synchronized void buildGoogleApiClient()
    {
        googleApiClient= new GoogleApiClient.Builder(getActivity()).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }
}