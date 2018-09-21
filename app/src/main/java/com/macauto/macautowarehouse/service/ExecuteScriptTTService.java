package com.macauto.macautowarehouse.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.util.Xml;

import com.macauto.macautowarehouse.data.Constants;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.net.SocketTimeoutException;

//import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.check_stock_in;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.table_X_M;
import static com.macauto.macautowarehouse.MainActivity.web_soap_port;
import static com.macauto.macautowarehouse.ProductionStorageFragment.product_table_X_M;
import static com.macauto.macautowarehouse.data.WebServiceParse.parseDataTableToXml;
import static com.macauto.macautowarehouse.data.WebServiceParse.parseDataTableToXml2;
import static com.macauto.macautowarehouse.data.WebServiceParse.parseToBoolean;

public class ExecuteScriptTTService extends IntentService {
    public static final String TAG = "ExecuteScriptTTService";

    public static final String SERVICE_IP = "172.17.17.244";

    public static final String SERVICE_PORT = "8484";

    private static final String NAMESPACE = "http://tempuri.org/"; // 命名空間

    private static final String METHOD_NAME = "Execute_Script_TT"; // 方法名稱

    private static final String SOAP_ACTION1 = "http://tempuri.org/Execute_Script_TT"; // SOAP_ACTION

    //private static final String URL = "http://172.17.17.244:8484/service.asmx"; // 網址

    //private String doc_type = "";
    private static boolean is_success = false;
    private static int process_type = 0;

    public ExecuteScriptTTService() {
        super("ExecuteScriptTTService");
    }


    //private String account;
    //private String device_id;



    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");



        /*pref = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        name = pref.getString("NAME", "");
        account = pref.getString("ACCOUNT", "");
        alarm_interval = pref.getInt("ALARM_INTERVAL", 30);
        sync_option = pref.getInt("SYNC_SETTING", 0);*/

        //context = getApplicationContext();

        // = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.TAIWAN);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.i(TAG, "Handle");

        //clear first
        //Intent cleanIntent = new Intent(Constants.ACTION.ACTION_SET_INSPECTED_RECEIVE_ITEM_CLEAN);
        //sendBroadcast(cleanIntent);

        //String device_id;


        String type = intent.getStringExtra("PROCESS_TYPE");
        process_type = Integer.valueOf(type);

        String URL = "http://172.17.17.244:"+web_soap_port+"/service.asmx";
        Log.e(TAG, "URL = "+URL);
        Log.e(TAG, "type = "+type);

        if (intent.getAction() != null) {
            if (intent.getAction().equals(Constants.ACTION.ACTION_EXECUTE_TT_ACTION)) {
                Log.i(TAG, "ACTION_EXECUTE_TT_ACTION");
            }
        }


        //StringWriter writer = new StringWriter();
        //XmlSerializer xmlSerializer = Xml.newSerializer();

        StringWriter writer;

