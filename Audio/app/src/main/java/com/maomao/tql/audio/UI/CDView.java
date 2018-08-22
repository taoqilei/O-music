package com.maomao.tql.audio.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import android.os.Handler;
import android.os.Message;
import java.util.logging.LogRecord;

import com.maomao.tql.audio.R;

public class CDView extends View {
    private static final int MSG_RUN = 0X0000100;
    private static final int TIME_UPDATE = 50;
    private Bitmap mCircleBitmap;
    private Bitmap mClipBitmap;  // cd 图片
    private float mRotation = 0.0f;
    private Matrix mMatrix;
    private volatile  boolean isRunning;

    public CDView(Context context, AttributeSet attrs){
        this(context,attrs,0);
    }

    public  CDView(Context context,AttributeSet attrs,int defStyleAttr){
        super(context,attrs,defStyleAttr);
        mCircleBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.cd_center);
        mMatrix = new Matrix();
    }

    @Override
    protected void onDetachedFromWindow(){
        super.onDetachedFromWindow();
        isRunning = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        if (mClipBitmap == null){
            super.onMeasure(widthMeasureSpec,heightMeasureSpec);
            return;
        }
        int width = 0;
        int height = 0;


        /*
        1.UNSPECIFIED（未指定）：父元素不对子元素施加任何束缚，子元素可以得到任意想要的大小；
        2.EXACTLY（完全）：父元素决定子元素的确切大小，子元素将被限定在给定的边界里而忽略它本身的大小；
        3.AT_MOST（最多）：子元素至最多达到指定大小的值。
        */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY){
            width = widthSize;
        }else {
            width = mClipBitmap.getWidth();
            if (widthMode == MeasureSpec.AT_MOST){
                width = Math.min(width,widthSize);
            }
        }

        if (heightMode == MeasureSpec.EXACTLY){
            height = heightSize;
        }else{
            height = mClipBitmap.getHeight();
            if (heightMode == MeasureSpec.AT_MOST){
                height = Math.min(height,heightSize);
            }
        }
        setMeasuredDimension(width,height);
    }

    @Override
    protected  void onDraw(Canvas canvas){
        if (mClipBitmap == null)
            return ;

        canvas.save();
        mMatrix.setRotate(mRotation,getMeasuredWidth()/2,getMeasuredHeight()/2);
        canvas.drawBitmap(mClipBitmap,mMatrix,null);
        canvas.drawBitmap(mCircleBitmap,(getMeasuredWidth() - mCircleBitmap.getWidth())/2,(getMinimumHeight()-mCircleBitmap.getHeight())/2,null);
        canvas.restore();
    }

    // 创建圆形剪切图
    private  Bitmap createCircleBitmap(Bitmap src){
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setARGB(255,241,239,229);
        Bitmap target = Bitmap.createBitmap(getMeasuredWidth(),getMeasuredHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        canvas.drawCircle(getMeasuredWidth()/2,getMeasuredWidth()/2,getMeasuredWidth()/2,paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(src,0,0,paint);

        return target;
    }

    //设置CD 图片
    public void setImage(Bitmap bmp){
        int widthSize = bmp.getWidth();
        int heightSize = bmp.getHeight();

        int widthSpec = MeasureSpec.makeMeasureSpec(widthSize,MeasureSpec.AT_MOST);
        int heightSpec = MeasureSpec.makeMeasureSpec(heightSize,MeasureSpec.AT_MOST);

        measure(widthSpec,heightSpec);

        mClipBitmap = createCircleBitmap(bmp);

        requestLayout();
        invalidate();
    }

    //开始旋转
    public void start(){
        if (isRunning)
            return;
        isRunning = true;
        mHandler.sendEmptyMessageDelayed(MSG_RUN,TIME_UPDATE);
    }

    //暂停旋转
    public void pause()
    {
        if (!isRunning)
            return;
        isRunning = false;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg){
            if (msg.what == MSG_RUN){
                if (isRunning){
                    mRotation += 0.01f;
                    if (mRotation >= 360)
                        mRotation = 0;
                    invalidate();
                    sendEmptyMessageDelayed(MSG_RUN,TIME_UPDATE);
                }
            }
        }
    };
}
