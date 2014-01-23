package com.example.demolist;

import java.io.IOException;
import java.io.InputStream;



import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.ArcShape;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnTouchListener {

	 private Button judgenet;
	 private ImageView imageView,imageView2,imageView3;
	 Bitmap bmp;
	 InputStream is;
	 BitMh bitMh;
	 Bitmap bitmap2;
	 Bitmap bitmap;
	 
	 GestureDetector mGestureDetector;
	 private ShapeDrawable mDrawable=null;
	 Canvas mCanvas=null;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		judgenet=(Button)findViewById(R.id.judgenet);
		imageView=(ImageView)findViewById(R.id.image00f);
		imageView2=(ImageView)findViewById(R.id.imageView2);
		imageView3=(ImageView)findViewById(R.id.imageView3);
		bitMh=new BitMh();
//		mDrawable=new ShapeDrawable(new ArcShape(45, -270));
//		mDrawable.getPaint().setColor(Color.RED);
//		bitmap2 = Bitmap.createBitmap(320, 320, Config.ARGB_8888);
//		mCanvas=new Canvas(bitmap2);
//		imageView.setImageBitmap(bitmap2);
		try {
//			is = getResources().getAssets().open("cool0.jpg");
//			is = getResources().getAssets().open("bg19_1.jpg");
			is = getResources().getAssets().open("bg5.JPG");
//			is = getResources().getAssets().open("kkk.png");
			bmp=BitmapFactory.decodeStream(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		judgenet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ConnectivityManager cManager=(ConnectivityManager)getSystemService(Activity.CONNECTIVITY_SERVICE);
				boolean wifi=cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
				boolean edge=cManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();
				if (wifi) {
					Toast.makeText(MainActivity.this, "WIFI未连接", Toast.LENGTH_LONG).show();
				}
				Toast.makeText(MainActivity.this, "图片:"+bmp+"输入流"+is, Toast.LENGTH_LONG).show();
//				Bitmap bitmap=blurImageAmeliorate(bmp);
//				imageView.setImageBitmap(bitmap);
//				imageView2.setImageBitmap(bitmap2);
				Thread mThread=new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						for (int i = 0; i < 10; i++) {
//							 bitmap	=blurImageAmeliorate(bmp);
//							 bitmap	=blurImageAmelioratebf(bmp);
							 bitmap=bitMh.fastblur(bmp, 10);
							 bmp=bitmap;
//							 mDrawable.setBounds(10, 10, 200, 200);
//							 mDrawable.draw(mCanvas);
							 Log.i("完成", "完成:"+i);
							 Message msg=new Message();
							 msg.what=0;
							 msg.obj=1;
							 myHandler.sendMessage(msg);
						}
//						imageView.setImageBitmap(bmp);
						
					}
				});
			mThread.start();
			}
		});
		mGestureDetector =new GestureDetector(this, new Mygesture());
		imageView.setOnTouchListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
	        if((System.currentTimeMillis()-exitTime) > 2000){  
	            Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();                                
	            exitTime = System.currentTimeMillis();   
	        } else {
	            finish();
	            System.exit(0);
	        }
	        return true;   
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	 private Bitmap blurImageAmeliorate(Bitmap bmp)  
	    {  
	        long start = System.currentTimeMillis();  
	        // 楂樻柉鐭╅樀  
	        int[] gauss = new int[] { 1, 2, 1, 2, 4, 2, 1, 2, 1 };  
	        int[] gauss1 = new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1 };  
	          
//	        int width = bmp.getWidth();  
//	        int height = bmp.getHeight();  
	        int width = bmp.getWidth();  
	        int height = bmp.getHeight();  
	        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);  
	          
	        int pixR = 0;  
	        int pixG = 0;  
	        int pixB = 0;  
	          
	        int pixColor = 0;  
	          
	        int newR = 0;  
	        int newG = 0;  
	        int newB = 0;  
	          
	        int delta = 16; // 鍊艰秺灏忓浘鐗囦細瓒婁寒锛岃秺澶у垯瓒婃殫  
	        int delta0=9;
	          
	        int idx = 0;  
	        int[] pixels = new int[width * height];  
	        bmp.getPixels(pixels, 0, width, 0, 0, width, height);  
	        for (int i = 1, length = height - 1; i < length; i++)  
	        {  
	            for (int k = 1, len = width - 1; k < len; k++)  
	            {  
	                idx = 0;  
	                
	    
	                	for (int m = -1; m <= 1; m++)  
		                {  
		                    for (int n = -1; n <= 1; n++)  
		                    {  
		                        pixColor = pixels[(i + m) * width + k + n];  
		                        pixR = Color.red(pixColor);  
		                        pixG = Color.green(pixColor);  
		                        pixB = Color.blue(pixColor);  
		                          
		                        newR = newR + (int) (pixR * gauss[idx]);  
		                        newG = newG + (int) (pixG * gauss[idx]);  
		                        newB = newB + (int) (pixB * gauss[idx]);  
		                        idx++;  
		                    }  
		                }  
					
//	                for (int m = -1; m <= 1; m++)  
//	                {  
//	                    for (int n = -1; n <= 1; n++)  
//	                    {  
//	                        pixColor = pixels[(i + m) * width + k + n];  
//	                        pixR = Color.red(pixColor);  
//	                        pixG = Color.green(pixColor);  
//	                        pixB = Color.blue(pixColor);  
//	                          
//	                        newR = newR + (int) (pixR * gauss[idx]);  
//	                        newG = newG + (int) (pixG * gauss[idx]);  
//	                        newB = newB + (int) (pixB * gauss[idx]);  
//	                        idx++;  
//	                    }  
//	                } 
	                
	                
//	                if (i>=height/2) {
//	                	for (int m = -1; m <= 1; m++)  
//		                {  
//		                    for (int n = -1; n <= 1; n++)  
//		                    {  
//		                        pixColor = pixels[(i + m) * width + k + n];  
//		                        pixR = Color.red(pixColor);  
//		                        pixG = Color.green(pixColor);  
//		                        pixB = Color.blue(pixColor);  
//		                          
//		                        newR = newR + (int) (pixR * gauss1[idx]);  
//		                        newG = newG + (int) (pixG * gauss1[idx]);  
//		                        newB = newB + (int) (pixB * gauss1[idx]);  
//		                        idx++;  
//		                    }  
//		                } 
//					}
	                  
	                newR /= delta;  
	                newG /= delta;  
	                newB /= delta;  
	                  
	                newR = Math.min(255, Math.max(0, newR));  
	                newG = Math.min(255, Math.max(0, newG));  
	                newB = Math.min(255, Math.max(0, newB));  
	                  
	                pixels[i * width + k] = Color.argb(0, newR, newG, newB);  
	                  
	                newR = 0;  
	                newG = 0;  
	                newB = 0;  
	            }  
	        }  
	          
	        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);  
	        long end = System.currentTimeMillis();  
	        Log.d("may", "used time="+(end - start));  
	        return bitmap;  
	    }  
	
