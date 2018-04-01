package barproximity.gatev.krasi.barproximitytest.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import barproximity.gatev.krasi.barproximitytest.BarProxiApplication;
import barproximity.gatev.krasi.barproximitytest.Constants;
import barproximity.gatev.krasi.barproximitytest.utils.LocationUtils;
import barproximity.gatev.krasi.barproximitytest.ws.APIClient;
import barproximity.gatev.krasi.barproximitytest.ws.data.PlacesData;
import barproximity.gatev.krasi.barproximitytest.ws.data.Result;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * View model for retrieving data from the Places API and feeding it to the UI components
 */
public class LocationsResultsViewModel extends ViewModel {

    private MutableLiveData<List<Result>> mLocationResults;

    public MutableLiveData<List<Result>> getCurrentResults() {
        if (mLocationResults == null) {
            mLocationResults = new MutableLiveData<List<Result>>();
        }
        return mLocationResults;
    }

    public void fetchResults() {
        if (mLocationResults == null) {
            mLocationResults = new MutableLiveData<List<Result>>();
        }
        APIClient.getAPIInterface().getNearbyPlaces(LocationUtils.getCoordinatesStringFromLatLng(BarProxiApplication.getUserLocation())
                , Constants.LOCATION_TYPE_BAR, Constants.LOCATION_SEARCH_RADIUS, Constants.KEY_GOOGLE_APIS).enqueue(new Callback<PlacesData>() {
            @Override
            public void onResponse(Call<PlacesData> call, @NonNull Response<PlacesData> response) {
                if (response.body() != null && response.body().getResults() != null) {
                    mLocationResults.setValue(response.body().getResults());
                } else {
                    mLocationResults.setValue(new ArrayList<Result>());
                }
            }

            @Override
            public void onFailure(Call<PlacesData> call, Throwable t) {
                mLocationResults.setValue(new ArrayList<Result>());
            }
        });
    }
}