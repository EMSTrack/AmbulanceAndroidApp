package hansyuan.cruzrojamobile.tab.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import hansyuan.cruzrojamobile.R;

/**
 * This class is purely meant to demonstrate that the information is able to send
 * to the server. The website is:
 *
 * http://cruzroja.ucsd.edu/ambulances/info/123456
 *
 *
 */
public class demo_viewTransmission extends Fragment {
    TextView checkInfo;
    String result, url;
    View rootView;
    Button refButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_demo_view_transmission, container, false);
        refButton = (Button) rootView.findViewById(R.id.button2);

        refButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh(v);
            }
        });

        checkInfo = (TextView) rootView.findViewById(R.id.checkInfo);
        url = "http://cruzroja.ucsd.edu/ambulances/info/123456";
        //refresh();
        return rootView;
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

    public void refresh() {
      //git   toasting("refresh is run.");

        // Go to the URL and get the result.
        RequestQueue queue = Volley.newRequestQueue(getActivity());

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
        Context context = getContext();
        CharSequence text = toToast;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}