        if (process_type == 0) {

            if (table_X_M != null) {
                writer = parseDataTableToXml(table_X_M);

                JSONObject jsonObj = null;
                try {
                    jsonObj = XML.toJSONObject(writer.toString());
                } catch (JSONException e) {
                    Log.e("JSON exception", e.getMessage());
                    e.printStackTrace();

                }

                //String temp = "NewDataSet=anyType{schema=anyType{element=anyType{complexType=anyType{choice=anyType{element=anyType{complexType=anyType{sequence=anyType{element=anyType{}; }; }; }; }; }; }; }; diffgram=anyType{DocumentElement=anyType{INCP=anyType{script=sh run_me 1 1 12701-1809140051; }; }; }; }\"";

                try {
                    // 建立一個 WebService 請求
                    //Log.d(TAG, "==>1");
                    SoapObject request = new SoapObject(NAMESPACE,
                            METHOD_NAME);

                    // 輸出值，帳號(account)、密碼(password)

                    request.addProperty("SID", "MAT");
                    //request.addProperty("script_list", writer.toString());
                    request.addProperty("script_list", writer.toString());
                    //request.addProperty("barcode_no", barcode_no);
                    //request.addProperty("k_id", "123456");
                    //request.addProperty("start_date", "");
                    //request.addProperty("end_date", "");
                    //request.addProperty("emp_no", account);
                    //request.addProperty("room_no", "");
                    //request.addProperty("user_no", account);
                    //request.addProperty("ime_code", device_id);
                    //request.addProperty("ime_code", account);
                    //request.addProperty("meeting_room_name", "");
                    //request.addProperty("subject_or_content", "");
                    //request.addProperty("meeting_type_id", "");
                    //request.addProperty("passWord", "sunnyhitest");

                    // 擴充 SOAP 序列化功能為第11版
                    //Log.d(TAG, "==>2");
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                            SoapEnvelope.VER11);

                    Log.e(TAG, "request = "+request.toString());

                    envelope.dotNet = true; // 設定為 .net 預設編碼
                    //Log.d(TAG, "==>3");
                    envelope.setOutputSoapObject(request); // 設定輸出的 SOAP 物件

                    //Log.d(TAG, "==>4");
                    // 建立一個 HTTP 傳輸層

                    HttpTransportSE httpTransport = new HttpTransportSE(URL);
                    httpTransport.debug = true; // 測試模式使用
                    //Log.d(TAG, "==>5");

                    httpTransport.call(SOAP_ACTION1, envelope); // 設定 SoapAction 所需的標題欄位
                    //Log.d(TAG, "==>6");

                    // 將 WebService 資訊轉為 DataTable
                    if (envelope.bodyIn instanceof SoapFault) {
                        String str= ((SoapFault) envelope.bodyIn).faultstring;
                        //Log.d(TAG, "==>7");
                        Log.e(TAG, str);
                        //Log.d(TAG, "==>8");
                        //Intent errorIntent = new Intent(Constants.ACTION.SOAP_CONNECTION_FAIL);
                        //sendBroadcast(errorIntent);
                    } else {
                        SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                        //Log.d(TAG, "==>9");
                        Log.e(TAG, String.valueOf(resultsRequestSOAP));


                        //boolean success = Boolean.valueOf(ret);
                        is_success = parseToBoolean(resultsRequestSOAP);

                        Intent checkResultIntent;
                        if (!is_success) {
                            checkResultIntent = new Intent(Constants.ACTION.ACTION_EXECUTE_TT_FAILED);
                            sendBroadcast(checkResultIntent);
                        } else {

                            checkResultIntent = new Intent(Constants.ACTION.ACTION_ENTERING_WAREHOUSE_COMPLETE);
                            sendBroadcast(checkResultIntent);

                        }

                    }

                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    Intent timeoutIntent = new Intent(Constants.ACTION.ACTION_SOCKET_TIMEOUT);
                    sendBroadcast(timeoutIntent);
                } catch (Exception e) {
                    // 抓到錯誤訊息

                    e.printStackTrace();
                    Intent getFailedIntent = new Intent(Constants.ACTION.ACTION_EXECUTE_TT_FAILED);
                    //getFailedIntent.putExtra("DOC_TYPE", doc_type);
                    sendBroadcast(getFailedIntent);
                }
            } else {
                Log.e(TAG, "table_X_M = null");
            }
        } else if (process_type == 1) {
            if (product_table_X_M != null) {
                writer = parseDataTableToXml(product_table_X_M);

                try {
                    // 建立一個 WebService 請求
                    //Log.d(TAG, "==>1");
                    SoapObject request = new SoapObject(NAMESPACE,
                            METHOD_NAME);

                    // 輸出值，帳號(account)、密碼(password)

                    request.addProperty("SID", "MAT");
                    request.addProperty("script_list", writer.toString());
                    //request.addProperty("barcode_no", barcode_no);
                    //request.addProperty("k_id", "123456");
                    //request.addProperty("start_date", "");
                    //request.addProperty("end_date", "");
                    //request.addProperty("emp_no", account);
                    //request.addProperty("room_no", "");
                    //request.addProperty("user_no", account);
                    //request.addProperty("ime_code", device_id);
                    //request.addProperty("ime_code", account);
                    //request.addProperty("meeting_room_name", "");
                    //request.addProperty("subject_or_content", "");
                    //request.addProperty("meeting_type_id", "");
                    //request.addProperty("passWord", "sunnyhitest");

                    // 擴充 SOAP 序列化功能為第11版
                    //Log.d(TAG, "==>2");
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                            SoapEnvelope.VER11);
                    envelope.dotNet = true; // 設定為 .net 預設編碼
                    //Log.d(TAG, "==>3");
                    envelope.setOutputSoapObject(request); // 設定輸出的 SOAP 物件

                    //Log.d(TAG, "==>4");
                    // 建立一個 HTTP 傳輸層

                    HttpTransportSE httpTransport = new HttpTransportSE(URL);
                    httpTransport.debug = true; // 測試模式使用
                    //Log.d(TAG, "==>5");

                    httpTransport.call(SOAP_ACTION1, envelope); // 設定 SoapAction 所需的標題欄位
                    //Log.d(TAG, "==>6");

                    // 將 WebService 資訊轉為 DataTable
                    if (envelope.bodyIn instanceof SoapFault) {
                        String str= ((SoapFault) envelope.bodyIn).faultstring;
                        //Log.d(TAG, "==>7");
                        Log.e(TAG, str);
                        //Log.d(TAG, "==>8");
                        //Intent errorIntent = new Intent(Constants.ACTION.SOAP_CONNECTION_FAIL);
                        //sendBroadcast(errorIntent);
                    } else {
                        SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                        //Log.d(TAG, "==>9");
                        Log.e(TAG, String.valueOf(resultsRequestSOAP));


                        //boolean success = Boolean.valueOf(ret);
                        is_success = parseToBoolean(resultsRequestSOAP);

                        Intent checkResultIntent;
                        if (!is_success) {
                            checkResultIntent = new Intent(Constants.ACTION.ACTION_EXECUTE_TT_FAILED);
                            sendBroadcast(checkResultIntent);
                        } else {

                            checkResultIntent = new Intent(Constants.ACTION.ACTION_PRODUCT_IN_STOCK_WORK_COMPLETE);
                            sendBroadcast(checkResultIntent);

                        }

                    }

                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                    Intent timeoutIntent = new Intent(Constants.ACTION.ACTION_SOCKET_TIMEOUT);
                    sendBroadcast(timeoutIntent);
                } catch (Exception e) {
                    // 抓到錯誤訊息

                    e.printStackTrace();
                    Intent getFailedIntent = new Intent(Constants.ACTION.ACTION_EXECUTE_TT_FAILED);
                    //getFailedIntent.putExtra("DOC_TYPE", doc_type);
                    sendBroadcast(getFailedIntent);
                }
            } else {
                Log.e(TAG, "product_table_X_M = null");
            }
        }












        //MeetingAlarm.last_sync_setting = sync_option;



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");



    }
}
