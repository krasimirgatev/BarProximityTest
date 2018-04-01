package barproximity.gatev.krasi.barproximitytest.ws.data;

import com.google.gson.annotations.SerializedName;

/**
 * Places WS data object
 */
public class Location {

    @SerializedName("lat")
    private Double lat;
    @SerializedName("lng")
    private Double lng;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

}
