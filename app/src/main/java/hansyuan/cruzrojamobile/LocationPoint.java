package hansyuan.cruzrojamobile;

import android.content.Context;
import android.location.Location;
import java.text.SimpleDateFormat; //used for time
import java.util.Date; //used for 'time'
import static android.location.Location.distanceBetween; //used for distanceBetween
import org.json.JSONObject;



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
    String name;
    //GPS
    double lon;
    double lat;
    //NEED TIME
    String time;
    String status;

    /**
     * Constructor for LocationPoint
     * @param location
     */
    public LocationPoint (Location location) {
        name = "";
        this.lon = location.getLongitude();
        this.lat = location.getLatitude();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        String format = simpleDateFormat.format(new Date());
    }

    /**
     * Constructor for LocationPoint.
     * @param lat
     * @param lon
     */
    public LocationPoint(double lon, double lat){
        name = "";
        this.lon = lon;
        this.lat = lat;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        time = simpleDateFormat.format(new Date());
    }


    /** Name: setName
     * @param newName
     * Sets the locationPoint's name
     */
    public void setName(String newName) {
        name = newName;
    }


    /**
     * Returns the string representation for this data type.
     * @return
     */
    @Override
    public String toString(){
        return "Name: " + name + " Time: " + time + " Longitude: " + lon + " Latitude: " + lat;
    }

    public String getTime() {
            setTime();
        return time;
    }

    public String getName() {
        return name;
    }

    public String getCoordinates () {
        return "Longitude: " + lon + "Latitude: " + lat;
    }

    /** Name: setTime
     * sets 'time' to current time
     */
    private void setTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        time = simpleDateFormat.format(new Date());
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



    /** Name: .equals
     * @param otherLocation
     * (checks if 2 locationPoints are the same)
     */
    private boolean equals(LocationPoint otherLocation) {
        if (this.lon == otherLocation.lon && this.lat == otherLocation.lat) {
            return true;
        }
        return false;
    }


    /** Name: .within
     * @param otherLocation
     * @param distance
     * Returns a boolean. Sets boolean to true if otherlocation LocationPoint
     * is within  'distance' meters of this LocationPoint. Calls on helper function.
     */
    public boolean within(LocationPoint otherLocation, double distance) {
        if (otherLocation == null)
            return false;

        double within = distanceBetweenTwoPlaces(this, otherLocation);


        if (within <= distance) {
            return true;
        } else {
            return false;
        }
    }


    float[] results = new float[3]; //used for distanceBetween as arg
    private final double MIN_DIST = 160; // 160m = 1/10th mile

    /**
     * Name: distanceBetweenTwoPlaces
     * @param p1
     * @param ourLocation
     * @return double that is the distance between the two locations in meters
     */
    public double distanceBetweenTwoPlaces(LocationPoint ourLocation, LocationPoint p1) {
        double distance = MIN_DIST + 1;
        distanceBetween(p1.lat, p1.lon, ourLocation.lat, ourLocation.lon, results);
        distance = results[0];
        System.out.println("\nDISTANCE BETWEEN THE TWO LOCATIONS:" + distance);
        GPSTracker.toasting2("DISTANCE BETWEEN THE TWO LOCATIONS:" + distance);
        return distance;
    }


}
