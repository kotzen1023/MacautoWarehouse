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

import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.macauto.macautowarehouse.data.Constants;

import com.macauto.macautowarehouse.service.CheckEmpExistService;
import com.macauto.macautowarehouse.service.CheckEmpPasswordService;


public class LoginFragment extends Fragment {
    private static final String TAG = LoginFragment.class.getName();

    private Context loginContext;

    //private String account;
    //private String password;
    private EditText editTextAccount;
    private EditText editTextPassword;

    private static BroadcastReceiver mReceiver = null;
    private static boolean isRegister = false;

    private Button btnLogin;

    ProgressBar progressBar = null;
    RelativeLayout relativeLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginContext = getContext();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");



        final  View view = inflater.inflate(R.layout.login_fragment, container, false);

        //TextView textView = view.findViewById(R.id.textLogin);
        relativeLayout = view.findViewById(R.id.login_container);
        progressBar = new ProgressBar(loginContext,null,android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        relativeLayout.addView(progressBar,params);
        progressBar.setVisibility(View.GONE);



        btnLogin = view.findViewById(R.id.btnLoginConfirm);

        editTextAccount = view.findViewById(R.id.accountInput);
        editTextPassword = view.findViewById(R.id.passwordInput);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                btnLogin.setEnabled(false);


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
                    if (intent.getAction().equalsIgnoreCase((Constants.ACTION.ACTION_SOCKET_TIMEOUT))) {
                        Log.d(TAG, "ACTION_SOCKET_TIMEOUT");

                        toast(loginContext.getResources().getString(R.string.socket_timeout));
                        progressBar.setVisibility(View.GONE);
                        btnLogin.setEnabled(true);
                    } else if (intent.getAction().equalsIgnoreCase((Constants.ACTION.SOAP_CONNECTION_FAIL))) {
                        Log.d(TAG, "SOAP_CONNECTION_FAIL");

                        toast(loginContext.getResources().getString(R.string.socket_failed));
                        progressBar.setVisibility(View.GONE);
                        btnLogin.setEnabled(true);
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_CHECK_EMP_EXIST_SUCCESS)) {
                        Log.d(TAG, "emp_no exist!");

                        Intent checkPasswordIntent = new Intent(context, CheckEmpPasswordService.class);
                        checkPasswordIntent.setAction(Constants.ACTION.ACTION_CHECK_EMP_PASSWORD_ACTION);
                        checkPasswordIntent.putExtra("EMP_NO", editTextAccount.getText().toString());
                        checkPasswordIntent.putExtra("EMP_PASSWORD", editTextPassword.getText().toString());
                        //intent.putExtra("PASSWORD", editTextPassword.getText().toString());

                        loginContext.startService(checkPasswordIntent);

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_CHECK_EMP_EXIST_NOT_EXIST)) {
                        Log.d(TAG, "emp_no is not exist!");
                        toast(getResources().getString(R.string.login_no_emp));

                        progressBar.setVisibility(View.GONE);
                        btnLogin.setEnabled(true);

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_CHECK_EMP_PASSWORD_SUCCESS)) {
                        progressBar.setVisibility(View.GONE);
                        btnLogin.setEnabled(true);

                        Intent loginResultIntent = new Intent(Constants.ACTION.ACTION_LOGIN_SUCCESS);
                        loginResultIntent.putExtra("ACCOUNT", editTextAccount.getText().toString());
                        loginResultIntent.putExtra("PASSWORD", editTextPassword.getText().toString());
                        loginContext.sendBroadcast(loginResultIntent);
                        //intent.putExtra("PASSWORD", editTextPassword.getText().toString());



                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_CHECK_EMP_PASSWORD_FAILED)) {
                        Log.d(TAG, "emp_no is not exist!");
                        toast(getResources().getString(R.string.login_password_error));

                        progressBar.setVisibility(View.GONE);
                        btnLogin.setEnabled(true);
                    }
                }
            }
        };


        if (!isRegister) {
            filter = new IntentFilter();
            filter.addAction(Constants.ACTION.ACTION_SOCKET_TIMEOUT);
            filter.addAction(Constants.ACTION.SOAP_CONNECTION_FAIL);
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
