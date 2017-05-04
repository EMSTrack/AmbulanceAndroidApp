package hansyuan.cruzrojamobile;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.TabLayout;
import android.view.View;

/**
 * This is the main activity -- the default screen
 */
public class MainActivity extends AppCompatActivity {

    //global StackLP variable
    static StackLP buffStack;
    // GoogleApiClient mGoogleApiClient;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ((AmbulanceApp) this.getApplication()).onCreate();
        buffStack = new StackLP();
        //this.OpenGPS(); //Comment this out if you don't want to auto-start the GPSActivity activity!

        //set up TabLayout Structure
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_home);
        tabLayout.addTab(tabLayout.newTab().setText("Dispatcher"));
        //tabLayout.addTab(tabLayout.newTab().setText("Messages"));
        tabLayout.addTab(tabLayout.newTab().setText("Hospital"));
        tabLayout.addTab(tabLayout.newTab().setText("GPS"));

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        //Setup Adapter
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    /**
     * This is the method for opening a new activity.
     * Generalizable to any method whose only purpose is to load a new activity.
     *
     * @param view
     */
    public void OpenGPS(View view) {
        OpenGPS();
    }

    // The no-arg activity starter for the GPSActivity activity.
    private void OpenGPS() {
        Intent i = new Intent(getApplication(), GPSActivity.class);
        startActivity(i);
    }

    public void openInfo(View view) {
        openInfo();
    }

    private void openInfo() {
        //Intent z = ;
        startActivity(new Intent(getApplication(), demo_viewTransmission.class));
    }

    public void openFileView(View view) {
        startActivity(new Intent(getApplication(), LPBackupExplorer.class));
    }
}