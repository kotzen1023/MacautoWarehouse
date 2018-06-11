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
import android.widget.TextView;
import android.widget.Toast;

import com.macauto.macautowarehouse.data.Constants;

import com.macauto.macautowarehouse.service.CheckEmpExistService;
import com.macauto.macautowarehouse.service.CheckEmpPasswordService;
import com.macauto.macautowarehouse.service.LoginCheckService;

public class LoginFragment extends Fragment {
    private static final String TAG = LoginFragment.class.getName();

    private Context loginContext;

    private String account;
    private String password;
    private EditText editTextAccount;
    private EditText editTextPassword;

    private static BroadcastReceiver mReceiver = null;
    private static boolean isRegister = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");



        final  View view = inflater.inflate(R.layout.login_fragment, container, false);

        //TextView textView = view.findViewById(R.id.textLogin);

        loginContext = getContext();

        Button btnLogin = view.findViewById(R.id.btnLoginConfirm);

        editTextAccount = view.findViewById(R.id.accountInput);
        editTextPassword = view.findViewById(R.id.passwordInput);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                Intent intent = new Intent(loginContext, CheckEmpExistService.class);
                intent.setAction(Constants.ACTION.ACTION_CHECK_EMP_EXIST_ACTION);
                intent.putExtra("EMP_NO", editTextAccount.getText().toString());
                //intent.putExtra("PASSWORD", editTextPassword.getText().toString());

                loginContext.startService(intent);
            }
        });

        IntentFilter filter;

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() != null) {

                    if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_CHECK_EMP_EXIST_SUCCESS)) {
                        Log.d(TAG, "emp_no exist!");

                        Intent checkPasswordIntent = new Intent(context, CheckEmpPasswordService.class);
                        checkPasswordIntent.setAction(Constants.ACTION.ACTION_CHECK_EMP_PASSWORD_ACTION);
                        checkPasswordIntent.putExtra("EMP_NO", editTextAccount.getText().toString());
                        checkPasswordIntent.putExtra("EMP_PASSWORD", editTextPassword.getText().toString());
                        //intent.putExtra("PASSWORD", editTextPassword.getText().toString());

                        loginContext.startService(checkPasswordIntent);

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_CHECK_EMP_EXIST_NOT_EXIST)) {
                        Log.d(TAG, "emp_no is not exist!");
                        toast("emp_no is not exist!");
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_CHECK_EMP_PASSWORD_SUCCESS)) {
                        Intent loginResultIntent = new Intent(Constants.ACTION.ACTION_LOGIN_SUCCESS);
                        loginResultIntent.putExtra("ACCOUNT", editTextAccount.getText().toString());
                        loginResultIntent.putExtra("PASSWORD", editTextPassword.getText().toString());
                        loginContext.sendBroadcast(loginResultIntent);
                        //intent.putExtra("PASSWORD", editTextPassword.getText().toString());


                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_CHECK_EMP_PASSWORD_FAILED)) {
                        Log.d(TAG, "emp_no is not exist!");
                        toast("password error!");
                    }
                }
            }
        };


        if (!isRegister) {
            filter = new IntentFilter();
            filter.addAction(Constants.ACTION.ACTION_CHECK_EMP_EXIST_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_CHECK_EMP_EXIST_NOT_EXIST);
            filter.addAction(Constants.ACTION.ACTION_CHECK_EMP_PASSWORD_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_CHECK_EMP_PASSWORD_FAILED);
            loginContext.registerReceiver(mReceiver, filter);
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
                loginContext.unregisterReceiver(mReceiver);
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
        Toast toast = Toast.makeText(loginContext, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }
}
