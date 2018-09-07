package com.macauto.macautowarehouse;

import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.macauto.macautowarehouse.data.Constants;


public class LogoutFragment extends Fragment {
    private static final String TAG = LogoutFragment.class.getName();

    private Context logoutContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");



        final  View view = inflater.inflate(R.layout.logout_fragment, container, false);

        //TextView textView = view.findViewById(R.id.textLogin);

        logoutContext = getContext();

        Button btnLogout= view.findViewById(R.id.btnLogout);




        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder confirmdialog = new AlertDialog.Builder(logoutContext);
                confirmdialog.setIcon(R.drawable.ic_warning_black_48dp);
                confirmdialog.setTitle(getResources().getString(R.string.logout_title));
                confirmdialog.setMessage(getResources().getString(R.string.logout_title_msg));
                confirmdialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent loginoutIntent = new Intent(Constants.ACTION.ACTION_LOGOUT_ACTION);
                        logoutContext.sendBroadcast(loginoutIntent);

                    }
                });
                confirmdialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                confirmdialog.show();




            }
        });



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

    public void toast(String message) {
        Toast toast = Toast.makeText(logoutContext, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }
}
