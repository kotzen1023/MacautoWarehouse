package com.macauto.macautowarehouse;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v4.app.Fragment;

public class ShipmentFragment extends Fragment {
    private static final String TAG = ShipmentFragment.class.getName();

    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");



        final  View view = inflater.inflate(R.layout.shipment_fragment, container, false);

        TextView textView = view.findViewById(R.id.textShipment);

        context = getContext();

        //Intent getintent = new Intent(context, CheckReceiveGoodService.class);
        //getintent.setAction(Constants.ACTION.ACTION_CHECK_RECEIVE_GOODS);
        //getintent.putExtra("ACCOUNT", account);
        //getintent.putExtra("DEVICE_ID", device_id);
        //getintent.putExtra("service_ip_address", service_ip_address);
        //getintent.putExtra("service_port", service_port);
        //context.startService(getintent);


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
