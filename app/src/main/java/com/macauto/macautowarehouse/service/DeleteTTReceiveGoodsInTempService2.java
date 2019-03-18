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

public class DeleteTTReceiveGoodsInTempService2 extends IntentService {
    public static final String TAG = "DeleteGoodsInTemp2";

    public static final String SERVICE_IP = "172.17.17.244";

    public static final String SERVICE_PORT = "8484";

    private static final String NAMESPACE = "http://tempuri.org/"; // 命名空間

    private static final String METHOD_NAME = "Delete_TT_ReceiveGoods_IN__in_no_Temp"; // 方法名稱

    private static final String SOAP_ACTION1 = "http://tempuri.org/Delete_TT_ReceiveGoods_IN__in_no_Temp"; // SOAP_ACTION

    //private static final String URL = "http://172.17.17.244:8484/service.asmx"; // 網址

    //private StringWriter writer;
    //private String rvu01="";

    public DeleteTTReceiveGoodsInTempService2() {
        super("DeleteTTReceiveGoodsInTempService2");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.i(TAG, "Handle");

        if (intent.getAction() != null) {
            if (intent.getAction().equals(Constants.ACTION.ACTION_DELETE_TT_RECEIVE_GOODS_IN_TEMP2_ACTION)) {
                Log.i(TAG, "ACTION_DELETE_TT_RECEIVE_GOODS_IN_TEMP2_ACTION");
            }
        }

        String in_no = intent.getStringExtra("IN_NO");
        String item_no = intent.getStringExtra("ITEM_NO");

        String URL = "http://"+service_ip+":"+web_soap_port+"/service.asmx";
        Log.e(TAG, "URL = "+URL);

        Log.e(TAG, "in_no = "+in_no);
        Log.e(TAG, "item_no = "+item_no);

        //Log.e(TAG, "rvu01 = "+rvu01);

        try {
            // 建立一個 WebService 請求

            SoapObject request = new SoapObject(NAMESPACE,
                    METHOD_NAME);

            // 輸出值，帳號(account)、密碼(password)

            request.addProperty("k_id", k_id);
            request.addProperty("in_no", in_no);
            request.addProperty("item_no", item_no);
            //request.addProperty("HAA", writer.toString());

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
                intent = new Intent(Constants.ACTION.SOAP_CONNECTION_FAIL);
                sendBroadcast(intent);
            } else {
                SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                Log.e(TAG, String.valueOf(resultsRequestSOAP));




                //result.setText(String.valueOf(resultsRequestSOAP));
                    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        InputStream stream = new ByteArrayInputStream(String.valueOf(resultsRequestSOAP).getBytes(StandardCharsets.UTF_8));
                        //LoadAndParseXML(stream);
                    } else {
                        InputStream stream = new ByteArrayInputStream(String.valueOf(resultsRequestSOAP).getBytes(Charset.forName("UTF-8")));
                        //LoadAndParseXML(stream);
                    }*/
                intent = new Intent(Constants.ACTION.ACTION_DELETE_TT_RECEIVE_GOODS_IN_TEMP2_SUCCESS);
                sendBroadcast(intent);
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
            intent = new Intent(Constants.ACTION.ACTION_DELETE_TT_RECEIVE_GOODS_IN_TEMP2_FAILED);
            sendBroadcast(intent);

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
        //Intent intent = new Intent(Constants.ACTION.GET_MESSAGE_LIST_COMPLETE);
        //sendBroadcast(intent);


    }
}
