package hansyuan.cruzrojamobile;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Java Class that implements code for the GPS Activity
 * Methods for lists and buttons are here.
 */
public class GPS extends AppCompatActivity {

    GPSTracker gps;


    /**
     * Default method
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

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
     * Does stuff:
     *
     * creates a new GPSTracker instance
     * detects whether it can find GPS.
     */
    private void doStuff(){

        gps = new GPSTracker( this );

        //GPSTracker gps = new GPSTracker(this); not sure why this exists.

        if(gps.canGetLocation()){
            this.toasting("Yeah");
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



    /**
     *
     * @param view
     */
    public void broadcastCruzRoja(View view){
        final TextView mTextView = (TextView) findViewById(R.id.text);


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        String url ="http://cruzroja.ucsd.edu/ambulances/update/123456?status=\"thisisandroid\"";
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
