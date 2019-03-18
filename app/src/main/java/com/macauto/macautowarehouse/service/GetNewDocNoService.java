package com.macauto.macautowarehouse.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.macauto.macautowarehouse.data.Constants;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.ConnectException;
import java.net.SocketTimeoutException;


import static com.macauto.macautowarehouse.MainActivity.k_id;
import static com.macauto.macautowarehouse.MainActivity.service_ip;
import static com.macauto.macautowarehouse.MainActivity.web_soap_port;
import static com.macauto.macautowarehouse.data.WebServiceParse.parseToString;

public class GetNewDocNoService extends IntentService {
    public static final String TAG = "GetNewDocNoService";

    public static final String SERVICE_IP = "172.17.17.244";

    public static final String SERVICE_PORT = "8484";

    private static final String NAMESPACE = "http://tempuri.org/"; // 命名空間

    private static final String METHOD_NAME = "Get_New_Doc_No"; // 方法名稱

    private static final String SOAP_ACTION1 = "http://tempuri.org/Get_New_Doc_No"; // SOAP_ACTION

    //private static final String URL = "http://172.17.17.244:8484/service.asmx"; // 網址

    public GetNewDocNoService() {
        super("GetNewDocNoService");
    }

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

        String pur_cso_type = intent.getStringExtra("PUR_CSO_TYPE");
        String session_date = intent.getStringExtra("SESSION_DATE");
        String insert_sql = intent.getStringExtra("INSERT_SQL");
        String doc_type = intent.getStringExtra("DOC_TYPE");
        String URL = "http://"+service_ip+":"+web_soap_port+"/service.asmx";
        //String stock_no = intent.getStringExtra("STOCK_NO");
        //String locate_no = intent.getStringExtra("LOCATE_NO");
        //String batch_no = intent.getStringExtra("BATCH_NO");
        //String ima02 = intent.getStringExtra("NAME");
        //String ima021 = intent.getStringExtra("SPEC");
        //String query_all = intent.getStringExtra("QUERY_ALL");

        //Log.e(TAG, "tag_id = "+tag_id);

        //Log.e(TAG, "batch_no = "+batch_no);
        //Log.e(TAG, "ima02 = "+ima02);
        //Log.e(TAG, "ima021 = "+ima021);

        //device_id = intent.getStringExtra("DEVICE_ID");
        //String service_ip = intent.getStringExtra(SERVICE_IP);
        //String service_port = intent.getStringExtra(SERVICE_PORT);

        //String combine_url = "http://"+SERVICE_IP+":"+SERVICE_PORT+"/service.asmx";

        if (intent.getAction() != null) {
            if (intent.getAction().equals(Constants.ACTION.ACTION_ALLOCATION_GET_NEW_DOC_NO_ACTION)) {
                Log.i(TAG, "ACTION_ALLOCATION_GET_NEW_DOC_NO_ACTION");
            }
        }



        try {
            // 建立一個 WebService 請求

            SoapObject request = new SoapObject(NAMESPACE,
                    METHOD_NAME);

            // 輸出值，帳號(account)、密碼(password)

            request.addProperty("SID", "MAT");

            request.addProperty("pur_cso_type", pur_cso_type);
            request.addProperty("session_id", k_id);
            request.addProperty("session_date", session_date);
            request.addProperty("insert_sql", insert_sql);
            request.addProperty("doc_type", doc_type);
            //request.addProperty("req_qty", Integer.valueOf(req_qty));
            //request.addProperty("stock_no", stock_no);
            //request.addProperty("locate_no", locate_no);
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



            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);
            envelope.dotNet = true; // 設定為 .net 預設編碼

            envelope.setOutputSoapObject(request); // 設定輸出的 SOAP 物件

            // 建立一個 HTTP 傳輸層

            HttpTransportSE httpTransport = new HttpTransportSE(URL);
            httpTransport.debug = true; // 測試模式使用



            httpTransport.call(SOAP_ACTION1, envelope); // 設定 SoapAction 所需的標題欄位


            // 將 WebService 資訊轉為 DataTable
            if (envelope.bodyIn instanceof SoapFault) {
                String str= ((SoapFault) envelope.bodyIn).faultstring;
                Log.e(TAG, str);

                Intent getSuccessIntent = new Intent(Constants.ACTION.SOAP_CONNECTION_FAIL);
                sendBroadcast(getSuccessIntent);
            } else {
                SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                Log.e(TAG, String.valueOf(resultsRequestSOAP));

                String ret = parseToString(resultsRequestSOAP);

                if (ret != null) {
                    Intent getSuccessIntent = new Intent(Constants.ACTION.ACTION_ALLOCATION_GET_NEW_DOC_NO_SUCCESS);
                    getSuccessIntent.putExtra("NEW_NO", ret);
                    sendBroadcast(getSuccessIntent);
                } else {
                    Intent getFailedIntent = new Intent(Constants.ACTION.ACTION_ALLOCATION_GET_NEW_DOC_NO_FAILED);
                    sendBroadcast(getFailedIntent);
                }





            }

            //meetingArrayAdapter = new MeetingArrayAdapter(MainActivity.this, R.layout.list_item, meetingList);
            //listView.setAdapter(meetingArrayAdapter);


            //Intent meetingAddintent = new Intent(Constants.ACTION.MEETING_NEW_BROCAST);
            //context.sendBroadcast(meetingAddintent);
            //SoapObject bodyIn = (SoapObject) envelope.bodyIn; // KDOM 節點文字編碼

            //Log.e(TAG, bodyIn.toString());

            //DataTable dt = soapToDataTable(bodyIn);


        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            Intent timeoutIntent = new Intent(Constants.ACTION.ACTION_SOCKET_TIMEOUT);
            sendBroadcast(timeoutIntent);
        } catch (ConnectException e) {
            Intent failedIntent = new Intent(Constants.ACTION.SOAP_CONNECTION_FAIL);
            sendBroadcast(failedIntent);
        } catch (Exception e) {
            // 抓到錯誤訊息

            e.printStackTrace();
            Intent getFailedIntent = new Intent(Constants.ACTION.ACTION_ALLOCATION_GET_NEW_DOC_NO_FAILED);
            sendBroadcast(getFailedIntent);
        }

        //MeetingAlarm.last_sync_setting = sync_option;



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");

    }
}
