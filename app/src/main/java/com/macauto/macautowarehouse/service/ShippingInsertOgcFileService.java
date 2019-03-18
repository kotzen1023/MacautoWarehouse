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

import static com.macauto.macautowarehouse.MainActivity.service_ip;
import static com.macauto.macautowarehouse.MainActivity.web_soap_port;
import static com.macauto.macautowarehouse.ShipmentFragment.table_SCX;
import static com.macauto.macautowarehouse.data.WebServiceParse.parseDataTableToSoapObject;

import static com.macauto.macautowarehouse.data.WebServiceParse.parseToBoolean;

public class ShippingInsertOgcFileService extends IntentService {
    public static final String TAG = "InsertOgcFile";

    public static final String SERVICE_IP = "172.17.17.244";

    public static final String SERVICE_PORT = "8484";

    private static final String NAMESPACE = "http://tempuri.org/"; // 命名空間

    private static final String METHOD_NAME = "SHIPPING_insert_ogc_file"; // 方法名稱

    private static final String SOAP_ACTION1 = "http://tempuri.org/SHIPPING_insert_ogc_file"; // SOAP_ACTION

    //private static final String URL = "http://172.17.17.244:8484/service.asmx"; // 網址

    //private StringWriter writer;
    //private String rvu01="";
    //private boolean is_success = false;

