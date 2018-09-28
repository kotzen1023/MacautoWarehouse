package com.macauto.macautowarehouse.data;

import android.content.Context;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.macauto.macautowarehouse.R;

import java.util.ArrayList;


public class ShippingNotificationItemAdapter extends RecyclerView.Adapter<ShippingNotificationItemAdapter.ViewHolder> implements View.OnClickListener {
    public static final String TAG = "ShippingNotifyAdapter";
    private ArrayList<ShippingNotificationItem> shippingList;
    private Context mContext;

    public ShippingNotificationItemAdapter(Context context, ArrayList<ShippingNotificationItem> shippingList) {
        this.shippingList = shippingList;
        this.mContext = context;
    }

    private SearchItemAdapter.OnItemClickListener mOnItemClickListener = null;

    @Override
    public @NonNull ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shipment_nofitication_dg1_recyclerview_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        view.setOnClickListener(this);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        //Log.e(TAG, "onBindViewHolder");

        ShippingNotificationItem shippingNotificationItem;

        //if (!isSorted)
        shippingNotificationItem = shippingList.get(position);
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
        String top = shippingNotificationItem.getItem_ogb04();
        String center = shippingNotificationItem.getItem_ogb12()+"  "+shippingNotificationItem.getItem_scanned_qty();
        String bottom = shippingNotificationItem.getItem_ogb09();
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
            ret = shippingList.size();
        //else
        //    ret = sortedSearchList.size();

        return ret;
    }

    @Override
    public void onClick(View v) {

        Log.e(TAG, "onClick "+(int)v.getTag());

        /*Intent detailIntent = new Intent(mContext, LookupInStockDetailActivity.class);
        detailIntent.putExtra("INDEX", String.valueOf((int)v.getTag()));
        mContext.startActivity(detailIntent);

        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }*/
    }

    public void setOnItemClickListener(SearchItemAdapter.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }


    public class ViewHolder  extends RecyclerView.ViewHolder {
        private TextView textViewIndex;
        private TextView textViewTop;
        private TextView textViewCenter;
        private TextView textViewBottom;

        public ViewHolder(View view) {
            super(view);
            this.textViewIndex = view.findViewById(R.id.shippingDg1ItemId);
            this.textViewTop = view.findViewById(R.id.shippingDg1Itemtitle);
            this.textViewCenter = view.findViewById(R.id.shippingDg1ItemDecrypt);
            this.textViewBottom = view.findViewById(R.id.shippingDg1ItemCount);
        }
    }
}
