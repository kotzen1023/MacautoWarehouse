package com.macauto.macautowarehouse;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.macauto.macautowarehouse.data.Constants;
import com.macauto.macautowarehouse.data.DetailItem;
import com.macauto.macautowarehouse.data.DividedItem;
import com.macauto.macautowarehouse.data.DividedItemAdapter;
import com.macauto.macautowarehouse.service.ConfirmEnteringWarehouseService;
import com.macauto.macautowarehouse.service.DeleteTTReceiveGoodsInTempService2;
import com.macauto.macautowarehouse.service.GetReceiveGoodsInDataAXService;
import com.macauto.macautowarehouse.table.DataRow;

import java.util.ArrayList;

import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.check_stock_in;
import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.dataTable;
import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.dataTable_Batch_area;
//import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.detailList;
//import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.no_list;
import static com.macauto.macautowarehouse.data.InspectedReceiveExpanedAdater.mSparseBooleanArray;

public class EnteringWarehouseDividedDialogActivity extends AppCompatActivity {

    private static final String TAG = "DividedDialog";

    public static DividedItemAdapter dividedItemAdapter;
    public static ArrayList<DividedItem> dividedList = new ArrayList<>();
    //private static Button btnAdd;
    //private static Button btnCancel;
    private Button btnOk;
    //private static TextView textViewStatus;
    private TextView textViewQuantity;
   // private ListView listViewDivide;
    private static int quantity_int;

    InputMethodManager imm;

    //private static MenuItem hide;

