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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * This is the main activity -- the default screen
 */
public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;
    private TextView statusText;
    MqttClient mqttServer;

    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((AmbulanceApp) this.getApplication()).onCreate(this);

        statusText = (TextView) findViewById(R.id.statusText);

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

        //pager
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Setup Adapter for tabLayout
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

        mqttMaster();
    }


    //Hamburger Menu setup
    private ActionBarDrawerToggle setupDrawerToggle(){
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    //Hamburger Menu Listener
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

    //Start selected activity in Hamburger
    public void selectDrawerItem(MenuItem menuItem) {
        Class activityClass;
        switch(menuItem.getItemId()) {
            case R.id.home:
                activityClass = MainActivity.class;
                break;
            case R.id.logout:
                activityClass = Login.class;
                break;
            default:
                activityClass = MainActivity.class;
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

    //MQTT
    private void mqttMaster() {
        mqttServer = MqttClient.getInstance(this);
        mqttServer.connect("brian", "cruzroja", new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                if(reconnect) {
                    Log.d(TAG, "Reconnected to broker");
                } else {
                    Log.d(TAG, "Connected to broker");
                }

                //subscribe here
                mqttServer.subscribeToTopic("ambulance/1/status");

                //formatting to JSON object
                try {
                    JSONObject parent = new JSONObject();
                    JSONObject location = new JSONObject();

                    location.put("latitude", "53.245354");
                    location.put("longigtude", "-127.27435");

                    parent.put("location", location);
                    parent.put("timestamp", "2098-10-25 14:30:59");
                    Log.d("output", parent.toString(2));

                    mqttServer.publish(parent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                Log.d(TAG, "Connection to broker lost");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d(TAG, "Message received: " + new String(message.getPayload()));
                statusText.setText(new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.d(TAG, "Message sent successfully");
            }
        });
    }
}