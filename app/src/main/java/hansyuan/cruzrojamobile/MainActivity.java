package hansyuan.cruzrojamobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * This is the main activity -- the default screen
 */
public class MainActivity extends AppCompatActivity {
   // GoogleApiClient mGoogleApiClient;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.OpenGPS(); //Comment this out if you don't want to auto-start the GPS activity!
    }



    /**
     * This is the method for opening a new activity.
     * Generalizable to any method whose only purpose is to load a new activity.
     * @param view
     */
    public void OpenGPS(View view) {
        OpenGPS();
    }

    // The no-arg activity starter for the GPS activity.
    private void OpenGPS() {
        Intent i = new Intent(this, GPS.class);
        startActivity(i);
    }

    public void openInfo(View view) {
        openInfo();
    }

    private void openInfo(){
        //Intent z = ;
        startActivity (new Intent (this, demo_viewTransmission.class));
    }



} // end class
