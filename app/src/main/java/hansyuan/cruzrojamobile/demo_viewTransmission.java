package hansyuan.cruzrojamobile;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * This class is purely meant to demonstrate that the information is able to send
 * to the server. The website is:
 *
 * http://cruzroja.ucsd.edu/ambulances/info/123456
 *
 *
 */
public class demo_viewTransmission extends AppCompatActivity {
    TextView checkInfo;
    String result, url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_view_transmission);
        checkInfo = (TextView) findViewById(R.id.checkInfo);
        url = "http://cruzroja.ucsd.edu/ambulances/info/123456";
        //refresh();
    }


    /**
     * Gives the refresh button its functionality:
     * Do a GET Request to get the info that is already on the server.
     * Pass it as a string into SetText.
     * @param view
     */
    public void refresh(View view) {
        refresh();
    }
    private void setResult(String r){
        this.result =  r;
        checkInfo.setText(this.result);
    }

    private void refresh() {
      //git   toasting("refresh is run.");

        // Go to the URL and get the result.
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //  mTextView.setText("Response is: "+ response.substring(0,500));
                        setResult("Response is: "+ response.substring(0, response.length()));
                       // toasting("This happened.");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
                setResult("That didn't work!!!!");
               // toasting("This failed");
            }
        });

        queue.add(stringRequest);

        //toasting("right before the setText");

    }

    public void toasting(String toToast){
        Context context = getApplicationContext();
        CharSequence text = toToast;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}
