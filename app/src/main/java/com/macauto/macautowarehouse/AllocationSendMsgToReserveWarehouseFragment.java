package com.macauto.macautowarehouse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.ImageView;

import android.widget.ListView;

import android.widget.Toast;


import com.daimajia.swipe.SwipeLayout;
import com.macauto.macautowarehouse.data.AllocationMsgStatusItem;
import com.macauto.macautowarehouse.data.AllocationMsgStatusItemAdapter;
import com.macauto.macautowarehouse.data.AllocationSendMsgItem;
import com.macauto.macautowarehouse.data.AllocationSendMsgItemAdapter;
import com.macauto.macautowarehouse.data.Constants;
import com.macauto.macautowarehouse.service.CheckMadeNoExistService;
import com.macauto.macautowarehouse.service.CheckStockNoExistService;
import com.macauto.macautowarehouse.service.GetLocateNoService;
import com.macauto.macautowarehouse.service.GetMadeInfoService;

import com.macauto.macautowarehouse.service.GetSfaDataMessMoveService;
import com.macauto.macautowarehouse.service.GetSfaDataMessService;
import com.macauto.macautowarehouse.service.GetSfaDataMessWorkerService;
import com.macauto.macautowarehouse.service.GetVarValueService;
import com.macauto.macautowarehouse.table.DataRow;
import com.macauto.macautowarehouse.table.DataTable;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.macauto.macautowarehouse.MainActivity.emp_no;

public class AllocationSendMsgToReserveWarehouseFragment extends Fragment {
    private static final String TAG = "AllocationSendMsg";

    private Context fragmentContext;

    private static BroadcastReceiver mReceiver = null;
    private static boolean isRegister = false;

    /*private EditText editTextWorkOrder;
    private EditText editTextStagingArea;
    private EditText edtiRate;
    private Spinner locateSpinner;
    private Spinner dateSpinner;
    private Spinner hourSpinner;
    private Spinner minSpinner;*/
    private Button btnAllocTransfer;
    private Button btnAllocTransferEmp;
    private Button btnReset;
    //private Button btnDelete;
    private Button btnSend;
    /*private LinearLayout layoutProduceView;
    private LinearLayout layoutResultView;
    private TextView s_part_no;
    private TextView s_part_desc;
    private TextView s_pdt_qty;
    private TextView s_pdted_qty;*/


    public static ArrayAdapter<String> locateAdapter;
    public static ArrayAdapter<String> dateAdapter;
    public static ArrayAdapter<String> hourAdapter;
    public static ArrayAdapter<String> minAdapter;

    public static ArrayList<String> locateList = new ArrayList<>();
    public static ArrayList<String> dateList = new ArrayList<>();
    public static ArrayList<String> hourList = new ArrayList<>();
    public static ArrayList<String> minList = new ArrayList<>();
    public static DataTable locateNoTable = new DataTable();
    public static DataTable madeInfoTable = new DataTable();
    public static DataTable sfaMessTable = new DataTable();
    public static DataTable hhh = new DataTable();

    private static int current_btn_state = 0;

    public AllocationSendMsgItemAdapter allocationSendMsgItemAdapter;
    private static ArrayList<AllocationSendMsgItem> myList = new ArrayList<>();

    public AllocationMsgStatusItemAdapter allocationMsgStatusItemAdapter;
    public static ArrayList<AllocationMsgStatusItem> statusList = new ArrayList<>();
    public ListView statusListView;
    private Locale current;
    //private SwipeLayout swipeLayout;
    private static boolean allocate_with_emp = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");
        current_btn_state = 0;


        final  View view = inflater.inflate(R.layout.allocation_send_msg_to_reserve_warehouse_fragment, container, false);

        fragmentContext = getContext();

        current = fragmentContext.getResources().getConfiguration().locale;



        /*editTextWorkOrder = view.findViewById(R.id.allocationSendMsgWorkOrder);
        editTextStagingArea = view.findViewById(R.id.allocationSendMsgStagingArea);
        edtiRate = view.findViewById(R.id.pdt_rate);

        locateSpinner = view.findViewById(R.id.allocationSendMsgStockLocate);
        dateSpinner = view.findViewById(R.id.dateSpinner);
        hourSpinner = view.findViewById(R.id.hourSpinner);
        minSpinner = view.findViewById(R.id.minSpinner);
        s_part_no = view.findViewById(R.id.s_part_no);
        s_part_desc = view.findViewById(R.id.s_part_desc);
        s_pdt_qty = view.findViewById(R.id.s_pdt_qty);
        s_pdted_qty = view.findViewById(R.id.s_pdted_qty);*/
        ListView listView = view.findViewById(R.id.listViewAllocationMsg);
        statusListView = view.findViewById(R.id.statusListView);


        /*statusListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.e(TAG, "Click "+position);

                for (int i=0; i<statusList.size(); i++) {
                    if (position == i) {
                        statusList.get(i).setSelected(true);
                    } else {
                        statusList.get(i).setSelected(false);
                    }
                }

                allocationMsgStatusItemAdapter.notifyDataSetChanged();
            }
        });





        statusListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                Intent detailIntent = new Intent(fragmentContext, AllocationSendMsgStatusDetailActivity.class);
                detailIntent.putExtra("ITEM_SFA03", statusList.get(position).getItem_SFA03());
                detailIntent.putExtra("ITEM_IMA021", statusList.get(position).getItem_IMA021());
                detailIntent.putExtra("ITEM_IMG10", statusList.get(position).getItem_IMG10());
                detailIntent.putExtra("ITEM_MOVED_QTY", statusList.get(position).getItem_MOVED_QTY());
                detailIntent.putExtra("ITEM_MOVED_QTY", statusList.get(position).getItem_MOVED_QTY());
                detailIntent.putExtra("ITEM_MESS_QTY", statusList.get(position).getItem_MESS_QTY());
                detailIntent.putExtra("ITEM_SFA05", statusList.get(position).getItem_SFA05());
                detailIntent.putExtra("ITEM_SFA12", statusList.get(position).getItem_SFA12());
                detailIntent.putExtra("ITEM_SFA11_NAME", statusList.get(position).getItem_SFA11_NAME());
                detailIntent.putExtra("ITEM_TC_OBF013", statusList.get(position).getItem_TC_OBF013());
                fragmentContext.startActivity(detailIntent);

                return true;
            }
        });*/


