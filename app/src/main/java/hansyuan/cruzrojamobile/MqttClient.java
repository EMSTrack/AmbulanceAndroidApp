package hansyuan.cruzrojamobile;

import android.content.Context;
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
import org.json.JSONObject;

/**
 * Created by Fabian Choi on 5/12/2017.
 * Singleton that connects to the Mqtt broker.
 */
public class MqttClient {
    private static final String TAG = MqttClient.class.getSimpleName();
    private static MqttClient instance;
    private static Context context;

    private MqttAndroidClient mqttClient;
    private final String serverUri = "ssl://cruzroja.ucsd.edu:8884";
    private String clientId = "AmbulanceClient-";

    //call mqtt?
    private MqttClient(Context context) {
        MqttClient.context = context;
        clientId += System.currentTimeMillis();

        // Initialize the client
        mqttClient = new MqttAndroidClient(context, serverUri, clientId);
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
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());

        try {
            mqttClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.d(TAG, "Connection to broker successful as clientId = " + clientId);
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttClient.setBufferOpts(disconnectedBufferOptions);
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
                    Log.d(TAG, "Subscribed to " + topic + " successfully");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d(TAG, "Failed to subscribe to " + topic);
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

    public void publish(JSONObject content){
        MqttMessage message = new MqttMessage(content.toString().getBytes());

        try {
            mqttClient.publish("user/1/location", message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}
