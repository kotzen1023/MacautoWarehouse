package com.macauto.macautowarehouse;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.macauto.macautowarehouse.data.Constants;
import com.macauto.macautowarehouse.data.DetailItem;
import com.macauto.macautowarehouse.data.DividedItem;
import com.macauto.macautowarehouse.data.DividedItemAdapter;
import com.macauto.macautowarehouse.table.DataRow;

import java.util.ArrayList;

import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.check_stock_in;
import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.dataTable;
import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.detailList;
import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.no_list;
import static com.macauto.macautowarehouse.data.InspectedReceiveExpanedAdater.mSparseBooleanArray;

public class EnteringWarehouseDividedDialogActivity extends AppCompatActivity {

    private static final String TAG = "DividedDialog";

    public static DividedItemAdapter dividedItemAdapter;
    public static ArrayList<DividedItem> dividedList = new ArrayList<>();
    private static Button btnAdd;
    private static Button btnCancel;
    private static Button btnOk;
    private static TextView textViewStatus;
    private static TextView textViewQuantity;
    private ListView listViewDivide;
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

        final String group_index = intent.getStringExtra("GROUP_INDEX");
        final String child_index = intent.getStringExtra("CHILD_INDEX");
        final String quantity_string = intent.getStringExtra("QUANTITY");

        float quantity = Float.valueOf(quantity_string);


        quantity_int = (int)quantity;

        btnAdd = findViewById(R.id.btnAdd);
        //textViewStatus = findViewById(R.id.textViewStatus);
        textViewQuantity = findViewById(R.id.textViewQuantity);
        btnCancel = findViewById(R.id.btnCancel);
        btnOk = findViewById(R.id.btnOk);
        listViewDivide = findViewById(R.id.listViewDivide);

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

                //DetailItem item = inspectedReceiveExpanedAdater.getChild(Integer.valueOf(group_index), Integer.valueOf(child_index));
                int base_index = Integer.valueOf(group_index);

                String base_head = no_list.get(base_index);
                //ArrayList<DetailItem> base_list = new ArrayList<>(detailList.get(no_list.get(base_index)));



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
                        if (k==0) {
                            copy_dataRow.setValue("check_sp", dataTable.Rows.get(base_index).getValue(k));
                        } else if (k==1) {
                            copy_dataRow.setValue("rvu01", dataTable.Rows.get(base_index).getValue(k));
                        } else if (k==2) {
                            copy_dataRow.setValue("rvu02", dataTable.Rows.get(base_index).getValue(k));
                        } else if (k==3) {
                            copy_dataRow.setValue("rvb05", dataTable.Rows.get(base_index).getValue(k));
                        } else if (k==4) {
                            copy_dataRow.setValue("pmn041", dataTable.Rows.get(base_index).getValue(k));
                        } else if (k==5) {
                            copy_dataRow.setValue("ima021", dataTable.Rows.get(base_index).getValue(k));
                        } else if (k==6) {
                            copy_dataRow.setValue("rvv32", dataTable.Rows.get(base_index).getValue(k));
                        } else if (k==7) {
                            copy_dataRow.setValue("rvv33", dataTable.Rows.get(base_index).getValue(k));
                        } else if (k==8) {
                            copy_dataRow.setValue("rvv34", dataTable.Rows.get(base_index).getValue(k));
                        } else if (k==9) {
                            copy_dataRow.setValue("rvb33", dataTable.Rows.get(base_index).getValue(k));
                        } else if (k==10) {
                            copy_dataRow.setValue("pmc03", dataTable.Rows.get(base_index).getValue(k));
                        } else if (k==11) {
                            copy_dataRow.setValue("gen02", dataTable.Rows.get(base_index).getValue(k));
                        }
                    }
                    //DataRow copy_dataRow = dataTable.Rows.get(base_index);
                    //fix quantity
                    copy_list.get(9).setName(String.valueOf(temp_count_list.get(i)));
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
                //no_list.remove(base_index);
                //detailList.get(no_list.get(base_index)).get(9).setName(String.valueOf(temp_count_list.get(0)));

                Intent addIntent = new Intent(Constants.ACTION.ACTION_ENTERING_WAREHOUSE_DIVIDED_DIALOG_ADD);
                sendBroadcast(addIntent);

                finish();
                /*int count = 0;
                for (int i=0; i<dividedList.size(); i++) {
                    if (dividedList.get(i).getEdit().getText().length() > 0)
                    {
                        count += Integer.valueOf(dividedList.get(i).getEdit().getText().toString());

                    }
                }

                String total = count+" / "+quantity_string;
                textViewQuantity.setText(total);*/


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

        dividedItemAdapter = new DividedItemAdapter(EnteringWarehouseDividedDialogActivity.this, R.layout.entering_warehouse_divide_dialog_item, dividedList, quantity_int);
        listViewDivide.setAdapter(dividedItemAdapter);




        IntentFilter filter;

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {



                if (intent.getAction() != null) {

                    if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ENTERING_WAREHOUSE_DIVIDED_DIALOG_TEXT_CHANGE)) {
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

                    }

                }
            }
        };

        if (!isRegister) {
            filter = new IntentFilter();
            filter.addAction(Constants.ACTION.ACTION_ENTERING_WAREHOUSE_DIVIDED_DIALOG_TEXT_CHANGE);

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
}
