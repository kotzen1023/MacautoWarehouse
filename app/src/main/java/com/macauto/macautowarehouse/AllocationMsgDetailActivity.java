package com.macauto.macautowarehouse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;


import com.macauto.macautowarehouse.data.Constants;


public class AllocationMsgDetailActivity extends AppCompatActivity {
    private static final String TAG = "AllocationMsgDetail";



    private static BroadcastReceiver mReceiver = null;
    private static boolean isRegister = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allocation_msg_detail_activity);

        Intent intent = getIntent();


        IntentFilter filter;

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {



                if (intent.getAction() != null) {

                    if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_DETAIL_FAILED)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_GET_MY_MESS_DETAIL_FAILED");



                    }

                }
            }
        };

        if (!isRegister) {
            filter = new IntentFilter();
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_DETAIL_FAILED);

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
}
