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
        HashMap<Integer, ArrayList<String>> equip = AmbulanceApp.equipmentMap;

        if(hosp == null){
            return hospitalList;
        }
        for (HashMap.Entry<Integer, String> entry : hosp.entrySet()) {
            Integer key = entry.getKey();
            String name = entry.getValue();
            String desc = "";

            ArrayList<String> e = equip.get(key);

            for (int i = 0; i < e.size(); i++) {
                String delims = "[/]";
                String[] tokens = e.get(i).split(delims);
                int cnt = Integer.parseInt(tokens[1]);
                String curr = "";
                if (cnt != 0) {
                    curr = tokens[0] + ": " + cnt;
                }
                desc += curr;
                if (i != e.size() -1) {
                    desc += "\n";
                }
            }
            Hospital hospital = new Hospital(key, name, desc);
            hospitalList.add(hospital);
        }

        return hospitalList;
    }


}
