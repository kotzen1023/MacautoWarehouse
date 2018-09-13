package com.macauto.macautowarehouse.service;

import android.app.IntentService;
import android.content.Intent;

import android.util.Log;



import com.macauto.macautowarehouse.data.Constants;
import com.macauto.macautowarehouse.table.DataRow;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.check_stock_in;
import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.dataTable;

import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.pp_list;
import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.table_X_M;
import static com.macauto.macautowarehouse.MainActivity.web_soap_port;
import static com.macauto.macautowarehouse.data.WebServiceParse.parseToString;

public class GetDocTypeIsRegOrSubService extends IntentService {
    public static final String TAG = "GetDocTypeService";

    public static final String SERVICE_IP = "172.17.17.244";

    public static final String SERVICE_PORT = "8484";

    private static final String NAMESPACE = "http://tempuri.org/"; // 命名空間

    private static final String METHOD_NAME = "Get_TT_doc_type_is_REG_or_SUB"; // 方法名稱

    private static final String SOAP_ACTION1 = "http://tempuri.org/Get_TT_doc_type_is_REG_or_SUB"; // SOAP_ACTION

    //private static final String URL = "http://172.17.17.244:8484/service.asmx"; // 網址

    private String doc_type = "";

    public GetDocTypeIsRegOrSubService() {
        super("GetDocTypeIsRegOrSubService");
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


        String current_table = intent.getStringExtra("CURRENT_TABLE");
        String doc_no = intent.getStringExtra("DOC_NO");

        String URL = "http://172.17.17.244:"+web_soap_port+"/service.asmx";
        Log.e(TAG, "URL = "+URL);



        if (intent.getAction() != null) {
            if (intent.getAction().equals(Constants.ACTION.ACTION_GET_DOC_TYPE_IS_REG_OR_SUB_ACTION)) {
                Log.i(TAG, "ACTION_GET_DOC_TYPE_IS_REG_OR_SUB_ACTION");
            }
        }

        //String doc_no5 = dataTable.Rows.get(Integer.valueOf(current_table)).getValue(1).toString();
        //Log.d(TAG, "current_table = "+current_table+", doc_no5 = "+doc_no5);

        String doc_no5 = doc_no.split("-")[0];



        try {
            // 建立一個 WebService 請求
            //Log.d(TAG, "==>1");
            SoapObject request = new SoapObject(NAMESPACE,
                    METHOD_NAME);

            // 輸出值，帳號(account)、密碼(password)

            request.addProperty("SID", "MAT");
            request.addProperty("doc_no5", doc_no5);
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
                //Log.d(TAG, "==>10");

                //SoapObject ret = (SoapObject) resultsRequestSOAP.getProperty("Get_TT_doc_type_is_REG_or_SUBResult");

                //SoapObject ret  = (SoapObject )resultsRequestSOAP.getAttribute("Get_TT_doc_type_is_REG_or_SUBResult");

                //doc_type = ret.toString();

                doc_type = parseToString(resultsRequestSOAP);

                //doc_type = resultsRequestSOAP.getPrimitiveProperty("Get_TT_doc_type_is_REG_or_SUBResult").toString();
                Log.d(TAG, "doc_type = "+doc_type);

                /*if (String.valueOf(resultsRequestSOAP).indexOf("REG") > 0) {
                    Log.e(TAG, "ret = true");
                    is_exist = true;
                    //loginResultIntent = new Intent(Constants.ACTION.ACTION_CHECK_EMP_EXIST_SUCCESS);
                    //sendBroadcast(loginResultIntent);
                } else {
                    Log.e(TAG, "ret = false");
                    is_exist = false;
                    //loginResultIntent = new Intent(Constants.ACTION.ACTION_CHECK_EMP_EXIST_NOT_EXIST);
                    //sendBroadcast(loginResultIntent);
                }*/

                String s_p = "1"; //TEST = 2, MAT or other = 1
                if (web_soap_port.equals("8484")) {
                    s_p = "2";
                }


                String script_string = "sh run_me " + s_p + " 1 " + doc_no5 + " '" + doc_type + "'";

                DataRow kr = table_X_M.NewRow();
                kr.setValue("script", script_string);
                table_X_M.Rows.add(kr);

                /*boolean found_same_script = false;
                for (DataRow dr : table_X_M.Rows) {
                    if (dr.getValue("script").equals(script_string)) {
                        found_same_script = true;
                        break;
                    }
                }
                if (found_same_script) {
                    Log.d(TAG, "Found same script, will not add");
                } else {
                    DataRow kr = table_X_M.NewRow();
                    kr.setValue("script", script_string);
                    table_X_M.Rows.add(kr);
                }*/



                //int found_index = -1;
                int next_table;

                next_table = Integer.valueOf(current_table) - 1;
                Log.e(TAG, "=== [ExecuteScriptTTService] check pp_list in start ===");
                for (int i=0; i < pp_list.size(); i++) {
                    Log.e(TAG, "pp_list["+i+"] = "+pp_list.get(i));
                }
                Log.e(TAG, "=== [ExecuteScriptTTService] check pp_list in end ===");



                /*for (int i=next_table; i>=0; i--) {
                    if (check_stock_in.get(i)) {
                        Log.e(TAG, "found_index =>>>>> "+i);
                        found_index = i;
                        break;
                    }
                }*/

                Intent getSuccessIntent = new Intent();

                if (next_table >= 0) {
                    Log.e(TAG, "send current index back and go next");
                    getSuccessIntent.setAction(Constants.ACTION.ACTION_GET_DOC_TYPE_IS_REG_OR_SUB_SUCCESS);
                    getSuccessIntent.putExtra("CURRENT_TABLE", String.valueOf(next_table));
                    //getSuccessIntent.putExtra("NEXT_TABLE", String.valueOf(found_index));
                    sendBroadcast(getSuccessIntent);
                } else { //next_table == -1
                    getSuccessIntent.setAction(Constants.ACTION.ACTION_GET_DOC_TYPE_IS_REG_OR_SUB_COMPLETE);
                    //getSuccessIntent.putExtra("CURRENT_TABLE", current_table);
                    sendBroadcast(getSuccessIntent);
                }

                /*if (next_table != -1) {


                    if (next_table == -1) {
                        Log.e(TAG, "send complete to stop!");
                        getSuccessIntent.setAction(Constants.ACTION.ACTION_GET_DOC_TYPE_IS_REG_OR_SUB_COMPLETE);
                        //getSuccessIntent.putExtra("CURRENT_TABLE", current_table);
                        sendBroadcast(getSuccessIntent);
                    } else {
                        Log.e(TAG, "send current index back and go next");
                        getSuccessIntent.setAction(Constants.ACTION.ACTION_GET_DOC_TYPE_IS_REG_OR_SUB_SUCCESS);
                        getSuccessIntent.putExtra("CURRENT_TABLE", String.valueOf(found_index));
                        //getSuccessIntent.putExtra("NEXT_TABLE", String.valueOf(found_index));
                        sendBroadcast(getSuccessIntent);
                    }


                } else {
                    Log.e(TAG, "send complete to stop!");
                    getSuccessIntent.setAction(Constants.ACTION.ACTION_GET_DOC_TYPE_IS_REG_OR_SUB_COMPLETE);
                    //getSuccessIntent.putExtra("CURRENT_TABLE", current_table);
                    sendBroadcast(getSuccessIntent);

                }*/

                /*Intent getSuccessIntent = new Intent(Constants.ACTION.ACTION_GET_DOC_TYPE_IS_REG_OR_SUB_SUCCESS);
                getSuccessIntent.putExtra("DOC_TYPE", doc_type);
                getSuccessIntent.putExtra("CURRENT_TABLE", current_table);
                getSuccessIntent.putExtra("RVU01", doc_no5);
                sendBroadcast(getSuccessIntent);*/
            }

            //meetingArrayAdapter = new MeetingArrayAdapter(MainActivity.this, R.layout.list_item, meetingList);
            //listView.setAdapter(meetingArrayAdapter);


            //Intent meetingAddintent = new Intent(Constants.ACTION.MEETING_NEW_BROCAST);
            //context.sendBroadcast(meetingAddintent);
            //SoapObject bodyIn = (SoapObject) envelope.bodyIn; // KDOM 節點文字編碼

            //Log.e(TAG, bodyIn.toString());

            //DataTable dt = soapToDataTable(bodyIn);


        } catch (Exception e) {
            // 抓到錯誤訊息

            e.printStackTrace();
            Intent getFailedIntent = new Intent(Constants.ACTION.ACTION_GET_DOC_TYPE_IS_REG_OR_SUB_FAILED);
            getFailedIntent.putExtra("DOC_TYPE", doc_type);
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
