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

/**
 * Created by justingil1748 on 4/14/17.
 */

public class Login extends AppCompatActivity implements View.OnClickListener {

    public EditText editUserName;
    public EditText editPassword;
    private Button buttonSignin;
    public AmbulanceApp ambulance;
    public static ProgressDialog dialog;
    private boolean log;
    MqttClient mqttServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ambulance = ((AmbulanceApp) this.getApplication()).onCreate(this);
        ambulance.mqttMaster();

        boolean loggedIn = true;
        //if user is logged in
        if (loggedIn) {
            //that means user is already logged in, so close this activity
            finish();
            //opens main activity
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        //user is not logged in yet,
        //initializing views and buttons and fields by ID
        editUserName = (EditText) findViewById(R.id.editUserName);
        editPassword = (EditText) findViewById(R.id.editPassword);
        buttonSignin = (Button) findViewById(R.id.buttonSignin);

        buttonSignin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        userLogin();
    }

    private void userLogin() {
        final String id = editUserName.getText().toString();
        final String password = editPassword.getText().toString();
        ambulance.setUserId(id);
        ambulance.setUserPw(password);

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(id)){
            Toast.makeText(this,"Please enter username",Toast.LENGTH_LONG).show();
            return;
        }

        //check that password is not empty
        if(TextUtils.isEmpty(password)){
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

    private void checking(){
        if(log){
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        else{
            Toast.makeText(getBaseContext(), "Can't find your account. \nPlease check your email or password",
                    Toast.LENGTH_LONG).show();
            return;
        }
    }
}
