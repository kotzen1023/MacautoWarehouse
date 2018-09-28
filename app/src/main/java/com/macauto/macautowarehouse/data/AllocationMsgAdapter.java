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



public class AllocationMsgAdapter extends ArrayAdapter<AllocationMsgItem> {
    public static final String TAG = "AllocationMsgAdapter";
    private LayoutInflater inflater;

    //private Context mContext;

    private int layoutResourceId;
    private ArrayList<AllocationMsgItem> items;

    public AllocationMsgAdapter(Context context, int textViewResourceId,
                                       ArrayList<AllocationMsgItem> objects) {
        super(context, textViewResourceId, objects);
        this.layoutResourceId = textViewResourceId;
        this.items = objects;
        //this.mContext = context;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return items.size();

    }

    public AllocationMsgItem getItem(int position)
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

        AllocationMsgItem allocationMsgItem = items.get(position);
        if (allocationMsgItem != null) {
            holder.itemTitle.setText(allocationMsgItem.getWork_order());
            //holder.itemDate.setText(allocationMsgItem.getDate());


            if (allocationMsgItem.isSelected()) {
                //Log.e(TAG, ""+position+" is selected.");
                //view.setSelected(true);
                view.setBackgroundColor(Color.rgb(0x4d, 0x90, 0xfe));
            } else {
                //Log.e(TAG, ""+position+" clear.");
                //view.setSelected(false);
                view.setBackgroundColor(Color.TRANSPARENT);
            }

            /*if (allocationMsgItem.isDelete()) {
                holder.btnDelete.setVisibility(View.VISIBLE);
                holder.btnDelete.setEnabled(true);
            } else {
                holder.btnDelete.setVisibility(View.INVISIBLE);
                holder.btnDelete.setEnabled(false);
            }




            holder.btnEdit.setOnClickListener(onEditListener(position, holder));
            holder.btnDelete.setOnClickListener(onDeleteListener(position, holder));


            holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

            holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, holder.bottom_wrapper);


            holder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onClose(SwipeLayout layout) {
                    Log.i(TAG, "onClose");
                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                    Log.i(TAG, "on swiping");
                }

                @Override
                public void onStartOpen(SwipeLayout layout) {
                    Log.i(TAG, "on start open");

                }

                @Override
                public void onOpen(SwipeLayout layout) {
                    Log.i(TAG, "the BottomView totally show");

                }

                @Override
                public void onStartClose(SwipeLayout layout) {
                    Log.i(TAG, "the BottomView totally close");

                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                    //when user's hand released.
                    Log.i(TAG, "onHandRelease");
                }
            });*/
        }
        return view;
    }

    /*private View.OnClickListener onEditListener(final int position, final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "edit click "+position);

                showEditDialog(position, holder);
            }
        };
    }

    private void showEditDialog(final int position, final ViewHolder holder) {



        String[] p_no = items.get(position).getWork_order().split("#");

        String iss_no = p_no[0];

        String dateTime_0="", dateTime_1="", dateTime_2="", dateTime_3="";

        if (p_no[2].length() > 0) {
            dateTime_0 = p_no[2].substring(0, 4);
            dateTime_1 = p_no[2].substring(4, 6);
            dateTime_2 = p_no[2].substring(6, 8);
            dateTime_3 = p_no[2].substring(9);
        }

        Intent getMessDetailIntent = new Intent(mContext, GetMyMessDetailNewService.class);
        getMessDetailIntent.setAction(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_DETAIL_ACTION);
        getMessDetailIntent.putExtra("ISS_NO", iss_no);
        getMessDetailIntent.putExtra("DATETIME_0", dateTime_0);
        getMessDetailIntent.putExtra("DATETIME_1", dateTime_1);
        getMessDetailIntent.putExtra("DATETIME_2", dateTime_2);
        getMessDetailIntent.putExtra("DATETIME_3", dateTime_3);
        mContext.startService(getMessDetailIntent);




    }

    private View.OnClickListener onDeleteListener(final int position, final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "delete click "+position);

                android.app.AlertDialog.Builder confirmdialog = new android.app.AlertDialog.Builder(mContext);
                confirmdialog.setIcon(R.drawable.ic_warning_black_48dp);
                confirmdialog.setTitle(mContext.getResources().getString(R.string.action_allocation_msg));
                confirmdialog.setMessage(mContext.getResources().getString(R.string.delete)+":\n"+items.get(position).getWork_order()+" ?");
                confirmdialog.setPositiveButton(mContext.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        holder.swipeLayout.close();

                        String[] p_no = items.get(position).getWork_order().split("#");
                        String iss_no = p_no[0];

                        Intent deleteIntent = new Intent(mContext, DeleteMessageNoService.class);
                        deleteIntent.setAction(Constants.ACTION.ACTION_ALLOCATION_HANDLE_MSG_DELETE_ACTION);
                        deleteIntent.putExtra("MESSAGE_NO", iss_no);
                        deleteIntent.putExtra("USER_NO", emp_no);
                        deleteIntent.putExtra("DELETE_INDEX", String.valueOf(position));
                        mContext.startService(deleteIntent);


                    }
                });
                confirmdialog.setNegativeButton(mContext.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // btnScan.setVisibility(View.VISIBLE);
                        // btnConfirm.setVisibility(View.GONE);

                    }
                });
                confirmdialog.show();




            }
        };
    }*/

    private class ViewHolder {
        private TextView itemTitle;
        //TextView itemDate;
        //private View btnDelete;
        //private View btnEdit;

        //private SwipeLayout swipeLayout;
        //private LinearLayout bottom_wrapper;

        private ViewHolder(View view) {
            this.itemTitle = view.findViewById(R.id.itemTitle);
            //this.swipeLayout = view.findViewById(R.id.swipe_layout_msg_list);
            //this.bottom_wrapper = view.findViewById(R.id.bottom_wrapper_msg_list);

            //this.btnDelete = view.findViewById(R.id.delete_msg_list);
            //this.btnEdit = view.findViewById(R.id.edit_query_msg_list);
        }
    }
}
