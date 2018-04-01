package barproximity.gatev.krasi.barproximitytest.ws.data;

import com.google.gson.annotations.SerializedName;

/**
 * Places WS data object
 */
public class Geometry {

    @SerializedName("location")
    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

}
