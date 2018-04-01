package barproximity.gatev.krasi.barproximitytest.ws;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Initializes API client a single time
 */
public class APIClient {

    public static final String API_ENDPOINT =
            "https://maps.googleapis.com/maps/api/";

    private static ServicesInterface mApiInterface =
        getClient().create(ServicesInterface.class);

    public static ServicesInterface getAPIInterface() {
        return mApiInterface;
    }

    private static Retrofit getClient() {

        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logger);
        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(API_ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();

        return retrofit;
    }
}
