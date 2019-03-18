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

import static com.macauto.macautowarehouse.MainActivity.dataTable;

import static com.macauto.macautowarehouse.MainActivity.global_sid;
import static com.macauto.macautowarehouse.MainActivity.service_ip;
import static com.macauto.macautowarehouse.MainActivity.web_soap_port;
import static com.macauto.macautowarehouse.data.WebServiceParse.parseDataTableToSoapObject;
import static com.macauto.macautowarehouse.data.WebServiceParse.parseDataTableToXml;

public class ConfirmEnteringWarehouseService extends IntentService {
    public static final String TAG = "ConfirmEnteringService";

    public static final String SERVICE_IP = "172.17.17.244";

    public static final String SERVICE_PORT = "8484";

    private static final String NAMESPACE = "http://tempuri.org/"; // 命名空間

    private static final String METHOD_NAME = "Update_TT_ReceiveGoods_IN_Rvv33"; // 方法名稱

    private static final String SOAP_ACTION1 = "http://tempuri.org/Update_TT_ReceiveGoods_IN_Rvv33"; // SOAP_ACTION

    //private static final String URL = "http://172.17.17.244:8484/service.asmx"; // 網址

    //private StringWriter writer;
    //private String rvu01="";

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

                String rvb33_string = dataTable.Rows.get(i).getValue("rvb33").toString();

                if (rvb33_string.contains(".")) {
                    String split_rvb33[] = rvb33_string.split("\\.");
                    if (Integer.valueOf(split_rvb33[1]) == 0) { // .0 .00 .000
                        dataTable.Rows.get(i).setValue("rvb33", split_rvb33[0]);
                    }
                }

                for (int j = 0; j < dataTable.Columns.size(); j++) {
                    System.out.print(dataTable.Rows.get(i).getValue(j));


                    //if (j == 1) {
                    //    rvu01 = dataTable.Rows.get(i).getValue(j).toString();
                    //}

                    if (j < dataTable.Columns.size() - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.print("\n");
            }
            Log.e(TAG, "========================================================");
        }

        Log.e(TAG, "parse to xml start");


    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.i(TAG, "Handle");

        if (intent.getAction() != null) {
            if (intent.getAction().equals(Constants.ACTION.ACTION_CONFIRM_ENTERING_WAREHOUSE_ACTION)) {
                Log.i(TAG, "ACTION_CONFIRM_ENTERING_WAREHOUSE_ACTION");
            }
        }

        String URL = "http://"+service_ip+":"+web_soap_port+"/service.asmx";
        Log.e(TAG, "URL = "+URL);

        //Log.e(TAG, "rvu01 = "+rvu01);

        String writer;

        if (dataTable != null) {

            dataTable.TableName = "GOODS_IN";

            writer = parseDataTableToXml(dataTable);
            SoapObject mySoap = parseDataTableToSoapObject("HAA", dataTable);
            //XmlToJson xmlToJson = new XmlToJson.Builder(writer.toString()).build();







            Log.d(TAG, "writer = "+writer);
            //Log.d(TAG, "xmlToJson = "+xmlToJson.toString());


            //String temp = "NewDataSet=anyType{schema=anyType{element=anyType{complexType=anyType{choice=anyType{element=anyType{complexType=anyType{sequence=anyType{element=anyType{}; element=anyType{}; element=anyType{}; element=anyType{}; element=anyType{}; element=anyType{}; element=anyType{}; element=anyType{}; element=anyType{}; element=anyType{}; element=anyType{}; element=anyType{}; }; }; }; }; }; }; }; diffgram=anyType{DocumentElement=anyType{INCP=anyType{check_sp=true; rvu01=12701-1809140051; rvv02=1; rvb05=48165A00195-0101; pmn041=導引器; ima021=滑塊 POM (F20-03 LOF)黑色 28.3*18*14.7mm D2SB天窗; rvv32=9110; rvv33=1G1211; rvv34=20180601*20180601*20180913; rvb33=2000.000; pmc03=寶泰模具; gen02=沈俊宏; rvv33_scan=1G1211; }; }; }; }";


            try {
                // 建立一個 WebService 請求

                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME);

                // 輸出值，帳號(account)、密碼(password)

                request.addProperty("SID", global_sid);

                //request.addProperty("HAA", writer.toString());
                //request.setInnerText("<HAA>"+writer+"</HAA>");
                request.addSoapObject(mySoap);
                //request.addProperty("HAA", writer);



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

                Log.e(TAG, "request = "+request.toString());

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
                    intent = new Intent(Constants.ACTION.ACTION_UPDATE_TT_RECEIVE_IN_RVV33_SUCCESS);
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
                intent = new Intent(Constants.ACTION.ACTION_UPDATE_TT_RECEIVE_IN_RVV33_FAILED);
                sendBroadcast(intent);

            }
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
