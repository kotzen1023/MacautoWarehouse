package com.macauto.macautowarehouse.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.util.Xml;

import com.macauto.macautowarehouse.data.Constants;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;

import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.dataTable;

public class ExecuteScriptTTService extends IntentService {
    public static final String TAG = "ExecuteScriptTTService";

    public static final String SERVICE_IP = "172.17.17.244";

    public static final String SERVICE_PORT = "8484";

    private static final String NAMESPACE = "http://tempuri.org/"; // 命名空間

    private static final String METHOD_NAME = "Execute_Script_TT"; // 方法名稱

    private static final String SOAP_ACTION1 = "http://tempuri.org/Execute_Script_TT"; // SOAP_ACTION

    private static final String URL = "http://172.17.17.244:8484/service.asmx"; // 網址

    //private String doc_type = "";

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


        String current_table = intent.getStringExtra("CURRENT_TABLE");
        String doc_type = intent.getStringExtra("DOC_TYPE");
        String rvu01 = intent.getStringExtra("RVU01");


        if (intent.getAction() != null) {
            if (intent.getAction().equals(Constants.ACTION.ACTION_EXECUTE_TT_ACTION)) {
                Log.i(TAG, "ACTION_EXECUTE_TT_ACTION");
            }
        }


        StringWriter writer = new StringWriter();
        XmlSerializer xmlSerializer = Xml.newSerializer();

        try {

            xmlSerializer.setOutput(writer);

            xmlSerializer.startDocument("UTF-8", true);

            xmlSerializer.startTag("", "printx");
            //declare
            //xs:schema
            xmlSerializer.startTag("", "xs:schema");
            xmlSerializer.attribute("", "id", "printx");
            xmlSerializer.attribute("", "xmlns:xs", "http://www.w3.org/2001/XMLSchema");
            xmlSerializer.attribute("", "xmlns:msdata", "urn:schemas-microsoft-com:xml-msdata");
            //xs:element
            xmlSerializer.startTag("", "xs:element");
            xmlSerializer.attribute("", "name", "printx");
            xmlSerializer.attribute("", "msdata:IsDataSet", "true");
            xmlSerializer.attribute("", "msdata:UseCurrentLocale", "true");
            //xs:complexType
            xmlSerializer.startTag("", "xs:complexType");
            //xs:choice
            xmlSerializer.startTag("", "xs:choice");
            xmlSerializer.attribute("", "minOccurs", "0");
            xmlSerializer.attribute("", "maxOccurs", "unbounded");
            //xs:element
            xmlSerializer.startTag("", "xs:element");
            xmlSerializer.attribute("", "name", "Table");
            //xs:complexType
            xmlSerializer.startTag("", "xs:complexType");
            //xs:sequence
            xmlSerializer.startTag("", "xs:sequence");
            //xs:element
            //script
            xmlSerializer.startTag("", "xs:element");
            xmlSerializer.attribute("", "name", "script");
            xmlSerializer.attribute("", "type", "xs:string");
            xmlSerializer.endTag("", "xs:element");

            xmlSerializer.endTag("", "xs:sequence");
            xmlSerializer.endTag("", "xs:complexType");
            xmlSerializer.endTag("", "xs:element");
            xmlSerializer.endTag("", "xs:choice");
            xmlSerializer.endTag("", "xs:complexType");
            xmlSerializer.endTag("", "xs:element");
            xmlSerializer.endTag("", "xs:schema");
            //end tag <schema>
            //table start
            xmlSerializer.startTag("", "Table");
            xmlSerializer.startTag("", "script");
            xmlSerializer.text("sh run_me 1 1 " + rvu01 + " '" + doc_type + "'");
            xmlSerializer.endTag("", "script");
            xmlSerializer.endTag("", "Table");
            //table end
            xmlSerializer.endTag("", "printx");
            xmlSerializer.endDocument();

            Log.e(TAG, "xml = "+writer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }





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
                //Log.d(TAG, "==>10");

                //SoapObject ret = (SoapObject) resultsRequestSOAP.getProperty("Get_TT_doc_type_is_REG_or_SUBResult");

                //SoapObject ret  = (SoapObject )resultsRequestSOAP.getAttribute("Get_TT_doc_type_is_REG_or_SUBResult");

                //doc_type = ret.toString();
                String ret  = resultsRequestSOAP.getPrimitiveProperty("Execute_Script_TTResult").toString();
                Log.d(TAG, "ret = "+ret);

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

                if (Integer.valueOf(current_table) == dataTable.Rows.size() -1 ) { //the last one
                    Log.e(TAG, "send complete to stop!");
                    Intent getSuccessIntent = new Intent(Constants.ACTION.ACTION_ENTERING_WAREHOUSE_COMPLETE);
                    sendBroadcast(getSuccessIntent);
                } else {
                    Log.e(TAG, "send current index back and go next");
                    Intent getSuccessIntent = new Intent(Constants.ACTION.ACTION_EXECUTE_TT_SUCCESS);
                    getSuccessIntent.putExtra("CURRENT_TABLE", current_table);
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


        } catch (Exception e) {
            // 抓到錯誤訊息

            e.printStackTrace();
            Intent getFailedIntent = new Intent(Constants.ACTION.ACTION_EXECUTE_TT_FAILED);
            //getFailedIntent.putExtra("DOC_TYPE", doc_type);
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
