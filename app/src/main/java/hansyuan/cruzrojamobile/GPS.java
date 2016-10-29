package hansyuan.cruzrojamobile;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Java Class that implements code for the GPS Activity
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

        doStuff();
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





}
