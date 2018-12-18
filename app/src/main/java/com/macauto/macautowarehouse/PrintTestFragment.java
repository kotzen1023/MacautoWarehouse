package com.macauto.macautowarehouse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
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
import com.macauto.macautowarehouse.data.SearchItemAdapter;
import com.macauto.macautowarehouse.service.GetPartWarehouseListService;


import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;
import static com.macauto.macautowarehouse.MainActivity.pda_type;
import static com.macauto.macautowarehouse.MainActivity.searchList;
import static com.macauto.macautowarehouse.MainActivity.sortedSearchList;

public class PrintTestFragment extends Fragment {
    private static final String TAG = PrintTestFragment.class.getName();

    private Context fragmentContext;

    private static BroadcastReceiver mReceiver = null;
    private static boolean isRegister = false;

    ProgressBar progressBar = null;
    RelativeLayout relativeLayout;

    InputMethodManager imm;

    LinearLayout linearLayoutInputView;
    LinearLayout linearLayoutPrintView;
    EditText testPartNo, testBatchNo, testName, testSpec, testQuantity;
    Button btnGenerate;
    ImageView imageViewQRCode;
    TextView resultPartNo, resultBatchNo, resultName, resultSpec, resultQuantity;
    //Button btnPrint, btnReGenerate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentContext = getContext();

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");



        final  View view = inflater.inflate(R.layout.print_test_fragment, container, false);
        imm = (InputMethodManager)fragmentContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        //progress bar
        relativeLayout = view.findViewById(R.id.print_test_container);
        progressBar = new ProgressBar(fragmentContext,null,android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        relativeLayout.addView(progressBar,params);
        progressBar.setVisibility(View.GONE);

        imm = (InputMethodManager)fragmentContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        linearLayoutInputView = view.findViewById(R.id.layoutPrintView);
        linearLayoutPrintView = view.findViewById(R.id.printResultView);

        testPartNo = view.findViewById(R.id.testPartNo);
        testBatchNo = view.findViewById(R.id.testBatchNo);
        testName = view.findViewById(R.id.testName);
        testSpec = view.findViewById(R.id.testSpec);
        testQuantity = view.findViewById(R.id.testQuantity);

        btnGenerate = view.findViewById(R.id.btnGenerate);
        imageViewQRCode = view.findViewById(R.id.imageViewQRCode);

        resultPartNo = view.findViewById(R.id.resultPartNo);
        resultBatchNo = view.findViewById(R.id.resultBatchNo);
        resultName = view.findViewById(R.id.resultName);
        resultSpec = view.findViewById(R.id.resultSpec);
        resultQuantity = view.findViewById(R.id.resultQuantity);

        //btnPrint = view.findViewById(R.id.btnPrint);
        //btnReGenerate = view.findViewById(R.id.btnReGenerate);

        btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (testPartNo.getText().toString().length() > 0 && testBatchNo.getText().toString().length() > 0) {

                    String input = testPartNo.getText().toString()+"#"+testBatchNo.getText().toString();

                    try {
                        // generate a 150x150 QR code
                        Bitmap bm = encodeAsBitmap(input,135, 135);

                        if(bm != null) {
                            imageViewQRCode.setImageBitmap(bm);
                            linearLayoutInputView.setVisibility(View.GONE);
                            linearLayoutPrintView.setVisibility(View.VISIBLE);
                        }
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }

                    if (testPartNo.getText().toString().length() > 0)
                        resultPartNo.setText(testPartNo.getText().toString());
                    if (testBatchNo.getText().toString().length() > 0)
                        resultBatchNo.setText(testBatchNo.getText().toString());
                    if (testName.getText().toString().length() > 0 )
                        resultName.setText(testName.getText().toString());
                    if (testSpec.getText().toString().length() > 0)
                        resultSpec.setText(testSpec.getText().toString());
                    if (testQuantity.getText().toString().length() > 0)
                        resultQuantity.setText(testQuantity.getText().toString());

                    Intent genIntent = new Intent();
                    genIntent.setAction(Constants.ACTION.ACTION_PRINT_TEST_SHOW_FAB_BUTTON);
                    fragmentContext.sendBroadcast(genIntent);


                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


                } else {
                    toast("Part no and Batch can't no be empty");
                }


            }
        });

        IntentFilter filter;

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction() != null) {
                    if (intent.getAction().equalsIgnoreCase(Constants.ACTION.ACTION_PRINT_TEST_SHOW_GENERATE)) {
                        Log.d(TAG, "receive ACTION_PRINT_TEST_SHOW_GENERATE !");

                        linearLayoutInputView.setVisibility(View.VISIBLE);
                        linearLayoutPrintView.setVisibility(View.GONE);

                    }


                }


            }
        };

        if (!isRegister) {
            filter = new IntentFilter();
            filter.addAction(Constants.ACTION.ACTION_PRINT_TEST_SHOW_GENERATE);

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