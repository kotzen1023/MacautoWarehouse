package com.macauto.macautowarehouse.data;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.macauto.macautowarehouse.EnteringWarehouseDetailActivity;
import com.macauto.macautowarehouse.EnteringWarehouseDividedDialogActivity;
import com.macauto.macautowarehouse.R;

import java.util.ArrayList;

import static com.macauto.macautowarehouse.MainActivity.dataTable;
import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.item_select;
import static com.macauto.macautowarehouse.ReceivingInspectionFragment.dataTable_TTCP;


public class ReceivingInspectionItemAdapter extends ArrayAdapter<ReceivingInspectionItem>{
    public static final String TAG = "RInspectionAdapter";
    private LayoutInflater inflater;

    private Context mContext;

    private int layoutResourceId;
    private ArrayList<ReceivingInspectionItem> items;
    public SparseBooleanArray mSparseBooleanArray;

    private SwipeLayout preswipes=null;
    private int pre_open_swipe = -1;

    public ReceivingInspectionItemAdapter(Context context, int textViewResourceId,
                                        ArrayList<ReceivingInspectionItem> objects) {
        super(context, textViewResourceId, objects);
        this.layoutResourceId = textViewResourceId;
        this.items = objects;
        this.mContext = context;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSparseBooleanArray = new SparseBooleanArray();

    }

    @Override
    public int getCount() {
        return items.size();

    }

    public ReceivingInspectionItem getItem(int position)
    {
        return items.get(position);
    }

    @Override
    public @NonNull
    View getView(final int position, View convertView, @NonNull ViewGroup parent) {

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

        final ReceivingInspectionItem receivingInspectionItem = items.get(position);
        if (receivingInspectionItem != null) {

            receivingInspectionItem.setCheckBox(holder.checkBox);
            holder.checkBox.setTag(position);
            //holder.textViewIndex.setText(String.valueOf(position+1));
            String topString = mContext.getResources().getString(R.string.item_title_rva01)+" "+receivingInspectionItem.getRva01();
            String centerString = mContext.getResources().getString(R.string.item_title_pmn041)+" "+receivingInspectionItem.getIma021();
            String bottomString = mContext.getResources().getString(R.string.receiving_inspection_vendor_name)+" "+receivingInspectionItem.getVend_name();
            holder.textViewTop.setText(topString);
            holder.textViewCenter.setText(centerString);
            holder.textViewBottom.setText(bottomString);
            //holder.itemTitle.setText(allocationMsgStatusItem.getItem_SFA03());
            //holder.itemDate.setText(allocationMsgItem.getDate());



            if (mSparseBooleanArray.get(position)) {
                holder.checkBox.setChecked(true);
            } else {
                holder.checkBox.setChecked(false);
            }



            holder.checkBox.setOnCheckedChangeListener(mCheckedChangeListener);


            holder.btnEdit.setOnClickListener(onEditListener(position, holder));
            holder.btnDivide.setOnClickListener(onDivideListener(position, holder));


            holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

            holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, holder.bottom_wrapper);


            holder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onClose(SwipeLayout layout) {
                    Log.i(TAG, "onClose");

                    if (preswipes == layout) {
                        item_select = -1;

                        Log.e(TAG, "item_select => "+item_select);

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

                    item_select = position;

                    Log.e(TAG, "pre_open_swipe => "+pre_open_swipe);
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

        //holder.swipeLayout.close();

        //Intent detailIntent = new Intent(mContext, EnteringWarehouseDetailActivity.class);
        //detailIntent.putExtra("INDEX", String.valueOf(position));
        //mContext.startActivity(detailIntent);


    }

    private View.OnClickListener onDivideListener(final int position, final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "divide click "+position);

            }
        };
    }

    private CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Log.i(TAG, "switch " + buttonView.getTag() + " checked = " + isChecked);
            //int idx = (Integer) buttonView.getTag();

            //if(isChecked == true) {
            ReceivingInspectionItem receivingInspectionItem = items.get((Integer) buttonView.getTag());
            //mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);


            if (receivingInspectionItem.getCheckBox() != null) {



                mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);

                if (dataTable_TTCP != null && dataTable_TTCP.Rows.size() > 0) {
                    dataTable_TTCP.Rows.get((Integer) buttonView.getTag()).setValue("check_sp", String.valueOf(isChecked));
                }

            }

            Intent newNotifyIntent = new Intent();
            newNotifyIntent.setAction(Constants.ACTION.ACTION_RECEIVING_INSPECTION_ITEM_SELECT_CHANGE);
            mContext.sendBroadcast(newNotifyIntent);

        }
    };

    private class ViewHolder  {
        CheckBox checkBox;
        TextView textViewTop;
        TextView textViewCenter;
        TextView textViewBottom;

        private View btnDivide;
        private View btnEdit;

        private SwipeLayout swipeLayout;
        private LinearLayout bottom_wrapper;

        private ViewHolder(View view) {
            this.checkBox = view.findViewById(R.id.checkBoxReceivingInspection);
            this.textViewTop = view.findViewById(R.id.receivingInspectionItemtitle);
            this.textViewCenter = view.findViewById(R.id.receivingInspectionItemDecrypt);
            this.textViewBottom = view.findViewById(R.id.receivingInspectionItemCount);

            this.swipeLayout = view.findViewById(R.id.swipe_layout_receiving_inspection);
            this.bottom_wrapper = view.findViewById(R.id.bottom_wrapper_receiving_inspection);

            this.btnDivide = view.findViewById(R.id.divide_receiving_inspection);
            this.btnEdit = view.findViewById(R.id.edit_query_receiving_inspection);
        }
    }
}
