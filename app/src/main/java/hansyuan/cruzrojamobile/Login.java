package hansyuan.cruzrojamobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import hansyuan.cruzrojamobile.tab.fragments.demo_viewTransmission;

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

/*        // TODO should this jump to the MainActivity or AmbulanceList?
        if (ambulance.getUserLoggedIn()) {
            //that means user is already logged in, so close this activity
            finish();
            //opens main activity
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }*/


        //user is not logged in yet,
        //initializing views and buttons and fields by ID
        editUserName = (EditText) findViewById(R.id.editUserName);
        editPassword = (EditText) findViewById(R.id.editPassword);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(this);

        // TODO remove, for testing only
        editUserName.setText("admin");
        editPassword.setText("cruzrojaadmin");

    }

    @Override
    public void onClick(View v) {
        userLogin();
    }

    private void userLogin() {

        final String id = editUserName.getText().toString();
        final String password = editPassword.getText().toString();

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


        ambulance.setUserId(id);
        ambulance.setUserPw(password);

        //mqttMaster connect to MQTT
        ambulance.mqttMaster();
        mqttServer = MqttClient.getInstance(this);

        //Sign in progress msg
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Signing in. Please wait...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

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

            // Create Intent and add AmbulanceList as a serialized extra
            Intent ambulanceListIntent = new Intent(getApplicationContext(), AmbulanceListActivity.class);
            ambulanceListIntent.putExtra("AmbulanceList", ambulance.ambulanceList);

            // Start the AmbulanceListActivity
            startActivity(ambulanceListIntent);

        } else {
            Toast.makeText(getBaseContext(), "Can't find your account. \nPlease check your email or password",
                    Toast.LENGTH_LONG).show();
        }
    }
}
