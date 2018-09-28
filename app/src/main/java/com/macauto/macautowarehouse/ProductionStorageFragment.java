package com.macauto.macautowarehouse;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
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

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.macauto.macautowarehouse.data.Constants;

import com.macauto.macautowarehouse.data.ProductionStorageItem;
import com.macauto.macautowarehouse.data.ProductionStorageItemAdapter;
import com.macauto.macautowarehouse.service.CheckStockLocateNoExistService;
import com.macauto.macautowarehouse.service.CheckTTProductEntryAlreadyConfirm;
import com.macauto.macautowarehouse.service.DeleteTTReceiveGoodsInTempService;
import com.macauto.macautowarehouse.service.ExecuteScriptTTService;
import com.macauto.macautowarehouse.service.GetDocTypeIsRegOrSubService;
import com.macauto.macautowarehouse.service.GetReceiveGoodsInDataService;
import com.macauto.macautowarehouse.service.GetTTProductEntryService;
import com.macauto.macautowarehouse.service.UpdateTTProductEntryLocateNoService;
import com.macauto.macautowarehouse.table.DataColumn;
import com.macauto.macautowarehouse.table.DataRow;
import com.macauto.macautowarehouse.table.DataTable;

import java.util.ArrayList;

import static com.macauto.macautowarehouse.MainActivity.emp_no;
import static com.macauto.macautowarehouse.MainActivity.k_id;
import static com.macauto.macautowarehouse.MainActivity.web_soap_port;
import static com.macauto.macautowarehouse.data.InspectedReceiveExpanedAdater.mSparseBooleanArray;

public class ProductionStorageFragment extends Fragment {
    private static final String TAG = ProductionStorageFragment.class.getName();

    private Context fragmentContext;

    private static BroadcastReceiver mReceiver = null;
    private static boolean isRegister = false;

    ProgressDialog loadDialog = null;

    private EditText exitTextInNo;
    private Button btnUserInputConfirm;
    private TextView c_in_no;
    private TextView c_in_date;
    private TextView c_made_no;
    private TextView c_dept_name;
    private ListView productListView;
    private Button btnInStockConfirm;

    public static DataTable dataTable_RR;
    public static DataTable product_table_X_M;
    private ProductionStorageItemAdapter productionStorageItemAdapter;
    public static ArrayList<ProductionStorageItem> productList = new ArrayList<>();
    public static int item_select = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentContext = getContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");

        item_select = -1;

        final  View view = inflater.inflate(R.layout.production_storage_fragment, container, false);

        exitTextInNo = view.findViewById(R.id.exitTextInNo);
        btnUserInputConfirm = view.findViewById(R.id.btnUserInputConfirm);
        c_in_no = view.findViewById(R.id.c_in_no);
        c_in_date = view.findViewById(R.id.c_in_date);
        c_made_no = view.findViewById(R.id.c_made_no);
        c_dept_name = view.findViewById(R.id.c_dept_name);
        productListView = view.findViewById(R.id.productListView);
        btnInStockConfirm = view.findViewById(R.id.btnInStockConfirm);

        productList.clear();

        /*ProductionStorageItem item1 = new ProductionStorageItem();
        item1.setPart_no("test1");
        item1.setQty("qty1");
        item1.setStock_unit("unit1");
        item1.setLocate_no("Locate1");
        item1.setLocate_no_scan("scan1");
        productList.add(item1);

        ProductionStorageItem item2 = new ProductionStorageItem();
        item2.setPart_no("test2");
        item2.setQty("qty2");
        item2.setStock_unit("unit2");
        item2.setLocate_no("Locate2");
        item2.setLocate_no_scan("scan2");
        productList.add(item2);*/

        productionStorageItemAdapter = new ProductionStorageItemAdapter(fragmentContext, R.layout.production_storage_fragment_swipe_item, productList);
        productListView.setAdapter(productionStorageItemAdapter);

        btnUserInputConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent checkIntent = new Intent(fragmentContext, CheckTTProductEntryAlreadyConfirm.class);
                checkIntent.setAction(Constants.ACTION.ACTION_CHECK_TT_PRODUCT_ENTRY_ALREADY_CONFIRM_ACTION);
                checkIntent.putExtra("IN_NO", exitTextInNo.getText().toString());
                fragmentContext.startService(checkIntent);

                loadDialog = new ProgressDialog(fragmentContext);
                loadDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                loadDialog.setTitle(getResources().getString(R.string.Processing));
                loadDialog.setIndeterminate(false);
                loadDialog.setCancelable(false);
                loadDialog.show();
            }
        });

        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "select "+position);

                item_select = position;

                //deselect other
                for (int i=0; i<productList.size(); i++) {

                    if (i == position) {
                        if (productList.get(i).isSelected()) {
                            productList.get(i).setSelected(false);
                            item_select = -1;
                        }
                        else
                            productList.get(i).setSelected(true);

                    } else {
                        productList.get(i).setSelected(false);

                    }

                    productListView.invalidateViews();
                }
            }
        });

        productListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Intent detailIntent = new Intent(fragmentContext, ProductionStorageDetailActivity.class);
                detailIntent.putExtra("INDEX", String.valueOf(position));
                fragmentContext.startActivity(detailIntent);

                return true;
            }
        });

        btnInStockConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean found_product_not_scan = false;
                if (productList.size() > 0) {
                    for (int i=0; i<productList.size(); i++) {
                        if (productList.get(i).getLocate_no_scan().equals("")) {
                            found_product_not_scan = true;
                            break;
                        }
                    }


                    if (!found_product_not_scan) { //all scanner, start in stock

                        //unselect all
                        for (int i=0; i<productList.size(); i++) {
                            productList.get(i).setSelected(false);
                        }
                        productListView.invalidateViews();
                        item_select = -1;

                        //start from last
                        int last = productList.size()-1;

                        Intent checkIntent = new Intent(fragmentContext, CheckStockLocateNoExistService.class);
                        checkIntent.setAction(Constants.ACTION.ACTION_PRODUCT_CHECK_STOCK_LOCATE_NO_EXIST_ACTION);
                        checkIntent.putExtra("STOCK_NO", productList.get(last).getStock_no());
                        checkIntent.putExtra("LOCATE_NO", productList.get(last).getLocate_no());
                        checkIntent.putExtra("CURRENT_INDEX", String.valueOf(last));
                        fragmentContext.startService(checkIntent);


                        loadDialog = new ProgressDialog(fragmentContext);
                        loadDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        loadDialog.setTitle(getResources().getString(R.string.Processing));
                        loadDialog.setIndeterminate(false);
                        loadDialog.setCancelable(false);
                        loadDialog.show();
                    } else {
                        toast(getResources().getString(R.string.production_storage_product_locate_not_scanned));
                    }



                }






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
                        loadDialog.dismiss();
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SOCKET_TIMEOUT)) {
                        Log.d(TAG, "receive ACTION_SOCKET_TIMEOUT");
                        if (loadDialog != null)
                            loadDialog.dismiss();
                        toast(getResources().getString(R.string.socket_timeout));

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_CHECK_TT_PRODUCT_ENTRY_ALREADY_CONFIRM_YES)) {
                        Log.d(TAG, "receive ACTION_CHECK_TT_PRODUCT_ENTRY_ALREADY_CONFIRM_YES");

                        toast(getResources().getString(R.string.production_storage_inbound_order_has_confirmed, exitTextInNo.getText().toString()));
                        loadDialog.dismiss();
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_CHECK_TT_PRODUCT_ENTRY_ALREADY_CONFIRM_NO)) {
                        Log.d(TAG, "receive ACTION_CHECK_TT_PRODUCT_ENTRY_ALREADY_CONFIRM_NO");

                        Intent checkIntent = new Intent(fragmentContext, GetTTProductEntryService.class);
                        checkIntent.setAction(Constants.ACTION.ACTION_GET_TT_PRODUCT_ENTRY_ACTION);
                        checkIntent.putExtra("IN_NO", exitTextInNo.getText().toString());
                        fragmentContext.startService(checkIntent);

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_CHECK_TT_PRODUCT_ENTRY_ALREADY_CONFIRM_FAILED)) {
                        Log.d(TAG, "receive ACTION_CHECK_TT_PRODUCT_ENTRY_ALREADY_CONFIRM_FAILED");
                        loadDialog.dismiss();
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_GET_TT_PRODUCT_ENTRY_FAILED)) {
                        Log.d(TAG, "receive ACTION_GET_TT_PRODUCT_ENTRY_FAILED");
                        loadDialog.dismiss();
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_GET_TT_PRODUCT_ENTRY_SUCCESS)) {
                        Log.d(TAG, "receive ACTION_GET_TT_PRODUCT_ENTRY_SUCCESS");
                        loadDialog.dismiss();

                        if (productionStorageItemAdapter != null) {
                            productionStorageItemAdapter.notifyDataSetChanged();
                        } else {
                            productionStorageItemAdapter = new ProductionStorageItemAdapter(fragmentContext, R.layout.production_storage_fragment_swipe_item, productList);
                            productListView.setAdapter(productionStorageItemAdapter);
                        }

                        c_in_no.setText(productList.get(0).getIn_no());
                        c_in_date.setText(productList.get(0).getIn_date());
                        c_made_no.setText(productList.get(0).getMade_no());
                        c_dept_name.setText(productList.get(0).getDept_name());

                        btnInStockConfirm.setEnabled(true);


                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_GET_TT_PRODUCT_ENTRY_EMPTY)) {
                        Log.d(TAG, "receive ACTION_GET_TT_PRODUCT_ENTRY_EMPTY");
                        loadDialog.dismiss();
                        toast(getResources().getString(R.string.production_storage_inbound_order_has_confirmed, exitTextInNo.getText().toString()));

                        btnInStockConfirm.setEnabled(false);
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_PRODUCT_CHECK_STOCK_LOCATE_NO_EXIST_YES)) {
                        Log.d(TAG, "receive ACTION_PRODUCT_CHECK_STOCK_LOCATE_NO_EXIST_YES");
                        String current_index = intent.getStringExtra("CURRENT_INDEX");
                        int index = Integer.valueOf(current_index);

                        Intent updateIntent = new Intent(fragmentContext, UpdateTTProductEntryLocateNoService.class);
                        updateIntent.setAction(Constants.ACTION.ACTION_PRODUCT_UPDATE_TT_PRODUCT_ENTRY_LOCATE_NO_ACTION);
                        updateIntent.putExtra("IN_NO", productList.get(index).getIn_no());
                        updateIntent.putExtra("ITEM_NO", productList.get(index).getItem_no());
                        updateIntent.putExtra("NEW_LOCATE_NO", productList.get(index).getLocate_no());
                        updateIntent.putExtra("CURRENT_INDEX", String.valueOf(index));
                        fragmentContext.startService(updateIntent);


                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_PRODUCT_CHECK_STOCK_LOCATE_NO_EXIST_NO)) {
                        Log.d(TAG, "receive ACTION_PRODUCT_CHECK_STOCK_LOCATE_NO_EXIST_NO");
                        loadDialog.dismiss();

                        toast(getResources().getString(R.string.production_storage_in_stock_process_abort));
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_PRODUCT_CHECK_STOCK_LOCATE_NO_EXIST_FAILED)) {
                        Log.d(TAG, "receive ACTION_PRODUCT_CHECK_STOCK_LOCATE_NO_EXIST_FAILED");

                        loadDialog.dismiss();
                        toast(getResources().getString(R.string.production_storage_in_stock_process_abort));

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_PRODUCT_UPDATE_TT_PRODUCT_ENTRY_LOCATE_NO_SUCCESS)) {
                        Log.d(TAG, "receive ACTION_PRODUCT_UPDATE_TT_PRODUCT_ENTRY_LOCATE_NO_SUCCESS");
                        String current_index = intent.getStringExtra("CURRENT_INDEX");
                        int index = Integer.valueOf(current_index);

                        //delete this
                        productList.remove(index);
                        productListView.invalidateViews();
                        //delete datatable
                        dataTable_RR.Rows.remove(index);

                        if (index > 0) {
                            //set next
                            index = index - 1;

                            Intent checkIntent = new Intent(fragmentContext, CheckStockLocateNoExistService.class);
                            checkIntent.setAction(Constants.ACTION.ACTION_PRODUCT_CHECK_STOCK_LOCATE_NO_EXIST_ACTION);
                            checkIntent.putExtra("STOCK_NO", productList.get(index).getStock_no());
                            checkIntent.putExtra("LOCATE_NO", productList.get(index).getLocate_no());
                            checkIntent.putExtra("CURRENT_INDEX", String.valueOf(index));
                            fragmentContext.startService(checkIntent);
                        } else {

                            //then execute TT
                            if (product_table_X_M != null) {
                                product_table_X_M.clear();
                            } else {
                                product_table_X_M = new DataTable("X_M");
                            }

                            DataColumn v1 = new DataColumn("script");
                            product_table_X_M.Columns.Add(v1);

                            DataRow kr;
                            kr = product_table_X_M.NewRow();

                            String s_p = "1";
                            if (web_soap_port.equals("8484")) {
                                s_p = "2";
                            }

                            String script_string = "sh run_me "+s_p+" 2 "+c_in_no.getText().toString()+" "+emp_no;
                            kr.setValue("script", script_string);
                            product_table_X_M.Rows.add(kr);

                            Intent executeIntent = new Intent(context, ExecuteScriptTTService.class);
                            executeIntent.setAction(Constants.ACTION.ACTION_EXECUTE_TT_ACTION);
                            executeIntent.putExtra("PROCESS_TYPE", "1"); //production execute TT
                            context.startService(executeIntent);
                        }


                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_PRODUCT_UPDATE_TT_PRODUCT_ENTRY_LOCATE_NO_FAILED)) {
                        Log.d(TAG, "receive ACTION_PRODUCT_UPDATE_TT_PRODUCT_ENTRY_LOCATE_NO_FAILED");
                        loadDialog.dismiss();
                        toast(getResources().getString(R.string.production_storage_update_process_error));
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_EXECUTE_TT_FAILED)) {
                        Log.d(TAG, "receive ACTION_EXECUTE_TT_FAILED");
                        loadDialog.dismiss();
                        toast(getResources().getString(R.string.production_storage_update_process_error));
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_PRODUCT_IN_STOCK_WORK_COMPLETE)) {
                        Log.d(TAG, "receive ACTION_PRODUCT_IN_STOCK_WORK_COMPLETE");
                        loadDialog.dismiss();
                        toast(getResources().getString(R.string.production_storage_in_stock_process_complete));
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_PRODUCT_IN_STOCK_WORK_COMPLETE)) {
                        Log.d(TAG, "receive ACTION_PRODUCT_IN_STOCK_WORK_COMPLETE");
                        loadDialog.dismiss();
                        toast(getResources().getString(R.string.production_storage_in_stock_process_complete));
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_PRODUCT_SWIPE_LAYOUT_UPDATE)) {
                        Log.d(TAG, "receive ACTION_PRODUCT_SWIPE_LAYOUT_UPDATE");
                        if (productionStorageItemAdapter != null)
                            productionStorageItemAdapter.notifyDataSetChanged();
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_PRODUCT_DELETE_ITEM_CONFIRM)) {
                        Log.d(TAG, "receive ACTION_PRODUCT_DELETE_ITEM_CONFIRM");

                        String index_string = intent.getStringExtra("INDEX");
                        int index = Integer.valueOf(index_string);

                        if (dataTable_RR != null && dataTable_RR.Rows.get(index) != null) {
                            dataTable_RR.Rows.remove(index);
                        }

                        productList.remove(index);

                        item_select = -1;

                        for (int i=0; i<productList.size(); i++) {
                            productList.get(i).setSelected(false);
                        }

                        if (productionStorageItemAdapter != null)
                            productionStorageItemAdapter.notifyDataSetChanged();


                    }


                    else if("unitech.scanservice.data".equals(intent.getAction())) {
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

                                if (counter >= 1) {

                                    toast(getResources().getString(R.string.production_storage_not_inbound_order));


                                } else {
                                    text = text.replaceAll("\\n","");


                                    if (item_select != -1) { //scan locate

                                        if (text.length() != 16) {
                                            toast(text);
                                            if (dataTable_RR != null && dataTable_RR.Rows.size() > 0) {
                                                if (dataTable_RR.Rows.get(item_select) != null)
                                                    dataTable_RR.Rows.get(item_select).setValue("locate_no", text);
                                            }

                                            productList.get(item_select).setLocate_no_scan(text);
                                            //productListView.invalidateViews();
                                            if (productionStorageItemAdapter != null) {
                                                productionStorageItemAdapter.notifyDataSetChanged();
                                            }

                                            productListView.invalidateViews();
                                        }


                                    } else { //item_select == -1
                                        exitTextInNo.setText(text);

                                        Log.e(TAG, "text.length() == "+text.length());
                                        if (text.length() == 16) {


                                            productList.clear();
                                            if (productionStorageItemAdapter != null)
                                                productionStorageItemAdapter.notifyDataSetChanged();


                                            Intent checkIntent = new Intent(fragmentContext, CheckTTProductEntryAlreadyConfirm.class);
                                            checkIntent.setAction(Constants.ACTION.ACTION_CHECK_TT_PRODUCT_ENTRY_ALREADY_CONFIRM_ACTION);
                                            checkIntent.putExtra("IN_NO", text);
                                            fragmentContext.startService(checkIntent);

                                            loadDialog = new ProgressDialog(fragmentContext);
                                            loadDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                            loadDialog.setTitle(getResources().getString(R.string.Processing));
                                            loadDialog.setIndeterminate(false);
                                            loadDialog.setCancelable(false);
                                            loadDialog.show();
                                        }

                                    }


                                }

                            }
                        }
                    }

                }
            }
        };

        if (!isRegister) {
            filter = new IntentFilter();
            filter.addAction(Constants.ACTION.SOAP_CONNECTION_FAIL);
            filter.addAction(Constants.ACTION.ACTION_SOCKET_TIMEOUT);
            filter.addAction(Constants.ACTION.ACTION_CHECK_TT_PRODUCT_ENTRY_ALREADY_CONFIRM_YES);
            filter.addAction(Constants.ACTION.ACTION_CHECK_TT_PRODUCT_ENTRY_ALREADY_CONFIRM_NO);
            filter.addAction(Constants.ACTION.ACTION_CHECK_TT_PRODUCT_ENTRY_ALREADY_CONFIRM_FAILED);

            filter.addAction(Constants.ACTION.ACTION_GET_TT_PRODUCT_ENTRY_FAILED);
            filter.addAction(Constants.ACTION.ACTION_GET_TT_PRODUCT_ENTRY_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_GET_TT_PRODUCT_ENTRY_EMPTY);

            filter.addAction(Constants.ACTION.ACTION_PRODUCT_CHECK_STOCK_LOCATE_NO_EXIST_YES);
            filter.addAction(Constants.ACTION.ACTION_PRODUCT_CHECK_STOCK_LOCATE_NO_EXIST_NO);
            filter.addAction(Constants.ACTION.ACTION_PRODUCT_CHECK_STOCK_LOCATE_NO_EXIST_FAILED);

            filter.addAction(Constants.ACTION.ACTION_PRODUCT_UPDATE_TT_PRODUCT_ENTRY_LOCATE_NO_FAILED);
            filter.addAction(Constants.ACTION.ACTION_PRODUCT_UPDATE_TT_PRODUCT_ENTRY_LOCATE_NO_SUCCESS);

            filter.addAction(Constants.ACTION.ACTION_EXECUTE_TT_FAILED);
            filter.addAction(Constants.ACTION.ACTION_PRODUCT_IN_STOCK_WORK_COMPLETE);
            filter.addAction(Constants.ACTION.ACTION_PRODUCT_SWIPE_LAYOUT_UPDATE);

            filter.addAction(Constants.ACTION.ACTION_PRODUCT_DELETE_ITEM_CONFIRM);

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
}