    public ShippingInsertOgcFileService() {
        super("ShippingInsertOgcFileService");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");


        Log.e(TAG, "parse to xml start");


    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.i(TAG, "Handle");

        if (intent.getAction() != null) {
            if (intent.getAction().equals(Constants.ACTION.ACTION_SHIPMENT_SHIPPING_INSERT_OGC_FILE_ACTION)) {
                Log.i(TAG, "ACTION_SHIPMENT_SHIPPING_INSERT_OGC_FILE_ACTION");
            }
        }




        String URL = "http://"+service_ip+":"+web_soap_port+"/service.asmx";
        Log.e(TAG, "URL = "+URL);

        //Log.e(TAG, "rvu01 = "+rvu01);

        //String writer;
        boolean is_success;

        if (table_SCX != null) {

            //writer = parseDataTableToXml(table_SCX);
            SoapObject mySoap = parseDataTableToSoapObject("HAA", table_SCX);

            /*XmlSerializer xmlSerializer = Xml.newSerializer();

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
                //check_sp
                xmlSerializer.startTag("", "xs:element");
                xmlSerializer.attribute("", "name", "check_sp");
                xmlSerializer.attribute("", "type", "xs:boolean");
                xmlSerializer.endTag("", "xs:element");
                //rvu01
                xmlSerializer.startTag("", "xs:element");
                xmlSerializer.attribute("", "name", "rvu01");
                xmlSerializer.attribute("", "type", "xs:string");
                xmlSerializer.attribute("", "minOccurs", "0");
                xmlSerializer.endTag("", "xs:element");
                //rvu02
                xmlSerializer.startTag("", "xs:element");
                xmlSerializer.attribute("", "name", "rvu02");
                xmlSerializer.attribute("", "type", "xs:string");
                xmlSerializer.attribute("", "minOccurs", "0");
                xmlSerializer.endTag("", "xs:element");
                //rvb05
                xmlSerializer.startTag("", "xs:element");
                xmlSerializer.attribute("", "name", "rvb05");
                xmlSerializer.attribute("", "type", "xs:string");
                xmlSerializer.attribute("", "minOccurs", "0");
                xmlSerializer.endTag("", "xs:element");
                //pmn041
                xmlSerializer.startTag("", "xs:element");
                xmlSerializer.attribute("", "name", "pmn041");
                xmlSerializer.attribute("", "type", "xs:string");
                xmlSerializer.attribute("", "minOccurs", "0");
                xmlSerializer.endTag("", "xs:element");
                //ima021
                xmlSerializer.startTag("", "xs:element");
                xmlSerializer.attribute("", "name", "ima021");
                xmlSerializer.attribute("", "type", "xs:string");
                xmlSerializer.attribute("", "minOccurs", "0");
                xmlSerializer.endTag("", "xs:element");
                //rvv32
                xmlSerializer.startTag("", "xs:element");
                xmlSerializer.attribute("", "name", "rvv32");
                xmlSerializer.attribute("", "type", "xs:string");
                xmlSerializer.attribute("", "minOccurs", "0");
                xmlSerializer.endTag("", "xs:element");
                //rvv33
                xmlSerializer.startTag("", "xs:element");
                xmlSerializer.attribute("", "name", "rvv33");
                xmlSerializer.attribute("", "type", "xs:string");
                xmlSerializer.attribute("", "minOccurs", "0");
                xmlSerializer.endTag("", "xs:element");
                //rvv34
                xmlSerializer.startTag("", "xs:element");
                xmlSerializer.attribute("", "name", "rvv34");
                xmlSerializer.attribute("", "type", "xs:string");
                xmlSerializer.attribute("", "minOccurs", "0");
                xmlSerializer.endTag("", "xs:element");
                //rvb33
                xmlSerializer.startTag("", "xs:element");
                xmlSerializer.attribute("", "name", "rvb33");
                xmlSerializer.attribute("", "type", "xs:string");
                xmlSerializer.attribute("", "minOccurs", "0");
                xmlSerializer.endTag("", "xs:element");
                //pmc03
                xmlSerializer.startTag("", "xs:element");
                xmlSerializer.attribute("", "name", "pmc03");
                xmlSerializer.attribute("", "type", "xs:string");
                xmlSerializer.attribute("", "minOccurs", "0");
                xmlSerializer.endTag("", "xs:element");
                //gen02
                xmlSerializer.startTag("", "xs:element");
                xmlSerializer.attribute("", "name", "gen02");
                xmlSerializer.attribute("", "type", "xs:string");
                xmlSerializer.attribute("", "minOccurs", "0");
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
                for (int i = 0; i < dataTable.Rows.size(); i++) {
                    xmlSerializer.startTag("", "Table");
                    for (int j = 0; j < dataTable.Columns.size(); j++) {
                        if (j==0) {
                            xmlSerializer.startTag("", "check_sp");
                            xmlSerializer.text(dataTable.Rows.get(i).getValue(j).toString());
                            xmlSerializer.endTag("", "check_sp");
                        } else if (j == 1) {
                            xmlSerializer.startTag("", "rvu01");
                            xmlSerializer.text(dataTable.Rows.get(i).getValue(j).toString());
                            xmlSerializer.endTag("", "rvu01");
                        } else if (j == 2) {
                            xmlSerializer.startTag("", "rvu02");
                            xmlSerializer.text(dataTable.Rows.get(i).getValue(j).toString());
                            xmlSerializer.endTag("", "rvu02");
                        } else if (j == 3) {
                            xmlSerializer.startTag("", "rvb05");
                            xmlSerializer.text(dataTable.Rows.get(i).getValue(j).toString());
                            xmlSerializer.endTag("", "rvb05");
                        } else if (j == 4) {
                            xmlSerializer.startTag("", "pmn041");
                            xmlSerializer.text(dataTable.Rows.get(i).getValue(j).toString());
                            xmlSerializer.endTag("", "pmn041");
                        } else if (j == 5) {
                            xmlSerializer.startTag("", "ima021");
                            xmlSerializer.text(dataTable.Rows.get(i).getValue(j).toString());
                            xmlSerializer.endTag("", "ima021");
                        } else if (j == 6) {
                            xmlSerializer.startTag("", "rvv32");
                            xmlSerializer.text(dataTable.Rows.get(i).getValue(j).toString());
                            xmlSerializer.endTag("", "rvv32");
                        } else if (j == 7) {
                            xmlSerializer.startTag("", "rvv33");
                            xmlSerializer.text(dataTable.Rows.get(i).getValue(j).toString());
                            xmlSerializer.endTag("", "rvv33");
                        } else if (j == 8) {
                            xmlSerializer.startTag("", "rvv34");
                            xmlSerializer.text(dataTable.Rows.get(i).getValue(j).toString());
                            xmlSerializer.endTag("", "rvv34");
                        } else if (j == 9) {
                            xmlSerializer.startTag("", "rvb33");
                            xmlSerializer.text(dataTable.Rows.get(i).getValue(j).toString());
                            xmlSerializer.endTag("", "rvb33");
                        } else if (j == 10) {
                            xmlSerializer.startTag("", "pmc03");
                            xmlSerializer.text(dataTable.Rows.get(i).getValue(j).toString());
                            xmlSerializer.endTag("", "pmc03");
                        } else if (j == 11) {
                            xmlSerializer.startTag("", "gen02");
                            xmlSerializer.text(dataTable.Rows.get(i).getValue(j).toString());
                            xmlSerializer.endTag("", "gen02");
                        }



                    }
                    //System.out.print("\n");
                    xmlSerializer.endTag("", "Table");
                }
                //table end
                xmlSerializer.endTag("", "printx");
                xmlSerializer.endDocument();

                Log.e(TAG, "xml = "+writer.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }*/



            try {
                // 建立一個 WebService 請求

                SoapObject request = new SoapObject(NAMESPACE,
                        METHOD_NAME);

                // 輸出值，帳號(account)、密碼(password)

                request.addProperty("SID", "MAT");
                //request.addProperty("HAA", writer);
                request.addSoapObject(mySoap);

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

                    is_success = parseToBoolean(resultsRequestSOAP);

                    Intent resultIntent;
                    if (!is_success) {
                        resultIntent = new Intent(Constants.ACTION.ACTION_SHIPMENT_SHIPPING_INSERT_OGC_FILE_FAILED);
                        sendBroadcast(resultIntent);
                    } else {
                        resultIntent = new Intent(Constants.ACTION.ACTION_SHIPMENT_SHIPPING_INSERT_OGC_FILE_SUCCESS);
                        sendBroadcast(resultIntent);
                    }
                    //result.setText(String.valueOf(resultsRequestSOAP));
                    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        InputStream stream = new ByteArrayInputStream(String.valueOf(resultsRequestSOAP).getBytes(StandardCharsets.UTF_8));
                        //LoadAndParseXML(stream);
                    } else {
                        InputStream stream = new ByteArrayInputStream(String.valueOf(resultsRequestSOAP).getBytes(Charset.forName("UTF-8")));
                        //LoadAndParseXML(stream);
                    }*/
                    //intent = new Intent(Constants.ACTION.ACTION_SHIPMENT_SHIPPING_INSERT_OGC_FILE_SUCCESS);
                    //sendBroadcast(intent);
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
                intent = new Intent(Constants.ACTION.ACTION_SHIPMENT_SHIPPING_INSERT_OGC_FILE_FAILED);
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
