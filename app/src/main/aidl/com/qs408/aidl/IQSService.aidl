

package com.qs408.aidl;

import android.graphics.Bitmap;

interface IQSService
{


    void openScan();


	void printText(int size,int align,String text);


	void printBitmap(int align,in Bitmap bitmap);


	void printBarCode(int align, int width, int height, String data);


	void printQRCode(int width, int height,String data);

	void sendCMD(in byte[] list);


}