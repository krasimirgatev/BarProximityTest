package barproximity.gatev.krasi.barproximitytest.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import barproximity.gatev.krasi.barproximitytest.R;
import barproximity.gatev.krasi.barproximitytest.adapters.PlacesListAdapter;
import barproximity.gatev.krasi.barproximitytest.viewmodels.LocationsResultsViewModel;
import barproximity.gatev.krasi.barproximitytest.ws.data.Result;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Locations List Fragment
 */
public class ListFragment extends Fragment {

    @BindView(R.id.lv_places)
    RecyclerView mRecyclerView;

    private PlacesListAdapter mListAdapter;
    private LocationsResultsViewModel mLocationsModel;

    public ListFragment() {
    }

    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationsModel = ViewModelProviders.of(getActivity()).get(LocationsResultsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, rootView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mListAdapter = new PlacesListAdapter(new PlacesListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Result item) {
                openGoogleMaps(item);
            }
        });
        mRecyclerView.setAdapter(mListAdapter);
        return rootView;
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
            mListAdapter.updateItems(results);
        }
    };

    private void openGoogleMaps(Result item) {
        double latitude = item.getGeometry().getLocation().getLat();
        double longitude = item.getGeometry().getLocation().getLng();
        String label = item.getName();
        String uriBegin = "geo:" + latitude + "," + longitude + "?q=";
        String encodedLabel = Uri.encode(label);
        Uri gmmIntentUri = Uri.parse(uriBegin + encodedLabel);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}