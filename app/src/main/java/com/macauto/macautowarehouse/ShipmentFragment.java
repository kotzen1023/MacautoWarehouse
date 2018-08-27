package com.macauto.macautowarehouse;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.macauto.macautowarehouse.data.AllocationMsgStatusItemAdapter;
import com.macauto.macautowarehouse.data.AllocationSendMsgItem;
import com.macauto.macautowarehouse.data.Constants;
import com.macauto.macautowarehouse.data.ShippingNotificationItem;
import com.macauto.macautowarehouse.data.ShippingNotificationItemAdapter;
import com.macauto.macautowarehouse.data.ShippingScannedItem;
import com.macauto.macautowarehouse.data.ShippingScannedItemAdapter;
import com.macauto.macautowarehouse.data.ShippingWaitForScanItem;
import com.macauto.macautowarehouse.data.ShippingWaitForScanItemAdapter;
import com.macauto.macautowarehouse.service.CheckStockNoExistService;
import com.macauto.macautowarehouse.service.GetLocateNoService;
import com.macauto.macautowarehouse.service.GetMadeInfoService;
import com.macauto.macautowarehouse.service.GetSfaDataMessService;
import com.macauto.macautowarehouse.service.GetVarValueService;
import com.macauto.macautowarehouse.service.ShippingGetOgcFile2Service;
import com.macauto.macautowarehouse.service.ShippingGetPreShippingInfoConfirmSpService;
import com.macauto.macautowarehouse.service.ShippingGetPreShippingInfoService;
import com.macauto.macautowarehouse.service.ShippingGetWarehouseQtyService;
import com.macauto.macautowarehouse.table.DataRow;
import com.macauto.macautowarehouse.table.DataTable;

import java.util.ArrayList;

public class ShipmentFragment extends Fragment {
    private static final String TAG = ShipmentFragment.class.getName();

    private Context fragmentContext;

    private static BroadcastReceiver mReceiver = null;
    private static boolean isRegister = false;

    private LinearLayout layoutScanWork;
    private LinearLayout layoutScanned;
    private LinearLayout layoutWaitForScan;

    private EditText editTextPreShipping;
    private Button btnPreShipping;
    private RecyclerView recyclerViewResultDG1;
    private RecyclerView recyclerViewResultDG3;
    private RecyclerView recyclerViewResultDG4;

    private Button btnScanWork;
    private Button btnScanned;
    private Button btnWaitForScan;

    ProgressDialog loadDialog = null;

    public static ArrayList<ShippingNotificationItem> shippingNotifyList = new ArrayList<>();
    private ShippingNotificationItemAdapter shippingNotificationItemAdapter;

    public static ArrayList<ShippingScannedItem> shippingScannedList = new ArrayList<>();
    private ShippingScannedItemAdapter shippingScannedItemAdapter;

    public static ArrayList<ShippingWaitForScanItem> shippingWaitForScanList = new ArrayList<>();
    private ShippingWaitForScanItemAdapter shippingWaitForScanItemAdapter;

    public static DataTable preShippingDataTable;
    public static DataTable dg3_table;
    public static DataTable dg4_table;
    private int need_scan_qty = 0;
    private static boolean scan_permission = false;


    private String x_part_no;
    private String x_batch_no;
    private String b_part_no;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");

        fragmentContext = getContext();

        final  View view = inflater.inflate(R.layout.shipment_fragment, container, false);

        layoutScanWork = view.findViewById(R.id.layoutScanWork);
        layoutScanned = view.findViewById(R.id.layoutScanned);
        layoutWaitForScan = view.findViewById(R.id.layoutWaitForScan);

        btnScanWork = view.findViewById(R.id.btnScanWork);
        btnScanned = view.findViewById(R.id.btnScanned);
        btnWaitForScan = view.findViewById(R.id.btnWaitForScan);

        btnScanWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnScanWork.setEnabled(false);
                btnScanned.setEnabled(true);
                btnWaitForScan.setEnabled(true);

