package hansyuan.cruzrojamobile;

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
 *
 */

class LocationPoint {
    //GPS
    double lon;
    double lat;

    /**
     * Constructor for LocationPoint.
     *
     * @param lat
     * @param lon
     */
    public LocationPoint(double lon, double lat){
        this.lon = lon;
        this.lat = lat;
    }

    /**
     * Returns the string representation for this data type.
     * @return
     */
    @Override
    public String toString(){
        return "Longitude: " + lon + "\nLatitude: " + lat;
    }
 }
