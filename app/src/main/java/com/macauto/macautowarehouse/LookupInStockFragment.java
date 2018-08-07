package com.macauto.macautowarehouse;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.macauto.macautowarehouse.data.AllocationMsgAdapter;
import com.macauto.macautowarehouse.data.Constants;
import com.macauto.macautowarehouse.data.DetailItem;
import com.macauto.macautowarehouse.data.GenerateRandomString;
import com.macauto.macautowarehouse.data.SearchDetailItem;
import com.macauto.macautowarehouse.data.SearchItem;
import com.macauto.macautowarehouse.data.SearchItemAdapter;
import com.macauto.macautowarehouse.service.CheckEmpExistService;
import com.macauto.macautowarehouse.service.CheckEmpPasswordService;
import com.macauto.macautowarehouse.service.GetMyMessDetailService;
import com.macauto.macautowarehouse.service.GetPartWarehouseListService;
import com.macauto.macautowarehouse.service.SearchPartNoByScanService;
import com.macauto.macautowarehouse.table.DataTable;
import com.macauto.macautowarehouse.table.LookupInStockDetailActivity;


import java.util.ArrayList;
import java.util.List;

import static com.macauto.macautowarehouse.MainActivity.emp_no;
import static com.macauto.macautowarehouse.MainActivity.k_id;
import static com.macauto.macautowarehouse.MainActivity.searchList;
import static com.macauto.macautowarehouse.MainActivity.sortedSearchList;

public class LookupInStockFragment extends Fragment {
    private static final String TAG = LookupInStockFragment.class.getName();

    private Context fragmentContext;

    private static BroadcastReceiver mReceiver = null;
    private static boolean isRegister = false;

    private LinearLayout layoutSearchView;
    private LinearLayout layoutResultView;
    private EditText partNo;
    private EditText batchNo;
    private EditText name;
    private EditText spec;
    private RecyclerView recyclerViewResult;
    public SearchItemAdapter searchItemAdapter;

    ProgressDialog loadDialog = null;

    public static DataTable lookUpDataTable;


    InputMethodManager imm;
    public static boolean isSorted = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentContext = getContext();

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");



        final  View view = inflater.inflate(R.layout.look_up_in_stock_fragment, container, false);

        imm = (InputMethodManager)fragmentContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        Button btnSearch = view.findViewById(R.id.btnSearch);
        Button btnResearch = view.findViewById(R.id.btnResearch);

        layoutSearchView = view.findViewById(R.id.layoutSearchView);
        layoutResultView = view.findViewById(R.id.layoutResultView);

        partNo = view.findViewById(R.id.serachPartNo);
        batchNo =view.findViewById(R.id.searchBatchNo);
        name = view.findViewById(R.id.searchName);
        spec = view.findViewById(R.id.searchSpec);

