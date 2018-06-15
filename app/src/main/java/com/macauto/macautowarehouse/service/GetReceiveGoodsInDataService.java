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

import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.dataTable;
import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.detailList;

import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.inspectedReceiveExpanedAdater;
import static com.macauto.macautowarehouse.EnteringWarehouseFragmnet.no_list;
import static com.macauto.macautowarehouse.data.FileOperation.append_record;

public class GetReceiveGoodsInDataService extends IntentService {
    public static final String TAG = "GetReceiveService";

    public static final String SERVICE_IP = "172.17.17.244";

    public static final String SERVICE_PORT = "8484";

    private static final String NAMESPACE = "http://tempuri.org/"; // 命名空間

    private static final String METHOD_NAME = "Get_TT_ReceiveGoods_IN_Data"; // 方法名稱

    private static final String SOAP_ACTION1 = "http://tempuri.org/Get_TT_ReceiveGoods_IN_Data"; // SOAP_ACTION

    private static final String URL = "http://172.17.17.244:8484/service.asmx"; // 網址

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

            request.addProperty("SID", "MAT");
            request.addProperty("part_no", part_no);
            request.addProperty("barcode_no", barcode_no);
            request.addProperty("k_id", "123456");
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
            } else {
                SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                Log.e(TAG, String.valueOf(resultsRequestSOAP));

                //append_record(String.valueOf(resultsRequestSOAP)+"\n\n\n\n", "test");

                SoapObject s_deals = (SoapObject) resultsRequestSOAP.getProperty("Get_TT_ReceiveGoods_IN_DataResult");


                Log.d(TAG, "count = "+s_deals.getPropertyCount());

                SoapObject property = (SoapObject) s_deals.getProperty(1);



                //SoapObject sub_property = (SoapObject) property.getProperty(0);

                int count = property.getPropertyCount();

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

                    String header = String.valueOf(i+1)+"#"+min_property.getProperty(1).toString();
                    no_list.add(header);
                    detailList.put(header, new ArrayList<DetailItem>());

                    //ArrayList detailArrayList = new ArrayList();
                    DataRow dataRow = dataTable.NewRow();


                    for (int j=0; j < min_property.getPropertyCount(); j++) {
                        DetailItem item = new DetailItem();
                        //SoapObject form_no = (SoapObject) min_property.getProperty(1);
                        Log.e(TAG, "sub_object = "+min_property.getProperty(j));


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


                        /*if (j == 1) {
                            String header = min_property.getProperty(1).toString()+"#"+String.valueOf(i+1);
                            no_list.add(header);
                        }*/
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
                /*do {
                    sub_property = (SoapObject) sub_property.getProperty(0);
                    count = sub_property.getPropertyCount();
                    Log.d(TAG, "count = "+count);
                } while (count == 1);

                Log.d(TAG, "count = "+sub_property.getPropertyCount());

                for (int i=0; i < sub_property.getPropertyCount(); i++) {
                    //Object property = s_deals.getProperty(i);
                    //SoapObject category_list = (SoapObject) property;
                    Log.d(TAG, "object["+i+"] = "+sub_property.getProperty(i));
                }*/

                /*for (int i=0; i < s_deals.getPropertyCount(); i++) {
                    Object property = s_deals.getProperty(i);
                    if (property instanceof SoapObject)
                    {
                        SoapObject category_list = (SoapObject) property;
                        String check_sp = category_list.getProperty("check_sp").toString();
                        String col_rvu01 = category_list.getProperty("rvu01").toString();
                        String col_rvv02 = category_list.getProperty("rvv02").toString();
                        String col_rvb05 = category_list.getProperty("rvb05").toString();
                        String col_pmn041 = category_list.getProperty("pmn041").toString();
                        String col_ima021 = category_list.getProperty("ima021").toString();
                        String col_rvv32 = category_list.getProperty("rvv32").toString();
                        String col_rvv33 = category_list.getProperty("rvv33").toString();
                        String col_rvv34 = category_list.getProperty("rvv34").toString();
                        String col_rvb33 = category_list.getProperty("rvb33").toString();
                        String col_pmc03 = category_list.getProperty("pmc03").toString();
                        String col_gen02 = category_list.getProperty("gen02").toString();

                        Log.e(TAG, "check_sp = "+check_sp);
                        Log.e(TAG, "col_rvu01 = "+col_rvu01);
                        Log.e(TAG, "col_rvv02 = "+col_rvv02);
                        Log.e(TAG, "col_rvb05 = "+col_rvb05);
                        Log.e(TAG, "col_pmn041 = "+col_pmn041);
                        Log.e(TAG, "col_ima021 = "+col_ima021);
                        Log.e(TAG, "col_rvv32 = "+col_rvv32);
                        Log.e(TAG, "col_rvv33 = "+col_rvv33);
                        Log.e(TAG, "col_rvv34 = "+col_rvv34);
                        Log.e(TAG, "col_rvb33 = "+col_rvb33);
                        Log.e(TAG, "col_pmc03 = "+col_pmc03);
                        Log.e(TAG, "col_gen02 = "+col_gen02);
                    }
                }*/


                //result.setText(String.valueOf(resultsRequestSOAP));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    InputStream stream = new ByteArrayInputStream(String.valueOf(resultsRequestSOAP).getBytes(StandardCharsets.UTF_8));
                    //LoadAndParseXML(stream);
                } else {
                    InputStream stream = new ByteArrayInputStream(String.valueOf(resultsRequestSOAP).getBytes(Charset.forName("UTF-8")));
                    //LoadAndParseXML(stream);
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
            Intent getFailedIntent = new Intent(Constants.ACTION.ACTION_GET_INSPECTED_RECEIVE_ITEM_FAILED);
            sendBroadcast(getFailedIntent);
        }

        //MeetingAlarm.last_sync_setting = sync_option;
        Intent getSuccessIntent = new Intent(Constants.ACTION.ACTION_GET_INSPECTED_RECEIVE_ITEM_SUCCESS);
        sendBroadcast(getSuccessIntent);


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
            InspectedReceiveItem item = null;
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
                        item = new InspectedReceiveItem();
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
                            case "check_sp":
                                if (tag_value.equals("true")) {
                                    item.setCheck_sp(true);
                                } else {
                                    item.setCheck_sp(false);
                                }
                                break;
                            case "rvu01":
                                item.setCol_rvu01(tag_value);
                                break;
                            case "rvv02":
                                item.setCol_rvv02(tag_value);
                                break;
                            case "rvb05":
                                item.setCol_rvb05(tag_value);
                                break;
                            case "pmn041":
                                item.setCol_pmn041(tag_value);
                                break;
                            case "ima021":
                                item.setCol_ima021(tag_value);
                                break;
                            case "rvv32":
                                item.setCol_rvv32(tag_value);
                                break;
                            case "rvv33":
                                item.setCol_rvv33(tag_value);
                                break;
                            case "rvv34":
                                item.setCol_rvv34(tag_value);
                                break;
                            case "rvb33":
                                item.setCol_rvb33(tag_value);
                                break;
                            case "pmc03":
                                item.setCol_pmc03(tag_value);
                                break;
                            case "gen02":
                                item.setCol_gen02(tag_value);
                                break;

                            default:
                                break;
                        }

                        if (name.equals("fxs")) {
                            Log.i(TAG, "=== End of Message record ===");
                            //historyItemArrayList.add(item);
                            Intent intentData = new Intent(Constants.ACTION.ACTION_GET_INSPECTED_RECEIVE_ITEM_SUCCESS);
                            intentData.putExtra("COL_CHECK_SP", item.isCheck_sp());
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
