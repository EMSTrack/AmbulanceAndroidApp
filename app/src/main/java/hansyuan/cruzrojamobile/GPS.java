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

    GPSTracker gps;


    //
    String url ="http://cruzroja.ucsd.edu/ambulances/update/123456?status=";
    public final static int INTERVAL = 1000 * 3 ;  // ( ____ sec * (1000 ms / 1 sec))
    Handler clockedHandler = new Handler();
    Runnable clockedHandlerTask = new Runnable()
        {
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
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        //checkLocationPermission();

        //doStuff();
        doStuffWrapper();

        // Dropdown Menu (spinner)
        Spinner spinner = (Spinner) findViewById(R.id.statusupdate);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_updates, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        clockEnable = (Switch) findViewById(R.id.clockSwitch);
        clockEnable.setOnCheckedChangeListener(this);
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



    /*
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_COARSE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    myLocationManager.getLastKnownLocationIfAllowed();
                } else {
                    //Permission denied
                }
                return;
            }
        }
    }
    */

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
    /*
    protected void checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                private void showPermissionDialog() {
                    if (!LocationController.checkPermission(this)) {
                        ActivityCompat.requestPermissions(
                                this,
                                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                                PERMISSION_LOCATION_REQUEST_CODE);
                    }
                }

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
                System.out.println("lol this does nothing! ");
            }
        }
    }*/

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
     * Does stuff:
     *
     * creates a new GPSTracker instance
     * detects whether it can find GPS.
     */
    private void doStuff(){

        gps = new GPSTracker( this );
        gps.getLastKnownLocationIfAllowed();
        //GPSTracker gps = new GPSTracker(this); not sure why this exists.

        if(gps.canGetLocation()){
            this.toasting("Got location.");
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
    private void doStuffWrapper() {
            // Execute some code after 2 seconds have passed
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    doStuff();
                }
            }, 2000);
    }


    /**
     *
     * @param view
     */
    public void UpdateGPS(View view) {

        doStuff();
    }

    /**
     *
     * @param view
     */
    public void broadcast(View view){
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


    /*
    * THIS IS A TEST METHOD
    * YES, IT IS OVERLOADING.
    *
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
     *
     * @param view
     */
    public void broadcastCruzRoja(View view){
        final TextView mTextView = (TextView) findViewById(R.id.text);

        Spinner mySpinner=(Spinner) findViewById(R.id.statusupdate);


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        String status = mySpinner.getSelectedItem().toString();
        String url = this.url + status;

        /** Insert Java method here to get the location, turn into string, and
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
        queue.add(stringRequest);
    }
}
