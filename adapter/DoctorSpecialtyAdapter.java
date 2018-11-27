package com.devool.ucicareconnect.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.devool.ucicareconnect.R;
import com.devool.ucicareconnect.helper.DoctorSpecialtyHelper;

import java.util.ArrayList;
import java.util.List;

public class DoctorSpecialtyAdapter extends BaseAdapter implements Filterable{
    ArrayList<String> doctorSpecialtyHelper;
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<String> mOriginalValues;



    public DoctorSpecialtyAdapter(Context context, ArrayList<String> doctorSpecialtyHelper) {
        this.doctorSpecialtyHelper = doctorSpecialtyHelper;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return doctorSpecialtyHelper.size();
    }

    @Override
    public String getItem(int position) {
        return doctorSpecialtyHelper.get(position);
    }

    @Override
    public long getItemId(int position) {
        return doctorSpecialtyHelper.size();
    }

    private class ViewHolder {
        TextView textView;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {

            holder = new ViewHolder();
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.list_item, null);

            holder.textView = (TextView) convertView.findViewById(R.id.product_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(doctorSpecialtyHelper.get(position));
        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                doctorSpecialtyHelper = (ArrayList<String>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                List<String> FilteredArrList = new ArrayList<String>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<String>(doctorSpecialtyHelper); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String data = mOriginalValues.get(i);
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(data);
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}