//	 public Bitmap getRes(String name)
//	 {
//		 ApplicationInfo appInfo = getApplicationInfo();
//		 int resID = getResources().getIdentifier(name, drawable, appInfo.packageName);
//		 return BitmapFactory.decodeResource(getResources(), resID);
//	 銆�銆�}
	 
	 private Handler myHandler =new Handler(){
			public void handleMessage(Message msg) {
				int what = msg.what;
				switch (what) {
				case 0:
//					imageView.setImageBitmap(bitmap);
					imageView2.setImageBitmap(bitmap);
//					imageView.invalidate();
					break;
					
				}
				super.handleMessage(msg);
				}
		};
	 
		class Mygesture implements OnGestureListener{

			@Override
			public boolean onDown(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				// TODO Auto-generated method stub
				  Log.i("鍛靛懙", "鍏跺疄"+(e2.getY()-e1.getY()));
				  if (e2.getY()-e1.getY()<-10) {
					Toast.makeText(MainActivity.this, "閫�鍑�", Toast.LENGTH_LONG).show();
					Intent intent=new Intent(MainActivity.this, Tym.class);
					startActivity(intent);
					overridePendingTransition(R.anim.push_up_in,
							R.anim.push_up_out);
				}
				return false;
			}

			@Override
			public void onLongPress(MotionEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onShowPress(MotionEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			mGestureDetector.onTouchEvent(event);
			return true;
		}
		
		
		 private Bitmap blurImageAmelioratebf(Bitmap bmp)  
		    {  
		        long start = System.currentTimeMillis();  
		        // 楂樻柉鐭╅樀  
		        int[] gauss = new int[] { 1, 2, 1, 2, 4, 2, 1, 2, 1 };  
		        int[] gauss1 = new int[] { 1, 1, 1, 1, 1, 1, 1, 1, 1 };  
		          
//		        int width = bmp.getWidth();  
//		        int height = bmp.getHeight();  
		        int width = bmp.getWidth();  
		        int height = bmp.getHeight();  
		        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);  
		          
		        int pixR = 0;  
		        int pixG = 0;  
		        int pixB = 0;  
		          
		        int pixColor = 0;  
		          
		        int newR = 0;  
		        int newG = 0;  
		        int newB = 0;  
		          
		        int delta = 16; // 鍊艰秺灏忓浘鐗囦細瓒婁寒锛岃秺澶у垯瓒婃殫  
		        int delta0=9;
		          
		        int idx = 0;  
		        int[] pixels = new int[width * height];  
		        bmp.getPixels(pixels, 0, width, 0, 0, width, height);  
		        for (int i = 1, length = height - 1; i < length; i++)  
		        {  
		            for (int k = 1, len = width - 1; k < len; k++)  
		            {  
		                idx = 0;  
		                
		    
		                	for (int m = -1; m <= 1; m++)  
			                {  
			                    for (int n = -1; n <= 1; n++)  
			                    {  
			                        pixColor = pixels[(i + m) * width + k + n];  
			                        pixR = Color.red(pixColor);  
			                        pixG = Color.green(pixColor);  
			                        pixB = Color.blue(pixColor);  
			                          
			                        newR = newR + (int) (pixR * gauss[idx]);  
			                        newG = newG + (int) (pixG * gauss[idx]);  
			                        newB = newB + (int) (pixB * gauss[idx]);  
			                        idx++;  
			                    }  
			                }  
						
		                  
		                newR /= delta;  
		                newG /= delta;  
		                newB /= delta;  
		                  
		                newR = Math.min(255, Math.max(0, newR));  
		                newG = Math.min(255, Math.max(0, newG));  
		                newB = Math.min(255, Math.max(0, newB));  
		                  if (k>width/3) {
		                	  pixels[i * width + k] = Color.argb(0, newR, newG, newB);  
						}
		               
		                  
		                newR = 0;  
		                newG = 0;  
		                newB = 0;  
		            }  
		        }  
		          
		        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);  
		        long end = System.currentTimeMillis();  
		        Log.d("may", "used time="+(end - start));  
		        return bitmap;  
		    }  
		
}
