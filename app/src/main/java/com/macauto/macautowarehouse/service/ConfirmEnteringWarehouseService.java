package com.macauto.macautowarehouse.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.util.Xml;

import com.macauto.macautowarehouse.R;
import com.macauto.macautowarehouse.data.Constants;
import com.macauto.macautowarehouse.data.DetailItem;
import com.macauto.macautowarehouse.data.InspectedReceiveItem;
import com.macauto.macautowarehouse.table.DataRow;
import com.macauto.macautowarehouse.table.DataTable;

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
import java.util.ArrayList;

import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.dataTable;
import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.detailList;
import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.no_list;

public class ConfirmEnteringWarehouseService extends IntentService {
    public static final String TAG = "ConfirmEnteringService";

    public static final String SERVICE_IP = "172.17.17.244";

    public static final String SERVICE_PORT = "8484";

    private static final String NAMESPACE = "http://tempuri.org/"; // 命名空間

    private static final String METHOD_NAME = "Update_TT_ReceiveGoods_IN_Rvv33"; // 方法名稱

    private static final String SOAP_ACTION1 = "http://tempuri.org/Update_TT_ReceiveGoods_IN_Rvv33"; // SOAP_ACTION

    private static final String URL = "http://172.17.17.244:8484/service.asmx"; // 網址

    public ConfirmEnteringWarehouseService() {
        super("ConfirmEnteringWarehouseService");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");



        if (dataTable != null) {

            Log.e(TAG, "========================================================");
            for (int i = 0; i < dataTable.Rows.size(); i++) {

                for (int j = 0; j < dataTable.Columns.size(); j++) {
                    System.out.print(dataTable.Rows.get(i).getValue(j));
                    if (j < dataTable.Columns.size() - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.print("\n");
            }
            Log.e(TAG, "========================================================");
        }
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.i(TAG, "Handle");

        if (intent.getAction() != null) {
            if (intent.getAction().equals(Constants.ACTION.ACTION_CONFIRM_ENTERING_WAREHOUSE_ACTION)) {
                Log.i(TAG, "ACTION_CONFIRM_ENTERING_WAREHOUSE_ACTION");
            }
        }


        if (dataTable != null) {

            /*try {
                // 建立一個 WebService 請求

                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME);

                // 輸出值，帳號(account)、密碼(password)

                request.addProperty("SID", "MAT");

                request.addProperty("HAA", dataTable);
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
                } else {
                    SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                    Log.e(TAG, String.valueOf(resultsRequestSOAP));

                    //append_record(String.valueOf(resultsRequestSOAP)+"\n\n\n\n", "test");


                }



            } catch (Exception e) {
                // 抓到錯誤訊息

                e.printStackTrace();
                Intent getFailedIntent = new Intent(Constants.ACTION.ACTION_UPDATE_TT_RECEIVE_IN_RVV33_FAILED);
                sendBroadcast(getFailedIntent);
            }

            //MeetingAlarm.last_sync_setting = sync_option;
            Intent getSuccessIntent = new Intent(Constants.ACTION.ACTION_UPDATE_TT_RECEIVE_IN_RVV33_SUCCESS);
            sendBroadcast(getSuccessIntent);*/
        } else {
            Log.e(TAG, "dataTable = null");
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
