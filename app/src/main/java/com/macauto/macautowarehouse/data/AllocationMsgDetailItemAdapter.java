package com.macauto.macautowarehouse.data;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.macauto.macautowarehouse.AllocationSendMsgStatusDetailActivity;
import com.macauto.macautowarehouse.R;

import java.util.ArrayList;

public class AllocationMsgDetailItemAdapter extends ArrayAdapter<AllocationMsgDetailItem> {
    public static final String TAG = "MsgDetailItemAdapter";
    private LayoutInflater inflater;

    private int layoutResourceId;
    private ArrayList<AllocationMsgDetailItem> items;
    private Context mContext;
    private SwipeLayout preswipes=null;

    public AllocationMsgDetailItemAdapter(Context context, int textViewResourceId,
                                          ArrayList<AllocationMsgDetailItem> objects) {
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

    public AllocationMsgDetailItem getItem(int position)
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

        AllocationMsgDetailItem allocationMsgDetailItem = items.get(position);
        if (allocationMsgDetailItem != null) {
            holder.textViewTop.setText(items.get(position).getItem_part_no());
            holder.textViewCenter.setText(items.get(position).getItem_ima021()+" "+items.get(position).getItem_qty()+" "+items.get(position).getItem_sfa12());
            holder.textViewBottom.setText(items.get(position).getItem_src_stock_no()+" "+items.get(position).getItem_src_locate_no());
            holder.textViewScan.setText(items.get(position).getItem_scan_desc());

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

                    if(preswipes==null) {
                        preswipes=layout;
                    }else
                    {
                        preswipes.close(true);
                        preswipes=layout;
                    }
                }

                @Override
                public void onOpen(SwipeLayout layout) {
                    Log.i(TAG, "the BottomView totally show");
                    /*Intent newNotifyIntent = new Intent();
                    newNotifyIntent.setAction(Constants.ACTION.ACTION_ALLOCATION_SWIPE_LAYOUT_UPDATE);
                    mContext.sendBroadcast(newNotifyIntent);*/
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
            });





        }
        return view;
    }

    private View.OnClickListener onEditListener(final int position, final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "edit click "+position);

                showEditDialog(position, holder);
            }
        };
    }

    private void showEditDialog(final int position, final ViewHolder holder) {

        /*holder.swipeLayout.close();

        Intent detailIntent = new Intent(mContext, AllocationSendMsgStatusDetailActivity.class);
        detailIntent.putExtra("ITEM_SFA03", items.get(position).getItem_SFA03());
        detailIntent.putExtra("ITEM_IMA021", items.get(position).getItem_IMA021());
        detailIntent.putExtra("ITEM_IMG10", items.get(position).getItem_IMG10());
        detailIntent.putExtra("ITEM_MOVED_QTY", items.get(position).getItem_MOVED_QTY());
        detailIntent.putExtra("ITEM_MOVED_QTY", items.get(position).getItem_MOVED_QTY());
        detailIntent.putExtra("ITEM_MESS_QTY", items.get(position).getItem_MESS_QTY());
        detailIntent.putExtra("ITEM_SFA05", items.get(position).getItem_SFA05());
        detailIntent.putExtra("ITEM_SFA12", items.get(position).getItem_SFA12());
        detailIntent.putExtra("ITEM_SFA11_NAME", items.get(position).getItem_SFA11_NAME());
        detailIntent.putExtra("ITEM_TC_OBF013", items.get(position).getItem_TC_OBF013());
        mContext.startActivity(detailIntent);*/

        /*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);

        alertDialogBuilder.setTitle("EDIT ELEMENT");
        final EditText input = new EditText(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setText(items.get(position));
        input.setLayoutParams(lp);
        alertDialogBuilder.setView(input);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result edit text
                                friends.set(position, input.getText().toString().trim());

                                //notify data set changed
                                //activity.updateAdapter();
                                holder.swipeLayout.close();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog and show it
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();*/
    }

    private View.OnClickListener onDeleteListener(final int position, final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "delete click "+position);

                //activity.updateAdapter();
                //items.remove(position);
                holder.swipeLayout.close();
                Intent newNotifyIntent = new Intent();
                newNotifyIntent.setAction(Constants.ACTION.ACTION_ALLOCATION_SWIPE_LAYOUT_DELETE_ROW);
                newNotifyIntent.putExtra("DELETE_ROW", String.valueOf(position));
                mContext.sendBroadcast(newNotifyIntent);

            }
        };
    }

    private class ViewHolder {
        private TextView textViewTop;
        private TextView textViewCenter;
        private TextView textViewBottom;
        private TextView textViewScan;

        private View btnDelete;
        private View btnEdit;

        private SwipeLayout swipeLayout;
        private LinearLayout bottom_wrapper;

        //TextView itemHeader;
        //TextView itemContent;


        private ViewHolder(View view) {
            //this.itemHeader = view.findViewById(R.id.itemDetailHeader);
            //this.itemContent = view.findViewById(R.id.itemDetailContent);
            this.textViewScan = view.findViewById(R.id.detailItemScan);
            this.textViewTop = view.findViewById(R.id.detailItemtitle);
            this.textViewCenter = view.findViewById(R.id.detailItemDecrypt);
            this.textViewBottom = view.findViewById(R.id.detailItemCount);
            this.swipeLayout = view.findViewById(R.id.swipe_layout_detail);
            this.bottom_wrapper = view.findViewById(R.id.bottom_wrapper_detail);

            this.btnDelete = view.findViewById(R.id.delete_detail);
            this.btnEdit = view.findViewById(R.id.edit_query_detail);
        }
    }
}
