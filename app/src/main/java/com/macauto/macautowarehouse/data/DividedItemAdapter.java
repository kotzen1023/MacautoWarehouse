package com.macauto.macautowarehouse.data;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.macauto.macautowarehouse.R;

import java.util.ArrayList;

import static com.macauto.macautowarehouse.EnteringWarehouseDividedDialogActivity.dividedItemAdapter;
import static com.macauto.macautowarehouse.EnteringWarehouseDividedDialogActivity.dividedList;
import static com.macauto.macautowarehouse.EnteringWarehouseDividedDialogActivity.temp_count_list;


public class DividedItemAdapter extends ArrayAdapter<DividedItem> {
    //private static final String TAG = DividedItemAdapter.class.getName();
    private LayoutInflater inflater;
    private Context context;
    private int layoutResourceId;
    private ArrayList<DividedItem> items;
    //private static String textChangeBefore;
    //private static String textChangeAfter;
    //private int total_quantity;

    public DividedItemAdapter(Context context, int textViewResourceId,
                                       ArrayList<DividedItem> objects) {
        super(context, textViewResourceId, objects);
        this.layoutResourceId = textViewResourceId;
        this.items = objects;
        this.context = context;
        //this.total_quantity = total_quantity;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return items.size();

    }

    public DividedItem getItem(int position)
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

        DividedItem dividedItem = items.get(position);
        if (dividedItem != null) {
            holder.itemIndex.setText(String.valueOf(position+1));
            holder.itemQuantity.setText(String.valueOf(dividedItem.getQuantity()));

            dividedItem.setEdit(holder.itemQuantity);


            if (position == 0) {
                holder.itemDelete.setVisibility(View.INVISIBLE);
            } else {
                holder.itemDelete.setVisibility(View.VISIBLE);
                holder.itemDelete.setText(view.getResources().getString(R.string.delete));
            }

            holder.itemDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dividedList.remove(position);

                    temp_count_list.remove(position);

                    for (int i=0; i<dividedList.size();i++) {
                        dividedList.get(i).setQuantity(temp_count_list.get(i));
                    }

                    if (dividedItemAdapter != null)
                        dividedItemAdapter.notifyDataSetChanged();
                }
            });

            holder.itemQuantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent showIntent = new Intent(Constants.ACTION.ACTION_SHOW_VIRTUAL_KEYBOARD_ACTION);
                    context.sendBroadcast(showIntent);
                }
            });

            /*if (Integer.valueOf(holder.itemIndex.getText().toString()) == position+1) {

            }*/

            holder.itemQuantity.addTextChangedListener(new TextWatcher() {


                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    //Log.d(TAG, "beforeTextChanged: "+s.toString());
                    //textChangeBefore = s.toString();
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //Log.d(TAG, "s = "+s.toString()+", start "+start+" before = "+before+" count = "+count );


                }

                @Override
                public void afterTextChanged(Editable s) {
                    //Log.d(TAG, "position "+position+", afterTextChanged: "+s.toString());


                    if (Integer.valueOf(holder.itemIndex.getText().toString()) == position + 1) {
                        //Log.d(TAG, "position " + position + ", afterTextChanged: " + s.toString());

                        if (holder.itemQuantity.getText().length() > 0) {
                            temp_count_list.set(position, Integer.valueOf(holder.itemQuantity.getText().toString()));

                        } else {
                            temp_count_list.set(position, 0);
                        }

                        /*int count = 0;

                        for (int i=0;i<temp_count_list.size(); i++) {
                            count+= temp_count_list.get(i);
                        }*/
                        //Log.e(TAG, "current total = "+count);

                        //if (count != total_quantity) {
                            Intent textchangeIntent = new Intent(Constants.ACTION.ACTION_ENTERING_WAREHOUSE_DIVIDED_DIALOG_TEXT_CHANGE);
                            context.sendBroadcast(textchangeIntent);
                        //}
                    }
                    //textChangeAfter = s.toString();
                    //Log.d(TAG, "afterTextChanged: "+s.toString()+", dividedItem.getEdit().toString() = "+dividedItem.getEdit().getText().toString());
                    //dividedItem.setQuantity(Integer.valueOf(dividedItem.getEdit().toString()));
                    //dividedItem.getEdit().setText(s.toString());
                    //if (s.toString().length() > 0) {
                        //dividedItem.setQuantity(Integer.valueOf(s.toString()));
                    //}

                    //if (s.length() > 0) {
                    //    items.get(position).setQuantity(Integer.valueOf(s.toString()));
                    //}

                    //Intent textchangeIntent = new Intent(Constants.ACTION.ACTION_ENTERING_WAREHOUSE_DIVIDED_DIALOG_TEXT_CHANGE);
                    //textchangeIntent.putExtra("INDEX", String.valueOf(position));
                    //textchangeIntent.putExtra("CHANGED_TEXT", s.toString());
                    //context.sendBroadcast(textchangeIntent);
                }
            });


        }
        return view;
    }

    private class ViewHolder {
        TextView itemIndex;
        EditText itemQuantity;
        Button itemDelete;


        private ViewHolder(View view) {
            this.itemIndex = view.findViewById(R.id.itemIndex);
            this.itemQuantity = view.findViewById(R.id.itemQuantity);
            this.itemDelete = view.findViewById(R.id.itemDelete);
        }
    }
}
