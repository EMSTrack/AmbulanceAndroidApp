package hansyuan.cruzrojamobile;

import android.location.Location;

import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by Hans on 10/29/2016.
 *
 * This class bundles information together to send out.
 * May need to add additional data types.
 *
 * Keep in mind that the strings should be matching the
 * agreed specification between the  server team and the
 * mobile team
 *
 * There should not be a no-args constructor.
 *
 * TODO
 * 1. Don't need to change everything to String. We may be able to use JSON objects directly.
 * Will need to confirm with server team.
 * 2. The creation of the LocationPoint should be saved into the phone.
 * 3. The creation of a Location Point should be pushed onto the stack.
 * We might need a separate toString method for the filenames.
 *
 */

class LocationPoint {
    //GPS
    double lon;
    double lat;
    //NEED TIME
    int time;
    String status;
    Calendar calendar;
    /**
     * Constructor for LocationPoint
     * @param location
     */
    public LocationPoint (Location location) {
        this.lon = location.getLongitude();
        this.lat = location.getLatitude();
        calendar = Calendar.getInstance();
        time = calendar.get(Calendar.SECOND);
    }

    /**
     * Constructor for LocationPoint.
     * @param lat
     * @param lon
     */
    public LocationPoint(double lon, double lat){
        this.lon = lon;
        this.lat = lat;
        calendar = Calendar.getInstance();
        time = calendar.get(Calendar.SECOND);
    }

    /**
     * Returns the string representation for this data type.
     * @return
     */
    @Override
    public String toString(){
        return "Longitude: " + lon + "\nLatitude: " + lat;
    }

    /**
     * This method will return a JSON Object.
     * When we decide if and how to use JSONs to transmit information, we could come
     * up with a format for the JSON object.
     * @return
     */
    public JSONObject getJSON(){
        return null;
    }


    //.equals(locationpoint) (checks if 2 locationPoints are the same)
    private boolean equals(LocationPoint otherLocation) {
        if (this.lon == otherLocation.lon && this.lat == otherLocation.lat) {
            return true;
        }
        return false;
    }

    //.within(locationpoint, int ) int = meters (checks this.locationpoint);
    private boolean within(LocationPoint otherLocation, int distance) {

        return true;
    }

 }
