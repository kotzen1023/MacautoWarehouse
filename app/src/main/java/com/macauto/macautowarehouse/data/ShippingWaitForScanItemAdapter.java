package com.macauto.macautowarehouse.data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.macauto.macautowarehouse.R;

import java.util.ArrayList;

public class ShippingWaitForScanItemAdapter extends RecyclerView.Adapter<ShippingWaitForScanItemAdapter.ViewHolder> {

    public static final String TAG = "ShippingWaitScanAdapter";
    private ArrayList<ShippingWaitForScanItem> shippingWaitForScanList;
    private Context mContext;

    public ShippingWaitForScanItemAdapter(Context context, ArrayList<ShippingWaitForScanItem> shippingWaitForScanList) {
        this.shippingWaitForScanList = shippingWaitForScanList;
        this.mContext = context;
    }

    //private ShippingWaitForScanItemAdapter.OnItemClickListener mOnItemClickListener = null;

    @Override
    public ShippingWaitForScanItemAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shipment_nofitication_dg1_recyclerview_item, viewGroup, false);
        ShippingWaitForScanItemAdapter.ViewHolder viewHolder = new ShippingWaitForScanItemAdapter.ViewHolder(view);

        //view.setOnClickListener(this);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ShippingWaitForScanItemAdapter.ViewHolder viewHolder, int position) {
        //Log.e(TAG, "onBindViewHolder");

        ShippingWaitForScanItem shippingWaitForScanItem;

        //if (!isSorted)
        shippingWaitForScanItem = shippingWaitForScanList.get(position);
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
        String top = shippingWaitForScanItem.getItem_img03();
        String center = shippingWaitForScanItem.getItem_img01()+"  "+shippingWaitForScanItem.getItem_img02();
        String bottom = shippingWaitForScanItem.getItem_img04();
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
        ret = shippingWaitForScanList.size();
        //else
        //    ret = sortedSearchList.size();

        return ret;
    }

    /*@Override
    public void onClick(View v) {

        Log.e(TAG, "onClick "+(int)v.getTag());

        Intent detailIntent = new Intent(mContext, LookupInStockDetailActivity.class);
        detailIntent.putExtra("INDEX", String.valueOf((int)v.getTag()));
        mContext.startActivity(detailIntent);

        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    public void setOnItemClickListener(SearchItemAdapter.OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }*/

    class ViewHolder  extends RecyclerView.ViewHolder {
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
