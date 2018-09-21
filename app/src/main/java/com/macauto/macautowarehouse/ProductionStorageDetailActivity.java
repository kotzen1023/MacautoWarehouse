package com.macauto.macautowarehouse;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.ListView;
import android.widget.Toast;

import com.macauto.macautowarehouse.data.Constants;
import com.macauto.macautowarehouse.data.InspectedDetailItem;
import com.macauto.macautowarehouse.data.InspectedDetailItemAdapter;
import com.macauto.macautowarehouse.data.ProductionStorageDetailItem;
import com.macauto.macautowarehouse.data.ProductionStorageDetailItemAdapter;
import com.macauto.macautowarehouse.data.ProductionStorageItem;
import com.macauto.macautowarehouse.data.ProductionStorageItemAdapter;
import com.macauto.macautowarehouse.service.CheckStockLocateNoExistService;
import com.macauto.macautowarehouse.service.CheckTTProductEntryAlreadyConfirm;
import com.macauto.macautowarehouse.service.ConfirmEnteringWarehouseService;
import com.macauto.macautowarehouse.service.ExecuteScriptTTService;
import com.macauto.macautowarehouse.service.GetTTProductEntryService;
import com.macauto.macautowarehouse.service.UpdateTTProductEntryLocateNoService;
import com.macauto.macautowarehouse.table.DataColumn;
import com.macauto.macautowarehouse.table.DataRow;
import com.macauto.macautowarehouse.table.DataTable;

import java.util.ArrayList;

import static com.macauto.macautowarehouse.MainActivity.emp_no;
import static com.macauto.macautowarehouse.MainActivity.web_soap_port;
import static com.macauto.macautowarehouse.ProductionStorageFragment.dataTable_RR;
import static com.macauto.macautowarehouse.ProductionStorageFragment.item_select;
import static com.macauto.macautowarehouse.ProductionStorageFragment.productList;


public class ProductionStorageDetailActivity extends AppCompatActivity {
    private static final String TAG = "ProductionDetail";

    public static ArrayList<ProductionStorageDetailItem> ProductionDetailList = new ArrayList<>();

    private static BroadcastReceiver mReceiver = null;
    private static boolean isRegister = false;
    //private SearchDetailItemAdapter searchDetailItemAdapter;
    //InputMethodManager imm;
    private ProductionStorageDetailItemAdapter productionStorageDetailItemAdapter;

