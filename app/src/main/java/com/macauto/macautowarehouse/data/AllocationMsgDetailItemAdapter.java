package com.macauto.macautowarehouse.data;

import android.content.Context;

import android.graphics.Color;
import android.support.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.ImageView;
import android.widget.TextView;



import com.macauto.macautowarehouse.R;

import java.util.ArrayList;

public class AllocationMsgDetailItemAdapter extends ArrayAdapter<AllocationMsgDetailItem> {
    public static final String TAG = "MsgDetailItemAdapter";
    private LayoutInflater inflater;

    private int layoutResourceId;
    private ArrayList<AllocationMsgDetailItem> items;
    //private Context mContext;
   //private SwipeLayout preswipes=null;

    public AllocationMsgDetailItemAdapter(Context context, int textViewResourceId,
                                          ArrayList<AllocationMsgDetailItem> objects) {
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
            String center = items.get(position).getItem_ima021()+" "+items.get(position).getItem_qty()+" "+items.get(position).getItem_sfa12();
            String bottom = items.get(position).getItem_src_stock_no()+" "+items.get(position).getItem_src_locate_no();
            holder.textViewCenter.setText(center);
            holder.textViewBottom.setText(bottom);
            //holder.textViewScan.setText(items.get(position).getItem_scan_desc());

            if (allocationMsgDetailItem.isChecked()) {
                holder.imageView.setVisibility(View.VISIBLE);
            } else {
                holder.imageView.setVisibility(View.GONE);
            }


            if (allocationMsgDetailItem.isSelected()) {
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
    }*/

    private class ViewHolder {
        private TextView textViewTop;
        private TextView textViewCenter;
        private TextView textViewBottom;
        private ImageView imageView;
        //private TextView textViewScan;

        //private View btnDelete;
        //private View btnEdit;

        //private SwipeLayout swipeLayout;
        //private LinearLayout bottom_wrapper;

        //TextView itemHeader;
        //TextView itemContent;


        private ViewHolder(View view) {
            //this.itemHeader = view.findViewById(R.id.itemDetailHeader);
            //this.itemContent = view.findViewById(R.id.itemDetailContent);
            //this.textViewScan = view.findViewById(R.id.detailItemScan);
            this.imageView = view.findViewById(R.id.detailItemImg);
            this.textViewTop = view.findViewById(R.id.detailItemtitle);
            this.textViewCenter = view.findViewById(R.id.detailItemDecrypt);
            this.textViewBottom = view.findViewById(R.id.detailItemCount);
            //this.swipeLayout = view.findViewById(R.id.swipe_layout_detail);
            //this.bottom_wrapper = view.findViewById(R.id.bottom_wrapper_detail);

            //this.btnDelete = view.findViewById(R.id.delete_detail);
            //this.btnEdit = view.findViewById(R.id.edit_query_detail);
        }
    }
}
