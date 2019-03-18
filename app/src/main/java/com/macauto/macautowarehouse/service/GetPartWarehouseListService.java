package com.macauto.macautowarehouse.service;

import android.app.IntentService;
import android.content.Intent;

import android.util.Log;


import com.macauto.macautowarehouse.data.Constants;

import com.macauto.macautowarehouse.data.SearchItem;

import com.macauto.macautowarehouse.table.DataTable;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.ConnectException;
import java.net.SocketTimeoutException;


import static com.macauto.macautowarehouse.MainActivity.lookUpDataTable;
import static com.macauto.macautowarehouse.MainActivity.searchList;
import static com.macauto.macautowarehouse.MainActivity.service_ip;
import static com.macauto.macautowarehouse.MainActivity.web_soap_port;
import static com.macauto.macautowarehouse.data.WebServiceParse.parseXmlToDataTable;

public class GetPartWarehouseListService extends IntentService {
    public static final String TAG = "GetPartWarehouseList";

    public static final String SERVICE_IP = "172.17.17.244";

    public static final String SERVICE_PORT = "8484";

    private static final String NAMESPACE = "http://tempuri.org/"; // 命名空間

    private static final String METHOD_NAME = "get_part_warehouse_list"; // 方法名稱

    private static final String SOAP_ACTION1 = "http://tempuri.org/get_part_warehouse_list"; // SOAP_ACTION

    //private static final String URL = "http://172.17.17.244:8484/service.asmx"; // 網址