    private static int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.production_storage_detail_activity);

        ListView listView = findViewById(R.id.productionStorageDetailListView);

        Intent intent = getIntent();
        //setTitle(getResources().getString(R.string.entering_warehouse_dialog_head));

        //imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        //View view = getCurrentFocus();
        //imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_chevron_left_white_24dp);
            actionBar.setTitle("");
        }

        index = Integer.valueOf(intent.getStringExtra("INDEX"));

        ProductionDetailList.clear();

        //in_no
        ProductionStorageDetailItem item1 = new ProductionStorageDetailItem();
        item1.setHeader(getResources().getString(R.string.production_storage_in_no));
        item1.setContent(String.valueOf(productList.get(index).getIn_no()));
        ProductionDetailList.add(item1);
        //item_no
        ProductionStorageDetailItem item2 = new ProductionStorageDetailItem();
        item2.setHeader(getResources().getString(R.string.production_storage_index));
        item2.setContent(productList.get(index).getItem_no());
        ProductionDetailList.add(item2);
        //in_date
        ProductionStorageDetailItem item3 = new ProductionStorageDetailItem();
        item3.setHeader(getResources().getString(R.string.production_storage_in_date));
        item3.setContent(productList.get(index).getIn_date());
        ProductionDetailList.add(item3);
        //made_no
        ProductionStorageDetailItem item4 = new ProductionStorageDetailItem();
        item4.setHeader(getResources().getString(R.string.production_storage_made_no));
        item4.setContent(productList.get(index).getMade_no());
        ProductionDetailList.add(item4);
        //store_type
        ProductionStorageDetailItem item5 = new ProductionStorageDetailItem();
        item5.setHeader(getResources().getString(R.string.production_storage_store_type));
        item5.setContent(productList.get(index).getStore_type());
        ProductionDetailList.add(item5);
        //dept_no
        ProductionStorageDetailItem item6 = new ProductionStorageDetailItem();
        item6.setHeader(getResources().getString(R.string.production_storage_dept_no));
        item6.setContent(productList.get(index).getDept_no());
        ProductionDetailList.add(item6);
        //dept_name
        ProductionStorageDetailItem item7 = new ProductionStorageDetailItem();
        item7.setHeader(getResources().getString(R.string.production_storage_dept));
        item7.setContent(productList.get(index).getDept_name());
        ProductionDetailList.add(item7);
        //part_no
        ProductionStorageDetailItem item8 = new ProductionStorageDetailItem();
        item8.setHeader(getResources().getString(R.string.production_storage_part_no));
        item8.setContent(productList.get(index).getPart_no());
        ProductionDetailList.add(item8);
        //part_desc
        ProductionStorageDetailItem item9 = new ProductionStorageDetailItem();
        item9.setHeader(getResources().getString(R.string.production_storage_part_desc));
        item9.setContent(productList.get(index).getPart_desc());
        ProductionDetailList.add(item9);
        //stock_no
        ProductionStorageDetailItem item10 = new ProductionStorageDetailItem();
        item10.setHeader(getResources().getString(R.string.production_storage_stock_no));
        item10.setContent(productList.get(index).getStock_no());
        ProductionDetailList.add(item10);
        //locate_no
        ProductionStorageDetailItem item11 = new ProductionStorageDetailItem();
        item11.setHeader(getResources().getString(R.string.production_storage_locate_no));
        item11.setContent(productList.get(index).getLocate_no());
        ProductionDetailList.add(item11);
        //locate_no_scan
        ProductionStorageDetailItem item12 = new ProductionStorageDetailItem();
        item12.setHeader(getResources().getString(R.string.production_storage_locate_no_scan));
        item12.setContent(productList.get(index).getLocate_no_scan());
        ProductionDetailList.add(item12);
        //batch_no
        ProductionStorageDetailItem item13 = new ProductionStorageDetailItem();
        item13.setHeader(getResources().getString(R.string.production_storage_batch_no));
        item13.setContent(productList.get(index).getBatch_no());
        ProductionDetailList.add(item13);
        //qty
        ProductionStorageDetailItem item14 = new ProductionStorageDetailItem();
        item14.setHeader(getResources().getString(R.string.production_storage_qty));
        item14.setContent(productList.get(index).getQty());
        ProductionDetailList.add(item14);
        //stock_unit
        ProductionStorageDetailItem item15 = new ProductionStorageDetailItem();
        item15.setHeader(getResources().getString(R.string.production_storage_unit));
        item15.setContent(productList.get(index).getStock_unit());
        ProductionDetailList.add(item15);
        //count_no
        ProductionStorageDetailItem item16 = new ProductionStorageDetailItem();
        item16.setHeader(getResources().getString(R.string.production_storage_count_no));
        item16.setContent(productList.get(index).getCount_no());
        ProductionDetailList.add(item16);
        //stock_no_name
        ProductionStorageDetailItem item17 = new ProductionStorageDetailItem();
        item17.setHeader(getResources().getString(R.string.production_storage_stock_no_name));
        item17.setContent(productList.get(index).getStock_no_name());
        ProductionDetailList.add(item17);


        productionStorageDetailItemAdapter = new ProductionStorageDetailItemAdapter(ProductionStorageDetailActivity.this, R.layout.production_storage_list_detail_item, ProductionDetailList);
        listView.setAdapter(productionStorageDetailItemAdapter);

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

                                if (counter >= 1) {

                                    toast(getResources().getString(R.string.not_locate_no_msg));


                                } else {
                                    text = text.replaceAll("\\n","");
                                    toast(text);

                                    //if (text.length() == 6) {

                                        if (item_select != -1) { //scan locate

                                            if (text.length() != 16) {
                                                toast(text);
                                                if (dataTable_RR != null && dataTable_RR.Rows.size() > 0) {
                                                    if (dataTable_RR.Rows.get(item_select) != null)
                                                        dataTable_RR.Rows.get(item_select).setValue("locate_no", text);
                                                }

                                                productList.get(item_select).setLocate_no_scan(text);

                                                ProductionDetailList.get(11).setContent(text);

                                                if (productionStorageDetailItemAdapter != null)
                                                    productionStorageDetailItemAdapter.notifyDataSetChanged();
                                                //productListView.invalidateViews();
                                            /*if (productionStorageItemAdapter != null) {
                                                productionStorageItemAdapter.notifyDataSetChanged();
                                            }

                                            productListView.invalidateViews();*/
                                            }


                                        }
                                    //}


                                }

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

        getMenuInflater().inflate(R.menu.production_storage_detail_menu, menu);








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

                        Intent clearIntent = new Intent(Constants.ACTION.ACTION_PRODUCT_DELETE_ITEM_CONFIRM);
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

    public void toast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }
}
