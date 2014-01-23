package com.example.demolist;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;

public class AlphaFilter {
	public static final int INSIDE_MODE = 0;  
    public static final int EDGE_MODE = 1;  
    public static final int OUTSIDE_MODE = 2;  
      
    public int findNonOpaque(int x, int y, int width, int height, int[] srcPixels) {  
        if(width < height) {  
            return findNonOpaqueByX(x, y, width, height, srcPixels);  
        } else {  
            return findNonOpaqueByY(x, y, width, height, srcPixels);  
        }  
    }  
      
    /* 
     * 横向检测左右两侧边界 
     */  
    public int findNonOpaqueByX(int x, int y, int width, int height, int[] srcPixels) {  
        int mode = OUTSIDE_MODE;  
        //当前点左右两侧均有边界点出现，则认为当前点在内部或者边框中  
        if(findNonOpaqueByXLeft(x, y, width, height, srcPixels) && findNonOpaqueByXRight(x, y, width, height, srcPixels)) {  
            mode = INSIDE_MODE;  
        }  
        int pos = y*width+x;  
        if(isMatch(pos, srcPixels)) {  
            mode = EDGE_MODE;  
        }  
        return mode;  
    }  
      
    /* 
     * 检测与当前点y坐标相同的左侧各点是否有边界存在 
     */  
    public boolean findNonOpaqueByXLeft(int x, int y, int width, int height, int[] srcPixels) {  
        for(int i=0; i < x; i++) {  
            int pos = y*width + i;  
            if(isMatch(pos, srcPixels)) {  
                return true;  
            }  
        }  
        return false;  
    }  
      
    /* 
     * 检测与当前点y坐标相同的右侧各点是否有边界存在 
     */  
    public boolean findNonOpaqueByXRight(int x, int y, int width, int height, int[] srcPixels) {  
        for(int i= x+1; i < width; i++) {  
            int pos = y*width + i;  
            if(isMatch(pos, srcPixels)) {  
                return true;  
            }  
        }  
        return false;  
    }  
      
    /* 
     * 纵向检测上下两侧的边界 
     */  
    public int findNonOpaqueByY(int x, int y, int width, int height, int[] srcPixels) {  
        int mode = OUTSIDE_MODE;  
        //当前点上下两侧均有边界点出现，则认为当前点在内部或者边框中  
        if(findNonOpaqueByYTop(x, y, width, height, srcPixels) && findNonOpaqueByYBottom(x, y, width, height, srcPixels)) {  
            mode = INSIDE_MODE;  
        }  
        int pos = y*width+x;  
        if(isMatch(pos, srcPixels)) {  
            mode = EDGE_MODE;  
        }  
        return mode;  
    }  
      
    /* 
     * 检测与当前点x坐标相同的上方各点是否有边界存在 
     */  
    public boolean findNonOpaqueByYTop(int x, int y, int width, int height, int[] srcPixels) {  
        for(int i=0; i < y; i++) {  
            int pos = i*width + x;  
            if(isMatch(pos, srcPixels)) {  
                return true;  
            }  
        }  
        return false;  
    }  
      
    /* 
     * 检测与当前点x坐标相同的下方各点是否有边界存在 
     */  
    public boolean findNonOpaqueByYBottom(int x, int y, int width, int height, int[] srcPixels) {  
        for(int i=y+1; i < height; i++) {  
            int pos = i*width + x;  
            if(isMatch(pos, srcPixels)) {  
                return true;  
            }  
        }  
        return false;  
    }  
      
    public boolean isMatch(int pos, int[]srcPixels) {  
        int color = srcPixels[pos];  
        int alpha = Color.alpha(color);  
        //检测是否是边界，针对背景图片选用透明度进行过滤  
        if(alpha >= 5 && alpha < 255) {  
            return true;  
        }  
        return false;  
    }  
      
