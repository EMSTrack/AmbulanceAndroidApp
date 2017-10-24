package hansyuan.cruzrojamobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by dchickey on 10/24/2017.
 */

public class AmbulanceListActivity extends AppCompatActivity {

    public static ArrayList<Ambulance> ambulanceList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance_list);


        // No hospitals associated with this account
        if (ambulanceList.size() < 1) {
            Toast toast = new Toast(this);
            toast.setText("This account has no ambulances associated with it!");
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        // Creates string array list of ambulance names
        ArrayList<String> listObjects = new ArrayList<>();
        for (int i = 0; i < ambulanceList.size(); i++) {
            listObjects.add(ambulanceList.get(i).getLicensePlate());
        }

        // Create the Spinner connection
        final Spinner ambulanceSpinner = (Spinner) findViewById(R.id.ambulanceSpinner);
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
        ArrayAdapter<String> hospitalListAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listObjects);
        hospitalListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the spinner's adapter
        ambulanceSpinner.setAdapter(hospitalListAdapter);

        // Create the hospital button
        Button submitHospitalButton = (Button) findViewById(R.id.submitAmbulanceButton);
        submitHospitalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Ambulance Submit Button Clicked");

                //Intent dashboard = new Intent(HospitalListActivity.this, DashboardActivity.class);
                int position = ambulanceSpinner.getSelectedItemPosition();
                Log.d("AMBULANCE_LIST", "Position Selected: " + position);
                Ambulance selectedHospital = ambulanceList.get(position);
                Log.d("AMBULANCE_LIST", "Selected Ambulance: " + selectedHospital.getLicensePlate());

            }
        });

    }

}
