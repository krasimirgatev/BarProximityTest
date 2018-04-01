package barproximity.gatev.krasi.barproximitytest.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import barproximity.gatev.krasi.barproximitytest.fragments.ListFragment;
import barproximity.gatev.krasi.barproximitytest.fragments.MapFragment;

/**
 * Switches between the List and Map fragments
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return ListFragment.newInstance();
        } else {
            return MapFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}