    /** 
     * 图片效果叠加 
     * @param bmp 要裁剪的图片 
     * @param filter 边框 
     * @return 
     */  
    @SuppressLint("NewApi")
	public Bitmap overlay(Bitmap bmp, Bitmap filter)  
    {  
    	long time=System.currentTimeMillis();
    	Log.i("过滤", "计时"+time);
        int width = bmp.getWidth();  
        int height = bmp.getHeight();  
        Bitmap overlay = filter;  
        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_4444);  
        bitmap.setHasAlpha(true);   
          
        // 对边框图片进行缩放  
        int w = overlay.getWidth();  
        int h = overlay.getHeight();  
        float scaleX = width * 1F / w;  
        float scaleY = height * 1F / h;  
        Matrix matrix = new Matrix();  
        matrix.postScale(scaleX, scaleY);  
          
        Bitmap overlayCopy = Bitmap.createBitmap(overlay, 0, 0, w, h, matrix, true);  
          
        int[] srcPixels = new int[width * height];  
        int[] layPixels = new int[width * height];  
        
//        int[] mxp=new int[srcPixels.length];//模糊处理
        
        bmp.getPixels(srcPixels, 0, width, 0, 0, width, height);  
        overlayCopy.getPixels(layPixels, 0, width, 0, 0, width, height);  
          
        int pos = 0;  
        
    /*******************************************/
        
//        for (int i = 0; i < height; i++)  
//        {  
//            for (int k = 0; k < width; k++)  
//            {  
//                pos = i * width + k;  
//                int mode=2;
////                int mode = findNonOpaque(k, i, width, height, layPixels);  
//                int alpha=Color.alpha(layPixels[pos]) ;
//                if (alpha>5) {
//                	mode=0;
//				}else {
//                	mode=2;
//                }
//               
//                if(mode == INSIDE_MODE) {  
//                    mxp[pos] = srcPixels[pos];                  	
//                    continue;  
//                } else if(mode == EDGE_MODE){  
//                } else{  
//                	mxp[pos]=0;
//                    continue;  
//                }  
//                  
//            }  
//        }  
//        int cclength=mxp.length;
//        int mmmm=0,ttt=0;
//        int[] mxx=new int [3];
//        int[] posxx=new int[3];
//        for (int i = 0; i < cclength; i++) {
////			if (mxp[i]!=0) {
//				if (ttt<3) {
//					mmmm+=mxp[i];
////					mxx[ttt]=mxp[i];
//					posxx[ttt]=i;
//					ttt++;
//				}else {
//					mmmm=mmmm/3;
//					ttt=0;
//					for (int j = 0; j <3; j++) {
//						mxp[posxx[j]]=mmmm;
//					}
//					mmmm=mxp[i];
////					mxx[ttt]=mxp[i];
//					posxx[ttt]=i;
//					ttt++;
//				}
//			
////			}
//			
//		}
        
    /*******************************************/    
        
        for (int i = 0; i < height; i++)  
        {  
            for (int k = 0; k < width; k++)  
            {  
                pos = i * width + k;  
                int mode=2;
//                int mode = findNonOpaque(k, i, width, height, layPixels);  
                int alpha=Color.alpha(layPixels[pos]) ;
                if (alpha>5) {
                	mode=0;
				}else {
                	mode=2;
                }
               
                if(mode == INSIDE_MODE) {  
                    srcPixels[pos] = srcPixels[pos];                  	
                    continue;  
                } else if(mode == EDGE_MODE){  
                    srcPixels[pos] = layPixels[pos];  
//                    srcPixels[pos] = 0;  
                } else{  
                    srcPixels[pos] = 0;  
                    continue;  
                }  
                  
            }  
        }  
        bitmap.setPixels(srcPixels, 0, width, 0, 0, width, height);  
//        bitmap.setPixels(mxp, 0, width, 0, 0, width, height);  
        Log.i("计时", "耗时"+(System.currentTimeMillis()-time));
        return bitmap;  
    }  

}
