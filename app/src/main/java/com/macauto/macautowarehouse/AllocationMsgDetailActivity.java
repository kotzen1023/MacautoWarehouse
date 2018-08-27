package com.macauto.macautowarehouse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.macauto.macautowarehouse.data.AllocationMsgDetailItem;
import com.macauto.macautowarehouse.data.AllocationMsgDetailItemAdapter;
import com.macauto.macautowarehouse.data.Constants;
import com.macauto.macautowarehouse.data.DetailItem;
import com.macauto.macautowarehouse.data.GenerateRandomString;
import com.macauto.macautowarehouse.service.GetLotCodeVer2Service;
import com.macauto.macautowarehouse.service.GetMyMessListService;

import java.util.ArrayList;

import static com.macauto.macautowarehouse.MainActivity.emp_no;
import static com.macauto.macautowarehouse.MainActivity.k_id;


public class AllocationMsgDetailActivity extends AppCompatActivity {
    private static final String TAG = "AllocationMsgDetail";

    AllocationMsgDetailItemAdapter allocationMsgDetailItemAdapter;

    private static BroadcastReceiver mReceiver = null;
    private static boolean isRegister = false;

    private static ArrayList<AllocationMsgDetailItem> showList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allocation_msg_detail_activity);

        Intent intent = getIntent();

        ListView detailListView = findViewById(R.id.allocationDetailListView);

        String iss_date = intent.getStringExtra("ISS_DATE");
        String made_no = intent.getStringExtra("MADE_NO");
        String tag_locate_no = intent.getStringExtra("TAG_LOCATE_NO");
        String tag_stock_no = intent.getStringExtra("TAG_STOCK_NO");
        String ima03 = intent.getStringExtra("IMA03");
        String pre_get_datetime = intent.getStringExtra("PRE_GET_DATETIME");

        showList.clear();

        AllocationMsgDetailItem item1 = new AllocationMsgDetailItem();
        item1.setHeader(getResources().getString(R.string.allocation_detail_message_material_sending_date));
        item1.setContent(iss_date);
        showList.add(item1);

        AllocationMsgDetailItem item2 = new AllocationMsgDetailItem();
        item2.setHeader(getResources().getString(R.string.allocation_detail_message_work_order_no));
        item2.setContent(made_no);
        showList.add(item2);

        AllocationMsgDetailItem item3 = new AllocationMsgDetailItem();
        item3.setHeader(getResources().getString(R.string.allocation_detail_send_material_to_warehouse));
        item3.setContent(tag_locate_no);
        showList.add(item3);

        AllocationMsgDetailItem item4 = new AllocationMsgDetailItem();
        item4.setHeader(getResources().getString(R.string.allocation_detail_stock_no));
        item4.setContent(tag_stock_no);
        showList.add(item4);

        AllocationMsgDetailItem item5 = new AllocationMsgDetailItem();
        item5.setHeader(getResources().getString(R.string.allocation_detail_request));
        item5.setContent(pre_get_datetime);
        showList.add(item5);

        AllocationMsgDetailItem item6 = new AllocationMsgDetailItem();
        item6.setHeader(getResources().getString(R.string.allocation_detail_model));
        item6.setContent(ima03);
        showList.add(item6);

        AllocationMsgDetailItem item7 = new AllocationMsgDetailItem();
        item7.setHeader(getResources().getString(R.string.allocation_detail_barcode));
        showList.add(item7);

        allocationMsgDetailItemAdapter = new AllocationMsgDetailItemAdapter(this, R.layout.allocation_msg_detail_item, showList);
        detailListView.setAdapter(allocationMsgDetailItemAdapter);

        IntentFilter filter;

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {



                if (intent.getAction() != null) {

                    if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_DETAIL_FAILED)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_GET_MY_MESS_DETAIL_FAILED");



                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_GET_LOT_CODE_EMPTY)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_GET_LOT_CODE_EMPTY");

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_GET_LOT_CODE_SUCCESS)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_GET_LOT_CODE_SUCCESS");
                        String batch_no = intent.getStringExtra("BATCH_NO");

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_GET_LOT_CODE_FAILED)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_GET_LOT_CODE_FAILED");

                    } else if("unitech.scanservice.data" .equals(intent.getAction())) {
                        Log.d(TAG, "unitech.scanservice.data");
                        Bundle bundle = intent.getExtras();
                        if(bundle != null )
                        {
                            String text = bundle.getString("text");
                            Log.e(TAG, "msg = "+text);

                            if (text.length() > 0 ) {
                                int counter = 0;
                                for (int i = 0; i < text.length(); i++) {
                                    if (text.charAt(i) == '#') {
                                        counter++;
                                    }
                                }

                                Log.e(TAG, "counter = " + counter);

                                if (counter >= 2) {
                                    String codeArray[] = text.split("#");
                                    String part_no = codeArray[0];
                                    String batch_no = codeArray[1];

                                    Intent getBarcodeIntent = new Intent(AllocationMsgDetailActivity.this, GetLotCodeVer2Service.class);
                                    getBarcodeIntent.setAction(Constants.ACTION.ACTION_ALLOCATION_GET_LOT_CODE_ACTION);
                                    getBarcodeIntent.putExtra("PART_NO", part_no);
                                    getBarcodeIntent.putExtra("BARCODE", text);
                                    startService(getBarcodeIntent);

                                }
                            }

                        }
                    }

                }
            }
        };

        if (!isRegister) {
            filter = new IntentFilter();
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_DETAIL_FAILED);
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_GET_LOT_CODE_EMPTY);
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_GET_LOT_CODE_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_GET_LOT_CODE_FAILED);
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
}
