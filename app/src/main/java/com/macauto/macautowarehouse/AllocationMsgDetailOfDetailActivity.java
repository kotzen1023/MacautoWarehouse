package com.macauto.macautowarehouse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.macauto.macautowarehouse.data.Constants;
import com.macauto.macautowarehouse.data.InspectedDetailItem;
import com.macauto.macautowarehouse.data.InspectedDetailItemAdapter;

import java.util.ArrayList;

import static com.macauto.macautowarehouse.AllocationMsgDetailActivity.showList;

public class AllocationMsgDetailOfDetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailOfDetailActivity";

    public static ArrayList<InspectedDetailItem> detailList = new ArrayList<>();

    private static BroadcastReceiver mReceiver = null;
    private static boolean isRegister = false;

    //private SearchDetailItemAdapter searchDetailItemAdapter;
    //InputMethodManager imm;
    private InspectedDetailItemAdapter inspectedDetailItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allocation_msg_detail_of_detail_activity);

        Intent intent = getIntent();

        final int index = Integer.valueOf(intent.getStringExtra("INDEX"));
        //final String barcode = intent.getStringExtra("BARCODE");

        ListView listView = findViewById(R.id.detailDetailListView);



        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_chevron_left_white_24dp);
            actionBar.setTitle("");
        }


        detailList.clear();

        // part_no
        InspectedDetailItem item1 = new InspectedDetailItem();
        item1.setHeader(getResources().getString(R.string.allocation_item_part_no));
        item1.setContent(String.valueOf(showList.get(index).getItem_part_no()));
        detailList.add(item1);
        //ima021
        InspectedDetailItem item2 = new InspectedDetailItem();
        item2.setHeader(getResources().getString(R.string.allocation_item_name));
        item2.setContent(showList.get(index).getItem_ima021());
        detailList.add(item2);
        //qty
        InspectedDetailItem item3 = new InspectedDetailItem();
        item3.setHeader(getResources().getString(R.string.allocation_item_qty));
        item3.setContent(showList.get(index).getItem_qty());
        detailList.add(item3);
        //src stock
        InspectedDetailItem item4 = new InspectedDetailItem();
        item4.setHeader(getResources().getString(R.string.allocation_item_src_stock));
        item4.setContent(showList.get(index).getItem_src_stock_no());
        detailList.add(item4);
        //src locate no
        InspectedDetailItem item5 = new InspectedDetailItem();
        item5.setHeader(getResources().getString(R.string.allocation_item_src_locate));
        item5.setContent(showList.get(index).getItem_src_locate_no());
        detailList.add(item5);
        //src batch no
        InspectedDetailItem item6 = new InspectedDetailItem();
        item6.setHeader(getResources().getString(R.string.allocation_item_src_batch));
        item6.setContent(showList.get(index).getItem_src_batch_no());
        detailList.add(item6);
        //sfa12
        InspectedDetailItem item7 = new InspectedDetailItem();
        item7.setHeader(getResources().getString(R.string.allocation_item_sfa12));
        item7.setContent(showList.get(index).getItem_sfa12());
        detailList.add(item7);
        //scan desc
        InspectedDetailItem item8 = new InspectedDetailItem();
        item8.setHeader(getResources().getString(R.string.allocation_item_scan_desc));
        item8.setContent(showList.get(index).getItem_scan_desc());
        detailList.add(item8);
        //rvv34
        /*InspectedDetailItem item9 = new InspectedDetailItem();
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
        detailList.add(item12);*/


        inspectedDetailItemAdapter = new InspectedDetailItemAdapter(AllocationMsgDetailOfDetailActivity.this, R.layout.inspected_receive_list_detail_item, detailList);
        listView.setAdapter(inspectedDetailItemAdapter);

        final IntentFilter filter;

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //Log.e(TAG, "intent.getAction() =>>>> "+intent.getAction().toString());

                if (intent.getAction() != null) {

                    if("unitech.scanservice.data".equals(intent.getAction())) {
                        Log.d(TAG, "unitech.scanservice.data");
                        Bundle bundle = intent.getExtras();
                        if(bundle != null )
                        {
                            String text = bundle.getString("text");
                            Log.e(TAG, "msg = "+text);

                            if (text != null && text.length() > 0 ) {

                                int counter = 0;
                                for( int i=0; i<text.length(); i++ ) {
                                    if( text.charAt(i) == '#' ) {
                                        counter++;
                                    }
                                }

                                Log.e(TAG, "counter = "+counter);

                                /*if (counter >= 1) {

                                    toast(getResources().getString(R.string.not_locate_no_msg));


                                } else {
                                    text = text.replaceAll("\\n","");
                                    toast(text);

                                    //if (text.length() == 6) {

                                    if (item_select != -1) { //scan locate
                                        if (dataTable != null && dataTable.Rows.size() > 0) {
                                            dataTable.Rows.get(item_select).setValue("rvv33", text);
                                            dataTable.Rows.get(item_select).setValue("rvv33_scan", text);
                                        }
                                        swipe_list.get(item_select).setCol_rvv33(text);
                                        swipe_list.get(item_select).setChecked(true);
                                        //check_stock_in.set(item_select, true);

                                        detailList.get(7).setContent(text);

                                        if (inspectedDetailItemAdapter != null)
                                            inspectedDetailItemAdapter.notifyDataSetChanged();
                                    }
                                    //}


                                }*/

                            }
                        }
                    }

                }
            }
        };

        if (!isRegister) {
            filter = new IntentFilter();


            filter.addAction("unitech.scanservice.data");

            registerReceiver(mReceiver, filter);
            isRegister = true;
            Log.d(TAG, "registerReceiver mReceiver");
        }
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");

        if (isRegister && mReceiver != null) {
            try {
                unregisterReceiver(mReceiver);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            isRegister = false;
            mReceiver = null;
            Log.d(TAG, "unregisterReceiver mReceiver");
        }

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
