package com.macauto.macautowarehouse.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.macauto.macautowarehouse.data.Constants;

import com.macauto.macautowarehouse.data.ReceivingInspectionItem;

import com.macauto.macautowarehouse.table.DataTable;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketTimeoutException;

import static com.macauto.macautowarehouse.MainActivity.web_soap_port;
import static com.macauto.macautowarehouse.ReceivingInspectionFragment.dataTable_TTCP;
import static com.macauto.macautowarehouse.ReceivingInspectionFragment.receivingList;
import static com.macauto.macautowarehouse.data.WebServiceParse.parseXmlToDataTable;

public class GetTTReceiveGoodsReportDataQCService extends IntentService {
    public static final String TAG = "ReceiveGoodsQCService";

    public static final String SERVICE_IP = "172.17.17.244";

    public static final String SERVICE_PORT = "8484";

    private static final String NAMESPACE = "http://tempuri.org/"; // 命名空間

    private static final String METHOD_NAME = "Get_TT_ReceiveGoods_report_Data_QC"; // 方法名稱

    private static final String SOAP_ACTION1 = "http://tempuri.org/Get_TT_ReceiveGoods_report_Data_QC"; // SOAP_ACTION
    //normal port 8000, test port 8484
    //private static final String URL = "http://172.17.17.244:"+web_soap_port+"/service.asmx"; // 網址

    public GetTTReceiveGoodsReportDataQCService() {
        super("GetTTReceiveGoodsReportDataQCService");
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

        String part_no = intent.getStringExtra("PART_NO");
        String barcode_no = intent.getStringExtra("BARCODE_NO");
        String k_id = intent.getStringExtra("K_ID");

        String URL = "http://172.17.17.244:"+web_soap_port+"/service.asmx"; // 網址

        Log.e(TAG, "part_no = "+part_no);
        Log.e(TAG, "barcode_no = "+barcode_no);
        Log.e(TAG, "URL = "+URL);

        //device_id = intent.getStringExtra("DEVICE_ID");
        //String service_ip = intent.getStringExtra(SERVICE_IP);
        //String service_port = intent.getStringExtra(SERVICE_PORT);

        //String combine_url = "http://"+SERVICE_IP+":"+SERVICE_PORT+"/service.asmx";

        if (intent.getAction() != null) {
            if (intent.getAction().equals(Constants.ACTION.ACTION_RECEIVING_INSPECTION_GET_TT_RECEIVE_GOODS_REPORT_DATA_QC_ACTION)) {
                Log.i(TAG, "ACTION_RECEIVING_INSPECTION_GET_TT_RECEIVE_GOODS_REPORT_DATA_QC_ACTION");
            }
        }



        try {
            // 建立一個 WebService 請求

            SoapObject request = new SoapObject(NAMESPACE,
                    METHOD_NAME);

            // 輸出值，帳號(account)、密碼(password)
            Log.e(TAG, "k_id = "+k_id);



            request.addProperty("SID", "MAT");
            request.addProperty("part_no", part_no);
            request.addProperty("barcode_no", barcode_no);
            request.addProperty("k_id", k_id);
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

                //append_record(String.valueOf(resultsRequestSOAP)+"\n\n\n\n", "test");

                SoapObject s_deals = (SoapObject) resultsRequestSOAP.getProperty("Get_TT_ReceiveGoods_report_Data_QCResult");

                if (dataTable_TTCP != null) {
                    dataTable_TTCP.clear();
                } else {
                    dataTable_TTCP = new DataTable();
                }

                dataTable_TTCP = parseXmlToDataTable(s_deals);

                if (dataTable_TTCP != null) {

                    Log.e(TAG, "dataTable_TTCP.Rows.size() = "+dataTable_TTCP.Rows.size());

                    if (dataTable_TTCP.Rows.size() == 0) {
                        Intent getSuccessIntent = new Intent(Constants.ACTION.ACTION_RECEIVING_INSPECTION_GET_TT_RECEIVE_GOODS_REPORT_DATA_QC_EMPTY);
                        sendBroadcast(getSuccessIntent);
                    } else {


                        for (int i=0; i < dataTable_TTCP.Rows.size(); i++) {
                            //String header = String.valueOf(i+1)+"#"+dataTable.getValue(i, 3).toString();
                            //no_list.add(header);
                            //detailList.put(header, new ArrayList<DetailItem>());

                            //add into stock check first
                            //DetailItem checkItem = new DetailItem();
                            //checkItem.setTitle(this.getResources().getString(R.string.item_title_confirm_stock_in));
                            //detailList.get(header).add(checkItem);

                            Log.e(TAG, "dataTable_TTCP.Columns.size() = "+dataTable_TTCP.Columns.size());

                            ReceivingInspectionItem item = new ReceivingInspectionItem();

                            item.setCheck_sp(Boolean.valueOf(dataTable_TTCP.Rows.get(i).getValue("check_sp").toString()));
                            item.setRva01(dataTable_TTCP.Rows.get(i).getValue("rva01").toString());
                            item.setRvb02(dataTable_TTCP.Rows.get(i).getValue("rvb02").toString());
                            item.setVend_name(dataTable_TTCP.Rows.get(i).getValue("vend_name").toString());
                            item.setIma102(dataTable_TTCP.Rows.get(i).getValue("ima102").toString());
                            item.setRvb05(dataTable_TTCP.Rows.get(i).getValue("rvb05").toString());
                            item.setIma021(dataTable_TTCP.Rows.get(i).getValue("ima021").toString());
                            item.setBmjsp(dataTable_TTCP.Rows.get(i).getValue("bmjsp").toString());
                            item.setTa_ima018(dataTable_TTCP.Rows.get(i).getValue("ta_ima018").toString());
                            item.setRva06(dataTable_TTCP.Rows.get(i).getValue("rva06").toString());
                            item.setRvb38(dataTable_TTCP.Rows.get(i).getValue("rvb38").toString());
                            item.setRvb01(dataTable_TTCP.Rows.get(i).getValue("rvb01").toString());
                            item.setRva05(dataTable_TTCP.Rows.get(i).getValue("rva05").toString());

                            receivingList.add(item);

                        }



                        Intent getSuccessIntent = new Intent(Constants.ACTION.ACTION_RECEIVING_INSPECTION_GET_TT_RECEIVE_GOODS_REPORT_DATA_QC_SUCCESS);
                        //getSuccessIntent.putExtra("TOTAL_COUNT", count_num);
                        sendBroadcast(getSuccessIntent);
                    }



                } else {
                    Intent getSuccessIntent = new Intent(Constants.ACTION.ACTION_RECEIVING_INSPECTION_GET_TT_RECEIVE_GOODS_REPORT_DATA_QC_EMPTY);
                    sendBroadcast(getSuccessIntent);
                }





            }




        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            Intent timeoutIntent = new Intent(Constants.ACTION.ACTION_SOCKET_TIMEOUT);
            sendBroadcast(timeoutIntent);
        } catch (Exception e) {
            // 抓到錯誤訊息

            e.printStackTrace();
            Intent getFailedIntent = new Intent(Constants.ACTION.ACTION_RECEIVING_INSPECTION_GET_TT_RECEIVE_GOODS_REPORT_DATA_QC_FAILED);
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
