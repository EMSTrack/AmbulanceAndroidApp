package hansyuan.cruzrojamobile.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import hansyuan.cruzrojamobile.fragments.DispatcherFragment;
import hansyuan.cruzrojamobile.fragments.GPSTestFragment;
import hansyuan.cruzrojamobile.fragments.HospitalListFragment;

/**
 * Created by justingil1748 on 4/17/17.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                DispatcherFragment tab1 = new DispatcherFragment();
                return tab1;
            case 1:
                HospitalListFragment tab3 = new HospitalListFragment();
                return tab3;
            case 2:
                GPSTestFragment tab4 = new GPSTestFragment();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}