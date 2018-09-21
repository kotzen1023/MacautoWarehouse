package com.macauto.macautowarehouse.data;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.macauto.macautowarehouse.EnteringWarehouseDetailActivity;
import com.macauto.macautowarehouse.EnteringWarehouseDividedDialogActivity;
import com.macauto.macautowarehouse.ProductionStorageDetailActivity;
import com.macauto.macautowarehouse.R;

import java.util.ArrayList;

import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.dataTable;
import static com.macauto.macautowarehouse.ProductionStorageFragment.item_select;
import static com.macauto.macautowarehouse.ProductionStorageFragment.dataTable_RR;

public class ProductionStorageItemAdapter extends ArrayAdapter<ProductionStorageItem> {
    public static final String TAG = "PStorageItemAdapter";
    private LayoutInflater inflater;

    private Context mContext;

    private int layoutResourceId;
    private ArrayList<ProductionStorageItem> items;

    private SwipeLayout preswipes=null;
    private int pre_open_swipe = -1;

    public ProductionStorageItemAdapter(Context context, int textViewResourceId,
                                          ArrayList<ProductionStorageItem> objects) {
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

    public ProductionStorageItem getItem(int position)
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

        final ProductionStorageItem productionStorageItem = items.get(position);
        if (productionStorageItem != null) {

            //holder.textViewIndex.setText(String.valueOf(position+1));
            String topString = mContext.getResources().getString(R.string.production_storage_part_no)+" "+productionStorageItem.getPart_no();
            String centerString = mContext.getResources().getString(R.string.production_storage_qty)+" "+productionStorageItem.getQty()+" "+productionStorageItem.getStock_unit();
            String bottomString = mContext.getResources().getString(R.string.production_storage_locate_no_def)+" "+productionStorageItem.getLocate_no()+"     "+
                    mContext.getResources().getString(R.string.production_storage_locate_no)+" "+productionStorageItem.getLocate_no_scan();
            holder.textViewTop.setText(topString);
            holder.textViewCenter.setText(centerString);
            holder.textViewBottom.setText(bottomString);
            //holder.itemTitle.setText(allocationMsgStatusItem.getItem_SFA03());
            //holder.itemDate.setText(allocationMsgItem.getDate());


            if (productionStorageItem.isSelected()) {
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

                        if (item_select > 0) {
                            items.get(item_select).setSelected(false);
                        }


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



        Intent detailIntent = new Intent(mContext, ProductionStorageDetailActivity.class);
        detailIntent.putExtra("INDEX", String.valueOf(position));
        mContext.startActivity(detailIntent);


    }

    private View.OnClickListener onDeleteListener(final int position, final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "delete click "+position);

                if (dataTable_RR != null && dataTable_RR.Rows.get(position) != null) {
                    dataTable_RR.Rows.remove(position);
                }

                items.remove(position);
                //holder.swipeLayout.close();

                item_select = -1;

                Intent newNotifyIntent = new Intent();
                newNotifyIntent.setAction(Constants.ACTION.ACTION_PRODUCT_SWIPE_LAYOUT_UPDATE);
                mContext.sendBroadcast(newNotifyIntent);

            }
        };
    }

    private class ViewHolder  {
        //TextView textViewIndex;
        TextView textViewTop;
        TextView textViewCenter;
        TextView textViewBottom;

        //private View btnDelete;
        //private View btnEdit;

        //private SwipeLayout swipeLayout;
        //private LinearLayout bottom_wrapper;

        private ViewHolder(View view) {
            //this.textViewIndex = view.findViewById(R.id.productItemId);
            this.textViewTop = view.findViewById(R.id.productItemtitle);
            this.textViewCenter = view.findViewById(R.id.productItemDecrypt);
            this.textViewBottom = view.findViewById(R.id.productItemCount);

            //this.swipeLayout = view.findViewById(R.id.swipe_layout_production_storage);
            //this.bottom_wrapper = view.findViewById(R.id.bottom_wrapper_production_storage);
            //this.btnDelete = view.findViewById(R.id.delete_production_storage);
            //this.btnEdit = view.findViewById(R.id.edit_query_production_storage);
        }
    }
}
