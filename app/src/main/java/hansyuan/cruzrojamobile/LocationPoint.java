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
