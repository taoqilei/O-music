package com.maomao.tql.audio.fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.content.Context;


import com.maomao.tql.audio.application.App;
import com.maomao.tql.audio.service.PlayService;
import com.maomao.tql.audio.utils.L;

public abstract class BaseFragment extends Fragment {
    private Activity activity;
    protected PlayService mPlayService;
    //protected DownloadService mDownloadService;

    private static final String TAG = "BaseFragment";

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        activity = getActivity();
    }

    public Context getContext(){
        if (activity == null){
            return App.getInstance();
        }
        return activity;
    }
    private ServiceConnection mPlayServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            L.l(TAG,"Play --- onServiceConnected");
            mPlayService = ((PlayService.PlayBinder) service).getService();
            mPlayService.setOnMusicEventListener(mMusicEventListener);
            //onChange(mPlayService.getPlayingPosition());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            L.l(TAG,"Play --- onServiceConnected");
            mPlayService = null;
        }
    };

    // 音乐播放服务回调接口的实现
    private PlayService.OnMusicEventListener mMusicEventListener = new PlayService.OnMusicEventListener() {
        @Override
        public void onPublish(int percent) {
            BaseFragment.this.onPublish(percent);
        }

        @Override
        public void onChange(int position) {
            //BaseFragment.this.onChange(position);
        }
    };

    //allowBindService()使用绑定的方式启动歌曲播放的服务
    //allowUnBindService()方法解除绑定
	 //在SplashActivity.java中使用startService()方法启动过该音乐播放服务了
	 //那么大家需要注意的事，该服务不会因为调用allowUnbindService()方法解除绑定
	 //而停止。
    public void allowBindService() {
        getContext().bindService(new Intent(getContext(),PlayService.class),mPlayServiceConnection,Context.BIND_AUTO_CREATE);
    }

    public void allowUnbindService(){
        getContext().unbindService(mPlayServiceConnection);
    }


    //更新进度
    //抽象方法由子类实现
    //实现service与主界面通信
    public abstract  void onPublish(int progress);

    //切换歌曲
    // 抽象方法由子类实现
    //实现service与主界面通信
    public abstract void onChange(int postion);
}
