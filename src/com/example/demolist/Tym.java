package com.example.demolist;

import java.io.IOException;
import java.io.InputStream;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Region.Op;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Tym extends Activity {
	
	private SurfaceView mSurfaceView;
	private SurfaceHolder mHolder;
	private Button mButton;
	Bitmap bmp,bmp2;
	InputStream is;
	Canvas canvas;
	Bitmap bitmap;
	 
	ImageView imageView,cImageView;
	AlphaFilter mAlphaFilter;
	Bitmap newBitmap;//切割后
	BitMh mBitMh;
	MyColorTools sation;
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		setContentView(R.layout.activity_tym);
		mAlphaFilter=new AlphaFilter();
		mBitMh=new BitMh();
		sation=new MyColorTools();
		try {
//			is = getResources().getAssets().open("bg17_1_001.jpg");
			is = getResources().getAssets().open("bg11.JPG");
			bmp=BitmapFactory.decodeStream(is);//截取图片
			bmp2=readBitMap(this, R.drawable.tyx);//图片 边框
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		imageView=(ImageView)findViewById(R.id.image00f);
		cImageView=(ImageView)findViewById(R.id.mywjia);
		
		mButton=(Button)findViewById(R.id.button1);
		mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				 bitmap = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(),bmp.getConfig());
//				 canvas=new Canvas(bitmap);
				Thread mThread=new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
//						blurImage(bmp, bmp2, canvas, 100);
						Log.i("计时", "开始运行");
						bmp= mBitMh.fastblur(bmp, 20);
						newBitmap=mAlphaFilter.overlay(bmp, bmp2);
						newBitmap=sation.getSaturation(newBitmap, 0.5f);
						Log.i("计时", "结束运行");
						Log.i("图片", "图片高:"+newBitmap.getHeight()+"宽:"+newBitmap.getWidth());
						Message msg=new Message();
						msg.what=0;
						msg.obj=1;
						myHandler.sendMessage(msg);
					}	
				});
				mThread.start();
//				mPaint.setColor(Color.RED);
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tym, menu);
		return true;
	}

	private  void blurImage(Bitmap bitmap_main,Bitmap bitmap_over,Canvas canvas,int blur) {

        int width =  bitmap_main.getWidth();
        int height = bitmap_main.getHeight();

        //设置覆盖图的图片的宽和高与模糊图片相同
        bitmap_over = setOverImage(bitmap_over, width,height );
        Paint paint = new Paint();
        //消除锯齿
        paint.setAntiAlias(true);
        //先画要模糊的图片
        canvas.drawBitmap(bitmap_main, 0, 0, paint);
        //设置画笔透明度(透明度越低，越模糊)
        paint.setAlpha(blur);
        //画上覆盖图片
        canvas.drawBitmap(bitmap_over, 0, 0, paint);
        //到这里图片模糊已经完成

        //下面设置以图片中心为原点，宽的四分之一的圆内不模糊
        canvas.save();
        //设置画笔不透明
        paint.setAlpha(255);
        //设置路径
        Path mPath = new Path();

        //添加圆
        mPath.addCircle(width/2, height/2, width/4, Path.Direction.CCW); 
        //设置绘图部分
        canvas.clipPath(mPath, Op.REPLACE); 
        //再绘画上模糊的图片
        canvas.drawBitmap(bitmap_main, 0, 0, paint);
        canvas.restore();  
}
	
	
	 private Bitmap setOverImage(Bitmap bmp,int new_width, int new_height) {
         Bitmap bitmap = null;
         try {
                 int width = bmp.getWidth();
                 int height = bmp.getHeight();

                 float scale_w = ((float)new_width)/width;
                 float scale_h = ((float)new_height)/height;
                 // 创建操作图片用的matrix对象
                 Matrix matrix = new Matrix();
                 // 缩放图片动作
                 matrix.postScale(scale_w, scale_h);
                 // 创建新的图片
                 bitmap = Bitmap.createBitmap(bmp, 0, 0, width, height,matrix, true);
         } catch (Exception e) {
                 e.printStackTrace();
         }
         return bitmap;
 }
	
	 private Handler myHandler =new Handler(){
			public void handleMessage(Message msg) {
				int what = msg.what;
				switch (what) {
				case 0:
					imageView.setImageBitmap(newBitmap);
					android.view.ViewGroup.LayoutParams iParams=imageView.getLayoutParams();
					iParams.width=720;
					iParams.height=1280;
					imageView.setLayoutParams(iParams);
//					Tym.this.getWindow().setBackgroundDrawable(new BitmapDrawable(newBitmap));
					Toast.makeText(Tym.this, "切换完成", Toast.LENGTH_LONG).show();
					break;
					
				}
				super.handleMessage(msg);
				}
		};
		/**
		 * 
		 * @param context 指针
		 * @param resId 图片地址
		 * @return 读取的图片
		 */
		public static Bitmap readBitMap(Context context, int resId){  
	          BitmapFactory.Options opt = new BitmapFactory.Options();  
	          opt.inPreferredConfig = Bitmap.Config.RGB_565;   
	          opt.inPurgeable = true;  
	          opt.inInputShareable = true;  
	          //获取资源图片  
	          InputStream is = context.getResources().openRawResource(resId);  
	          return BitmapFactory.decodeStream(is,null,opt);  
	    }
}
