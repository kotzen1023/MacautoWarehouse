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

public class SearchDetailItemAdapter extends ArrayAdapter<SearchDetailItem> {
    private LayoutInflater inflater;

    private int layoutResourceId;
    private ArrayList<SearchDetailItem> items;
    //private Context context;

    public SearchDetailItemAdapter(Context context, int textViewResourceId,
                                       ArrayList<SearchDetailItem> objects) {
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

    public SearchDetailItem getItem(int position)
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

        SearchDetailItem searchDetailItem = items.get(position);
        if (searchDetailItem != null) {
            holder.itemHeader.setText(searchDetailItem.getHeader());
            holder.itemContent.setText(searchDetailItem.getContent());







        }
        return view;
    }

    private class ViewHolder {
        TextView itemHeader;
        TextView itemContent;


        private ViewHolder(View view) {
            this.itemHeader = view.findViewById(R.id.itemDetailHeader);
            this.itemContent = view.findViewById(R.id.itemDetailContent);
        }
    }
}
