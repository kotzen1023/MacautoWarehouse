package com.macauto.macautowarehouse;


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

import android.widget.AdapterView;

import android.widget.Button;

import android.widget.ListView;

import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.macauto.macautowarehouse.data.AllocationMsgAdapter;


import com.macauto.macautowarehouse.data.Constants;

import com.macauto.macautowarehouse.service.CheckDeleteMessageRightsService;

import com.macauto.macautowarehouse.service.DeleteMessageNoService;

import com.macauto.macautowarehouse.service.GetMyMessDetailNewService;
import com.macauto.macautowarehouse.service.GetMyMessListService;



import static com.macauto.macautowarehouse.MainActivity.emp_no;
import static com.macauto.macautowarehouse.MainActivity.msg_list;


public class AllocationMsgFragment extends Fragment {
    private static final String TAG = AllocationMsgFragment.class.getName();

    private Context fragmentContext;

    private static BroadcastReceiver mReceiver = null;
    private static boolean isRegister = false;

    //ProgressDialog loadDialog = null;
    ProgressBar progressBar = null;
    RelativeLayout relativeLayout;
    //public static ArrayList<AllocationMsgItem> msg_list = new ArrayList<>();

    //private LinearLayout layoutMsgShow;
    //private LinearLayout layoutScan;

    //private Button btnMsgList;
    //private Button btnMsgScan;

    private AllocationMsgAdapter allocationMsgAdapter;
    private ListView msgListView;
    private Button btnDelete;

    /*private TextView x_scan_no;
    private TextView s_iss_date;
    private TextView sp_made_no;
    private TextView s_tag_stock_no;
    private TextView s_tag_locate_no;
    private TextView s_pre_get_datetime;
    private TextView s_ima03;*/

    //public static DataTable msgDataTable;

    private static String iss_no;
    private static String dateTime_0, dateTime_1, dateTime_2, dateTime_3;
    private static int current_check_delete = -1;
    //private static boolean is_delete_right = false;

    private static int item_select = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");

        fragmentContext = getContext();

        final  View view = inflater.inflate(R.layout.allocation_msg_fragment, container, false);

        relativeLayout = view.findViewById(R.id.allocation_msg_list_container);
        progressBar = new ProgressBar(fragmentContext,null,android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        relativeLayout.addView(progressBar,params);
        progressBar.setVisibility(View.GONE);
        //layoutMsgShow = view.findViewById(R.id.layoutMsgShow);
        btnDelete = view.findViewById(R.id.btnMsgDelete);
        //layoutScan = view.findViewById(R.id.layoutScan);

        //btnMsgList = view.findViewById(R.id.btnMsgList);
        //btnMsgScan = view.findViewById(R.id.btnMsgScan);

        /*x_scan_no = view.findViewById(R.id.x_scan_no);
        s_iss_date = view.findViewById(R.id.s_iss_date);
        sp_made_no = view.findViewById(R.id.sp_made_no);
        s_tag_stock_no = view.findViewById(R.id.s_tag_stock_no);
        s_tag_locate_no = view.findViewById(R.id.s_tag_locate_no);
        s_pre_get_datetime = view.findViewById(R.id.s_pre_get_datetime);
        s_ima03 = view.findViewById(R.id.s_ima03);*/

        /*btnMsgList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnMsgList.setEnabled(false);
                btnMsgScan.setEnabled(true);
                layoutMsgShow.setVisibility(View.VISIBLE);
                layoutScan.setVisibility(View.GONE);
            }
        });

        btnMsgScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnMsgList.setEnabled(true);
                btnMsgScan.setEnabled(false);
                layoutMsgShow.setVisibility(View.GONE);
                layoutScan.setVisibility(View.VISIBLE);
            }
        });*/

        msgListView = view.findViewById(R.id.listViewAllocationMsg);
        //btnDelete = view.findViewById(R.id.btnDelete);

        msgListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "select "+position);



                //deselect other
                for (int i=0; i<msg_list.size(); i++) {

                    if (i == position) {

                        if (msg_list.get(i).isSelected()) {
                            msg_list.get(i).setSelected(false);
                            item_select = -1;
                            btnDelete.setEnabled(false);
                        } else {
                            msg_list.get(i).setSelected(true);
                            item_select = position;
                            //btnDelete.setEnabled(true);
                            if (msg_list.get(i).isDelete()) {
                                btnDelete.setEnabled(true);
                            } else {
                                btnDelete.setEnabled(false);
                            }
                        }

                    } else {
                        msg_list.get(i).setSelected(false);

                    }
                }

