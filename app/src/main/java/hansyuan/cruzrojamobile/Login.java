package hansyuan.cruzrojamobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

    private EditText editUserName;
    private EditText editPassword;
    private Button buttonSignin;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        boolean loggedIn = false;
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

        progressDialog = new ProgressDialog(this);

        buttonSignin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        userLogin();
    }

    private void userLogin() {
        String id = editUserName.getText().toString();
        String password = editPassword.getText().toString();

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

        //progress dialog screen
        progressDialog.setMessage("Signing in Please Wait...");
        progressDialog.show();

        //Check authenticity (id and pw - login team)
        progressDialog.dismiss();

        if(id.equals("king") && password.equals("justin")){
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
