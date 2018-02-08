package hansyuan.cruzrojamobile;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import hansyuan.cruzrojamobile.activities.HomeActivity;
import hansyuan.cruzrojamobile.models.Ambulance;
import hansyuan.cruzrojamobile.models.DispatcherCall;
import hansyuan.cruzrojamobile.models.LocationPoint;
import hansyuan.cruzrojamobile.mqtt.MqttClient;
import hansyuan.cruzrojamobile.services.GPSTracker;

import static hansyuan.cruzrojamobile.fragments.DispatcherFragment.updateAddress;
import static hansyuan.cruzrojamobile.activities.HomeActivity.updateStatus;
/**
 * Created by jkapi on 4/19/2017.
 *
 * To use a function:
 * If it is not being used in an activity page:
 * (((AmbulanceApp)AmbulanceApp.getAppContext()).FUNCTIONYOUNEED();
 * If it is being used in an activity page:
 * ((AmbulanceApp) this.getApplication()).FUNCTIONYOUNEED()
 *
 */

public class AmbulanceApp extends Application {

    //to skip login page_debug
    private boolean userLoggedIn = false;

    // Available List of Ambulances
    public ArrayList<Ambulance> ambulanceList;
    public static HashMap<Integer, String> hospitalMap;
    public static HashMap<Integer, ArrayList<String>> equipmentMap;
    private static Context appContext;
    private Context context;
    private String currStatus = "Idle";
    private String userId = "-1";
    private String userPw = "-1";
    public static String globalAddress = "No Address Received";
    private int id_Number = 14;
    private String license_Plate = "default_Plate";
    MqttClient mqttServer;
    Boolean authenticated;
    JSONObject GPSCoordinate;
    private LocationPoint lastKnownLocation;
    GPSTracker gpsTracker;
    JSONObject id_Object;

    private static final String TAG = HomeActivity.class.getSimpleName();

