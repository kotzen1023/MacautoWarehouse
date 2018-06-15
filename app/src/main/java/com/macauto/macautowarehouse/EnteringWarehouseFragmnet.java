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
import android.widget.TextView;
import android.widget.Toast;

import com.macauto.macautowarehouse.data.Constants;
import com.macauto.macautowarehouse.data.DetailItem;
import com.macauto.macautowarehouse.data.InspectedReceiveExpanedAdater;
import com.macauto.macautowarehouse.data.InspectedReceiveItem;
import com.macauto.macautowarehouse.service.ConfirmEnteringWarehouseService;
import com.macauto.macautowarehouse.service.GetReceiveGoodsInDataService;
import com.macauto.macautowarehouse.table.DataTable;

import java.util.ArrayList;
import java.util.HashMap;

public class EnteringWarehouseFragmnet extends Fragment {
    private static final String TAG = EnteringWarehouseFragmnet.class.getName();

    private Context context;
    private ExpandableListView expandableListView;
    public static InspectedReceiveExpanedAdater inspectedReceiveExpanedAdater;

    public static ArrayList<String> no_list = new ArrayList<>();
    //public static HashMap<String, ArrayList<ContactItem>> staticContactList = new HashMap<> ();
    public static HashMap<String, ArrayList<DetailItem>> detailList = new HashMap<> ();

    private static BroadcastReceiver mReceiver = null;
    private static boolean isRegister = false;

    private boolean is_barcode_receive = false;
    public static DataTable dataTable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");



        final  View view = inflater.inflate(R.layout.entering_warehouse_fragment, container, false);

        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        expandableListView = view.findViewById(R.id.listViewExpand);
        //TextView textView = view.findViewById(R.id.textEnteringWarehouse);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                Log.d(TAG, "groupPosition = "+groupPosition+", childPosition = "+childPosition);

                DetailItem item = inspectedReceiveExpanedAdater.getChild(groupPosition, childPosition);

                if (childPosition == 7) {



                    if (item.getLinearLayout().getVisibility() == View.VISIBLE) {
                        item.getLinearLayout().setVisibility(View.GONE);
                        item.getTextView().setVisibility(View.VISIBLE);
                    } else {
                        item.getLinearLayout().setVisibility(View.VISIBLE);
                        item.getTextView().setVisibility(View.GONE);
                    }
                }

                return true;
            }
        });

        final Button btnScan = view.findViewById(R.id.btnEnteringWarehouseScan);
        final Button btnConfirm = view.findViewById(R.id.btnEnteringWarehouseConfirm);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context, ScannedBarcodeActivity.class);
                intent.putExtra("SPLITTER_COUNT", "8");
                startActivity(intent);

                /*Intent intent = new Intent();
                intent.setAction("unitech.scanservice.scan2key_setting");
                intent.putExtra("scan2key", false);
                context.sendBroadcast(intent);*/


            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder confirmdialog = new AlertDialog.Builder(context);
                confirmdialog.setIcon(R.drawable.ic_warning_black_48dp);
                confirmdialog.setTitle("Entering Warehouse");
                confirmdialog.setMessage("Confirm entering warehouse?");
                confirmdialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Intent getintent = new Intent(context, ConfirmEnteringWarehouseService.class);
                        getintent.setAction(Constants.ACTION.ACTION_CONFIRM_ENTERING_WAREHOUSE_ACTION);
                        context.startService(getintent);
                    }
                });
                confirmdialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       // btnScan.setVisibility(View.VISIBLE);
                       // btnConfirm.setVisibility(View.GONE);

                    }
                });
                confirmdialog.show();
            }
        });
        //context = getContext();

        IntentFilter filter;

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction() != null) {

                    if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_GET_BARCODE_MESSGAGE_COMPLETE)) {
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
                        toast("Get item success!");


                        inspectedReceiveExpanedAdater = new InspectedReceiveExpanedAdater(context, R.layout.list_group, R.layout.inspected_receive_list_item, no_list, detailList);
                        expandableListView.setAdapter(inspectedReceiveExpanedAdater);
                        is_barcode_receive = false;

                        //btnScan.setVisibility(View.GONE);
                        btnConfirm.setVisibility(View.VISIBLE);

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_GET_INSPECTED_RECEIVE_ITEM_FAILED)) {
                        toast("Get item failed!");
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

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SCAN_RESET)) {
                        //btnScan.setVisibility(View.VISIBLE);
                        //btnConfirm.setVisibility(View.GONE);
                    } else if("unitech.scanservice.data" .equals(intent.getAction())) {
                        Log.d(TAG, "unitech.scanservice.data");
                        Bundle bundle = intent.getExtras();
                        if(bundle != null )
                        {
                            String text = bundle.getString("text");
                            Log.e(TAG, "msg = "+text);
                        }
                    } else if("unitech.scanservice.datatype" .equals(intent.getAction())) {
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
                    }

                }
            }
        };

        if (!isRegister) {
            filter = new IntentFilter();
            filter.addAction(Constants.ACTION.ACTION_GET_BARCODE_MESSGAGE_COMPLETE);
            filter.addAction(Constants.ACTION.ACTION_SET_INSPECTED_RECEIVE_ITEM_CLEAN);
            filter.addAction(Constants.ACTION.ACTION_GET_INSPECTED_RECEIVE_ITEM_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_GET_INSPECTED_RECEIVE_ITEM_FAILED);
            filter.addAction(Constants.ACTION.ACTION_MODIFIED_ITEM_COMPLETE);
            filter.addAction(Constants.ACTION.ACTION_SCAN_RESET);
            filter.addAction("unitech.scanservice.data");
            filter.addAction("unitech.scanservice.datatype");
            filter.addAction("unitech.scanservice.scan2key_setting");
            context.registerReceiver(mReceiver, filter);
            isRegister = true;
            Log.d(TAG, "registerReceiver mReceiver");
        }


        return view;
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, "onDestroy");

        if (isRegister && mReceiver != null) {
            try {
                context.unregisterReceiver(mReceiver);
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
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }
}
