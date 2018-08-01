package com.macauto.macautowarehouse.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.macauto.macautowarehouse.R;
import com.macauto.macautowarehouse.table.DataRow;

import java.util.ArrayList;
import java.util.HashMap;

import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.check_stock_in;
import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.dataTable;

public class InspectedReceiveExpanedAdater extends BaseExpandableListAdapter {
    private static final String TAG = InspectedReceiveExpanedAdater.class.getName();
    private Context context;
    private ArrayList<String> expandableListTitle;
    private HashMap<String, ArrayList<DetailItem>> expandableListDetail;
    private int layoutResourceIdTitle;
    private int layoutResourceIdInside;

    private LayoutInflater inflater = null;
    public static SparseBooleanArray mSparseBooleanArray;


    public InspectedReceiveExpanedAdater(Context context, int layoutResourceIdTitle, int layoutResourceIdInside, ArrayList<String> expandableListTitle,
                                    HashMap<String, ArrayList<DetailItem>> expandableListDetail) {
        this.context = context;
        this.layoutResourceIdTitle = layoutResourceIdTitle;
        this.layoutResourceIdInside = layoutResourceIdInside;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSparseBooleanArray = new SparseBooleanArray();
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return this.expandableListDetail.get(this.expandableListTitle.get(i))
                .size();
    }

    @Override
    public Object getGroup(int i) {
        return this.expandableListTitle.get(i);
    }

    @Override
    public DetailItem getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String listTitle = (String) getGroup(listPosition);

        //Log.e(TAG, "listPosition = "+listPosition+", isExpanded = "+isExpanded+", listTitle = "+listTitle+ ", V = "+expandableListDetail.get(listTitle).get(0).getName());
        View view;



