package com.macauto.macautowarehouse.data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.macauto.macautowarehouse.R;
import com.macauto.macautowarehouse.table.DataRow;

import java.util.ArrayList;
import java.util.HashMap;

import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.dataTable;

public class InspectedReceiveExpanedAdater extends BaseExpandableListAdapter {
    private static final String TAG = InspectedReceiveExpanedAdater.class.getName();
    private Context context;
    private ArrayList<String> expandableListTitle;
    private HashMap<String, ArrayList<DetailItem>> expandableListDetail;
    private int layoutResourceIdTitle;
    private int layoutResourceIdInside;

    private LayoutInflater inflater = null;

    public InspectedReceiveExpanedAdater(Context context, int layoutResourceIdTitle, int layoutResourceIdInside, ArrayList<String> expandableListTitle,
                                    HashMap<String, ArrayList<DetailItem>> expandableListDetail) {
        this.context = context;
        this.layoutResourceIdTitle = layoutResourceIdTitle;
        this.layoutResourceIdInside = layoutResourceIdInside;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        //if (listTitle.equals("Friends")) {
        //    listTitle = context.getResources().getString(R.string.macauto_chat_expand_friends);
        //}

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public View getChildView(final int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ViewHolder holder;

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


        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceIdInside, parent, false);
            holder = new ViewHolder(convertView);

            holder.title = (TextView) convertView.findViewById(R.id.itemTitle);
            holder.name = (TextView) convertView.findViewById(R.id.itemName);
            holder.linearLayout = convertView.findViewById(R.id.itemLinearLayout);
            holder.edit = convertView.findViewById(R.id.itemEdit);
            holder.button = convertView.findViewById(R.id.itemConfirm);

            //holder.date = (TextView) convertView.findViewById(R.id.startDate);
            //holder.place = (TextView) convertView.findViewById(R.id.place);
            //holder.cancel = (TextView) convertView.findViewById(R.id.cancelStat);
            convertView.setTag(holder);


        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (getChildrenCount(listPosition) > 0) {

            try {

                DetailItem item = (DetailItem) getChild(listPosition, expandedListPosition);

                holder.title.setText(item.getTitle());
                holder.name.setText(item.getName());
                holder.edit.setText(item.getName());

                //holder.linearLayout.setVisibility(View.GONE);
                //holder.name.setVisibility(View.VISIBLE);
                //EditText editText = convertView.findViewById(R.id.itemEdit);
                //TextView textView = convertView.findViewById(R.id.itemName);

                //item.getTextView().setText(item.getName());
                item.setLinearLayout(holder.linearLayout);
                item.setEdit(holder.edit);
                item.setTextView(holder.name);
                item.setButton(holder.button);

                convertView.setBackgroundColor(Color.rgb(0xea, 0xea, 0xea));

                if (holder.linearLayout.getVisibility() == View.VISIBLE) {
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
                            dataTable.Rows.get(listPosition).setValue("rvv33", (holder.edit.getText().toString()));
                        }

                        Intent getFailedIntent = new Intent(Constants.ACTION.ACTION_MODIFIED_ITEM_COMPLETE);
                        context.sendBroadcast(getFailedIntent);
                    }
                });

            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    private class ViewHolder {
        TextView title;
        TextView name;
        LinearLayout linearLayout;
        EditText edit;
        Button button;

        private ViewHolder(View view) {
            this.title = view.findViewById(R.id.itemTitle);
            this.name = view.findViewById(R.id.itemName);
            this.linearLayout = view.findViewById(R.id.itemLinearLayout);
            this.edit = view.findViewById(R.id.itemEdit);
            this.button = view.findViewById(R.id.itemConfirm);
        }
    }
}
