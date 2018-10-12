package com.macauto.macautowarehouse;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import android.widget.AdapterView;
import android.widget.Button;

import android.widget.LinearLayout;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.macauto.macautowarehouse.data.Constants;


import com.macauto.macautowarehouse.data.GenerateRandomString;


import com.macauto.macautowarehouse.data.InspectedReceiveItem;
import com.macauto.macautowarehouse.data.InspectedReceiveItemAdapter;
import com.macauto.macautowarehouse.service.ConfirmEnteringWarehouseService;
import com.macauto.macautowarehouse.service.DeleteTTReceiveGoodsInTempService;
import com.macauto.macautowarehouse.service.ExecuteScriptTTService;
import com.macauto.macautowarehouse.service.GetDocTypeIsRegOrSubService;
import com.macauto.macautowarehouse.service.GetReceiveGoodsInDataService;
import com.macauto.macautowarehouse.table.DataColumn;
import com.macauto.macautowarehouse.table.DataRow;
import com.macauto.macautowarehouse.table.DataTable;

import java.util.ArrayList;
import java.util.HashMap;


import static com.macauto.macautowarehouse.MainActivity.k_id;
import static com.macauto.macautowarehouse.MainActivity.pda_type;


public class EnteringWarehouseFragmnet extends Fragment {
    private static final String TAG = EnteringWarehouseFragmnet.class.getName();

    private Context fragmentContext;

    //static SharedPreferences pref ;
    //static SharedPreferences.Editor editor;
    //private static final String FILE_NAME = "Preference";

    //private ExpandableListView expandableListView;
    private ListView listView;
    //private TextView textViewTotalCount;
    //public InspectedReceiveExpanedAdater inspectedReceiveExpanedAdater;
    public InspectedReceiveItemAdapter inspectedReceiveItemAdapter;

    //public static ArrayList<String> no_list = new ArrayList<>();
    //public static HashMap<String, ArrayList<ContactItem>> staticContactList = new HashMap<> ();
    //public static HashMap<String, ArrayList<DetailItem>> detailList = new HashMap<> ();
    //public static ArrayList<Boolean> check_stock_in = new ArrayList<>();
    public static ArrayList<InspectedReceiveItem> swipe_list = new ArrayList<>();

    private static BroadcastReceiver mReceiver = null;
    private static boolean isRegister = false;

    private boolean is_scan_receive = false;
    public static DataTable dataTable;
    public static DataTable table_X_M;
    public static DataTable dataTable_Batch_area;
    //private static boolean is_getScan = false;
    ProgressDialog loadDialog = null;
    InputMethodManager imm;
    //public static int current_expanded_group = -1;
    //public static int total_count_int = 0;
    public static ArrayList<String> pp_list = new ArrayList<>();
    public static HashMap<String, Integer> total_count_list = new HashMap<>();
    //public static DividedItemAdapter dividedItemAdapter;
    //public static ArrayList<DividedItem> dividedList = new ArrayList<>();
    public static int item_select = -1;
    private static String barcode;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentContext = getContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");



        final  View view = inflater.inflate(R.layout.entering_warehouse_fragment, container, false);

        imm = (InputMethodManager)fragmentContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        listView = view.findViewById(R.id.listViewExpand);
        //textViewTotalCount = view.findViewById(R.id.textViewTotal);

        swipe_list.clear();

        /*InspectedReceiveItem item1 = new InspectedReceiveItem();
        item1.setCol_rvb05("item1");
        item1.setCol_pmn041("item1");
        item1.setCol_rvb33("item1");
        item1.setCol_rvv32("item1");
        item1.setCol_rvv33("item1");
        item1.setCol_rvb33("8000.000");
        swipe_list.add(item1);

        InspectedReceiveItem item2 = new InspectedReceiveItem();
        item2.setCol_rvb05("item2");
        item2.setCol_pmn041("item2");
        item2.setCol_rvb33("item2");
        item2.setCol_rvv32("item2");
        item2.setCol_rvv33("item2");
        item2.setCol_rvb33("4000.000");
        swipe_list.add(item2);*/

        inspectedReceiveItemAdapter = new InspectedReceiveItemAdapter(fragmentContext, R.layout.inspected_receive_list_swipe_item, swipe_list);
        listView.setAdapter(inspectedReceiveItemAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "select "+position);

                item_select = position;

                //deselect other
                for (int i=0; i<swipe_list.size(); i++) {

                    if (i == position) {
                        if (swipe_list.get(i).isSelected()) {
                            swipe_list.get(i).setSelected(false);
                            item_select = -1;
                        }
                        else
                            swipe_list.get(i).setSelected(true);

                    } else {
                        swipe_list.get(i).setSelected(false);

                    }
                }

                listView.invalidateViews();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (position < dataTable.Rows.size()) {

                    Intent detailIntent = new Intent(fragmentContext, EnteringWarehouseDetailActivity.class);
                    detailIntent.putExtra("INDEX", String.valueOf(position));
                    //detailIntent.putExtra("BARCODE", barcode);
                    fragmentContext.startActivity(detailIntent);
                }

