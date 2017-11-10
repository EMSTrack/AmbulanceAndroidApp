package hansyuan.cruzrojamobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by rawaa_ali on 6/1/17.
 */

public class HospitalAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Hospital> mDataSource;

    public HospitalAdapter(Context context, ArrayList<Hospital> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //1
    @Override
    public int getCount() {
        return mDataSource.size();
    }

    //2
    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }

    //4
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.hospital_item, parent, false);
        // Get title element
        TextView titleTextView =
                (TextView) rowView.findViewById(hansyuan.cruzrojamobile.R.id.hospital_list_title);

        // Get subtitle element
        TextView subtitleTextView =
                (TextView) rowView.findViewById(hansyuan.cruzrojamobile.R.id.hospital_list_subtitle);


        Hospital hospital = (Hospital) getItem(position);

        titleTextView.setText(hospital.name);
        subtitleTextView.setText(hospital.description);



        return rowView;
    }
}
