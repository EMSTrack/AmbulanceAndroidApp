package hansyuan.cruzrojamobile.tab.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import hansyuan.cruzrojamobile.GPSTracker;
import hansyuan.cruzrojamobile.R;

/**
 * Java Class AND ACTIVITY
 * implements code for the GPSActivity Activity
 * Methods for lists and buttons are here.
 *
 * TODO
 * Location Point should probably be its own entity.
 *
 * Then when the LP is used to store data inside the phone, there
 * might be a method specific to the I/O that will parse the
 * LP Object. Or, LP might have the toString method modified
 * so that when we print to the file, we can just call the
 * toString method, and the save the time.
 *
 * The stack that will try to continually push most recent
 * data to the server might use the LP's method that will
 * return a new JSONObject.
 */
public class GPSActivity extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener{
    GPSTracker gpsTracker;
    Switch trackByTime;             // The switch for clock enable.
    Switch trackByDistance;
    View rootView;

    /*
     * Default method
     * Always called when an activity is created.
     * @param savedInstanceState
     */    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_gps, container, false);

        //checkLocationPermission(); //Might be needed, might not.
        gpsTracker = new GPSTracker(rootView.getContext());
        gpsTracker.setLatLongTextView((TextView) rootView.findViewById(R.id.LatLongText));


        //Determine whether to listen by dist changed or time changed
        trackByTime = (Switch) rootView.findViewById(R.id.trackByTimeSwitch);
        trackByTime.setOnCheckedChangeListener(this);

        trackByDistance = (Switch) rootView.findViewById(R.id.trackByDistanceSwitch);
        trackByDistance.setOnCheckedChangeListener(this);
        return rootView;
    }


    @Override
    public void onPause() {
        System.err.println("onPause: GPSActivity");
        super.onPause(); // This is required for some reason.
    }
    @Override
    public void onStop(){
        System.err.println("onStop: GPSActivity");
        trackByTime.setChecked(false);
        super.onStop(); // Same.
    }

    @Override
    public void onDestroy() {
        System.err.print("onDestroy: GPSActivity");
        trackByTime.setChecked(false);
        super.onDestroy(); // Same.
    }

    /** checks to see if any buttons were switched on or off.
     * If DistanceSwitch or TimeSwitch is turned off or on, update the listener
     * @param buttonView
     * @param isChecked
     */

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            //determine what was turned on
            switch (buttonView.getId()) {
                case R.id.trackByTimeSwitch: //turn on tracking by time
                    gpsTracker.turnOff();
                    long currDistanceTracking = gpsTracker.getMinDistanceChangeForUpdates();
                    gpsTracker = new GPSTracker(rootView.getContext(), currDistanceTracking, -1);
                    gpsTracker.setLatLongTextView((TextView) rootView.findViewById(R.id.LatLongText));
                    break;
                case R.id.trackByDistanceSwitch: //turn on tracking by distance
                    gpsTracker.turnOff();
                    long currTimeTracking = gpsTracker.getMinTimeBWUpdates();
                    gpsTracker = new GPSTracker(rootView.getContext(), -1, currTimeTracking);
                    gpsTracker.setLatLongTextView((TextView) rootView.findViewById(R.id.LatLongText));
                    break;
                default:

            }
        } else { // something was turned off
            switch (buttonView.getId()) {
                case R.id.trackByTimeSwitch: //turn off tracking by time
                    gpsTracker.turnOff();
                    long currDistanceTracking = gpsTracker.getMinDistanceChangeForUpdates();
                    //continue tracking by distance
                    gpsTracker = new GPSTracker(rootView.getContext(), 0, currDistanceTracking);
                    break;
                case R.id.trackByDistanceSwitch: //turn off tracking by distance
                    gpsTracker.turnOff();
                    long currTimeTracking = gpsTracker.getMinTimeBWUpdates();
                    //continue tracking by time
                    gpsTracker = new GPSTracker(rootView.getContext(), currTimeTracking, 0);
                    break;
                default:
            }
        }
    }



    /**
     * Will set the textview as the string you pass in.
     * If you delete LatLongText DELETE THIS METHOD TODO
     * @param s
     */
    public void display(String s){
        TextView t = (TextView) rootView.findViewById(R.id.LatLongText);
        t.setText(s);
    }


    @Override
    public void onClick(View v) {

    }
}