                return true;
            }
        });

        /*expandableListView = view.findViewById(R.id.listViewExpand);
        //TextView textView = view.findViewById(R.id.textEnteringWarehouse);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                Log.d(TAG, "groupPosition = "+groupPosition+", childPosition = "+childPosition);

                DetailItem item = inspectedReceiveExpanedAdater.getChild(groupPosition, childPosition);

                if (childPosition == 10) { //rvv33(stock no), rvb33(quantity)

                    float quantity = Float.valueOf(item.getName());
                    int quantity_int = (int)quantity;


                    //set dataTable_Batch_area
                    if (dataTable_Batch_area != null) {
                        dataTable_Batch_area.clear();
                    } else {
                        dataTable_Batch_area = new DataTable();
                    }
                    dataTable_Batch_area.TableName = "batch_area";
                    DataColumn c_locate_no = new DataColumn("rvv33");
                    DataColumn c_qty = new DataColumn("rvb33");
                    DataColumn c_stock_no = new DataColumn("rvv32");
                    DataColumn c_batch_no = new DataColumn("rvv34");
                    dataTable_Batch_area.Columns.Add(c_locate_no);
                    dataTable_Batch_area.Columns.Add(c_qty);
                    dataTable_Batch_area.Columns.Add(c_stock_no);
                    dataTable_Batch_area.Columns.Add(c_batch_no);

                    DataRow ur = dataTable_Batch_area.NewRow();
                    ur.setValue("rvv33", dataTable.getValue(groupPosition, "rvv33"));
                    ur.setValue("rvb33", dataTable.getValue(groupPosition, "rvb33"));
                    ur.setValue("rvv32", dataTable.getValue(groupPosition, "rvv32"));
                    ur.setValue("rvv34", dataTable.getValue(groupPosition, "rvv34"));

                    dataTable_Batch_area.Rows.add(ur);


                    //showInputDialog(groupPosition, childPosition);
                    Intent intent = new Intent(fragmentContext, EnteringWarehouseDividedDialogActivity.class);
                    //intent.putExtra("GROUP_INDEX", String.valueOf(groupPosition));
                    //intent.putExtra("CHILD_INDEX", String.valueOf(groupPosition));
                    intent.putExtra("IN_NO", dataTable.getValue(groupPosition, "rvu01").toString());
                    intent.putExtra("ITEM_NO", dataTable.getValue(groupPosition, "rvv02").toString());
                    intent.putExtra("PART_NO", dataTable.getValue(groupPosition, "rvb05").toString());
                    intent.putExtra("QUANTITY", String.valueOf(quantity_int));
                    intent.putExtra("BATCH_NO", dataTable.getValue(groupPosition, "rvv34").toString());
                    intent.putExtra("LOCATE_NO", dataTable.getValue(groupPosition, "rvv33").toString());
                    intent.putExtra("STOCK_NO", dataTable.getValue(groupPosition, "rvv32").toString());
                    intent.putExtra("CHECK_SP", dataTable.getValue(groupPosition, 0).toString());

                    startActivity(intent);
                }

                return true;
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //Log.d(TAG, "groupPosition = "+groupPosition);

                current_expanded_group = groupPosition;

                //Log.e(TAG, "current_expanded_group = "+current_expanded_group);

                for (int i=0; i<no_list.size(); i++) {
                    if (i != groupPosition) {
                        expandableListView.collapseGroup(i);
                    }
                }


            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                //Log.d(TAG, "groupPosition = "+groupPosition+" onGroupCollapse");
                if (current_expanded_group == groupPosition) {
                    current_expanded_group = -1;
                }

                //Log.e(TAG, "current_expanded_group = "+current_expanded_group);


            }
        });*/

        final TextView textView = view.findViewById(R.id.barCode);
        final Button btnScan = view.findViewById(R.id.btnEnteringWarehouseScan);
        final Button btnConfirm = view.findViewById(R.id.btnEnteringWarehouseConfirm);
        //final LinearLayout layoutBottom = view.findViewById(R.id.layoutBottom);
        LinearLayout barCodeLayout = view.findViewById(R.id.barCodeLayout);

        if (pda_type == 0) { //PA720
            Intent scanIntent = new Intent();
            scanIntent.setAction("unitech.scanservice.scan2key_setting");
            scanIntent.putExtra("scan2key", false);
            fragmentContext.sendBroadcast(scanIntent);
            //set TextView gone
            barCodeLayout.setVisibility(View.GONE);

        } else { //TB120
            Intent scanIntent = new Intent();
            scanIntent.setAction("unitech.scanservice.scan2key_setting");
            scanIntent.putExtra("scan2key", true);
            fragmentContext.sendBroadcast(scanIntent);

            //set TextView gone
            barCodeLayout.setVisibility(View.VISIBLE);
        }

        /*textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.e(TAG, "Focus change!");
                if (!is_getScan)
                    is_getScan = true;
                else { //is_getScan == true
                    if (textView.getText().length() > 0 ) {
                        int counter = 0;
                        for( int i=0; i<textView.getText().length(); i++ ) {
                            if( textView.getText().charAt(i) == '#' ) {
                                counter++;
                            }
                        }

                        Log.e(TAG, "counter = "+counter);

                        String codeArray[] = textView.getText().toString().split("#");
                        Intent scanResultIntent = new Intent(Constants.ACTION.ACTION_SET_INSPECTED_RECEIVE_ITEM_CLEAN);
                        for (int i=0; i<codeArray.length; i++) {
                            Log.e(TAG, "codeArray["+i+"] = "+codeArray[i]);
                            String column = "COLUMN_"+String.valueOf(i);
                            scanResultIntent.putExtra(column, codeArray[i]);
                        }



                        scanResultIntent.putExtra("BARCODE", textView.getText().toString());
                        fragmentContext.sendBroadcast(scanResultIntent);

                        is_getScan = false;
                    }
                }
            }
        });*/



        /*btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");

                //init scan
                Intent scanIntent = new Intent();
                scanIntent.setAction("unitech.scanservice.scan2key_setting");
                scanIntent.putExtra("scan2key", false);
                context.sendBroadcast(scanIntent);
                int counter = 0;
                for( int i=0; i<textView.getText().length(); i++ ) {
                    if( textView.getText().charAt(i) == '#' ) {
                        counter++;
                    }
                }

                Log.e(TAG, "counter = "+counter);

                String codeArray[] = textView.getText().toString().split("#");
                Intent scanResultIntent = new Intent(Constants.ACTION.ACTION_SET_INSPECTED_RECEIVE_ITEM_CLEAN);
                for (int i=0; i<codeArray.length; i++) {
                    Log.e(TAG, "codeArray["+i+"] = "+codeArray[i]);
                    String column = "COLUMN_"+String.valueOf(i);
                    scanResultIntent.putExtra(column, codeArray[i]);
                }



                scanResultIntent.putExtra("BARCODE", textView.getText().toString());
                context.sendBroadcast(scanResultIntent);
            }
        });*/


        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /*Intent intent = new Intent(context, ScannedBarcodeActivity.class);
                intent.putExtra("SPLITTER_COUNT", "8");
                startActivity(intent);*/

                textView.setText("");

                //init scan
                Intent scanIntent = new Intent();
                scanIntent.setAction("unitech.scanservice.scan2key_setting");
                scanIntent.putExtra("scan2key", false);
                fragmentContext.sendBroadcast(scanIntent);

                //is_barcode_receive = false;
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String head = fragmentContext.getResources().getString(R.string.entering_warehouse_dialog_content);
                String msg = "";
                boolean found = false;
                //for (int i=0; i< check_stock_in.size(); i++) {
                for (int i=0; i< dataTable.Rows.size(); i++) {
                    if (swipe_list.get(i).isChecked()) {
                        /*msg += detailList.get(no_list.get(i)).get(4).getName()+"\n["+fragmentContext.getResources().getString(R.string.item_title_rvv33)+" "+
                                detailList.get(no_list.get(i)).get(8).getName()+"]\n["+fragmentContext.getResources().getString(R.string.item_title_rvb33)+" "+
                                detailList.get(no_list.get(i)).get(10).getName()+"]\n\n";*/
                        found = true;
                        msg += swipe_list.get(i).getCol_pmn041()+"\n["+fragmentContext.getResources().getString(R.string.item_title_rvv33)+" "+
                                swipe_list.get(i).getCol_rvv33()+"]\n["+fragmentContext.getResources().getString(R.string.item_title_rvb33)+" "+
                                swipe_list.get(i).getCol_rvb33()+"]";
                    }
                }

                if (found) {
                    AlertDialog.Builder confirmdialog = new AlertDialog.Builder(fragmentContext);
                    confirmdialog.setIcon(R.drawable.ic_warning_black_48dp);
                    confirmdialog.setTitle(fragmentContext.getResources().getString(R.string.entering_warehouse_dialog_title));
                    confirmdialog.setMessage(head+"\n"+msg);
                    confirmdialog.setPositiveButton(fragmentContext.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            btnConfirm.setEnabled(false);

                            boolean found = false;
                            boolean same_in_no_but_not_check = false;

                            //check if set in-stock check, but not scan
                            for (int i=0; i<dataTable.Rows.size(); i++) {

                                if (swipe_list.get(i).isSelected()) {

                                    if (dataTable.Rows.get(i).getValue("rvv33_scan").toString().equals("")) {
                                        found = true;
                                        break;
                                    }
                                }
                            }

                            //check same in_no but not set check

                            ArrayList<String> rvu01_list = new ArrayList<>();
                            String temp_rvu01 = "";
                            for (int i=0; i<dataTable.Rows.size(); i++) {
                                if (!temp_rvu01.equals(swipe_list.get(i).getCol_rvu01())) {
                                    temp_rvu01 = swipe_list.get(i).getCol_rvu01();
                                    rvu01_list.add(temp_rvu01);
                                }
                            }

                            Log.e(TAG, "rvu01_list.size = "+rvu01_list.size());

                            for (int i=0; i<rvu01_list.size(); i++) {
                                int check_count = 0;
                                int tatal_count = 0;
                                for (int j=0; j<dataTable.Rows.size(); j++) {
                                    if (rvu01_list.get(i).equals(swipe_list.get(i).getCol_rvu01())) {
                                        tatal_count = tatal_count + 1;

                                        if (swipe_list.get(j).isChecked()) {
                                            check_count = check_count + 1;
                                        }
                                    }
                                }

                                if (check_count > 0 && check_count != tatal_count) { //in same in_no, some item are checked, but some are not.
                                    same_in_no_but_not_check = true;
                                    break;
                                }

                                Log.d(TAG, "tatal_count = "+tatal_count+",check_count = "+check_count);
                            }




                            if (found) { //found empty locate no in list. Must scan locate no before in stock.
                                toast(getResources().getString(R.string.entering_warehouse_locate_no_not_scan));

                            } else if (same_in_no_but_not_check) {
                                toast(getResources().getString(R.string.entering_warehouse_same_in_no_not_check));
                            } else { //all locate no were scanned, then go in stock procedure.
                                pp_list.clear();
                                String ss = "";
                                //original code line 295
                                if (table_X_M != null) {
                                    table_X_M.clear();
                                } else {
                                    table_X_M = new DataTable();
                                }
                                table_X_M.TableName = "X_M";
                                //DataColumn v1 = new DataColumn("script");
                                table_X_M.Columns.Add("script");

                                int index = 0;
                                for (DataRow rx : dataTable.Rows) {
                                    if (!ss.equals(dataTable.Rows.get(index).getValue("rvu01").toString())) {
                                        ss = rx.getValue("rvu01").toString();
                                        //if (check_stock_in.get(index)) //if check, then set
                                        if (swipe_list.get(index).isChecked()) //if check, then set
                                            pp_list.add(rx.getValue("rvu01").toString());
                                    }
                                    index++;
                                }

                                Intent getintent = new Intent(fragmentContext, ConfirmEnteringWarehouseService.class);
                                getintent.setAction(Constants.ACTION.ACTION_CONFIRM_ENTERING_WAREHOUSE_ACTION);
                                fragmentContext.startService(getintent);


                                loadDialog = new ProgressDialog(fragmentContext);
                                loadDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                loadDialog.setTitle(getResources().getString(R.string.Processing));
                                loadDialog.setIndeterminate(false);
                                loadDialog.setCancelable(false);
                                loadDialog.show();
                            }


                        }
                    });
                    confirmdialog.setNegativeButton(fragmentContext.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // btnScan.setVisibility(View.VISIBLE);
                            // btnConfirm.setVisibility(View.GONE);

                        }
                    });
                    confirmdialog.show();
                } else {
                    toast(getResources().getString(R.string.entering_warehouse_set_check_before_in_stock));
                }
                //original line 272
                /*for (DataRow hx : dataTable.Rows) {
                    if (!hx.getValue(12).toString().equals("")) {
                        hx.setValue(7, hx.getValue(12).toString().toUpperCase());
                    }
                }*/


            }
        });


        final IntentFilter filter;

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //Log.e(TAG, "intent.getAction() =>>>> "+intent.getAction().toString());

                if (intent.getAction() != null) {

                    if (intent.getAction().equalsIgnoreCase(Constants.ACTION.SOAP_CONNECTION_FAIL)) {
                        Log.d(TAG, "receive SOAP_CONNECTION_FAIL");
                        if (loadDialog != null)
                            loadDialog.dismiss();

                        is_scan_receive = false;

                        if (dataTable.Rows.size() > 0) {
                            btnConfirm.setEnabled(true);
                        }


                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SOCKET_TIMEOUT)) {
                        Log.d(TAG, "receive ACTION_SOCKET_TIMEOUT");
                        if (loadDialog != null)
                            loadDialog.dismiss();

                        is_scan_receive = false;

                        toast(getResources().getString(R.string.socket_timeout));

                        if (dataTable.Rows.size() > 0) {
                            btnConfirm.setEnabled(true);
                        }

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SHOW_VIRTUAL_KEYBOARD_ACTION)) {
                        Log.d(TAG, "receive ACTION_SHOW_VIRTUAL_KEYBOARD_ACTION");
                        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,InputMethodManager.HIDE_IMPLICIT_ONLY);
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_GET_BARCODE_MESSGAGE_COMPLETE)) {
                        Log.d(TAG, "receive brocast !");


                        /*for (int i =0; i < 8; i++) {
                            String col = "COLUMN_"+String.valueOf(i);
                            Log.e(TAG, "col_"+i+" = "+intent.getStringExtra(col));
                        }
                        String barcode = intent.getStringExtra("BARCODE");
                        Log.e(TAG, "barcode = "+barcode);

                        Intent getintent = new Intent(context, GetReceiveGoodsInDataService.class);
                        getintent.putExtra("PART_NO", intent.getStringExtra("COLUMN_0"));
                        getintent.putExtra("BARCODE_NO", barcode);
                        context.startService(getintent);*/


                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SET_INSPECTED_RECEIVE_ITEM_CLEAN)) {
                        Log.d(TAG, "get ACTION_SET_INSPECTED_RECEIVE_ITEM_CLEAN");

                        swipe_list.clear();
                        total_count_list.clear();
                        if (inspectedReceiveItemAdapter != null)
                            inspectedReceiveItemAdapter.notifyDataSetChanged();

                        //clear view
                        //no_list.clear();
                        //check_stock_in.clear();
                        //detailList.clear();
                        //if (inspectedReceiveExpanedAdater != null)
                        //    inspectedReceiveExpanedAdater.notifyDataSetChanged();

                        //start service
                        /*for (int i = 0; i < 8; i++) {
                            String col = "COLUMN_" + String.valueOf(i);
                            Log.e(TAG, "col_" + i + " = " + intent.getStringExtra(col));
                        }*/
                        //String barcode = intent.getStringExtra("BARCODE");
                        //Log.e(TAG, "barcode = " + barcode);

                        Intent getintent = new Intent(context, GetReceiveGoodsInDataService.class);
                        getintent.putExtra("PART_NO", intent.getStringExtra("COLUMN_0"));
                        getintent.putExtra("BARCODE_NO", barcode);
                        getintent.putExtra("K_ID", k_id);
                        context.startService(getintent);

                        //start loadDialog
                        loadDialog = new ProgressDialog(fragmentContext);
                        loadDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        loadDialog.setTitle(getResources().getString(R.string.Processing));
                        loadDialog.setIndeterminate(false);
                        loadDialog.setCancelable(false);
                        loadDialog.show();

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SET_INSPECTED_RECEIVE_ITEM_CLEAN_ONLY)) {
                        Log.d(TAG, "get ACTION_SET_INSPECTED_RECEIVE_ITEM_CLEAN_ONLY");

                        //swipe_list.clear();
                        //total_count_list.clear();
                        //if (inspectedReceiveItemAdapter != null)
                        //    inspectedReceiveItemAdapter.notifyDataSetChanged();

                        //toast(getResources().getString(R.string.entering_warehouse_split_complete));

                        //regenerate new session id
                        GenerateRandomString rString = new GenerateRandomString();
                        k_id = rString.randomString(32);
                        Log.e(TAG, "session_id = "+k_id);

                        String codeArray[] = barcode.split("#");
                        Intent scanResultIntent = new Intent(Constants.ACTION.ACTION_SET_INSPECTED_RECEIVE_ITEM_CLEAN);
                        for (int i = 0; i < codeArray.length; i++) {
                            Log.e(TAG, "codeArray[" + i + "] = " + codeArray[i]);
                            String column = "COLUMN_" + String.valueOf(i);
                            scanResultIntent.putExtra(column, codeArray[i]);
                        }
                        //barcode = text;

                        //scanResultIntent.putExtra("BARCODE", text);
                        scanResultIntent.putExtra("K_ID", k_id);
                        fragmentContext.sendBroadcast(scanResultIntent);

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_GET_INSPECTED_RECEIVE_ITEM_SUCCESS)) {
                        //toast(context.getResources().getString(R.string.get_receive_goods_success));

                        //String total_count = intent.getStringExtra("TOTAL_COUNT");

                        Log.d(TAG, "receive ACTION_GET_INSPECTED_RECEIVE_ITEM_SUCCESS");

                        if (loadDialog != null)
                            loadDialog.dismiss();

                        if (inspectedReceiveItemAdapter != null)
                            inspectedReceiveItemAdapter.notifyDataSetChanged();

                        //String total_count_string = getResources().getString(R.string.item_total, dataTable.Rows.size(), total_count);
                        //textViewTotalCount.setText(total_count_string);


                        /*inspectedReceiveExpanedAdater = new InspectedReceiveExpanedAdater(context, R.layout.list_group, R.layout.inspected_receive_list_item, no_list, detailList);
                        expandableListView.setAdapter(inspectedReceiveExpanedAdater);*/

                        //is_barcode_receive = false;

                        is_scan_receive = false;

                        //btnScan.setVisibility(View.GONE);
                        //btnConfirm.setVisibility(View.VISIBLE);
                        btnConfirm.setEnabled(true);
                        Log.e(TAG, "========================================================");
                        for (int i=0; i<dataTable.Rows.size(); i++) {

                            for (int j=0; j<dataTable.Columns.size(); j++) {
                                System.out.print(dataTable.Rows.get(i).getValue(j));
                                if (j < dataTable.Columns.size() - 1) {
                                    System.out.print(", ");
                                }
                            }
                            System.out.print("\n");
                        }
                        Log.e(TAG, "========================================================");

                        /*int total = 0;
                        for (int i=0; i<dataTable.Rows.size(); i++) {
                            double temp = Double.valueOf(dataTable.Rows.get(i).getValue("rvb33").toString());
                            total = total + (int) temp;
                        }

                        toast(getResources().getString(R.string.item_total, dataTable.Rows.size(), total));*/

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_GET_INSPECTED_RECEIVE_ITEM_EMPTY)) {
                        Log.d(TAG, "receive ACTION_GET_INSPECTED_RECEIVE_ITEM_EMPTY");
                        if (loadDialog != null)
                            loadDialog.dismiss();

                        is_scan_receive = false;

                        btnConfirm.setEnabled(true);

                        toast(context.getResources().getString(R.string.entering_warehouse_no_record));

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_GET_INSPECTED_RECEIVE_ITEM_FAILED)) {
                        if (loadDialog != null)
                            loadDialog.dismiss();

                        is_scan_receive = false;

                        btnConfirm.setEnabled(true);

                        toast(context.getResources().getString(R.string.get_receive_goods_fail));
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ENTERING_WAREHOUSE_DIVIDED_DIALOG_ADD)) {
                        Log.d(TAG, "get ACTION_ENTERING_WAREHOUSE_DIVIDED_DIALOG_ADD");

                        //if (inspectedReceiveExpanedAdater != null)
                        //    inspectedReceiveExpanedAdater.notifyDataSetChanged();

                        if (inspectedReceiveItemAdapter != null)
                            inspectedReceiveItemAdapter.notifyDataSetChanged();

                        //layoutBottom.setVisibility(View.GONE);

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ENTERING_WAREHOUSE_DIVIDED_DIALOG_SHOW)) {
                        Log.d(TAG, "get ACTION_ENTERING_WAREHOUSE_DIVIDED_DIALOG_SHOW");

                        String index_string = intent.getStringExtra("INDEX");
                        ///String barcode = intent.getStringExtra("BARCODE");
                        int index = Integer.valueOf(index_string);

                        if (dataTable != null && dataTable.Rows.size() > 0) {

                            //set dataTable_Batch_area
                            if (dataTable_Batch_area != null) {
                                dataTable_Batch_area.clear();
                            } else {
                                dataTable_Batch_area = new DataTable();
                            }
                            dataTable_Batch_area.TableName = "batch_area";
                            DataColumn c_locate_no = new DataColumn("rvv33");
                            DataColumn c_qty = new DataColumn("rvb33");
                            DataColumn c_stock_no = new DataColumn("rvv32");
                            DataColumn c_batch_no = new DataColumn("rvv34");
                            dataTable_Batch_area.Columns.Add(c_locate_no);
                            dataTable_Batch_area.Columns.Add(c_qty);
                            dataTable_Batch_area.Columns.Add(c_stock_no);
                            dataTable_Batch_area.Columns.Add(c_batch_no);

                            DataRow ur = dataTable_Batch_area.NewRow();
                            ur.setValue("rvv33", dataTable.getValue(index, "rvv33"));
                            ur.setValue("rvb33", dataTable.getValue(index, "rvb33"));
                            ur.setValue("rvv32", dataTable.getValue(index, "rvv32"));
                            ur.setValue("rvv34", dataTable.getValue(index, "rvv34"));

                            dataTable_Batch_area.Rows.add(ur);


                            float quantity = Float.valueOf(dataTable.getValue(index, "rvb33").toString());
                            int quantity_int = (int)quantity;

                            Intent divideIntent = new Intent(fragmentContext, EnteringWarehouseDividedDialogActivity.class);
                            divideIntent.putExtra("IN_NO", dataTable.getValue(index, "rvu01").toString());
                            divideIntent.putExtra("ITEM_NO", dataTable.getValue(index, "rvv02").toString());
                            divideIntent.putExtra("PART_NO", dataTable.getValue(index, "rvb05").toString());
                            divideIntent.putExtra("QUANTITY", String.valueOf(quantity_int));
                            divideIntent.putExtra("BATCH_NO", dataTable.getValue(index, "rvv34").toString());
                            divideIntent.putExtra("LOCATE_NO", dataTable.getValue(index, "rvv33").toString());
                            divideIntent.putExtra("STOCK_NO", dataTable.getValue(index, "rvv32").toString());
                            divideIntent.putExtra("CHECK_SP", dataTable.getValue(index, 0).toString());
                            divideIntent.putExtra("BARCODE", barcode);

                            fragmentContext.startActivity(divideIntent);

                        }



                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_MODIFIED_ITEM_COMPLETE)) {
                        Log.d(TAG, "get ACTION_MODIFIED_ITEM_COMPLETE");

                        //if (inspectedReceiveExpanedAdater != null)
                        //    inspectedReceiveExpanedAdater.notifyDataSetChanged();
                        if (inspectedReceiveItemAdapter != null)
                            inspectedReceiveItemAdapter.notifyDataSetChanged();
                        //hide keyboard
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                        //check all the stock no. If at least one is not NA, show the confirm button
                        /*int count = 0 ;
                        for (int i=0; i<dataTable.Rows.size(); i++) {
                            if (dataTable.Rows.get(i).getValue("rvv33").equals("NA")) {
                                Log.d(TAG, "dataTable.Row["+i+"] = "+dataTable.Rows.get(i).getValue("rvv33"));

                            } else {
                                Log.e(TAG, "dataTable.Row["+i+"] = "+dataTable.Rows.get(i).getValue("rvv33"));
                                count++;
                            }
                        }*/

                        /*Log.e(TAG, "Not NA count = "+count);
                        if (count > 0) {
                            layoutBottom.setVisibility(View.VISIBLE);
                        } else {
                            layoutBottom.setVisibility(View.GONE);
                        }*/



                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SCAN_RESET)) {
                        Log.d(TAG, "get ACTION_SCAN_RESET");
                        //btnScan.setVisibility(View.VISIBLE);
                        //btnConfirm.setVisibility(View.GONE);
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_UPDATE_TT_RECEIVE_IN_RVV33_FAILED)) {
                        Log.d(TAG, "get ACTION_UPDATE_TT_RECEIVE_IN_RVV33_FAILED");
                        toast(getResources().getString(R.string.entering_warehouse_failed));
                        loadDialog.dismiss();
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_UPDATE_TT_RECEIVE_IN_RVV33_SUCCESS)) {
                        Log.d(TAG, "get ACTION_UPDATE_TT_RECEIVE_IN_RVV33_SUCCESS");

                        //pp_list.clear();

                        //int found_index = -1;
                        //start from the last
                        int found_index = pp_list.size() - 1;
                        /*for (int i=check_stock_in.size()-1; i>=0; i--) {
                            if (check_stock_in.get(i)) {
                                found_index = i;
                                break;
                            }
                        }*/

                        Log.e(TAG, "found_index = "+found_index);

                        if (found_index != -1) {
                            Intent getintent = new Intent(context, GetDocTypeIsRegOrSubService.class);
                            getintent.setAction(Constants.ACTION.ACTION_GET_DOC_TYPE_IS_REG_OR_SUB_ACTION);
                            getintent.putExtra("CURRENT_TABLE", String.valueOf(found_index));
                            getintent.putExtra("DOC_NO", pp_list.get(found_index));
                            context.startService(getintent);
                        }



                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_GET_DOC_TYPE_IS_REG_OR_SUB_FAILED)) {
                        Log.d(TAG, "get ACTION_GET_DOC_TYPE_IS_REG_OR_SUB_FAILED");
                        toast(getResources().getString(R.string.entering_warehouse_failed));
                        loadDialog.dismiss();
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_GET_DOC_TYPE_IS_REG_OR_SUB_SUCCESS)) {
                        Log.d(TAG, "get ACTION_GET_DOC_TYPE_IS_REG_OR_SUB_SUCCESS");
                        String current_table = intent.getStringExtra("CURRENT_TABLE");
                        //String doc_type = intent.getStringExtra("DOC_TYPE");
                        //String rvu01 = intent.getStringExtra("RVU01");
                        int cur_index = Integer.valueOf(current_table);

                        /*Intent getintent = new Intent(context, ExecuteScriptTTService.class);
                        getintent.setAction(Constants.ACTION.ACTION_EXECUTE_TT_ACTION);
                        getintent.putExtra("DOC_TYPE", doc_type);
                        getintent.putExtra("CURRENT_TABLE", current_table);
                        getintent.putExtra("RVU01", rvu01);
                        context.startService(getintent);*/

                        //if (cur_index < dataTable.Rows.size()) {
                        if (cur_index >= 0) {
                            Log.e(TAG, "[update table["+current_table+"], rvu02 = "+dataTable.Rows.get(cur_index).getValue("rvu02")+"]");


                            /*detailList.get(no_list.get(cur_index)).clear();
                            no_list.remove(cur_index);
                            check_stock_in.remove(cur_index);
                            dataTable.Rows.remove(cur_index);
                            if (inspectedReceiveExpanedAdater != null)
                                inspectedReceiveExpanedAdater.notifyDataSetChanged();

                            Log.e(TAG, "=== [ACTION_EXECUTE_TT_SUCCESS] check stock in start ===");
                            for (int i=0; i < check_stock_in.size(); i++) {
                                Log.e(TAG, "check_stock_in["+i+"] = "+check_stock_in.get(i));
                            }
                            Log.e(TAG, "=== [ACTION_EXECUTE_TT_SUCCESS] check stock in end ===");*/

                            //cur_index = cur_index + 1;
                            //Log.e(TAG, "next=> table:"+next_table);

                            Intent getintent = new Intent(context, GetDocTypeIsRegOrSubService.class);
                            getintent.setAction(Constants.ACTION.ACTION_GET_DOC_TYPE_IS_REG_OR_SUB_ACTION);
                            getintent.putExtra("CURRENT_TABLE", current_table);
                            getintent.putExtra("DOC_NO", pp_list.get(cur_index));
                            context.startService(getintent);
                        }

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_GET_DOC_TYPE_IS_REG_OR_SUB_COMPLETE)) {
                        Log.d(TAG, "get ACTION_GET_DOC_TYPE_IS_REG_OR_SUB_COMPLETE");
                        loadDialog.dismiss();

                        Log.e(TAG, "=================== table_X_M ==========================");
                        for (int i=0; i<table_X_M.Rows.size(); i++) {

                            for (int j=0; j<table_X_M.Columns.size(); j++) {
                                System.out.print(table_X_M.Rows.get(i).getValue(j));
                                if (j < table_X_M.Columns.size() - 1) {
                                    System.out.print(", ");
                                }
                            }
                            System.out.print("\n");
                        }
                        Log.e(TAG, "========================================================");


                        Intent executeIntent = new Intent(context, ExecuteScriptTTService.class);
                        executeIntent.setAction(Constants.ACTION.ACTION_EXECUTE_TT_ACTION);
                        executeIntent.putExtra("PROCESS_TYPE", "0"); //receiving execute TT
                        //getintent.putExtra("DOC_TYPE", doc_type);
                        //getintent.putExtra("CURRENT_TABLE", current_table);
                        //getintent.putExtra("RVU01", rvu01);
                        context.startService(executeIntent);

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_EXECUTE_TT_FAILED)) {
                        Log.d(TAG, "get ACTION_EXECUTE_TT_FAILED");
                        toast(getResources().getString(R.string.entering_warehouse_failed));
                        loadDialog.dismiss();
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_EXECUTE_TT_SUCCESS)) {
                        Log.d(TAG, "get ACTION_EXECUTE_TT_SUCCESS");
                        String current_table = intent.getStringExtra("CURRENT_TABLE");
                        String next_table = intent.getStringExtra("NEXT_TABLE");
                        //int cur_index = Integer.valueOf(current_table);

                        Log.d(TAG, "get ACTION_EXECUTE_TT_SUCCESS: current_table = "+current_table+", next_table = "+next_table);

                        /*if (cur_index < dataTable.Rows.size()) {
                            Log.e(TAG, "[update table rvu02 = "+dataTable.Rows.get(cur_index).getValue("rvu02")+"]");


                            detailList.get(no_list.get(cur_index)).clear();
                            no_list.remove(cur_index);
                            check_stock_in.remove(cur_index);
                            dataTable.Rows.remove(cur_index);
                            if (inspectedReceiveExpanedAdater != null)
                                inspectedReceiveExpanedAdater.notifyDataSetChanged();

                            Log.e(TAG, "=== [ACTION_EXECUTE_TT_SUCCESS] check stock in start ===");
                            for (int i=0; i < check_stock_in.size(); i++) {
                                Log.e(TAG, "check_stock_in["+i+"] = "+check_stock_in.get(i));
                            }
                            Log.e(TAG, "=== [ACTION_EXECUTE_TT_SUCCESS] check stock in end ===");

                            //cur_index = cur_index + 1;
                            Log.e(TAG, "next=> table:"+next_table);

                            Intent getintent = new Intent(context, GetDocTypeIsRegOrSubService.class);
                            getintent.setAction(Constants.ACTION.ACTION_GET_DOC_TYPE_IS_REG_OR_SUB_ACTION);
                            getintent.putExtra("CURRENT_TABLE", next_table);
                            context.startService(getintent);
                        }*/
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ENTERING_WAREHOUSE_COMPLETE)) {
                        Log.d(TAG, "get ACTION_ENTERING_WAREHOUSE_COMPLETE");
                        //String current_table = intent.getStringExtra("CURRENT_TABLE");
                        //int cur_index = Integer.valueOf(current_table);
                        //delete last one
                        //Log.e(TAG, "[update table rvu02 = "+dataTable.Rows.get(cur_index).getValue("rvu02")+"]");

                        //remove
                        //for (int i=check_stock_in.size()-1; i>=0; i--) {
                        for (int i=dataTable.Rows.size()-1; i>=0; i--) {
                            if (swipe_list.get(i).isChecked()) {
                                //detailList.get(no_list.get(i)).clear();
                                //no_list.remove(i);
                                dataTable.Rows.remove(i);
                            }
                        }


                        //reset
                        //check_stock_in.clear();
                        //for (int i=0 ;i<dataTable.Rows.size(); i++) {
                        //    check_stock_in.add(i, false);
                        //}

                        //check_stock_in.remove(cur_index);


                        //mSparseBooleanArray.clear();
                        //if (inspectedReceiveExpanedAdater != null)
                        //    inspectedReceiveExpanedAdater.notifyDataSetChanged();


                        /*Log.e(TAG, "=== [ACTION_ENTERING_WAREHOUSE_COMPLETE] check stock in start ===");
                        for (int i=0; i < check_stock_in.size(); i++) {
                            Log.e(TAG, "check_stock_in["+i+"] = "+check_stock_in.get(i));
                        }
                        Log.e(TAG, "=== [ACTION_ENTERING_WAREHOUSE_COMPLETE] check stock in end ===");*/

                        toast(getResources().getString(R.string.entering_warehouse_complete));
                        loadDialog.dismiss();
                        //layoutBottom.setVisibility(View.GONE);

                        //if dataTable Rows = 0, delete temp
                        /*if (dataTable.Rows.size() == 0) {
                            Intent deleteIntent = new Intent(context, DeleteTTReceiveGoodsInTempService.class);
                            deleteIntent.setAction(Constants.ACTION.ACTION_DELETE_TT_RECEIVE_GOODS_IN_TEMP_ACTION);
                            context.startService(deleteIntent);
                        }*/
                        Intent deleteIntent = new Intent(context, DeleteTTReceiveGoodsInTempService.class);
                        deleteIntent.setAction(Constants.ACTION.ACTION_DELETE_TT_RECEIVE_GOODS_IN_TEMP_ACTION);
                        context.startService(deleteIntent);

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_DELETE_TT_RECEIVE_GOODS_IN_TEMP_SUCCESS)) {
                        Log.d(TAG, "get ACTION_DELETE_TT_RECEIVE_GOODS_IN_TEMP_SUCCESS");

                        swipe_list.clear();

                        if (inspectedReceiveItemAdapter != null) {
                            inspectedReceiveItemAdapter.mSparseBooleanArray.clear();
                            inspectedReceiveItemAdapter.notifyDataSetChanged();
                        }

                        btnConfirm.setEnabled(false);
                        //toast(getResources().getString(R.string.entering_warehouse_failed));
                        //loadDialog.dismiss();
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_DELETE_TT_RECEIVE_GOODS_IN_TEMP_FAILED)) {
                        Log.d(TAG, "get ACTION_DELETE_TT_RECEIVE_GOODS_IN_TEMP_FAILED");
                        //toast(getResources().getString(R.string.entering_warehouse_failed));
                        //loadDialog.dismiss();
                    }



                    else if("unitech.scanservice.data" .equals(intent.getAction())) {
                        Log.d(TAG, "unitech.scanservice.data");
                        Bundle bundle = intent.getExtras();
                        if(bundle != null )
                        {
                            String text = bundle.getString("text");
                            Log.e(TAG, "msg = "+text);

                            //is_barcode_receive = false;
                            if (text.length() > 0 ) {
                                int counter = 0;
                                for( int i=0; i<text.length(); i++ ) {
                                    if( text.charAt(i) == '#' ) {
                                        counter++;
                                    }
                                }

                                Log.e(TAG, "counter = "+counter);

                                if (counter == 8) {

                                    if (!is_scan_receive) {
                                        //set scan true
                                        is_scan_receive = true;
                                        //regenerate new session id
                                        GenerateRandomString rString = new GenerateRandomString();
                                        k_id = rString.randomString(32);
                                        Log.e(TAG, "session_id = "+k_id);



                                        String codeArray[] = text.split("#");
                                        Intent scanResultIntent = new Intent(Constants.ACTION.ACTION_SET_INSPECTED_RECEIVE_ITEM_CLEAN);
                                        for (int i = 0; i < codeArray.length; i++) {
                                            Log.e(TAG, "codeArray[" + i + "] = " + codeArray[i]);
                                            String column = "COLUMN_" + String.valueOf(i);
                                            scanResultIntent.putExtra(column, codeArray[i]);
                                        }
                                        barcode = text;

                                        //scanResultIntent.putExtra("BARCODE", text);
                                        scanResultIntent.putExtra("K_ID", k_id);
                                        fragmentContext.sendBroadcast(scanResultIntent);
                                    }

                                } else {

                                    text = text.replaceAll("\\n","");
                                    toast(text);
                                    //if (no_list.size() > 0 && detailList.size() > 0) {
                                    if (swipe_list.size() > 0) {

                                        if (item_select != -1) { //scan locate
                                            if (dataTable != null && dataTable.Rows.size() > 0) {
                                                dataTable.Rows.get(item_select).setValue("rvv33", text);
                                                dataTable.Rows.get(item_select).setValue("rvv33_scan", text);
                                            }
                                            swipe_list.get(item_select).setCol_rvv33(text);
                                            swipe_list.get(item_select).setChecked(true);
                                            //check_stock_in.set(item_select, true);

                                            if (inspectedReceiveItemAdapter != null)
                                                inspectedReceiveItemAdapter.notifyDataSetChanged();
                                        }

                                        /*if (current_expanded_group > -1) {


                                            String head = no_list.get(current_expanded_group);
                                            DetailItem detailItem = detailList.get(head).get(8);
                                            DetailItem detailItem2 = detailList.get(head).get(13);
                                            detailItem.setName(text);
                                            detailItem2.setName(text);

                                            Log.e(TAG, "current_expanded_group = "+current_expanded_group+", head = "+head);

                                            if (dataTable != null) {
                                                dataTable.Rows.get(current_expanded_group).setValue("rvv33", text);
                                                dataTable.Rows.get(current_expanded_group).setValue("rvv33_scan", text);
                                            }



                                            Intent getFailedIntent = new Intent(Constants.ACTION.ACTION_MODIFIED_ITEM_COMPLETE);
                                            context.sendBroadcast(getFailedIntent);
                                        }*/

                                        /*Log.e(TAG, "========================================================");
                                            for (int i = 0; i < dataTable.Rows.size(); i++) {

                                                for (int j = 0; j < dataTable.Columns.size(); j++) {
                                                    System.out.print(dataTable.Rows.get(i).getValue(j));
                                                    if (j < dataTable.Columns.size() - 1) {
                                                        System.out.print(", ");
                                                    }
                                                }
                                                System.out.print("\n");
                                            }
                                            Log.e(TAG, "========================================================");*/
                                    }
                                }

                            }
                        }
                    }/*else if("unitech.scanservice.datatype" .equals(intent.getAction())) {
                        Log.d(TAG, "unitech.scanservice.datatype");
                        Bundle bundle = intent.getExtras();
                        if(bundle != null )
                        {
                            int type = Integer.valueOf(bundle.getString("text"));
                            String text = "";
                            if(type == 0x01)
                                text = "This is Code 39.";
                            else if(type == 0x02)
                                text = "This is Code 39.";
                            Log.e(TAG, "msg = "+text);

                        }
                    } else if("unitech.scanservice.scan2key_setting" .equals(intent.getAction())) {
                        Log.d(TAG, "receive unitech.scanservice.scan2key_setting");
                    }*/

                }
            }
        };

        if (!isRegister) {
            filter = new IntentFilter();
            filter.addAction(Constants.ACTION.SOAP_CONNECTION_FAIL);
            filter.addAction(Constants.ACTION.ACTION_SOCKET_TIMEOUT);
            filter.addAction(Constants.ACTION.ACTION_SHOW_VIRTUAL_KEYBOARD_ACTION);
            filter.addAction(Constants.ACTION.ACTION_GET_BARCODE_MESSGAGE_COMPLETE);
            filter.addAction(Constants.ACTION.ACTION_SET_INSPECTED_RECEIVE_ITEM_CLEAN);
            filter.addAction(Constants.ACTION.ACTION_SET_INSPECTED_RECEIVE_ITEM_CLEAN_ONLY);
            filter.addAction(Constants.ACTION.ACTION_GET_INSPECTED_RECEIVE_ITEM_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_GET_INSPECTED_RECEIVE_ITEM_EMPTY);
            filter.addAction(Constants.ACTION.ACTION_GET_INSPECTED_RECEIVE_ITEM_FAILED);

            filter.addAction(Constants.ACTION.ACTION_UPDATE_TT_RECEIVE_IN_RVV33_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_UPDATE_TT_RECEIVE_IN_RVV33_FAILED);
            filter.addAction(Constants.ACTION.ACTION_GET_DOC_TYPE_IS_REG_OR_SUB_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_GET_DOC_TYPE_IS_REG_OR_SUB_FAILED);
            filter.addAction(Constants.ACTION.ACTION_GET_DOC_TYPE_IS_REG_OR_SUB_COMPLETE);
            filter.addAction(Constants.ACTION.ACTION_EXECUTE_TT_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_EXECUTE_TT_FAILED);
            filter.addAction(Constants.ACTION.ACTION_ENTERING_WAREHOUSE_COMPLETE);
            filter.addAction(Constants.ACTION.ACTION_DELETE_TT_RECEIVE_GOODS_IN_TEMP_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_DELETE_TT_RECEIVE_GOODS_IN_TEMP_FAILED);
            filter.addAction(Constants.ACTION.ACTION_MODIFIED_ITEM_COMPLETE);
            filter.addAction(Constants.ACTION.ACTION_ENTERING_WAREHOUSE_DIVIDED_DIALOG_ADD);
            filter.addAction(Constants.ACTION.ACTION_ENTERING_WAREHOUSE_DIVIDED_DIALOG_SHOW);
            //filter.addAction(Constants.ACTION.ACTION_ENTERING_WAREHOUSE_HIDE_CONFIRM_BUTTON);
            filter.addAction(Constants.ACTION.ACTION_SCAN_RESET);
            filter.addAction("unitech.scanservice.data");
            //filter.addAction("unitech.scanservice.datatype");
            //filter.addAction("unitech.scanservice.scan2key_setting");
            fragmentContext.registerReceiver(mReceiver, filter);
            isRegister = true;
            Log.d(TAG, "registerReceiver mReceiver");
        }

        /*no_list.clear();
        detailList.clear();

        no_list.add("test1");
        no_list.add("test2");
        detailList.put("test1", new ArrayList<DetailItem>());
        detailList.put("test2", new ArrayList<DetailItem>());

        DetailItem item0 = new DetailItem();
        item0.setTitle("title 0");
        item0.setName("text 0");
        detailList.get("test1").add(item0);

        DetailItem item1 = new DetailItem();
        item1.setTitle("title 1");
        item1.setName("text 1");
        detailList.get("test1").add(item1);

        DetailItem item2 = new DetailItem();
        item2.setTitle("title 2");
        item2.setName("text 2");
        detailList.get("test1").add(item2);

        DetailItem item3 = new DetailItem();
        item3.setTitle("title 3");
        item3.setName("text 3");
        detailList.get("test1").add(item3);

        DetailItem item4 = new DetailItem();
        item4.setTitle("title 4");
        item4.setName("text 4");
        detailList.get("test1").add(item4);

        DetailItem item5 = new DetailItem();
        item5.setTitle("title 5");
        item5.setName("text 5");
        detailList.get("test1").add(item5);

        DetailItem item6 = new DetailItem();
        item6.setTitle("title 6");
        item6.setName("text 6");
        detailList.get("test1").add(item6);

        DetailItem item7 = new DetailItem();
        item7.setTitle("title 7");
        item7.setName("text 7");
        detailList.get("test1").add(item7);

        DetailItem item8 = new DetailItem();
        item8.setTitle("title 8");
        item8.setName("text 8");
        detailList.get("test1").add(item8);

        DetailItem item9 = new DetailItem();
        item9.setTitle("title 9");
        item9.setName("text 9");
        detailList.get("test1").add(item9);

        DetailItem item10 = new DetailItem();
        item10.setTitle("title 10");
        item10.setName("text 10");
        detailList.get("test1").add(item10);

        DetailItem item11 = new DetailItem();
        item11.setTitle("title 11");
        item11.setName("text 11");
        detailList.get("test1").add(item11);

        DetailItem item12 = new DetailItem();
        item12.setTitle("title 0");
        item12.setName("text 0");
        detailList.get("test2").add(item12);

        DetailItem item13 = new DetailItem();
        item13.setTitle("title 1");
        item13.setName("text 1");
        detailList.get("test2").add(item13);

        inspectedReceiveExpanedAdater = new InspectedReceiveExpanedAdater(fragmentContext, R.layout.list_group, R.layout.inspected_receive_list_item, no_list, detailList);
        expandableListView.setAdapter(inspectedReceiveExpanedAdater);*/


        return view;
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, "onDestroy");

        if (isRegister && mReceiver != null) {
            try {
                fragmentContext.unregisterReceiver(mReceiver);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            isRegister = false;
            mReceiver = null;
            Log.d(TAG, "unregisterReceiver mReceiver");
        }

        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

    }

    public void toast(String message) {
        Toast toast = Toast.makeText(fragmentContext, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }


}
