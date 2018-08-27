package com.macauto.macautowarehouse.data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.macauto.macautowarehouse.R;

import java.util.ArrayList;

public class ShippingScannedItemAdapter extends RecyclerView.Adapter<ShippingScannedItemAdapter.ViewHolder> {

    public static final String TAG = "ShippingScannedAdapter";
    private ArrayList<ShippingScannedItem> shippingScannedList;
    private Context mContext;

    public ShippingScannedItemAdapter(Context context, ArrayList<ShippingScannedItem> shippingScannedList) {
        this.shippingScannedList = shippingScannedList;
        this.mContext = context;
    }

    /*private OnItemClickListener mOnItemClickListener = null;


    //define interface
    private ShippingScannedItemAdapter.OnItemClickListener mOnItemClickListener = null;*/

    @Override
    public ShippingScannedItemAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shipment_nofitication_dg1_recyclerview_item, viewGroup, false);
        ShippingScannedItemAdapter.ViewHolder viewHolder = new ShippingScannedItemAdapter.ViewHolder(view);

        //view.setOnClickListener(this);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ShippingScannedItemAdapter.ViewHolder viewHolder, int position) {
        //Log.e(TAG, "onBindViewHolder");

        ShippingScannedItem shippingScannedItem;

        //if (!isSorted)
        shippingScannedItem = shippingScannedList.get(position);
        //else
        //searchItem = sortedSearchList.get(position);



        //Render image using Picasso library
        /*if (!TextUtils.isEmpty(feedItem.getThumbnail())) {
            Picasso.with(mContext).load(feedItem.getThumbnail())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(customViewHolder.imageView);
        }*/

        //Setting text view title
        String top = shippingScannedItem.getItem_ogc092();
        String center = shippingScannedItem.getItem_ogc09()+"  "+shippingScannedItem.getItem_ogc091();
        String bottom = shippingScannedItem.getItem_ogc12();
        viewHolder.textViewIndex.setText(String.valueOf(position+1));
        viewHolder.textViewTop.setText(top);
        viewHolder.textViewCenter.setText(center);
        viewHolder.textViewBottom.setText(bottom);
        viewHolder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {

        int ret;

        //if (!isSorted)
        ret = shippingScannedList.size();
        //else
        //    ret = sortedSearchList.size();

        return ret;
    }
    /*
    @Override
    public void onClick(View v) {

        Log.e(TAG, "onClick "+(int)v.getTag());

        Intent detailIntent = new Intent(mContext, LookupInStockDetailActivity.class);
        detailIntent.putExtra("INDEX", String.valueOf((int)v.getTag()));
        mContext.startActivity(detailIntent);

        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    public void setOnItemClickListener(ShippingScannedItemAdapter.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }*/


    class ViewHolder  extends RecyclerView.ViewHolder {
        protected TextView textViewIndex;
        protected TextView textViewTop;
        protected TextView textViewCenter;
        protected TextView textViewBottom;

        public ViewHolder(View view) {
            super(view);
            this.textViewIndex = view.findViewById(R.id.shippingDg1ItemId);
            this.textViewTop = view.findViewById(R.id.shippingDg1Itemtitle);
            this.textViewCenter = view.findViewById(R.id.shippingDg1ItemDecrypt);
            this.textViewBottom = view.findViewById(R.id.shippingDg1ItemCount);
        }
    }
}
