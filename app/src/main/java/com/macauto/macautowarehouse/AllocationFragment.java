package com.macauto.macautowarehouse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.macauto.macautowarehouse.data.Constants;

public class AllocationFragment extends Fragment {
    private static final String TAG = AllocationFragment.class.getName();

    private Context context;

    private static BroadcastReceiver mReceiver = null;
    private static boolean isRegister = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");



        final  View view = inflater.inflate(R.layout.allocation_fragment, container, false);

        TextView textView = view.findViewById(R.id.textAlloccation);


        return view;
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, "onDestroy");

        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

    }
}
