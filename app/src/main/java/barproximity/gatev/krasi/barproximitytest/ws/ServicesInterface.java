package barproximity.gatev.krasi.barproximitytest.ws;

import barproximity.gatev.krasi.barproximitytest.ws.data.PlacesData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit Web Service Interface
 */
public interface ServicesInterface {
    @GET("place/nearbysearch/json?")
    Call<PlacesData> getNearbyPlaces(@Query("location") String location, @Query("types") String types, @Query("radius") String radius, @Query("key") String key);
}