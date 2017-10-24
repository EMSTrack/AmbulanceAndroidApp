package hansyuan.cruzrojamobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

/**
 *
 * Class and Activity for creating code to communicate with database.
 *
 * This class may or may not be used in the future.
 *
 */
public class CommunicationDatabase extends AppCompatActivity {

    /**
     * Default method for activities when activity is created.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("COMMUNICATION_DATABASE","onCreate called");
        setContentView(R.layout.activity_communication_database);
    }
}
