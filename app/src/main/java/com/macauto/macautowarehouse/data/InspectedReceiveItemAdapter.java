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
import com.macauto.macautowarehouse.EnteringWarehouseDividedDialogActivity;
import com.macauto.macautowarehouse.R;


import java.util.ArrayList;

import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.check_stock_in;
import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.dataTable;

public class InspectedReceiveItemAdapter extends ArrayAdapter<InspectedReceiveItem> {
    public static final String TAG = "InspectedItemAdapter";
    private LayoutInflater inflater;

    private Context mContext;

    private int layoutResourceId;
    private ArrayList<InspectedReceiveItem> items;

    public SparseBooleanArray mSparseBooleanArray;
    //private int count = 0;

    public InspectedReceiveItemAdapter(Context context, int textViewResourceId,
                            ArrayList<InspectedReceiveItem> objects) {
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

    public InspectedReceiveItem getItem(int position)
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

        InspectedReceiveItem inspectedReceiveItem = items.get(position);
        if (inspectedReceiveItem != null) {

            inspectedReceiveItem.setCheckBox(holder.checkBox);

            String top = inspectedReceiveItem.getCol_rvb05();
            String center = mContext.getResources().getString(R.string.item_title_pmn041)+" "+inspectedReceiveItem.getCol_pmn041()+" "+
                    mContext.getResources().getString(R.string.item_title_rvb33)+" "+inspectedReceiveItem.getCol_rvb33();
            String bottom = mContext.getResources().getString(R.string.item_title_rvv32)+" "+inspectedReceiveItem.getCol_rvv32()+" "+
                    mContext.getResources().getString(R.string.item_title_rvv33)+" "+inspectedReceiveItem.getCol_rvv33();

            holder.textViewTop.setText(top);
            holder.textViewCenter.setText(center);
            holder.textViewBottom.setText(bottom);

            if (!inspectedReceiveItem.isCheck_sp()) //check sp = false
                view.setBackgroundColor(Color.rgb(0xff, 0xd6, 0x00));

            if (mSparseBooleanArray.get(position))
            {
                check_stock_in.set(position, true);
                holder.checkBox.setChecked(true);
            } else {
                check_stock_in.set(position, false);
                holder.checkBox.setChecked(false);
            }

            holder.checkBox.setTag(position);
            holder.checkBox.setOnCheckedChangeListener(mCheckedChangeListener);


            holder.btnEdit.setOnClickListener(onEditListener(position, holder));
            holder.btnDivide.setOnClickListener(onDivideListener(position, holder));


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

    private View.OnClickListener onDivideListener(final int position, final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "divide click "+position);

                //activity.updateAdapter();
                //items.remove(position);
                /*holder.swipeLayout.close();
                Intent newNotifyIntent = new Intent();
                newNotifyIntent.setAction(Constants.ACTION.ACTION_ALLOCATION_SWIPE_LAYOUT_DELETE_ROW);
                newNotifyIntent.putExtra("DELETE_ROW", String.valueOf(position));
                mContext.sendBroadcast(newNotifyIntent);*/
                float quantity = Float.valueOf(dataTable.getValue(position, "rvb33").toString());
                int quantity_int = (int)quantity;

                Intent intent = new Intent(mContext, EnteringWarehouseDividedDialogActivity.class);
                //intent.putExtra("GROUP_INDEX", String.valueOf(groupPosition));
                //intent.putExtra("CHILD_INDEX", String.valueOf(groupPosition));
                intent.putExtra("IN_NO", dataTable.getValue(position, "rvu01").toString());
                intent.putExtra("ITEM_NO", dataTable.getValue(position, "rvv02").toString());
                intent.putExtra("PART_NO", dataTable.getValue(position, "rvb05").toString());
                intent.putExtra("QUANTITY", String.valueOf(quantity_int));
                intent.putExtra("BATCH_NO", dataTable.getValue(position, "rvv34").toString());
                intent.putExtra("LOCATE_NO", dataTable.getValue(position, "rvv33").toString());
                intent.putExtra("STOCK_NO", dataTable.getValue(position, "rvv32").toString());
                intent.putExtra("CHECK_SP", dataTable.getValue(position, 0).toString());

                mContext.startActivity(intent);

            }
        };
    }

    private CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Log.i(TAG, "switch " + buttonView.getTag() + " checked = " + isChecked);
            //int idx = (Integer) buttonView.getTag();

            //if(isChecked == true) {
            InspectedReceiveItem inspectedReceiveItem = items.get((Integer) buttonView.getTag());
            mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
            check_stock_in.set((Integer) buttonView.getTag(), isChecked);




            if (inspectedReceiveItem.getCheckBox() != null) {

                mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
            }

            Intent newNotifyIntent = new Intent();
            newNotifyIntent.setAction(Constants.ACTION.ACTION_ENTERING_WAREHOUSE_CHECKBOX_CHANGE);
            mContext.sendBroadcast(newNotifyIntent);

        }
    };

    private class ViewHolder {
        private TextView textViewTop;
        private TextView textViewCenter;
        private TextView textViewBottom;
        private CheckBox checkBox;

        private View btnDivide;
        private View btnEdit;

        private SwipeLayout swipeLayout;
        private LinearLayout bottom_wrapper;



        private ViewHolder(View view) {
            this.checkBox = view.findViewById(R.id.checkConfirm);
            this.textViewTop = view.findViewById(R.id.InspectedItemtitle);
            this.textViewCenter = view.findViewById(R.id.InspectedItemDecrypt);
            this.textViewBottom = view.findViewById(R.id.InspectedItemCount);
            this.swipeLayout = view.findViewById(R.id.swipe_layout_inspected);
            this.bottom_wrapper = view.findViewById(R.id.bottom_wrapper_inspected);

            this.btnDivide = view.findViewById(R.id.divide_inspected);
            this.btnEdit = view.findViewById(R.id.edit_query_inspected);
        }
    }
}
