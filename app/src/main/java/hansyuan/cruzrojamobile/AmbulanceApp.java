package hansyuan.cruzrojamobile;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by jkapi on 4/19/2017.
 *
 *
 * To use a function:
 * If it is not being used in an activity page:
 * (((AmbulanceApp)AmbulanceApp.getAppContext()).FUNCTIONYOUNEED();
 * If it is being used in an activity page:
 * ((AmbulanceApp) this.getApplication()).FUNCTIONYOUNEED()
 *
 *
 */

public class AmbulanceApp extends Application {

    private static Context context;
    private String currStatus;

    public void onCreate() {
        super.onCreate();
        AmbulanceApp.context = getApplicationContext();
    }

    /*************************GETTERS **********************/
    public String getCurrStatus() {
        return currStatus;
    }

    public static Context getAppContext() {
        return AmbulanceApp.context;
    }

    /******************** SETTERS *******************************/

    public void setCurrStatus(String newStatus) {
        currStatus = newStatus;
    }


    /********************* other methods ********************************/
    /** Takes in a string to Toast
     *
     * @param toToast
     */
    public void toasting(String toToast){
        CharSequence text = toToast;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /*START OF FILE I/O CODE*************************************************/

    //check if storage is writable
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }

        isExternalStorageWritable(); // I hope this isn't a badly recursive file.'
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


        String filename = point.getTime() + ".txt";
        String string = point.getTime();

        FileOutputStream outputStream;


        if(isExternalStorageWritable()) {

            System.err.println( "THE PERMISSION IS ... " + checkPermission());

            try {


//            FileWriter fw = new FileWriter(getLPStorageDir(filename));
                //  BufferedWriter bw = new BufferedWriter(fw);
                File file = getLPStorageDir(filename);

                //PrintWriter out = new PrintWriter(file);
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
                // PrintWriter out = new PrintWriter("/storage/emulated/0/Download/" + filename);

                out.write(filename.getBytes());
                out.close();

                toasting("Wrote: " + filename);
                System.err.println("exists: " + file.exists());
                System.err.println("path: " + file.getAbsolutePath());
                file.createNewFile();
                System.err.println("path: " + file.getParent());

                // Trying to make the file immediately available for Windows Explorer.
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(Uri.fromFile(file));
                context.sendBroadcast(intent);

            } catch (Exception e) {
                e.printStackTrace();
                toasting("Exception was thrown trying to write to storage.");

            }


        }
        else{
            toasting("not writable");

        }

    }
    /***********************END OF RAMMY CODE*****************************/
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission((Activity)context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
/*

KNOWN ISSUE. FILES WILL NOT SHOW UP IN PHONE FOR WINDOWS
UNTIL PHONE IS RESTARTED

UPDATE: THIS ISSUE IS FIXED.
(This comment is left for your entertainment)

from search:

Ok workaround for text files:
For every update i delete the log file and rewrite the whole
file new with the appended new data. Before rewriting i delete
the file from index and readd it after writing the updated file.
And now after unplugging and plug in the usb cable back again
my text file is updated. Now my application works.
Thanks Google.. Thanks for nothing!


 */

    public File getLPStorageDir(String filename) {
        // Get the directory for the user's public pictures directory.
        /*File path = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "LPs");*/
        //Files are an abstraction for both the PATH and the FILE
        File path = new File("/sdcard/", "LPs");
        File file = new File(path, filename);


        if (!path.mkdirs()) {
            System.err.println("THE DIRECTORY WAS NOT CREATED. ");
        }
        try {
            file.createNewFile();
        }
        catch (Exception f) {f.printStackTrace();}

        file.setWritable(true);
        System.err.println(" Can write? " + file.canWrite());
        return file;
    }

}
