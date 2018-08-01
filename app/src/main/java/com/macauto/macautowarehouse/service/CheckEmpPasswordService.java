package com.macauto.macautowarehouse.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.util.Xml;

import com.macauto.macautowarehouse.data.Constants;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static com.macauto.macautowarehouse.MainActivity.web_soap_port;
import static com.macauto.macautowarehouse.data.WebServiceParse.parseToBoolean;

public class CheckEmpPasswordService extends IntentService {
    public static final String TAG = "CheckEmpPasswordService";

    public static final String SERVICE_IP = "172.17.17.244";

    public static final String SERVICE_PORT = "8484";

    private static final String NAMESPACE = "http://tempuri.org/"; // 命名空間

    private static final String METHOD_NAME = "check_emp_password"; // 方法名稱

    private static final String SOAP_ACTION1 = "http://tempuri.org/check_emp_password"; // SOAP_ACTION
    //normal port 8000, test port 8484
    //private static final String URL = "http://172.17.17.244:8484/service.asmx"; // 網址

    public CheckEmpPasswordService() {
        super("CheckEmpPasswordService");
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


        //String device_id;

        String emp_no = intent.getStringExtra("EMP_NO");
        String emp_password = intent.getStringExtra("EMP_PASSWORD");
        String URL = "http://172.17.17.244:"+web_soap_port+"/service.asmx";
        Log.e(TAG, "URL = "+URL);

        //device_id = intent.getStringExtra("DEVICE_ID");
        //String service_ip = intent.getStringExtra(SERVICE_IP);
        //String service_port = intent.getStringExtra(SERVICE_PORT);

        //String combine_url = "http://"+SERVICE_IP+":"+SERVICE_PORT+"/service.asmx";

        if (intent.getAction() != null) {
            if (intent.getAction().equals(Constants.ACTION.ACTION_CHECK_EMP_PASSWORD_ACTION)) {
                Log.i(TAG, "ACTION_CHECK_EMP_PASSWORD_ACTION");
            }
        }



        try {
            // 建立一個 WebService 請求

            SoapObject request = new SoapObject(NAMESPACE,
                    METHOD_NAME);

            // 輸出值，帳號(account)、密碼(password)

            //request.addProperty("SID", "MAT");
            request.addProperty("user_no", emp_no);
            request.addProperty("password", emp_password);
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
                Intent decryptDoneIntent = new Intent(Constants.ACTION.SOAP_CONNECTION_FAIL);
                sendBroadcast(decryptDoneIntent);
            } else {
                Intent loginResultIntent;
                SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                Log.d(TAG, String.valueOf(resultsRequestSOAP));

                boolean ret = parseToBoolean(resultsRequestSOAP);

                Log.e(TAG, "parseToBoolean = "+ ret);

                if (ret) {
                    //Log.e(TAG, "ret = true");
                    loginResultIntent = new Intent(Constants.ACTION.ACTION_CHECK_EMP_PASSWORD_SUCCESS);
                    sendBroadcast(loginResultIntent);
                } else {
                    //Log.e(TAG, "ret = false");
                    loginResultIntent = new Intent(Constants.ACTION.ACTION_CHECK_EMP_PASSWORD_FAILED);
                    sendBroadcast(loginResultIntent);
                }
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    InputStream stream = new ByteArrayInputStream(String.valueOf(resultsRequestSOAP).getBytes(StandardCharsets.UTF_8));
                    LoadAndParseXML(stream);
                } else {
                    InputStream stream = new ByteArrayInputStream(String.valueOf(resultsRequestSOAP).getBytes(Charset.forName("UTF-8")));
                    LoadAndParseXML(stream);
                }*/

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
            //Intent decryptDoneIntent = new Intent(Constants.ACTION.SOAP_CONNECTION_FAIL);
            //sendBroadcast(decryptDoneIntent);
        }

