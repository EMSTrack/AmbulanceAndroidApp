package hansyuan.cruzrojamobile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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
 * implements code for the GPS Activity
 * Methods for lists and buttons are here.
 */
public class GPS extends AppCompatActivity  implements CompoundButton.OnCheckedChangeListener{
    //public final static int INTERVAL = 1000 * 3 ;  // ( ____ sec * (1000 ms / 1 sec))
    public final static int DELAY_START = 2000;

    GPSTracker gps;

    //
    //String url ="http://cruzroja.ucsd.edu/ambulances/update/123456?status=";
    public final static int INTERVAL = 1000 * 3 ;  // ( ____ sec * (1000 ms / 1 sec))

    String url = "http://cruzroja.ucsd.edu/ambulances/update/123456?status=";

    Handler clockedHandler = new Handler();
    //Switch clockEnable;
    Spinner spinner;


    //The following is a declaration, instantiation, with a lambda function defined.
    //Terrible style.
    Runnable clockedHandlerTask = new Runnable()
        {
            /** todo Perhaps the method should also refresh the location first otherwise
             * it will just keep transmitting the same thing.
             *
             * Also, I think it's currently hooked up to the Google broadcaster. 
             */
        @Override
        public void run () {
            broadcast();
            clockedHandler.postDelayed(clockedHandlerTask,INTERVAL);
            if ( !clockEnable.isChecked() ) {
                stopRepeatingTask();
            }
        }
    };



    Switch clockEnable;

    /**
     * Default method
     * Always called when an activity is created.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        //checkLocationPermission(); //Might be needed, might not.

        timeDelay(DELAY_START);
        tryGPS(); //MUST BE CALLED AFTER THE DELAY

        // Dropdown Menu (spinner)
        spinner = (Spinner) findViewById(R.id.statusupdate);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_updates, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        clockEnable = (Switch) findViewById(R.id.clockSwitch);
        clockEnable.setOnCheckedChangeListener(this);

        //Sets the listener. TODO: check if gps is on, if not, toast

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
        Context context = getApplicationContext();
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
        TextView t = (TextView) findViewById(R.id.textView1);
        t.setText(s);
    }

    /**
     * creates a new GPSTracker instance
     * detects whether it can find GPS first
     * If it can't, then simply stop immediately.
     */
    private void tryGPS(){
        gps = new GPSTracker( this );
        gps.getLastKnownLocationIfAllowed();
        //GPSTracker gps = new GPSTracker(this); not sure why this exists.

        if(gps.canGetLocation()){
            this.toasting("Got location.");
        }
        else{
            this.toasting("Could not get location.");
            return;
        }

        double lat = gps.getLatitude(); // returns latitude
        double lon = gps.getLongitude(); // returns longitude

        //String s = new String();
        //s += "Lat: " + lat + ". Lon: " + lon;

        LocationPoint loc = new LocationPoint(lon, lat);
        this.display(loc.toString());

       // this.display(s);
    }

    /**
     *
     */
    private void timeDelay(int milliseconds) {
            // Execute some code after 2 seconds have passed
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run(){}}, //Run nothing
                    milliseconds); //Delay Set
    }


    /**
     *
     * @param view
     */
    public void UpdateGPS(View view) {
        tryGPS();
    }

    /**
     *
     * @param view
     */
    public void broadcast(View view){
        /*
        final TextView mTextView = (TextView) findViewById(R.id.text);


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
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
        */
        broadcast();
    }


    /*
    * This
    * */

    public void broadcast(){
        final TextView mTextView = (TextView) findViewById(R.id.text);


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
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
    public void broadcastCruzRoja(View view){
        final TextView mTextView = (TextView) findViewById(R.id.text);

        Spinner mySpinner=(Spinner) findViewById(R.id.statusupdate);


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String status = mySpinner.getSelectedItem().toString();

        String url = this.url + status; // Incomplete
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
                        toasting("Response is: "+ response.substring(0, response.length()));
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
}
