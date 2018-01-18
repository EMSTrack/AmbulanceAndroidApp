package hansyuan.cruzrojamobile;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Spinner;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.logging.Logger;
import org.json.JSONObject;

/**
 * Created by Fabian Choi on 5/12/2017.
 * Singleton that connects to the Mqtt broker.
 */
public class MqttClient {
    private static final String TAG = MqttClient.class.getSimpleName();
    // TODO Leak in static context
    private static MqttClient instance;
    private static Context context;
    private Context mContext;
    public AmbulanceApp ambulance;


    private MqttAndroidClient mqttClient;
    private final String serverUri = "ssl://cruzroja.ucsd.edu:8883";
    private String clientId = "AmbulanceClient-";

    //call mqtt?
    private MqttClient(Context context) {
        mContext = context;

        MqttClient.context = context;
        clientId += System.currentTimeMillis();

        // Initialize the client
        mqttClient = new MqttAndroidClient(context, serverUri, clientId);
        ambulance = ((AmbulanceApp) mContext);
    }

    /**
     * Connect the client to the broker with a username and password.
     * To reset the callback, use the setCallback function instead.
     * @param username Username
     * @param password Password
     * @param callback Actions to complete on connection
     */
    public void connect(String username, String password, final MqttCallbackExtended callback) {
        // Set callback options
        mqttClient.setCallback(callback);

        // Set client options
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);

        Log.e(TAG, "----Username: "+username);
        Log.e(TAG, "----Password: "+password);

        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());

        try {
            mqttClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.e(TAG, "Connection to broker successful as clientId = " + clientId);
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttClient.setBufferOpts(disconnectedBufferOptions);

                    //Log.e(TAG, "Ambulance ID Message received: ");
                    subscribeToTopic("ambulance/" + ambulance.getUserIdNum() + "/status");
                    //Log.e(TAG, "Status Message received: ");
                    subscribeToTopic("ambulance/" + ambulance.getUserIdNum() + "/call");
                    //Log.e(TAG, "Dispatch Message received: ");

                    publish(ambulance.getGPSCoordinate(), ambulance.getUserId());
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG, "Connection to broker failed");
                    Log.e(TAG, exception.getMessage());
                }
            });

        } catch (MqttException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Set the callback of the Mqtt client
     * @param callback Callback info
     */
    public void setCallback(final MqttCallback callback) {
        mqttClient.setCallback(callback);
    }

    /**
     * Subscribe to a topic
     * @param topic Topic to subscribe
     */
    public void subscribeToTopic(final String topic) {
        try {
            mqttClient.subscribe(topic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.e(TAG, "Subscribed to " + topic + " successfully");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e(TAG, "Failed to subscribe to " + topic);
                }
            });
        } catch (MqttException ex){
            System.err.println("Exception whilst subscribing");
            ex.printStackTrace();
        }
    }

    /**
     * Subscribe to a topic with callback
     * @param topic Topic to subscribe
     * @param callback Callback
     */
    public void subscribeToTopic(final String topic, IMqttActionListener callback) {
        try {
            mqttClient.subscribe(topic, 0, null, callback);
        } catch (MqttException ex){
            System.err.println("Exception whilst subscribing");
            ex.printStackTrace();
        }
    }

    /**
     * Returns existing instance or creates if non exist
     * @param context Context
     * @return Existing or new instance of the client
     */
    public static synchronized MqttClient getInstance(Context context) {
        if(instance == null) {
            instance = new MqttClient(context);
        }
        return instance;
    }

    public void publish(int id, String username){
        Log.e("Published data", String.valueOf(id));
        MqttMessage message = new MqttMessage(String.valueOf(id).getBytes());

        try {
            mqttClient.publish("user/" + username +"/ambulance", message);
            Log.e("Publish_id", "Success");
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public void publish(JSONObject content, String username){
        Log.e("Published data", content.toString());
        MqttMessage message = new MqttMessage(content.toString().getBytes());

        if (content.has("id")) {
            try {
                mqttClient.publish("user/" + username +"/ambulance", message);
                Log.e("Publish_id", "Success");
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                mqttClient.publish("user/" + username + "/location", message);
                Log.e("Publish_location", "Success");
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean userLogin(String id, String pw) {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(id);
        options.setPassword(pw.toCharArray());

        boolean retVal = false;
        try {
            mqttClient.connect(options);
            retVal =  mqttClient.isConnected();

        } catch (MqttException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public void disconnect() {
        try {
            Log.e(TAG, "MqttClient disconnected");
            mqttClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}
