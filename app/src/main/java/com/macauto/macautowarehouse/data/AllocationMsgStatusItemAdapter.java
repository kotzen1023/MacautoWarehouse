package com.macauto.macautowarehouse.data;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.macauto.macautowarehouse.LookupInStockDetailActivity;
import com.macauto.macautowarehouse.R;

import java.util.ArrayList;

import static com.macauto.macautowarehouse.LookupInStockFragment.isSorted;
import static com.macauto.macautowarehouse.MainActivity.sortedSearchList;

public class AllocationMsgStatusItemAdapter extends ArrayAdapter<AllocationMsgStatusItem> {
    public static final String TAG = "StatusItemAdapter";
    private LayoutInflater inflater = null;

    private Context mContext;

    private int layoutResourceId;
    private ArrayList<AllocationMsgStatusItem> items = new ArrayList<>();

    public AllocationMsgStatusItemAdapter(Context context, int textViewResourceId,
                                ArrayList<AllocationMsgStatusItem> objects) {
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

    public AllocationMsgStatusItem getItem(int position)
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

        AllocationMsgStatusItem allocationMsgStatusItem = items.get(position);
        if (allocationMsgStatusItem != null) {

            holder.textViewIndex.setText(String.valueOf(position+1));
            holder.textViewTop.setText(items.get(position).getItem_SFA03());
            holder.textViewCenter.setText(items.get(position).getItem_IMA021());


            float aw1_float = Float.valueOf(items.get(position).getItem_IMG10());
            int aw1 = (int) aw1_float;
            int aw2 = Integer.valueOf(items.get(position).getItem_MOVED_QTY());
            int aw3 = Integer.valueOf(items.get(position).getItem_SFA05());
            int aw4 = Integer.valueOf(items.get(position).getItem_TC_OBF013());
            float aw5_float = Float.valueOf(items.get(position).getItem_MESS_QTY());
            int aw5 = (int)aw5_float;

            if (aw1 > (aw3 - aw2 - aw5)) {
                aw1 = aw3 - aw2 - aw5;
                aw1 = aw1 < 0 ? 0 : aw1;
            }

            aw1 = aw1 > aw4 ? aw4 : aw1;
            holder.textViewBottom.setText(mContext.getResources().getString(R.string.allocation_send_message_to_material_status_detail_IMG10)+ " " +String.valueOf(aw1));
            //holder.itemTitle.setText(allocationMsgStatusItem.getItem_SFA03());
            //holder.itemDate.setText(allocationMsgItem.getDate());


            if (allocationMsgStatusItem.isSelected()) {
                //Log.e(TAG, ""+position+" is selected.");
                //view.setSelected(true);
                view.setBackgroundColor(Color.rgb(0x4d, 0x90, 0xfe));
            } else {
                //Log.e(TAG, ""+position+" clear.");
                //view.setSelected(false);
                view.setBackgroundColor(Color.rgb(0xF1, 0x8D, 0x0A));
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
            this.textViewIndex = view.findViewById(R.id.statusItemId);
            this.textViewTop = view.findViewById(R.id.statusItemtitle);
            this.textViewCenter = view.findViewById(R.id.statusItemDecrypt);
            this.textViewBottom = view.findViewById(R.id.statusItemCount);
        }
    }
}
