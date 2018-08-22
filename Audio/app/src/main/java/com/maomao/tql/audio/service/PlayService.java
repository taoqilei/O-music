package com.maomao.tql.audio.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.widget.Toast;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.maomao.tql.audio.utils.Constants;
import com.maomao.tql.audio.utils.L;
import com.maomao.tql.audio.utils.MusicUtils;
import com.maomao.tql.audio.utils.SpUtils;

public class PlayService extends Service implements MediaPlayer.OnCompletionListener{
    private static final String TAG = PlayService.class.getSimpleName();
    private MediaPlayer mPlayer;
    private OnMusicEventListener mListener;
    private int mPlayingPosition;
    private WakeLock mWakeLock = null ;//获取设备电源锁，防止锁屏后服务被停止;
    private boolean isShaking;
    // 单线程持
    private ExecutorService mProgressUpdatedListener = Executors.newSingleThreadExecutor();

    public class PlayBinder extends Binder {
        public PlayService getService(){
            return PlayService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent){
        return new PlayBinder();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(){
        super.onCreate();
        //acquireWakeLock();//获取设备电源锁
        MusicUtils.initMusicList();
        mPlayingPosition = (Integer) SpUtils.get(this, Constants.PLAY_POS,0);

        if (MusicUtils.sMusicList.size() <= 0){
            Toast.makeText(getApplicationContext(),"当前设备没有音频文件",Toast.LENGTH_LONG).show();
        }else{
            if (getPlayingPosition() <= 0){
                mPlayingPosition = 0;
            }
            Uri uri = Uri.parse(MusicUtils.sMusicList.get(getPlayingPosition()).getUri());
            mPlayer = MediaPlayer.create(PlayService.this,uri);
            mPlayer.setOnCompletionListener(this);
        }
        //开始更新进度的线程
        mProgressUpdatedListener.execute(mPublishProgressRunnable);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        return Service.START_STICKY;
    }
    /**
     * 更新进度的线程
     */
    private Runnable mPublishProgressRunnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                if (mPlayer != null && mPlayer.isPlaying()
                        && mListener != null) {
                    mListener.onPublish(mPlayer.getCurrentPosition());
                }
                /*
                 * SystemClock.sleep(millis) is a utility function very similar
                 * to Thread.sleep(millis), but it ignores InterruptedException.
                 * Use this function for delays if you do not use
                 * Thread.interrupt(), as it will preserve the interrupted state
                 * of the thread. 这种sleep方式不会被Thread.interrupt()所打断
                 */
                SystemClock.sleep(200);
            }
        }
    };

    /**
     * 设置回调
     *
     * @param l
     */
    public void setOnMusicEventListener(OnMusicEventListener l) {
        mListener = l;
    }

    /**
     * 播放
     *
     * @param position
     *            音乐列表播放的位置
     * @return 当前播放的位置
     */
    public int play(int position) {
        L.l(TAG, "play(int position)方法");
        if(MusicUtils.sMusicList.size()<=0){
            Toast.makeText(getApplicationContext(),
                    "当前手机没有MP3文件", Toast.LENGTH_LONG).show();
            return -1;
        }
        if (position < 0)
            position = 0;
        if (position >= MusicUtils.sMusicList.size())
            position = MusicUtils.sMusicList.size() - 1;

        try {
            mPlayer.reset();
            mPlayer.setDataSource(MusicUtils
                    .sMusicList.get(position).getUri());
            mPlayer.prepare();

            start();
            if (mListener != null)
                mListener.onChange(position);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mPlayingPosition = position;
        SpUtils.put(Constants.PLAY_POS, mPlayingPosition);
        //setRemoteViews();
        return mPlayingPosition;
    }

    /**
     * 继续播放
     *
     * @return 当前播放的位置 默认为0
     */
    public int resume() {
        if(mPlayer==null){
            return -1;
        }
        if (isPlaying())
            return -1;
        mPlayer.start();
        //setRemoteViews();
        return mPlayingPosition;
    }

    /**
     * 暂停播放
     *
     * @return 当前播放的位置
     */
    public int pause() {
        if(MusicUtils.sMusicList.size()==0){
            Toast.makeText(getApplicationContext(),
                    "当前设备没有音频文件", Toast.LENGTH_LONG).show();
            return -1;
        }
        if (!isPlaying())
            return -1;
        mPlayer.pause();
        //setRemoteViews();
        return mPlayingPosition;
    }

    /**
     * 下一曲
     *
     * @return 当前播放的位置
     */
    public int next() {
        if (mPlayingPosition >= MusicUtils.sMusicList.size() - 1) {
            return play(0);
        }
        return play(mPlayingPosition + 1);
    }

    /**
     * 上一曲
     *
     * @return 当前播放的位置
     */
    public int pre() {
        if (mPlayingPosition <= 0) {
            return play(MusicUtils.sMusicList.size() - 1);
        }
        return play(mPlayingPosition - 1);
    }

    /**
     * 是否正在播放
     *
     * @return
     */
    public boolean isPlaying() {
        return null != mPlayer && mPlayer.isPlaying();
    }

    /**
     * 获取正在播放的歌曲在歌曲列表的位置
     *
     * @return
     */
    public int getPlayingPosition() {
        return mPlayingPosition;
    }

    /**
     * 获取当前正在播放音乐的总时长
     *
     * @return
     */
    public int getDuration() {
        if (!isPlaying())
            return 0;
        return mPlayer.getDuration();
    }

    /**
     * 拖放到指定位置进行播放
     *
     * @param msec
     */
    public void seek(int msec) {
        if (!isPlaying())
            return;
        mPlayer.seekTo(msec);
    }

    /**
     * 开始播放
     */
    private void start() {
        mPlayer.start();
    }

    /**
     * 音乐播放完毕 自动下一曲
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        next();
    }

    @Override
    public boolean onUnbind(Intent intent){
        L.l("play service", "unbind");
        //mSensorManager.unregisterListener(mSensorEventListener);
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        if (mListener != null)
            mListener.onChange(mPlayingPosition);
    }

    @Override
    public void onDestroy() {
        L.l(TAG, "PlayService.java的onDestroy()方法调用");
        release();
        //stopForeground(true);
        //mSensorManager.unregisterListener(mSensorEventListener);
        super.onDestroy();
    }

    /**
     * 服务销毁时，释放各种控件
     */
    private void release() {
        if (!mProgressUpdatedListener.isShutdown())
            mProgressUpdatedListener.shutdownNow();
        mProgressUpdatedListener = null;
        //释放设备电源锁
        //releaseWakeLock();
        if (mPlayer != null)
            mPlayer.release();
        mPlayer = null;
    }

    // 申请设备电源锁
    private void acquireWakeLock() {
        L.l(TAG, "正在申请电源锁");
        if (null == mWakeLock) {
            PowerManager pm = (PowerManager) this
                    .getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK
                    | PowerManager.ON_AFTER_RELEASE, "");
            if (null != mWakeLock) {
                mWakeLock.acquire();
                L.l(TAG, "电源锁申请成功");
            }
        }
    }

    // 释放设备电源锁
    private void releaseWakeLock() {
        L.l(TAG, "正在释放电源锁");
        if (null != mWakeLock) {
            mWakeLock.release();
            mWakeLock = null;
            L.l(TAG, "电源锁释放成功");
        }
    }

    /**
     * 音乐播放回调接口
     */
    public interface OnMusicEventListener {
        public void onPublish(int percent);

        public void onChange(int position);
    }
}