    private static BroadcastReceiver mReceiver = null;
    private static boolean isRegister = false;
    public static ArrayList<Integer> temp_count_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entering_warehouse_divided_dialog_activity);

        Intent intent = getIntent();

        setTitle(getResources().getString(R.string.entering_warehouse_dialog_head));

        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        //final String group_index = intent.getStringExtra("GROUP_INDEX");
        //final String child_index = intent.getStringExtra("CHILD_INDEX");
        final String in_no_string = intent.getStringExtra("IN_NO");
        final String item_no_string = intent.getStringExtra("ITEM_NO");
        final String part_no_string = intent.getStringExtra("PART_NO");
        final String quantity_string = intent.getStringExtra("QUANTITY");
        final String batch_no_string = intent.getStringExtra("BATCH_NO");
        final String locate_no_string = intent.getStringExtra("LOCATE_NO");
        final String stock_no_string = intent.getStringExtra("STOCK_NO");
        final String check_sp_string = intent.getStringExtra("CHECK_SP");

        float quantity = Float.valueOf(quantity_string);


        quantity_int = (int)quantity;

        Button btnAdd = findViewById(R.id.btnAdd);
        //textViewStatus = findViewById(R.id.textViewStatus);
        textViewQuantity = findViewById(R.id.textViewQuantity);
        Button btnCancel = findViewById(R.id.btnCancel);
        btnOk = findViewById(R.id.btnOk);
        ListView listViewDivide = findViewById(R.id.listViewDivide);

        //textViewQuantity.setText(getResources().getString(R.string.entering_warehouse_dialog_total, quantity_string));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DividedItem addItem = new DividedItem();
                addItem.setQuantity(0);
                dividedList.add(addItem);
                temp_count_list.add(0);

                /*Log.e(TAG, "== current temp count list ==");
                for (int i=0; i<temp_count_list.size(); i++) {
                    Log.e(TAG, "index["+i+"] = "+temp_count_list.get(i));
                }*/

                for (int i=0; i<dividedList.size();i++) {
                    dividedList.get(i).setQuantity(temp_count_list.get(i));
                }

                if (dividedItemAdapter != null)
                    dividedItemAdapter.notifyDataSetChanged();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //reset dataTable_Batch_area

                for (int i=0; i<dividedList.size(); i++) {
                    if (i==0) {
                        dataTable_Batch_area.Rows.get(0).setValue("rvb33", String.valueOf(dividedList.get(i).getQuantity()));
                    } else { //i > 0
                        DataRow dr = dataTable_Batch_area.NewRow();
                        dr.setValue("rvv33", dataTable_Batch_area.getValue(0, "rvv33"));
                        dr.setValue("rvb33", String.valueOf(dividedList.get(i).getQuantity()));
                        dr.setValue("rvv32", dataTable_Batch_area.getValue(0, "rvv32"));
                        dr.setValue("rvv34", dataTable_Batch_area.getValue(0, "rvv34"));
                        dataTable_Batch_area.Rows.add(dr);
                    }
                }

                Intent splitIntent = new Intent(EnteringWarehouseDividedDialogActivity.this, ConfirmEnteringWarehouseService.class);
                splitIntent.setAction(Constants.ACTION.ACTION_GET_TT_SPLIT_RVV_ITEM_ACTION);
                splitIntent.putExtra("IN_NO", in_no_string);
                splitIntent.putExtra("ITEM_NO", item_no_string);
                splitIntent.putExtra("PART_NO", part_no_string);
                startService(splitIntent);


                /*int base_index = Integer.valueOf(group_index);

                String base_head = no_list.get(base_index);

                //the original must be delete
                for (int i=0; i<dividedList.size(); i++) {
                    String head = base_head+"_"+i;
                    no_list.add(base_index+(i+1), head);
                    check_stock_in.add(base_index+(i+1), false);
                    ArrayList<DetailItem> copy_list = new ArrayList<>();
                    for (int j=0 ; j< detailList.get(no_list.get(base_index)).size(); j++) {
                        DetailItem item = new DetailItem();
                        item.setTitle(detailList.get(no_list.get(base_index)).get(j).getTitle());
                        item.setName(detailList.get(no_list.get(base_index)).get(j).getName());
                        copy_list.add(item);
                    }

                    DataRow copy_dataRow = dataTable.NewRow();

                    for (int k=0; k< dataTable.Columns.size(); k++) {

                        copy_dataRow.setValue(dataTable.Columns.get(k).ColumnName, dataTable.Rows.get(base_index).getValue(k));


                    }
                    //DataRow copy_dataRow = dataTable.Rows.get(base_index);
                    //fix quantity
                    copy_list.get(10).setName(String.valueOf(temp_count_list.get(i)));
                    detailList.put(head, copy_list);
                    Log.e(TAG, "temp_count_list["+i+"] = "+temp_count_list.get(i));


                    copy_dataRow.setValue(9, String.valueOf(temp_count_list.get(i)));
                    dataTable.Rows.add(base_index+(i+1), copy_dataRow);
                }
                //remove original
                no_list.remove(base_index);
                check_stock_in.remove(base_index);
                detailList.remove(base_head);
                dataTable.Rows.remove(base_index);

                Log.e(TAG, "=== list start === ");
                for (int i=0; i< no_list.size(); i++) {
                    Log.d(TAG, "no_list["+i+"] = "+no_list.get(i));

                    for (int j=0; j< detailList.get(no_list.get(i)).size(); j++) {
                        Log.d(TAG, "detailList["+j+"] = "+detailList.get(no_list.get(i)).get(j).getName());
                    }
                }
                Log.e(TAG, "=== list end ===");

                //reset checkbox
                for (int j=0; j<check_stock_in.size(); j++) {
                    check_stock_in.set(j, false);
                    mSparseBooleanArray.put(j, false);
                }


                Intent addIntent = new Intent(Constants.ACTION.ACTION_ENTERING_WAREHOUSE_DIVIDED_DIALOG_ADD);
                sendBroadcast(addIntent);

                finish();*/



            }
        });

        dividedList.clear();
        temp_count_list.clear();

        //DetailItem item = inspectedReceiveExpanedAdater.getChild(Integer.valueOf(group_index), Integer.valueOf(child_index));


        DividedItem dividedItem = new DividedItem();
        dividedItem.setQuantity(quantity_int);
        dividedList.add(dividedItem);
        temp_count_list.add(quantity_int);

        String total = quantity_string+" / "+quantity_string;
        textViewQuantity.setText(total);

        dividedItemAdapter = new DividedItemAdapter(EnteringWarehouseDividedDialogActivity.this, R.layout.entering_warehouse_divide_dialog_item, dividedList);
        listViewDivide.setAdapter(dividedItemAdapter);




        IntentFilter filter;

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {



                if (intent.getAction() != null) {


                    if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SOCKET_TIMEOUT)) {
                        Log.d(TAG, "receive ACTION_SOCKET_TIMEOUT");
                        //if (loadDialog != null)
                        //    loadDialog.dismiss();

                        toast(getResources().getString(R.string.socket_timeout));
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ENTERING_WAREHOUSE_DIVIDED_DIALOG_TEXT_CHANGE)) {
                        //Log.d(TAG, "receive ACTION_ENTERING_WAREHOUSE_DIVIDED_DIALOG_TEXT_CHANGE");

                        int count = 0;

                        for (int i=0;i<temp_count_list.size(); i++) {
                            dividedList.get(i).setQuantity(temp_count_list.get(i));

                            count+= temp_count_list.get(i);
                        }
                        //Log.e(TAG, "current total = "+count);

                        if (count == quantity_int) {
                            String total = String.valueOf(count)+" / "+String.valueOf(quantity_int);
                            textViewQuantity.setText(total);

                            //check temp_count_list, if there is more than one > 0, set btnOk enabled, else set disabled
                            int bigger_than_zero = 0;

                            for (int j=0; j<temp_count_list.size(); j++) {
                                if (temp_count_list.get(j) > 0)
                                    bigger_than_zero++;
                            }

                            if (bigger_than_zero > 1 && temp_count_list.get(0) > 0 && bigger_than_zero == temp_count_list.size())
                                btnOk.setEnabled(true);
                            else
                                btnOk.setEnabled(false);

                        } else {
                            btnOk.setEnabled(false);

                            String text = "<font color=#ce0000>"+String.valueOf(count)+"</font> / <font color=#000000>"+String.valueOf(quantity_int)+"</font>";
                            textViewQuantity.setText(Html.fromHtml(text));


                        }


                        //int index = Integer.valueOf(intent.getStringExtra("INDEX"));
                        //String change_text = intent.getStringExtra("CHANGED_TEXT");
                        //if (change_text.length() > 0) {
                        //    temp_count_list.set() = Integer.valueOf(change_text);
                        //}

                        //if (dividedItemAdapter != null) {
                        //    dividedItemAdapter.notifyDataSetChanged();
                        //}

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_GET_TT_SPLIT_RVV_ITEM_SUCCESS)) {
                        Log.d(TAG, "receive ACTION_GET_TT_SPLIT_RVV_ITEM_SUCCESS");

                        Intent splitIntent = new Intent(EnteringWarehouseDividedDialogActivity.this, DeleteTTReceiveGoodsInTempService2.class);
                        splitIntent.setAction(Constants.ACTION.ACTION_GET_TT_SPLIT_RVV_ITEM_ACTION);
                        splitIntent.putExtra("IN_NO", in_no_string);
                        splitIntent.putExtra("ITEM_NO", item_no_string);
                        startService(splitIntent);


                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_GET_TT_SPLIT_RVV_ITEM_FAILED)) {
                        Log.d(TAG, "receive ACTION_GET_TT_SPLIT_RVV_ITEM_FAILED");
                        toast(getResources().getString(R.string.entering_warehouse_tt_split_rvv_failed));
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_DELETE_TT_RECEIVE_GOODS_IN_TEMP2_SUCCESS)) {
                        Log.d(TAG, "receive ACTION_DELETE_TT_RECEIVE_GOODS_IN_TEMP2_SUCCESS");

                        //clear swipe_list only
                        Intent clearIntent = new Intent(Constants.ACTION.ACTION_SET_INSPECTED_RECEIVE_ITEM_CLEAN_ONLY);
                        sendBroadcast(clearIntent);

                        //reset item string
                        String xx = "";
                        int start_index = Integer.valueOf(item_no_string);
                        for (int i=0; i<dataTable_Batch_area.Rows.size(); i++) {
                            xx = xx + "rvv02="+start_index;

                            if (i != dataTable_Batch_area.Rows.size() -1) {
                                xx = xx + " or ";
                            }
                        }

                        xx = " ( "+xx+" ) ";
                        Log.e(TAG, "xx = "+xx);

                        Intent splitIntent = new Intent(EnteringWarehouseDividedDialogActivity.this, GetReceiveGoodsInDataAXService.class);
                        splitIntent.setAction(Constants.ACTION.ACTION_GET_TT_SPLIT_RVV_ITEM_ACTION);
                        splitIntent.putExtra("IN_NO", in_no_string);
                        splitIntent.putExtra("ITEM_NO_LIST", xx);
                        splitIntent.putExtra("CHECK_SP", check_sp_string);
                        splitIntent.putExtra("ITEM_NO", item_no_string);
                        startService(splitIntent);

                        finish();


                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_DELETE_TT_RECEIVE_GOODS_IN_TEMP2_FAILED)) {
                        Log.d(TAG, "receive ACTION_DELETE_TT_RECEIVE_GOODS_IN_TEMP2_FAILED");
                    }

                }
            }
        };

        if (!isRegister) {
            filter = new IntentFilter();
            filter.addAction(Constants.ACTION.ACTION_SOCKET_TIMEOUT);
            filter.addAction(Constants.ACTION.ACTION_ENTERING_WAREHOUSE_DIVIDED_DIALOG_TEXT_CHANGE);

            filter.addAction(Constants.ACTION.ACTION_GET_TT_SPLIT_RVV_ITEM_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_GET_TT_SPLIT_RVV_ITEM_FAILED);

            filter.addAction(Constants.ACTION.ACTION_DELETE_TT_RECEIVE_GOODS_IN_TEMP2_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_DELETE_TT_RECEIVE_GOODS_IN_TEMP2_FAILED);

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

        getMenuInflater().inflate(R.menu.divided_activity_menu, menu);








        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.hide_or_show_keyboard:
                View view = getCurrentFocus();
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                break;

            default:
                break;
        }

        return true;
    }

    public void toast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }
}
