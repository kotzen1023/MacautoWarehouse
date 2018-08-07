package com.macauto.macautowarehouse.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.util.Xml;

import com.macauto.macautowarehouse.R;
import com.macauto.macautowarehouse.data.Constants;
import com.macauto.macautowarehouse.data.DetailItem;
import com.macauto.macautowarehouse.data.GenerateRandomString;
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
import java.sql.ResultSet;
import java.util.ArrayList;

import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.check_stock_in;
import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.dataTable;
import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.detailList;

import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.inspectedReceiveExpanedAdater;
import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.no_list;
import static com.macauto.macautowarehouse.MainActivity.k_id;
import static com.macauto.macautowarehouse.MainActivity.web_soap_port;
import static com.macauto.macautowarehouse.data.FileOperation.append_record;
import static com.macauto.macautowarehouse.data.WebServiceParse.parseXmlToDataTable;

public class GetReceiveGoodsInDataService extends IntentService {
    public static final String TAG = "GetReceiveService";

    public static final String SERVICE_IP = "172.17.17.244";

    public static final String SERVICE_PORT = "8484";

    private static final String NAMESPACE = "http://tempuri.org/"; // 命名空間

    private static final String METHOD_NAME = "Get_TT_ReceiveGoods_IN_Data"; // 方法名稱

    private static final String SOAP_ACTION1 = "http://tempuri.org/Get_TT_ReceiveGoods_IN_Data"; // SOAP_ACTION
    //normal port 8000, test port 8484
    //private static final String URL = "http://172.17.17.244:"+web_soap_port+"/service.asmx"; // 網址

