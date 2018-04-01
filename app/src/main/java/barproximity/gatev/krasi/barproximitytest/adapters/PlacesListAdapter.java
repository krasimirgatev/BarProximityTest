package barproximity.gatev.krasi.barproximitytest.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import barproximity.gatev.krasi.barproximitytest.BarProxiApplication;
import barproximity.gatev.krasi.barproximitytest.R;
import barproximity.gatev.krasi.barproximitytest.utils.LocationUtils;
import barproximity.gatev.krasi.barproximitytest.ws.data.Result;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter used for the Locations List
 */
public class PlacesListAdapter extends RecyclerView.Adapter<PlacesListAdapter.ViewHolder> {

    private List<Result> mDataset;

    public interface OnItemClickListener {
        void onItemClick(Result item);
    }

    private final OnItemClickListener mListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.list_item_name)
        public TextView mTvName;
        @BindView(R.id.list_item_distance)
        public TextView mTvDistance;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Result item, final OnItemClickListener listener) {
            mTvName.setText(item.getName());
            LatLng locationLatLng = new LatLng(item.getGeometry().getLocation().getLat(), item.getGeometry().getLocation().getLng());
            mTvDistance.setText(LocationUtils.distanceToLocation(BarProxiApplication.getUserLocation(), locationLatLng) + "m");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    public PlacesListAdapter(OnItemClickListener listener) {
        mDataset = new ArrayList<>();
        mListener = listener;
    }

    @Override
    public PlacesListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_place, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mDataset.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void updateItems(List<Result> items) {
        mDataset.clear();
        mDataset.addAll(items);
        notifyDataSetChanged();
    }
}