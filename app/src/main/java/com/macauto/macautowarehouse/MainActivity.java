package com.macauto.macautowarehouse;

import android.Manifest;
import android.app.Activity;
import android.app.SearchManager;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;


import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

//import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v4.content.ContextCompat;

import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.util.Log;

import android.view.Gravity;
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

import android.widget.TextView;
import android.widget.Toast;

import com.macauto.macautowarehouse.data.AllocationMsgDetailItem;
import com.macauto.macautowarehouse.data.AllocationMsgItem;
import com.macauto.macautowarehouse.data.AllocationMsgStatusItem;
import com.macauto.macautowarehouse.data.AllocationSendMsgDetailItem;
import com.macauto.macautowarehouse.data.AllocationSendMsgItem;
import com.macauto.macautowarehouse.data.Constants;
import com.macauto.macautowarehouse.data.DividedItem;
import com.macauto.macautowarehouse.data.InspectedDetailItem;
import com.macauto.macautowarehouse.data.InspectedReceiveItem;
import com.macauto.macautowarehouse.data.ProductionStorageDetailItem;
import com.macauto.macautowarehouse.data.ProductionStorageItem;

import com.macauto.macautowarehouse.data.SearchDetailItem;
import com.macauto.macautowarehouse.data.SearchItem;
import com.macauto.macautowarehouse.table.DataTable;
import com.qs408.aidl.IQSService;

