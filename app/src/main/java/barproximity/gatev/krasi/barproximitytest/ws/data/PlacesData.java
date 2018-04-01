package barproximity.gatev.krasi.barproximitytest.ws.data;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * Places WS data object
 */
public class PlacesData {

    @SerializedName("results")
    private List<Result> results = null;
    @SerializedName("status")
    private String status;

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