    public GetPartWarehouseListService() {
        super("GetPartWarehouseListService");
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

        String part_no = intent.getStringExtra("PART_NO");
        //String stock_no = intent.getStringExtra("STOCK_NO");
        //String locate_no = intent.getStringExtra("LOCATE_NO");
        String batch_no = intent.getStringExtra("BATCH_NO");
        String ima02 = intent.getStringExtra("NAME");
        String ima021 = intent.getStringExtra("SPEC");
        String URL = "http://"+service_ip+":"+web_soap_port+"/service.asmx";
        //String query_all = intent.getStringExtra("QUERY_ALL");

        Log.e(TAG, "part_no = "+part_no);
        Log.e(TAG, "batch_no = "+batch_no);
        Log.e(TAG, "ima02 = "+ima02);
        Log.e(TAG, "ima021 = "+ima021);

        //device_id = intent.getStringExtra("DEVICE_ID");
        //String service_ip = intent.getStringExtra(SERVICE_IP);
        //String service_port = intent.getStringExtra(SERVICE_PORT);

        //String combine_url = "http://"+SERVICE_IP+":"+SERVICE_PORT+"/service.asmx";

        if (intent.getAction() != null) {
            if (intent.getAction().equals(Constants.ACTION.ACTION_SEARCH_PART_WAREHOUSE_LIST_ACTION)) {
                Log.i(TAG, "ACTION_SEARCH_PART_WAREHOUSE_LIST_ACTION");
            }
        }



        try {
            // 建立一個 WebService 請求

            SoapObject request = new SoapObject(NAMESPACE,
                    METHOD_NAME);

            // 輸出值，帳號(account)、密碼(password)

            request.addProperty("SID", "MAT");
            if (part_no != null)
                request.addProperty("part_no", part_no);
            else
                request.addProperty("part_no", "");
            request.addProperty("stock_no", "");
            request.addProperty("locate_no", "");
            if (batch_no != null)
                request.addProperty("batch_no", batch_no);
            else
                request.addProperty("batch_no", "");
            if (ima02 != null)
                request.addProperty("ima02", ima02);
            else
                request.addProperty("ima02", "");
            if (ima021 != null)
                request.addProperty("ima021", ima021);
            else
                request.addProperty("ima021", "");
            request.addProperty("query_all", "Y");
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

                Intent getSuccessIntent = new Intent(Constants.ACTION.ACTION_SEARCH_PART_WAREHOUSE_LIST_EMPTY);
                sendBroadcast(getSuccessIntent);
            } else {
                SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
                Log.e(TAG, String.valueOf(resultsRequestSOAP));

                SoapObject s_deals = (SoapObject) resultsRequestSOAP.getProperty("get_part_warehouse_listResult");

                if (lookUpDataTable != null)
                    lookUpDataTable.clear();
                else
                    lookUpDataTable = new DataTable();

                lookUpDataTable = parseXmlToDataTable(s_deals);

                if (lookUpDataTable != null) {
                    if (lookUpDataTable.Rows.size() == 0) {
                        Intent getSuccessIntent = new Intent(Constants.ACTION.ACTION_SEARCH_PART_WAREHOUSE_LIST_EMPTY);
                        sendBroadcast(getSuccessIntent);
                    } else {
                        for (int i=0; i < lookUpDataTable.Rows.size(); i++) {

                            SearchItem searchItem = new SearchItem();

                            for (int j=0; j < lookUpDataTable.Columns.size(); j++) {


                                if (j==0) {
                                    searchItem.setItem_IMG01(lookUpDataTable.getValue(i, j).toString());
                                } else if (j==1) {
                                    searchItem.setItem_IMA02(lookUpDataTable.getValue(i, j).toString());
                                } else if (j==2) {
                                    searchItem.setItem_IMA021(lookUpDataTable.getValue(i, j).toString());
                                } else if (j==3) {
                                    searchItem.setItem_IMG02(lookUpDataTable.getValue(i, j).toString());
                                } else if (j==4) {
                                    searchItem.setItem_IMD02(lookUpDataTable.getValue(i, j).toString());
                                } else if (j==5) {
                                    searchItem.setItem_IMG03(lookUpDataTable.getValue(i, j).toString());
                                } else if (j==6) {
                                    searchItem.setItem_IMG04(lookUpDataTable.getValue(i, j).toString());
                                } else if (j==7) {
                                    searchItem.setItem_IMG10(lookUpDataTable.getValue(i, j).toString());
                                } else if (j==8) {
                                    searchItem.setItem_IMA25(lookUpDataTable.getValue(i, j).toString());
                                } else if (j==9) {
                                    searchItem.setItem_IMG23(lookUpDataTable.getValue(i, j).toString());
                                } else if (j==10) {
                                    searchItem.setItem_IMA08(lookUpDataTable.getValue(i, j).toString());
                                } else if (j==11) {
                                    searchItem.setItem_STOCK_MAN(lookUpDataTable.getValue(i, j).toString());
                                } else if (j==12) {
                                    if (lookUpDataTable.getValue(i, j) != null)
                                        searchItem.setItem_IMA03(lookUpDataTable.getValue(i, j).toString());
                                } else if (j==13) {
                                    if (lookUpDataTable.getValue(i, j) != null)
                                        searchItem.setItem_PMC03(lookUpDataTable.getValue(i, j).toString());
                                }



                            }

                            searchList.add(searchItem);

                        }

                        Intent getSuccessIntent = new Intent(Constants.ACTION.ACTION_SEARCH_PART_WAREHOUSE_LIST_SUCCESS);
                        getSuccessIntent.putExtra("RECORDS", String.valueOf(lookUpDataTable.Rows.size()));
                        sendBroadcast(getSuccessIntent);
                    }
                } else {
                    Intent getSuccessIntent = new Intent(Constants.ACTION.ACTION_SEARCH_PART_WAREHOUSE_LIST_EMPTY);
                    sendBroadcast(getSuccessIntent);
                }



                //append_record(String.valueOf(resultsRequestSOAP)+"\n\n\n\n", "test");

                /*SoapObject s_deals = (SoapObject) resultsRequestSOAP.getProperty("Get_TT_ReceiveGoods_IN_DataResult");


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



                //result.setText(String.valueOf(resultsRequestSOAP));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    InputStream stream = new ByteArrayInputStream(String.valueOf(resultsRequestSOAP).getBytes(StandardCharsets.UTF_8));
                    //LoadAndParseXML(stream);
                } else {
                    InputStream stream = new ByteArrayInputStream(String.valueOf(resultsRequestSOAP).getBytes(Charset.forName("UTF-8")));
                    //LoadAndParseXML(stream);
                }*/

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
            Intent getFailedIntent = new Intent(Constants.ACTION.ACTION_SEARCH_PART_WAREHOUSE_LIST_FAILED);
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
