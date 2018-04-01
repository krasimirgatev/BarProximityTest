package barproximity.gatev.krasi.barproximitytest.utils;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import java.util.Locale;

/**
 * Miscellaneous utility classes
 */
public class LocationUtils {

    //Convert LatLng to coma separated string
    public static String getCoordinatesStringFromLatLng(LatLng input) {
        if (input == null) {
            return "";
        }
        double lat = input.latitude;
        double lng = input.longitude;
        return (new StringBuilder(60)).append(lat).append(",").append(lng).toString();
    }

    public static String getCoordinatesStringFromLocation(double lat, double lng) {
        LatLng input = new LatLng(lat, lng);
        return getCoordinatesStringFromLatLng(input);
    }

    public static String distanceToLocation(LatLng user, LatLng location) {
        return String.format(Locale.US, "%.2f", SphericalUtil.computeDistanceBetween(user, location));
    }
}
