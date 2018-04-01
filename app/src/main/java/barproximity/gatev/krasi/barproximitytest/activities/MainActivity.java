package barproximity.gatev.krasi.barproximitytest.activities;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import barproximity.gatev.krasi.barproximitytest.BarProxiApplication;
import barproximity.gatev.krasi.barproximitytest.R;
import barproximity.gatev.krasi.barproximitytest.adapters.SectionsPagerAdapter;
import barproximity.gatev.krasi.barproximitytest.utils.LocationUtils;
import barproximity.gatev.krasi.barproximitytest.viewmodels.LocationsResultsViewModel;
import barproximity.gatev.krasi.barproximitytest.ws.data.Result;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Main activity. Initializes the List and Map fragments.
 */
public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final float DEFAULT_ZOOM = 15;
    private static final int REQUEST_PERMISSION = 104;

    @BindView(R.id.pager_view)
    ViewPager mViewPager;
    @Nullable
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tabs)
    TabLayout mTabLayout;
    @BindView(R.id.fab)
    FloatingActionButton mFab;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private GoogleMap mMap;
    private FusedLocationProviderClient mLocationClient;
    private boolean mHasPermission;
    private LocationsResultsViewModel mLocationsModel;

    private ViewPager.OnPageChangeListener pageChangeListener = new
            ViewPager.OnPageChangeListener() {
                @Override
                public void onPageSelected(int newPage) {
                    if (newPage == 1) {
                        mFab.show();
                    } else {
                        mFab.hide();
                    }
                }

                @Override
                public void onPageScrolled(int initialPage, float offset, int arg2) {

                }

                @Override
                public void onPageScrollStateChanged(int arg0) {

                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bind Views
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            mFab.hide();
        }

        //Init View Model
        mLocationsModel = ViewModelProviders.of(this).get(LocationsResultsViewModel.class);

        setSupportActionBar(mToolbar);
        mLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //Init Fragment Adapters and Tabs
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mViewPager.addOnPageChangeListener(pageChangeListener);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMap != null) {
                    getDeviceLocation();
                }
            }
        });


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    //This will be called from the map fragment when it is ready
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        initLocations();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mHasPermission = false;
        switch (requestCode) {
            case REQUEST_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mHasPermission = true;
                }
            }
        }
        initLocations();
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mHasPermission = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSION);
        }
    }

    private void initLocations() {
        if (mMap == null) {
            return;
        }
        if (BarProxiApplication.getUserLocation() == null) {
            getDeviceLocation();
        } else {
            updatePlaces();
        }
    }

    private void getDeviceLocation() {
        //make sure we have location permission
        getLocationPermission();
        try {
            if (mHasPermission) {
                Task locationResult = mLocationClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            BarProxiApplication.setUserLocation(new LatLng(task.getResult().getLatitude(),
                                    task.getResult().getLongitude()));
                            updatePlaces();
                        } else {
                            Log.d(TAG, "Could not find current location. Location was null.");
                            Log.e(TAG, "Exception: %s", task.getException());
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void updatePlaces() {
        mLocationsModel.fetchResults();
    }

    @Override
    public void onStart() {
        super.onStart();
        mLocationsModel.getCurrentResults().observe(this, mObserver);
    }

    @Override
    public void onStop() {
        super.onStop();
        mLocationsModel.getCurrentResults().removeObserver(mObserver);
    }

    private Observer<List<Result>> mObserver = new Observer<List<Result>>() {
        @Override
        public void onChanged(@Nullable List<Result> results) {
            intiMapFlags(results);
        }
    };

    private void intiMapFlags(List<Result> results) {
        if (mMap != null) {
            if (BarProxiApplication.getUserLocation() != null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BarProxiApplication.getUserLocation()
                        , DEFAULT_ZOOM));
                mMap.addMarker(new MarkerOptions().position(BarProxiApplication.getUserLocation()).title("You are here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            }
            LatLng currentLocation;
            for (Result result : results) {
                currentLocation = new LatLng(result.getGeometry().getLocation().getLat(),
                        result.getGeometry().getLocation().getLng());
                mMap.addMarker(new MarkerOptions().position(currentLocation).title(result.getName() + " " + LocationUtils.distanceToLocation(BarProxiApplication.getUserLocation(), currentLocation) + "m"));
            }
        }
    }
}
