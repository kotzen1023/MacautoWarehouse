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
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.macauto.macautowarehouse.data.Constants;
import com.macauto.macautowarehouse.data.DetailItem;
import com.macauto.macautowarehouse.data.InspectedReceiveExpanedAdater;
import com.macauto.macautowarehouse.data.InspectedReceiveItem;
import com.macauto.macautowarehouse.service.ConfirmEnteringWarehouseService;
import com.macauto.macautowarehouse.service.ExecuteScriptTTService;
import com.macauto.macautowarehouse.service.GetDocTypeIsRegOrSubService;
import com.macauto.macautowarehouse.service.GetReceiveGoodsInDataService;
import com.macauto.macautowarehouse.table.DataTable;

import java.util.ArrayList;
import java.util.HashMap;

import static com.macauto.macautowarehouse.MainActivity.pda_type;

public class EnteringWarehouseFragmnet extends Fragment {
    private static final String TAG = EnteringWarehouseFragmnet.class.getName();

    private Context fragmentContext;
    private ExpandableListView expandableListView;
    public static InspectedReceiveExpanedAdater inspectedReceiveExpanedAdater;

    public static ArrayList<String> no_list = new ArrayList<>();
    //public static HashMap<String, ArrayList<ContactItem>> staticContactList = new HashMap<> ();
    public static HashMap<String, ArrayList<DetailItem>> detailList = new HashMap<> ();

    private static BroadcastReceiver mReceiver = null;
    private static boolean isRegister = false;

    private boolean is_barcode_receive = false;
    public static DataTable dataTable;
    private static boolean is_getScan = false;
    ProgressDialog loadDialog = null;
    InputMethodManager imm;
    public static int current_expanded_group = -1;
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




        expandableListView = view.findViewById(R.id.listViewExpand);
        //TextView textView = view.findViewById(R.id.textEnteringWarehouse);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                Log.d(TAG, "groupPosition = "+groupPosition+", childPosition = "+childPosition);

                DetailItem item = inspectedReceiveExpanedAdater.getChild(groupPosition, childPosition);

                if (childPosition == 9) { //rvv33(stock no), rvb33(quantity)

                    /*if (item.getLinearLayout().getVisibility() == View.VISIBLE) {
                        item.getLinearLayout().setVisibility(View.GONE);
                        item.getTextView().setVisibility(View.VISIBLE);
                    } else {
                        item.getLinearLayout().setVisibility(View.VISIBLE);
                        item.getTextView().setVisibility(View.GONE);
                        item.getEdit().setFocusable(true);
                    }*/
                    item.getLinearLayout().setVisibility(View.VISIBLE);
                    item.getTextView().setVisibility(View.GONE);

                    //item.getEdit().requestFocus();
                    //item.getEdit().setSelection(item.getName().length());

                    //imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
                }

                return true;
            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Log.d(TAG, "groupPosition = "+groupPosition);

                current_expanded_group = groupPosition;

                Log.e(TAG, "current_expanded_group = "+current_expanded_group);

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
                Log.d(TAG, "groupPosition = "+groupPosition+" onGroupCollapse");
                if (current_expanded_group == groupPosition) {
                    current_expanded_group = -1;
                }

