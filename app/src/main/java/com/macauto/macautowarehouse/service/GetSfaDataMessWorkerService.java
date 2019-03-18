package com.macauto.macautowarehouse.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.macauto.macautowarehouse.data.AllocationMsgStatusItem;
import com.macauto.macautowarehouse.data.Constants;
import com.macauto.macautowarehouse.table.DataRow;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import static com.macauto.macautowarehouse.MainActivity.global_sid;
import static com.macauto.macautowarehouse.MainActivity.service_ip;
import static com.macauto.macautowarehouse.MainActivity.sfaMessTable;
import static com.macauto.macautowarehouse.MainActivity.statusList;
import static com.macauto.macautowarehouse.MainActivity.emp_no;
import static com.macauto.macautowarehouse.MainActivity.web_soap_port;
import static com.macauto.macautowarehouse.data.WebServiceParse.parseXmlToDataTable;

public class GetSfaDataMessWorkerService extends IntentService {
    public static final String TAG = "GetSfaDataMessWorker";

    public static final String SERVICE_IP = "172.17.17.244";

    public static final String SERVICE_PORT = "8484";

    private static final String NAMESPACE = "http://tempuri.org/"; // 命名空間

    private static final String METHOD_NAME = "get_sfa_data_mess_Warehouse_worker"; // 方法名稱

    private static final String SOAP_ACTION1 = "http://tempuri.org/get_sfa_data_mess_Warehouse_worker"; // SOAP_ACTION

    //private static final String URL = "http://172.17.17.244:8484/service.asmx"; // 網址

