package com.macauto.macautowarehouse.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.macauto.macautowarehouse.data.Constants;

import com.macauto.macautowarehouse.data.ProductionStorageItem;

import com.macauto.macautowarehouse.table.DataRow;
import com.macauto.macautowarehouse.table.DataTable;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


import java.net.ConnectException;
import java.net.SocketTimeoutException;

import static com.macauto.macautowarehouse.MainActivity.global_sid;
import static com.macauto.macautowarehouse.MainActivity.service_ip;
import static com.macauto.macautowarehouse.MainActivity.web_soap_port;
import static com.macauto.macautowarehouse.MainActivity.dataTable_RR;
import static com.macauto.macautowarehouse.MainActivity.productList;
import static com.macauto.macautowarehouse.data.WebServiceParse.parseXmlToDataTable;

public class GetTTProductEntryService extends IntentService {
    public static final String TAG = "GetTTProductEntry";

    public static final String SERVICE_IP = "172.17.17.244";

    public static final String SERVICE_PORT = "8484";

    private static final String NAMESPACE = "http://tempuri.org/"; // 命名空間

    private static final String METHOD_NAME = "Get_TT_product_Entry"; // 方法名稱

    private static final String SOAP_ACTION1 = "http://tempuri.org/Get_TT_product_Entry"; // SOAP_ACTION
    //normal port 8000, test port 8484
    //private static final String URL = "http://172.17.17.244:"+web_soap_port+"/service.asmx"; // 網址

    public GetTTProductEntryService() {
        super("GetTTProductEntryService");
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

        String in_no = intent.getStringExtra("IN_NO");
        //String barcode_no = intent.getStringExtra("BARCODE_NO");
        //String k_id = intent.getStringExtra("K_ID");

        String URL = "http://"+service_ip+":"+web_soap_port+"/service.asmx";

        Log.e(TAG, "in_no = "+in_no);
        //Log.e(TAG, "barcode_no = "+barcode_no);
        //Log.e(TAG, "URL = "+URL);

        //device_id = intent.getStringExtra("DEVICE_ID");
        //String service_ip = intent.getStringExtra(SERVICE_IP);
        //String service_port = intent.getStringExtra(SERVICE_PORT);

        //String combine_url = "http://"+SERVICE_IP+":"+SERVICE_PORT+"/service.asmx";

        if (intent.getAction() != null) {
            if (intent.getAction().equals(Constants.ACTION.ACTION_GET_TT_PRODUCT_ENTRY_ACTION)) {
                Log.i(TAG, "ACTION_GET_TT_PRODUCT_ENTRY_ACTION");
            }
        }



        try {
            // 建立一個 WebService 請求

            SoapObject request = new SoapObject(NAMESPACE,
                    METHOD_NAME);

            // 輸出值，帳號(account)、密碼(password)
            //Log.e(TAG, "k_id = "+k_id);


            request.addProperty("in_no", in_no);
            request.addProperty("SID", global_sid);
            //request.addProperty("part_no", part_no);
            //request.addProperty("barcode_no", barcode_no);
            //request.addProperty("k_id", k_id);
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
                Intent getFailedIntent = new Intent(Constants.ACTION.ACTION_GET_INSPECTED_RECEIVE_ITEM_FAILED);
                sendBroadcast(getFailedIntent);
            } else {
                SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                Log.e(TAG, String.valueOf(resultsRequestSOAP));

                //append_record(String.valueOf(resultsRequestSOAP)+"\n\n\n\n", "test");

                SoapObject s_deals = (SoapObject) resultsRequestSOAP.getProperty("Get_TT_product_EntryResult");

                if (dataTable_RR != null) {
                    dataTable_RR.clear();
                } else {
                    dataTable_RR = new DataTable();
                }

                dataTable_RR = parseXmlToDataTable(s_deals);

                if (dataTable_RR != null) {

                    Log.e(TAG, "dataTable.Rows.size() = "+dataTable_RR.Rows.size());

                    if (dataTable_RR.Rows.size() == 0) {
                        Intent getSuccessIntent = new Intent(Constants.ACTION.ACTION_GET_TT_PRODUCT_ENTRY_EMPTY);
                        sendBroadcast(getSuccessIntent);
                    } else {


                        for (DataRow rx : dataTable_RR.Rows) {
                            ProductionStorageItem item = new ProductionStorageItem();

                            item.setIn_no(rx.getValue("in_no").toString());
                            item.setItem_no(rx.getValue("item_no").toString());
                            item.setIn_date(rx.getValue("in_date").toString());
                            item.setMade_no(rx.getValue("made_no").toString());
                            item.setStore_type(rx.getValue("store_type").toString());
                            item.setDept_no(rx.getValue("dept_no").toString());
                            item.setDept_name(rx.getValue("dept_name").toString());
                            item.setPart_no(rx.getValue("part_no").toString());
                            item.setPart_desc(rx.getValue("part_desc").toString());
                            item.setStock_no(rx.getValue("stock_no").toString());
                            item.setLocate_no(rx.getValue("locate_no").toString());
                            item.setBatch_no(rx.getValue("batch_no").toString());
                            item.setQty(rx.getValue("qty").toString());
                            item.setStock_unit(rx.getValue("stock_unit").toString());
                            item.setEmp_name(rx.getValue("emp_name").toString());
                            item.setCount_no(rx.getValue("count_no").toString());
                            item.setStock_no_name(rx.getValue("stock_no_name").toString());
                            item.setLocate_no_scan("");
                            item.setSelected(false);

                            productList.add(item);
                        }




                        Intent getSuccessIntent = new Intent(Constants.ACTION.ACTION_GET_TT_PRODUCT_ENTRY_SUCCESS);
                        sendBroadcast(getSuccessIntent);
                    }



                } else {
                    Intent getSuccessIntent = new Intent(Constants.ACTION.ACTION_GET_TT_PRODUCT_ENTRY_EMPTY);
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
        } catch (ConnectException e) {
            Intent failedIntent = new Intent(Constants.ACTION.SOAP_CONNECTION_FAIL);
            sendBroadcast(failedIntent);
        } catch (Exception e) {
            // 抓到錯誤訊息

            e.printStackTrace();
            Intent getFailedIntent = new Intent(Constants.ACTION.ACTION_GET_TT_PRODUCT_ENTRY_FAILED);
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
