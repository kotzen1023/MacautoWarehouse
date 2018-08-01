package com.macauto.macautowarehouse;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.macauto.macautowarehouse.data.Constants;
import com.macauto.macautowarehouse.data.GenerateRandomString;
import com.macauto.macautowarehouse.service.ConfirmEnteringWarehouseService;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.macauto.macautowarehouse.data.Constants.ACTION.ACTION_SCAN;
import static com.macauto.macautowarehouse.data.FileOperation.init_folder_and_files;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getName();

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    static SharedPreferences pref ;
    static SharedPreferences.Editor editor;
    private static final String FILE_NAME = "Preference";

    private static final String LOG_TAG = "Barcode Scanner API";
    private static final int PHOTO_REQUEST = 10;
    private TextView scanResults;
    private BarcodeDetector detector;
    private Uri imageUri;
    private static final int REQUEST_WRITE_PERMISSION = 20;
    private static final String SAVED_INSTANCE_URI = "uri";
    private static final String SAVED_INSTANCE_RESULT = "result";
    private Context context;

    private static BroadcastReceiver mReceiver = null;
    private static boolean isRegister = false;

    //private MenuItem menuItemReceiveGoods;
    //private MenuItem menuItemShipment;
    private MenuItem menuItemAllocation;
    private MenuItem menuItemEnteringWareHouse;
    //private MenuItem menuItemProductionStorage;
    //private MenuItem menuItemReceivingInspection;
    private MenuItem menuItemLogin;
    private MenuItem menuItemLogout;

    public static boolean isLogin = false;

    private MenuItem setting;

    private MenuItem shipment_main;
    private MenuItem shipment_find;

    private MenuItem allocation_find;
    private MenuItem allocation_replenishment;
    private MenuItem allocation_send_msg;
    private MenuItem allocation_msg;
    private MenuItem allocation_area_confirm;
    private MenuItem allocation_direct;

    private MenuItem receiving_main;
    private MenuItem receiving_record;
    private MenuItem receiving_board;
    private MenuItem receiving_multi;

    private MenuItem entering_warehouse_main;
    private MenuItem entering_warehouse_find;

    private MenuItem production_storage_main;
    private MenuItem production_storage_find;
    private MenuItem production_storage_scan;

    public static int pda_type;
    private InputMethodManager imm;
    public static String k_id;
    public static String web_soap_port;
    public static String emp_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate");
        //create a new kid
        //GenerateRandomString rString = new GenerateRandomString();
        //k_id = rString.randomString(32);
        //Log.e(TAG, "session_id = "+k_id);

        //get default pda type
        pref = getSharedPreferences(FILE_NAME, MODE_PRIVATE);

        //String account = pref.getString("PDA_TYPE", "");
        pda_type = pref.getInt("PDA_TYPE", 0);
        web_soap_port = pref.getString("WEB_SOAP_PORT", "8484");

        context = getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get virtual keyboard
        imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        menuItemLogin = navigationView.getMenu().findItem(R.id.nav_login);
        menuItemLogout = navigationView.getMenu().findItem(R.id.nav_logout);
        //menuItemReceiveGoods = navigationView.getMenu().findItem(R.id.nav_receiving);
        //menuItemShipment = navigationView.getMenu().findItem(R.id.nav_shipment);
        menuItemAllocation = navigationView.getMenu().findItem(R.id.nav_allocation);
        menuItemEnteringWareHouse = navigationView.getMenu().findItem(R.id.nav_entering_warehouse);
        //menuItemReceivingInspection = navigationView.getMenu().findItem(R.id.nav_receiving_inspection);
        //menuItemProductionStorage = navigationView.getMenu().findItem(R.id.nav_production_storage);




        /*Button button = (Button) findViewById(R.id.button);
        scanResults = (TextView) findViewById(R.id.scan_results);

        if (savedInstanceState != null) {
            imageUri = Uri.parse(savedInstanceState.getString(SAVED_INSTANCE_URI));
            scanResults.setText(savedInstanceState.getString(SAVED_INSTANCE_RESULT));
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(MainActivity.this, new
                        String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
            }
        });

        detector = new BarcodeDetector.Builder(getApplicationContext())
                .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                .build();
        if (!detector.isOperational()) {
            scanResults.setText("Could not set up the detector!");
            return;
        }*/

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //init_folder_and_files();
            //init_setting();
            //init_folder_and_files();
            //loadSongs();

        } else {
            if(checkAndRequestPermissions()) {
                // carry on the normal flow, as the case of  permissions  granted.

                //init_folder_and_files();
                //loadSongs();
            }
        }

        Log.d(TAG, "isLogin = "+isLogin);

        if (!isLogin) {
            Fragment fragment = null;
            Class fragmentClass;
            fragmentClass = LoginFragment.class;

            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            //fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commitAllowingStateLoss();

        }


        IntentFilter filter;

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction() != null) {
                    if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SETTING_PDA_TYPE_ACTION)) {
                        Log.d(TAG, "receive ACTION_SETTING_PDA_TYPE_ACTION !");

                        String ret = intent.getStringExtra("MODEL_TYPE");
                        pda_type = Integer.valueOf(ret);

                        editor = pref.edit();
                        editor.putInt("PDA_TYPE", pda_type);
                        editor.apply();

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SETTING_WEB_SOAP_PORT_ACTION)) {
                        Log.d(TAG, "receive ACTION_SETTING_WEB_SOAP_PORT_ACTION !");

                        web_soap_port = intent.getStringExtra("WEB_SOAP_PORT");
                        Log.e(TAG, "web_soap_port = "+web_soap_port);

                        editor = pref.edit();
                        editor.putString("WEB_SOAP_PORT", web_soap_port);
                        editor.apply();

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_LOGIN_SUCCESS)) {
                        Log.d(TAG, "receive brocast !");

                        isLogin = true;

                        emp_no = intent.getStringExtra("ACCOUNT");

                        TextView empID = findViewById(R.id.empId);
                        //TextView password = findViewById(R.id.empName);

                        empID.setText(emp_no);
                        //password.setText(intent.getStringExtra("PASSWORD"));


                        Fragment fragment = null;
                        Class fragmentClass=null;
                        fragmentClass = AllocationMsgFragment.class;

                        try {
                            fragment = (Fragment) fragmentClass.newInstance();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        FragmentManager fragmentManager = getSupportFragmentManager();
                        //fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commitAllowingStateLoss();
                        // Highlight the selected item has been done by NavigationView
                        //menuItem.setChecked(true);
                        // Set action bar title
                        //setTitle(menuItem.getTitle());
                        // Close the navigation drawer
                        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        //drawer.closeDrawer(GravityCompat.START);
                        if (menuItemLogin != null && menuItemLogout != null) {
                            receiving_main.setVisible(true);
                            receiving_record.setVisible(true);
                            receiving_board.setVisible(true);
                            receiving_multi.setVisible(true);

                            //menuItemReceiveGoods.setVisible(true);
                            //menuItemShipment.setVisible(true);
                            menuItemAllocation.setVisible(true);
                            menuItemEnteringWareHouse.setVisible(true);
                            //menuItemProductionStorage.setVisible(true);
                            //menuItemReceivingInspection.setVisible(true);

                            menuItemLogin.setVisible(false);
                            menuItemLogout.setVisible(true);
                        }

                        //init scan
                        //Intent scanIntent = new Intent();
                        //scanIntent.setAction("unitech.scanservice.scan2key_setting");
                        //scanIntent.putExtra("scan2key", false);
                        //sendBroadcast(scanIntent);
                        setTitle(getResources().getString(R.string.action_allocation_msg));

                        View view = getCurrentFocus();
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_LOGOUT_ACTION)) {
                        Log.d(TAG, "receive ACTION_LOGIN_SUCCESS!");

                        Fragment fragment = null;
                        Class fragmentClass;

                        if (menuItemLogin != null && menuItemLogout != null) {
                            //menuItemReceiveGoods.setVisible(false);
                            //menuItemShipment.setVisible(false);
                            menuItemAllocation.setVisible(false);
                            menuItemEnteringWareHouse.setVisible(false);
                            //menuItemProductionStorage.setVisible(false);
                            //menuItemReceivingInspection.setVisible(false);

                            menuItemLogin.setVisible(true);
                            menuItemLogout.setVisible(false);
                        }
                        fragmentClass = LoginFragment.class;
                        isLogin = false;

                        entering_warehouse_main.setVisible(false);
                        entering_warehouse_find.setVisible(false);

                        try {
                            fragment = (Fragment) fragmentClass.newInstance();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // Insert the fragment by replacing any existing fragment
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        //fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commitAllowingStateLoss();
                    }


                }

                if("unitech.scanservice.data" .equals(intent.getAction()))
                {
                    Bundle bundle = intent.getExtras();
                    if(bundle != null )
                    {
                        String text = bundle.getString("text");
                        Log.e(TAG, "text = "+text);
                    }
                }
                if("unitech.scanservice.datatype" .equals(intent.getAction()))
                {
                    Bundle bundle = intent.getExtras();
                    if(bundle != null )
                    {
                        int type = bundle.getInt("text");

                        Log.e(TAG, "type = "+type);

                    }
                }

            }
        };

        if (!isRegister) {
            filter = new IntentFilter();
            filter.addAction(Constants.ACTION.ACTION_LOGIN_FAIL);
            filter.addAction(Constants.ACTION.ACTION_LOGIN_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_LOGOUT_ACTION);
            filter.addAction(Constants.ACTION.ACTION_SETTING_PDA_TYPE_ACTION);
            filter.addAction((Constants.ACTION.ACTION_SETTING_WEB_SOAP_PORT_ACTION));
            filter.addAction("unitech.scanservice.data");
            filter.addAction("unitech.scanservice.datatype");
            context.registerReceiver(mReceiver, filter);
            isRegister = true;
            Log.d(TAG, "registerReceiver mReceiver");
        }
    }

    @Override
    protected void onDestroy() {
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

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        android.app.AlertDialog.Builder confirmdialog = new android.app.AlertDialog.Builder(MainActivity.this);
        confirmdialog.setIcon(R.drawable.baseline_exit_to_app_black_48);
        confirmdialog.setTitle("Exit App");
        confirmdialog.setMessage("Exit this App?");
        confirmdialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }

                finish();
            }
        });
        confirmdialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // btnScan.setVisibility(View.VISIBLE);
                // btnConfirm.setVisibility(View.GONE);

            }
        });
        confirmdialog.show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        Log.e(TAG, "onCreateOptionsMenu");

        getMenuInflater().inflate(R.menu.main, menu);

        setting = menu.findItem(R.id.action_settings);



        receiving_main = menu.findItem(R.id.action_receiving_main);
        receiving_record = menu.findItem(R.id.action_receiving_record);
        receiving_board = menu.findItem(R.id.action_receiving_board);
        receiving_multi = menu.findItem(R.id.action_receiving_multi);

        shipment_main = menu.findItem(R.id.action_shipping_main);
        shipment_find = menu.findItem(R.id.action_shipping_find);

        allocation_find = menu.findItem(R.id.action_allocation_find);
        allocation_replenishment = menu.findItem(R.id.action_allocation_replenishment);
        allocation_send_msg = menu.findItem(R.id.action_allocation_send_msg_to_reserve);
        allocation_msg = menu.findItem(R.id.action_allocation_msg);
        allocation_area_confirm = menu.findItem(R.id.action_allocation_area_confirm);
        allocation_direct = menu.findItem(R.id.action_allocation_direct);

        entering_warehouse_main = menu.findItem(R.id.action_entering_warehouse_main);
        entering_warehouse_find = menu.findItem(R.id.action_entering_warehouse_find);

        production_storage_main = menu.findItem(R.id.action_production_storage_main);
        production_storage_find = menu.findItem(R.id.action_production_storage_find);
        production_storage_scan = menu.findItem(R.id.action_production_storage_scan);


        if (isLogin) {
            setting.setVisible(false);

            receiving_main.setVisible(true);
            receiving_record.setVisible(true);
            receiving_board.setVisible(true);
            receiving_multi.setVisible(true);

            /*shipment_main.setVisible(false);
            shipment_find.setVisible(false);

            allocation_find.setVisible(false);
            allocation_replenishment.setVisible(false);
            allocation_send_msg.setVisible(false);
            allocation_msg.setVisible(false);
            allocation_area_confirm.setVisible(true);
            allocation_direct.setVisible(true);

            entering_warehouse_main.setVisible(true);
            entering_warehouse_find.setVisible(true);

            production_storage_main.setVisible(true);
            production_storage_find.setVisible(true);
            production_storage_scan.setVisible(true);*/

            menuItemLogin.setVisible(false);
            menuItemLogout.setVisible(true);
            //menuItemReceiveGoods.setVisible(true);
            //menuItemShipment.setVisible(true);
            menuItemAllocation.setVisible(true);
            menuItemEnteringWareHouse.setVisible(true);
            //menuItemReceivingInspection.setVisible(true);
            //menuItemProductionStorage.setVisible(true);
        } else {
            setting.setVisible(false);

            receiving_main.setVisible(false);
            receiving_record.setVisible(false);
            receiving_board.setVisible(false);
            receiving_multi.setVisible(false);

            shipment_main.setVisible(false);
            shipment_find.setVisible(false);

            allocation_find.setVisible(false);
            allocation_replenishment.setVisible(false);
            allocation_send_msg.setVisible(false);
            allocation_msg.setVisible(false);
            allocation_area_confirm.setVisible(false);
            allocation_direct.setVisible(false);

            entering_warehouse_main.setVisible(false);
            entering_warehouse_find.setVisible(false);

            production_storage_main.setVisible(false);
            production_storage_find.setVisible(false);
            production_storage_scan.setVisible(false);

            menuItemLogin.setVisible(true);
            menuItemLogout.setVisible(false);
            //menuItemReceiveGoods.setVisible(false);
            //menuItemShipment.setVisible(false);
            menuItemAllocation.setVisible(false);
            menuItemEnteringWareHouse.setVisible(false);
            //menuItemReceivingInspection.setVisible(false);
            //menuItemProductionStorage.setVisible(false);
        }
        /*setting.setVisible(false);

        receiving_main.setVisible(false);
        receiving_record.setVisible(false);
        receiving_board.setVisible(false);
        receiving_multi.setVisible(false);

        shipment_main.setVisible(false);
        shipment_find.setVisible(false);

        allocation_find.setVisible(false);
        allocation_replenishment.setVisible(false);
        allocation_send_msg.setVisible(false);
        allocation_msg.setVisible(false);
        allocation_area_confirm.setVisible(false);
        allocation_direct.setVisible(false);

        entering_warehouse_main.setVisible(false);
        entering_warehouse_find.setVisible(false);

        production_storage_main.setVisible(false);
        production_storage_find.setVisible(false);
        production_storage_scan.setVisible(false);*/


        //menuItemLogin = menu.findItem(R.id.nav_login);
        //menuItemLogout = menu.findItem(R.id.nav_logout);
        /*menuItemLogout.setVisible(false);
        menuItemReceiveGoods.setVisible(false);
        menuItemShipment.setVisible(false);
        menuItemAllocation.setVisible(false);
        menuItemEnteringWareHouse.setVisible(false);
        menuItemReceivingInspection.setVisible(false);
        menuItemProductionStorage.setVisible(false);*/

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        setTitle(item.getTitle());
        //noinspection SimplifiableIfStatement
        Fragment fragment = null;
        Class fragmentClass=null;

        switch (id) {
            case R.id.action_settings:
                //hide virtual keoboard
                Intent getSuccessIntent = new Intent(Constants.ACTION.ACTION_SCAN_RESET);
                sendBroadcast(getSuccessIntent);
                break;
            case R.id.action_shipping_find:
            case R.id.action_allocation_find:
            case R.id.action_entering_warehouse_find:
            case R.id.action_production_storage_find:
                fragmentClass = LookupInStockFragment.class;
                break;
            case R.id.action_receiving_main:
                fragmentClass = ReceivingFragment.class;
                break;
            case R.id.action_receiving_record:
                fragmentClass = ReceivingRecordFragment.class;
                break;
            case R.id.action_receiving_board:
                fragmentClass = ReceivingBoardFragment.class;
                break;
            case R.id.action_receiving_multi:
                fragmentClass = ReceivingMultiFragment.class;
                break;
            case R.id.action_shipping_main:
                fragmentClass = ShipmentFragment.class;
                break;
            case R.id.action_allocation_replenishment:
                fragmentClass = AllocationReplenishmentFragment.class;
                break;
            case R.id.action_allocation_send_msg_to_reserve:
                fragmentClass = AllocationSendMsgToReserveWarehouseFragment.class;
                break;
            case R.id.action_allocation_msg:
                fragmentClass = AllocationMsgFragment.class;
                break;
            case R.id.action_allocation_area_confirm:
                fragmentClass = AllocationAreaConfirmFragment.class;
                break;
            case R.id.action_allocation_direct:
                fragmentClass = AllocationDirectFragment.class;
                break;
            case R.id.action_entering_warehouse_main:
                fragmentClass = EnteringWarehouseFragmnet.class;
                break;
            case R.id.action_production_storage_main:
                fragmentClass = ProductionStorageFragment.class;
                break;
            case R.id.action_production_storage_scan:
                fragmentClass = ProductionFeedingScanFragment.class;
                break;
        }

        if (fragmentClass != null) {
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            //fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commitAllowingStateLoss();
        }



        /*if (id == R.id.action_settings) {

            Intent getSuccessIntent = new Intent(Constants.ACTION.ACTION_SCAN_RESET);
            sendBroadcast(getSuccessIntent);
        }*/
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        selectDrawerItem(item);

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(GravityCompat.START);




        return true;
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass=null;

        String title="";
        if (imm.isAcceptingText())
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        //initializing the fragment object which is selected
        switch (menuItem.getItemId()) {

            /*case R.id.nav_receiving:
                fragmentClass = ReceivingFragment.class;
                title = getResources().getString(R.string.action_receiving_main);
                receiving_main.setVisible(true);
                receiving_record.setVisible(true);
                receiving_board.setVisible(true);
                receiving_multi.setVisible(true);
                shipment_main.setVisible(false);
                shipment_find.setVisible(false);
                allocation_find.setVisible(false);
                allocation_replenishment.setVisible(false);
                allocation_send_msg.setVisible(false);
                allocation_msg.setVisible(false);
                allocation_area_confirm.setVisible(false);
                allocation_direct.setVisible(false);
                entering_warehouse_main.setVisible(false);
                entering_warehouse_find.setVisible(false);
                production_storage_main.setVisible(false);
                production_storage_find.setVisible(false);
                production_storage_scan.setVisible(false);

                break;
            case R.id.nav_shipment:
                fragmentClass = ShipmentFragment.class;
                title = getResources().getString(R.string.action_shipment_main);
                receiving_main.setVisible(false);
                receiving_record.setVisible(false);
                receiving_board.setVisible(false);
                receiving_multi.setVisible(false);
                shipment_main.setVisible(true);
                shipment_find.setVisible(true);
                allocation_find.setVisible(false);
                allocation_replenishment.setVisible(false);
                allocation_send_msg.setVisible(false);
                allocation_msg.setVisible(false);
                allocation_area_confirm.setVisible(false);
                allocation_direct.setVisible(false);
                entering_warehouse_main.setVisible(false);
                entering_warehouse_find.setVisible(false);
                production_storage_main.setVisible(false);
                production_storage_find.setVisible(false);
                production_storage_scan.setVisible(false);

                break;*/

            case R.id.nav_allocation:
                fragmentClass = AllocationMsgFragment.class;
                title = getResources().getString(R.string.action_allocation_msg);
                receiving_main.setVisible(false);
                receiving_record.setVisible(false);
                receiving_board.setVisible(false);
                receiving_multi.setVisible(false);
                shipment_main.setVisible(false);
                shipment_find.setVisible(false);
                allocation_find.setVisible(true);
                allocation_replenishment.setVisible(true);
                allocation_send_msg.setVisible(true);
                allocation_msg.setVisible(true);
                allocation_area_confirm.setVisible(true);
                allocation_direct.setVisible(true);
                entering_warehouse_main.setVisible(false);
                entering_warehouse_find.setVisible(false);
                production_storage_main.setVisible(false);
                production_storage_find.setVisible(false);
                production_storage_scan.setVisible(false);

                break;
            case R.id.nav_entering_warehouse:
                fragmentClass = EnteringWarehouseFragmnet.class;
                title = getResources().getString(R.string.action_entering_warehouse_main);
                receiving_main.setVisible(false);
                receiving_record.setVisible(false);
                receiving_board.setVisible(false);
                receiving_multi.setVisible(false);
                shipment_main.setVisible(false);
                shipment_find.setVisible(false);
                allocation_find.setVisible(false);
                allocation_replenishment.setVisible(false);
                allocation_send_msg.setVisible(false);
                allocation_msg.setVisible(false);
                allocation_area_confirm.setVisible(false);
                allocation_direct.setVisible(false);
                entering_warehouse_main.setVisible(true);
                entering_warehouse_find.setVisible(true);
                production_storage_main.setVisible(false);
                production_storage_find.setVisible(false);
                production_storage_scan.setVisible(false);

                break;
            /*case R.id.nav_production_storage:
                fragmentClass = ProductionStorageFragment.class;
                title = getResources().getString(R.string.action_production_storage_main);
                receiving_main.setVisible(false);
                receiving_record.setVisible(false);
                receiving_board.setVisible(false);
                receiving_multi.setVisible(false);
                shipment_main.setVisible(false);
                shipment_find.setVisible(false);
                allocation_find.setVisible(false);
                allocation_replenishment.setVisible(false);
                allocation_send_msg.setVisible(false);
                allocation_msg.setVisible(false);
                allocation_area_confirm.setVisible(false);
                allocation_direct.setVisible(false);
                entering_warehouse_main.setVisible(false);
                entering_warehouse_find.setVisible(false);
                production_storage_main.setVisible(true);
                production_storage_find.setVisible(true);
                production_storage_scan.setVisible(true);

                break;
            case R.id.nav_receiving_inspection:
                fragmentClass = ReceivingInspectionFragment.class;
                title = getResources().getString(R.string.action_receiving_inspection_main);
                receiving_main.setVisible(false);
                receiving_record.setVisible(false);
                receiving_board.setVisible(false);
                receiving_multi.setVisible(false);
                shipment_main.setVisible(false);
                shipment_find.setVisible(false);
                allocation_find.setVisible(false);
                allocation_replenishment.setVisible(false);
                allocation_send_msg.setVisible(false);
                allocation_msg.setVisible(false);
                allocation_area_confirm.setVisible(false);
                allocation_direct.setVisible(false);
                entering_warehouse_main.setVisible(false);
                entering_warehouse_find.setVisible(false);
                production_storage_main.setVisible(false);
                production_storage_find.setVisible(false);
                production_storage_scan.setVisible(false);

                break;*/
            case R.id.nav_setting:
                fragmentClass = SettingFragment.class;

                receiving_main.setVisible(false);
                receiving_record.setVisible(false);
                receiving_board.setVisible(false);
                receiving_multi.setVisible(false);
                shipment_main.setVisible(false);
                shipment_find.setVisible(false);
                allocation_find.setVisible(false);
                allocation_replenishment.setVisible(false);
                allocation_send_msg.setVisible(false);
                allocation_msg.setVisible(false);
                allocation_area_confirm.setVisible(false);
                allocation_direct.setVisible(false);
                entering_warehouse_main.setVisible(false);
                entering_warehouse_find.setVisible(false);
                production_storage_main.setVisible(false);
                production_storage_find.setVisible(false);
                production_storage_scan.setVisible(false);


                break;
            case R.id.nav_login:
                fragmentClass = LoginFragment.class;

                receiving_main.setVisible(false);
                receiving_record.setVisible(false);
                receiving_board.setVisible(false);
                receiving_multi.setVisible(false);
                shipment_main.setVisible(false);
                shipment_find.setVisible(false);
                allocation_find.setVisible(false);
                allocation_replenishment.setVisible(false);
                allocation_send_msg.setVisible(false);
                allocation_msg.setVisible(false);
                allocation_area_confirm.setVisible(false);
                allocation_direct.setVisible(false);
                entering_warehouse_main.setVisible(false);
                entering_warehouse_find.setVisible(false);
                production_storage_main.setVisible(false);
                production_storage_find.setVisible(false);
                production_storage_scan.setVisible(false);

                break;
            case R.id.nav_logout:
                fragmentClass = LogoutFragment.class;

                receiving_main.setVisible(false);
                receiving_record.setVisible(false);
                receiving_board.setVisible(false);
                receiving_multi.setVisible(false);
                shipment_main.setVisible(false);
                shipment_find.setVisible(false);
                allocation_find.setVisible(false);
                allocation_replenishment.setVisible(false);
                allocation_send_msg.setVisible(false);
                allocation_msg.setVisible(false);
                allocation_area_confirm.setVisible(false);
                allocation_direct.setVisible(false);
                entering_warehouse_main.setVisible(false);
                entering_warehouse_find.setVisible(false);
                production_storage_main.setVisible(false);
                production_storage_find.setVisible(false);
                production_storage_scan.setVisible(false);

                /*if (menuItemLogin != null && menuItemLogout != null) {
                    menuItemReceiveGoods.setVisible(false);
                    menuItemShipment.setVisible(false);
                    menuItemAllocation.setVisible(false);
                    menuItemEnteringWareHouse.setVisible(false);
                    menuItemProductionStorage.setVisible(false);
                    menuItemReceivingInspection.setVisible(false);

                    menuItemLogin.setVisible(true);
                    menuItemLogout.setVisible(false);
                }
                fragmentClass = LoginFragment.class;
                isLogin = false;

                entering_warehouse_main.setVisible(false);
                entering_warehouse_find.setVisible(false);*/

                break;

            default:
                fragmentClass = LoginFragment.class;

                receiving_main.setVisible(false);
                receiving_record.setVisible(false);
                receiving_board.setVisible(false);
                receiving_multi.setVisible(false);
                shipment_main.setVisible(false);
                shipment_find.setVisible(false);
                allocation_find.setVisible(false);
                allocation_replenishment.setVisible(false);
                allocation_send_msg.setVisible(false);
                allocation_msg.setVisible(false);
                allocation_area_confirm.setVisible(false);
                allocation_direct.setVisible(false);
                entering_warehouse_main.setVisible(false);
                entering_warehouse_find.setVisible(false);
                production_storage_main.setVisible(false);
                production_storage_find.setVisible(false);
                production_storage_scan.setVisible(false);

                break;


        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        //fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commitAllowingStateLoss();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        if (title.length() > 0)
            setTitle(title);
        else
            setTitle(menuItem.getTitle());
        // Close the navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

    private void InitView() {
        /*FragmentTabHost mTabHost;

        mTabHost =  findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        //mTabHost.addTab(setIndicator(MainMenu.this, mTabHost.newTabSpec(TAB_1_TAG),
        //        R.drawable.tab_indicator_gen, getResources().getString(R.string.scm_history_tab), R.drawable.ic_history_white_48dp), HistoryFragment.class, null);
        mTabHost.addTab(setIndicator(MainMenu.this, mTabHost.newTabSpec(TAB_1_TAG),
                R.drawable.tab_indicator_gen, R.drawable.mail), HistoryFragment.class, null);




        //mTabHost.addTab(setIndicator(MainMenu.this, mTabHost.newTabSpec(TAB_2_TAG),
        //        R.drawable.tab_indicator_gen, getResources().getString(R.string.scm_setting), R.drawable.ic_settings_white_48dp), SettingsFragment.class, null);
        mTabHost.addTab(setIndicator(MainMenu.this, mTabHost.newTabSpec(TAB_2_TAG),
                R.drawable.tab_indicator_gen,  R.drawable.gear), SettingsFragment.class, null);






        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {


                switch (tabId) {
                    case "tab_1":
                        //if (item_clear != null)
                        //    item_clear.setVisible(true);
                        if (item_search != null)
                            item_search.setVisible(true);
                        break;
                    case "tab_2":
                        //if (item_clear != null)
                        //    item_clear.setVisible(false);
                        if (item_search != null)
                            item_search.setVisible(false);
                        break;

                    default:
                        break;

                }
            }
        });*/
    }

    private  boolean checkAndRequestPermissions() {

        //int accessNetworkStatePermission = ContextCompat.checkSelfPermission(this,
        //        Manifest.permission.ACCESS_NETWORK_STATE);

        //int accessWiFiStatePermission = ContextCompat.checkSelfPermission(this,
        //        Manifest.permission.ACCESS_WIFI_STATE);

        //int readPermission = ContextCompat.checkSelfPermission(this,
        //        Manifest.permission.READ_EXTERNAL_STORAGE);
        //int writePermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int networkPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET);

        //int cameraPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);

        List<String> listPermissionsNeeded = new ArrayList<>();

        /*f (readPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }*/

        if (networkPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.INTERNET);
        }

        /*if (accessNetworkStatePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_NETWORK_STATE);
        }

        if (accessWiFiStatePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_WIFI_STATE);
        }*/
        //if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
        //    listPermissionsNeeded.add(android.Manifest.permission.WRITE_CALENDAR);
        //}
        //if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
        //    listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        //}

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        //Log.e(TAG, "result size = "+grantResults.length+ "result[0] = "+grantResults[0]+", result[1] = "+grantResults[1]);


        /*switch (requestCode) {
            case 200: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.


                    Log.i(TAG, "WRITE_CALENDAR permissions granted");
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.i(TAG, "READ_CONTACTS permissions denied");

                    RetryDialog();
                }
            }
            break;

            // other 'case' lines to check for other
            // permissions this app might request
        }*/
        Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                //perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                //perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.INTERNET, PackageManager.PERMISSION_GRANTED);
                //perms.put(Manifest.permission.ACCESS_NETWORK_STATE, PackageManager.PERMISSION_GRANTED);
                //perms.put(Manifest.permission.ACCESS_WIFI_STATE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (//perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        //    && perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                             perms.get(Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
                            //&& perms.get(Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED &&
                            //perms.get(Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED
                            )

                    {
                        Log.d(TAG, "write permission granted");

                        // process the normal flow
                        //else any one or both the permissions are not granted
                        init_folder_and_files();
                        //init_setting();
                    } else {
                        Log.d(TAG, "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (//ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            //    || ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.INTERNET )
                                //|| ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_NETWORK_STATE )
                                //|| ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_WIFI_STATE )
                                ) {
                            showDialogOK("Warning",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    finish();
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new android.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("Ok", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }


}