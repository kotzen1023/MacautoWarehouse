package com.macauto.macautowarehouse.data;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.macauto.macautowarehouse.R;
import com.macauto.macautowarehouse.table.LookupInStockDetailActivity;

import java.util.ArrayList;
import java.util.List;

import static com.macauto.macautowarehouse.LookupInStockFragment.isSorted;
import static com.macauto.macautowarehouse.MainActivity.sortedSearchList;

public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemAdapter.ViewHolder> implements View.OnClickListener {
    public static final String TAG = "SearchItemAdapter";
    private ArrayList<SearchItem> searchList;
    private Context mContext;

    public SearchItemAdapter(Context context, ArrayList<SearchItem> searchList) {
        this.searchList = searchList;
        this.mContext = context;
    }

    private OnItemClickListener mOnItemClickListener = null;


    //define interface
    public interface OnItemClickListener {

        void onItemClick(View view , int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.look_up_in_stock_recyclerview_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        view.setOnClickListener(this);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        //Log.e(TAG, "onBindViewHolder");

        SearchItem searchItem;

        if (!isSorted)
            searchItem = searchList.get(position);
        else
            searchItem = sortedSearchList.get(position);



        //Render image using Picasso library
        /*if (!TextUtils.isEmpty(feedItem.getThumbnail())) {
            Picasso.with(mContext).load(feedItem.getThumbnail())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(customViewHolder.imageView);
        }*/

        //Setting text view title
        String top = searchItem.getItem_IMG01();
        String center = searchItem.getItem_IMA021();
        String bottom = searchItem.getItem_IMA02()+"  "+searchItem.getItem_IMG10()+ " "+searchItem.getItem_IMA25();
        viewHolder.textViewIndex.setText(String.valueOf(position+1));
        viewHolder.textViewTop.setText(top);
        viewHolder.textViewCenter.setText(center);
        viewHolder.textViewBottom.setText(bottom);
        viewHolder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {

        int ret;

        if (!isSorted)
            ret = searchList.size();
        else
            ret = sortedSearchList.size();

        return ret;
    }

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

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    class ViewHolder  extends RecyclerView.ViewHolder {
        protected TextView textViewIndex;
        protected TextView textViewTop;
        protected TextView textViewCenter;
        protected TextView textViewBottom;

        public ViewHolder(View view) {
            super(view);
            this.textViewIndex = view.findViewById(R.id.searchItemId);
            this.textViewTop = view.findViewById(R.id.searchItemtitle);
            this.textViewCenter = view.findViewById(R.id.searchItemDecrypt);
            this.textViewBottom = view.findViewById(R.id.searchItemCount);
        }
    }
}
