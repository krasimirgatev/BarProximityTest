package barproximity.gatev.krasi.barproximitytest;

import android.app.Application;
import android.content.Context;
import android.location.LocationManager;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import barproximity.gatev.krasi.barproximitytest.utils.LocationUtils;
import barproximity.gatev.krasi.barproximitytest.ws.ServicesInterface;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Application context for storing current user location globally
 */
public class BarProxiApplication extends Application {

    private static final String TAG = BarProxiApplication.class.getSimpleName();
    private static LatLng userLocation = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static LatLng getUserLocation() {
        return userLocation;
    }

    public static void setUserLocation(LatLng userLocation) {
        Log.d(TAG, "Setting User Location to: " + LocationUtils.getCoordinatesStringFromLatLng(userLocation));
        BarProxiApplication.userLocation = userLocation;
    }
}