        //if (listTitle.equals("Friends")) {
        //    listTitle = context.getResources().getString(R.string.macauto_chat_expand_friends);
        //}

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_group, null);
        } else {
            view = convertView;

        }
        TextView listTitleTextView =  view.findViewById(R.id.listTitle);

        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        if (expandableListDetail.get(listTitle).get(1).getName().equals("true")) {
            try {
                view.setBackgroundColor(Color.rgb(0xff, 0xd6, 0x00));
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        } else {
            view.setBackgroundColor(Color.TRANSPARENT);
        }


        return view;
    }

    @Override
    public View getChildView(final int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        View view;
        //Log.e(TAG, "listPosition = "+listPosition+", expandedListPosition = "+expandedListPosition);
        /*final String expandedListText = (String) getChild(listPosition, expandedListPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.contact_list_item, null);
        }
        TextView expandedListTextView = (TextView) convertView
                .findViewById(R.id.contact_jid);
        expandedListTextView.setText(expandedListText);*/


        if (convertView == null || convertView.getTag() == null) {
            //LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(layoutResourceIdInside, parent, false);
            holder = new ViewHolder(view);

            /*holder.title = (TextView) convertView.findViewById(R.id.itemTitle);
            holder.name = (TextView) convertView.findViewById(R.id.itemName);
            holder.linearLayout = convertView.findViewById(R.id.itemLinearLayout);
            holder.edit = convertView.findViewById(R.id.itemEdit);
            holder.button = convertView.findViewById(R.id.itemConfirm);*/

            //holder.date = (TextView) convertView.findViewById(R.id.startDate);
            //holder.place = (TextView) convertView.findViewById(R.id.place);
            //holder.cancel = (TextView) convertView.findViewById(R.id.cancelStat);
            view.setTag(holder);


        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        if (getChildrenCount(listPosition) > 0) {

            try {

                DetailItem item = (DetailItem) getChild(listPosition, expandedListPosition);

                holder.title.setText(item.getTitle());
                holder.name.setText(item.getName());
                holder.checkBox.setTag(listPosition);
                item.setCheckBox(holder.checkBox);
                //holder.edit.setText(item.getName());

                //int text_color = holder.name.getCurrentTextColor();

                if (expandedListPosition != 10) {
                    //holder.linearLayout.setVisibility(View.GONE);
                    holder.name.setVisibility(View.VISIBLE);

                }

                if (item.getTitle().equals(context.getResources().getString(R.string.item_title_rvv33))) {
                    holder.name.setTextColor(Color.RED);
                } else {
                    holder.name.setTextColor(Color.BLACK);
                }

                if (item.getTitle().equals(context.getResources().getString(R.string.item_title_rvb33))) {
                    holder.imageView.setVisibility(View.VISIBLE);
                } else {
                    holder.imageView.setVisibility(View.GONE);
                }

                if (item.getTitle().equals(context.getResources().getString(R.string.item_title_confirm_stock_in))) {
                    holder.checkBox.setVisibility(View.VISIBLE);
                    holder.linearLayout.setVisibility(View.GONE);
                } else {
                    holder.checkBox.setVisibility(View.GONE);
                    holder.linearLayout.setVisibility(View.VISIBLE);
                }

                /*if (check_stock_in.get(listPosition)) {
                    holder.checkBox.setChecked(true);
                } else {
                    holder.checkBox.setChecked(false);
                }*/

                if (mSparseBooleanArray.get(listPosition))
                {
                    check_stock_in.set(listPosition, true);
                    holder.checkBox.setChecked(true);
                } else {
                    check_stock_in.set(listPosition, false);
                    holder.checkBox.setChecked(false);
                }

                /*holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                        check_stock_in.set(listPosition, isChecked);

                        Log.e(TAG, "=== check box list start ===");
                        for (int i=0; i<expandableListTitle.size(); i++) {
                            Log.d(TAG, "check_stock_in["+i+"] = "+check_stock_in.get(i));
                        }
                        Log.e(TAG, "=== check box list end ===");
                    }
                });*/



                //show storage no



                /*if (expandedListPosition != 7) {
                    holder.name.setTextColor(text_color);

                } else {
                    if (holder.title.getText().toString().equals(context.getResources().getString(R.string.item_title_rvv33))) {

                        if (holder.name.getText().toString().equals("NA")) {
                            holder.name.setTextColor(Color.BLACK);
                        } else {
                            holder.name.setTextColor(Color.RED);
                        }
                    }

                }*/

                //holder.linearLayout.setVisibility(View.GONE);
                //holder.name.setVisibility(View.VISIBLE);
                //EditText editText = convertView.findViewById(R.id.itemEdit);
                //TextView textView = convertView.findViewById(R.id.itemName);

                //item.getTextView().setText(item.getName());
                //item.setLinearLayout(holder.linearLayout);
                //item.setEdit(holder.edit);
                item.setTextView(holder.name);
                //item.setButton(holder.button);

                view.setBackgroundColor(Color.rgb(0xea, 0xea, 0xea));

                holder.checkBox.setOnCheckedChangeListener(mCheckedChangeListener);
                /*if (holder.linearLayout.getVisibility() == View.VISIBLE) {
                    holder.edit.requestFocus();
                    holder.edit.setSelection(item.getName().length());
                }
                //holder.name.setOnClickListener(mClickListener);
                holder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.name.setVisibility(View.VISIBLE);
                        holder.linearLayout.setVisibility(View.GONE);

                        String head = expandableListTitle.get(listPosition);
                        DetailItem detailItem = expandableListDetail.get(head).get(expandedListPosition);
                        detailItem.setName(holder.edit.getText().toString());

                        if (dataTable != null) {
                            dataTable.Rows.get(listPosition).setValue("rvb33", (holder.edit.getText().toString()));
                        }

                        Log.e(TAG, "========================================================");
                        for (int i = 0; i < dataTable.Rows.size(); i++) {

                            for (int j = 0; j < dataTable.Columns.size(); j++) {
                                System.out.print(dataTable.Rows.get(i).getValue(j));
                                if (j < dataTable.Columns.size() - 1) {
                                    System.out.print(", ");
                                }
                            }
                            System.out.print("\n");
                        }

                        Intent getFailedIntent = new Intent(Constants.ACTION.ACTION_MODIFIED_ITEM_COMPLETE);
                        context.sendBroadcast(getFailedIntent);
                    }
                });*/

            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    private CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            //int idx = (Integer) buttonView.getTag();


            mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
            check_stock_in.set((Integer) buttonView.getTag(), isChecked);

            int count = 0;
            if (isChecked)
                count++;
            else
                count--;

            //Log.e(TAG, "switch " + buttonView.getTag() + " checked = " + isChecked +", count = "+count);

            /*Intent newNotifyIntent = new Intent();
            if (count > 0) {
                newNotifyIntent.setAction(Constants.ACTION.ACTION_ENTERING_WAREHOUSE_SHOW_CONFIRM_BUTTON);
            } else  {
                newNotifyIntent.setAction(Constants.ACTION.ACTION_ENTERING_WAREHOUSE_HIDE_CONFIRM_BUTTON);
            }*/
            Intent newNotifyIntent = new Intent();
            newNotifyIntent.setAction(Constants.ACTION.ACTION_ENTERING_WAREHOUSE_CHECKBOX_CHANGE);
            //newNotifyIntent.putExtra("CHECK_INDEX", String.valueOf((Integer) buttonView.getTag()));
            //newNotifyIntent.putExtra("CHECK_BOX", String.valueOf(isChecked));
            context.sendBroadcast(newNotifyIntent);

            /*Log.e(TAG, "=== check box list start ===");
            for (int i=0; i<expandableListTitle.size(); i++) {
                Log.d(TAG, "check_stock_in["+i+"] = "+check_stock_in.get(i)+", mSparseBooleanArray["+i+"] = "+mSparseBooleanArray.get(i));
            }
            Log.e(TAG, "=== check box list end ===");*/



            //if(isChecked == true) {
            /*FileChooseItem fileChooseItem = items.get((Integer) buttonView.getTag());


            for


            if (fileChooseItem.getCheckBox() != null) {

                if (!fileChooseItem.getName().equals("..")) {
                    mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
                    if (isChecked)
                        count++;
                    else
                        count--;
                }
                else {
                    fileChooseItem.getCheckBox().setChecked(false);
                    fileChooseItem.getCheckBox().setVisibility(View.INVISIBLE);
                    mSparseBooleanArray.put((Integer) buttonView.getTag(), false);
                    count--;
                }
            }*/

        }
    };

    private class ViewHolder {
        TextView title;
        TextView name;
        ImageView imageView;
        LinearLayout linearLayout;
        CheckBox checkBox;
        //EditText edit;
        //Button button;

        private ViewHolder(View view) {
            this.title = view.findViewById(R.id.itemTitle);
            this.name = view.findViewById(R.id.itemName);
            this.imageView = view.findViewById(R.id.itemImage);
            this.linearLayout = view.findViewById(R.id.layoutShow);
            this.checkBox = view.findViewById(R.id.checkConfirm);
            //this.edit = view.findViewById(R.id.itemEdit);
            //this.button = view.findViewById(R.id.itemConfirm);
        }
    }
}