import java.io.File;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.macauto.macautowarehouse.data.FileOperation.init_folder_and_files;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getName();

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    static SharedPreferences pref ;
    static SharedPreferences.Editor editor;
    private static final String FILE_NAME = "Preference";

    //private static final String LOG_TAG = "Barcode Scanner API";
    //private static final int PHOTO_REQUEST = 10;
    //private TextView scanResults;
    //private BarcodeDetector detector;
    //private Uri imageUri;
    //private static final int REQUEST_WRITE_PERMISSION = 20;
    //private static final String SAVED_INSTANCE_URI = "uri";
    //private static final String SAVED_INSTANCE_RESULT = "result";
    private Context context;

    private static BroadcastReceiver mReceiver = null;
    private static boolean isRegister = false;

    //private MenuItem menuItemReceiveGoods;
    //private MenuItem menuItemShipment;
    private MenuItem menuItemSearch;
    private MenuItem menuItemAllocation;
    private MenuItem menuItemAllocationSendMsg;
    private MenuItem menuItemEnteringWareHouse;
    private MenuItem menuItemProductionStorage;
    //private MenuItem menuItemReceivingInspection;
    private MenuItem menuItemPrintTag;
    private MenuItem menuItemLogin;
    private MenuItem menuItemLogout;

    public static boolean isLogin = false;

    //private MenuItem setting;

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
    private MenuItem keyboard;
    private MenuItem searchFilter;

    private MenuItem getLocateNo;
    private MenuItem getMsg;

    public static int pda_type;
    private InputMethodManager imm;
    public static String k_id;
    public static String web_soap_port;
    public static String service_ip;
    public static String global_sid;
    public static String emp_no;

    //for search fragment
    public static ArrayList<SearchItem> searchList = new ArrayList<>();
    public static ArrayList<SearchItem> sortedSearchList = new ArrayList<>();
    public static DataTable lookUpDataTable;
    public static ArrayList<SearchDetailItem> lookUpDetailList = new ArrayList<>();
    //allocation send msg
    public static ArrayList<String> locateList = new ArrayList<>();
    public static ArrayList<String> dateList = new ArrayList<>();
    public static ArrayList<String> hourList = new ArrayList<>();
    public static ArrayList<String> minList = new ArrayList<>();
    public static DataTable locateNoTable = new DataTable();
    public static DataTable madeInfoTable = new DataTable();
    public static DataTable sfaMessTable = new DataTable();
    public static DataTable hhh = new DataTable();
    public static ArrayList<AllocationSendMsgItem> myList = new ArrayList<>();
    public static ArrayList<AllocationMsgStatusItem> statusList = new ArrayList<>();
    public static ArrayList<AllocationSendMsgDetailItem> sendMsgDetailList = new ArrayList<>();
    //msg fragment
    public static ArrayList<AllocationMsgItem> msg_list = new ArrayList<>();
    public static DataTable msgDataTable;
    public static DataTable dataTable_SSS;
    public static ArrayList<AllocationMsgDetailItem> msgDetailShowList = new ArrayList<>();
    public static ArrayList<InspectedDetailItem> msgDetailOfDetailList = new ArrayList<>();
    //entering warehouse
    public static ArrayList<InspectedReceiveItem> scan_list = new ArrayList<>();
    public static ArrayList<String> pp_list = new ArrayList<>();
    public static HashMap<String, Integer> total_count_list = new HashMap<>();
    public static DataTable dataTable;
    public static DataTable table_X_M;
    public static DataTable dataTable_Batch_area;
    public static ArrayList<DividedItem> dividedList = new ArrayList<>();
    public static ArrayList<Integer> temp_count_list = new ArrayList<>();
    public static ArrayList<InspectedDetailItem> wareHouseDetailList = new ArrayList<>();
    //production storage
    public static DataTable dataTable_RR;
    public static DataTable product_table_X_M;
    public static ArrayList<ProductionStorageItem> productList = new ArrayList<>();
    public static ArrayList<ProductionStorageDetailItem> ProductionDetailList = new ArrayList<>();
    //for print
    public static ArrayList<String> printArray = new ArrayList<>();

    //write log
    //public static Process process;
    //public static String log_filename;

    private IQSService iqspda;
    FloatingActionButton fabBack;
    FloatingActionButton fabPrint;
    private Intent pda_408_intent;
    private boolean pda_408_intent_bind = false;

    public static int screen_width;
    public static int screen_height;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate");

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screen_height = displayMetrics.heightPixels;
        screen_width = displayMetrics.widthPixels;

        Log.e(TAG, "width = "+screen_width+", height = "+screen_height);

        //create a new kid
        //GenerateRandomString rString = new GenerateRandomString();
        //k_id = rString.randomString(32);
        //Log.e(TAG, "session_id = "+k_id);

        //get default pda type
        pref = getSharedPreferences(FILE_NAME, MODE_PRIVATE);

        //String account = pref.getString("PDA_TYPE", "");
        pda_type = pref.getInt("PDA_TYPE", 0);
        web_soap_port = pref.getString("WEB_SOAP_PORT", "8000");
        service_ip = pref.getString("SERVICE_IP", "172.17.17.244");
        global_sid = pref.getString("GLOBAL_SID", "MAT");
        context = getApplicationContext();

        /*Log.e(TAG, "=== start log ===");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        log_filename = "logcat_"+currentDateandTime+".txt";
        File outputFile = new File(context.getExternalCacheDir(),log_filename);
        try {
            //process = Runtime.getRuntime().exec("logcat -d -f " + outputFile.getAbsolutePath());
            process = Runtime.getRuntime().exec("logcat -c");
            process = Runtime.getRuntime().exec("logcat -f " + outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get virtual keyboard
        imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

        fabBack = findViewById(R.id.fabBack);
        fabPrint = findViewById(R.id.fabPrint);
        fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //fabBack.setVisibility(View.GONE);
                //fabPrint.setVisibility(View.GONE);

                fabBack.hide();
                fabPrint.hide();

                Intent genIntent = new Intent();
                genIntent.setAction(Constants.ACTION.ACTION_PICKING_CHANGE_HIDE_GENERATE);
                sendBroadcast(genIntent);
            }
        });
        fabPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = takeScreenshot();
                if (bitmap != null) {
                    try {
                        iqspda.printBitmap(1, bitmap);

                        //sendCmd(new byte[]{0x1d,0x0c}); //for MAT old machine
                        sendCmd(new byte[] { 0x1b, 0x61, 0x01 }); //for MAC 20190319 new machine

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        menuItemLogin = navigationView.getMenu().findItem(R.id.nav_login);
        menuItemLogout = navigationView.getMenu().findItem(R.id.nav_logout);
        //menuItemReceiveGoods = navigationView.getMenu().findItem(R.id.nav_receiving);
        //menuItemShipment = navigationView.getMenu().findItem(R.id.nav_shipment);
        menuItemSearch = navigationView.getMenu().findItem(R.id.nav_search);
        menuItemAllocation = navigationView.getMenu().findItem(R.id.nav_allocation);
        menuItemAllocationSendMsg = navigationView.getMenu().findItem(R.id.nav_allocation_send_msg);
        menuItemEnteringWareHouse = navigationView.getMenu().findItem(R.id.nav_entering_warehouse);
        //menuItemReceivingInspection = navigationView.getMenu().findItem(R.id.nav_receiving_inspection);
        menuItemProductionStorage = navigationView.getMenu().findItem(R.id.nav_production_storage);
        menuItemPrintTag = navigationView.getMenu().findItem(R.id.nav_print_tag);



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

        //permission

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndRequestPermissions();
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

                        if (pda_type == 2 && isLogin) { //pda408
                            menuItemPrintTag.setVisible(true);

                            if (pda_408_intent == null && !pda_408_intent_bind) { //pda408
                                Log.e(TAG, "pda408 start service");
                                try {
                                    Intent pda_intent = new Intent("COM.QS.DEMO.QSSERVICE");
                                    pda_408_intent = new Intent(getExplicitIntent(context, pda_intent));
                                    startService(pda_408_intent);
                                    bindService(pda_408_intent, conn, Service.BIND_AUTO_CREATE);
                                    pda_408_intent_bind = true;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        } else {
                            Log.e(TAG, "Not pda408, stop service");
                            menuItemPrintTag.setVisible(false);

                            try {
                                unbindService(conn);
                                stopService(pda_408_intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            pda_408_intent = null;
                            pda_408_intent_bind = false;
                        }






                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SETTING_WEB_SOAP_PORT_ACTION)) {
                        Log.d(TAG, "receive ACTION_SETTING_WEB_SOAP_PORT_ACTION !");

                        web_soap_port = intent.getStringExtra("WEB_SOAP_PORT");
                        Log.e(TAG, "web_soap_port = "+web_soap_port);

                        editor = pref.edit();
                        editor.putString("WEB_SOAP_PORT", web_soap_port);
                        editor.apply();

                    }  else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SETTING_WEB_SERVICE_IP_ACTION)) {
                        Log.d(TAG, "receive ACTION_SETTING_WEB_SERVICE_IP_ACTION !");

                        service_ip = intent.getStringExtra("SERVICE_IP");
                        global_sid = intent.getStringExtra("GLOBAL_SID");
                        Log.e(TAG, "service_ip = "+service_ip+", sid = "+global_sid);

                        editor = pref.edit();
                        editor.putString("SERVICE_IP", service_ip);
                        editor.putString("GLOBAL_SID", global_sid);
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
                        Class fragmentClass = LookupInStockFragment.class;
                        //fragmentClass = LookupInStockFragment.class;

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

                            //menuItemReceiveGoods.setVisible(true);

                            menuItemSearch.setVisible(true);
                            menuItemAllocation.setVisible(true);
                            menuItemAllocationSendMsg.setVisible(true);
                            menuItemEnteringWareHouse.setVisible(true);
                            menuItemProductionStorage.setVisible(true);
                            if (pda_type == 2)
                                menuItemPrintTag.setVisible(true);
                            else
                                menuItemPrintTag.setVisible(false);
                            //menuItemShipment.setVisible(true);
                            //menuItemReceivingInspection.setVisible(true);

                            menuItemLogin.setVisible(false);
                            menuItemLogout.setVisible(true);
                        }

                        //init scan
                        //Intent scanIntent = new Intent();
                        //scanIntent.setAction("unitech.scanservice.scan2key_setting");
                        //scanIntent.putExtra("scan2key", false);
                        //sendBroadcast(scanIntent);
                        setTitle(getResources().getString(R.string.action_allocation_find));

                        View view = getCurrentFocus();
                        try {
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_LOGOUT_ACTION)) {
                        Log.d(TAG, "receive ACTION_LOGOUT_ACTION!");

                        TextView empID = findViewById(R.id.empId);
                        //TextView password = findViewById(R.id.empName);

                        empID.setText("");

                        Fragment fragment = null;
                        Class fragmentClass;

                        if (menuItemLogin != null && menuItemLogout != null) {
                            //menuItemReceiveGoods.setVisible(false);

                            menuItemSearch.setVisible(false);
                            menuItemAllocation.setVisible(false);
                            menuItemAllocationSendMsg.setVisible(false);
                            menuItemEnteringWareHouse.setVisible(false);
                            menuItemProductionStorage.setVisible(false);
                            menuItemPrintTag.setVisible(false);
                            //menuItemShipment.setVisible(false);
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

                        setTitle(getResources().getString(R.string.nav_login));
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SEARCH_MENU_SHOW_ACTION)) {
                        Log.d(TAG, "receive ACTION_SEARCH_MENU_SHOW_ACTION!");

                        searchFilter.setVisible(true);
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SEARCH_MENU_HIDE_ACTION)) {
                        Log.d(TAG, "receive ACTION_SEARCH_MENU_HIDE_ACTION!");

                        searchFilter.setVisible(false);
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_RESET_TITLE_PART_IN_STOCK)) {
                        Log.d(TAG, "receive ACTION_RESET_TITLE_PART_IN_STOCK!");

                        setTitle(getResources().getString(R.string.action_allocation_find));
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_MAIN_RESET_TITLE)) {
                        Log.d(TAG, "receive ACTION_MAIN_RESET_TITLE!");
                        String new_title = intent.getStringExtra("NEW_TITLE");

                        setTitle(new_title);
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_PRINT_TEST_HIDE_FAB_BUTTON)) {
                        Log.d(TAG, "receive ACTION_PRINT_TEST_HIDE_FAB_BUTTON!");
                        //fabBack.setVisibility(View.GONE);
                        //fabPrint.setVisibility(View.GONE);

                        fabBack.hide();
                        fabPrint.hide();

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_PRINT_TEST_SHOW_FAB_BUTTON)) {
                        Log.d(TAG, "receive ACTION_PRINT_TEST_SHOW_FAB_BUTTON!");

                        //fabBack.setVisibility(View.VISIBLE);
                        //fabPrint.setVisibility(View.VISIBLE);

                        fabBack.show();
                        fabPrint.show();
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_GET_LOCATE_NO_ICON_SHOW)) {
                        Log.d(TAG, "receive ACTION_GET_LOCATE_NO_ICON_SHOW !");
                        getLocateNo.setVisible(true);

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_GET_LOCATE_NO_ICON_HIDE)) {
                        Log.d(TAG, "receive ACTION_GET_LOCATE_NO_ICON_HIDE !");
                        getLocateNo.setVisible(false);

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_GET_MSG_SYNC_ICON_SHOW)) {
                        Log.d(TAG, "receive ACTION_GET_MSG_SYNC_ICON_SHOW !");
                        getMsg.setVisible(true);

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_GET_MSG_SYNC_ICON_HIDE)) {
                        Log.d(TAG, "receive ACTION_GET_MSG_SYNC_ICON_HIDE !");
                        getMsg.setVisible(false);

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

                if ("com.qs.scancode".equals(intent.getAction()))
                {
                    Bundle bundle = intent.getExtras();
                    if(bundle != null )
                    {
                        String text = bundle.getString("code");
                        Log.e(TAG, "code = "+text);
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
            filter.addAction(Constants.ACTION.ACTION_SETTING_WEB_SOAP_PORT_ACTION);
            filter.addAction(Constants.ACTION.ACTION_SETTING_WEB_SERVICE_IP_ACTION);
            filter.addAction(Constants.ACTION.ACTION_SEARCH_MENU_SHOW_ACTION);
            filter.addAction(Constants.ACTION.ACTION_SEARCH_MENU_HIDE_ACTION);
            filter.addAction(Constants.ACTION.ACTION_RESET_TITLE_PART_IN_STOCK);
            filter.addAction(Constants.ACTION.ACTION_MAIN_RESET_TITLE);
            filter.addAction(Constants.ACTION.ACTION_PRINT_TEST_SHOW_FAB_BUTTON);
            filter.addAction(Constants.ACTION.ACTION_PRINT_TEST_HIDE_FAB_BUTTON);
            filter.addAction(Constants.ACTION.ACTION_GET_LOCATE_NO_ICON_SHOW);
            filter.addAction(Constants.ACTION.ACTION_GET_LOCATE_NO_ICON_HIDE);
            filter.addAction(Constants.ACTION.ACTION_GET_MSG_SYNC_ICON_SHOW);
            filter.addAction(Constants.ACTION.ACTION_GET_MSG_SYNC_ICON_HIDE);
            filter.addAction("unitech.scanservice.data");
            filter.addAction("unitech.scanservice.datatype");
            //pda408
            filter.addAction("com.qs.scancode");
            context.registerReceiver(mReceiver, filter);
            isRegister = true;
            Log.d(TAG, "registerReceiver mReceiver");
        }

        //for pda408
        if (pda_type == 2) {
            try {
                Intent pda_intent = new Intent("COM.QS.DEMO.QSSERVICE");
                pda_408_intent = new Intent(getExplicitIntent(this, pda_intent));
                this.startService(pda_408_intent);
                bindService(pda_408_intent, conn, Service.BIND_AUTO_CREATE);
                pda_408_intent_bind = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            pda_408_intent = null;
            pda_408_intent_bind = false;
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

        //process.destroy();

        if (conn != null && pda_408_intent_bind) {
            unbindService(conn);
        }

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        android.app.AlertDialog.Builder confirmdialog = new android.app.AlertDialog.Builder(MainActivity.this);
        confirmdialog.setIcon(R.drawable.baseline_exit_to_app_black_48);
        confirmdialog.setTitle(getResources().getString(R.string.exit_app_title));
        confirmdialog.setMessage(getResources().getString(R.string.exit_app_msg));
        confirmdialog.setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }

                isLogin = false;

                finish();
            }
        });
        confirmdialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
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

        MenuItem setting = menu.findItem(R.id.action_settings);

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

        keyboard = menu.findItem(R.id.main_hide_or_show_keyboard);
        getLocateNo = menu.findItem(R.id.main_sync_locate_no);
        getMsg = menu.findItem(R.id.main_sync_msg);

        if (isLogin) {
            setting.setVisible(false);

            receiving_main.setVisible(false);
            receiving_record.setVisible(false);
            receiving_board.setVisible(false);
            receiving_multi.setVisible(false);


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

            allocation_send_msg.setVisible(true);
            production_storage_main.setVisible(true);

            menuItemLogin.setVisible(false);
            menuItemLogout.setVisible(true);
            //menuItemReceiveGoods.setVisible(true);

            menuItemSearch.setVisible(true);
            menuItemAllocation.setVisible(true);
            menuItemAllocationSendMsg.setVisible(true);
            menuItemEnteringWareHouse.setVisible(true);
            menuItemProductionStorage.setVisible(true);
            if (pda_type == 2)
                menuItemPrintTag.setVisible(true);
            else
                menuItemPrintTag.setVisible(false);
            //menuItemShipment.setVisible(true);
            //menuItemReceivingInspection.setVisible(true);

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

            menuItemSearch.setVisible(false);
            menuItemAllocation.setVisible(false);
            menuItemAllocationSendMsg.setVisible(false);
            menuItemEnteringWareHouse.setVisible(false);
            menuItemProductionStorage.setVisible(false);
            menuItemPrintTag.setVisible(false);
            //menuItemShipment.setVisible(false);
            //menuItemReceivingInspection.setVisible(false);
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

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        searchFilter = menu.findItem(R.id.action_search);
        //SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        if (searchManager != null) {

            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


            //item_clear = menu.findItem(R.id.action_clear);

            //item_clear.setVisible(false);

            try {
                //SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search_keeper));
                searchView.setOnQueryTextListener(queryListener);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (!item.getTitle().equals(getResources().getString(R.string.hide_keyboard))) {
            setTitle(item.getTitle());
        }


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
            case R.id.main_hide_or_show_keyboard:
                View view = getCurrentFocus();

                if (view != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                break;
            case R.id.main_sync_locate_no:
                Intent getLocateIntent = new Intent(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_LOCATE_NO_RESEND);
                sendBroadcast(getLocateIntent);
                break;
            case R.id.main_sync_msg:
                Intent getMsgIntent = new Intent(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_LIST_RESEND);
                sendBroadcast(getMsgIntent);
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
        //int id = item.getItemId();

        selectDrawerItem(item);

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(GravityCompat.START);




        return true;
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass=null;

        String title="";
        //if (imm.isAcceptingText())
        //    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);


        View view = getCurrentFocus();
        if (view != null)
        {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        //initializing the fragment object which is selected

        //fabBack.setVisibility(View.GONE);
        //fabPrint.setVisibility(View.GONE);

        fabBack.hide();
        fabPrint.hide();

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

                break;*/

            case R.id.nav_search:
                fragmentClass = LookupInStockFragment.class;
                title = getResources().getString(R.string.action_allocation_find);
                searchFilter.setVisible(false);
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
                keyboard .setVisible(true);
                break;

            case R.id.nav_allocation_send_msg:
                fragmentClass = AllocationSendMsgToReserveWarehouseFragment.class;
                title = getResources().getString(R.string.nav_allocation_send);
                searchFilter.setVisible(false);
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
                keyboard.setVisible(true);
                break;

            case R.id.nav_allocation:
                fragmentClass = AllocationMsgFragment.class;
                title = getResources().getString(R.string.action_allocation_msg);
                searchFilter.setVisible(false);
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
                keyboard.setVisible(false);
                break;
            case R.id.nav_entering_warehouse:
                fragmentClass = EnteringWarehouseFragmnet.class;
                title = getResources().getString(R.string.action_entering_warehouse_main);
                searchFilter.setVisible(false);
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
                keyboard.setVisible(false);
                break;
            case R.id.nav_production_storage:
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
                production_storage_main.setVisible(false);
                production_storage_find.setVisible(false);
                production_storage_scan.setVisible(false);
                keyboard.setVisible(true);
                break;
            /*case R.id.nav_shipment:
                fragmentClass = ShipmentFragment.class;
                title = getResources().getString(R.string.action_shipment_main);
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
                keyboard.setVisible(true);
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
                keyboard.setVisible(true);
                break;*/
            case R.id.nav_print_tag:
                Log.d(TAG, "nav_print_tag");
                fragmentClass = PickingChangePrintFragment.class;
                title = getResources().getString(R.string.nav_print_tag);
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
                keyboard.setVisible(true);
                break;

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
                keyboard.setVisible(false);

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
                keyboard.setVisible(true);
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
                keyboard.setVisible(false);
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
                keyboard.setVisible(true);
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }



    private void checkAndRequestPermissions() {

        //int accessNetworkStatePermission = ContextCompat.checkSelfPermission(this,
        //        Manifest.permission.ACCESS_NETWORK_STATE);

        //int accessWiFiStatePermission = ContextCompat.checkSelfPermission(this,
        //        Manifest.permission.ACCESS_WIFI_STATE);

        int readPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int writePermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int networkPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET);

        //int cameraPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (readPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (writePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

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
            //return false;
        }
        //return true;
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
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.INTERNET, PackageManager.PERMISSION_GRANTED);
                //perms.put(Manifest.permission.ACCESS_NETWORK_STATE, PackageManager.PERMISSION_GRANTED);
                //perms.put(Manifest.permission.ACCESS_WIFI_STATE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                             && perms.get(Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
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
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.INTERNET )
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

    final private android.support.v7.widget.SearchView.OnQueryTextListener queryListener = new android.support.v7.widget.SearchView.OnQueryTextListener() {
        //searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            Intent intent;

            //ArrayList<MeetingListItem> list = new ArrayList<>();
            sortedSearchList.clear();
            if (!newText.equals("")) {

                for (int i = 0; i < searchList.size(); i++) {
                    if (searchList.get(i).getItem_IMG01() != null && searchList.get(i).getItem_IMG01().contains(newText)) {
                        sortedSearchList.add(searchList.get(i));
                    } else if (searchList.get(i).getItem_IMA02() != null && searchList.get(i).getItem_IMA02().contains(newText)) {
                        sortedSearchList.add(searchList.get(i));
                    } else if (searchList.get(i).getItem_IMA021() != null && searchList.get(i).getItem_IMA021().contains(newText)) {
                        sortedSearchList.add(searchList.get(i));
                    } else if (searchList.get(i).getItem_IMG02() != null && searchList.get(i).getItem_IMG02().contains(newText)) {
                        sortedSearchList.add(searchList.get(i));
                    } else if (searchList.get(i).getItem_IMD02() != null && searchList.get(i).getItem_IMD02().contains(newText)) {
                        sortedSearchList.add(searchList.get(i));
                    } else if (searchList.get(i).getItem_IMG03() != null && searchList.get(i).getItem_IMG03().contains(newText)) {
                        sortedSearchList.add(searchList.get(i));
                    } else if (searchList.get(i).getItem_IMG04() != null && searchList.get(i).getItem_IMG04().contains(newText)) {
                        sortedSearchList.add(searchList.get(i));
                    } else if (searchList.get(i).getItem_IMG10() != null && searchList.get(i).getItem_IMG10().contains(newText)) {
                        sortedSearchList.add(searchList.get(i));
                    } else if (searchList.get(i).getItem_IMA25() != null && searchList.get(i).getItem_IMA25().contains(newText)) {
                        sortedSearchList.add(searchList.get(i));
                    } else if (searchList.get(i).getItem_IMG23() != null && searchList.get(i).getItem_IMG23().contains(newText)) {
                        sortedSearchList.add(searchList.get(i));
                    } else if (searchList.get(i).getItem_IMA08() != null && searchList.get(i).getItem_IMA08().contains(newText)) {
                        sortedSearchList.add(searchList.get(i));
                    } else if (searchList.get(i).getItem_STOCK_MAN() != null && searchList.get(i).getItem_STOCK_MAN().contains(newText)) {
                        sortedSearchList.add(searchList.get(i));
                    } else if (searchList.get(i).getItem_IMA03() != null && searchList.get(i).getItem_IMA03().contains(newText)) {
                        sortedSearchList.add(searchList.get(i));
                    } else if (searchList.get(i).getItem_PMC03() != null && searchList.get(i).getItem_PMC03().contains(newText)) {
                        sortedSearchList.add(searchList.get(i));
                    }
                }

                intent = new Intent(Constants.ACTION.ACTION_SEARCH_PART_WAREHOUSE_SORT_COMPLETE);
                sendBroadcast(intent);


                //listView.setAdapter(passwordKeeperArrayAdapter);

            } else {
                intent = new Intent(Constants.ACTION.ACTION_SEARCH_PART_WAREHOUSE_GET_ORIGINAL_LIST);
                sendBroadcast(intent);


                //passwordKeeperArrayAdapter = new PasswordKeeperArrayAdapter(Password_Keeper.this, R.layout.passwd_keeper_browsw_item, list);
                //listView.setAdapter(passwordKeeperArrayAdapter);
            }

            //meetingArrayAdapter = new MeetingArrayAdapter(context, R.layout.meeting_list_item, list);
            //AllFragment.resetAdapter(list);
            //AllFragment.listView.setAdapter(AllFragment.meetingArrayAdapter);



            return false;
        }
    };

    public void toast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    //for pda408
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        synchronized public void onServiceConnected(ComponentName name,
                                                    IBinder service) {
            iqspda = IQSService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iqspda = null;
        }
    };

    public static Intent getExplicitIntent(Context context,
                                           Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent,
                0);
        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }
        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);
        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);
        // Set the component to be explicit
        explicitIntent.setComponent(component);
        return explicitIntent;
    }

    private Bitmap takeScreenshot() {
        Log.e(TAG, "takeScreenshot start");
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);


        Bitmap croppedBitmap = null;

        try {
            // image naming and path  to include sd card  appending name you choose for file
            //String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            //save as a file
            //File imageFile = new File(mPath);
            //FileOutputStream outputStream = new FileOutputStream(imageFile);

            //int quality = 100;

            croppedBitmap = Bitmap.createBitmap(bitmap, 0, 120, bitmap.getWidth(), 260);



            //croppedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            //outputStream.flush();
            //outputStream.close();

            //openScreenshot(imageFile);

            if (croppedBitmap == null) {
                Log.e(TAG, "croppedBitmap is null");
            }

        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }

        Log.e(TAG, "takeScreenshot stop");

        return croppedBitmap;
    }

    public void sendCmd(byte[] b) {
        try {
            iqspda.sendCMD(b);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
