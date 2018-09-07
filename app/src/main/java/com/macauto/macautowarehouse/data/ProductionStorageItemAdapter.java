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

public class ProductionStorageItemAdapter extends ArrayAdapter<ProductionStorageItem> {
    public static final String TAG = "PStorageItemAdapter";
    private LayoutInflater inflater;

    private Context mContext;

    private int layoutResourceId;
    private ArrayList<ProductionStorageItem> items;

    public ProductionStorageItemAdapter(Context context, int textViewResourceId,
                                          ArrayList<ProductionStorageItem> objects) {
        super(context, textViewResourceId, objects);
        this.layoutResourceId = textViewResourceId;
        this.items = objects;
        this.mContext = context;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return items.size();

    }

    public ProductionStorageItem getItem(int position)
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

        ProductionStorageItem productionStorageItem = items.get(position);
        if (productionStorageItem != null) {

            holder.textViewIndex.setText(String.valueOf(position+1));
            String topString = mContext.getResources().getString(R.string.production_storage_part_no)+" "+productionStorageItem.getPart_no();
            String centerString = mContext.getResources().getString(R.string.production_storage_qty)+" "+productionStorageItem.getQty()+" "+productionStorageItem.getStock_unit();
            String bottomString = mContext.getResources().getString(R.string.production_storage_locate_no_def)+" "+productionStorageItem.getLocate_no()+"     "+
                    mContext.getResources().getString(R.string.production_storage_locate_no)+" "+productionStorageItem.getLocate_no_scan();
            holder.textViewTop.setText(topString);
            holder.textViewCenter.setText(centerString);
            holder.textViewBottom.setText(bottomString);
            //holder.itemTitle.setText(allocationMsgStatusItem.getItem_SFA03());
            //holder.itemDate.setText(allocationMsgItem.getDate());


            if (productionStorageItem.isSelected()) {
                //Log.e(TAG, ""+position+" is selected.");
                //view.setSelected(true);
                view.setBackgroundColor(Color.rgb(0x4d, 0x90, 0xfe));
            } else {
                //Log.e(TAG, ""+position+" clear.");
                //view.setSelected(false);
                view.setBackgroundColor(Color.TRANSPARENT);
            }





        }
        return view;
    }



    private class ViewHolder  {
        TextView textViewIndex;
        TextView textViewTop;
        TextView textViewCenter;
        TextView textViewBottom;

        private ViewHolder(View view) {
            this.textViewIndex = view.findViewById(R.id.productItemId);
            this.textViewTop = view.findViewById(R.id.productItemtitle);
            this.textViewCenter = view.findViewById(R.id.productItemDecrypt);
            this.textViewBottom = view.findViewById(R.id.productItemCount);
        }
    }
}
