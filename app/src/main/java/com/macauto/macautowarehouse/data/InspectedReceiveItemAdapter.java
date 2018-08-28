package com.macauto.macautowarehouse.data;

import android.content.Context;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.TextView;

import com.macauto.macautowarehouse.R;


import java.util.ArrayList;

public class InspectedReceiveItemAdapter extends ArrayAdapter<InspectedReceiveItem> {
    private LayoutInflater inflater;

    private int layoutResourceId;
    private ArrayList<InspectedReceiveItem> items;

    public InspectedReceiveItemAdapter(Context context, int textViewResourceId,
                            ArrayList<InspectedReceiveItem> objects) {
        super(context, textViewResourceId, objects);
        this.layoutResourceId = textViewResourceId;
        this.items = objects;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return items.size();

    }

    public InspectedReceiveItem getItem(int position)
    {
        return items.get(position);
    }
    @Override
    public @NonNull
    View getView(int position, View convertView, @NonNull ViewGroup parent) {

        //Log.e(TAG, "getView = "+ position);
        View view;
        ViewHolder holder;
        if (convertView == null || convertView.getTag() == null) {
            //Log.e(TAG, "convertView = null");
            /*view = inflater.inflate(layoutResourceId, null);
            holder = new ViewHolder(view);
            view.setTag(holder);*/

            //LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layoutResourceId, null);
            holder = new ViewHolder(view);
            //holder.checkbox.setVisibility(View.INVISIBLE);
            view.setTag(holder);
        }
        else {
            view = convertView ;
            holder = (ViewHolder) view.getTag();
        }

        //holder.fileicon = (ImageView) view.findViewById(R.id.fd_Icon1);
        //holder.filename = (TextView) view.findViewById(R.id.fileChooseFileName);
        //holder.checkbox = (CheckBox) view.findViewById(R.id.checkBoxInRow);

        InspectedReceiveItem inspectedReceiveItem = items.get(position);
        if (inspectedReceiveItem != null) {
            holder.itemName.setText("");









        }
        return view;
    }

    private class ViewHolder {
        TextView itemTitle;
        TextView itemName;


        private ViewHolder(View view) {
            this.itemTitle = view.findViewById(R.id.itemTitle);
            this.itemName = view.findViewById(R.id.itemName);
        }
    }
}
