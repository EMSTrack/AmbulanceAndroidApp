package hansyuan.cruzrojamobile;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import android.content.Intent;

import android.util.Log;
/**
 * Created by rawaa_ali on 6/1/17.
 */

public class Hospital {
    private int id;
    public ArrayList<String> equipment;
    public String name;
    public String description;

    public Hospital(int id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.description = desc;
    }

    public int getId() {
        return id;
    }

    public static ArrayList<Hospital> getHospitals() {
        final ArrayList<Hospital> hospitalList = new ArrayList<>();

        // Get the Ambulance List from the Extras
        HashMap<Integer, String> hosp = AmbulanceApp.hospitalMap;
        HashMap<Integer, String> equip = AmbulanceApp.equipmentMap;

        try {
        for (HashMap.Entry<Integer, String> entry : hosp.entrySet()) {
            Integer key = entry.getKey();
            String name = entry.getValue();
            String desc = "";


            JSONArray equipArray = new JSONArray(equip.get(key));

            for (int i = 0; i < equipArray.length(); i++) {
                JSONObject tempObject = equipArray.getJSONObject(i);
                if (tempObject.getBoolean("toggleable")) {
                    desc = desc + tempObject.getString("name");
                }
                if (i != equipArray.length() - 1) {
                    desc += ", ";
                }
            }
            Hospital hospital = new Hospital(key, name, desc);
            hospitalList.add(hospital);
        }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return hospitalList;
    }


}
