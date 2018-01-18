package hansyuan.cruzrojamobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by rawaa_ali on 6/1/17.
 */

public class HospitalAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Hospital> mDataSource;

    public HospitalAdapter(Context context, ArrayList<Hospital> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mDataSource.get(groupPosition).getEquipment().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View view, ViewGroup parent) {

        HospitalEquipment equipment = (HospitalEquipment) getChild(groupPosition, childPosition);
        if (view == null) {
//            LayoutInflater infalInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(R.layout.equipment_list_body, null);
        }
        TextView equipmentItem = (TextView) view.findViewById(R.id.equipment_item);
        equipmentItem.setText(equipment.getEntry().trim());

        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        ArrayList<HospitalEquipment> equipmentList = mDataSource.get(groupPosition).getEquipment();
        return equipmentList.size();

    }

    @Override
    public Object getGroup(int groupPosition) {
        return mDataSource.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return mDataSource.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view,
                             ViewGroup parent) {

        Hospital headerInfo = (Hospital) getGroup(groupPosition);
        if (view == null) {
//            LayoutInflater inf = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(R.layout.equipment_list_header, null);
        }

        TextView heading = (TextView) view.findViewById(R.id.equipment_header);
        heading.setText(headerInfo.getName().trim());

        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
