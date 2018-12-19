package com.macauto.macautowarehouse;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;


import com.macauto.macautowarehouse.data.AllocationSendMsgDetailItem;
import com.macauto.macautowarehouse.data.AllocationSendMsgDetailItemAdapter;
import com.macauto.macautowarehouse.data.Constants;


import java.util.ArrayList;

import static com.macauto.macautowarehouse.MainActivity.sendMsgDetailList;


public class AllocationSendMsgStatusDetailActivity extends AppCompatActivity {
    private static final String TAG = "SendMsgStatusDetail";

    //public static ArrayList<AllocationSendMsgDetailItem> detailList = new ArrayList<>();

    //private AllocationSendMsgDetailItemAdapter allocationSendMsgDetailItemAdapter;
    //InputMethodManager imm;
    private static int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allocation_send_msg_status_detail_activity);

        ListView listView = findViewById(R.id.allocationSendMsgDetailListView);

        Intent intent = getIntent();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_chevron_left_white_24dp);
            actionBar.setTitle("");
        }

        index = Integer.valueOf(intent.getStringExtra("INDEX"));


        String item_SFA03 = intent.getStringExtra("ITEM_SFA03");
        String item_IMA021 = intent.getStringExtra("ITEM_IMA021");
        String item_IMG10 = intent.getStringExtra("ITEM_IMG10");
        String item_MOVED_QTY = intent.getStringExtra("ITEM_MOVED_QTY");
        String item_MESS_QTY = intent.getStringExtra("ITEM_MESS_QTY");
        String item_SFA05 = intent.getStringExtra("ITEM_SFA05");
        String item_SFA12 = intent.getStringExtra("ITEM_SFA12");
        String item_SFA11_NAME = intent.getStringExtra("ITEM_SFA11_NAME");
        String item_TC_OBF013 = intent.getStringExtra("ITEM_TC_OBF013");
        //setTitle(getResources().getString(R.string.entering_warehouse_dialog_head));

        //imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        //View view = getCurrentFocus();
        //imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        float aw1_float = Float.valueOf(item_IMG10);
        int aw1 = (int) aw1_float;
        int aw2 = Integer.valueOf(item_MOVED_QTY);
        int aw3 = Integer.valueOf(item_SFA05);
        int aw4 = Integer.valueOf(item_TC_OBF013);
        float aw5_float = Float.valueOf(item_MESS_QTY);
        int aw5 = (int)aw5_float;

        if (aw1 > (aw3 - aw2 - aw5)) {
            aw1 = aw3 - aw2 - aw5;
            aw1 = aw1 < 0 ? 0 : aw1;
        }

        aw1 = aw1 > aw4 ? aw4 : aw1;

        sendMsgDetailList.clear();

        //item_SFA03
        AllocationSendMsgDetailItem item1 = new AllocationSendMsgDetailItem();
        item1.setHeader(getResources().getString(R.string.allocation_send_message_to_material_status_detail_SFA03));
        item1.setContent(item_SFA03);
        sendMsgDetailList.add(item1);
        //item_IMA021
        AllocationSendMsgDetailItem item2 = new AllocationSendMsgDetailItem();
        item2.setHeader(getResources().getString(R.string.allocation_send_message_to_material_status_detail_IMA021));
        item2.setContent(item_IMA021);
        sendMsgDetailList.add(item2);
        //item_IMG10
        AllocationSendMsgDetailItem item3 = new AllocationSendMsgDetailItem();
        item3.setHeader(getResources().getString(R.string.allocation_send_message_to_material_status_detail_IMG10));
        item3.setContent(String.valueOf(aw1));
        sendMsgDetailList.add(item3);
        //item_MOVED_QTY
        AllocationSendMsgDetailItem item4 = new AllocationSendMsgDetailItem();
        item4.setHeader(getResources().getString(R.string.allocation_send_message_to_material_status_detail_MOVED_QTY));
        item4.setContent(item_MOVED_QTY);
        sendMsgDetailList.add(item4);
        //item_MESS_QTY
        AllocationSendMsgDetailItem item5 = new AllocationSendMsgDetailItem();
        item5.setHeader(getResources().getString(R.string.allocation_send_message_to_material_status_detail_MESS_QTY));
        item5.setContent(item_MESS_QTY);
        sendMsgDetailList.add(item5);
        //item_SFA05
        AllocationSendMsgDetailItem item6 = new AllocationSendMsgDetailItem();
        item6.setHeader(getResources().getString(R.string.allocation_send_message_to_material_status_detail_SFA05));
        item6.setContent(item_SFA05);
        sendMsgDetailList.add(item6);
        //item_SFA12
        AllocationSendMsgDetailItem item7 = new AllocationSendMsgDetailItem();
        item7.setHeader(getResources().getString(R.string.allocation_send_message_to_material_status_detail_SFA12));
        item7.setContent(item_SFA12);
        sendMsgDetailList.add(item7);
        //item_SFA11_NAME
        AllocationSendMsgDetailItem item8 = new AllocationSendMsgDetailItem();
        item8.setHeader(getResources().getString(R.string.allocation_send_message_to_material_status_detail_SFA11_NAME));
        item8.setContent(item_SFA11_NAME);
        sendMsgDetailList.add(item8);
        //item_TC_OBF013
        AllocationSendMsgDetailItem item9 = new AllocationSendMsgDetailItem();
        item9.setHeader(getResources().getString(R.string.allocation_send_message_to_material_status_detail_TC_OBF013));
        item9.setContent(item_TC_OBF013);
        sendMsgDetailList.add(item9);

        AllocationSendMsgDetailItemAdapter allocationSendMsgDetailItemAdapter = new AllocationSendMsgDetailItemAdapter(AllocationSendMsgStatusDetailActivity.this, R.layout.allocation_msg_send_allocation_status_detail_item, sendMsgDetailList);
        listView.setAdapter(allocationSendMsgDetailItemAdapter);
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");

        sendMsgDetailList.clear();

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

        getMenuInflater().inflate(R.menu.allocation_send_detail_menu, menu);








        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.delete_this_item:
                Log.e(TAG, "delete_this_item");

                AlertDialog.Builder confirmdialog = new AlertDialog.Builder(this);
                confirmdialog.setIcon(R.drawable.ic_warning_black_48dp);
                confirmdialog.setTitle(getResources().getString(R.string.delete));
                confirmdialog.setMessage(getResources().getString(R.string.delete_this_item));
                confirmdialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Intent clearIntent = new Intent(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_DELETE_ITEM_CONFIRM);
                        clearIntent.putExtra("INDEX", String.valueOf(index));
                        sendBroadcast(clearIntent);

                        finish();
                    }
                });
                confirmdialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                confirmdialog.show();

                break;
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }

        return true;
    }
}