    public void updateLastKnownLocation(LocationPoint location) {
        lastKnownLocation = location;
        GPSCoordinate = new JSONObject();
        JSONObject newlocation = new JSONObject();
        System.err.println("updating JSON last known location");

        try {
            newlocation.put("latitude", ("" + location.getLatitude()));
            newlocation.put("longitude", ("" + location.getLongitude()));

            GPSCoordinate.put("location", newlocation);
            GPSCoordinate.put("timestamp", ("" + location.getTime()));
            Log.d("output", GPSCoordinate.toString(2));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onCreate() {
        super.onCreate();
        authenticated = false;
        appContext = getApplicationContext();
    }

    public AmbulanceApp onCreate (Context context) {
        super.onCreate();
        this.context = context;
        authenticated = false;
        appContext = getApplicationContext();

        //TODO Will probably have to pass credentials?
//        userId = "admin";
//        userPw = "cruzrojaadmin";

        //gpsTracker = new GPSTracker(appContext, 500, -1);
        //txtView = (TextView) ((Activity)context).findViewById(R.id.address);
        gpsTracker = new GPSTracker(appContext, 500 / 4, 0);

        Log.e("trakcer Created", "Tracker Created");
        LocationPoint loc = new LocationPoint(-192, 123);
        updateLastKnownLocation(loc);
        return this;
    }

    /*************************GETTERS **********************/
    public String getCurrStatus() {
        return currStatus;
    }
    public String getUserId(){return userId;}
    public String getUserPw() {return userPw;}
    public boolean getUserLoggedIn(){return userLoggedIn;}
    public static Context getAppContext() {
        return AmbulanceApp.appContext;
    }
    public boolean getAuthenticated() {return authenticated;}
    public int getUserIdNum(){return id_Number;}
    public JSONObject getGPSCoordinate(){return GPSCoordinate;}

    /******************** SETTERS *******************************/

    public void setCurrStatus(String newStatus) {
        currStatus = newStatus;
    }
    public void setUserId (String newId){userId = newId;}
    public void setUserPw(String newPw) {userPw = newPw;}
    public  void setUserLoggedIn(boolean newStatus){userLoggedIn = newStatus;}

    /********************* other methods ********************************/
    /** Takes in a string to Toast
     *
     * @param toToast
     */
    public void toasting(String toToast){
        CharSequence text = toToast;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(appContext, text, duration);
        toast.show();
    }

    /************************MQTT********************************/

    //MQTT
    public void mqttMaster() {
        Log.e("mqtt", "mqttmaster is called");
        mqttServer = MqttClient.getInstance(this);

        mqttServer.connect(userId, userPw, new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                if(reconnect) {
                    Log.e(TAG, "Reconnected to broker");
                } else {
                    Log.e(TAG, "Connected to broker");
                }
                //Connection is successful
                authenticated = true;

                //subscribe to topics
                mqttServer.subscribeToTopic("user/" + getUserId() + "/ambulances");
                mqttServer.subscribeToTopic("user/" + getUserId() + "/hospitals");
            }

            @Override
            public void connectionLost(Throwable cause) {
                //TODO Hnadle connection lost - Devin?
                //Should we manually pause subscribing and publishing?
                Log.d(TAG, "Connection to broker lost");
            }

            //receiving subscribed data
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String subsData = new String(message.getPayload());
                Log.e("MESSAGE ARRIVED", subsData);

                if (topic.contains("call")) {
                    JSONObject c = new JSONObject(subsData);
                    DispatcherCall dCall = new DispatcherCall(c);
                    globalAddress = dCall.getAddress();
                    updateAddress(globalAddress);
                    dispatchCallNotification();
                    Log.e(TAG, "Call message received: " + subsData);
                } else if (topic.contains("status")) {
                    Log.e(TAG, "Status message received: " + subsData);
                    currStatus = subsData;
                    updateStatus(currStatus);
                } else if (topic.contains("ambulances")) {
                    Log.d(TAG, "User message received: " + subsData);
                    JSONObject jsonObject = new JSONObject(subsData);
                    JSONArray ambulanceJSON = jsonObject.getJSONArray("ambulances");

                    ambulanceList = new ArrayList<>();
                    for (int i = 0; i < ambulanceJSON.length(); i++) {
                        JSONObject tempObject = ambulanceJSON.getJSONObject(i);
                        Ambulance ambulance = new Ambulance(tempObject.getInt("id"), tempObject.getString("license_plate"));
                        ambulanceList.add(ambulance);
                    }
                }
                if (topic.contains("hospitals")) {
                    //Log.e(TAG, "User message received: " + subsData);
                    JSONObject jsonObject = new JSONObject(subsData);
                    JSONArray hospitalJSON = jsonObject.getJSONArray("hospitals");

                    Log.e("HospitalJSON ---", jsonObject.toString());
                    Log.e("JSON Length", ""+hospitalJSON.length());

                    hospitalMap = new HashMap<Integer, String>();
                    equipmentMap = new HashMap<Integer, ArrayList<String>>();

                    for (int i = 0; i < hospitalJSON.length(); i++) {
                        JSONObject tempObject = hospitalJSON.getJSONObject(i);
                        Log.e("OBJECT: ", tempObject.toString());
                        int id = tempObject.getInt("id");
                        hospitalMap.put(id, tempObject.getString("name"));
                        mqttServer.subscribeToTopic("hospital/" + id + "/metadata");
                    }

                }
                if (topic.contains("metadata")){
                    JSONObject jsonObject = new JSONObject(subsData);

                    Log.e("METADATA JSON",jsonObject.toString());

                    JSONArray equipmentJSON = jsonObject.getJSONArray("equipment");
                    String delims = "[/]";
                    String[] tokens = topic.split(delims);
                    int idIdx = 1;
                    int id = Integer.parseInt(tokens[idIdx]);

                    ArrayList<String> equipList = new ArrayList<>();
                    // Subscribe to each element in the equipment list to get the value
                    for (int i = 0; i < equipmentJSON.length(); i++) {
                        JSONObject tempObject = equipmentJSON.getJSONObject(i);
                        String name = tempObject.getString("name");
                        mqttServer.subscribeToTopic("hospital/" + id + "/equipment/" + name);

                        equipList.add(name);
                    }

                    equipmentMap.put(id,equipList);
                }

                if (topic.contains("equipment")){
                    Log.e("EQUIPMENT DATA--",topic);
                    String delims = "[/]";
                    String[] tokens = topic.split(delims);
                    int idIdx = 1;
                    int enIdx = 3;
                    int id = Integer.parseInt(tokens[idIdx]);
                    String equip = tokens[enIdx];

                    // Equipment Count // TODO this should be a number and a toggle
                    int count = Integer.parseInt(subsData);

                    ArrayList<String> e = equipmentMap.get(id);
                    e.set(e.indexOf(equip), equip + "/" + count);
                    equipmentMap.put(id, e);
                }
                //hospital/hosp_id/equipment/equipment_name
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                Log.d(TAG, "Message sent successfully");
            }
        });
    }


    /*START OF FILE I/O CODE*************************************************/
    boolean writableExternalStorage = false;
    boolean checked = false;
    //check if storage is writable
    public boolean isExternalStorageWritable() {
        checked = true;
        String state = Environment.getExternalStorageState();
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            writableExternalStorage = true;
            return true;
        }
        writableExternalStorage = false;
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
        String filename = point.toString() + ".txt";
        String string = point.getTime();

        Log.e("local file", "writing coordinates" + filename);

        FileOutputStream outputStream;

        if (checked == false )
            isExternalStorageWritable();
        if(writableExternalStorage) {
            System.err.println( "THE PERMISSION IS ... " + checkPermission());
            try {
                //FileWriter fw = new FileWriter(getLPStorageDir(filename));
                //  BufferedWriter bw = new BufferedWriter(fw);
                File file = getLPStorageDir(filename);

                //PrintWriter out = new PrintWriter(file);
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file));
                // PrintWriter out = new PrintWriter("/storage/emulated/0/Download/" + filename);

                out.write(filename.getBytes());
                out.close();

                //toasting("Wrote: " + filename);
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
        File path = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "LPs");
        //Files are an abstraction for both the PATH and the FILE
        //File path = new File("/sdcard/", "LPs");
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


    /*
        Logout(Justin)
    */
    public void logout(){
        Log.e(TAG, "Publish: " + -1 + "  ID_NUMBER: " + id_Number + "  USER_ID: " + userId);
        mqttServer.publish(-1, userId);
        userLoggedIn = false;
        mqttServer.disconnect();
    }

    /*
       function to publish the selected ambulance id to
       TODO publish the RETAIN Flag
    */
    public void publishAmbulanceID(int ambulanceID) {
        id_Number = ambulanceID;
        Log.e(TAG, "Publish: " + id_Number + "  ID_NUMBER: " + id_Number + "  USER_ID: " + userId);
        if(mqttServer != null) {
            mqttServer.publish(id_Number, userId);
        }
    }

    public void dispatchCallNotification() {
        //To be heads up , the process is the same but setPriority should be called with at leas
        //Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.siren);


        Intent intent = new Intent(appContext, HomeActivity.class);
        PendingIntent pi = PendingIntent.getActivity(appContext, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setContentTitle("DISPATCH CALL ARRIVED")
                .setContentText(globalAddress)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setFullScreenIntent(pi, true)
                .setVibrate(new long[]{Notification.DEFAULT_VIBRATE})
                .setPriority(Notification.PRIORITY_MAX);
                //.setSound(uri);


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

}
