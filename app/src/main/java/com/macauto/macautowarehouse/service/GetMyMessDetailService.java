package com.macauto.macautowarehouse.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;


import com.macauto.macautowarehouse.data.AllocationMsgDetailItem;
import com.macauto.macautowarehouse.data.Constants;
import com.macauto.macautowarehouse.table.DataColumn;
import com.macauto.macautowarehouse.table.DataRow;
import com.macauto.macautowarehouse.table.DataTable;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketTimeoutException;


import static com.macauto.macautowarehouse.AllocationMsgDetailActivity.showList;
import static com.macauto.macautowarehouse.AllocationMsgFragment.msgDataTable;
import static com.macauto.macautowarehouse.MainActivity.web_soap_port;
import static com.macauto.macautowarehouse.data.WebServiceParse.parseXmlToDataTable;

public class GetMyMessDetailService extends IntentService{
    public static final String TAG = "GetMyMessDetail";

    public static final String SERVICE_IP = "172.17.17.244";

    public static final String SERVICE_PORT = "8484";

    private static final String NAMESPACE = "http://tempuri.org/"; // 命名空間

    private static final String METHOD_NAME = "get_my_mess_detail"; // 方法名稱

    private static final String SOAP_ACTION1 = "http://tempuri.org/get_my_mess_detail"; // SOAP_ACTION
    //normal port 8000, test port 8484
    //private static final String URL = "http://172.17.17.244:"+web_soap_port+"/service.asmx"; // 網址

    public GetMyMessDetailService() {
        super("GetMyMessDetailService");
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

        String iss_no = intent.getStringExtra("ISS_NO");
        String dateTime_0 = intent.getStringExtra("DATETIME_0");
        String dateTime_1 = intent.getStringExtra("DATETIME_1");
        String dateTime_2 = intent.getStringExtra("DATETIME_2");
        String dateTime_3 = intent.getStringExtra("DATETIME_3");


        String URL = "http://172.17.17.244:"+web_soap_port+"/service.asmx"; // 網址

        Log.e(TAG, "iss_no = "+iss_no);

        Log.e(TAG, "URL = "+URL);

        //device_id = intent.getStringExtra("DEVICE_ID");
        //String service_ip = intent.getStringExtra(SERVICE_IP);
        //String service_port = intent.getStringExtra(SERVICE_PORT);

        //String combine_url = "http://"+SERVICE_IP+":"+SERVICE_PORT+"/service.asmx";

        if (intent.getAction() != null) {
            if (intent.getAction().equals(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_DETAIL_ACTION)) {
                Log.i(TAG, "ACTION_ALLOCATION_GET_MY_MESS_DETAIL_ACTION");
            }
        }



        try {
            // 建立一個 WebService 請求



            SoapObject request = new SoapObject(NAMESPACE,
                    METHOD_NAME);

            request.addProperty("SID", "MAT");
            request.addProperty("iss_no", iss_no);
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
                Intent getFailedIntent = new Intent(Constants.ACTION.SOAP_CONNECTION_FAIL);
                sendBroadcast(getFailedIntent);
            } else {
                SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                Log.e(TAG, String.valueOf(resultsRequestSOAP));

                SoapObject s_deals = (SoapObject) resultsRequestSOAP.getProperty("get_my_mess_detailResult");
                Log.d(TAG, "s_deals = "+s_deals.toString());

                if (msgDataTable != null) {
                    msgDataTable.clear();
                } else {
                    msgDataTable = new DataTable();
                }


                msgDataTable = parseXmlToDataTable(s_deals);




                if (msgDataTable.Rows.size() > 0) {
                    msgDataTable.TableName = "TYYC";
                    //add for scan
                    DataColumn scan_sp = new DataColumn("scan_sp");
                    DataColumn scan_desc = new DataColumn("scan_desc");

                    msgDataTable.Columns.add(scan_sp);
                    msgDataTable.Columns.add(scan_desc);









                    Intent getSuccessIntent = new Intent(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_DETAIL_SUCCESS);
                    getSuccessIntent.putExtra("ISS_DATE", msgDataTable.Rows.get(0).getValue("iss_date").toString());
                    getSuccessIntent.putExtra("MADE_NO", msgDataTable.Rows.get(0).getValue("made_no").toString());
                    getSuccessIntent.putExtra("TAG_LOCATE_NO", msgDataTable.Rows.get(0).getValue("tag_locate_no").toString());
                    getSuccessIntent.putExtra("TAG_STOCK_NO", msgDataTable.Rows.get(0).getValue("tag_stock_no").toString());
                    getSuccessIntent.putExtra("IMA03", msgDataTable.Rows.get(0).getValue("ima03").toString());
                    getSuccessIntent.putExtra("DATETIME_0", dateTime_0);
                    getSuccessIntent.putExtra("DATETIME_1", dateTime_1);
                    getSuccessIntent.putExtra("DATETIME_2", dateTime_2);
                    getSuccessIntent.putExtra("DATETIME_3", dateTime_3);
                    sendBroadcast(getSuccessIntent);
                } else {
                    Intent getFailedIntent = new Intent(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_DETAIL_FAILED);
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
            // 抓到錯誤訊息

            e.printStackTrace();
            Intent getTimeOutIntent = new Intent(Constants.ACTION.ACTION_SOCKET_TIMEOUT);
            sendBroadcast(getTimeOutIntent);
        } catch (Exception e) {
            // 抓到錯誤訊息

            e.printStackTrace();
            Intent getFailedIntent = new Intent(Constants.ACTION.ACTION_ALLOCATION_GET_MY_MESS_DETAIL_FAILED);
            sendBroadcast(getFailedIntent);
        }

        //MeetingAlarm.last_sync_setting = sync_option;



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
        //Intent intent = new Intent(Constants.ACTION.GET_MESSAGE_LIST_COMPLETE);
        //sendBroadcast(intent);
    }
}