                Log.e(TAG, "current_expanded_group = "+current_expanded_group);
            }
        });

        final TextView textView = view.findViewById(R.id.barCode);
        final Button btnScan = view.findViewById(R.id.btnEnteringWarehouseScan);
        final Button btnConfirm = view.findViewById(R.id.btnEnteringWarehouseConfirm);
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

                is_barcode_receive = false;
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder confirmdialog = new AlertDialog.Builder(fragmentContext);
                confirmdialog.setIcon(R.drawable.ic_warning_black_48dp);
                confirmdialog.setTitle(fragmentContext.getResources().getString(R.string.entering_warehouse_dialog_title));
                confirmdialog.setMessage(fragmentContext.getResources().getString(R.string.entering_warehouse_dialog_content));
                confirmdialog.setPositiveButton(fragmentContext.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

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
                });
                confirmdialog.setNegativeButton(fragmentContext.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       // btnScan.setVisibility(View.VISIBLE);
                       // btnConfirm.setVisibility(View.GONE);

                    }
                });
                confirmdialog.show();
            }
        });


        final IntentFilter filter;

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Log.e(TAG, "intent.getAction() =>>>> "+intent.getAction().toString());

                if (intent.getAction() != null) {

                    if (intent.getAction().equalsIgnoreCase(Constants.ACTION.SOAP_CONNECTION_FAIL)) {
                        Log.d(TAG, "receive SOAP_CONNECTION_FAIL");
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


                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_GET_INSPECTED_RECEIVE_ITEM_SUCCESS)) {
                        toast(context.getResources().getString(R.string.get_receive_goods_success));


                        inspectedReceiveExpanedAdater = new InspectedReceiveExpanedAdater(context, R.layout.list_group, R.layout.inspected_receive_list_item, no_list, detailList);
                        expandableListView.setAdapter(inspectedReceiveExpanedAdater);
                        is_barcode_receive = false;

                        //btnScan.setVisibility(View.GONE);
                        btnConfirm.setVisibility(View.VISIBLE);

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_GET_INSPECTED_RECEIVE_ITEM_FAILED)) {
                        toast(context.getResources().getString(R.string.get_receive_goods_fail));
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SET_INSPECTED_RECEIVE_ITEM_CLEAN)) {
                        Log.d(TAG, "get ACTION_SET_INSPECTED_RECEIVE_ITEM_CLEAN");

                        if (!is_barcode_receive) {

                            no_list.clear();
                            detailList.clear();
                            if (inspectedReceiveExpanedAdater != null)
                                inspectedReceiveExpanedAdater.notifyDataSetChanged();

                            //start service
                            for (int i = 0; i < 8; i++) {
                                String col = "COLUMN_" + String.valueOf(i);
                                Log.e(TAG, "col_" + i + " = " + intent.getStringExtra(col));
                            }
                            String barcode = intent.getStringExtra("BARCODE");
                            Log.e(TAG, "barcode = " + barcode);

                            Intent getintent = new Intent(context, GetReceiveGoodsInDataService.class);
                            getintent.putExtra("PART_NO", intent.getStringExtra("COLUMN_0"));
                            getintent.putExtra("BARCODE_NO", barcode);
                            context.startService(getintent);
                            is_barcode_receive = true;
                        }


                    }  else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_MODIFIED_ITEM_COMPLETE)) {
                        Log.d(TAG, "get ACTINO_MODIFIED_ITEM_COMPLETE");

                        if (inspectedReceiveExpanedAdater != null)
                            inspectedReceiveExpanedAdater.notifyDataSetChanged();
                        //hide keyboard
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SCAN_RESET)) {
                        //btnScan.setVisibility(View.VISIBLE);
                        //btnConfirm.setVisibility(View.GONE);
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_UPDATE_TT_RECEIVE_IN_RVV33_FAILED)) {
                        Log.d(TAG, "get ACTION_UPDATE_TT_RECEIVE_IN_RVV33_FAILED");
                        toast(getResources().getString(R.string.entering_warehouse_failed));
                        loadDialog.dismiss();
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_UPDATE_TT_RECEIVE_IN_RVV33_SUCCESS)) {
                        Log.d(TAG, "get ACTION_UPDATE_TT_RECEIVE_IN_RVV33_SUCCESS");

                        Intent getintent = new Intent(context, GetDocTypeIsRegOrSubService.class);
                        getintent.setAction(Constants.ACTION.ACTION_GET_DOC_TYPE_IS_REG_OR_SUB_ACTION);
                        getintent.putExtra("CURRENT_TABLE", "0");
                        context.startService(getintent);

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_GET_DOC_TYPE_IS_REG_OR_SUB_FAILED)) {
                        Log.d(TAG, "get ACTION_GET_DOC_TYPE_IS_REG_OR_SUB_FAILED");
                        toast(getResources().getString(R.string.entering_warehouse_failed));
                        loadDialog.dismiss();
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_GET_DOC_TYPE_IS_REG_OR_SUB_SUCCESS)) {
                        Log.d(TAG, "get ACTION_GET_DOC_TYPE_IS_REG_OR_SUB_SUCCESS");
                        String current_table = intent.getStringExtra("CURRENT_TABLE");
                        String doc_type = intent.getStringExtra("DOC_TYPE");
                        String rvu01 = intent.getStringExtra("RVU01");

                        Intent getintent = new Intent(context, ExecuteScriptTTService.class);
                        getintent.setAction(Constants.ACTION.ACTION_EXECUTE_TT_ACTION);
                        getintent.putExtra("DOC_TYPE", doc_type);
                        getintent.putExtra("CURRENT_TABLE", current_table);
                        getintent.putExtra("RVU01", rvu01);
                        context.startService(getintent);
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_EXECUTE_TT_FAILED)) {
                        Log.d(TAG, "get ACTION_EXECUTE_TT_FAILED");
                        toast(getResources().getString(R.string.entering_warehouse_failed));
                        loadDialog.dismiss();
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_EXECUTE_TT_SUCCESS)) {
                        Log.d(TAG, "get ACTION_EXECUTE_TT_SUCCESS");
                        String current_table = intent.getStringExtra("CURRENT_TABLE");
                        int cur_index = Integer.valueOf(current_table);

                        if (cur_index < dataTable.Rows.size()) {
                            Log.e(TAG, "[update table rvu02 = "+dataTable.Rows.get(0).getValue("rvu02")+"]");


                            detailList.get(no_list.get(0)).clear();
                            no_list.remove(0);
                            dataTable.Rows.remove(0);
                            if (inspectedReceiveExpanedAdater != null)
                                inspectedReceiveExpanedAdater.notifyDataSetChanged();

                            //cur_index = cur_index + 1;
                            Log.e(TAG, "next=> table:"+cur_index);

                            Intent getintent = new Intent(context, GetDocTypeIsRegOrSubService.class);
                            getintent.setAction(Constants.ACTION.ACTION_GET_DOC_TYPE_IS_REG_OR_SUB_ACTION);
                            getintent.putExtra("CURRENT_TABLE", String.valueOf(cur_index));
                            context.startService(getintent);
                        }
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ENTERING_WAREHOUSE_COMPLETE)) {
                        Log.d(TAG, "get ACTION_ENTERING_WAREHOUSE_COMPLETE");
                        //delete last one
                        Log.e(TAG, "[update table rvu02 = "+dataTable.Rows.get(0).getValue("rvu02")+"]");
                        detailList.get(no_list.get(0)).clear();
                        no_list.remove(0);
                        dataTable.Rows.remove(0);
                        if (inspectedReceiveExpanedAdater != null)
                            inspectedReceiveExpanedAdater.notifyDataSetChanged();

                        toast(getResources().getString(R.string.entering_warehouse_complete));
                        loadDialog.dismiss();
                        btnConfirm.setVisibility(View.GONE);
                    }



                    else if("unitech.scanservice.data" .equals(intent.getAction())) {
                        Log.d(TAG, "unitech.scanservice.data");
                        Bundle bundle = intent.getExtras();
                        if(bundle != null )
                        {
                            String text = bundle.getString("text");
                            Log.e(TAG, "msg = "+text);

                            is_barcode_receive = false;
                            if (text.length() > 0 ) {
                                int counter = 0;
                                for( int i=0; i<text.length(); i++ ) {
                                    if( text.charAt(i) == '#' ) {
                                        counter++;
                                    }
                                }

                                Log.e(TAG, "counter = "+counter);

                                if (counter == 8) {

                                    String codeArray[] = text.toString().split("#");
                                    Intent scanResultIntent = new Intent(Constants.ACTION.ACTION_SET_INSPECTED_RECEIVE_ITEM_CLEAN);
                                    for (int i = 0; i < codeArray.length; i++) {
                                        Log.e(TAG, "codeArray[" + i + "] = " + codeArray[i]);
                                        String column = "COLUMN_" + String.valueOf(i);
                                        scanResultIntent.putExtra(column, codeArray[i]);
                                    }


                                    scanResultIntent.putExtra("BARCODE", text.toString());
                                    fragmentContext.sendBroadcast(scanResultIntent);
                                } else {
                                    toast(text);
                                    if (text.length() == 5) {//storage place string

                                        if (no_list.size() > 0 && detailList.size() > 0) {

                                            String head = no_list.get(current_expanded_group);
                                            DetailItem detailItem = detailList.get(head).get(7);
                                            detailItem.setName(text);

                                            Intent getFailedIntent = new Intent(Constants.ACTION.ACTION_MODIFIED_ITEM_COMPLETE);
                                            context.sendBroadcast(getFailedIntent);
                                        }
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
            filter.addAction(Constants.ACTION.ACTION_GET_BARCODE_MESSGAGE_COMPLETE);
            filter.addAction(Constants.ACTION.ACTION_SET_INSPECTED_RECEIVE_ITEM_CLEAN);
            filter.addAction(Constants.ACTION.ACTION_GET_INSPECTED_RECEIVE_ITEM_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_GET_INSPECTED_RECEIVE_ITEM_FAILED);

            filter.addAction(Constants.ACTION.ACTION_UPDATE_TT_RECEIVE_IN_RVV33_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_UPDATE_TT_RECEIVE_IN_RVV33_FAILED);
            filter.addAction(Constants.ACTION.ACTION_GET_DOC_TYPE_IS_REG_OR_SUB_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_GET_DOC_TYPE_IS_REG_OR_SUB_FAILED);
            filter.addAction(Constants.ACTION.ACTION_EXECUTE_TT_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_EXECUTE_TT_FAILED);
            filter.addAction(Constants.ACTION.ACTION_ENTERING_WAREHOUSE_COMPLETE);
            filter.addAction(Constants.ACTION.ACTION_MODIFIED_ITEM_COMPLETE);
            filter.addAction(Constants.ACTION.ACTION_SCAN_RESET);
            filter.addAction("unitech.scanservice.data");
            //filter.addAction("unitech.scanservice.datatype");
            //filter.addAction("unitech.scanservice.scan2key_setting");
            fragmentContext.registerReceiver(mReceiver, filter);
            isRegister = true;
            Log.d(TAG, "registerReceiver mReceiver");
        }

        no_list.clear();
        detailList.clear();

        no_list.add("test1");
        no_list.add("test2");
        detailList.put("test1", new ArrayList<DetailItem>());
        detailList.put("test2", new ArrayList<DetailItem>());

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
        detailList.get("test2").add(item11);

        DetailItem item12 = new DetailItem();
        item12.setTitle("title 12");
        item12.setName("text 12");
        detailList.get("test2").add(item12);

        inspectedReceiveExpanedAdater = new InspectedReceiveExpanedAdater(fragmentContext, R.layout.list_group, R.layout.inspected_receive_list_item, no_list, detailList);
        expandableListView.setAdapter(inspectedReceiveExpanedAdater);


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
        Toast toast = Toast.makeText(fragmentContext, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }
}
