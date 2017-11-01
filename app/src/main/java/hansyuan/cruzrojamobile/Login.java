package hansyuan.cruzrojamobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by justingil1748 on 4/14/17.
 */

public class Login extends AppCompatActivity implements View.OnClickListener {

    public EditText editUserName;
    public EditText editPassword;
    private Button buttonLogin;
    public AmbulanceApp ambulance;
    public static ProgressDialog dialog;
    private boolean log;
    MqttClient mqttServer;


    private boolean testLogin = true;


    /*
    * Admin Account Login:
    * admin
    * cruzrojaadmin
    * */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ambulance = ((AmbulanceApp) this.getApplication()).onCreate(this);

        //if user is logged in

        if (ambulance.getUserLoggedIn()) {
            //that means user is already logged in, so close this activity
            finish();
            //opens main activity
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        //user is not logged in yet,
        //initializing views and buttons and fields by ID
        editUserName = (EditText) findViewById(R.id.editUserName);
        editPassword = (EditText) findViewById(R.id.editPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        userLogin();
    }

    private void userLogin() {

        // TODO remove after done testing, autologin
        String tempId; // = editUserName.getText().toString();
        String tempPassword; // = editPassword.getText().toString();
        if (testLogin) {
            tempId = "admin";
            tempPassword = "cruzrojaadmin";
        } else {
            tempId = editUserName.getText().toString();
            tempPassword = editPassword.getText().toString();
        }

        final String id = tempId;
        final String password = tempPassword;

        ambulance.setUserId(id);
        ambulance.setUserPw(password);


        //checking if email and passwords are empty
        if (TextUtils.isEmpty(id)) {
            Toast.makeText(this,"Please enter username",Toast.LENGTH_LONG).show();
            return;
        }

        //check that password is not empty
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        mqttServer = MqttClient.getInstance(this);

        dialog = new ProgressDialog(this); // this = YourActivity
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Signing in. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        log = mqttServer.userLogin(id, password);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                log = mqttServer.userLogin(id, password);
                checking();
                dialog.dismiss();
            }
        }, 5000);
    }

    private void checking() {

        if (log) {
            finish();
            

            // TODO remove the test values and pull from MQTT here
            String[] arr = { "3ABC123", "5FGH789", "0PLM980" };
            ArrayList<Ambulance> ambulanceList = new ArrayList<>();
            for (int i = 0; i < arr.length; i++) {
                Ambulance ambulance = new Ambulance(i, arr[i]);
                ambulanceList.add(ambulance);
            }


            // Create Intent and add AmbulanceList as a serialized extra
            Intent ambulanceListIntent = new Intent(getApplicationContext(), AmbulanceListActivity.class);
            ambulanceListIntent.putExtra("AmbulanceList", ambulanceList);

            // Start the AmbulanceListActivity
            startActivity(ambulanceListIntent);

        } else {
            Toast.makeText(getBaseContext(), "Can't find your account. \nPlease check your email or password",
                    Toast.LENGTH_LONG).show();
            return;
        }
    }
}
