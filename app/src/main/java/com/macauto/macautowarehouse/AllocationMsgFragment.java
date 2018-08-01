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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.macauto.macautowarehouse.data.AllocationMsgAdapter;
import com.macauto.macautowarehouse.data.AllocationMsgItem;
import com.macauto.macautowarehouse.data.Constants;
import com.macauto.macautowarehouse.data.DetailItem;
import com.macauto.macautowarehouse.data.GenerateRandomString;
import com.macauto.macautowarehouse.data.InspectedReceiveExpanedAdater;
import com.macauto.macautowarehouse.service.CheckEmpPasswordService;
import com.macauto.macautowarehouse.service.ExecuteScriptTTService;
import com.macauto.macautowarehouse.service.GetDocTypeIsRegOrSubService;
import com.macauto.macautowarehouse.service.GetMyMessDetailService;
import com.macauto.macautowarehouse.service.GetMyMessListService;
import com.macauto.macautowarehouse.service.GetReceiveGoodsInDataService;

import java.util.ArrayList;

import static com.macauto.macautowarehouse.MainActivity.emp_no;
import static com.macauto.macautowarehouse.MainActivity.k_id;
import static com.macauto.macautowarehouse.data.InspectedReceiveExpanedAdater.mSparseBooleanArray;

public class AllocationMsgFragment extends Fragment {
    private static final String TAG = AllocationMsgFragment.class.getName();

    private Context fragmentContext;

    private static BroadcastReceiver mReceiver = null;
    private static boolean isRegister = false;

    ProgressDialog loadDialog = null;
    public static ArrayList<AllocationMsgItem> msg_list = new ArrayList<>();

    private AllocationMsgAdapter allocationMsgAdapter;
    private ListView msgListView;

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
        msgListView = view.findViewById(R.id.listViewAllocationMsg);

        msgListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String[] p_no = msg_list.get(position).getMsg().split("#");

                Intent getMessDetailIntent = new Intent(fragmentContext, GetMyMessDetailService.class);
                getMessDetailIntent.setAction(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_DETAIL_ACTION);
                getMessDetailIntent.putExtra("ISS_NO", p_no[0]);
                fragmentContext.startService(getMessDetailIntent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        msg_list.clear();

        //get my mess list
        Intent getMessIntent = new Intent(fragmentContext, GetMyMessListService.class);
        getMessIntent.setAction(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_LIST_ACTION);
        getMessIntent.putExtra("USER_NO", emp_no);
        fragmentContext.startService(getMessIntent);

        loadDialog = new ProgressDialog(fragmentContext);
        loadDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loadDialog.setTitle(getResources().getString(R.string.Processing));
        loadDialog.setIndeterminate(false);
        loadDialog.setCancelable(false);
        loadDialog.show();

        final IntentFilter filter;

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //Log.e(TAG, "intent.getAction() =>>>> "+intent.getAction().toString());

                if (intent.getAction() != null) {

                    if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_LIST_FAILED)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_GET_MY_MESS_LIST_FAILED");
                        loadDialog.dismiss();
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_LIST_SUCCESS)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_GET_MY_MESS_LIST_SUCCESS");
                        loadDialog.dismiss();

                        allocationMsgAdapter = new AllocationMsgAdapter(fragmentContext, R.layout.allocation_msg_list_item, msg_list);
                        msgListView.setAdapter(allocationMsgAdapter);

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_LIST_EMPTY)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_GET_MY_MESS_LIST_EMPTY");
                        loadDialog.dismiss();
                        toast(getResources().getString(R.string.allocation_no_message));


                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_DETAIL_FAILED)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_GET_MY_MESS_DETAIL_FAILED");



                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_DETAIL_SUCCESS)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_GET_MY_MESS_DETAIL_SUCCESS");



                    } else if("unitech.scanservice.data" .equals(intent.getAction())) {
                        Log.d(TAG, "unitech.scanservice.data");
                        Bundle bundle = intent.getExtras();
                        if(bundle != null )
                        {
                            String text = bundle.getString("text");
                            Log.e(TAG, "msg = "+text+", emp_no = "+emp_no);



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
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_LIST_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_LIST_FAILED);
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_LIST_EMPTY);
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_DETAIL_FAILED);
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_DETAIL_SUCCESS);
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
