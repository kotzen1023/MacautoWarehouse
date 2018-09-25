package com.macauto.macautowarehouse.data;

import android.app.ProgressDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;

import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.daimajia.swipe.SwipeLayout;
import com.macauto.macautowarehouse.AllocationSendMsgStatusDetailActivity;
import com.macauto.macautowarehouse.R;
import com.macauto.macautowarehouse.service.ConfirmEnteringWarehouseService;
import com.macauto.macautowarehouse.table.DataTable;

import java.util.ArrayList;

import static com.macauto.macautowarehouse.AllocationSendMsgToReserveWarehouseFragment.hhh;


public class AllocationMsgStatusItemAdapter extends ArrayAdapter<AllocationMsgStatusItem> {
    public static final String TAG = "StatusItemAdapter";
    private LayoutInflater inflater;

    private Context mContext;

    private int layoutResourceId;
    private ArrayList<AllocationMsgStatusItem> items;
    private SwipeLayout preswipes=null;
    private int pre_open_swipe = -1;

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
    View getView(final int position, View convertView, @NonNull ViewGroup parent) {

        //Log.e(TAG, "getView = "+ position);
        View view;
        final ViewHolder holder;
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
            String temp = mContext.getResources().getString(R.string.allocation_send_message_to_material_status_detail_IMG10)+ " " +String.valueOf(aw1);
            holder.textViewBottom.setText(temp);
            //holder.itemTitle.setText(allocationMsgStatusItem.getItem_SFA03());
            //holder.itemDate.setText(allocationMsgItem.getDate());


            if (allocationMsgStatusItem.isSelected()) {
                //Log.e(TAG, ""+position+" is selected.");
                //view.setSelected(true);
                view.setBackgroundColor(Color.rgb(0x4d, 0x90, 0xfe));
            } else {
                //Log.e(TAG, ""+position+" clear.");
                //view.setSelected(false);
                view.setBackgroundColor(Color.TRANSPARENT);
            }


            /*holder.btnEdit.setOnClickListener(onEditListener(position, holder));
            holder.btnDelete.setOnClickListener(onDeleteListener(position, holder));


            holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

            holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, holder.bottom_wrapper);


            holder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onClose(SwipeLayout layout) {
                    Log.i(TAG, "onClose");

                    if (preswipes == layout) {
                        pre_open_swipe = -1;

                        Log.e(TAG, "pre_open_swipe => "+pre_open_swipe);

                    }

                    layout.setBackgroundColor(Color.TRANSPARENT);
                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                    Log.i(TAG, "on swiping");
                }

                @Override
                public void onStartOpen(SwipeLayout layout) {
                    Log.i(TAG, "on start open");
                    if(preswipes==null) {
                        preswipes=layout;

                    } else {
                        preswipes.close(true);
                        preswipes=layout;
                    }

                    layout.setBackgroundColor(Color.rgb(0x4d, 0x90, 0xfe));

                    pre_open_swipe = position;

                    Log.e(TAG, "pre_open_swipe => "+pre_open_swipe);
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

        //holder.swipeLayout.close();

        Intent detailIntent = new Intent(mContext, AllocationSendMsgStatusDetailActivity.class);
        detailIntent.putExtra("ITEM_SFA03", items.get(position).getItem_SFA03());
        detailIntent.putExtra("ITEM_IMA021", items.get(position).getItem_IMA021());
        detailIntent.putExtra("ITEM_IMG10", items.get(position).getItem_IMG10());
        detailIntent.putExtra("ITEM_MOVED_QTY", items.get(position).getItem_MOVED_QTY());
        detailIntent.putExtra("ITEM_MESS_QTY", items.get(position).getItem_MESS_QTY());
        detailIntent.putExtra("ITEM_SFA05", items.get(position).getItem_SFA05());
        detailIntent.putExtra("ITEM_SFA12", items.get(position).getItem_SFA12());
        detailIntent.putExtra("ITEM_SFA11_NAME", items.get(position).getItem_SFA11_NAME());
        detailIntent.putExtra("ITEM_TC_OBF013", items.get(position).getItem_TC_OBF013());
        mContext.startActivity(detailIntent);


    }

    private View.OnClickListener onDeleteListener(final int position, final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "delete click "+position);

                //activity.updateAdapter();
                items.remove(position);
                holder.swipeLayout.close();
                Intent newNotifyIntent = new Intent();
                newNotifyIntent.setAction(Constants.ACTION.ACTION_ALLOCATION_SWIPE_LAYOUT_UPDATE);
                mContext.sendBroadcast(newNotifyIntent);

            }
        };
    }*/

    private class ViewHolder  {
        private TextView textViewIndex;
        private TextView textViewTop;
        private TextView textViewCenter;
        private TextView textViewBottom;

        //private View btnDelete;
        //private View btnEdit;

        //private SwipeLayout swipeLayout;
        //private LinearLayout bottom_wrapper;

        private ViewHolder(View view) {
            this.textViewIndex = view.findViewById(R.id.statusItemId);
            this.textViewTop = view.findViewById(R.id.statusItemtitle);
            this.textViewCenter = view.findViewById(R.id.statusItemDecrypt);
            this.textViewBottom = view.findViewById(R.id.statusItemCount);
            //this.swipeLayout = view.findViewById(R.id.swipe_layout);
            //this.bottom_wrapper = view.findViewById(R.id.bottom_wrapper);

            //this.btnDelete = view.findViewById(R.id.delete);
            //this.btnEdit = view.findViewById(R.id.edit_query);

            //set show mode.
            //swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);


        }
    }
}
