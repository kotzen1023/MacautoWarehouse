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

import static com.macauto.macautowarehouse.AllocationSendMsgToReserveWarehouseFragment.hhh;

import static com.macauto.macautowarehouse.data.WebServiceParse.parseDataTableToXml;
import static com.macauto.macautowarehouse.data.WebServiceParse.parseToString;


public class GetSfaDataMessMoveService extends IntentService {
    public static final String TAG = "GetSfaDataMessMove";

    public static final String SERVICE_IP = "172.17.17.244";

    public static final String SERVICE_PORT = "8484";

    private static final String NAMESPACE = "http://tempuri.org/"; // 命名空間

    private static final String METHOD_NAME = "get_sfa_data_mess_move"; // 方法名稱

    private static final String SOAP_ACTION1 = "http://tempuri.org/get_sfa_data_mess_move"; // SOAP_ACTION

    private static final String URL = "http://172.17.17.244:8484/service.asmx"; // 網址

    public GetSfaDataMessMoveService() {
        super("GetSfaDataMessMoveService");
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

        if (intent.getAction() != null) {
            if (intent.getAction().equals(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_MOVE_ACTION)) {
                Log.i(TAG, "ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_MOVE_ACTION");
            }
        }

        String user_no = intent.getStringExtra("USER_NO");
        String made_no = intent.getStringExtra("MADE_NO");
        String request_time = intent.getStringExtra("REQUEST_TIME");
        //String stock_no = intent.getStringExtra("STOCK_NO");
        //String locate_no = intent.getStringExtra("LOCATE_NO");
        //String stock_no = intent.getStringExtra("STOCK_NO");
        //String locate_no = intent.getStringExtra("LOCATE_NO");
        //String batch_no = intent.getStringExtra("BATCH_NO");
        //String ima02 = intent.getStringExtra("NAME");
        //String ima021 = intent.getStringExtra("SPEC");
        //String query_all = intent.getStringExtra("QUERY_ALL");

        Log.e(TAG, "user_no = "+user_no);
        Log.e(TAG, "made_no = "+made_no);
        Log.e(TAG, "request_time = "+request_time);
        //Log.e(TAG, "locate_no = "+locate_no);
        //Log.e(TAG, "batch_no = "+batch_no);
        //Log.e(TAG, "ima02 = "+ima02);
        //Log.e(TAG, "ima021 = "+ima021);

        //device_id = intent.getStringExtra("DEVICE_ID");
        //String service_ip = intent.getStringExtra(SERVICE_IP);
        //String service_port = intent.getStringExtra(SERVICE_PORT);

        //String combine_url = "http://"+SERVICE_IP+":"+SERVICE_PORT+"/service.asmx";


        Log.e(TAG, "========================================================");
        for (int i=0; i<hhh.Rows.size(); i++) {

            for (int j=0; j<hhh.Columns.size(); j++) {
                System.out.print(hhh.Rows.get(i).getValue(j));
                if (j < hhh.Columns.size() - 1) {
                    System.out.print(", ");
                }
            }
            System.out.print("\n");
        }
        Log.e(TAG, "========================================================");


        String writer;

        if (hhh != null) {
            writer = parseDataTableToXml(hhh);

            try {
                // 建立一個 WebService 請求

                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME);

                // 輸出值，帳號(account)、密碼(password)

                request.addProperty("SID", "MAT");
                request.addProperty("HHH", writer);
                request.addProperty("user_no", user_no);
                request.addProperty("made_no", made_no);
                request.addProperty("request_time", request_time);
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

                    Intent getSuccessIntent = new Intent(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_MOVE_FAILED);
                    sendBroadcast(getSuccessIntent);
                } else {
                    SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                    Log.e(TAG, String.valueOf(resultsRequestSOAP));

                    //SoapObject s_deals = (SoapObject) resultsRequestSOAP.getProperty("get_sfa_data_messResult");
                    String ret = parseToString(resultsRequestSOAP);

                    Log.d(TAG, "ret = "+ret);

                    if (ret.equals("anyType{}")) {
                        Intent getFailedIntent = new Intent(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_MOVE_EMPTY);
                        sendBroadcast(getFailedIntent);
                    } else {
                        Intent getSuccessIntent = new Intent(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_MOVE_SUCCESS);
                        getSuccessIntent.putExtra("MSG_RET", ret);
                        sendBroadcast(getSuccessIntent);
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
            } catch (Exception e) {
                // 抓到錯誤訊息

                e.printStackTrace();
                Intent getFailedIntent = new Intent(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_MOVE_FAILED);
                sendBroadcast(getFailedIntent);
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
