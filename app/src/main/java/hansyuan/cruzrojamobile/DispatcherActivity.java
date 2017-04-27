package hansyuan.cruzrojamobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by justingil1748 on 4/26/17.
 */

public class DispatcherActivity extends Fragment implements View.OnClickListener {
    View rootView;
    GPSTracker gps;
    Button mapButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_dispatcher, container, false);
        mapButton = (Button) rootView.findViewById(R.id.gmap);
        mapButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v == mapButton){
            gps = new GPSTracker(rootView.getContext());
            gps.getLastKnownLocationIfAllowed();

            if(gps.isGPSEnabled() || gps.isNetworkEnabled()){
                Toast.makeText(getActivity(),"Can get location",Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getActivity(),"can't get location",Toast.LENGTH_LONG).show();
            }

            gps.getLocation();

            double lat = gps.getLatitude(); // returns latitude
            double lon = gps.getLongitude(); // returns longitude
            LocationPoint loc = new LocationPoint(lon, lat);


            String geoUri = "http://maps.google.com/maps?q=loc:" + 32.879409 + "," + -117.2382162 + " (" + loc + ")";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
            getActivity().startActivity(intent);
        }
    }


}
