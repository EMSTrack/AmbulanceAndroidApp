package hansyuan.cruzrojamobile;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

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
                GPS tab1 = new GPS();
                return tab1;
            case 1:
                demo_viewTransmission tab2 = new demo_viewTransmission();
                return tab2;
            case 2:
                demo_viewTransmission tab3 = new demo_viewTransmission();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}