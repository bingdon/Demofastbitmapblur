package com.example.demolist;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

public class MyColorTools {

	private ColorMatrix mAllMatrix;
	private ColorMatrix saturationMatrix;
	private ColorMatrix hubMatrix;
	private ColorMatrix lightMatrix;
	private float Saturation=0f;
	
	public MyColorTools() {
		// TODO Auto-generated constructor stub
		mAllMatrix=new ColorMatrix();
		saturationMatrix=new ColorMatrix();
		hubMatrix=new ColorMatrix();
		lightMatrix=new ColorMatrix();
	}
	
	public Bitmap getSaturation(Bitmap bitmap,float flag){
		Bitmap bmp=Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas mCanvas=new Canvas(bmp);
		Paint mPaint=new Paint();
		mPaint.setAntiAlias(true);
		saturationMatrix.reset();
		mAllMatrix.reset();
		saturationMatrix.setSaturation(flag);
//		hubMatrix.setScale(1, 1, 1, 1);
		lightMatrix.setRotate(0, -9.6f);
		lightMatrix.setRotate(1,-9.6f);
		lightMatrix.setRotate(2, -9.6f);
//		mAllMatrix.postConcat(hubMatrix);
		mAllMatrix.postConcat(saturationMatrix);
		mAllMatrix.postConcat(lightMatrix);
		mPaint.setColorFilter(new ColorMatrixColorFilter(mAllMatrix));
		mCanvas.drawBitmap(bitmap, 0, 0, mPaint);
		return bmp;
	}

}
