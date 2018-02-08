package hansyuan.cruzrojamobile.models;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;

import hansyuan.cruzrojamobile.AmbulanceApp;

/**
 * Created by rawaa_ali on 6/1/17.
 */

public class Hospital {
    private int id;
    private ArrayList<HospitalEquipment> equipmentList;
    private String name;

    public Hospital(int id, String name) {
        this.id = id;
        this.name = name;
        equipmentList = new ArrayList<>();

        Log.e("Hospital_Created", "ID: " + id);
        Log.e("Hospital_Created", "Name: " + name);
    }

    public int getId() {
        return id;
    }

    public ArrayList<HospitalEquipment> getEquipment(){
        return equipmentList;
    }

    public void addEquipment(HospitalEquipment equipment) {

        equipmentList.add(equipment);

    }

    public String getName() {
        return name;
    }


    // Create the list of Hospitals and their equipment based on the maps created from the server in AmbulanceApp.java
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
            Hospital hospital = new Hospital(key, name);

            ArrayList<String> e = equip.get(key);

            if(e == null){
                return hospitalList;
            }

            for (int i = 0; i < e.size(); i++) {
                String delims = "[/]";
                String[] tokens = e.get(i).split(delims);

                HospitalEquipment hospitalEquipment = new HospitalEquipment(tokens[0], tokens[1]);
                hospital.addEquipment(hospitalEquipment);
            }



            hospitalList.add(hospital);
        }

        return hospitalList;

    }


}
