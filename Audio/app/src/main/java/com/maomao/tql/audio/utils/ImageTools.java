package com.maomao.tql.audio.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.maomao.tql.audio.application.App;

public class ImageTools {
    //缩放图片
    public static Bitmap scaleBitmap(Bitmap bmp){
        return scaleBitmap(bmp, (int)(App.sScreenWidth * 0.13));
    }
    //缩放图片
    public static Bitmap scaleBitmap(Bitmap bmp, int size){
        return Bitmap.createScaledBitmap(bmp, size, size, true);
    }

    //根据文件url缩放图片
    private static Bitmap scaleBitmap(String uri, int size){
        return scaleBitmap(BitmapFactory.decodeFile(uri), size);
    }
    //根据文件uri缩放图片
    private static Bitmap scaleBitmap(String uri){
        return scaleBitmap(BitmapFactory.decodeFile(uri));
    }

    // 缩放资源图片
    public static Bitmap scaleBitmap(int res){
        return scaleBitmap(BitmapFactory.decodeResource(App.sContext.getResources(), res));
    }

    //创建圆形图片
    private static Bitmap createCircleBitmap(Bitmap src){
        int size = (int) (App.sScreenWidth * 0.13);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setARGB(255,241,239,229);

        Bitmap target = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        canvas.drawCircle(size/2, size/2,size/2, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(src,0 , 0, paint);

        return  target;
    }

    private static Bitmap createCircleBitmap(String uri){
        return createCircleBitmap(BitmapFactory.decodeFile(uri));
    }

    private static Bitmap createCircleBitmap(int res){
        return createCircleBitmap(BitmapFactory.decodeResource(App.sContext.getResources(), res));
    }
}
