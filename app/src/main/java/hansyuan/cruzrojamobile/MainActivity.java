package hansyuan.cruzrojamobile;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.TabLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * This is the main activity -- the default screen
 */
public class MainActivity extends AppCompatActivity {

    //global StackLP variable
    static StackLP buffStack;
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    private Spinner dropdown;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ((AmbulanceApp) this.getApplication()).onCreate();
        buffStack = new StackLP();

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawerToggle = setupDrawerToggle();

        mDrawer.addDrawerListener(drawerToggle);

        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);

        //set up TabLayout Structure
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_home);
        tabLayout.addTab(tabLayout.newTab().setText("Dispatcher"));
        tabLayout.addTab(tabLayout.newTab().setText("Hospital"));
        tabLayout.addTab(tabLayout.newTab().setText("GPS"));

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        dropdown = (Spinner) findViewById(R.id.spinner1);
        String[] items = new String[]{"Idle", "To patient", "To Hospital", "Rest"};
        ArrayAdapter<String> dropDownAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(dropDownAdapter);


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

    private ActionBarDrawerToggle setupDrawerToggle(){
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Activity activity= null;
        Class activityClass;
        switch(menuItem.getItemId()) {
            case R.id.home:
                activityClass = MainActivity.class;
                break;
            case R.id.profile:
                activityClass = MainActivity.class;
                break;
            case R.id.settings:
                activityClass = MainActivity.class;
                break;
            case R.id.logout:
                activityClass = Login.class;
                break;
            default:
                activityClass = MainActivity.class;
        }

        try {
            activity = (Activity) activityClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent i = new Intent(this, activityClass);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);

        // Close the navigation drawer
        mDrawer.closeDrawers();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }
}