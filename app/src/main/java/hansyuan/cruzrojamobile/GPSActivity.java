package hansyuan.cruzrojamobile;

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
    //public final static int INTERVAL = 1000 * 3 ;  // ( ____ sec * (1000 ms / 1 sec))
    public final static int INTERVAL = 1000 * 10 ;  // ( ____ sec * (1000 ms / 1 sec))
    public final static int DELAY_START = 2000;
    GPSTracker gps;
    String url = "http://cruzroja.ucsd.edu/ambulances/update/123456?status=";
    Handler clockedHandler = new Handler();
    //Switch clockEnable;
    Switch clockEnable;             // The switch for clock enable.
    View rootView;
    Button sendInfoButton;
    Button savedLocationButton;
    Switch listenerEnable;          // The switch for location listener

    /*
     * Default method
     * Always called when an activity is created.
     * @param savedInstanceState
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.activity_gps, container, false);

        sendInfoButton = (Button) rootView.findViewById(R.id.broadcastCruz);
        savedLocationButton = (Button) rootView.findViewById(R.id.savedLocations);
        sendInfoButton.setOnClickListener(this);
        savedLocationButton.setOnClickListener(this);


        //checkLocationPermission(); //Might be needed, might not.

        /* TODO This activity will NO LONGER automatically check a new location upon starting.

        timeDelay(DELAY_START);
        tryGPS(); //MUST BE CALLED AFTER THE DELAY
        */
        gps = new GPSTracker(rootView.getContext());


        clockEnable = (Switch) rootView.findViewById(R.id.trackByTimeSwitch);
        clockEnable.setOnCheckedChangeListener(this);

        return rootView;
    }


    //The following is a declaration, instantiation, with a lambda function defined.
    Runnable clockedHandlerTask = new Runnable()
    {
        /** todo Perhaps the method should also refresh the location first otherwise
         * Repeats whatever is in the run method.
         *
         * Also, I think it's currently hooked up to the Google broadcaster.
         */
        @Override
        public void run () {
            tryGPS();           // get an updated location
            broadcast();        // Do a GET request to Google.
            listenerEnable = (Switch) rootView.findViewById(R.id.trackByTimeSwitch);

            //a Delay is done:
            clockedHandler.postDelayed(clockedHandlerTask,INTERVAL);
            if ( !clockEnable.isChecked() ) {
                stopRepeatingTask();
            }
        }
    };


    /** For the following three methods, stop the clock when the activity, in any way, is left. */
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

                return true;
            } else {


                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation

            return true;
        }
    }
    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(getActivity(), "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onPause() {
        System.err.println("onPause: GPSActivity");
        super.onPause(); // This is required for some reason.

    }

    @Override
    public void onStop(){
        super.onStop(); // Same.
        clockEnable.setChecked(false);

    }

    @Override
    public void onDestroy() {

        super.onDestroy(); // Same.
        clockEnable.setChecked(false);

    }

    /**
     * Anytime we want to do a time delay, use this method.
     * This method can be copy and pasted as ubiquitiously as the toast method.
     */
    private void timeDelay(int milliseconds) {
        // Execute some code after 2 seconds have passed
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
                                @Override
                                public void run(){}}, //Run nothing
                milliseconds); //Delay Set
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked){
            startRepeatingTask();
        }
        else{
            stopRepeatingTask();
        }
    }

    void startRepeatingTask(){
        clockedHandlerTask.run();
    }
    void stopRepeatingTask()
    {
        clockedHandler.removeCallbacks(clockedHandlerTask);
    }

    /**
     * Automatically initiate a toast based on the string parameter
     * Can consider this to be Android's println for the sake of debugging.
     * @param toToast the string you display in a toast
     */
    public void toasting(String toToast){
        Context context = getContext();
        CharSequence text = toToast;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
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

    /**
     * creates a new GPSTracker instance
     * detects whether it can find GPSActivity first
     * If it can't, then simply stop immediately.
     *
     * For now, this creates a new GPSTracker every single time I run the ability to get an
     * updated location. This might not be efficient but for now it's okay and we should focus
     * on improving the connection with the server.
     */
    private void tryGPS(){
        //gps = new GPSTracker( this );
        gps.getLastKnownLocationIfAllowed();
        //GPSTracker gps = new GPSTracker(this); not sure why this exists.

        if(gps.isGPSEnabled() || gps.isNetworkEnabled()){
            this.toasting("Can get location.");
        }
        else{
            this.toasting("Could not get location.");
            return;
        }


        gps.getLocation();

        double lat = gps.getLatitude(); // returns latitude
        double lon = gps.getLongitude(); // returns longitude



        LocationPoint loc = new LocationPoint(lon, lat);
        this.display(loc.toString());
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
                        // Display the first 500 characters of the response string.
                        //  mTextView.setText("Response is: "+ response.substring(0,500));
                        toasting("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
                toasting("That didn't work!!!!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    /**
     * Thie method will compile together the location point information, append
     * it to the url, and then do a GET request on the URL.
     * @param view
     */
    public void broadcastCruzRoja(View view) {
        final TextView mTextView = (TextView) rootView.findViewById(R.id.text);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String lon = "?longitude=1.2345";
        String latt = "?lattitude=5.4321";

        String url = this.url + lon + latt; // arbitrary values for lon and lat
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
                        toasting("Response is: " + response.substring(0, response.length()));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
                toasting("That didn't work!!!!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest); // Not sure about this.
    }

    @Override
    public void onClick(View v) {
        if(v == sendInfoButton){
            broadcastCruzRoja(v);
        }
        else if(v == savedLocationButton){
            startActivity(new Intent(this.getActivity(), SavedLocations.class));
        }
    }
}


