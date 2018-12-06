package com.macauto.macautowarehouse;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.macauto.macautowarehouse.data.Constants;

import static com.macauto.macautowarehouse.MainActivity.pda_type;
import static com.macauto.macautowarehouse.MainActivity.web_soap_port;

public class SettingFragment extends Fragment {
    private static final String TAG = SettingFragment.class.getName();

    private Context context;

    //static SharedPreferences pref ;
    //private static final String FILE_NAME = "Preference";

    //private static BroadcastReceiver mReceiver = null;
    //private static boolean isRegister = false;

    CheckBox checkBoxPA720;
    CheckBox checkBoxTB120;
    CheckBox checkBoxPDA408;
    CheckBox checkBoxTestPort;
    CheckBox checkBoxRealPort;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");



        final  View view = inflater.inflate(R.layout.setting_fragment, container, false);

        checkBoxPA720 = view.findViewById(R.id.checkBoxPA720);
        checkBoxTB120 = view.findViewById(R.id.checkBoxTB120);
        checkBoxPDA408 = view.findViewById(R.id.checkBoxPDA408);
        checkBoxTestPort = view.findViewById(R.id.checkBoxTestPort);
        checkBoxRealPort = view.findViewById(R.id.checkBoxRealPort);

        //pref = context.getSharedPreferences(FILE_NAME, MODE_PRIVATE);

        //String account = pref.getString("PDA_TYPE", "");


        if (pda_type == 0) {
            checkBoxPA720.setChecked(true);
            checkBoxTB120.setChecked(false);
            checkBoxPDA408.setChecked(false);
        } else if (pda_type == 1) {
            checkBoxPA720.setChecked(false);
            checkBoxTB120.setChecked(true);
            checkBoxPDA408.setChecked(false);
        } else {
            checkBoxPA720.setChecked(false);
            checkBoxTB120.setChecked(false);
            checkBoxPDA408.setChecked(true);
        }

        if (web_soap_port.equals("8484")) {
            checkBoxTestPort.setChecked(true);
            checkBoxRealPort.setChecked(false);
        } else { //port 8000
            checkBoxTestPort.setChecked(false);
            checkBoxRealPort.setChecked(true);
        }


        checkBoxPA720.setOnClickListener(new View.OnClickListener() {
            Intent settingIntent;

            @Override
            public void onClick(View v) {
                /*if (checkBoxPA720.isChecked()) {
                    checkBoxTB120.setChecked(false);


                    settingIntent = new Intent(Constants.ACTION.ACTION_SETTING_PDA_TYPE_ACTION);
                    settingIntent.putExtra("MODEL_TYPE", "0");

                } else {
                    checkBoxTB120.setChecked(true);

                    settingIntent = new Intent(Constants.ACTION.ACTION_SETTING_PDA_TYPE_ACTION);
                    settingIntent.putExtra("MODEL_TYPE", "1");
                }*/
                checkBoxPA720.setChecked(true);
                checkBoxTB120.setChecked(false);
                checkBoxPDA408.setChecked(false);
                settingIntent = new Intent(Constants.ACTION.ACTION_SETTING_PDA_TYPE_ACTION);
                settingIntent.putExtra("MODEL_TYPE", "0");

                context.sendBroadcast(settingIntent);
            }
        });

        checkBoxTB120.setOnClickListener(new View.OnClickListener() {
            Intent settingIntent;
            @Override
            public void onClick(View v) {
                /*if (checkBoxTB120.isChecked()) {
                    checkBoxPA720.setChecked(false);

                    settingIntent = new Intent(Constants.ACTION.ACTION_SETTING_PDA_TYPE_ACTION);
                    settingIntent.putExtra("MODEL_TYPE", "1");
                } else {
                    checkBoxPA720.setChecked(true);

                    settingIntent = new Intent(Constants.ACTION.ACTION_SETTING_PDA_TYPE_ACTION);
                    settingIntent.putExtra("MODEL_TYPE", "0");
                }*/
                checkBoxPA720.setChecked(false);
                checkBoxTB120.setChecked(true);
                checkBoxPDA408.setChecked(false);
                settingIntent = new Intent(Constants.ACTION.ACTION_SETTING_PDA_TYPE_ACTION);
                settingIntent.putExtra("MODEL_TYPE", "1");
                context.sendBroadcast(settingIntent);
            }
        });

        checkBoxPDA408.setOnClickListener(new View.OnClickListener() {
            Intent settingIntent;
            @Override
            public void onClick(View v) {
                checkBoxPA720.setChecked(false);
                checkBoxTB120.setChecked(false);
                checkBoxPDA408.setChecked(true);
                settingIntent = new Intent(Constants.ACTION.ACTION_SETTING_PDA_TYPE_ACTION);
                settingIntent.putExtra("MODEL_TYPE", "2");
                context.sendBroadcast(settingIntent);
            }
        });

        checkBoxTestPort.setOnClickListener(new View.OnClickListener() {
            Intent settingIntent;

            @Override
            public void onClick(View v) {
                if (checkBoxTestPort.isChecked()) {
                    checkBoxRealPort.setChecked(false);

                    settingIntent = new Intent(Constants.ACTION.ACTION_SETTING_WEB_SOAP_PORT_ACTION);
                    settingIntent.putExtra("WEB_SOAP_PORT", "8484");

                } else {
                    checkBoxRealPort.setChecked(true);

                    settingIntent = new Intent(Constants.ACTION.ACTION_SETTING_WEB_SOAP_PORT_ACTION);
                    settingIntent.putExtra("WEB_SOAP_PORT", "8000");
                }
                context.sendBroadcast(settingIntent);
            }
        });

        checkBoxRealPort.setOnClickListener(new View.OnClickListener() {
            Intent settingIntent;
            @Override
            public void onClick(View v) {
                if (checkBoxRealPort.isChecked()) {
                    checkBoxTestPort.setChecked(false);

                    settingIntent = new Intent(Constants.ACTION.ACTION_SETTING_WEB_SOAP_PORT_ACTION);
                    settingIntent.putExtra("WEB_SOAP_PORT", "8000");
                } else {
                    checkBoxTestPort.setChecked(true);

                    settingIntent = new Intent(Constants.ACTION.ACTION_SETTING_WEB_SOAP_PORT_ACTION);
                    settingIntent.putExtra("WEB_SOAP_PORT", "8484");
                }
                context.sendBroadcast(settingIntent);
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
}
