package com.macauto.macautowarehouse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.macauto.macautowarehouse.data.SearchDetailItem;
import com.macauto.macautowarehouse.data.SearchDetailItemAdapter;

import java.util.ArrayList;

import static com.macauto.macautowarehouse.LookupInStockFragment.isSorted;
import static com.macauto.macautowarehouse.MainActivity.searchList;
import static com.macauto.macautowarehouse.MainActivity.sortedSearchList;


public class LookupInStockDetailActivity extends AppCompatActivity {
    private static final String TAG = "LookupInStockDetail";

    public static ArrayList<SearchDetailItem> detailList = new ArrayList<>();

    //private SearchDetailItemAdapter searchDetailItemAdapter;
    //InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.look_up_in_stock_detail_activity);

        ListView listView = findViewById(R.id.searchDetailListView);

        Intent intent = getIntent();
        //setTitle(getResources().getString(R.string.entering_warehouse_dialog_head));

        //imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        //View view = getCurrentFocus();
        //imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_chevron_left_white_24dp);
            actionBar.setTitle("");
        }

        int index = Integer.valueOf(intent.getStringExtra("INDEX"));
        detailList.clear();

        //item_IMG01
        SearchDetailItem item1 = new SearchDetailItem();
        item1.setHeader(getResources().getString(R.string.look_up_in_stock_part_no));
        if (!isSorted)
            item1.setContent(searchList.get(index).getItem_IMG01());
        else
            item1.setContent(sortedSearchList.get(index).getItem_IMG01());
        detailList.add(item1);
        //item_IMA02
        SearchDetailItem item2 = new SearchDetailItem();
        item2.setHeader(getResources().getString(R.string.look_up_in_stock_part_name));
        if (!isSorted)
            item2.setContent(searchList.get(index).getItem_IMA02());
        else
            item2.setContent(sortedSearchList.get(index).getItem_IMA02());
        detailList.add(item2);
        //item_IMA021
        SearchDetailItem item3 = new SearchDetailItem();
        item3.setHeader(getResources().getString(R.string.look_up_in_stock_spec));
        if (!isSorted)
            item3.setContent(searchList.get(index).getItem_IMA021());
        else
            item3.setContent(sortedSearchList.get(index).getItem_IMA021());
        detailList.add(item3);
        //item_IMG02
        SearchDetailItem item4 = new SearchDetailItem();
        item4.setHeader(getResources().getString(R.string.look_up_in_stock_stock_no));
        if (!isSorted)
            item4.setContent(searchList.get(index).getItem_IMG02());
        else
            item4.setContent(sortedSearchList.get(index).getItem_IMG02());
        detailList.add(item4);
        //item_IMD02
        SearchDetailItem item5 = new SearchDetailItem();
        item5.setHeader(getResources().getString(R.string.look_up_in_stock_stock_name));
        if (!isSorted)
            item5.setContent(searchList.get(index).getItem_IMD02());
        else
            item5.setContent(sortedSearchList.get(index).getItem_IMD02());
        detailList.add(item5);
        //item_IMG03
        SearchDetailItem item6 = new SearchDetailItem();
        item6.setHeader(getResources().getString(R.string.look_up_in_stock_stock_locate));
        if (!isSorted)
            item6.setContent(searchList.get(index).getItem_IMG03());
        else
            item6.setContent(sortedSearchList.get(index).getItem_IMG03());
        detailList.add(item6);
        //item_IMG04
        SearchDetailItem item7 = new SearchDetailItem();
        item7.setHeader(getResources().getString(R.string.look_up_in_stock_batch_no));
        if (!isSorted)
            item7.setContent(searchList.get(index).getItem_IMG04());
        else
            item7.setContent(sortedSearchList.get(index).getItem_IMG04());
        detailList.add(item7);
        //item_IMG10
        SearchDetailItem item8 = new SearchDetailItem();
        item8.setHeader(getResources().getString(R.string.look_up_in_stock_quantity));
        if (!isSorted)
            item8.setContent(searchList.get(index).getItem_IMG10());
        else
            item8.setContent(sortedSearchList.get(index).getItem_IMG10());
        detailList.add(item8);
        //item_IMA25
        SearchDetailItem item9 = new SearchDetailItem();
        item9.setHeader(getResources().getString(R.string.look_up_in_stock_part_stock_quantity));
        if (!isSorted)
            item9.setContent(searchList.get(index).getItem_IMA25());
        else
            item9.setContent(sortedSearchList.get(index).getItem_IMA25());
        detailList.add(item9);
        //item_IMG23
        SearchDetailItem item10 = new SearchDetailItem();
        item10.setHeader(getResources().getString(R.string.look_up_in_stock_available));
        if (!isSorted)
            item10.setContent(searchList.get(index).getItem_IMG23());
        else
            item10.setContent(sortedSearchList.get(index).getItem_IMG23());
        detailList.add(item10);
        //item_IMA08
        SearchDetailItem item11 = new SearchDetailItem();
        item11.setHeader(getResources().getString(R.string.look_up_in_stock_source_code));
        if (!isSorted)
            item11.setContent(searchList.get(index).getItem_IMA08());
        else
            item11.setContent(sortedSearchList.get(index).getItem_IMA08());
        detailList.add(item11);
        //item_STOCK_MAN
        SearchDetailItem item12 = new SearchDetailItem();
        item12.setHeader(getResources().getString(R.string.look_up_in_stock_stock_man));
        if (!isSorted)
            item12.setContent(searchList.get(index).getItem_STOCK_MAN());
        else
            item12.setContent(sortedSearchList.get(index).getItem_STOCK_MAN());
        detailList.add(item12);
        //item_IMA03
        SearchDetailItem item13 = new SearchDetailItem();
        item13.setHeader(getResources().getString(R.string.look_up_in_stock_model));
        if (!isSorted)
            item13.setContent(searchList.get(index).getItem_IMA03());
        else
            item13.setContent(sortedSearchList.get(index).getItem_IMA03());
        detailList.add(item13);
        //item_PMC03
        SearchDetailItem item14 = new SearchDetailItem();
        item14.setHeader(getResources().getString(R.string.look_up_in_stock_vender_name));
        if (!isSorted)
            item14.setContent(searchList.get(index).getItem_PMC03());
        else
            item14.setContent(sortedSearchList.get(index).getItem_PMC03());
        detailList.add(item14);

        SearchDetailItemAdapter searchDetailItemAdapter = new SearchDetailItemAdapter(LookupInStockDetailActivity.this, R.layout.look_up_in_stock_detail_item, detailList);
        listView.setAdapter(searchDetailItemAdapter);
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        finish();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        Log.e(TAG, "onCreateOptionsMenu");

        //getMenuInflater().inflate(R.menu.divided_activity_menu, menu);








        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }

        return true;
    }
}