                msgListView.invalidateViews();
            }
        });

        msgListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                progressBar.setVisibility(View.VISIBLE);
                /*loadDialog = new ProgressDialog(fragmentContext);
                loadDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                loadDialog.setTitle(getResources().getString(R.string.Processing));
                loadDialog.setIndeterminate(false);
                loadDialog.setCancelable(false);
                loadDialog.show();*/

                String[] p_no = msg_list.get(position).getWork_order().split("#");

                String iss_no = p_no[0];

                String dateTime_0="", dateTime_1="", dateTime_2="", dateTime_3="";

                if (p_no[2].length() > 0) {
                    dateTime_0 = p_no[2].substring(0, 4);
                    dateTime_1 = p_no[2].substring(4, 6);
                    dateTime_2 = p_no[2].substring(6, 8);
                    dateTime_3 = p_no[2].substring(9);
                }

                Intent getMessDetailIntent = new Intent(fragmentContext, GetMyMessDetailNewService.class);
                getMessDetailIntent.setAction(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_DETAIL_ACTION);
                getMessDetailIntent.putExtra("ISS_NO", iss_no);
                getMessDetailIntent.putExtra("DATETIME_0", dateTime_0);
                getMessDetailIntent.putExtra("DATETIME_1", dateTime_1);
                getMessDetailIntent.putExtra("DATETIME_2", dateTime_2);
                getMessDetailIntent.putExtra("DATETIME_3", dateTime_3);
                fragmentContext.startService(getMessDetailIntent);

                return true;
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int found = -1;

                for (int i=0; i<msg_list.size(); i++) {
                    if (msg_list.get(i).isSelected()) {
                        found = i;
                    }
                }



                android.app.AlertDialog.Builder confirmdialog = new android.app.AlertDialog.Builder(fragmentContext);
                confirmdialog.setIcon(R.drawable.ic_warning_black_48dp);
                confirmdialog.setTitle(getResources().getString(R.string.action_allocation_msg));
                confirmdialog.setMessage(getResources().getString(R.string.delete)+":\n"+msg_list.get(found).getWork_order()+" ?");
                confirmdialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        /*loadDialog = new ProgressDialog(fragmentContext);
                        loadDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        loadDialog.setTitle(getResources().getString(R.string.Processing));
                        loadDialog.setIndeterminate(false);
                        loadDialog.setCancelable(false);
                        loadDialog.show();*/
                        progressBar.setVisibility(View.VISIBLE);

                        Intent deleteIntent = new Intent(fragmentContext, DeleteMessageNoService.class);
                        deleteIntent.setAction(Constants.ACTION.ACTION_ALLOCATION_HANDLE_MSG_DELETE_ACTION);
                        deleteIntent.putExtra("MESSAGE_NO", iss_no);
                        deleteIntent.putExtra("USER_NO", emp_no);
                        fragmentContext.startService(deleteIntent);


                    }
                });
                confirmdialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // btnScan.setVisibility(View.VISIBLE);
                        // btnConfirm.setVisibility(View.GONE);

                    }
                });
                confirmdialog.show();
            }
        });

        msg_list.clear();

        /*AllocationMsgItem item1 = new AllocationMsgItem();
        item1.setWork_order("180809001#A001A01#20180809 10:23");
        //item1.setDate("20180808");
        msg_list.add(item1);

        AllocationMsgItem item2 = new AllocationMsgItem();
        item2.setWork_order("180809001#A001A02#20180809 11:23");
        //item1.setDate("20180808");
        msg_list.add(item2);*/

        allocationMsgAdapter = new AllocationMsgAdapter(fragmentContext, R.layout.allocation_msg_list_item, msg_list);
        msgListView.setAdapter(allocationMsgAdapter);

        //get my mess list
        Intent getMessIntent = new Intent(fragmentContext, GetMyMessListService.class);
        getMessIntent.setAction(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_LIST_ACTION);
        getMessIntent.putExtra("USER_NO", emp_no);
        fragmentContext.startService(getMessIntent);

        progressBar.setVisibility(View.VISIBLE);
        /*loadDialog = new ProgressDialog(fragmentContext);
        loadDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadDialog.setTitle(getResources().getString(R.string.Processing));
        loadDialog.setIndeterminate(false);
        loadDialog.setCancelable(false);
        loadDialog.show();*/

        final IntentFilter filter;

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //Log.e(TAG, "intent.getAction() =>>>> "+intent.getAction().toString());

                if (intent.getAction() != null) {

                    if (intent.getAction().equalsIgnoreCase(Constants.ACTION.SOAP_CONNECTION_FAIL)) {
                        Log.d(TAG, "receive SOAP_CONNECTION_FAIL");
                        //if (loadDialog != null)
                        //    loadDialog.dismiss();
                        progressBar.setVisibility(View.GONE);
                        toast(fragmentContext.getResources().getString(R.string.socket_failed));
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SOCKET_TIMEOUT)) {
                        Log.d(TAG, "receive ACTION_SOCKET_TIMEOUT");
                        //if (loadDialog != null)
                        //    loadDialog.dismiss();
                        progressBar.setVisibility(View.GONE);
                        toast(getResources().getString(R.string.socket_timeout));

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_LIST_FAILED)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_GET_MY_MESS_LIST_FAILED");
                        //loadDialog.dismiss();
                        progressBar.setVisibility(View.GONE);
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_LIST_SUCCESS)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_GET_MY_MESS_LIST_SUCCESS");
                        //loadDialog.dismiss();
                        progressBar.setVisibility(View.GONE);

                        if (allocationMsgAdapter != null)
                            allocationMsgAdapter.notifyDataSetChanged();
                        else {
                            allocationMsgAdapter = new AllocationMsgAdapter(fragmentContext, R.layout.allocation_msg_list_item, msg_list);
                            msgListView.setAdapter(allocationMsgAdapter);
                        }

                        //then check delete right
                        current_check_delete = msg_list.size() - 1; //start from the last

                        if (current_check_delete >= 0) {
                            String[] p_no = msg_list.get(current_check_delete).getWork_order().split("#");
                            if (p_no.length > 1) {

                                iss_no = p_no[0];

                                Intent getDeleteRightIntent = new Intent(fragmentContext, CheckDeleteMessageRightsService.class);
                                getDeleteRightIntent.setAction(Constants.ACTION.ACTION_ALLOCATION_CHECK_IS_DELETE_RIGHT_ACTION);
                                getDeleteRightIntent.putExtra("ISS_NO", iss_no);
                                getDeleteRightIntent.putExtra("USER_NO", emp_no);
                                getDeleteRightIntent.putExtra("CURRENT_CHECK_DELETE", String.valueOf(current_check_delete));
                                fragmentContext.startService(getDeleteRightIntent);
                            }
                        }



                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_LIST_EMPTY)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_GET_MY_MESS_LIST_EMPTY");
                        //loadDialog.dismiss();
                        progressBar.setVisibility(View.GONE);
                        toast(getResources().getString(R.string.allocation_no_message));


                        //then check delete right
                        current_check_delete = msg_list.size() - 1; //start from the last

                        if (current_check_delete >= 0) {
                            String[] p_no = msg_list.get(current_check_delete).getWork_order().split("#");
                            if (p_no.length > 1) {

                                iss_no = p_no[0];

                                Intent getDeleteRightIntent = new Intent(fragmentContext, CheckDeleteMessageRightsService.class);
                                getDeleteRightIntent.setAction(Constants.ACTION.ACTION_ALLOCATION_CHECK_IS_DELETE_RIGHT_ACTION);
                                getDeleteRightIntent.putExtra("ISS_NO", iss_no);
                                getDeleteRightIntent.putExtra("USER_NO", emp_no);
                                getDeleteRightIntent.putExtra("CURRENT_CHECK_DELETE", String.valueOf(current_check_delete));
                                fragmentContext.startService(getDeleteRightIntent);
                            }
                        }

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_DETAIL_FAILED)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_GET_MY_MESS_DETAIL_FAILED");
                        //loadDialog.dismiss();
                        progressBar.setVisibility(View.GONE);
                        toast(getResources().getString(R.string.allocation_get_msg_detail_error));

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_DETAIL_SUCCESS)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_GET_MY_MESS_DETAIL_SUCCESS");
                        //loadDialog.dismiss();
                        progressBar.setVisibility(View.GONE);
                        String iss_date = intent.getStringExtra("ISS_DATE");
                        String made_no = intent.getStringExtra("MADE_NO");
                        String tag_locate_no = intent.getStringExtra("TAG_LOCATE_NO");
                        String tag_stock_no = intent.getStringExtra("TAG_STOCK_NO");
                        String ima03 = intent.getStringExtra("IMA03");
                        dateTime_0 = intent.getStringExtra("DATETIME_0");
                        dateTime_1 = intent.getStringExtra("DATETIME_1");
                        dateTime_2 = intent.getStringExtra("DATETIME_2");
                        dateTime_3 = intent.getStringExtra("DATETIME_3");

                        /*s_iss_date.setText(iss_date);
                        sp_made_no.setText(made_no);
                        s_tag_locate_no.setText(tag_locate_no);
                        s_tag_stock_no.setText(tag_stock_no);
                        s_ima03.setText(ima03);
                        String date_string = dateTime_0+"-"+dateTime_1+"-"+dateTime_2+"  "+dateTime_3;
                        s_pre_get_datetime.setText(date_string);*/

                        Intent successIntent = new Intent(fragmentContext, AllocationMsgDetailActivity.class);
                        successIntent.putExtra("ISS_NO", iss_no);
                        successIntent.putExtra("INDEX", String.valueOf(item_select));
                        successIntent.putExtra("ISS_DATE", iss_date);
                        successIntent.putExtra("MADE_NO", made_no);
                        successIntent.putExtra("TAG_LOCATE_NO", tag_locate_no);
                        successIntent.putExtra("TAG_STOCK_NO", tag_stock_no);
                        successIntent.putExtra("IMA03", ima03);
                        successIntent.putExtra("PRE_GET_DATETIME", dateTime_0+"-"+dateTime_1+"-"+dateTime_2+"  "+dateTime_3);
                        successIntent.putExtra("DATETIME_0", dateTime_0);
                        successIntent.putExtra("DATETIME_1", dateTime_1);
                        successIntent.putExtra("DATETIME_2", dateTime_2);
                        startActivity(successIntent);

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_CHECK_IS_DELETE_RIGHT_YES)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_CHECK_IS_DELETE_RIGHT_YES");

                        String current_check_string = intent.getStringExtra("CURRENT_CHECK_DELETE");
                        current_check_delete = Integer.valueOf(current_check_string);
                        msg_list.get(current_check_delete).setDelete(true);
                        //renew listView
                        if (allocationMsgAdapter != null)
                            allocationMsgAdapter.notifyDataSetChanged();
                        //find next
                        current_check_delete = current_check_delete - 1;
                        if (current_check_delete >= 0) {
                            String[] p_no = msg_list.get(current_check_delete).getWork_order().split("#");
                            if (p_no.length > 1) {

                                iss_no = p_no[0];

                                Intent getDeleteRightIntent = new Intent(fragmentContext, CheckDeleteMessageRightsService.class);
                                getDeleteRightIntent.setAction(Constants.ACTION.ACTION_ALLOCATION_CHECK_IS_DELETE_RIGHT_ACTION);
                                getDeleteRightIntent.putExtra("ISS_NO", iss_no);
                                getDeleteRightIntent.putExtra("USER_NO", emp_no);
                                getDeleteRightIntent.putExtra("CURRENT_CHECK_DELETE", String.valueOf(current_check_delete));
                                fragmentContext.startService(getDeleteRightIntent);
                            }
                        }

                        //is_delete_right = true;
                        //btnDelete.setEnabled(true);

                        /*Intent getMessDetailIntent = new Intent(fragmentContext, GetMyMessDetailService.class);
                        getMessDetailIntent.setAction(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_DETAIL_ACTION);
                        getMessDetailIntent.putExtra("ISS_NO", iss_no);
                        fragmentContext.startService(getMessDetailIntent);*/

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_CHECK_IS_DELETE_RIGHT_NO)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_CHECK_IS_DELETE_RIGHT_NO");

                        String current_check_string = intent.getStringExtra("CURRENT_CHECK_DELETE");
                        current_check_delete = Integer.valueOf(current_check_string);
                        msg_list.get(current_check_delete).setDelete(false);
                        //renew listView
                        if (allocationMsgAdapter != null)
                            allocationMsgAdapter.notifyDataSetChanged();
                        //find next
                        current_check_delete = current_check_delete - 1;
                        if (current_check_delete >= 0) {
                            String[] p_no = msg_list.get(current_check_delete).getWork_order().split("#");
                            if (p_no.length > 1) {

                                iss_no = p_no[0];

                                Intent getDeleteRightIntent = new Intent(fragmentContext, CheckDeleteMessageRightsService.class);
                                getDeleteRightIntent.setAction(Constants.ACTION.ACTION_ALLOCATION_CHECK_IS_DELETE_RIGHT_ACTION);
                                getDeleteRightIntent.putExtra("ISS_NO", iss_no);
                                getDeleteRightIntent.putExtra("USER_NO", emp_no);
                                getDeleteRightIntent.putExtra("CURRENT_CHECK_DELETE", String.valueOf(current_check_delete));
                                fragmentContext.startService(getDeleteRightIntent);
                            }
                        }

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_CHECK_IS_DELETE_RIGHT_FAILED)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_CHECK_IS_DELETE_RIGHT_FAILED");

                        String current_check_string = intent.getStringExtra("CURRENT_CHECK_DELETE");
                        current_check_delete = Integer.valueOf(current_check_string);
                        msg_list.get(current_check_delete).setDelete(false);
                        //renew listView
                        if (allocationMsgAdapter != null)
                            allocationMsgAdapter.notifyDataSetChanged();
                        //find next
                        current_check_delete = current_check_delete - 1;
                        if (current_check_delete >= 0) {
                            String[] p_no = msg_list.get(current_check_delete).getWork_order().split("#");
                            if (p_no.length > 1) {

                                iss_no = p_no[0];

                                Intent getDeleteRightIntent = new Intent(fragmentContext, CheckDeleteMessageRightsService.class);
                                getDeleteRightIntent.setAction(Constants.ACTION.ACTION_ALLOCATION_CHECK_IS_DELETE_RIGHT_ACTION);
                                getDeleteRightIntent.putExtra("ISS_NO", iss_no);
                                getDeleteRightIntent.putExtra("USER_NO", emp_no);
                                getDeleteRightIntent.putExtra("CURRENT_CHECK_DELETE", current_check_delete);
                                fragmentContext.startService(getDeleteRightIntent);
                            }
                        }

                    }else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_HANDLE_MSG_DELETE_SUCCESS)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_HANDLE_MSG_DELETE_SUCCESS");

                        msg_list.clear();
                        if (allocationMsgAdapter != null)
                            allocationMsgAdapter.notifyDataSetChanged();


                        Intent getMessIntent = new Intent(fragmentContext, GetMyMessListService.class);
                        getMessIntent.setAction(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_LIST_ACTION);
                        getMessIntent.putExtra("USER_NO", emp_no);
                        fragmentContext.startService(getMessIntent);

                        //String delete_index = intent.getStringExtra("DELETE_INDEX");
                        //int delete_index_int = Integer.valueOf(delete_index);


                        //msg_list.remove(delete_index_int);

                        //if (allocationMsgAdapter != null) {
                        //    allocationMsgAdapter.notifyDataSetChanged();
                        //}


                        /*msg_list.clear();

                        if (allocationMsgAdapter != null) {
                            allocationMsgAdapter.notifyDataSetChanged();
                        }



                        //get my mess list
                        Intent getMessIntent = new Intent(fragmentContext, GetMyMessListService.class);
                        getMessIntent.setAction(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_LIST_ACTION);
                        getMessIntent.putExtra("USER_NO", emp_no);
                        fragmentContext.startService(getMessIntent);*/


                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_HANDLE_MSG_DELETE_FAILED)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_HANDLE_MSG_DELETE_FAILED");
                        //loadDialog.dismiss();
                        progressBar.setVisibility(View.GONE);

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_SWIPE_LAYOUT_UPDATE)){
                        Log.d(TAG, "receive ACTION_ALLOCATION_SWIPE_LAYOUT_UPDATE");
                        if (allocationMsgAdapter != null)
                            allocationMsgAdapter.notifyDataSetChanged();
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_PRODUCT_DELETE_ITEM_CONFIRM)){
                        Log.d(TAG, "receive ACTION_PRODUCT_DELETE_ITEM_CONFIRM");
                        if (allocationMsgAdapter != null)
                            allocationMsgAdapter.notifyDataSetChanged();
                    }/*else if("unitech.scanservice.data" .equals(intent.getAction())) {
                        Log.d(TAG, "unitech.scanservice.data");
                        Bundle bundle = intent.getExtras();
                        if(bundle != null )
                        {
                            String text = bundle.getString("text");
                            Log.e(TAG, "msg = "+text);



                        }
                    }else if("unitech.scanservice.datatype" .equals(intent.getAction())) {
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
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_LIST_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_LIST_FAILED);
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_LIST_EMPTY);
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_DETAIL_FAILED);
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_DETAIL_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_CHECK_IS_DELETE_RIGHT_YES);
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_CHECK_IS_DELETE_RIGHT_NO);
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_CHECK_IS_DELETE_RIGHT_FAILED);
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_HANDLE_MSG_DELETE_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_HANDLE_MSG_DELETE_FAILED);

            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_SWIPE_LAYOUT_UPDATE);
            filter.addAction(Constants.ACTION.ACTION_PRODUCT_DELETE_ITEM_CONFIRM);


            //filter.addAction("unitech.scanservice.data");
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

        dataClear();

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

    private void dataClear() {

        progressBar = null;

        if (msg_list != null)
            msg_list.clear();
    }
}
