package hansyuan.cruzrojamobile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
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


/**
 * Java Class AND ACTIVITY
 * implements code for the GPSActivity Activity
 * Methods for lists and buttons are here.
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
    //public final static int INTERVAL = 1000 * 3 ;  // ( ____ sec * (1000 ms / 1 sec))
    public final static int INTERVAL = 1000 * 10 ;  // ( ____ sec * (1000 ms / 1 sec))
    public final static int DELAY_START = 2000;
    GPSTracker gpsTracker;
    String url = "http://cruzroja.ucsd.edu/ambulances/update/123456?status=";
    //Switch clockEnable;
    Spinner statusSpinner;
    Spinner mySpinner;
    Switch trackByTime;             // The switch for clock enable.
    Switch trackByDistance;
    View rootView;


    Button broadCastCruzRoja;

    /*
     * Default method
     * Always called when an activity is created.
     * @param savedInstanceState
     */    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_gps, container, false);

        broadCastCruzRoja = (Button) rootView.findViewById(R.id.broadcastCruz);
        broadCastCruzRoja.setOnClickListener(this);
                //checkLocationPermission(); //Might be needed, might not.
        gpsTracker = new GPSTracker(rootView.getContext());

        // Dropdown Menu (statusSpinner)
        statusSpinner = (Spinner) rootView.findViewById(R.id.statusupdate);

        // Create an ArrayAdapter using the string array and a default statusSpinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.status_updates, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the statusSpinner
        statusSpinner.setAdapter(adapter);

        // set current status
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String newStatus = statusSpinner.getSelectedItem().toString();
                ((AmbulanceApp)AmbulanceApp.getAppContext()).setCurrStatus(newStatus);
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        //Determine whether to listen by dist changed or time changed
        trackByTime = (Switch) rootView.findViewById(R.id.trackByTimeSwitch);
        trackByTime.setOnCheckedChangeListener(this);

        trackByDistance = (Switch) rootView.findViewById(R.id.trackByDistanceSwitch);
        trackByDistance.setOnCheckedChangeListener(this);
        return rootView;
    }

    public void openSavedLocations(View v){
        startActivity(new Intent(this.getActivity(), SavedLocations.class));
    }

    @Override
    public void onPause() {
        super.onPause(); // This is required for some reason.
        trackByTime.setChecked(false);
    }
    @Override
    public void onStop(){
        super.onStop(); // Same.
        trackByTime.setChecked(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy(); // Same.
        trackByTime.setChecked(false);
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
                    break;
                case R.id.trackByDistanceSwitch: //turn on tracking by distance
                    gpsTracker.turnOff();
                    long currTimeTracking = gpsTracker.getMinTimeBWUpdates();
                    gpsTracker = new GPSTracker(rootView.getContext(), -1, currTimeTracking);
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
     * If you delete textview1 DELETE THIS METHOD TODO
     * @param s
     */
    private void display(String s){
        TextView t = (TextView) rootView.findViewById(R.id.textView1);
        t.setText(s);
    }




    public void broadcast(View view){
        broadcast();
    }


    /*
    * This
    * */

    public void broadcast(){
        final TextView mTextView = (TextView) rootView.findViewById(R.id.text);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url ="http://www.google.com";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");

            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }



    /**
     * Thie method will compile together the location point information, append
     * it to the url, and then do a GET request on the URL.
     */
    public void broadcastCruzRoja() {
        final TextView mTextView = (TextView) rootView.findViewById(R.id.text);

        mySpinner = (Spinner) rootView.findViewById(R.id.statusupdate);


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String status = mySpinner.getSelectedItem().toString();
        String lon = "?longitude=1.2345";
        String latt = "?lattitude=5.4321";

        String url = this.url + status + lon + latt; // arbitrary values for lon and lat
        // todo May need to do a method call to locationpoint or something here.

        /** TODO Insert Java method here to get the location, turn into string, and
         * concat with URL  */


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //  mTextView.setText("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");

            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest); // Not sure about this.
    }

    @Override
    public void onClick(View v) {
        if(v == broadCastCruzRoja){
            broadcastCruzRoja();
        }
    }
}


