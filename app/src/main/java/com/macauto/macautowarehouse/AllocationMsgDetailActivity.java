package com.macauto.macautowarehouse;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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

import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.macauto.macautowarehouse.data.AllocationMsgDetailItem;
import com.macauto.macautowarehouse.data.AllocationMsgDetailItemAdapter;
import com.macauto.macautowarehouse.data.Constants;

import com.macauto.macautowarehouse.data.GenerateRandomString;
import com.macauto.macautowarehouse.service.GetLotCodeVer2Service;

import com.macauto.macautowarehouse.service.GetNewDocNoService;
import com.macauto.macautowarehouse.service.GetPartNoNeedScanStatusService;
import com.macauto.macautowarehouse.service.GetVarValueService;
import com.macauto.macautowarehouse.service.InsertTTImnFileNoTlfNoImgService;
import com.macauto.macautowarehouse.table.DataColumn;
import com.macauto.macautowarehouse.table.DataRow;
import com.macauto.macautowarehouse.table.DataTable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


import static com.macauto.macautowarehouse.AllocationMsgFragment.msgDataTable;
import static com.macauto.macautowarehouse.MainActivity.emp_no;
import static com.macauto.macautowarehouse.MainActivity.k_id;


public class AllocationMsgDetailActivity extends AppCompatActivity {
    private static final String TAG = "AllocationMsgDetail";

    AllocationMsgDetailItemAdapter allocationMsgDetailItemAdapter;

    private static BroadcastReceiver mReceiver = null;
    private static boolean isRegister = false;

    private ListView detailListView;

    private TextView s_iss_date;
    private TextView sp_made_no;
    private TextView s_tag_stock_no;
    private TextView s_tag_locate_no;
    private TextView s_pre_get_datetime;
    private TextView s_ima03;
    private String datetime_0;
    private String datetime_1;
    private String datetime_2;
    private static String dept_no;
    private static String made_no;
    private static String isi;
    private static String iss_no;
    private static String new_no;
    private static int y;

    public static DataTable dataTable_SSS;

    private static String part_no;
    private static String batch_no;
    private static Button btnTransfer;

    public static ArrayList<AllocationMsgDetailItem> showList = new ArrayList<>();

    private static int current_detail_row = -1;
    private static int current_index;

