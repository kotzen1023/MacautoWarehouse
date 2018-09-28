package com.macauto.macautowarehouse;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;


import com.macauto.macautowarehouse.data.Constants;

import com.macauto.macautowarehouse.service.GetTTReceiveGoodsDataService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import static com.macauto.macautowarehouse.MainActivity.pda_type;



public class ReceivingAddDialogActivity extends AppCompatActivity {
    private static final String TAG = "ReceivingAddDialog";

    private static BroadcastReceiver mReceiver = null;
    private static boolean isRegister = false;
    //private Context context;

    InputMethodManager imm;

    private static EditText receiving_part_no, receiving_part_date, receiving_vendor_no, edittextRecvEdi, receiving_locate;
    private static Button btnConfirm, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receiving_add_new_dialog_activity);

        //context = getApplicationContext();

        setTitle(getResources().getString(R.string.add));



        if (pda_type == 0) { //PA720
            Intent scanIntent = new Intent();
            scanIntent.setAction("unitech.scanservice.scan2key_setting");
            scanIntent.putExtra("scan2key", false);
            sendBroadcast(scanIntent);

        } else { //TB120
            Intent scanIntent = new Intent();
            scanIntent.setAction("unitech.scanservice.scan2key_setting");
            scanIntent.putExtra("scan2key", true);
            sendBroadcast(scanIntent);
        }

        btnConfirm = findViewById(R.id.btnRecvAddNewConfirm);
        btnCancel = findViewById(R.id.btnRecvAddNewCancel);
        receiving_part_no = findViewById(R.id.receiving_part_no);
        receiving_part_date = findViewById(R.id.receiving_part_date);
        receiving_vendor_no = findViewById(R.id.receiving_vendor_no);
        edittextRecvEdi = findViewById(R.id.receiving_edi);
        receiving_locate = findViewById(R.id.receiving_locate);

        Date currentTime = Calendar.getInstance().getTime();
        String fDate = new SimpleDateFormat("yyyyMMdd").format(currentTime);
        receiving_part_date.setText(fDate);

        edittextRecvEdi.setKeyListener(null);
        edittextRecvEdi.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Log.d(TAG, "beforeTextChanged: "+s.toString());
                //textChangeBefore = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.d(TAG, "s = "+s.toString()+", start "+start+" before = "+before+" count = "+count );


            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "afterTextChanged: "+s.toString());

                btnConfirm.setEnabled(true);
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getintent = new Intent(ReceivingAddDialogActivity.this, GetTTReceiveGoodsDataService.class);
                getintent.putExtra("SHIPPING_NO", edittextRecvEdi.getText().toString());
                startService(getintent);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        final IntentFilter filter;

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //Log.e(TAG, "intent.getAction() =>>>> "+intent.getAction().toString());

                if (intent.getAction() != null) {

                    if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_RECEIVING_GOODS_DATA_FAILED)) {
                        Log.d(TAG, "receive ACTION_RECEIVING_GOODS_DATA_FAILED");
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_RECEIVING_GOODS_DATA_SUCCESS)) {
                        Log.d(TAG, "receive ACTION_RECEIVING_GOODS_DATA_SUCCESS");
                    } else if("unitech.scanservice.data" .equals(intent.getAction())) {
                        Log.d(TAG, "unitech.scanservice.data");
                        Bundle bundle = intent.getExtras();
                        if(bundle != null )
                        {
                            String text = bundle.getString("text");
                            Log.e(TAG, "msg = "+text);

                            edittextRecvEdi.setText(text);

                            if (text.length() > 0 ) {
                                int counter = 0;
                                for( int i=0; i<text.length(); i++ ) {
                                    if( text.charAt(i) == '#' ) {
                                        counter++;
                                    }
                                }

                                Log.e(TAG, "counter = "+counter);



                            }
                        }
                    }

                }
            }
        };

        if (!isRegister) {
            filter = new IntentFilter();
            filter.addAction("unitech.scanservice.data");
            filter.addAction(Constants.ACTION.ACTION_RECEIVING_GOODS_DATA_FAILED);
            filter.addAction(Constants.ACTION.ACTION_RECEIVING_GOODS_DATA_SUCCESS);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        Log.e(TAG, "onCreateOptionsMenu");

        getMenuInflater().inflate(R.menu.divided_activity_menu, menu);



        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);





        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.hide_or_show_keyboard:
                View view = getCurrentFocus();
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                break;

            default:
                break;
        }

        return true;
    }
}