    public GetSfaDataMessWorkerService() {
        super("GetSfaDataMessWorkerService");
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

        String made_no = intent.getStringExtra("MADE_NO");
        String req_qty = intent.getStringExtra("REQ_QTY");
        String stock_no = intent.getStringExtra("STOCK_NO");
        String locate_no = intent.getStringExtra("LOCATE_NO");
        String URL = "http://"+service_ip+":"+web_soap_port+"/service.asmx";
        //String stock_no = intent.getStringExtra("STOCK_NO");
        //String locate_no = intent.getStringExtra("LOCATE_NO");
        //String batch_no = intent.getStringExtra("BATCH_NO");
        //String ima02 = intent.getStringExtra("NAME");
        //String ima021 = intent.getStringExtra("SPEC");
        //String query_all = intent.getStringExtra("QUERY_ALL");

        Log.e(TAG, "made_no = "+made_no);
        Log.e(TAG, "req_qty = "+req_qty);
        Log.e(TAG, "stock_no = "+stock_no);
        Log.e(TAG, "locate_no = "+locate_no);
        //Log.e(TAG, "batch_no = "+batch_no);
        //Log.e(TAG, "ima02 = "+ima02);
        //Log.e(TAG, "ima021 = "+ima021);

        //device_id = intent.getStringExtra("DEVICE_ID");
        //String service_ip = intent.getStringExtra(SERVICE_IP);
        //String service_port = intent.getStringExtra(SERVICE_PORT);

        //String combine_url = "http://"+SERVICE_IP+":"+SERVICE_PORT+"/service.asmx";

        if (intent.getAction() != null) {
            if (intent.getAction().equals(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_ACTION)) {
                Log.i(TAG, "ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_ACTION");
            }
        }



        try {
            // 建立一個 WebService 請求

            SoapObject request = new SoapObject(NAMESPACE,
                    METHOD_NAME);

            // 輸出值，帳號(account)、密碼(password)

            request.addProperty("SID", global_sid);

            request.addProperty("made_no", made_no);
            request.addProperty("req_qty", Integer.valueOf(req_qty));
            request.addProperty("stock_no", stock_no);
            request.addProperty("locate_no", locate_no);
            request.addProperty("Warehouse_worker", emp_no);
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

                Intent getSuccessIntent = new Intent(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_FAILED);
                sendBroadcast(getSuccessIntent);
            } else {
                SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                Log.e(TAG, String.valueOf(resultsRequestSOAP));

                SoapObject s_deals = (SoapObject) resultsRequestSOAP.getProperty("get_sfa_data_mess_Warehouse_workerResult");


                sfaMessTable = parseXmlToDataTable(s_deals);

                if (sfaMessTable != null) {
                    if (sfaMessTable.Rows.size() == 0) {
                        Intent getSuccessIntent = new Intent(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_EMPTY);
                        sendBroadcast(getSuccessIntent);
                    } else {
                        Log.e(TAG, "sfaMessTable.Rows = "+sfaMessTable.Rows.size());
                        //original code 1334
                        int aw1, aw2, aw3, aw4, aw5;

                        for (DataRow rx : sfaMessTable.Rows) {
                            float aw1_float = Float.valueOf(rx.getValue("IMG10").toString());
                            aw1 = (int) aw1_float;
                            aw2 = Integer.valueOf(rx.getValue("MOVED_QTY").toString());
                            aw3 = Integer.valueOf(rx.getValue("SFA05").toString());
                            aw4 = Integer.valueOf(rx.getValue("TC_OBF013").toString());
                            float aw5_float = Float.valueOf(rx.getValue("MESS_QTY").toString());
                            aw5 = (int) aw5_float;

                            if (aw1 > (aw3 - (aw2 + aw5))) {
                                aw1 = aw3 - (aw2 + aw5);
                                aw1 = aw1 < 0 ? 0 : aw1;
                            }

                            aw1 = aw1 > aw4 ? aw4 : aw1;
                            rx.setValue("IMG10", String.valueOf(aw1));
                        }

                        /*Log.e(TAG, "========================================================");
                        for (int i=0; i<sfaMessTable.Rows.size(); i++) {

                            for (int j=0; j<sfaMessTable.Columns.size(); j++) {
                                System.out.print(sfaMessTable.Rows.get(i).getValue(j));
                                if (j < sfaMessTable.Columns.size() - 1) {
                                    System.out.print(", ");
                                }
                            }
                            System.out.print("\n");
                        }
                        Log.e(TAG, "========================================================");*/

                        for (int i=0; i < sfaMessTable.Rows.size(); i++) {
                            AllocationMsgStatusItem item = new AllocationMsgStatusItem();
                            Log.e(TAG, "value["+i+"] = "+sfaMessTable.getValue(i, 0).toString() );
                            //locateList.add(sfaMessTable.getValue(i, 0).toString());
                            for (int j=0; j < sfaMessTable.Columns.size(); j++) {
                                if (j == 0)
                                    item.setItem_SFA03(sfaMessTable.getValue(i, j).toString());
                                else if (j == 1)
                                    item.setItem_IMA021(sfaMessTable.getValue(i, j).toString());
                                else if (j == 2)
                                    item.setItem_SFA06(sfaMessTable.getValue(i, j).toString());
                                else if (j == 3)
                                    item.setItem_SFA063(sfaMessTable.getValue(i, j).toString());
                                else if (j == 4)
                                    item.setItem_SFA12(sfaMessTable.getValue(i, j).toString());
                                else if (j == 5)
                                    item.setItem_SFA161(sfaMessTable.getValue(i, j).toString());
                                else if (j == 6)
                                    item.setItem_SFA05(sfaMessTable.getValue(i, j).toString());
                                else if (j == 7)
                                    item.setItem_MOVED_QTY(sfaMessTable.getValue(i, j).toString());
                                else if (j == 8)
                                    item.setItem_MESS_QTY(sfaMessTable.getValue(i, j).toString());
                                else if (j == 9)
                                    item.setItem_IMG10(sfaMessTable.getValue(i, j).toString());
                                else if (j == 10)
                                    item.setItem_IN_STOCK_NO(sfaMessTable.getValue(i, j).toString());
                                else if (j == 11)
                                    item.setItem_IN_LOCATE_NO(sfaMessTable.getValue(i, j).toString());
                                else if (j == 12)
                                    item.setItem_SCAN_SP(sfaMessTable.getValue(i, j).toString());
                                else if (j == 13)
                                    item.setItem_SFA11(sfaMessTable.getValue(i, j).toString());
                                else if (j == 14)
                                    item.setItem_SFA11_NAME(sfaMessTable.getValue(i, j).toString());
                                else if (j == 15)
                                    item.setItem_TC_OBF013(sfaMessTable.getValue(i, j).toString());
                                else if (j == 16)
                                    item.setItem_INV_QTY(sfaMessTable.getValue(i, j).toString());
                                else if (j == 17)
                                    item.setItem_SFA30(sfaMessTable.getValue(i, j).toString());
                            }

                            statusList.add(item);
                        }

                        Intent getSuccessIntent = new Intent(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_SUCCESS);
                        //getSuccessIntent.putExtra("RECORDS", String.valueOf(lookUpDataTable.Rows.size()));
                        sendBroadcast(getSuccessIntent);
                    }
                } else {
                    Intent getSuccessIntent = new Intent(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_EMPTY);
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
            Intent getFailedIntent = new Intent(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_FAILED);
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