        btnAllocTransfer = view.findViewById(R.id.btnAllocateTransfer);
        btnAllocTransferEmp = view.findViewById(R.id.btnAllocateTransferEmp);
        btnReset = view.findViewById(R.id.btnRest);
        //btnDelete = view.findViewById(R.id.btnDelete);
        btnSend = view.findViewById(R.id.btnSend);
        btnAllocTransferEmp.setVisibility(View.VISIBLE);
        btnAllocTransfer.setVisibility(View.GONE);
        btnReset.setVisibility(View.GONE);
        btnSend.setVisibility(View.GONE);

        //layoutProduceView = view.findViewById(R.id.layoutAllocationSendMsgProduceView);
        //layoutResultView = view.findViewById(R.id.layoutAllocationSendMsgResultView);




        ImageView btnLeftArrow = view.findViewById(R.id.leftArrow);
        ImageView btnRightArrow = view.findViewById(R.id.rightArrow);

        btnLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (current_btn_state) {
                    case 0:
                        current_btn_state = 3;
                        btnAllocTransferEmp.setVisibility(View.GONE);
                        btnSend.setVisibility(View.VISIBLE);
                        if (statusList.size() > 0) {
                            btnSend.setEnabled(true);
                        } else {
                            btnSend.setEnabled(false);
                        }
                        break;
                    case 1:
                        current_btn_state = 0;
                        btnAllocTransfer.setVisibility(View.GONE);
                        btnAllocTransferEmp.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        current_btn_state = 1;
                        btnReset.setVisibility(View.GONE);
                        btnAllocTransfer.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        current_btn_state = 2;
                        btnSend.setVisibility(View.GONE);
                        btnReset.setVisibility(View.VISIBLE);

                }
            }
        });

        btnRightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (current_btn_state) {
                    case 0:
                        current_btn_state = 1;
                        btnAllocTransferEmp.setVisibility(View.GONE);
                        btnAllocTransfer.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        current_btn_state = 2;
                        btnAllocTransfer.setVisibility(View.GONE);
                        btnReset.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        current_btn_state = 3;
                        btnReset.setVisibility(View.GONE);
                        btnSend.setVisibility(View.VISIBLE);
                        if (statusList.size() > 0) {
                            btnSend.setEnabled(true);
                        } else {
                            btnSend.setEnabled(false);
                        }
                        break;
                    case 3:
                        current_btn_state = 0;
                        btnSend.setVisibility(View.GONE);
                        btnAllocTransferEmp.setVisibility(View.VISIBLE);

                }
            }
        });

        /*Button btnRest = view.findViewById(R.id.btnRest);

        btnRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutProduceView.setVisibility(View.VISIBLE);
                layoutResultView.setVisibility(View.GONE);
            }
        });*/

        btnAllocTransferEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set emp true
                allocate_with_emp = true;

                statusList.clear();
                if (allocationMsgStatusItemAdapter != null)
                    allocationMsgStatusItemAdapter.notifyDataSetChanged();

                madeInfoTable.clear();
                sfaMessTable.clear();


                //check made no first

                //AllocationSendMsgItem item = allocationSendMsgItemAdapter.getItem(0);
                AllocationSendMsgItem item = myList.get(0);

                Log.e(TAG, "item = "+item.getContent()+" editText = "+item.getEditText().getText());

                if (item != null) {

                    Intent getMadeNoIntent = new Intent(fragmentContext, CheckMadeNoExistService.class);
                    getMadeNoIntent.setAction(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_CHECK_MADE_NO_EXIST_ACTION);
                    getMadeNoIntent.putExtra("MADE_NO", item.getContent());
                    fragmentContext.startService(getMadeNoIntent);
                }

                //set date
                dateList.clear();
                if (dateAdapter != null)
                    dateAdapter.notifyDataSetChanged();
                //set date spinner
                Calendar calendar = Calendar.getInstance();
                //add 30 minutes
                calendar.add(Calendar.MINUTE, 30);
                Date today = calendar.getTime();
                calendar.setTime(today);
                int hours = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);

                Log.e(TAG, "hours = "+hours+", minutes = "+minutes);

                String fToday = new SimpleDateFormat("yyyy-MM-dd", current).format(today);
                dateList.add(fToday);

                for (int i=0; i<11; i++) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                    Date date = calendar.getTime();

                    String fDate = new SimpleDateFormat("yyyy-MM-dd", current).format(date);
                    dateList.add(fDate);
                }

                if (dateAdapter != null)
                    dateAdapter.notifyDataSetChanged();
                else
                    dateAdapter = new ArrayAdapter<>(fragmentContext, R.layout.myspinner, dateList);

                AllocationSendMsgItem item_date = myList.get(8);
                AllocationSendMsgItem item_hour = myList.get(9);
                AllocationSendMsgItem item_min = myList.get(10);
                if (item_date != null) {
                    if (item_date.getSpinner() != null)
                        item_date.getSpinner().setAdapter(dateAdapter);
                }
                if (item_hour != null) {
                    if (item_hour.getSpinner() != null) {
                        item_hour.setContent(String.valueOf(hours));
                        item_hour.getSpinner().setSelection(hours);
                    }

                }
                if (item_min != null) {
                    if (item_min.getSpinner() != null) {
                        item_min.setContent(String.valueOf(minutes));
                        item_min.getSpinner().setSelection(minutes);
                    }
                }
            }
        });

        btnAllocTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clear table

                //editTextWorkOrder.setText(editTextWorkOrder.getText().toString().trim().toUpperCase());
                //editTextStagingArea.setText(editTextStagingArea.getText().toString().trim().toUpperCase());

                statusList.clear();
                if (allocationMsgStatusItemAdapter != null)
                    allocationMsgStatusItemAdapter.notifyDataSetChanged();

                madeInfoTable.clear();
                sfaMessTable.clear();


                //check made no first

                //AllocationSendMsgItem item = allocationSendMsgItemAdapter.getItem(0);
                AllocationSendMsgItem item = myList.get(0);

                Log.e(TAG, "item = "+item.getContent()+" editText = "+item.getEditText().getText());

                if (item != null) {

                    Intent getMadeNoIntent = new Intent(fragmentContext, CheckMadeNoExistService.class);
                    getMadeNoIntent.setAction(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_CHECK_MADE_NO_EXIST_ACTION);
                    getMadeNoIntent.putExtra("MADE_NO", item.getContent());
                    fragmentContext.startService(getMadeNoIntent);
                }

                //set date
                dateList.clear();
                if (dateAdapter != null)
                    dateAdapter.notifyDataSetChanged();
                //set date spinner
                Calendar calendar = Calendar.getInstance();
                //add 30 minutes
                calendar.add(Calendar.MINUTE, 30);
                Date today = calendar.getTime();
                calendar.setTime(today);
                int hours = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);

                Log.e(TAG, "hours = "+hours+", minutes = "+minutes);

                String fToday = new SimpleDateFormat("yyyy-MM-dd", current).format(today);
                dateList.add(fToday);

                for (int i=0; i<11; i++) {
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                    Date date = calendar.getTime();

                    String fDate = new SimpleDateFormat("yyyy-MM-dd", current).format(date);
                    dateList.add(fDate);
                }

                if (dateAdapter != null)
                    dateAdapter.notifyDataSetChanged();
                else
                    dateAdapter = new ArrayAdapter<>(fragmentContext, R.layout.myspinner, dateList);

                AllocationSendMsgItem item_date = myList.get(8);
                AllocationSendMsgItem item_hour = myList.get(9);
                AllocationSendMsgItem item_min = myList.get(10);
                if (item_date != null) {
                    if (item_date.getSpinner() != null)
                        item_date.getSpinner().setAdapter(dateAdapter);
                }
                if (item_hour != null) {
                    if (item_hour.getSpinner() != null) {
                        item_hour.setContent(String.valueOf(hours));
                        item_hour.getSpinner().setSelection(hours);
                    }

                }
                if (item_min != null) {
                    if (item_min.getSpinner() != null) {
                        item_min.setContent(String.valueOf(minutes));
                        item_min.getSpinner().setSelection(minutes);
                    }
                }


            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (locateList.size() == 0) {
                    toast("allocation_send_message_to_material_get_Locate_no_failed");
                } else {

                    if (myList.get(3).getEditText().getText().toString().matches("[0-9]+") && myList.get(3).getEditText().getText().toString().length() > 0) {
                        int szs = Integer.valueOf(myList.get(3).getEditText().getText().toString());

                        //int pdt_qty_int = Integer.valueOf(myList.get(6).getContent());
                        //int ptted_qty_int = Integer.valueOf(myList.get(7).getContent());



                        if (szs <= 0) {
                            toast(getResources().getString(R.string.allocation_send_message_to_material_reg_qty_zero));
                        } else if (myList.get(1).getEditText().getText().length() != 4) {
                            toast(getResources().getString(R.string.allocation_send_message_to_material_sotck_no_mismatch));
                        } else {

                            statusList.clear();
                            if (allocationMsgStatusItemAdapter != null)
                                allocationMsgStatusItemAdapter.notifyDataSetChanged();

                            madeInfoTable.clear();
                            sfaMessTable.clear();


                            //check made no first

                            //AllocationSendMsgItem item = allocationSendMsgItemAdapter.getItem(0);
                            AllocationSendMsgItem item = myList.get(0);

                            Log.e(TAG, "item = "+item.getContent()+" editText = "+item.getEditText().getText());

                            if (item != null) {

                                Intent getMadeNoIntent = new Intent(fragmentContext, CheckMadeNoExistService.class);
                                getMadeNoIntent.setAction(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_CHECK_MADE_NO_EXIST_ACTION);
                                getMadeNoIntent.putExtra("MADE_NO", item.getContent());
                                fragmentContext.startService(getMadeNoIntent);
                            }

                            //set date
                            dateList.clear();
                            if (dateAdapter != null)
                                dateAdapter.notifyDataSetChanged();
                            //set date spinner
                            Calendar calendar = Calendar.getInstance();
                            //add 30 minutes
                            calendar.add(Calendar.MINUTE, 30);
                            Date today = calendar.getTime();
                            calendar.setTime(today);
                            int hours = calendar.get(Calendar.HOUR_OF_DAY);
                            int minutes = calendar.get(Calendar.MINUTE);

                            Log.e(TAG, "hours = "+hours+", minutes = "+minutes);

                            String fToday = new SimpleDateFormat("yyyy-MM-dd", current).format(today);
                            dateList.add(fToday);

                            for (int i=0; i<11; i++) {
                                calendar.add(Calendar.DAY_OF_YEAR, 1);
                                Date date = calendar.getTime();

                                String fDate = new SimpleDateFormat("yyyy-MM-dd", current).format(date);
                                dateList.add(fDate);
                            }

                            if (dateAdapter != null)
                                dateAdapter.notifyDataSetChanged();
                            else
                                dateAdapter = new ArrayAdapter<>(fragmentContext, R.layout.myspinner, dateList);

                            AllocationSendMsgItem item_date = myList.get(8);
                            AllocationSendMsgItem item_hour = myList.get(9);
                            AllocationSendMsgItem item_min = myList.get(10);
                            if (item_date != null) {
                                if (item_date.getSpinner() != null)
                                    item_date.getSpinner().setAdapter(dateAdapter);
                            }
                            if (item_hour != null) {
                                if (item_hour.getSpinner() != null) {
                                    item_hour.setContent(String.valueOf(hours));
                                    item_hour.getSpinner().setSelection(hours);
                                }

                            }
                            if (item_min != null) {
                                if (item_min.getSpinner() != null) {
                                    item_min.setContent(String.valueOf(minutes));
                                    item_min.getSpinner().setSelection(minutes);
                                }
                            }
                        }
                    } else {
                        toast(getResources().getString(R.string.allocation_send_message_to_material_reg_qty_mismatch));
                    }




                }

            }
        });

        /*btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selected = 0;

                for (int i=0; i<statusList.size(); i++) {
                    if (statusList.get(i).isSelected()) {
                        selected = i;
                    }
                }

                Log.e(TAG, "seletced = "+selected);

                statusList.remove(selected);

                if (statusList.size() > 0) {
                    statusList.get(0).setSelected(true);
                }

                allocationMsgStatusItemAdapter.notifyDataSetChanged();


                hhh.Rows.remove(selected);



                if (statusList.size() > 0) {
                    btnDelete.setEnabled(true);
                } else {
                    btnDelete.setEnabled(false);
                }
            }
        });*/

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (statusList.size() > 0 && hhh.Rows.size() > 0) {
                    hhh.TableName = "ZOO";
                    AllocationSendMsgItem item = myList.get(0);
                    AllocationSendMsgItem item_date = myList.get(8);
                    AllocationSendMsgItem item_hour = myList.get(9);
                    AllocationSendMsgItem item_min = myList.get(10);
                    String request_date = item_date.getContent();
                    String request_hour = item_hour.getContent();
                    String request_min = item_min.getContent();
                    String request_date_string = request_date.replace("-", "/")+" "+request_hour+":"+request_min+":00";

                    Intent sendIntent = new Intent(fragmentContext, GetSfaDataMessMoveService.class);
                    sendIntent.setAction(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_MOVE_ACTION);
                    sendIntent.putExtra("USER_NO", emp_no);
                    sendIntent.putExtra("MADE_NO", item.getContent());
                    sendIntent.putExtra("REQUEST_TIME", request_date_string);
                    fragmentContext.startService(sendIntent);

                }
            }
        });

        locateNoTable.clear();
        locateList.clear();
        dateList.clear();
        hourList.clear();
        minList.clear();

        myList.clear();

        AllocationSendMsgItem item1 = new AllocationSendMsgItem();
        item1.setIndex(0);
        item1.setHeader(getResources().getString(R.string.allocation_send_message_to_material_work_order));
        item1.setContent("13112-1411210003");
        myList.add(item1);

        AllocationSendMsgItem item2 = new AllocationSendMsgItem();
        item1.setIndex(1);
        item2.setHeader(getResources().getString(R.string.allocation_send_message_to_material_staging_area));
        item2.setContent("9115");
        myList.add(item2);

        AllocationSendMsgItem item3 = new AllocationSendMsgItem();
        item1.setIndex(2);
        item3.setHeader(getResources().getString(R.string.allocation_send_message_to_material_stock_locate));
        item3.setContent("A001A01");
        myList.add(item3);

        AllocationSendMsgItem item4 = new AllocationSendMsgItem();
        item1.setIndex(3);
        item4.setHeader(getResources().getString(R.string.allocation_send_message_to_material_rate));
        item4.setContent("0");
        myList.add(item4);

        AllocationSendMsgItem item5 = new AllocationSendMsgItem();
        item1.setIndex(4);
        item5.setHeader(getResources().getString(R.string.allocation_send_message_to_material_production_no));
        item5.setContent("");
        myList.add(item5);

        AllocationSendMsgItem item6 = new AllocationSendMsgItem();
        item1.setIndex(5);
        item6.setHeader(getResources().getString(R.string.allocation_send_message_to_material_model_type));
        item6.setContent("");
        myList.add(item6);

        AllocationSendMsgItem item7 = new AllocationSendMsgItem();
        item1.setIndex(6);
        item7.setHeader(getResources().getString(R.string.allocation_send_message_to_material_predict_production_quantity));
        item7.setContent("");
        myList.add(item7);

        AllocationSendMsgItem item8 = new AllocationSendMsgItem();
        item1.setIndex(7);
        item8.setHeader(getResources().getString(R.string.allocation_send_message_to_material_real_production_quantity));
        item8.setContent("");
        myList.add(item8);



        //set date spinner
        Calendar calendar = Calendar.getInstance();
        //add 30 minutes
        calendar.add(Calendar.MINUTE, 30);
        Date today = calendar.getTime();
        calendar.setTime(today);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);


        String fToday = new SimpleDateFormat("yyyy-MM-dd", current).format(today);
        dateList.add(fToday);

        for (int i=0; i<11; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            Date date = calendar.getTime();

            String fDate = new SimpleDateFormat("yyyy-MM-dd", current).format(date);
            dateList.add(fDate);
        }
        dateAdapter = new ArrayAdapter<>(fragmentContext, R.layout.myspinner, dateList);

        AllocationSendMsgItem item9 = new AllocationSendMsgItem();
        item1.setIndex(8);
        item9.setHeader(getResources().getString(R.string.allocation_send_message_to_material_date_year_month_day));
        item9.setContent(fToday);
        myList.add(item9);

        //item9.getSpinner().setAdapter(dateAdapter);

        for (int i=0; i<24; i++) {
            hourList.add(String.valueOf(i));
        }
        hourAdapter = new ArrayAdapter<>(fragmentContext, R.layout.myspinner, hourList);

        AllocationSendMsgItem item10 = new AllocationSendMsgItem();
        item1.setIndex(9);
        item10.setHeader(getResources().getString(R.string.allocation_send_message_to_material_date_hour));
        item10.setContent(String.valueOf(hours));
        myList.add(item10);
        //item10.getSpinner().setAdapter(hourAdapter);

        //item10.getSpinner().setSelection(hours);

        for (int i=0; i<60; i++) {
            minList.add(String.valueOf(i));
        }
        minAdapter = new ArrayAdapter<>(fragmentContext, R.layout.myspinner, minList);

        AllocationSendMsgItem item11 = new AllocationSendMsgItem();
        item1.setIndex(10);
        item11.setHeader(getResources().getString(R.string.allocation_send_message_to_material_date_minute));
        item11.setContent(String.valueOf(minutes));
        myList.add(item11);
        //item11.getSpinner().setAdapter(minAdapter);

        //item11.getSpinner().setSelection(minutes);

        allocationSendMsgItemAdapter = new AllocationSendMsgItemAdapter(fragmentContext, R.layout.allocation_msg_send_allocationmsg_list_item, myList);
        listView.setAdapter(allocationSendMsgItemAdapter);

        //get locate list
        Intent getLocateNoIntent = new Intent(fragmentContext, GetLocateNoService.class);
        getLocateNoIntent.setAction(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_LOCATE_NO_ACTION);
        getLocateNoIntent.putExtra("STOCK_NO", item2.getContent());
        fragmentContext.startService(getLocateNoIntent);




        final IntentFilter filter;

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //Log.e(TAG, "intent.getAction() =>>>> "+intent.getAction().toString());

                if (intent.getAction() != null) {

                    if (intent.getAction().equalsIgnoreCase(Constants.ACTION.SOAP_CONNECTION_FAIL)) {
                        Log.d(TAG, "receive SOAP_CONNECTION_FAIL");

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SOCKET_TIMEOUT)) {
                        Log.d(TAG, "receive ACTION_SOCKET_TIMEOUT");

                        toast(getResources().getString(R.string.socket_timeout));

                        //try to get locate again
                        if (locateList.size() == 0) {
                            Intent getLocateNoIntent = new Intent(fragmentContext, GetLocateNoService.class);
                            getLocateNoIntent.setAction(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_LOCATE_NO_ACTION);
                            getLocateNoIntent.putExtra("STOCK_NO", myList.get(1).getContent());
                            fragmentContext.startService(getLocateNoIntent);
                        }

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_LOCATE_NO_SUCCESS)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_SEND_MSG_GET_LOCATE_NO_SUCCESS");
                        AllocationSendMsgItem item = myList.get(2);

                        //AllocationSendMsgItem item_date = allocationSendMsgItemAdapter.getItem(8);
                        //AllocationSendMsgItem item_hour = allocationSendMsgItemAdapter.getItem(9);
                        //AllocationSendMsgItem item_min = allocationSendMsgItemAdapter.getItem(10);

                        if (item != null) {
                            locateAdapter = new ArrayAdapter<>(fragmentContext, R.layout.myspinner, locateList);
                            item.getSpinner().setAdapter(locateAdapter);

                            allocationSendMsgItemAdapter.notifyDataSetChanged();
                        }





                        btnAllocTransfer.setEnabled(true);
                        btnAllocTransferEmp.setEnabled(true);

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_LOCATE_NO_EMPTY)){
                        Log.d(TAG, "receive ACTION_ALLOCATION_SEND_MSG_GET_LOCATE_NO_EMPTY");
                        toast(getResources().getString(R.string.allocation_send_message_to_material_get_Locate_no_failed));



                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_LOCATE_NO_FAILED)){
                        Log.d(TAG, "receive ACTION_ALLOCATION_SEND_MSG_GET_LOCATE_NO_FAILED");
                        toast(getResources().getString(R.string.allocation_send_message_to_material_get_Locate_no_failed));
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_MADE_INFO_SUCCESS)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_SEND_MSG_GET_MADE_INFO_SUCCESS");

                        String part_no = intent.getStringExtra("S_PART_NO");
                        String part_desc = intent.getStringExtra("S_PART_DESC");
                        String pdt_qty = intent.getStringExtra("S_PDT_QTY");
                        String pdted_qty = intent.getStringExtra("S_PDTED_QTY");

                        int pdt_qty_int = Integer.valueOf(pdt_qty);
                        int pdted_qty_int = Integer.valueOf(pdted_qty);
                        int rate_int = pdt_qty_int - pdted_qty_int;
                        if (rate_int <= 0)
                            rate_int = 0;

                        //AllocationSendMsgItem item_rate = myList.get(3);
                        //AllocationSendMsgItem item_part_no = myList.get(4);
                        //AllocationSendMsgItem item_part_desc = myList.get(5);
                        //AllocationSendMsgItem item_pdt_qty = myList.get(6);
                        //AllocationSendMsgItem item_pdted_qty = myList.get(7);

                        myList.get(3).setContent(String.valueOf(rate_int));
                        myList.get(4).setContent(part_no);
                        myList.get(5).setContent(part_desc);
                        myList.get(6).setContent(pdt_qty);
                        myList.get(7).setContent(pdted_qty);

                        if (allocationSendMsgItemAdapter != null)
                            allocationSendMsgItemAdapter.notifyDataSetChanged();
                        /*if (item_rate != null)
                            item_rate.getEditText().setText(String.valueOf(rate_int));
                        if (item_part_no != null)
                            item_part_no.getTextView().setText(part_no);
                        if (item_part_desc != null)
                            item_part_desc.getTextView().setText(part_desc);
                        if (item_pdt_qty != null)
                            item_pdt_qty.getTextView().setText(pdt_qty);
                        if (item_pdted_qty != null)
                            item_pdted_qty.getTextView().setText(pdted_qty);*/

                        /*edtiRate.setText(String.valueOf(rate_int));

                        s_part_no.setText(part_no);
                        s_part_desc.setText(part_desc);
                        s_pdt_qty.setText(pdt_qty);
                        s_pdted_qty.setText(pdted_qty);

                        layoutProduceView.setVisibility(View.GONE);
                        layoutResultView.setVisibility(View.VISIBLE);*/



                        if (allocate_with_emp) { //get mess by emp
                            Intent getSfaMessIntent = new Intent(fragmentContext, GetSfaDataMessWorkerService.class);
                            getSfaMessIntent.setAction(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_ACTION);
                            getSfaMessIntent.putExtra("MADE_NO", myList.get(0).getContent());
                            getSfaMessIntent.putExtra("REQ_QTY", myList.get(3).getContent());
                            getSfaMessIntent.putExtra("STOCK_NO", myList.get(1).getContent());
                            getSfaMessIntent.putExtra("LOCATE_NO", myList.get(2).getContent());
                            fragmentContext.startService(getSfaMessIntent);
                            allocate_with_emp = false;
                        } else { //get all
                            Intent getSfaMessIntent = new Intent(fragmentContext, GetSfaDataMessService.class);
                            getSfaMessIntent.setAction(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_ACTION);
                            getSfaMessIntent.putExtra("MADE_NO", myList.get(0).getContent());
                            getSfaMessIntent.putExtra("REQ_QTY", myList.get(3).getContent());
                            getSfaMessIntent.putExtra("STOCK_NO", myList.get(1).getContent());
                            getSfaMessIntent.putExtra("LOCATE_NO", myList.get(2).getContent());
                            fragmentContext.startService(getSfaMessIntent);
                        }





                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_MADE_INFO_EMPTY)){
                        Log.d(TAG, "receive ACTION_ALLOCATION_SEND_MSG_GET_MADE_INFO_EMPTY");
                        //toast(getResources().getString(R.string.allocation_send_message_to_material_get_Locate_no_failed));
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_MADE_INFO_FAILED)){
                        Log.d(TAG, "receive ACTION_ALLOCATION_SEND_MSG_GET_MADE_INFO_FAILED");
                        //toast(getResources().getString(R.string.allocation_send_message_to_material_get_Locate_no_failed));
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_LOCATE_NO_FAILED)){
                        Log.d(TAG, "receive ACTION_ALLOCATION_SEND_MSG_GET_LOCATE_NO_FAILED");
                        toast(getResources().getString(R.string.allocation_send_message_to_material_get_Locate_no_failed));
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_CHECK_MADE_NO_EXIST_NOT_EXIST)){
                        Log.d(TAG, "receive ACTION_ALLOCATION_SEND_MSG_CHECK_MADE_NO_EXIST_NOT_EXIST");

                        toast(getResources().getString(R.string.allocation_send_message_to_material_made_no_not_matched));

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_CHECK_MADE_NO_EXIST_SUCCESS)){
                        Log.d(TAG, "receive ACTION_ALLOCATION_SEND_MSG_CHECK_MADE_NO_EXIST_SUCCESS");
                        //then check stock no

                        AllocationSendMsgItem item = myList.get(1);

                        if (item != null) {

                            Log.e(TAG, "getEditText() = "+item.getEditText().getText().toString());

                            Intent getStockNoIntent = new Intent(fragmentContext, CheckStockNoExistService.class);
                            getStockNoIntent.setAction(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_CHECK_STOCK_NO_EXIST_ACTION);
                            getStockNoIntent.putExtra("STOCK_NO", item.getContent());
                            fragmentContext.startService(getStockNoIntent);
                        }




                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_CHECK_MADE_NO_EXIST_FAILED)){
                        Log.d(TAG, "receive ACTION_ALLOCATION_SEND_MSG_CHECK_MADE_NO_EXIST_FAILED");

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_CHECK_STOCK_NO_EXIST_NOT_EXIST)){
                        Log.d(TAG, "receive ACTION_ALLOCATION_SEND_MSG_CHECK_STOCK_NO_EXIST_NOT_EXIST");

                        toast(getResources().getString(R.string.allocation_send_message_to_material_staging_area_not_matched));

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_CHECK_STOCK_NO_EXIST_SUCCESS)){
                        Log.d(TAG, "receive ACTION_ALLOCATION_SEND_MSG_CHECK_STOCK_NO_EXIST_SUCCESS");

                        AllocationSendMsgItem item = myList.get(0);

                        if (item != null) {
                            Intent getMadeInfoIntent = new Intent(fragmentContext, GetMadeInfoService.class);
                            getMadeInfoIntent.setAction(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_MADE_INFO_ACTION);
                            getMadeInfoIntent.putExtra("MADE_NO", item.getContent());
                            fragmentContext.startService(getMadeInfoIntent);
                        }

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_CHECK_STOCK_NO_EXIST_FAILED)){
                        Log.d(TAG, "receive ACTION_ALLOCATION_SEND_MSG_CHECK_STOCK_NO_EXIST_FAILED");

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_EMPTY)){
                        Log.d(TAG, "receive ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_EMPTY");
                        toast(getResources().getString(R.string.allocation_send_message_to_material_get_sfa_empty));
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_FAILED)){
                        Log.d(TAG, "receive ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_FAILED");
                        toast(getResources().getString(R.string.allocation_send_message_to_material_get_sfa_mess_failed));
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_SUCCESS)) {
                        Log.d(TAG, "receive ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_SUCCESS");

                        Intent get_X9115_sp_Intent = new Intent(fragmentContext, GetVarValueService.class);
                        get_X9115_sp_Intent.setAction(Constants.ACTION.ACTION_ALLOCATION_GET_TAG_ID_ACTION);
                        get_X9115_sp_Intent.putExtra("TAG_ID", "SHOW_9115");
                        fragmentContext.startService(get_X9115_sp_Intent);

                        if (allocationMsgStatusItemAdapter != null) {
                            //statusList.get(0).setSelected(true);
                            allocationMsgStatusItemAdapter.notifyDataSetChanged();
                        } else {
                            //statusList.get(0).setSelected(true);
                            allocationMsgStatusItemAdapter = new AllocationMsgStatusItemAdapter(fragmentContext, R.layout.allocation_msg_send_allocation_status_swipe_item, statusList);
                            statusListView.setAdapter(allocationMsgStatusItemAdapter);
                        }

                        toast(getResources().getString(R.string.look_up_in_stock_find_records, String.valueOf(statusList.size())));

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_GET_TAG_ID_SUCCESS)){
                        Log.d(TAG, "receive ACTION_ALLOCATION_GET_TAG_ID_SUCCESS");

                        String x9115_sp = intent.getStringExtra("GET_VAR_VALUE_RETURN");

                        if (hhh.Rows.size() > 0) {
                            hhh.Rows.clear();
                        } else {

                            hhh = new DataTable();
                        }
                        hhh.TableName = "HHHA";

                        hhh.Columns.Add("sfa03");
                        hhh.Columns.Add("ima021");
                        hhh.Columns.Add("img10");
                        hhh.Columns.Add("moved_qty");
                        hhh.Columns.Add("mess_qty");
                        hhh.Columns.Add("sfa05");
                        hhh.Columns.Add("sfa12");
                        hhh.Columns.Add("sfa11_name");
                        hhh.Columns.Add("tc_obf013");
                        hhh.Columns.Add("inv_qty");
                        hhh.Columns.Add("sfa06");
                        hhh.Columns.Add("sfa063");
                        hhh.Columns.Add("sfa161");
                        hhh.Columns.Add("img02");
                        hhh.Columns.Add("img03");
                        hhh.Columns.Add("img04");
                        hhh.Columns.Add("in_stock_no");
                        hhh.Columns.Add("in_locate_no");
                        hhh.Columns.Add("scan_sp");
                        hhh.Columns.Add("sfa11");
                        hhh.Columns.Add("sfa30");

                        for (DataRow rx : sfaMessTable.Rows) {
                            if (x9115_sp.equals("NO")) {
                                //myList.get(1) = stock_no
                                Log.e(TAG, "rx.getValue(sfa30) = "+rx.getValue("sfa30").toString()+", myList.get(1).getContent() = "+myList.get(1).getContent());
                                if (rx.getValue("sfa30").toString().equals(myList.get(1).getContent())) {
                                    continue;
                                }
                            }



                            DataRow newRow = hhh.NewRow();

                            newRow.setValue("sfa03", rx.getValue("sfa03"));
                            newRow.setValue("ima021", rx.getValue("ima021"));
                            newRow.setValue("img10", rx.getValue("img10"));
                            newRow.setValue("moved_qty", rx.getValue("moved_qty"));
                            newRow.setValue("mess_qty", rx.getValue("mess_qty"));
                            newRow.setValue("sfa05", rx.getValue("sfa05"));
                            newRow.setValue("sfa12", rx.getValue("sfa12"));
                            newRow.setValue("sfa11_name", rx.getValue("sfa11_name"));

                            if (rx.getValue("tc_obf013").toString().contains(".")) {
                                newRow.setValue("tc_obf013", Double.valueOf(rx.getValue("tc_obf013").toString()));
                            } else {
                                newRow.setValue("tc_obf013", Integer.valueOf(rx.getValue("tc_obf013").toString()));
                            }

                            if (rx.getValue("inv_qty").toString().contains(".")) {
                                newRow.setValue("inv_qty", Double.valueOf(rx.getValue("inv_qty").toString()));
                            } else {
                                newRow.setValue("inv_qty", Integer.valueOf(rx.getValue("inv_qty").toString()));
                            }

                            if (rx.getValue("sfa06").toString().contains(".")) {
                                newRow.setValue("sfa06", Double.valueOf(rx.getValue("sfa06").toString()));
                            } else {
                                newRow.setValue("sfa06", Integer.valueOf(rx.getValue("sfa06").toString()));
                            }

                            if (rx.getValue("sfa063").toString().contains(".")) {
                                newRow.setValue("sfa063", Double.valueOf(rx.getValue("sfa063").toString()));
                            } else {
                                newRow.setValue("sfa063", Integer.valueOf(rx.getValue("sfa063").toString()));
                            }

                            if (rx.getValue("sfa161").toString().contains(".")) {
                                newRow.setValue("sfa161", Double.valueOf(rx.getValue("sfa161").toString()));
                            } else {
                                newRow.setValue("sfa161", Integer.valueOf(rx.getValue("sfa161").toString()));
                            }

                            newRow.setValue("img02", rx.getValue("img02"));
                            newRow.setValue("img03", rx.getValue("img03"));
                            newRow.setValue("img04", rx.getValue("img04"));
                            newRow.setValue("in_stock_no", rx.getValue("in_stock_no"));
                            newRow.setValue("in_locate_no", rx.getValue("in_locate_no"));
                            newRow.setValue("scan_sp", rx.getValue("scan_sp"));
                            newRow.setValue("sfa11", rx.getValue("sfa11"));
                            newRow.setValue("sfa30", rx.getValue("sfa30"));

                            hhh.Rows.add(newRow);
                        }



                        /*if (statusList.size() > 0) {
                            btnDelete.setEnabled(true);
                        } else {
                            btnDelete.setEnabled(false);
                        }*/

                        /*Log.e(TAG, "========================================================");
                        for (int i=0; i<hhh.Rows.size(); i++) {

                            for (int j=0; j<hhh.Columns.size(); j++) {
                                System.out.print(hhh.Rows.get(i).getValue(j));
                                if (j < hhh.Columns.size() - 1) {
                                    System.out.print(", ");
                                }
                            }
                            System.out.print("\n");
                        }
                        Log.e(TAG, "========================================================");*/

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_GET_TAG_ID_FAILED)){
                        Log.d(TAG, "receive ACTION_ALLOCATION_GET_TAG_ID_FAILED");

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_MOVE_SUCCESS)){
                        Log.d(TAG, "receive ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_MOVE_SUCCESS");

                        String new_mess_no = intent.getStringExtra("MSG_RET");

                        if (new_mess_no.equals("NOQTY")) {
                            toast(getResources().getString(R.string.allocation_send_message_to_material_send_stock_empty));
                        } else if (new_mess_no.length() < 5 || !new_mess_no.substring(0 , 4).equals("DONE")) {
                            toast(getResources().getString(R.string.allocation_send_message_to_material_send_error));
                        } else {
                            String mess_no = new_mess_no.substring(4);
                            String mess_no_array[] = mess_no.split("#");
                            String rr="";

                            for (String s:
                                    mess_no_array) {
                                rr = rr + s+"\n";
                            }

                            /*for(int i=0;i<mess_no_array.length;i++)
                            {
                                rr=rr+mess_no_array[i]+"\n";
                            }*/

                            android.app.AlertDialog.Builder confirmdialog = new android.app.AlertDialog.Builder(fragmentContext);
                            confirmdialog.setIcon(R.drawable.ic_warning_black_48dp);
                            confirmdialog.setTitle(getResources().getString(R.string.allocation_send_message_to_material_the_msg_order_below));
                            confirmdialog.setMessage(rr);
                            confirmdialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                            confirmdialog.show();

                            hhh.clear();
                        }

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_MOVE_EMPTY)){
                        Log.d(TAG, "receive ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_MOVE_EMPTY");

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_MOVE_FAILED)){
                        Log.d(TAG, "receive ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_MOVE_FAILED");

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_ALLOCATION_SWIPE_LAYOUT_UPDATE)){
                        Log.d(TAG, "receive ACTION_ALLOCATION_SWIPE_LAYOUT_UPDATE");
                        if (allocationMsgStatusItemAdapter != null)
                            allocationMsgStatusItemAdapter.notifyDataSetChanged();
                    }

                }
            }
        };

        if (!isRegister) {
            filter = new IntentFilter();
            filter.addAction(Constants.ACTION.SOAP_CONNECTION_FAIL);
            filter.addAction(Constants.ACTION.ACTION_SOCKET_TIMEOUT);
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_LOCATE_NO_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_LOCATE_NO_EMPTY);
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_LOCATE_NO_FAILED);

            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_MADE_INFO_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_MADE_INFO_EMPTY);
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_MADE_INFO_FAILED);

            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_CHECK_MADE_NO_EXIST_NOT_EXIST);
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_CHECK_MADE_NO_EXIST_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_CHECK_MADE_NO_EXIST_FAILED);

            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_CHECK_STOCK_NO_EXIST_NOT_EXIST);
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_CHECK_STOCK_NO_EXIST_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_CHECK_STOCK_NO_EXIST_FAILED);

            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_EMPTY);
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_FAILED);

            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_GET_TAG_ID_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_GET_TAG_ID_FAILED);

            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_MOVE_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_MOVE_EMPTY);
            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_SEND_MSG_GET_SFA_MESS_MOVE_FAILED);

            filter.addAction(Constants.ACTION.ACTION_ALLOCATION_SWIPE_LAYOUT_UPDATE);

            //filter.addAction("unitech.scanservice.data");
            fragmentContext.registerReceiver(mReceiver, filter);
            isRegister = true;
            Log.d(TAG, "registerReceiver mReceiver");
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, "onDestroy");

        if (isRegister && mReceiver != null) {
            try {
                fragmentContext.unregisterReceiver(mReceiver);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            isRegister = false;
            mReceiver = null;
            Log.d(TAG, "unregisterReceiver mReceiver");
        }

        super.onDestroyView();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

    }

    public void toast(String message) {
        Toast toast = Toast.makeText(fragmentContext, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }


}
