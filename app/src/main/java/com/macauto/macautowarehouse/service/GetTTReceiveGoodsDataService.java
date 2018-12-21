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


public class GetTTReceiveGoodsDataService extends IntentService {
    public static final String TAG = "GetTTReceiveGoods";

    public static final String SERVICE_IP = "172.17.17.244";

    public static final String SERVICE_PORT = "8484";

    private static final String NAMESPACE = "http://tempuri.org/"; // 命名空間

    private static final String METHOD_NAME = "Get_TT_ReceiveGoods_Data"; // 方法名稱

    private static final String SOAP_ACTION1 = "http://tempuri.org/Get_TT_ReceiveGoods_Data"; // SOAP_ACTION

    private static final String URL = "http://172.17.17.244:8484/service.asmx"; // 網址

    //private String doc_type = "";

    public GetTTReceiveGoodsDataService() {
        super("GetTTReceiveGoodsDataService");
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


        String shipping_no = intent.getStringExtra("SHIPPING_NO");



        if (intent.getAction() != null) {
            if (intent.getAction().equals(Constants.ACTION.ACTION_RECEIVING_GOODS_DATA_ACTION)) {
                Log.i(TAG, "ACTION_RECEIVING_GOODS_DATA_ACTION");
            }
        }







        try {
            // 建立一個 WebService 請求
            //Log.d(TAG, "==>1");
            SoapObject request = new SoapObject(NAMESPACE,
                    METHOD_NAME);

            // 輸出值，帳號(account)、密碼(password)

            request.addProperty("SID", "MAT");
            request.addProperty("rec_no", shipping_no.toUpperCase());
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
                Log.e(TAG, str);
                Intent getFailedIntent = new Intent(Constants.ACTION.ACTION_RECEIVING_GOODS_DATA_FAILED);
                sendBroadcast(getFailedIntent);
            } else {
                SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                Log.e(TAG, String.valueOf(resultsRequestSOAP));

                //append_record(String.valueOf(resultsRequestSOAP)+"\n\n\n\n", "test");

                //SoapObject s_deals = (SoapObject) resultsRequestSOAP.getProperty("Get_TT_ReceiveGoods_DataResult");




                Intent getSuccessIntent = new Intent(Constants.ACTION.ACTION_RECEIVING_GOODS_DATA_SUCCESS);
                sendBroadcast(getSuccessIntent);
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
            Intent getFailedIntent = new Intent(Constants.ACTION.ACTION_RECEIVING_GOODS_DATA_FAILED);
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
