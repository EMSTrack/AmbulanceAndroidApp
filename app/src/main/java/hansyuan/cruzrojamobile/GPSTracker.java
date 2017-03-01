package hansyuan.cruzrojamobile;

/**
 *
 * Created by Hans Yuan on 10/19/2016.
 *
 * Java Class Only:
 * Purpose is to get the GPS information from Android.
 * Use by creating a new instance of GPSTracker. This
 * is done by the GPS class.
 */

import android.Manifest;
import android.app.Activity;
import android.app.Service;

import java.io.FileOutputStream;
import java.util.Calendar; //needed for testing how calendar works
//package com.example.gpstracking;

        import android.app.AlertDialog;
        import android.app.Service;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
        import android.location.LocationListener;
        import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
        import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

public class GPSTracker extends Service implements LocationListener {
    private static LocationManager m_locationManager;
    private Context mContext;
    private static String provider;
    private static final int REQUEST_COARSE_LOCATION = 999;
    private static final int REQUEST_FINE_LOCATION = 998;
    private final int DISTANCE = 5;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    // Declaring a Location Manager
    protected LocationManager locationManager;

    //Used in LocationListener to check whether to add a new locationPoint
    LocationPoint lastKnownLocation;



    public GPSTracker(Context context) {
        this.mContext = context;


        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {

            provider = LocationManager.GPS_PROVIDER;
            m_locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            m_locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            m_locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        }
        getLocation();
        toasting("CREATED GPSTRACKER");
    }

    /** Name: getLastKnowLocation
     *  Returns LocationPoint lastKnownLocation
     */
    public LocationPoint getLastKnownLocation() {
        return lastKnownLocation;
    }

    /**
     * http://stackoverflow.com/questions/33562951/android-6-0-location-permissions
     *
     * This method resolves operating system version differences when requesting
     * permission for locations.
     */
    public LocationPoint getLastKnownLocationIfAllowed() {

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {

            provider = LocationManager.GPS_PROVIDER;
            m_locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            Location location = m_locationManager.getLastKnownLocation(provider);
            System.out.println("\nGET LAST KNOWN LOCATION");
            if (location == null ) {
                return null;
            }
            lastKnownLocation = new LocationPoint (location);
            return lastKnownLocation;
        }

        return null;
    }



    /**
     * Initializes needed variables, checks if
     * @return
     */

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            /** Test */
            if (getLastKnownLocationIfAllowed() == null) {

                ActivityCompat.requestPermissions(
                        (Activity) mContext,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_FINE_LOCATION);
            }


            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                this.canGetLocation = false;
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                try {
                    if (isNetworkEnabled) {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("Network", "Network");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                    /** Adding code to check for location permission*/
                    int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                    if (currentapiVersion >= Build.VERSION_CODES.M){
                        //Request Permission at Runtime!




                    } else{
                        // do something for phones running an SDK before lollipop
                    }

                    // TODO This is the original isGPSEnabled if-statement.
                    if (isGPSEnabled) {
                        if (location == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }
                    /* TODO this code is for higher level APIs...
                    TODO Included just in case...
                    // if GPS Enabled get lat/long using GPS Services
                    if (isGPSEnabled) {
                        if (location == null) {
                            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS);
                            }
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }
                    */
                } catch(SecurityException e) {
                    //error
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     * */
    public void stopUsingGPS(){
        if(locationManager != null){
            try {
                locationManager.removeUpdates(GPSTracker.this);
            } catch (SecurityException e) {
                //Fix later
            }
        }
    }

    /**
     * Function to get latitude
     * */
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    public void toasting(String toToast){
        CharSequence text = toToast;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(mContext, text, duration);
        toast.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        toasting("ON LOCATION CHANGED");
        if (location == null) {
            toasting("onLocationChanged, location is null");
        }
        LocationPoint newLocation = new LocationPoint(location);
        //check current location with last location
        if (!newLocation.within(lastKnownLocation, DISTANCE)) {
            return;
        }
        lastKnownLocation = newLocation;
        System.out.println("\n LOCATION IS BEING WRITTEN\n");
        toasting("LOCATION IS BEING WRITTEN");
        writeLocationsToFile(newLocation);
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    

/*START OF FILE I/O CODE*************************************************/


//check if storage is writerable
public boolean isExternalStorageWritable() {
    String state = Environment.getExternalStorageState();
    if (Environment.MEDIA_MOUNTED.equals(state)) {
        return true;
    }
    return false;
}
//printerwrite, andorid equivilant
/* Checks if external storage is available to at least read */
public boolean isExternalStorageReadable() {
    String state = Environment.getExternalStorageState();
    if (Environment.MEDIA_MOUNTED.equals(state) ||
            Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
        return true;
    }
    return false;
}

//Method to write locatoins points to external stoage
//parameters: locationPointer point: object, carrying time of location
public void writeLocationsToFile( LocationPoint point ){
    //write to file i/o and must figure out whether to add to stack
    //or have julia add it to mainactivity.buffstack, as well as
    //gettime() instead of to string once i merge
    String filename = point.getTime();
    String string = point.getTime();
    FileOutputStream outputStream;

    if(isExternalStorageWritable()) {
        toasting("Written.");
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    else{
        toasting("Not writable");
    }

}
/***********************END OF RAMMY CODE*****************************/


}