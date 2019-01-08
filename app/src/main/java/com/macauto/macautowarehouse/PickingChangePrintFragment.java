package com.macauto.macautowarehouse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.macauto.macautowarehouse.data.Constants;
import com.macauto.macautowarehouse.service.GetLotCodeService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;
import static com.macauto.macautowarehouse.MainActivity.pda_type;
import static com.macauto.macautowarehouse.MainActivity.printArray;
import static com.macauto.macautowarehouse.MainActivity.screen_width;

public class PickingChangePrintFragment extends Fragment {
    private static final String TAG = PickingChangePrintFragment.class.getName();

    private Context fragmentContext;

    private static BroadcastReceiver mReceiver = null;
    private static boolean isRegister = false;

    //ProgressBar progressBar = null;
    //RelativeLayout relativeLayout;

    ProgressBar progressBar = null;
    RelativeLayout relativeLayout;
    InputMethodManager imm;

    LinearLayout layoutPickingChange;
    LinearLayout layoutPickingPrint;
    TextView pickingPartNo, pickingDate, pickingBatchNo, pickingSendNo, pickingPackageType, pickingPackageSerial, pickingSupplierNo, pickingName, pickingStorage;
    EditText pickingQuantity;
    Button btnConfirm;


    TextView pickingChangeTitle, pickingChangeSendNoPrint, pickingChangePartNoPrint, pickingChangeQuantityPrint, pickingChangeSupplierNoPrint, pickingChangeStorageTypePrint
            , pickingChangeNamePrint, pickingProductionDatePrint, pickingBatchPrint, pickingPackageTypePrint, pickingPackageSerialPrint;

    ImageView imageViewQRCodePrint;
    private Locale current;
    //ImageView imageViewQRCode;
    //TextView resultPartNo, resultBatchNo, resultName, resultSpec, resultQuantity;
    //Button btnPrint, btnReGenerate;

    //
    private int original_quantity = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentContext = getContext();

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");



        final  View view = inflater.inflate(R.layout.picking_change_print_fragment, container, false);
        imm = (InputMethodManager)fragmentContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        current = fragmentContext.getResources().getConfiguration().locale;
        //progress bar
        //relativeLayout = view.findViewById(R.id.print_test_container);
        //progressBar = new ProgressBar(fragmentContext,null,android.R.attr.progressBarStyleLarge);
        //RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(screen_width/4,screen_width/4);
        //params.addRule(RelativeLayout.CENTER_IN_PARENT);
        //relativeLayout.addView(progressBar,params);
        //progressBar.setVisibility(View.GONE);

        imm = (InputMethodManager)fragmentContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        relativeLayout = view.findViewById(R.id.picking_change_container);

