package hansyuan.cruzrojamobile;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import android.util.Log;
/**
 * Created by rawaa_ali on 6/1/17.
 */

public class Hospital {

    public String title;
    public String description;


    public static ArrayList<Hospital> getHospitalsFromFile(String filename, Context context){
        final ArrayList<Hospital> hospitalList = new ArrayList<>();

        try {
            // Load data
            String jsonString = loadJsonFromAsset("hospitals.json", context);
            JSONObject json = new JSONObject(jsonString);
            JSONArray hospitals = json.getJSONArray("hospitals");

            // Get hospital objects from data
            for(int i = 0; i < hospitals.length(); i++){
                Hospital hospital = new Hospital();

                hospital.title = hospitals.getJSONObject(i).getString("title");
                hospital.description = hospitals.getJSONObject(i).getString("description");

                hospitalList.add(hospital);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return hospitalList;
    }

    private static String loadJsonFromAsset(String filename, Context context) {
        String json = null;

        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }
        catch (java.io.IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }



}
