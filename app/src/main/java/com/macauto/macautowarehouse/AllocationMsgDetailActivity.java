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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.macauto.macautowarehouse.data.AllocationMsgDetailItem;
import com.macauto.macautowarehouse.data.AllocationMsgDetailItemAdapter;
import com.macauto.macautowarehouse.data.Constants;
import com.macauto.macautowarehouse.data.DetailItem;
import com.macauto.macautowarehouse.data.GenerateRandomString;
import com.macauto.macautowarehouse.service.GetLotCodeVer2Service;
import com.macauto.macautowarehouse.service.GetMyMessListService;
import com.macauto.macautowarehouse.service.GetPartNoNeedScanStatusService;
import com.macauto.macautowarehouse.service.GetVarValueService;
import com.macauto.macautowarehouse.table.DataRow;

import java.util.ArrayList;


import static com.macauto.macautowarehouse.AllocationMsgFragment.msgDataTable;
import static com.macauto.macautowarehouse.MainActivity.emp_no;
import static com.macauto.macautowarehouse.MainActivity.k_id;


public class AllocationMsgDetailActivity extends AppCompatActivity {
    private static final String TAG = "AllocationMsgDetail";

    AllocationMsgDetailItemAdapter allocationMsgDetailItemAdapter;

    private static BroadcastReceiver mReceiver = null;
    private static boolean isRegister = false;

    private TextView s_iss_date;
    private TextView sp_made_no;
    private TextView s_tag_stock_no;
    private TextView s_tag_locate_no;
    private TextView s_pre_get_datetime;
    private TextView s_ima03;

    private static String part_no;
    private static String batch_no;
    private static Button btnTransfer;

    public static ArrayList<AllocationMsgDetailItem> showList = new ArrayList<>();

