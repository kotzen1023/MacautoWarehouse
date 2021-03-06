package com.macauto.macautowarehouse;

import android.content.BroadcastReceiver;

import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;



import com.macauto.macautowarehouse.data.AllocationMsgShowItem;
import com.macauto.macautowarehouse.data.AllocationMsgShowItemAdapter;


import java.util.ArrayList;



public class AllocationShowActivity extends AppCompatActivity {
    private static final String TAG = "AllocationShowActivity";

    public static ArrayList<AllocationMsgShowItem> detailList = new ArrayList<>();

    private static BroadcastReceiver mReceiver = null;
    private static boolean isRegister = false;

    //private SearchDetailItemAdapter searchDetailItemAdapter;
    //InputMethodManager imm;
    private AllocationMsgShowItemAdapter allocationMsgShowItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allocation_msg_show_activity);

        ListView listView = findViewById(R.id.allocatonShowListView);

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

        // V
        /*InspectedDetailItem item1 = new InspectedDetailItem();
        item1.setHeader(getResources().getString(R.string.item_title_check_sp));
        item1.setContent(String.valueOf(swipe_list.get(index).isCheck_sp()));
        detailList.add(item1);
        //rvu01
        InspectedDetailItem item2 = new InspectedDetailItem();
        item2.setHeader(getResources().getString(R.string.item_title_rvu01));
        item2.setContent(swipe_list.get(index).getCol_rvu01());
        detailList.add(item2);
        //rvv02
        InspectedDetailItem item3 = new InspectedDetailItem();
        item3.setHeader(getResources().getString(R.string.item_title_rvv02));
        item3.setContent(swipe_list.get(index).getCol_rvv02());
        detailList.add(item3);
        //rvb05
        InspectedDetailItem item4 = new InspectedDetailItem();
        item4.setHeader(getResources().getString(R.string.item_title_rvb05));
        item4.setContent(swipe_list.get(index).getCol_rvb05());
        detailList.add(item4);
        //pmn041
        InspectedDetailItem item5 = new InspectedDetailItem();
        item5.setHeader(getResources().getString(R.string.item_title_pmn041));
        item5.setContent(swipe_list.get(index).getCol_pmn041());
        detailList.add(item5);
        //ima021
        InspectedDetailItem item6 = new InspectedDetailItem();
        item6.setHeader(getResources().getString(R.string.item_title_ima021));
        item6.setContent(swipe_list.get(index).getCol_ima021());
        detailList.add(item6);
        //rvv32
        InspectedDetailItem item7 = new InspectedDetailItem();
        item7.setHeader(getResources().getString(R.string.item_title_rvv32));
        item7.setContent(swipe_list.get(index).getCol_rvv32());
        detailList.add(item7);
        //rvv33
        InspectedDetailItem item8 = new InspectedDetailItem();
        item8.setHeader(getResources().getString(R.string.item_title_rvv33));
        item8.setContent(swipe_list.get(index).getCol_rvv33());
        detailList.add(item8);
        //rvv34
        InspectedDetailItem item9 = new InspectedDetailItem();
        item9.setHeader(getResources().getString(R.string.item_title_rvv34));
        item9.setContent(swipe_list.get(index).getCol_rvv34());
        detailList.add(item9);
        //rvb33
        InspectedDetailItem item10 = new InspectedDetailItem();
        item10.setHeader(getResources().getString(R.string.item_title_rvb33));
        item10.setContent(swipe_list.get(index).getCol_rvb33());
        detailList.add(item10);
        //pmc03
        InspectedDetailItem item11 = new InspectedDetailItem();
        item11.setHeader(getResources().getString(R.string.item_title_pmc03));
        item11.setContent(swipe_list.get(index).getCol_pmc03());
        detailList.add(item11);
        //gen02
        InspectedDetailItem item12 = new InspectedDetailItem();
        item12.setHeader(getResources().getString(R.string.item_title_gen02));
        item12.setContent(swipe_list.get(index).getCol_gen02());
        detailList.add(item12);


        inspectedDetailItemAdapter = new InspectedDetailItemAdapter(EnteringWarehouseDetailActivity.this, R.layout.inspected_receive_list_detail_item, detailList);
        listView.setAdapter(inspectedDetailItemAdapter);*/


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

    public void toast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }
}
