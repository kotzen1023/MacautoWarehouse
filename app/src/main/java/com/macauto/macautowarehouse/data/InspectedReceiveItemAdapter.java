package com.macauto.macautowarehouse.data;

import android.content.Context;


import android.graphics.Color;
import android.support.annotation.NonNull;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


import android.widget.ImageView;

import android.widget.TextView;


import com.macauto.macautowarehouse.R;



import java.util.ArrayList;




public class InspectedReceiveItemAdapter extends ArrayAdapter<InspectedReceiveItem> {
    public static final String TAG = "InspectedItemAdapter";
    private LayoutInflater inflater;

    private Context mContext;

    private int layoutResourceId;
    private ArrayList<InspectedReceiveItem> items;

    public SparseBooleanArray mSparseBooleanArray;
    //private SwipeLayout preswipes=null;
    //private int pre_open_swipe = -1;

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
    View getView(final int position, View convertView, @NonNull ViewGroup parent) {

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

        final InspectedReceiveItem inspectedReceiveItem = items.get(position);
        if (inspectedReceiveItem != null) {

            //inspectedReceiveItem.setCheckBox(holder.checkBox);

            if (inspectedReceiveItem.getCol_rvu01() != null && inspectedReceiveItem.getCol_rvu01().equals("")) {

                String top = inspectedReceiveItem.getCol_pmn041() +" "+
                        mContext.getResources().getString(R.string.item_total, inspectedReceiveItem.getCol_rvb33());

                //holder.checkBox.setVisibility(View.INVISIBLE);
                holder.textViewCenter.setVisibility(View.GONE);
                holder.textViewBottom.setVisibility(View.GONE);

                holder.textViewTop.setText(top);

                //holder.btnDivide.setVisibility(View.GONE);
                //holder.btnEdit.setVisibility(View.GONE);

                view.setBackgroundColor(Color.TRANSPARENT);
            } else {

                String top = inspectedReceiveItem.getCol_rvb05();
                String center = mContext.getResources().getString(R.string.item_title_pmn041) + " " + inspectedReceiveItem.getCol_pmn041() + " " +
                        mContext.getResources().getString(R.string.item_title_rvb33) + " " + inspectedReceiveItem.getCol_rvb33();
                String bottom = mContext.getResources().getString(R.string.item_title_rvv32) + " " + inspectedReceiveItem.getCol_rvv32() + " " +
                        mContext.getResources().getString(R.string.item_title_rvv33) + " " + inspectedReceiveItem.getCol_rvv33();

                //holder.checkBox.setVisibility(View.VISIBLE);
                holder.textViewCenter.setVisibility(View.VISIBLE);
                holder.textViewBottom.setVisibility(View.VISIBLE);
                holder.textViewTop.setText(top);
                holder.textViewCenter.setText(center);
                holder.textViewBottom.setText(bottom);

                if (inspectedReceiveItem.isChecked()) {
                    holder.imageView.setVisibility(View.VISIBLE);
                } else {
                    holder.imageView.setVisibility(View.GONE);
                }

                /*if (mSparseBooleanArray.get(position)) {
                    check_stock_in.set(position, true);
                    //holder.checkBox.setChecked(true);
                } else {
                    check_stock_in.set(position, false);
                    //holder.checkBox.setChecked(false);
                }*/
                if (inspectedReceiveItem.isSelected()) {
                    view.setBackgroundColor(Color.rgb(0x4d, 0x90, 0xfe));
                } else if (!inspectedReceiveItem.isCheck_sp()) {//check sp = false
                    view.setBackgroundColor(Color.rgb(0xff, 0xd6, 0x00));
                }  else {
                    view.setBackgroundColor(Color.TRANSPARENT);
                }

                //holder.checkBox.setTag(position);
                //holder.checkBox.setOnCheckedChangeListener(mCheckedChangeListener);


                //holder.btnEdit.setOnClickListener(onEditListener(position, holder));
                //holder.btnDivide.setOnClickListener(onDivideListener(position, holder));


                //holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

                //holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, holder.bottom_wrapper);


                /*holder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                    @Override
                    public void onClose(SwipeLayout layout) {
                        Log.i(TAG, "onClose");

                        if (preswipes == layout) {

                            if (item_select > 0) {
                                items.get(item_select).setSelected(false);
                            }

                            item_select = -1;

                            Log.e(TAG, "item_select => "+item_select);

                        }

                        if (!inspectedReceiveItem.isCheck_sp()) {
                            layout.setBackgroundColor(Color.rgb(0xff, 0xd6, 0x00));
                        } else {
                            layout.setBackgroundColor(Color.TRANSPARENT);
                        }


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

                        //deselect other
                        for (int i=0; i<items.size(); i++) {

                            if (i == position) {

                                items.get(i).setSelected(true);

                            } else {
                                items.get(i).setSelected(false);

                            }
                        }

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

        Intent detailIntent = new Intent(mContext, EnteringWarehouseDetailActivity.class);
        detailIntent.putExtra("INDEX", String.valueOf(position));
        mContext.startActivity(detailIntent);


    }

    private View.OnClickListener onDivideListener(final int position, final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "divide click "+position);

                //activity.updateAdapter();
                //items.remove(position);


                if (dataTable != null && dataTable.Rows.size() > 0) {

                    //set dataTable_Batch_area
                    if (dataTable_Batch_area != null) {
                        dataTable_Batch_area.clear();
                    } else {
                        dataTable_Batch_area = new DataTable();
                    }
                    dataTable_Batch_area.TableName = "batch_area";
                    DataColumn c_locate_no = new DataColumn("rvv33");
                    DataColumn c_qty = new DataColumn("rvb33");
                    DataColumn c_stock_no = new DataColumn("rvv32");
                    DataColumn c_batch_no = new DataColumn("rvv34");
                    dataTable_Batch_area.Columns.Add(c_locate_no);
                    dataTable_Batch_area.Columns.Add(c_qty);
                    dataTable_Batch_area.Columns.Add(c_stock_no);
                    dataTable_Batch_area.Columns.Add(c_batch_no);

                    DataRow ur = dataTable_Batch_area.NewRow();
                    ur.setValue("rvv33", dataTable.getValue(position, "rvv33"));
                    ur.setValue("rvb33", dataTable.getValue(position, "rvb33"));
                    ur.setValue("rvv32", dataTable.getValue(position, "rvv32"));
                    ur.setValue("rvv34", dataTable.getValue(position, "rvv34"));

                    dataTable_Batch_area.Rows.add(ur);


                    float quantity = Float.valueOf(dataTable.getValue(position, "rvb33").toString());
                    int quantity_int = (int)quantity;

                    Intent intent = new Intent(mContext, EnteringWarehouseDividedDialogActivity.class);
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
            }
        };
    }*/

    /*private CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Log.i(TAG, "switch " + buttonView.getTag() + " checked = " + isChecked);
            //int idx = (Integer) buttonView.getTag();

            //if(isChecked == true) {
            InspectedReceiveItem inspectedReceiveItem = items.get((Integer) buttonView.getTag());
            mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
            check_stock_in.set((Integer) buttonView.getTag(), isChecked);






            Intent newNotifyIntent = new Intent();
            newNotifyIntent.setAction(Constants.ACTION.ACTION_ENTERING_WAREHOUSE_CHECKBOX_CHANGE);
            mContext.sendBroadcast(newNotifyIntent);

        }
    };*/

    private class ViewHolder {
        private TextView textViewTop;
        private TextView textViewCenter;
        private TextView textViewBottom;
        private ImageView imageView;

        //private View btnDivide;
        //private View btnEdit;

        //private SwipeLayout swipeLayout;
        //private LinearLayout bottom_wrapper;



        private ViewHolder(View view) {
            this.imageView = view.findViewById(R.id.InspectedItemImg);
            this.textViewTop = view.findViewById(R.id.InspectedItemtitle);
            this.textViewCenter = view.findViewById(R.id.InspectedItemDecrypt);
            this.textViewBottom = view.findViewById(R.id.InspectedItemCount);
            //this.swipeLayout = view.findViewById(R.id.swipe_layout_inspected);
            //this.bottom_wrapper = view.findViewById(R.id.bottom_wrapper_inspected);

            //this.btnDivide = view.findViewById(R.id.divide_inspected);
            //this.btnEdit = view.findViewById(R.id.edit_query_inspected);
        }
    }
}
