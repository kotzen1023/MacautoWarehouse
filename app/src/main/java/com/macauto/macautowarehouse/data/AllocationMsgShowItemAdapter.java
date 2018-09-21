package com.macauto.macautowarehouse.data;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.macauto.macautowarehouse.R;

import java.util.ArrayList;

public class AllocationMsgShowItemAdapter extends ArrayAdapter<AllocationMsgShowItem> {
    private LayoutInflater inflater;

    private int layoutResourceId;
    private ArrayList<AllocationMsgShowItem> items;
    //private Context context;

    public AllocationMsgShowItemAdapter(Context context, int textViewResourceId,
                                      ArrayList<AllocationMsgShowItem> objects) {
        super(context, textViewResourceId, objects);
        this.layoutResourceId = textViewResourceId;
        this.items = objects;
        //this.context = context;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return items.size();

    }

    public AllocationMsgShowItem getItem(int position)
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

        AllocationMsgShowItem allocationMsgShowItem = items.get(position);
        if (allocationMsgShowItem != null) {
            holder.itemHeader.setText(allocationMsgShowItem.getHeader());
            holder.itemContent.setText(allocationMsgShowItem.getContent());


            if (position == 7) {
                holder.itemContent.setTextColor(Color.RED);
            } else {
                holder.itemContent.setTextColor(Color.BLACK);
            }




        }
        return view;
    }

    private class ViewHolder {
        TextView itemHeader;
        TextView itemContent;


        private ViewHolder(View view) {
            this.itemHeader = view.findViewById(R.id.inspectedItemDetailHeader);
            this.itemContent = view.findViewById(R.id.inspectedItemDetailContent);
        }
    }
}
