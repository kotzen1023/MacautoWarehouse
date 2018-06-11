package com.macauto.macautowarehouse.data;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.macauto.macautowarehouse.R;

import java.util.ArrayList;
import java.util.HashMap;

public class InspectedReceiveExpanedAdater extends BaseExpandableListAdapter {
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
    public Object getChild(int listPosition, int expandedListPosition) {
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
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder;
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
            holder = new ViewHolder();

            holder.title = (TextView) convertView.findViewById(R.id.itemTitle);
            holder.name = (TextView) convertView.findViewById(R.id.itemName);

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
                /*if (item.getNick() != null && item.getNick().length() > 0) {
                    holder.jid.setText(item.getNick());
                } else if ((item.getFirstName() != null && item.getFirstName().length() > 0) &&
                        (item.getLastName() != null && item.getLastName().length() > 0)) {
                    holder.jid.setText(item.getFirstName() + " " + item.getLastName());
                } else if (item.getFirstName() != null && item.getFirstName().length() > 0) {
                    holder.jid.setText(item.getFirstName());
                } else if (item.getLastName() != null && item.getLastName().length() > 0) {
                    holder.jid.setText(item.getLastName());
                } else {
                    String split[] = item.getJid().split("@");
                    if (split[1].equals("group")) {
                        holder.jid.setText(split[0].split("_")[1]);
                    } else {
                        holder.jid.setText(split[0]);
                    }
                }


                if (item.getAvatar() != null)
                    holder.avatar.setImageBitmap(item.getAvatar());
                else {
                    if (item.getJid().split("@")[1].equals("group")) {
                        holder.avatar.setImageResource(R.drawable.ic_people_outline_black_48dp);
                    } else {
                        holder.avatar.setImageResource(R.drawable.ic_person_outline_black_48dp);
                    }
                }*/

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

    class ViewHolder {
        TextView title;
        TextView name;
    }
}
