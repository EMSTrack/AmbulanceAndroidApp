package hansyuan.cruzrojamobile;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.location.Location.distanceBetween;

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
 */

class LocationPoint {
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
        this.lon = lon;
        this.lat = lat;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        time = simpleDateFormat.format(new Date());
    }


    /**
     * Returns the string representation for this data type.
     * @return
     */
    @Override
    public String toString(){
        return "Longitude: " + lon + "\nLatitude: " + lat;
    }

    /** Name: setTime
     * sets 'time' to current time
     */
    private void setTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        time = simpleDateFormat.format(new Date());
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
     * is within  'distance' of this LocationPoint. Calls on helper function.
     */
    private boolean within(LocationPoint otherLocation, double distance) {
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
        return distance;
    }


}