        progressBar = new ProgressBar(fragmentContext,null,android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(screen_width/4,screen_width/4);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        relativeLayout.addView(progressBar,params);
        progressBar.setVisibility(View.GONE);

        layoutPickingChange = view.findViewById(R.id.layoutPickingChange);
        layoutPickingPrint = view.findViewById(R.id.layoutPickingPrint);



        pickingPartNo = view.findViewById(R.id.pickingChangePartNo);
        pickingDate = view.findViewById(R.id.pickingChangeDate);
        pickingBatchNo = view.findViewById(R.id.pickingChangeBatchNo);
        pickingSendNo = view.findViewById(R.id.pickingChangeSendNo);
        pickingPackageType = view.findViewById(R.id.pickingChangePackageType);
        pickingPackageSerial = view.findViewById(R.id.pickingChangePackageSerial);
        pickingQuantity = view.findViewById(R.id.pickingChangeQuantity);
        pickingSupplierNo = view.findViewById(R.id.pickingChangeSupplierNo);
        pickingName = view.findViewById(R.id.pickingChangeName);
        pickingStorage = view.findViewById(R.id.pickingChangeStorage);

        btnConfirm = view.findViewById(R.id.btnPickingChangeConfirm);

        pickingChangeTitle = view.findViewById(R.id.pickingChangeTitle);
        pickingChangeSendNoPrint = view.findViewById(R.id.pickingChangeSendNoPrint);
        pickingChangePartNoPrint = view.findViewById(R.id.pickingChangePartNoPrint);
        pickingChangeQuantityPrint = view.findViewById(R.id.pickingChangeQuantityPrint);
        pickingChangeSupplierNoPrint = view.findViewById(R.id.pickingChangeSupplierNoPrint);
        pickingChangeStorageTypePrint = view.findViewById(R.id.pickingChangeStorageTypePrint);
        pickingChangeNamePrint = view.findViewById(R.id.pickingChangeNamePrint);
        pickingProductionDatePrint = view.findViewById(R.id.pickingProductionDatePrint);
        pickingBatchPrint = view.findViewById(R.id.pickingBatchPrint);
        pickingPackageTypePrint = view.findViewById(R.id.pickingPackageTypePrint);
        pickingPackageSerialPrint = view.findViewById(R.id.pickingPackageSerialPrint);

        imageViewQRCodePrint = view.findViewById(R.id.imageViewQRCodePrint);

        //btnPrint = view.findViewById(R.id.btnPrint);
        //btnReGenerate = view.findViewById(R.id.btnReGenerate);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (Integer.valueOf(pickingQuantity.getText().toString()) > original_quantity) {
                        toast(fragmentContext.getResources().getString(R.string.picking_change_quantity_much_more));
                    } else if (Integer.valueOf(pickingQuantity.getText().toString()) < 0) {
                        toast(fragmentContext.getResources().getString(R.string.picking_change_quantity_less_than_zero));
                    } else {
                        layoutPickingChange.setVisibility(View.GONE);
                        layoutPickingPrint.setVisibility(View.VISIBLE);

                        Calendar calendar = Calendar.getInstance();

                        Date today = calendar.getTime();
                        calendar.setTime(today);
                        String fToday = new SimpleDateFormat("yyyy-MM-dd", current).format(today);

                        pickingChangeTitle.setText(fragmentContext.getResources().getString(R.string.picking_change_title_print, fToday));
                        pickingChangeSendNoPrint.setText(fragmentContext.getResources().getString(R.string.picking_change_send_no_print, printArray.get(3)+"-"+printArray.get(4)));
                        pickingChangePartNoPrint.setText(fragmentContext.getResources().getString(R.string.picking_change_part_no_print, printArray.get(0)));
                        pickingChangeQuantityPrint.setText(fragmentContext.getResources().getString(R.string.picking_change_quantity_print, pickingQuantity.getText()));
                        pickingChangeSupplierNoPrint.setText(fragmentContext.getResources().getString(R.string.picking_change_support_print, printArray.get(8)));
                        pickingChangeStorageTypePrint.setText(fragmentContext.getResources().getString(R.string.picking_change_storage_print, printArray.get(11)));
                        pickingChangeNamePrint.setText(fragmentContext.getResources().getString(R.string.picking_change_name_print, printArray.get(10)));
                        pickingProductionDatePrint.setText(fragmentContext.getResources().getString(R.string.picking_change_date_print, printArray.get(1)));
                        pickingBatchPrint.setText(fragmentContext.getResources().getString(R.string.picking_change_batch_no_print, printArray.get(2)));

                        if (printArray.get(5).equals("1")) {
                            pickingPackageTypePrint.setText(fragmentContext.getResources().getString(R.string.picking_change_package_type_print_small));
                        } else if (printArray.get(5).equals("2")) {
                            pickingPackageTypePrint.setText(fragmentContext.getResources().getString(R.string.picking_change_package_type_print_large));
                        } else {
                            pickingPackageTypePrint.setText(fragmentContext.getResources().getString(R.string.picking_change_package_type_print_na));
                        }

                        pickingPackageSerialPrint.setText(fragmentContext.getResources().getString(R.string.picking_change_package_serial_print, printArray.get(6)));


                        String input = printArray.get(0)+"#"+
                                printArray.get(1)+"#"+
                                printArray.get(2)+"#"+
                                printArray.get(3)+"#"+
                                printArray.get(4)+"#"+
                                printArray.get(5)+"#"+
                                printArray.get(6)+"#"+
                                pickingQuantity.getText()+"#";


                        try {
                            // generate a 150x150 QR code
                            Bitmap bm = encodeAsBitmap(input,100, 100);

                            if(bm != null) {
                                imageViewQRCodePrint.setImageBitmap(bm);

                            }
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }

                        Intent genIntent = new Intent();
                        genIntent.setAction(Constants.ACTION.ACTION_PRINT_TEST_SHOW_FAB_BUTTON);
                        fragmentContext.sendBroadcast(genIntent);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    toast(fragmentContext.getResources().getString(R.string.picking_change_quantity_format_error));
                }





            }
        });

        IntentFilter filter;

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction() != null) {
                    if (intent.getAction().equalsIgnoreCase(Constants.ACTION.SOAP_CONNECTION_FAIL)) {
                        Log.d(TAG, "receive SOAP_CONNECTION_FAIL");
                        progressBar.setVisibility(View.GONE);
                        toast(fragmentContext.getResources().getString(R.string.socket_failed));

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_SOCKET_TIMEOUT)) {
                        Log.d(TAG, "receive ACTION_SOCKET_TIMEOUT");
                        progressBar.setVisibility(View.GONE);
                        toast(fragmentContext.getResources().getString(R.string.socket_timeout));
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_PRINT_TEST_SHOW_GENERATE)) {
                        Log.d(TAG, "receive ACTION_PRINT_TEST_SHOW_GENERATE !");

                        //linearLayoutInputView.setVisibility(View.VISIBLE);
                        //linearLayoutPrintView.setVisibility(View.GONE);

                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_GET_LOT_CODE_FAILED)) {
                        Log.d(TAG, "receive ACTION_GET_LOT_CODE_FAILED !");
                        progressBar.setVisibility(View.GONE);
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_GET_LOT_CODE_EMPTY)) {
                        Log.d(TAG, "receive ACTION_GET_LOT_CODE_EMPTY !");
                        progressBar.setVisibility(View.GONE);
                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_GET_LOT_CODE_SUCCESS)) {
                        Log.d(TAG, "receive ACTION_GET_LOT_CODE_SUCCESS !");

                        progressBar.setVisibility(View.GONE);

                        layoutPickingChange.setVisibility(View.VISIBLE);

                        pickingPartNo.setText(printArray.get(0));
                        pickingDate.setText(printArray.get(1));
                        pickingBatchNo.setText(printArray.get(2));
                        if (printArray.get(4).equals("")) {
                            pickingSendNo.setText(printArray.get(3));
                        } else {
                            pickingSendNo.setText(printArray.get(3)+"-"+printArray.get(4));
                        }

                        pickingPackageType.setText(printArray.get(5));
                        pickingPackageSerial.setText(printArray.get(6));
                        pickingQuantity.setText(printArray.get(7));

                        original_quantity = Integer.valueOf(printArray.get(7));

                        pickingSupplierNo.setText(printArray.get(8));
                        pickingName.setText(printArray.get(10));
                        pickingStorage.setText(printArray.get(11));



                    } else if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_PICKING_CHANGE_HIDE_GENERATE)) {
                        Log.d(TAG, "receive ACTION_PICKING_CHANGE_HIDE_GENERATE !");

                        layoutPickingPrint.setVisibility(View.GONE);
                        layoutPickingChange.setVisibility(View.GONE);

                    } else if("unitech.scanservice.data" .equals(intent.getAction()) || "com.qs.scancode".equals(intent.getAction())) {
                        Log.d(TAG, "unitech.scanservice.data | com.qs.scancode");
                        Bundle bundle = intent.getExtras();
                        if(bundle != null )
                        {
                            progressBar.setVisibility(View.VISIBLE);

                            Intent genIntent = new Intent();
                            genIntent.setAction(Constants.ACTION.ACTION_PRINT_TEST_HIDE_FAB_BUTTON);
                            fragmentContext.sendBroadcast(genIntent);

                            layoutPickingPrint.setVisibility(View.GONE);
                            layoutPickingChange.setVisibility(View.GONE);
                            //String text = bundle.getString("text");
                            String text;
                            if (pda_type == 2) {
                                text = bundle.getString("code");
                            } else {
                                text = bundle.getString("text");
                            }
                            Log.e(TAG, "msg = "+text);

                            text = text.replaceAll("\\n","");
                            toast(text);

                            Intent getintent = new Intent(context, GetLotCodeService.class);
                            getintent.putExtra("BAR_CODE", text);
                            context.startService(getintent);

                            //is_barcode_receive = false;
                            if (text.length() > 0 ) {
                                int counter = 0;
                                for( int i=0; i<text.length(); i++ ) {
                                    if( text.charAt(i) == '#' ) {
                                        counter++;
                                    }
                                }

                                Log.e(TAG, "counter = "+counter);

                            /*if (counter == 8) {

                                if (!is_scan_receive) {
                                    //set scan true
                                    is_scan_receive = true;
                                    //regenerate new session id
                                    GenerateRandomString rString = new GenerateRandomString();
                                    k_id = rString.randomString(32);
                                    Log.e(TAG, "session_id = "+k_id);



                                    String codeArray[] = text.split("#");
                                    Intent scanResultIntent = new Intent(Constants.ACTION.ACTION_SET_INSPECTED_RECEIVE_ITEM_CLEAN);
                                    for (int i = 0; i < codeArray.length; i++) {
                                        Log.e(TAG, "codeArray[" + i + "] = " + codeArray[i]);
                                        String column = "COLUMN_" + String.valueOf(i);
                                        scanResultIntent.putExtra(column, codeArray[i]);
                                    }
                                    barcode = text;

                                    //scanResultIntent.putExtra("BARCODE", text);
                                    scanResultIntent.putExtra("K_ID", k_id);
                                    fragmentContext.sendBroadcast(scanResultIntent);
                                }

                            } else {

                                text = text.replaceAll("\\n","");
                                toast(text);
                                //if (no_list.size() > 0 && detailList.size() > 0) {
                                if (scan_list.size() > 0) {

                                    if (item_select != -1) { //scan locate
                                        if (dataTable != null && dataTable.Rows.size() > 0) {
                                            dataTable.Rows.get(item_select).setValue("rvv33", text);
                                            dataTable.Rows.get(item_select).setValue("rvv33_scan", text);
                                        }
                                        scan_list.get(item_select).setCol_rvv33(text);
                                        scan_list.get(item_select).setChecked(true);
                                        //check_stock_in.set(item_select, true);

                                        if (inspectedReceiveItemAdapter != null)
                                            inspectedReceiveItemAdapter.notifyDataSetChanged();
                                    }


                                }
                            }*/

                            }
                        }
                    }


                }


            }
        };

        if (!isRegister) {
            filter = new IntentFilter();
            filter.addAction(Constants.ACTION.SOAP_CONNECTION_FAIL);
            filter.addAction(Constants.ACTION.ACTION_SOCKET_TIMEOUT);
            filter.addAction(Constants.ACTION.ACTION_PRINT_TEST_SHOW_GENERATE);
            filter.addAction(Constants.ACTION.ACTION_GET_LOT_CODE_FAILED);
            filter.addAction(Constants.ACTION.ACTION_GET_LOT_CODE_EMPTY);
            filter.addAction(Constants.ACTION.ACTION_GET_LOT_CODE_SUCCESS);
            filter.addAction(Constants.ACTION.ACTION_PICKING_CHANGE_HIDE_GENERATE);
            filter.addAction("unitech.scanservice.data");
            filter.addAction("com.qs.scancode");
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
        Toast toast = Toast.makeText(fragmentContext, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }

    Bitmap encodeAsBitmap(String str, int in_width, int in_height) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, in_width, in_height, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, in_width, 0, 0, w, h);
        return bitmap;
    }
}