        //MeetingAlarm.last_sync_setting = sync_option;
        //Intent decryptDoneIntent = new Intent(Constants.ACTION.GET_PERSONAL_MEETING_LIST_COMPLETE);
        //sendBroadcast(decryptDoneIntent);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
        //Intent intent = new Intent(Constants.ACTION.GET_MESSAGE_LIST_COMPLETE);
        //sendBroadcast(intent);
    }

    public void LoadAndParseXML(InputStream xmlString) {

        //notifyList.clear();
        //historyItemArrayList.clear();

        //Intent intentClear = new Intent(Constants.ACTION.GET_MESSAGE_LIST_CLEAR);
        //sendBroadcast(intentClear);

        XmlPullParser pullParser = Xml.newPullParser();
        //int i=0;
        //String value="";
        String tag_start, tag_value="";
        //boolean start_get_item_from_tag = false;
        try {
            pullParser.setInput(xmlString, "utf-8");

            //利用eventType來判斷目前分析到XML是哪一個部份
            int eventType = pullParser.getEventType();
            //XmlPullParser.END_DOCUMENT表示已經完成分析XML
            //HistoryItem item = null;
            //ArrayList<String> myArrayList = new ArrayList<>();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                //i++;
                //XmlPullParser.START_TAG表示目前分析到的是XML的Tag，如<title>

                if (eventType == XmlPullParser.START_TAG) {
                    tag_start = pullParser.getName();
                    Log.e(TAG, "<"+tag_start+">");
                    if (tag_start.equals("fxs")) {
                        Log.i(TAG, "=== Start of Message record ===");
                        //myArrayList.clear();
                        //item = new HistoryItem();
                    }
                }
                //XmlPullParser.TEXT表示目前分析到的是XML Tag的值，如：台南美食吃不完
                if (eventType == XmlPullParser.TEXT) {
                    tag_value = pullParser.getText();
                    //Log.e(TAG, "value = "+tag_value);

                    //tv02.setText(tv02.getText() + ", " + value);
                }

                if (eventType == XmlPullParser.END_TAG) {
                    String name = pullParser.getName();
                    Log.e(TAG, "value = "+tag_value);
                    //myArrayList.add(tag_value);
                    Log.e(TAG, "</"+name+">");

                    /*if (name != null && item != null) {

                        switch (name) {
                            case "message_id":
                                item.setMsg_id(tag_value);
                                break;
                            case "message_code":
                                item.setMsg_code(tag_value);
                                break;
                            case "message_title":
                                item.setMsg_title(tag_value);
                                break;
                            case "message_content":
                                item.setMsg_content(tag_value);
                                break;
                            case "announce_date":
                                item.setAnnounce_date(tag_value);
                                break;
                            case "internal_doc_no":
                                item.setInternal_doc_no(tag_value);
                                break;
                            case "internal_part_no":
                                item.setInternal_part_no(tag_value);
                                break;
                            case "internal_model_no":
                                item.setInternal_model_no(tag_value);
                                break;
                            case "internal_machine_no":
                                item.setInternal_machine_no(tag_value);
                                break;
                            case "internal_plant_no":
                                item.setInternal_plant_no(tag_value);
                                break;
                            case "announcer":
                                item.setAnnouncer(tag_value);
                                break;

                            case "ime_code":
                                item.setIme_code(tag_value);
                                break;

                            case "read_sp":
                                if (tag_value.equals("Y")) {
                                    item.setRead_sp(true);
                                } else {
                                    item.setRead_sp(false);
                                }
                                break;

                            default:
                                break;
                        }

                        if (name.equals("fxs")) {
                            Log.i(TAG, "=== End of Message record ===");
                            //historyItemArrayList.add(item);
                            Intent intentData = new Intent(Constants.ACTION.GET_MESSAGE_DATA);
                            intentData.putExtra("message_id", item.getMsg_id());
                            intentData.putExtra("message_code", item.getMsg_code());
                            intentData.putExtra("message_title", item.getMsg_title());
                            intentData.putExtra("message_content", item.getMsg_content());
                            intentData.putExtra("announce_date", item.getAnnounce_date());
                            intentData.putExtra("internal_doc_no", item.getInternal_doc_no());
                            intentData.putExtra("internal_part_no", item.getInternal_part_no());
                            intentData.putExtra("internal_model_no", item.getInternal_model_no());
                            intentData.putExtra("internal_machine_no", item.getInternal_machine_no());
                            intentData.putExtra("internal_plant_no", item.getInternal_plant_no());
                            intentData.putExtra("announcer", item.getAnnouncer());
                            intentData.putExtra("ime_code", item.getIme_code());
                            intentData.putExtra("read_sp", item.isRead_sp());
                            sendBroadcast(intentData);
                        }
                    }*/
                }
                //分析下一個XML Tag
                try {
                    eventType = pullParser.next();
                } catch (XmlPullParserException ep) {
                    ep.printStackTrace();
                }
            }

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }


    }
}
