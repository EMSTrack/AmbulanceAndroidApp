package hansyuan.cruzrojamobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by dchickey on 10/24/2017.
 */

public class AmbulanceListActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Ambulance> ambulanceList;
    private Spinner ambulanceSpinner;
    private Button submitAmbulanceButton;
    private Toolbar toolbar;

    AmbulanceApp ambulanceApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance_list);

        // TODO Set the toolbar


        ambulanceApp = ((AmbulanceApp) this.getApplication());

        // Get the Ambulance List from the Extras
        ambulanceList = (ArrayList<Ambulance>) getIntent().getSerializableExtra("AmbulanceList");

        // Initialize the UI elements
        submitAmbulanceButton = (Button) findViewById(R.id.submitAmbulanceButton);
        ambulanceSpinner = (Spinner) findViewById(R.id.ambulanceSpinner);

        // No hospitals associated with this account
        if (ambulanceList == null || ambulanceList.size() < 1) {
            // Create a toast for the user
            Toast.makeText(getBaseContext(), "This account has no ambulances associated with it!",
                    Toast.LENGTH_LONG).show();

            // If there are no Ambulance options, make the submit button unclickable
            submitAmbulanceButton.setClickable(false);

            return;
        }

        // Creates string array list of ambulance names
        ArrayList<String> listObjects = new ArrayList<>();
        for (int i = 0; i < ambulanceList.size(); i++) {
            listObjects.add(ambulanceList.get(i).getLicensePlate());
        }

        // Create the Spinner connection
        ambulanceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("AMBULANCE_LIST", "OnItemSelected Called");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("AMBULANCE_LIST", "OnNothingSelected Called");
            }
        });

        // Create the basic adapter
        ArrayAdapter<String> hospitalListAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listObjects);
        hospitalListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the spinner's adapter
        ambulanceSpinner.setAdapter(hospitalListAdapter);

        // Create the hospital button
        submitAmbulanceButton.setOnClickListener(this);

    }  // END onCreate


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.submitAmbulanceButton:
                System.out.println("Ambulance Submit Button Clicked");

                int position = ambulanceSpinner.getSelectedItemPosition();
                Ambulance selectedAmbulance = ambulanceList.get(position);
                Log.d("AMBULANCE_LIST", "Selected Ambulance: " + selectedAmbulance.getLicensePlate() + " ID: " + selectedAmbulance.getId());

                // TODO  Set the AmbulanceApp ID?
                // TODO push the selected id to user/@username/ambulance
                ambulanceApp.publishAmbulanceID(selectedAmbulance.getId());

                // Create the dashboard intent
                Intent dashboardIntent = new Intent(getApplicationContext(), MainActivity.class);

                // Add Serialized Ambulance Class
                dashboardIntent.putExtra("AmbulanceClass", selectedAmbulance);
                startActivity(dashboardIntent);

                break;

            default:
                Log.d("AMBULANCE_LIST", "onClick Default");

        }
    }

}