    private static int current_detail_row = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allocation_msg_detail_activity);

        Intent intent = getIntent();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_chevron_left_white_24dp);
            actionBar.setTitle("");
        }

        s_iss_date = findViewById(R.id.s_iss_date);
        sp_made_no = findViewById(R.id.sp_made_no);
        s_tag_stock_no = findViewById(R.id.s_tag_stock_no);
        s_tag_locate_no = findViewById(R.id.s_tag_locate_no);
        s_pre_get_datetime = findViewById(R.id.s_pre_get_datetime);
        s_ima03 = findViewById(R.id.s_ima03);

        ListView detailListView = findViewById(R.id.allocationDetailListView);

        btnTransfer = findViewById(R.id.btnTransfer);

        String iss_date = intent.getStringExtra("ISS_DATE");
        String made_no = intent.getStringExtra("MADE_NO");
        String tag_locate_no = intent.getStringExtra("TAG_LOCATE_NO");
        String tag_stock_no = intent.getStringExtra("TAG_STOCK_NO");
        String ima03 = intent.getStringExtra("IMA03");
        String pre_get_datetime = intent.getStringExtra("PRE_GET_DATETIME");

        s_iss_date.setText(iss_date);
        sp_made_no.setText(made_no);
        s_tag_locate_no.setText(tag_locate_no);
        s_tag_stock_no.setText(tag_stock_no);
        s_ima03.setText(ima03);
        s_pre_get_datetime.setText(pre_get_datetime);

        showList.clear();

        if (msgDataTable.Rows.size() > 0) {
            for (DataRow rx : msgDataTable.Rows) {

                AllocationMsgDetailItem item = new AllocationMsgDetailItem();

                //rx.setValue("scan_sp", "N");
                //rx.setValue("scan_desc", "");

                item.setItem_part_no(rx.getValue("part_no").toString());
                item.setItem_ima021(rx.getValue("ima021").toString());
                item.setItem_qty(rx.getValue("qty").toString());
                item.setItem_src_stock_no(rx.getValue("src_stock_no").toString());
                item.setItem_src_locate_no(rx.getValue("src_locate_no").toString());
                item.setItem_src_batch_no(rx.getValue("src_batch_no").toString());
                item.setItem_sfa12(rx.getValue("sfa12").toString());
                item.setItem_scan_desc(rx.getValue("scan_desc").toString());

                showList.add(item);
            }
        }

        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent checkIntent = new Intent(AllocationMsgDetailActivity.this, GetVarValueService.class);
                checkIntent.setAction(Constants.ACTION.ACTION_ALLOCATION_GET_TAG_ID_ACTION);
                checkIntent.putExtra("TAG_ID", "MOVE_TAKE_SCAN_LOCK_SP");
                startService(checkIntent);
            }
        });

        /*AllocationMsgDetailItem item1 = new AllocationMsgDetailItem();
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
        showList.add(item7);*/

        allocationMsgDetailItemAdapter = new AllocationMsgDetailItemAdapter(this, R.layout.allocation_msg_detail_swipe_item, showList);
        detailListView.setAdapter(allocationMsgDetailItemAdapter);

        IntentFilter filter;

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {



                if (intent.getAction() != null) {

                    if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_GET_LOT_CODE_EMPTY)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_GET_LOT_CODE_EMPTY");

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_GET_LOT_CODE_SUCCESS)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_GET_LOT_CODE_SUCCESS");
                        String ret_batch_no = intent.getStringExtra("BATCH_NO");
                        Log.e(TAG, "return batch_no = "+ret_batch_no+", scanned batch_no = "+batch_no);
                        batch_no = ret_batch_no;

                        boolean found = false;
                        int index = 0;
                        for (DataRow yx : msgDataTable.Rows) {
                            if (yx.getValue(4).toString().equals(part_no) && yx.getValue(9).toString().equals(batch_no)) {
                                yx.setValue(17, "Y");
                                yx.setValue("scan_desc", getResources().getString(R.string.allocation_detail_scanned));
                                showList.get(index).setItem_scan_desc(getResources().getString(R.string.allocation_detail_scanned));
                                found = true;
                            }
                            index = index + 1;
                        }

                        if (!found)
                            toast(getResources().getString(R.string.allocation_detail_scanned_not_found));
                        else {
                            if (allocationMsgDetailItemAdapter != null) {
                                allocationMsgDetailItemAdapter.notifyDataSetChanged();
                            }
                        }
                        //check if all scan

                        found = false;
                        for (DataRow yx : msgDataTable.Rows) {
                            if (yx.getValue("scan_desc").toString().equals("")) { //found not scan
                                found = true;
                                break;
                            }

                        }

                        if (!found) {
                            btnTransfer.setEnabled(true);
                        } else {
                            btnTransfer.setEnabled(false);
                        }

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_GET_LOT_CODE_FAILED)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_GET_LOT_CODE_FAILED");

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_SWIPE_LAYOUT_DELETE_ROW)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_SWIPE_LAYOUT_DELETE_ROW");

                        String delete_row_string = intent.getStringExtra("DELETE_ROW");
                        int delete_row = Integer.valueOf(delete_row_string);

                        msgDataTable.Rows.remove(delete_row);
                        showList.remove(delete_row);

                        if (allocationMsgDetailItemAdapter != null) {
                            allocationMsgDetailItemAdapter.notifyDataSetChanged();
                        }

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_GET_TAG_ID_SUCCESS)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_GET_TAG_ID_SUCCESS");

                        String ret = intent.getStringExtra("GET_VAR_VALUE_RETURN");


                        if (ret.equals("YES")) {
                            current_detail_row = msgDataTable.Rows.size() - 1;


                            Intent getPartNoCheckIntent = new Intent(AllocationMsgDetailActivity.this, GetPartNoNeedScanStatusService.class);
                            getPartNoCheckIntent.setAction(Constants.ACTION.ACTION_GET_PART_NO_NEED_SCAN_STATUS_ACTION);
                            getPartNoCheckIntent.putExtra("PART_NO", msgDataTable.Rows.get(current_detail_row).getValue("part_no").toString());
                            startService(getPartNoCheckIntent);
                        }

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_GET_TAG_ID_FAILED)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_GET_TAG_ID_FAILED");

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_GET_PART_NO_NEED_SCAN_STATUS_YES)) {
                        Log.d(TAG, "receive ACTION_GET_PART_NO_NEED_SCAN_STATUS_YES");

                        if (!msgDataTable.Rows.get(current_detail_row).getValue("scan_sp1").toString().equals("Y")) {
                            toast(getResources().getString(R.string.allocation_detail_should_scan, msgDataTable.Rows.get(current_detail_row).getValue("part_no").toString()));
                        }


                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_GET_PART_NO_NEED_SCAN_STATUS_NO)) {
                        Log.d(TAG, "receive ACTION_GET_PART_NO_NEED_SCAN_STATUS_NO");

                        //check next
                        current_detail_row = current_detail_row -1;

                        if (current_detail_row >= 0) {
                            Intent getPartNoCheckIntent = new Intent(AllocationMsgDetailActivity.this, GetPartNoNeedScanStatusService.class);
                            getPartNoCheckIntent.setAction(Constants.ACTION.ACTION_GET_PART_NO_NEED_SCAN_STATUS_ACTION);
                            getPartNoCheckIntent.putExtra("PART_NO", msgDataTable.Rows.get(current_detail_row).getValue("part_no").toString());
                            startService(getPartNoCheckIntent);
                        } else { //current_detail_row = -1
                            //start generate
                            Log.e(TAG, " == Start remove column");

                            //start from original code line 1881
                            msgDataTable.Columns.Remove("scan_desc");
                        }

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_GET_PART_NO_NEED_SCAN_STATUS_FAILED)) {
                        Log.d(TAG, "receive ACTION_GET_PART_NO_NEED_SCAN_STATUS_FAILED");

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

                                if (counter >= 0) {
                                    String codeArray[] = text.split("#");
                                    part_no = codeArray[0];
                                    batch_no = codeArray[1];

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
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_SWIPE_LAYOUT_DELETE_ROW);
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
        Toast toast = Toast.makeText(AllocationMsgDetailActivity.this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }
}