        recyclerViewResult = view.findViewById(R.id.recyclerViewSearch);
        recyclerViewResult.setLayoutManager(new LinearLayoutManager(fragmentContext));
        recyclerViewResult.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        if (searchItemAdapter != null) {
            searchItemAdapter.setOnItemClickListener(new SearchItemAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                    Log.e(TAG, "onItemClick "+position);

                    Intent detailIntent = new Intent(fragmentContext, LookupInStockDetailActivity.class);
                    detailIntent.putExtra("INDEX", String.valueOf(position));
                    fragmentContext.startActivity(detailIntent);

                }
            });
        }

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchList.clear();

                if (searchItemAdapter != null)
                    searchItemAdapter.notifyDataSetChanged();

                Intent getintent = new Intent(fragmentContext, GetPartWarehouseListService.class);
                getintent.setAction(Constants.ACTION.ACTION_SEARCH_PART_WAREHOUSE_LIST_ACTION);
                getintent.putExtra("PART_NO", partNo.getText().toString().toUpperCase());
                getintent.putExtra("BATCH_NO", batchNo.getText().toString().toUpperCase());
                getintent.putExtra("NAME", name.getText().toString());
                getintent.putExtra("SPEC", spec.getText().toString());
                fragmentContext.startService(getintent);



                loadDialog = new ProgressDialog(fragmentContext);
                loadDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                loadDialog.setTitle(getResources().getString(R.string.Processing));
                loadDialog.setIndeterminate(false);
                loadDialog.setCancelable(false);
                loadDialog.show();
            }
        });

        btnResearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent resetIntent = new Intent(Constants.ACTION.ACTION_RESET_TITLE_PART_IN_STOCK);
                fragmentContext.sendBroadcast(resetIntent);

                layoutSearchView.setVisibility(View.VISIBLE);
                layoutResultView.setVisibility(View.GONE);
            }
        });

        IntentFilter filter;

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //Log.e(TAG, "intent.getAction() =>>>> "+intent.getAction().toString());

                if (intent.getAction() != null) {

                    if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SEARCH_PART_BATCH_CLEAN)) {
                        Log.d(TAG, "receive ACTION_SEARCH_PART_BATCH_CLEAN");

                        /*if (searchItemAdapter != null)
                            searchItemAdapter.notifyDataSetChanged();


                        Intent getintent = new Intent(fragmentContext, GetPartWarehouseListService.class);
                        getintent.setAction(Constants.ACTION.ACTION_SEARCH_PART_WAREHOUSE_LIST_ACTION);
                        getintent.putExtra("PART_NO", partNo.getText().toString().toUpperCase());
                        getintent.putExtra("BATCH_NO", batchNo.getText().toString().toUpperCase());
                        getintent.putExtra("NAME", name.getText().toString());
                        getintent.putExtra("SPEC", spec.getText().toString());
                        fragmentContext.startService(getintent);*/
                        //loadDialog.dismiss();
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SEARCH_PART_BATCH_FAILED)) {
                        Log.d(TAG, "receive ACTION_SEARCH_PART_BATCH_FAILED");
                        loadDialog.dismiss();

                        toast(fragmentContext.getResources().getString(R.string.look_up_in_stock_no_match_batch));
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SEARCH_PART_BATCH_SUCCESS)) {
                        Log.d(TAG, "receive ACTION_SEARCH_PART_BATCH_SUCCESS");
                        loadDialog.dismiss();

                        String batch_no = intent.getStringExtra("BATCH_NO");
                        Log.e(TAG, "batch_no = "+batch_no);




                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SEARCH_PART_WAREHOUSE_LIST_CLEAN)) {
                        Log.d(TAG, "receive ACTION_SEARCH_PART_WAREHOUSE_LIST_CLEAN");
                        loadDialog.dismiss();
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SEARCH_PART_WAREHOUSE_LIST_FAILED)) {
                        Log.d(TAG, "receive ACTION_SEARCH_PART_WAREHOUSE_LIST_FAILED");
                        loadDialog.dismiss();

                        toast(fragmentContext.getResources().getString(R.string.look_up_in_stock_no_match_batch));
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SEARCH_PART_WAREHOUSE_LIST_SUCCESS)) {
                        Log.d(TAG, "receive ACTION_SEARCH_PART_WAREHOUSE_LIST_SUCCESS");

                        String records = intent.getStringExtra("RECORDS");

                        loadDialog.dismiss();

                        Log.e(TAG, "searchList.size = " + searchList.size());

                        layoutResultView.setVisibility(View.VISIBLE);
                        layoutSearchView.setVisibility(View.GONE);

                        if (searchItemAdapter != null) {
                            searchItemAdapter.notifyDataSetChanged();
                        } else {
                            searchItemAdapter = new SearchItemAdapter(fragmentContext, searchList);
                            recyclerViewResult.setAdapter(searchItemAdapter);
                        }

                        toast(getResources().getString(R.string.look_up_in_stock_find_records, records));

                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                        Intent showIntent = new Intent(Constants.ACTION.ACTION_SEARCH_MENU_SHOW_ACTION);
                        fragmentContext.sendBroadcast(showIntent);

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SEARCH_PART_WAREHOUSE_LIST_EMPTY)) {
                        Log.d(TAG, "receive ACTION_SEARCH_PART_WAREHOUSE_LIST_EMPTY");
                        loadDialog.dismiss();

                        if (searchItemAdapter != null)
                            searchItemAdapter.notifyDataSetChanged();

                        Intent showIntent = new Intent(Constants.ACTION.ACTION_SEARCH_MENU_HIDE_ACTION);
                        fragmentContext.sendBroadcast(showIntent);

                        toast(fragmentContext.getResources().getString(R.string.look_up_in_stock_find_records, "0"));

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SEARCH_PART_WAREHOUSE_SORT_COMPLETE)) {
                        Log.d(TAG, "receive ACTION_SEARCH_PART_WAREHOUSE_SORT_COMPLETE");

                        searchItemAdapter = null;

                        isSorted = true;

                        searchItemAdapter = new SearchItemAdapter(fragmentContext, sortedSearchList);
                        recyclerViewResult.setAdapter(searchItemAdapter);
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SEARCH_PART_WAREHOUSE_GET_ORIGINAL_LIST)) {
                        Log.d(TAG, "receive ACTION_SEARCH_PART_WAREHOUSE_GET_ORIGINAL_LIST");

                        searchItemAdapter = null;

                        isSorted = false;

                        searchItemAdapter = new SearchItemAdapter(fragmentContext, searchList);
                        recyclerViewResult.setAdapter(searchItemAdapter);
                    }

                    else if("unitech.scanservice.data" .equals(intent.getAction())) {
                        Log.d(TAG, "unitech.scanservice.data");
                        Bundle bundle = intent.getExtras();
                        if(bundle != null )
                        {
                            String text = bundle.getString("text");
                            Log.e(TAG, "msg = "+text);

                            if (text.length() > 0 ) {




                                int counter = 0;
                                for( int i=0; i<text.length(); i++ ) {
                                    if( text.charAt(i) == '#' ) {
                                        counter++;
                                    }
                                }

                                Log.e(TAG, "counter = "+counter);

                                if (counter >= 2) {



                                    String codeArray[] = text.toString().split("#");
                                    partNo.setText(codeArray[0]);
                                    //batchNo.setText(codeArray[1]);

                                    /*Intent getPartIntent = new Intent(fragmentContext, SearchPartNoByScanService.class);
                                    getPartIntent.setAction(Constants.ACTION.ACTION_SEARCH_PART_BATCH_ACTION);
                                    getPartIntent.putExtra("PART_NO", codeArray[0]);
                                    getPartIntent.putExtra("BARCODE", text);
                                    fragmentContext.startService(getPartIntent);*/
                                }

                                /*if (counter == 8) {

                                    //regenerate new session id
                                    GenerateRandomString rString = new GenerateRandomString();
                                    k_id = rString.randomString(32);
                                    Log.e(TAG, "session_id = "+k_id);



                                    String codeArray[] = text.toString().split("#");
                                    Intent scanResultIntent = new Intent(Constants.ACTION.ACTION_SET_INSPECTED_RECEIVE_ITEM_CLEAN);
                                    for (int i = 0; i < codeArray.length; i++) {
                                        Log.e(TAG, "codeArray[" + i + "] = " + codeArray[i]);
                                        String column = "COLUMN_" + String.valueOf(i);
                                        scanResultIntent.putExtra(column, codeArray[i]);
                                    }


                                    scanResultIntent.putExtra("BARCODE", text.toString());
                                    scanResultIntent.putExtra("K_ID", k_id);
                                    fragmentContext.sendBroadcast(scanResultIntent);
                                } else {
                                    toast(text);
                                    if (no_list.size() > 0 && detailList.size() > 0) {

                                        if (current_expanded_group > -1) {


                                            String head = no_list.get(current_expanded_group);
                                            DetailItem detailItem = detailList.get(head).get(7);
                                            detailItem.setName(text);

                                            Log.e(TAG, "current_expanded_group = "+current_expanded_group+", head = "+head);

                                            if (dataTable != null) {
                                                dataTable.Rows.get(current_expanded_group).setValue("rvv33", text);
                                            }



                                            Intent getFailedIntent = new Intent(Constants.ACTION.ACTION_MODIFIED_ITEM_COMPLETE);
                                            context.sendBroadcast(getFailedIntent);
                                        }
                                    }
                                }*/

                            }
                        }
                    }

                }
            }
        };

        if (!isRegister) {
            filter = new IntentFilter();
            filter.addAction(Constants.ACTION.ACTION_SEARCH_PART_BATCH_CLEAN);
            filter.addAction(Constants.ACTION.ACTION_SEARCH_PART_BATCH_FAILED);
            filter.addAction(Constants.ACTION.ACTION_SEARCH_PART_BATCH_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_SEARCH_PART_WAREHOUSE_LIST_CLEAN);
            filter.addAction(Constants.ACTION.ACTION_SEARCH_PART_WAREHOUSE_LIST_FAILED);
            filter.addAction(Constants.ACTION.ACTION_SEARCH_PART_WAREHOUSE_LIST_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_SEARCH_PART_WAREHOUSE_LIST_EMPTY);
            filter.addAction(Constants.ACTION.ACTION_SEARCH_PART_WAREHOUSE_SORT_COMPLETE);
            filter.addAction(Constants.ACTION.ACTION_SEARCH_PART_WAREHOUSE_GET_ORIGINAL_LIST);
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
        Toast toast = Toast.makeText(fragmentContext, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }




}