    private static int item_select = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allocation_msg_detail_activity);

        Intent intent = getIntent();

        current_index = Integer.valueOf(intent.getStringExtra("INDEX"));

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

        detailListView = findViewById(R.id.allocationDetailListView);

        detailListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //deselect other
                for (int i=0; i<showList.size(); i++) {

                    if (i == position) {

                        if (showList.get(i).isSelected()) {
                            showList.get(i).setSelected(false);
                            item_select = -1;
                        } else {
                            showList.get(i).setSelected(true);
                            item_select = position;

                        }

                    } else {
                        showList.get(i).setSelected(false);

                    }
                }

                detailListView.invalidateViews();
            }
        });

        btnTransfer = findViewById(R.id.btnTransfer);

        String iss_date = intent.getStringExtra("ISS_DATE");
        made_no = intent.getStringExtra("MADE_NO");
        String tag_locate_no = intent.getStringExtra("TAG_LOCATE_NO");
        String tag_stock_no = intent.getStringExtra("TAG_STOCK_NO");
        String ima03 = intent.getStringExtra("IMA03");
        String pre_get_datetime = intent.getStringExtra("PRE_GET_DATETIME");
        datetime_0 = intent.getStringExtra("dateTime_0");
        datetime_1 = intent.getStringExtra("dateTime_1");
        datetime_2 = intent.getStringExtra("dateTime_2");
        iss_no = intent.getStringExtra("ISS_NO");

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

        allocationMsgDetailItemAdapter = new AllocationMsgDetailItemAdapter(this, R.layout.allocation_msg_detail_item, showList);
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

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_MSG_DETAIL_DELETE_ITEM_CONFIRM)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_MSG_DETAIL_DELETE_ITEM_CONFIRM");

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

                        //original code line 1897, get dept_no


                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_GET_PART_NO_NEED_SCAN_STATUS_FAILED)) {
                        Log.d(TAG, "receive ACTION_GET_PART_NO_NEED_SCAN_STATUS_FAILED");

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_GET_DEPT_NO_SUCCESS)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_GET_DEPT_NO_SUCCESS");

                        dept_no = intent.getStringExtra("DEPT_NO");
                        //line 1900
                        String plant = "MAT";
                        String req_time = datetime_0 + "/" + datetime_1 + "/" + datetime_2 + ":00";

                        y = 0;

                        Date cDate = new Date();
                        String fDate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cDate);

                        new_no = "";

                        //generate new k_id
                        //regenerate new session id
                        GenerateRandomString rString = new GenerateRandomString();
                        k_id = rString.randomString(32);
                        Log.e(TAG, "session_id = "+k_id);
                        /*

                        isi = "insert into imm_file (imm01,imm02,imm03,imm04,imm09,imm10,immacti,immuser,immgrup,immmodu,immdate,immconf,
                        imm14,immspc,immud06,immplant,immlegal,immoriu,immorig,imm15,imm16,immmksg,ta_imm_direct_to_cust,immud05,immud04,immud03) " +
                        "VALUES('BBBBB',TO_DATE('" + DateTime.Now.ToString("yyyyMMdd") + "','YYYYMMDD'),'N','N','工單:" + sp_made_no.Text +
                        ",補料:庫存->備料區','1','Y','" + user_id + "','" + dept_no + "','" + user_id + "',TO_DATE('" +
                        DateTime.Now.ToString("yyyyMMdd") + "','YYYYMMDD'),'N','" +dept_no + "','0','" +
                        sp_made_no.Text + "','" + plant + "','" + plant + "','" + user_id + "','" + dept_no + "','1','" +
                        user_id + "','N','N','PAD','" + p_no[0] + "','" + req_time + "')";

                         */

                        isi = "insert into imm_file (imm01,imm02,imm03,imm04,imm09,imm10,immacti,immuser,immgrup,immmodu,immdate,"+
                                "immconf,imm14,immspc,immud06,immplant,immlegal,immoriu,immorig,imm15,imm16,immmksg,"+"" +
                                "ta_imm_direct_to_cust,immud05,immud04,immud03) " +
                                "VALUES('BBBBB',TO_DATE('" + fDate + "','YYYYMMDD'),'N','N','工單:" +
                                sp_made_no.getText().toString() + ",補料:庫存->備料區','1','Y','" + emp_no + "','" + dept_no + "','" +
                                emp_no + "',TO_DATE('" + fDate + "','YYYYMMDD'),'N','" +
                                dept_no + "','0','" + sp_made_no.getText().toString() + "','" + plant + "','" + plant + "','" +
                                emp_no + "','" + dept_no + "','1','" + emp_no + "','N','N','PAD','" + iss_no + "','" + req_time + "')";

                        y=y+1; //line 1906

                        Intent getPartNoCheckIntent = new Intent(AllocationMsgDetailActivity.this, GetNewDocNoService.class);
                        getPartNoCheckIntent.setAction(Constants.ACTION.ACTION_ALLOCATION_GET_NEW_DOC_NO_ACTION);
                        getPartNoCheckIntent.putExtra("PUR_CSO_TYPE", "11402"); //for pad
                        getPartNoCheckIntent.putExtra("SESSION_DATE", fDate);
                        getPartNoCheckIntent.putExtra("INSERT_SQL", isi);
                        getPartNoCheckIntent.putExtra("DOC_TYPE", "imm");
                        startService(getPartNoCheckIntent);


                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_GET_DEPT_NO_FAILED)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_GET_DEPT_NO_FAILED");

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_GET_NEW_DOC_NO_SUCCESS)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_GET_NEW_DOC_NO_SUCCESS");

                        new_no = intent.getStringExtra("NEW_NO");

                        String plant = "MAT";
                        String req_time = datetime_0 + "/" + datetime_1 + "/" + datetime_2 + ":00";

                        if (new_no.length() > 10) { //stop and go to line 1913

                            if (dataTable_SSS != null) {
                                dataTable_SSS.clear();
                            } else {
                                dataTable_SSS = new DataTable();
                            }

                            dataTable_SSS.TableName = "IMN";

                            /*
                            DataColumn xid = new DataColumn("id");
                            DataColumn imn01 = new DataColumn("imn01");
                            DataColumn imn02 = new DataColumn("imn02", Type.GetType("System.Int32"));
                            DataColumn imn03 = new DataColumn("imn03");
                            DataColumn imn04 = new DataColumn("imn04");
                            DataColumn imn05 = new DataColumn("imn05");
                            DataColumn imn06 = new DataColumn("imn06");
                            DataColumn imn09 = new DataColumn("imn09");
                            DataColumn imn10 = new DataColumn("imn10", Type.GetType("System.Decimal"));
                            DataColumn imn15 = new DataColumn("imn15");
                            DataColumn imn16 = new DataColumn("imn16");
                            DataColumn imn17 = new DataColumn("imn17");
                            DataColumn imn20 = new DataColumn("imn20");
                            DataColumn imn21 = new DataColumn("imn21");
                            DataColumn imn22 = new DataColumn("imn22", Type.GetType("System.Decimal"));
                            DataColumn imn29 = new DataColumn("imn29");
                            DataColumn imnplant = new DataColumn("imnplant");
                            DataColumn imnlegal = new DataColumn("imnlegal");
                            DataColumn imnud03 = new DataColumn("imnud03");*/
                            DataColumn xid = new DataColumn("id");
                            DataColumn imn01 = new DataColumn("imn01");
                            DataColumn imn02 = new DataColumn("imn02"); //Type in32
                            DataColumn imn03 = new DataColumn("imn03");
                            DataColumn imn04 = new DataColumn("imn04");
                            DataColumn imn05 = new DataColumn("imn05");
                            DataColumn imn06 = new DataColumn("imn06");
                            DataColumn imn09 = new DataColumn("imn09");
                            DataColumn imn10 = new DataColumn("imn10"); //Type Decimal
                            DataColumn imn15 = new DataColumn("imn15");
                            DataColumn imn16 = new DataColumn("imn16");
                            DataColumn imn17 = new DataColumn("imn17");
                            DataColumn imn20 = new DataColumn("imn20");
                            DataColumn imn21 = new DataColumn("imn21");
                            DataColumn imn22 = new DataColumn("imn22"); //Type Decimal
                            DataColumn imn29 = new DataColumn("imn29");
                            DataColumn imnplant = new DataColumn("imnplant");
                            DataColumn imnlegal = new DataColumn("imnlegal");
                            DataColumn imnud03 = new DataColumn("imnud03");
                            dataTable_SSS.Columns.Add(xid);
                            dataTable_SSS.Columns.Add(imn01);
                            dataTable_SSS.Columns.Add(imn02);
                            dataTable_SSS.Columns.Add(imn03);
                            dataTable_SSS.Columns.Add(imn04);
                            dataTable_SSS.Columns.Add(imn05);
                            dataTable_SSS.Columns.Add(imn06);
                            dataTable_SSS.Columns.Add(imn09);
                            dataTable_SSS.Columns.Add(imn10);
                            dataTable_SSS.Columns.Add(imn15);
                            dataTable_SSS.Columns.Add(imn16);
                            dataTable_SSS.Columns.Add(imn17);
                            dataTable_SSS.Columns.Add(imn20);
                            dataTable_SSS.Columns.Add(imn21);
                            dataTable_SSS.Columns.Add(imn22);
                            dataTable_SSS.Columns.Add(imn29);
                            dataTable_SSS.Columns.Add(imnplant);
                            dataTable_SSS.Columns.Add(imnlegal);
                            dataTable_SSS.Columns.Add(imnud03);

                            int index = 0;
                            //line 1942
                            for (DataRow rs : msgDataTable.Rows) {
                                double qty_double = Double.valueOf(rs.getValue("qty").toString());
                                if ((int)qty_double == 0) {
                                    continue;
                                }
                                index++;
                                DataRow sk = dataTable_SSS.NewRow();
                                sk.setValue("imn01", new_no);
                                sk.setValue("imn02", String.valueOf(index));
                                sk.setValue("imn03", rs.getValue("part_no"));
                                sk.setValue("imn04", rs.getValue("src_stock_no"));
                                sk.setValue("imn05", rs.getValue("src_locate_no"));
                                sk.setValue("imn06", rs.getValue("src_batch_no"));
                                sk.setValue("imn09", rs.getValue("sfa12"));
                                sk.setValue("imn10", String.valueOf((int)qty_double));
                                sk.setValue("imn15", s_tag_stock_no.getText().toString().trim().toUpperCase());
                                sk.setValue("imn16", s_tag_locate_no.getText().toString().trim().toUpperCase());
                                sk.setValue("imn17", rs.getValue("src_batch_no"));
                                sk.setValue("imn20", rs.getValue("sfa12"));
                                sk.setValue("imn21", "1.0");
                                sk.setValue("imn22", String.valueOf((int)qty_double));
                                sk.setValue("imn29", "N");
                                sk.setValue("imnplant", plant);
                                sk.setValue("imnlegal", plant);
                                sk.setValue("imnud03", req_time);

                                dataTable_SSS.Rows.add(sk);
                            }

                            //line 1977
                            Intent insertIntent = new Intent(AllocationMsgDetailActivity.this, InsertTTImnFileNoTlfNoImgService.class);
                            insertIntent.setAction(Constants.ACTION.ACTION_ALLOCATION_INSERT_TT_IMN_FILE_NO_TLF_NO_IMG_ACTION);
                            insertIntent.putExtra("PLANT_ID", plant);
                            insertIntent.putExtra("NOTE", getResources().getString(R.string.allocation_detail_insert_tt_imn_string));
                            insertIntent.putExtra("DEPT_NO", dept_no);
                            insertIntent.putExtra("MADE_NO", made_no);
                            startService(insertIntent);

                        } else { //new_no length <= 10 && y < 30
                            if (y<30) {
                                y=y+1;

                                Date cDate = new Date();
                                String fDate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cDate);

                                Intent getPartNoCheckIntent = new Intent(AllocationMsgDetailActivity.this, GetNewDocNoService.class);
                                getPartNoCheckIntent.setAction(Constants.ACTION.ACTION_ALLOCATION_GET_NEW_DOC_NO_ACTION);
                                getPartNoCheckIntent.putExtra("PUR_CSO_TYPE", "11402"); //for pad
                                getPartNoCheckIntent.putExtra("SESSION_DATE", fDate);
                                getPartNoCheckIntent.putExtra("INSERT_SQL", isi);
                                getPartNoCheckIntent.putExtra("DOC_TYPE", "imm");
                                startService(getPartNoCheckIntent);

                            } else { //y>=30
                                toast(getResources().getString(R.string.allocation_detail_save_error_a1));
                            }
                        }



                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_GET_NEW_DOC_NO_FAILED)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_GET_NEW_DOC_NO_FAILED");

                        if (y < 30) {
                            y=y+1;

                            Date cDate = new Date();
                            String fDate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(cDate);

                            Intent getPartNoCheckIntent = new Intent(AllocationMsgDetailActivity.this, GetNewDocNoService.class);
                            getPartNoCheckIntent.setAction(Constants.ACTION.ACTION_ALLOCATION_GET_NEW_DOC_NO_ACTION);
                            getPartNoCheckIntent.putExtra("PUR_CSO_TYPE", "11402"); //for pad
                            getPartNoCheckIntent.putExtra("SESSION_DATE", fDate);
                            getPartNoCheckIntent.putExtra("INSERT_SQL", isi);
                            getPartNoCheckIntent.putExtra("DOC_TYPE", "imm");
                            startService(getPartNoCheckIntent);
                        } else { //y>30
                            toast(getResources().getString(R.string.allocation_detail_save_error_a1));
                        }

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_INSERT_TT_IMN_FILE_NO_TLF_NO_IMG_YES)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_INSERT_TT_IMN_FILE_NO_TLF_NO_IMG_YES");

                        AlertDialog.Builder confirmdialog = new AlertDialog.Builder(AllocationMsgDetailActivity.this);
                        confirmdialog.setIcon(R.drawable.ic_warning_black_48dp);
                        confirmdialog.setTitle(getResources().getString(R.string.allocation_detail_generate_no, new_no));
                        //confirmdialog.setMessage(getResources().getString(R.string.delete_this_item));
                        confirmdialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        /*confirmdialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        });*/
                        confirmdialog.show();

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_INSERT_TT_IMN_FILE_NO_TLF_NO_IMG_NO)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_INSERT_TT_IMN_FILE_NO_TLF_NO_IMG_NO");



                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_INSERT_TT_IMN_FILE_NO_TLF_NO_IMG_FAILED)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_INSERT_TT_IMN_FILE_NO_TLF_NO_IMG_FAILED");

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
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_MSG_DETAIL_DELETE_ITEM_CONFIRM);
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_GET_DEPT_NO_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_GET_DEPT_NO_FAILED);
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

        getMenuInflater().inflate(R.menu.allocation_receive_detail_menu, menu);








        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.delete_this_item:
                Log.e(TAG, "delete_this_item");

                if (item_select != -1) {
                    AlertDialog.Builder confirmdialog = new AlertDialog.Builder(this);
                    confirmdialog.setIcon(R.drawable.ic_warning_black_48dp);
                    confirmdialog.setTitle(getResources().getString(R.string.delete));
                    confirmdialog.setMessage(getResources().getString(R.string.delete_this_item));
                    confirmdialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            Intent deleteIntent = new Intent(Constants.ACTION.ACTION_ALLOCATION_MSG_DETAIL_DELETE_ITEM_CONFIRM);
                            deleteIntent.putExtra("DELETE_ROW", String.valueOf(item_select));
                            sendBroadcast(deleteIntent);

                            finish();
                        }
                    });
                    confirmdialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    });
                    confirmdialog.show();
                }



                break;
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }

        return true;
    }

    public void toast(String message) {
        Toast toast = Toast.makeText(AllocationMsgDetailActivity.this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }
}
