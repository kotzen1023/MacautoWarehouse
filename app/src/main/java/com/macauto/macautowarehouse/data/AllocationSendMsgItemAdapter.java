package com.macauto.macautowarehouse.data;

import android.content.Context;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.macauto.macautowarehouse.R;

import java.util.ArrayList;


import static com.macauto.macautowarehouse.AllocationSendMsgToReserveWarehouseFragment.dateAdapter;
import static com.macauto.macautowarehouse.AllocationSendMsgToReserveWarehouseFragment.hourAdapter;
import static com.macauto.macautowarehouse.AllocationSendMsgToReserveWarehouseFragment.locateAdapter;
import static com.macauto.macautowarehouse.AllocationSendMsgToReserveWarehouseFragment.minAdapter;

public class AllocationSendMsgItemAdapter extends ArrayAdapter<AllocationSendMsgItem> {
    //private static final String TAG = "ASendMsgAdapter";
    private LayoutInflater inflater;

    private int layoutResourceId;
    private ArrayList<AllocationSendMsgItem> items;
    private Context context;

    public AllocationSendMsgItemAdapter(Context context, int textViewResourceId,
                                          ArrayList<AllocationSendMsgItem> objects) {
        super(context, textViewResourceId, objects);
        this.layoutResourceId = textViewResourceId;
        this.items = objects;
        this.context = context;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return items.size();

    }

    public AllocationSendMsgItem getItem(int position)
    {
        return items.get(position);
    }
    @Override
    public @NonNull
    View getView(final int position, View convertView, @NonNull ViewGroup parent) {

        //Log.e(TAG, "getView = "+ position);
        View view;
        final ViewHolder holder;
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

        final AllocationSendMsgItem allocationSendMsgItem = items.get(position);
        if (allocationSendMsgItem != null) {
            holder.index = position;
            holder.itemHeader.setText(allocationSendMsgItem.getHeader());
            holder.itemContent.setText(allocationSendMsgItem.getContent());
            holder.itemEditText.setText(allocationSendMsgItem.getContent());


            allocationSendMsgItem.setTextView(holder.itemContent);
            allocationSendMsgItem.setEditText(holder.itemEditText);
            allocationSendMsgItem.setSpinner(holder.itemSpinner);



            if (allocationSendMsgItem.getHeader().equals(context.getResources().getString(R.string.allocation_send_message_to_material_work_order)) ||
                    allocationSendMsgItem.getHeader().equals(context.getResources().getString(R.string.allocation_send_message_to_material_staging_area)) ||
                    allocationSendMsgItem.getHeader().equals(context.getResources().getString(R.string.allocation_send_message_to_material_rate)))
            {
                holder.itemEditText.setVisibility(View.VISIBLE);
                holder.itemContent.setVisibility(View.GONE);
                holder.itemSpinner.setVisibility(View.GONE);
            } else if (allocationSendMsgItem.getHeader().equals(context.getResources().getString(R.string.allocation_send_message_to_material_stock_locate)) ||
                    allocationSendMsgItem.getHeader().equals(context.getResources().getString(R.string.allocation_send_message_to_material_date_year_month_day)) ||
                    allocationSendMsgItem.getHeader().equals(context.getResources().getString(R.string.allocation_send_message_to_material_date_hour)) ||
                    allocationSendMsgItem.getHeader().equals(context.getResources().getString(R.string.allocation_send_message_to_material_date_minute))) {
                holder.itemSpinner.setVisibility(View.VISIBLE);
                holder.itemContent.setVisibility(View.GONE);
                holder.itemEditText.setVisibility(View.GONE);

                /*Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MINUTE, 30);
                Date today = calendar.getTime();
                calendar.setTime(today);
                int hours = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);*/

                if (allocationSendMsgItem.getHeader().equals(context.getResources().getString(R.string.allocation_send_message_to_material_date_year_month_day))) {

                    holder.itemSpinner.setAdapter(dateAdapter);
                } else if (allocationSendMsgItem.getHeader().equals(context.getResources().getString(R.string.allocation_send_message_to_material_date_hour))) {
                    holder.itemSpinner.setAdapter(hourAdapter);
                    //holder.itemSpinner.setSelection(hours);
                } else if (allocationSendMsgItem.getHeader().equals(context.getResources().getString(R.string.allocation_send_message_to_material_date_minute))) {
                    holder.itemSpinner.setAdapter(minAdapter);
                    //holder.itemSpinner.setSelection(minutes);
                } else if (allocationSendMsgItem.getHeader().equals(context.getResources().getString(R.string.allocation_send_message_to_material_stock_locate))) {
                    holder.itemSpinner.setAdapter(locateAdapter);
                    //holder.itemSpinner.setSelection(minutes);
                }
            } else if (allocationSendMsgItem.getHeader().equals(context.getResources().getString(R.string.allocation_send_message_to_material_predict_production_quantity)) ||
                    allocationSendMsgItem.getHeader().equals(context.getResources().getString(R.string.allocation_send_message_to_material_real_production_quantity))) {
                //view.setBackgroundColor(Color.rgb(0xff, 0xd6, 0x00));
                holder.itemContent.setVisibility(View.VISIBLE);
                holder.itemEditText.setVisibility(View.GONE);
                holder.itemSpinner.setVisibility(View.GONE);

                holder.itemContent.setBackgroundColor(Color.rgb(0xff, 0xd6, 0x00));
            } else {
                holder.itemContent.setVisibility(View.VISIBLE);
                holder.itemEditText.setVisibility(View.GONE);
                holder.itemSpinner.setVisibility(View.GONE);
            }

            for (int i=0; i<holder.itemSpinner.getCount(); i++) {
                if (allocationSendMsgItem.getContent().equals(holder.itemSpinner.getItemAtPosition(i))) {
                    holder.itemSpinner.setSelection(i);
                    break;
                }
            }

            holder.itemEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    //Log.e(TAG, "item[index: "+holder.index+", position: "+position+"] change to "+s.toString());

                    if (holder.index == position) {
                        items.get(position).setContent(s.toString());
                    }
                }
            });

            holder.itemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int item_position, long id) {
                    //Log.e(TAG, "item[index: "+holder.index+", position: "+position+"] change to "+holder.itemSpinner.getSelectedItem().toString());
                    //allocationSendMsgItem.setContent(this.toString());



                    if (holder.index == position) {
                        items.get(position).setContent(holder.itemSpinner.getSelectedItem().toString());
                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
        return view;
    }

    private class ViewHolder {
        int index;
        TextView itemHeader;
        TextView itemContent;
        EditText itemEditText;
        Spinner itemSpinner;


        private ViewHolder(View view) {
            this.itemHeader = view.findViewById(R.id.itemHeaderTextView);
            this.itemContent = view.findViewById(R.id.itemContentTextView);
            this.itemEditText = view.findViewById(R.id.itemContentEditText);
            this.itemSpinner = view.findViewById(R.id.itemContentSpinner);
        }
    }
}
