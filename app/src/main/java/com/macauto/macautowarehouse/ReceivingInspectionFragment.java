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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.macauto.macautowarehouse.data.Constants;
import com.macauto.macautowarehouse.data.GenerateRandomString;
import com.macauto.macautowarehouse.data.ProductionStorageItemAdapter;
import com.macauto.macautowarehouse.data.ReceivingInspectionItem;
import com.macauto.macautowarehouse.data.ReceivingInspectionItemAdapter;
import com.macauto.macautowarehouse.service.CheckStockLocateNoExistService;
import com.macauto.macautowarehouse.service.CheckTTProductEntryAlreadyConfirm;
import com.macauto.macautowarehouse.service.CheckTTRecNoAlreadyInQCItemService;
import com.macauto.macautowarehouse.service.ConfirmEnteringWarehouseService;
import com.macauto.macautowarehouse.service.DeleteTTReceiveGoodsInTempService;
import com.macauto.macautowarehouse.service.ExecuteScriptTTService;
import com.macauto.macautowarehouse.service.ExecuteTTPrgAService;
import com.macauto.macautowarehouse.service.ExecuteTTcqcp001SetService;
import com.macauto.macautowarehouse.service.GetTTProductEntryService;
import com.macauto.macautowarehouse.service.GetTTReceiveGoodsReportDataQCService;
import com.macauto.macautowarehouse.service.UpdateTTProductEntryLocateNoService;
import com.macauto.macautowarehouse.table.DataColumn;
import com.macauto.macautowarehouse.table.DataRow;
import com.macauto.macautowarehouse.table.DataTable;

import java.util.ArrayList;

import static com.macauto.macautowarehouse.MainActivity.emp_no;
import static com.macauto.macautowarehouse.MainActivity.k_id;
import static com.macauto.macautowarehouse.MainActivity.web_soap_port;

public class ReceivingInspectionFragment extends Fragment {
    private static final String TAG = ReceivingInspectionFragment.class.getName();

    private Context fragmentContext;

    private static BroadcastReceiver mReceiver = null;
    private static boolean isRegister = false;

    private EditText editTextReceivingInspection;
    //private Button btnReceivingInspection;
    private ListView listView;
    private Button btnQCGenerate;

    public static DataTable dataTable_TTCP;
    public static DataTable dataTable_YIC;
    public static DataTable dataTable_PG_M;

    ProgressDialog loadDialog = null;

    private ReceivingInspectionItemAdapter receivingInspectionItemAdapter;
    public static ArrayList<ReceivingInspectionItem> receivingList = new ArrayList<>();

    private static int current_row = -1;
    private String warning_msg = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentContext = getContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");



        final  View view = inflater.inflate(R.layout.receiving_inspection_fragment, container, false);

        editTextReceivingInspection = view.findViewById(R.id.editTextReceivingInspection);
        //btnReceivingInspection = view.findViewById(R.id.btnReceivingInspection);
        btnQCGenerate = view.findViewById(R.id.btnQCGenerate);
        listView = view.findViewById(R.id.receivingInspectionListView);

        btnQCGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (receivingInspectionItemAdapter != null && receivingInspectionItemAdapter.mSparseBooleanArray.size() > 0) {
                    boolean found = false;

                    for (int i=0; i<receivingInspectionItemAdapter.mSparseBooleanArray.size(); i++) {
                        if (receivingInspectionItemAdapter.mSparseBooleanArray.get(i)) {
                            found = true;
                        }
                    }

                    if (!found) {
                        toast(getResources().getString(R.string.receiving_inspection_generate_not_select));
                    } else {

                        if (dataTable_TTCP != null && dataTable_TTCP.Rows.size() > 0) {

                            if (dataTable_YIC != null) {
                                dataTable_YIC.clear();
                            } else {
                                dataTable_YIC = new DataTable();

                            }

                            dataTable_YIC.TableName = "YTC";
                            DataColumn c1 = new DataColumn("rva01");
                            DataColumn c2 = new DataColumn("rvb02");
                            dataTable_YIC.Columns.Add(c1);
                            dataTable_YIC.Columns.Add(c2);


                            for (DataRow jr : dataTable_TTCP.Rows) {
                                if (jr.getValue("check_sp").toString().toUpperCase().equals("TRUE")) {
                                    DataRow nr = dataTable_YIC.NewRow();
                                    nr.setValue("rva01", jr.getValue("rva01").toString());
                                    nr.setValue("rvb02", jr.getValue("rvb02").toString());
                                    dataTable_YIC.Rows.add(nr);
                                }
                            }

                            //clear warning msg
                            warning_msg = "";

                            if (dataTable_YIC.Rows.size() > 0) {
                                current_row = dataTable_YIC.Rows.size() - 1;

                                Intent checkIntent = new Intent(fragmentContext, CheckTTRecNoAlreadyInQCItemService.class);
                                checkIntent.setAction(Constants.ACTION.ACTION_RECEIVING_INSPECTION_GET_TT_REC_NO_IN_QC_ACTION);
                                checkIntent.putExtra("CURRENT_ROW", String.valueOf(current_row));
                                checkIntent.putExtra("REC_NO", dataTable_YIC.Rows.get(current_row).getValue("rva01").toString());
                                checkIntent.putExtra("ITEM_NO", dataTable_YIC.Rows.get(current_row).getValue("rvb02").toString());
                                fragmentContext.startService(checkIntent);
                            }
                        } else {
                            Log.e(TAG, "dataTable_TTCP = null or dataTable_TTCP.Row.size() = 0");
                        }
                    }
                }


            }
        });


        receivingList.clear();

        /*ReceivingInspectionItem item = new ReceivingInspectionItem();
        item.setRva05("test1");
        item.setIma021("test2");
        item.setIma021("test3");
        receivingList.add(item);

        ReceivingInspectionItem item2 = new ReceivingInspectionItem();
        item2.setRva05("test1");
        item2.setIma021("test2");
        item2.setIma021("test3");
        receivingList.add(item2);

        ReceivingInspectionItem item3 = new ReceivingInspectionItem();
        item3.setRva05("test1");
        item3.setIma021("test2");
        item3.setIma021("test3");
        receivingList.add(item3);

        ReceivingInspectionItem item4 = new ReceivingInspectionItem();
        item4.setRva05("test1");
        item4.setIma021("test2");
        item4.setIma021("test3");
        receivingList.add(item4);

        ReceivingInspectionItem item5 = new ReceivingInspectionItem();
        item5.setRva05("test1");
        item5.setIma021("test2");
        item5.setIma021("test3");
        receivingList.add(item5);

        ReceivingInspectionItem item6 = new ReceivingInspectionItem();
        item6.setRva05("test1");
        item6.setIma021("test2");
        item6.setIma021("test3");
        receivingList.add(item6);

        ReceivingInspectionItem item7 = new ReceivingInspectionItem();
        item7.setRva05("test1");
        item7.setIma021("test2");
        item7.setIma021("test3");
        receivingList.add(item7);*/

        receivingInspectionItemAdapter = new ReceivingInspectionItemAdapter(fragmentContext, R.layout.receiving_inspection_fragment_list_item, receivingList);
        listView.setAdapter(receivingInspectionItemAdapter);

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

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_RECEIVING_INSPECTION_GET_TT_RECEIVE_GOODS_REPORT_DATA_QC_SUCCESS)) {
                        Log.d(TAG, "receive ACTION_RECEIVING_INSPECTION_GET_TT_RECEIVE_GOODS_REPORT_DATA_QC_SUCCESS");

                        if (receivingInspectionItemAdapter != null) {
                            receivingInspectionItemAdapter.notifyDataSetChanged();
                        }

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_RECEIVING_INSPECTION_GET_TT_RECEIVE_GOODS_REPORT_DATA_QC_EMPTY)) {
                        Log.d(TAG, "receive ACTION_RECEIVING_INSPECTION_GET_TT_RECEIVE_GOODS_REPORT_DATA_QC_EMPTY");

                        toast(getResources().getString(R.string.receiving_inspection_get_receive_report_data_qc_empty));
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_RECEIVING_INSPECTION_GET_TT_RECEIVE_GOODS_REPORT_DATA_QC_FAILED)) {
                        Log.d(TAG, "receive ACTION_RECEIVING_INSPECTION_GET_TT_RECEIVE_GOODS_REPORT_DATA_QC_FAILED");

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_RECEIVING_INSPECTION_GET_TT_REC_NO_IN_QC_YES)) {
                        Log.d(TAG, "receive ACTION_RECEIVING_INSPECTION_GET_TT_REC_NO_IN_QC_YES");

                        warning_msg = warning_msg +
                                getResources().getString(R.string.receiving_inspection_has_been_generate_to_qc, dataTable_YIC.Rows.get(current_row), dataTable_YIC.Rows.get(current_row)) +"\n";

                        //go next
                        current_row = current_row - 1;
                        Intent checkIntent = new Intent(fragmentContext, CheckTTRecNoAlreadyInQCItemService.class);
                        checkIntent.setAction(Constants.ACTION.ACTION_RECEIVING_INSPECTION_GET_TT_REC_NO_IN_QC_ACTION);
                        checkIntent.putExtra("CURRENT_ROW", String.valueOf(current_row));
                        checkIntent.putExtra("REC_NO", dataTable_YIC.Rows.get(current_row).getValue("rva01").toString());
                        checkIntent.putExtra("ITEM_NO", dataTable_YIC.Rows.get(current_row).getValue("rvb02").toString());
                        fragmentContext.startService(checkIntent);

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_RECEIVING_INSPECTION_GET_TT_REC_NO_IN_QC_NO)) {
                        Log.d(TAG, "receive ACTION_RECEIVING_INSPECTION_GET_TT_REC_NO_IN_QC_NO");

                        //go next
                        current_row = current_row - 1;
                        Intent checkIntent = new Intent(fragmentContext, CheckTTRecNoAlreadyInQCItemService.class);
                        checkIntent.setAction(Constants.ACTION.ACTION_RECEIVING_INSPECTION_GET_TT_REC_NO_IN_QC_ACTION);
                        checkIntent.putExtra("CURRENT_ROW", String.valueOf(current_row));
                        checkIntent.putExtra("REC_NO", dataTable_YIC.Rows.get(current_row).getValue("rva01").toString());
                        checkIntent.putExtra("ITEM_NO", dataTable_YIC.Rows.get(current_row).getValue("rvb02").toString());
                        fragmentContext.startService(checkIntent);

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_RECEIVING_INSPECTION_GET_TT_REC_NO_IN_QC_COMPLETE)) {
                        Log.d(TAG, "receive ACTION_RECEIVING_INSPECTION_GET_TT_REC_NO_IN_QC_COMPLETE");

                        if (warning_msg.length() > 0) {
                            warning_msg = warning_msg + getResources().getString(R.string.receiving_inspection_check_tt_rec_no_already_in_qc);

                            AlertDialog.Builder confirmdialog = new AlertDialog.Builder(fragmentContext);
                            confirmdialog.setIcon(R.drawable.ic_warning_black_48dp);
                            confirmdialog.setTitle(fragmentContext.getResources().getString(R.string.receiving_inspection_qc_generate));
                            confirmdialog.setMessage(warning_msg);
                            confirmdialog.setPositiveButton(fragmentContext.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {


                                }
                            });
                            /*confirmdialog.setNegativeButton(fragmentContext.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {


                                }
                            });*/
                            confirmdialog.show();
                        } else { //no warning msg, then execute tt
                            Intent executeIntent = new Intent(fragmentContext, ExecuteTTcqcp001SetService.class);
                            executeIntent.setAction(Constants.ACTION.ACTION_RECEIVING_INSPECTION_EXECUTE_TT_OQCP_ACTION);
                            fragmentContext.startService(executeIntent);

                        }

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_RECEIVING_INSPECTION_GET_TT_REC_NO_IN_QC_FAILED)) {
                        Log.d(TAG, "receive ACTION_RECEIVING_INSPECTION_GET_TT_REC_NO_IN_QC_FAILED");

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_RECEIVING_INSPECTION_EXECUTE_TT_OQCP_SUCCESS)) {
                        Log.d(TAG, "receive ACTION_RECEIVING_INSPECTION_EXECUTE_TT_OQCP_SUCCESS");

                        //then make QC check, generate in-stock order for confirm
                        if (dataTable_PG_M != null) {
                            dataTable_PG_M.clear();
                        } else {
                            dataTable_PG_M = new DataTable();
                        }

                        dataTable_PG_M.TableName = "PG_M";

                        DataColumn v1 = new DataColumn("rec_no");
                        DataColumn v2 = new DataColumn("user_no");
                        DataColumn v3 = new DataColumn("item_no");

                        dataTable_PG_M.Columns.Add(v1);
                        dataTable_PG_M.Columns.Add(v2);
                        dataTable_PG_M.Columns.Add(v3);

                        for (DataRow rx : dataTable_YIC.Rows) {
                            DataRow kr = dataTable_PG_M.NewRow();
                            kr.setValue("rec_no", rx.getValue("rva01"));
                            kr.setValue("item_no", rx.getValue("rvb02"));
                            kr.setValue("user_no", emp_no);
                            dataTable_PG_M.Rows.add(kr);
                        }

                        //start execute_TT_Prg_A service
                        Intent executeIntent = new Intent(fragmentContext, ExecuteTTPrgAService.class);
                        executeIntent.setAction(Constants.ACTION.ACTION_RECEIVING_INSPECTION_EXECUTE_TT_PRG_A_ACTION);
                        fragmentContext.startService(executeIntent);



                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_RECEIVING_INSPECTION_EXECUTE_TT_OQCP_FAILED)) {
                        Log.d(TAG, "receive ACTION_RECEIVING_INSPECTION_EXECUTE_TT_OQCP_FAILED");

                        toast(getResources().getString(R.string.receiving_inspection_execute_tt_oqcp_failed));

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_RECEIVING_INSPECTION_EXECUTE_TT_PRG_A_SUCCESS)) {
                        Log.d(TAG, "receive ACTION_RECEIVING_INSPECTION_EXECUTE_TT_PRG_A_SUCCESS");

                        toast(getResources().getString(R.string.receiving_inspection_qc_was_been_confirmed));

                        //remove receive no were successfully been generate to QC in dataTable_TTCP
                        for (int i=dataTable_TTCP.Rows.size()-1; i>=0; i--) {
                            if (Boolean.valueOf(dataTable_TTCP.Rows.get(i).getValue("check_sp").toString())) {
                                dataTable_TTCP.Rows.remove(i);
                                receivingList.remove(i);
                            }
                        }

                        if (receivingInspectionItemAdapter != null)
                            receivingInspectionItemAdapter.notifyDataSetChanged();

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_RECEIVING_INSPECTION_EXECUTE_TT_PRG_A_FAILED)) {
                        Log.d(TAG, "receive ACTION_RECEIVING_INSPECTION_EXECUTE_TT_PRG_A_FAILED");

                        toast(getResources().getString(R.string.receiving_inspection_qc_was_been_confirmed_failed));

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_RECEIVING_INSPECTION_ITEM_SELECT_CHANGE)) {
                        Log.d(TAG, "receive ACTION_RECEIVING_INSPECTION_ITEM_SELECT_CHANGE");



                        if (receivingInspectionItemAdapter != null &&
                                receivingInspectionItemAdapter.mSparseBooleanArray.size() > 0) {

                            boolean found = false;

                            for (int i=0; i<receivingInspectionItemAdapter.mSparseBooleanArray.size(); i++) {
                                if (receivingInspectionItemAdapter.mSparseBooleanArray.get(i)) {
                                    found = true;
                                    break;
                                }
                            }

                            if (found) {
                                btnQCGenerate.setEnabled(true);
                            } else {
                                btnQCGenerate.setEnabled(false);
                            }
                        }


                    }


                    else if("unitech.scanservice.data".equals(intent.getAction())) {
                        Log.d(TAG, "unitech.scanservice.data");
                        Bundle bundle = intent.getExtras();
                        if(bundle != null )
                        {
                            String text = bundle.getString("text");

                            Log.e(TAG, "msg = "+text);

                            if (text != null && text.length() > 0 ) {

                                text = text.replaceAll("\\n","");

                                int counter = 0;
                                for( int i=0; i<text.length(); i++ ) {
                                    if( text.charAt(i) == '#' ) {
                                        counter++;
                                    }
                                }

                                Log.e(TAG, "counter = "+counter);

                                if (counter >= 1) {
                                    receivingList.clear();
                                    if (receivingInspectionItemAdapter != null) {
                                        receivingInspectionItemAdapter.notifyDataSetChanged();
                                    }

                                    //regenerate new session id
                                    GenerateRandomString rString = new GenerateRandomString();
                                    k_id = rString.randomString(32);
                                    Log.e(TAG, "session_id = "+k_id);

                                    String codeArray[] = text.split("#");

                                    editTextReceivingInspection.setText(codeArray[0]);

                                    Intent checkIntent = new Intent(context, GetTTReceiveGoodsReportDataQCService.class);
                                    checkIntent.setAction(Constants.ACTION.ACTION_RECEIVING_INSPECTION_GET_TT_RECEIVE_GOODS_REPORT_DATA_QC_ACTION);
                                    checkIntent.putExtra("PART_NO", codeArray[0]);
                                    checkIntent.putExtra("BARCODE_NO", text);
                                    context.startService(checkIntent);

                                } else {






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
            filter.addAction(Constants.ACTION.ACTION_RECEIVING_INSPECTION_GET_TT_RECEIVE_GOODS_REPORT_DATA_QC_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_RECEIVING_INSPECTION_GET_TT_RECEIVE_GOODS_REPORT_DATA_QC_EMPTY);
            filter.addAction(Constants.ACTION.ACTION_RECEIVING_INSPECTION_GET_TT_RECEIVE_GOODS_REPORT_DATA_QC_FAILED);
            filter.addAction(Constants.ACTION.ACTION_RECEIVING_INSPECTION_GET_TT_REC_NO_IN_QC_YES);
            filter.addAction(Constants.ACTION.ACTION_RECEIVING_INSPECTION_GET_TT_REC_NO_IN_QC_NO);
            filter.addAction(Constants.ACTION.ACTION_RECEIVING_INSPECTION_GET_TT_REC_NO_IN_QC_COMPLETE);
            filter.addAction(Constants.ACTION.ACTION_RECEIVING_INSPECTION_GET_TT_REC_NO_IN_QC_FAILED);
            filter.addAction(Constants.ACTION.ACTION_RECEIVING_INSPECTION_EXECUTE_TT_OQCP_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_RECEIVING_INSPECTION_EXECUTE_TT_OQCP_FAILED);
            filter.addAction(Constants.ACTION.ACTION_RECEIVING_INSPECTION_EXECUTE_TT_PRG_A_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_RECEIVING_INSPECTION_EXECUTE_TT_PRG_A_FAILED);
            filter.addAction(Constants.ACTION.ACTION_RECEIVING_INSPECTION_ITEM_SELECT_CHANGE);

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
