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


    }



    /**
     * This is the method for opening a new activity.
     * Generalizable to any method whose only purpose is to load a new activity.
     * @param view
     */
    public void OpenGPS(View view) {

        Intent i = new Intent(this, GPS.class);
        startActivity(i);
    }



} // end class