    public GetReceiveGoodsInDataService() {
        super("GetReceiveGoodsInDataService");
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
            if (intent.getAction().equals(Constants.ACTION.ACTION_CHECK_RECEIVE_GOODS)) {
                Log.i(TAG, "GET_MESSAGE_LIST_ACTION");
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
                Intent getFailedIntent = new Intent(Constants.ACTION.ACTION_GET_INSPECTED_RECEIVE_ITEM_FAILED);
                sendBroadcast(getFailedIntent);
            } else {
                SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                Log.e(TAG, String.valueOf(resultsRequestSOAP));

                //append_record(String.valueOf(resultsRequestSOAP)+"\n\n\n\n", "test");

                SoapObject s_deals = (SoapObject) resultsRequestSOAP.getProperty("Get_TT_ReceiveGoods_IN_DataResult");

                if (dataTable != null) {
                    dataTable.clear();
                } else {
                    dataTable = new DataTable();
                }

                dataTable = parseXmlToDataTable(s_deals);

                if (dataTable != null) {

                    Log.e(TAG, "dataTable.Rows.size() = "+dataTable.Rows.size());

                    if (dataTable.Rows.size() == 0) {
                        Intent getSuccessIntent = new Intent(Constants.ACTION.ACTION_GET_INSPECTED_RECEIVE_ITEM_EMPTY);
                        sendBroadcast(getSuccessIntent);
                    } else {
                        for (int i=0; i < dataTable.Rows.size(); i++) {
                            String header = String.valueOf(i+1)+"#"+dataTable.getValue(i, 3).toString();
                            no_list.add(header);
                            check_stock_in.add(false);
                            detailList.put(header, new ArrayList<DetailItem>());

                            //add into stock check first
                            DetailItem checkItem = new DetailItem();
                            checkItem.setTitle(this.getResources().getString(R.string.item_title_confirm_stock_in));
                            detailList.get(header).add(checkItem);

                            Log.e(TAG, "dataTable.Columns.size() = "+dataTable.Columns.size());

                            for (int j=0; j < dataTable.Columns.size(); j++) {
                                DetailItem item = new DetailItem();

                                if (j==0) {
                                    item.setTitle(this.getResources().getString(R.string.item_title_check_sp));

                                } else if (j==1) {
                                    item.setTitle(this.getResources().getString(R.string.item_title_rvu01));
                                } else if (j==2) {
                                    item.setTitle(this.getResources().getString(R.string.item_title_rvv02));
                                } else if (j==3) {
                                    item.setTitle(this.getResources().getString(R.string.item_title_rvb05));
                                } else if (j==4) {
                                    item.setTitle(this.getResources().getString(R.string.item_title_pmn041));
                                } else if (j==5) {
                                    item.setTitle(this.getResources().getString(R.string.item_title_ima021));
                                } else if (j==6) {
                                    item.setTitle(this.getResources().getString(R.string.item_title_rvv32));
                                } else if (j==7) {
                                    item.setTitle(this.getResources().getString(R.string.item_title_rvv33));
                                } else if (j==8) {
                                    item.setTitle(this.getResources().getString(R.string.item_title_rvv34));
                                } else if (j==9) {
                                    item.setTitle(this.getResources().getString(R.string.item_title_rvb33));
                                } else if (j==10) {
                                    item.setTitle(this.getResources().getString(R.string.item_title_pmc03));
                                } else if (j==11) {
                                    item.setTitle(this.getResources().getString(R.string.item_title_gen02));
                                }
                                item.setName(dataTable.getValue(i,j).toString());


                                if (item != null)
                                    detailList.get(header).add(item);
                            }



                        }

                        Intent getSuccessIntent = new Intent(Constants.ACTION.ACTION_GET_INSPECTED_RECEIVE_ITEM_SUCCESS);
                        sendBroadcast(getSuccessIntent);
                    }



                } else {
                    Intent getSuccessIntent = new Intent(Constants.ACTION.ACTION_GET_INSPECTED_RECEIVE_ITEM_EMPTY);
                    sendBroadcast(getSuccessIntent);
                }


                /*Log.d(TAG, "count = "+s_deals.getPropertyCount());

                SoapObject property = (SoapObject) s_deals.getProperty(1);



                //SoapObject sub_property = (SoapObject) property.getProperty(0);

                int count = property.getPropertyCount();

                if (count > 0) {
                    SoapObject sub_property = (SoapObject) property.getProperty(0);

                    Log.d(TAG, "object = "+sub_property);
                    Log.d(TAG, "count = "+sub_property.getPropertyCount());

                    //no_list.clear();
                    //detailList.clear();
                    //if (inspectedReceiveExpanedAdater != null)
                    //    inspectedReceiveExpanedAdater.notifyDataSetChanged();

                    dataTable = null;
                    dataTable = new DataTable();

                    dataTable.Columns.Add("check_sp");
                    dataTable.Columns.Add("rvu01");
                    dataTable.Columns.Add("rvu02");
                    dataTable.Columns.Add("rvb05");
                    dataTable.Columns.Add("pmn041");
                    dataTable.Columns.Add("ima021");
                    dataTable.Columns.Add("rvv32");
                    dataTable.Columns.Add("rvv33");
                    dataTable.Columns.Add("rvv34");
                    dataTable.Columns.Add("rvb33");
                    dataTable.Columns.Add("pmc03");
                    dataTable.Columns.Add("gen02");

                    for (int i=0; i < sub_property.getPropertyCount(); i++) {


                        //Object property = s_deals.getProperty(i);
                        //SoapObject category_list = (SoapObject) property;
                        Log.d(TAG, "object["+i+"] = "+sub_property.getProperty(i));

                        SoapObject min_property = (SoapObject) sub_property.getProperty(i);

                        String header = String.valueOf(i+1)+"#"+min_property.getProperty(3).toString();
                        no_list.add(header);
                        check_stock_in.add(false);
                        detailList.put(header, new ArrayList<DetailItem>());

                        //ArrayList detailArrayList = new ArrayList();
                        DataRow dataRow = dataTable.NewRow();

                        //add into stock check first
                        DetailItem checkItem = new DetailItem();
                        checkItem.setTitle(this.getResources().getString(R.string.item_title_confirm_stock_in));
                        detailList.get(header).add(checkItem);

                        for (int j=0; j < min_property.getPropertyCount(); j++) {
                            DetailItem item = new DetailItem();
                            //SoapObject form_no = (SoapObject) min_property.getProperty(1);
                            Log.e(TAG, "sub_object = "+min_property.getProperty(j)+" name = "+min_property.getPropertyInfo(j).toString().trim().split(":")[0]);




                            if (j==0) {
                                item.setTitle(this.getResources().getString(R.string.item_title_check_sp));
                                item.setName(min_property.getProperty(j).toString());
                                dataRow.setValue("check_sp", min_property.getProperty(j).toString());
                            } else if (j==1) {
                                item.setTitle(this.getResources().getString(R.string.item_title_rvu01));
                                item.setName(min_property.getProperty(j).toString());
                                dataRow.setValue("rvu01", min_property.getProperty(j).toString());
                            } else if (j==2) {
                                item.setTitle(this.getResources().getString(R.string.item_title_rvv02));
                                item.setName(min_property.getProperty(j).toString());
                                dataRow.setValue("rvu02", min_property.getProperty(j).toString());
                            } else if (j==3) {
                                item.setTitle(this.getResources().getString(R.string.item_title_rvb05));
                                item.setName(min_property.getProperty(j).toString());
                                dataRow.setValue("rvb05", min_property.getProperty(j).toString());
                            } else if (j==4) {
                                item.setTitle(this.getResources().getString(R.string.item_title_pmn041));
                                item.setName(min_property.getProperty(j).toString());
                                dataRow.setValue("pmn041", min_property.getProperty(j).toString());
                            } else if (j==5) {
                                item.setTitle(this.getResources().getString(R.string.item_title_ima021));
                                item.setName(min_property.getProperty(j).toString());
                                dataRow.setValue("ima021", min_property.getProperty(j).toString());
                            } else if (j==6) {
                                item.setTitle(this.getResources().getString(R.string.item_title_rvv32));
                                item.setName(min_property.getProperty(j).toString());
                                dataRow.setValue("rvv32", min_property.getProperty(j).toString());
                            } else if (j==7) {
                                item.setTitle(this.getResources().getString(R.string.item_title_rvv33));
                                item.setName(min_property.getProperty(j).toString());
                                dataRow.setValue("rvv33", min_property.getProperty(j).toString());
                            } else if (j==8) {
                                item.setTitle(this.getResources().getString(R.string.item_title_rvv34));
                                item.setName(min_property.getProperty(j).toString());
                                dataRow.setValue("rvv34", min_property.getProperty(j).toString());
                            } else if (j==9) {
                                item.setTitle(this.getResources().getString(R.string.item_title_rvb33));
                                item.setName(min_property.getProperty(j).toString());
                                dataRow.setValue("rvb33", min_property.getProperty(j).toString());
                            } else if (j==10) {
                                item.setTitle(this.getResources().getString(R.string.item_title_pmc03));
                                item.setName(min_property.getProperty(j).toString());
                                dataRow.setValue("pmc03", min_property.getProperty(j).toString());
                            } else if (j==11) {
                                item.setTitle(this.getResources().getString(R.string.item_title_gen02));
                                item.setName(min_property.getProperty(j).toString());
                                dataRow.setValue("gen02", min_property.getProperty(j).toString());
                            }



                            if (item != null)
                                detailList.get(header).add(item);



                        }



                        dataTable.Rows.add(dataRow);


                    }

                    Log.e(TAG, "========================================================");
                    for (int i=0; i<dataTable.Rows.size(); i++) {

                        for (int j=0; j<dataTable.Columns.size(); j++) {
                            System.out.print(dataTable.Rows.get(i).getValue(j));
                            if (j < dataTable.Columns.size() - 1) {
                                System.out.print(", ");
                            }
                        }
                        System.out.print("\n");
                    }
                    Log.e(TAG, "========================================================");


                    Intent getSuccessIntent = new Intent(Constants.ACTION.ACTION_GET_INSPECTED_RECEIVE_ITEM_SUCCESS);
                    sendBroadcast(getSuccessIntent);
                } else {
                    Intent getSuccessIntent = new Intent(Constants.ACTION.ACTION_GET_INSPECTED_RECEIVE_ITEM_EMPTY);
                    sendBroadcast(getSuccessIntent);
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
            Intent getFailedIntent = new Intent(Constants.ACTION.ACTION_GET_INSPECTED_RECEIVE_ITEM_FAILED);
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
