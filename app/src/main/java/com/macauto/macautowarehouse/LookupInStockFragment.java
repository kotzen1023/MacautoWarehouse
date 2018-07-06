package com.macauto.macautowarehouse;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.macauto.macautowarehouse.data.Constants;
import com.macauto.macautowarehouse.service.CheckEmpExistService;
import com.macauto.macautowarehouse.service.CheckEmpPasswordService;
import com.macauto.macautowarehouse.service.GetPartWarehouseListService;

public class LookupInStockFragment extends Fragment {
    private static final String TAG = LookupInStockFragment.class.getName();

    private Context context;

    private static BroadcastReceiver mReceiver = null;
    private static boolean isRegister = false;

    private EditText partNo;
    private EditText batchNo;
    private EditText name;
    private EditText spec;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");



        final  View view = inflater.inflate(R.layout.look_up_in_stock_fragment, container, false);



        context = getContext();

        Button btnSearch = view.findViewById(R.id.btnSearch);

        partNo = view.findViewById(R.id.serachPartNo);
        batchNo =view.findViewById(R.id.searchBatchNo);
        name = view.findViewById(R.id.searchName);
        spec = view.findViewById(R.id.searchSpec);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getintent = new Intent(context, GetPartWarehouseListService.class);
                getintent.putExtra("PART_NO", partNo.getText().toString());
                getintent.putExtra("BATCH_NO", batchNo.getText().toString());
                getintent.putExtra("NAME", name.getText().toString());
                getintent.putExtra("SPEC", spec.getText().toString());
                context.startService(getintent);
            }
        });


        return view;
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, "onDestroy");

        if (isRegister && mReceiver != null) {
            try {
                context.unregisterReceiver(mReceiver);
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
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }
}
