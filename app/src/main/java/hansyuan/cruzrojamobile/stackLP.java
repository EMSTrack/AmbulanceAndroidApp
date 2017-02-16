package hansyuan.cruzrojamobile;

import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Stack;

/**
 * Created by sinan on 2/8/2017.
 */

public class StackLP {

    //temp boolean, should check if the server received
    boolean serverSent = true;

    Stack<LocationPoint> s;
    String name;

    //basic LocationPoint stack ctor
    StackLP() {
        s = new Stack<LocationPoint>();
    }

    //stackLP ctor where the stack has a unique name
    StackLP(String inputName) {
        s = new Stack<LocationPoint>();
        name = inputName;
    }

    //to insert a new LocationPoint unto the stack
    public void insert(LocationPoint input) {
        s.push(input);
    }

    //mostRecent method to find the LocationPoint at the top of the stack
    public LocationPoint mostRecent() {
       return s.peek();
    }

    //method that pops a locationPoint from the stack if the server had received the location
    public boolean popIfSent() {
        /*final TextView mTextView = (TextView) findViewById(R.id.text);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //  mTextView.setText("Response is: "+ response.substring(0,500));
                        toasting("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
                toasting("That didn't work!!!!");
            }
        });*/

        if (serverSent) {
            s.pop();
            return true; //return true if pop happened
        }
        return false;
    }


    //not needed for now, not finished either
    //find method
    /*public boolean find(LocationPoint toFind) {
        Iterator<LocationPoint> it = s.iterator();
        while(it.hasNext()) {
     }
    }*/

    //insert



}