                layoutScanWork.setVisibility(View.VISIBLE);
                layoutScanned.setVisibility(View.GONE);
                layoutWaitForScan.setVisibility(View.GONE);
            }
        });

        btnScanned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnScanWork.setEnabled(true);
                btnScanned.setEnabled(false);
                btnWaitForScan.setEnabled(true);

                layoutScanWork.setVisibility(View.GONE);
                layoutScanned.setVisibility(View.VISIBLE);
                layoutWaitForScan.setVisibility(View.GONE);
            }
        });

        btnWaitForScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnScanWork.setEnabled(true);
                btnScanned.setEnabled(true);
                btnWaitForScan.setEnabled(false);

                layoutScanWork.setVisibility(View.GONE);
                layoutScanned.setVisibility(View.GONE);
                layoutWaitForScan.setVisibility(View.VISIBLE);
            }
        });

        editTextPreShipping = view.findViewById(R.id.editTextShipment);
        btnPreShipping = view.findViewById(R.id.btnOkShipment);
        recyclerViewResultDG1 = view.findViewById(R.id.recyclerViewShipment);
        recyclerViewResultDG3 = view.findViewById(R.id.recyclerViewScanned);
        recyclerViewResultDG4 = view.findViewById(R.id.recyclerViewWaitScan);

        btnPreShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shippingNotifyList.clear();

                if (shippingNotificationItemAdapter != null)
                    shippingNotificationItemAdapter.notifyDataSetChanged();

                if (editTextPreShipping.getText().length() > 0) {
                    Intent getintent = new Intent(fragmentContext, ShippingGetPreShippingInfoService.class);
                    getintent.setAction(Constants.ACTION.ACTION_SHIPMENT_GET_PRE_SHIPPING_INFO_ACTION);
                    getintent.putExtra("PRE_SHIPPING_NO", editTextPreShipping.getText().toString().trim().toUpperCase());
                    fragmentContext.startService(getintent);

                    loadDialog = new ProgressDialog(fragmentContext);
                    loadDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    loadDialog.setTitle(getResources().getString(R.string.Processing));
                    loadDialog.setIndeterminate(false);
                    loadDialog.setCancelable(false);
                    loadDialog.show();
                } else {
                    toast(getResources().getString(R.string.shipment_shipping_notification_empty));
                }


            }
        });


        //Intent getintent = new Intent(context, CheckReceiveGoodService.class);
        //getintent.setAction(Constants.ACTION.ACTION_CHECK_RECEIVE_GOODS);
        //getintent.putExtra("ACCOUNT", account);
        //getintent.putExtra("DEVICE_ID", device_id);
        //getintent.putExtra("service_ip_address", service_ip_address);
        //getintent.putExtra("service_port", service_port);
        //context.startService(getintent);

        final IntentFilter filter;

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //Log.e(TAG, "intent.getAction() =>>>> "+intent.getAction().toString());

                if (intent.getAction() != null) {

                    if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SOCKET_TIMEOUT)) {
                        Log.d(TAG, "receive ACTION_SOCKET_TIMEOUT");
                        if (loadDialog != null)
                            loadDialog.dismiss();
                        toast(getResources().getString(R.string.socket_timeout));

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SHIPMENT_GET_PRE_SHIPPING_INFO_SUCCESS)) {
                        Log.d(TAG, "receive ACTION_SHIPMENT_GET_PRE_SHIPPING_INFO_SUCCESS");
                        if (loadDialog != null)
                            loadDialog.dismiss();

                        if (shippingNotificationItemAdapter != null) {
                            shippingNotificationItemAdapter.notifyDataSetChanged();
                        } else {
                            shippingNotificationItemAdapter = new ShippingNotificationItemAdapter(fragmentContext, shippingNotifyList);
                            recyclerViewResultDG1.setAdapter(shippingNotificationItemAdapter);
                        }

                        //from original line 113
                        if (preShippingDataTable.Rows.size() > 0) {
                            shippingScannedList.clear();
                            Intent getintent = new Intent(fragmentContext, ShippingGetOgcFile2Service.class);
                            getintent.setAction(Constants.ACTION.ACTION_SHIPMENT_GET_OGC_FILE_2_ACTION);
                            getintent.putExtra("SHIPPING_NO", editTextPreShipping.getText().toString().trim().toUpperCase());
                            getintent.putExtra("ITEM_NO", preShippingDataTable.getValue(0,0).toString());
                            fragmentContext.startService(getintent);
                        } /*else {
                            Intent getintent = new Intent(fragmentContext, ShippingGetPreShippingInfoConfirmSpService.class);
                            getintent.setAction(Constants.ACTION.ACTION_SHIPMENT_GET_PRE_SHIPPING_INFO_CONFIRM_SP_ACTION);
                            getintent.putExtra("PRE_SHIPPING_NO", editTextPreShipping.getText().toString());
                            fragmentContext.startService(getintent);
                        }*/

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SHIPMENT_GET_PRE_SHIPPING_INFO_FAILED)) {
                        Log.d(TAG, "receive ACTION_SHIPMENT_GET_PRE_SHIPPING_INFO_FAILED");
                        if (loadDialog != null)
                            loadDialog.dismiss();

                        toast(getResources().getString(R.string.shipment_shipping_notification_order_disposed));

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SHIPMENT_GET_PRE_SHIPPING_INFO_EMPTY)) {
                        Log.d(TAG, "receive ACTION_SHIPMENT_GET_PRE_SHIPPING_INFO_EMPTY");
                        if (loadDialog != null)
                            loadDialog.dismiss();

                        Intent getintent = new Intent(fragmentContext, ShippingGetPreShippingInfoConfirmSpService.class);
                        getintent.setAction(Constants.ACTION.ACTION_SHIPMENT_GET_PRE_SHIPPING_INFO_CONFIRM_SP_ACTION);
                        getintent.putExtra("PRE_SHIPPING_NO", editTextPreShipping.getText().toString());
                        fragmentContext.startService(getintent);

                        //toast(getResources().getString(R.string.shipment_shipping_notification_order_disposed));

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SHIPMENT_GET_OGC_FILE_2_FAILED)) {
                        Log.d(TAG, "receive ACTION_SHIPMENT_GET_OGC_FILE_2_FAILED");
                        if (loadDialog != null)
                            loadDialog.dismiss();

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SHIPMENT_GET_OGC_FILE_2_SUCCESS)) {
                        Log.d(TAG, "receive ACTION_SHIPMENT_GET_OGC_FILE_2_SUCCESS");

                        if (loadDialog != null)
                            loadDialog.dismiss();

                        if (shippingScannedItemAdapter != null) {
                            shippingScannedItemAdapter.notifyDataSetChanged();
                        } else {
                            shippingScannedItemAdapter = new ShippingScannedItemAdapter(fragmentContext, shippingScannedList);
                            recyclerViewResultDG3.setAdapter(shippingScannedItemAdapter);
                        }

                        if (dg3_table.Rows.size() > 0) {
                            b_part_no = preShippingDataTable.Rows.get(0).getValue("ogb04").toString();
                            //need_scan_qty = Convert.ToInt32(DG1.Rows[0].Cells["ogb12"].Value.ToString().Replace(".00", "")) - Convert.ToInt32(DG1.Rows[0].Cells["scanned_qty"].Value.ToString().Replace(".00", ""));
                            need_scan_qty = Integer.valueOf(preShippingDataTable.Rows.get(0).getValue("ogb12").toString().replace(".00", "")) -
                                    Integer.valueOf(preShippingDataTable.Rows.get(0).getValue("scanned_qty").toString().replace(".00", ""));

                            scan_permission = need_scan_qty != 0;
                            if (need_scan_qty == 0)
                            {
                                dg4_table.clear();
                            }

                            shippingWaitForScanList.clear();

                            Intent getintent = new Intent(fragmentContext, ShippingGetWarehouseQtyService.class);
                            getintent.setAction(Constants.ACTION.ACTION_SHIPMENT_GET_WAREHOUSE_ACTION);
                            getintent.putExtra("PART_NO", preShippingDataTable.getValue(0, 1).toString());
                            getintent.putExtra("STOCK_NO", preShippingDataTable.getValue(0,3).toString());
                            getintent.putExtra("AV_QTY", preShippingDataTable.getValue(0,7).toString());
                            getintent.putExtra("SHIPPING_QTY", preShippingDataTable.getValue(0,7).toString());
                            fragmentContext.startService(getintent);
                        }

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SHIPMENT_GET_OGC_FILE_2_EMPTY)) {
                        Log.d(TAG, "receive ACTION_SHIPMENT_GET_OGC_FILE_2_EMPTY");

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SHIPMENT_GET_WAREHOUSE_FAILED)) {
                        Log.d(TAG, "receive ACTION_SHIPMENT_GET_WAREHOUSE_FAILED");

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SHIPMENT_GET_WAREHOUSE_SUCCESS)) {
                        Log.d(TAG, "receive ACTION_SHIPMENT_GET_WAREHOUSE_SUCCESS");

                        if (shippingWaitForScanItemAdapter != null) {
                            shippingWaitForScanItemAdapter.notifyDataSetChanged();
                        } else {
                            shippingWaitForScanItemAdapter = new ShippingWaitForScanItemAdapter(fragmentContext, shippingWaitForScanList);
                            recyclerViewResultDG4.setAdapter(shippingWaitForScanItemAdapter);
                        }

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SHIPMENT_GET_WAREHOUSE_EMPTY)) {
                        Log.d(TAG, "receive ACTION_SHIPMENT_GET_WAREHOUSE_EMPTY");

                    }

                    else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SHIPMENT_GET_PRE_SHIPPING_INFO_CONFIRM_SP_FAILED)) {
                        Log.d(TAG, "receive ACTION_SHIPMENT_GET_PRE_SHIPPING_INFO_CONFIRM_SP_FAILED");



                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SHIPMENT_GET_PRE_SHIPPING_INFO_CONFIRM_SP_SUCCESS)) {
                        Log.d(TAG, "receive ACTION_SHIPMENT_GET_PRE_SHIPPING_INFO_CONFIRM_SP_SUCCESS");

                        String ret = intent.getStringExtra("CONFIRM_SP_RET");
                        if (Integer.valueOf(ret) > 0) {
                            toast(getResources().getString(R.string.shipment_shipping_notification_not_confirm));
                        } else {
                            toast(getResources().getString(R.string.shipment_shipping_notification_order_disposed));
                        }

                    } else if("unitech.scanservice.data" .equals(intent.getAction())) {
                        Log.d(TAG, "unitech.scanservice.data");
                        Bundle bundle = intent.getExtras();
                        if(bundle != null )
                        {
                            String text = bundle.getString("text");
                            Log.e(TAG, "msg = "+text);

                            if (text != null && text.length() > 0) {
                                if (preShippingDataTable != null && preShippingDataTable.Rows.size() > 0) {
                                    if (text.indexOf("#") > 0 && need_scan_qty > 0) {
                                        String barcode_date = text;
                                        String ks[] = barcode_date.split("#");
                                        x_part_no = ks[0];
                                        x_batch_no = ks[1];

                                        if (!set_scanned(x_part_no, x_batch_no)) {
                                            toast(getResources().getString(R.string.shipment_shipping_notification_rescan));
                                        }

                                    }
                                } else {
                                    toast(getResources().getString(R.string.shipment_shipping_notification_input_shipping_notification_first));
                                }
                            }




                        }
                    }

                }
            }
        };

        if (!isRegister) {
            filter = new IntentFilter();
            filter.addAction(Constants.ACTION.ACTION_SOCKET_TIMEOUT);
            filter.addAction(Constants.ACTION.ACTION_SHIPMENT_GET_PRE_SHIPPING_INFO_FAILED);
            filter.addAction(Constants.ACTION.ACTION_SHIPMENT_GET_PRE_SHIPPING_INFO_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_SHIPMENT_GET_PRE_SHIPPING_INFO_EMPTY);
            filter.addAction(Constants.ACTION.ACTION_SHIPMENT_GET_OGC_FILE_2_FAILED);
            filter.addAction(Constants.ACTION.ACTION_SHIPMENT_GET_OGC_FILE_2_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_SHIPMENT_GET_OGC_FILE_2_EMPTY);
            filter.addAction(Constants.ACTION.ACTION_SHIPMENT_GET_WAREHOUSE_FAILED);
            filter.addAction(Constants.ACTION.ACTION_SHIPMENT_GET_WAREHOUSE_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_SHIPMENT_GET_WAREHOUSE_EMPTY);
            filter.addAction(Constants.ACTION.ACTION_SHIPMENT_GET_PRE_SHIPPING_INFO_CONFIRM_SP_FAILED);
            filter.addAction(Constants.ACTION.ACTION_SHIPMENT_GET_PRE_SHIPPING_INFO_CONFIRM_SP_SUCCESS);
            filter.addAction("unitech.scanservice.data");
            fragmentContext.registerReceiver(mReceiver, filter);
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

    public boolean set_scanned(String part_no, String batch_no) {
        boolean ret = false;

        if (b_part_no != part_no)
        {
            return ret;
        }

        if (need_scan_qty == 0) {
            return true;
        }

        //this.Text = reer + DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss") + "      " + ss_id;

        int r1 = 0;
        int h = 0;

        for (DataRow yx : dg4_table.Rows) {
            if (yx.getValue("scanned_qty1").toString().equals(yx.getValue("img10").toString())) {
                continue;
            }

            if (yx.getValue("img04").toString().equals(batch_no)) {
                r1 = Integer.valueOf(yx.getValue("img10").toString());
                //this.Text = r1.ToString() + "----" + DateTime.Now.ToString("HH:mm:ss") + "-----" + need_scan_qty.ToString();
                //yx.Cells["scan_sp"].Value = "Y";
                yx.setValue("scan_sp", "Y");
                ret = true;

                if (r1 >= need_scan_qty)
                {
                    //yx.Cells["scanned_qty1"].Value = need_scan_qty;
                    yx.setValue("scanned_qty1", need_scan_qty);
                    break;
                }
                else
                {
                    //yx.Cells["scanned_qty1"].Value = r1;
                    yx.setValue("scanned_qty1", r1);
                    need_scan_qty = need_scan_qty - r1;
                }
            }
        }




        return ret;

    }
}
