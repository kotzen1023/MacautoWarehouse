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

import java.io.StringWriter;
import java.net.SocketTimeoutException;

import static com.macauto.macautowarehouse.ReceivingInspectionFragment.dataTable_PG_M;
import static com.macauto.macautowarehouse.data.WebServiceParse.parseDataTableToSoapObject;
import static com.macauto.macautowarehouse.data.WebServiceParse.parseDataTableToXml;
import static com.macauto.macautowarehouse.data.WebServiceParse.parseToBoolean;

public class ExecuteTTPrgAService extends IntentService {
    public static final String TAG = "ExecuteTTPrgA";

    public static final String SERVICE_IP = "172.17.17.244";

    public static final String SERVICE_PORT = "8484";

    private static final String NAMESPACE = "http://tempuri.org/"; // 命名空間

    private static final String METHOD_NAME = "Execute_TT_prg_A"; // 方法名稱

    private static final String SOAP_ACTION1 = "http://tempuri.org/Execute_TT_prg_A"; // SOAP_ACTION

    private static final String URL = "http://172.17.17.244:8484/service.asmx"; // 網址

    private boolean is_success = false;

    public ExecuteTTPrgAService() {
        super("ExecuteTTPrgAService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");


    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.i(TAG, "Handle");

        //clear first
        //Intent cleanIntent = new Intent(Constants.ACTION.ACTION_SET_INSPECTED_RECEIVE_ITEM_CLEAN);
        //sendBroadcast(cleanIntent);

        //String device_id;

        //String current_row = intent.getStringExtra("CURRENT_ROW");
        //String rec_no = intent.getStringExtra("REC_NO");
        //String item_no = intent.getStringExtra("ITEM_NO");
        //String stock_no = intent.getStringExtra("STOCK_NO");
        //String locate_no = intent.getStringExtra("LOCATE_NO");
        //String batch_no = intent.getStringExtra("BATCH_NO");
        //String ima02 = intent.getStringExtra("NAME");
        //String ima021 = intent.getStringExtra("SPEC");
        //String query_all = intent.getStringExtra("QUERY_ALL");

        //Log.e(TAG, "rec_no = "+rec_no);
        //Log.e(TAG, "batch_no = "+batch_no);
        //Log.e(TAG, "ima02 = "+ima02);
        //Log.e(TAG, "ima021 = "+ima021);

        //device_id = intent.getStringExtra("DEVICE_ID");
        //String service_ip = intent.getStringExtra(SERVICE_IP);
        //String service_port = intent.getStringExtra(SERVICE_PORT);

        //String combine_url = "http://"+SERVICE_IP+":"+SERVICE_PORT+"/service.asmx";

        if (intent.getAction() != null) {
            if (intent.getAction().equals(Constants.ACTION.ACTION_RECEIVING_INSPECTION_EXECUTE_TT_PRG_A_ACTION)) {
                Log.i(TAG, "ACTION_RECEIVING_INSPECTION_EXECUTE_TT_PRG_A_ACTION");
            }
        }

        String writer;

        if (dataTable_PG_M != null && dataTable_PG_M.Rows.size() > 0) {
            writer = parseDataTableToXml(dataTable_PG_M);
            SoapObject mySoap = parseDataTableToSoapObject("rec_no_list", dataTable_PG_M);

            try {
                // 建立一個 WebService 請求

                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME);

                // 輸出值，帳號(account)、密碼(password)

                request.addProperty("SID", "MAT");

                request.addSoapObject(mySoap);
                //request.addProperty("rec_no_list", writer);
                //request.addProperty("user_no", emp_no);
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
                    String str = ((SoapFault) envelope.bodyIn).faultstring;
                    Log.e(TAG, str);

                    Intent getSuccessIntent = new Intent(Constants.ACTION.SOAP_CONNECTION_FAIL);
                    sendBroadcast(getSuccessIntent);
                } else {
                    SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                    Log.e(TAG, String.valueOf(resultsRequestSOAP));

                    is_success = parseToBoolean(resultsRequestSOAP);

                    Intent checkResultIntent;
                    if (!is_success) {
                        checkResultIntent = new Intent(Constants.ACTION.ACTION_RECEIVING_INSPECTION_EXECUTE_TT_PRG_A_FAILED);

                    } else {
                        checkResultIntent = new Intent(Constants.ACTION.ACTION_RECEIVING_INSPECTION_EXECUTE_TT_PRG_A_SUCCESS);

                    }
                    sendBroadcast(checkResultIntent);



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
            } catch (Exception e) {
                // 抓到錯誤訊息

                e.printStackTrace();
                Intent getFailedIntent = new Intent(Constants.ACTION.ACTION_RECEIVING_INSPECTION_EXECUTE_TT_PRG_A_FAILED);
                sendBroadcast(getFailedIntent);
            }
        } else {
            Intent getFailedIntent = new Intent(Constants.ACTION.ACTION_RECEIVING_INSPECTION_EXECUTE_TT_PRG_A_FAILED);
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
