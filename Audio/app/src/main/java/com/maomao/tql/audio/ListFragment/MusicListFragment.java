package com.maomao.tql.audio.ListFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DialogTitle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;

import com.maomao.tql.audio.R;
import com.maomao.tql.audio.acitvity.MainActivity;
//import com.maomao.tql.audio.adapter.MusicListAdapter;
import com.maomao.tql.audio.fragment.BaseFragment;
import com.maomao.tql.audio.pojo.Music;
import com.maomao.tql.audio.utils.L;
import com.maomao.tql.audio.utils.MusicUtils;


public class MusicListFragment extends BaseFragment implements View.OnClickListener{

    private ListView mMusicListView;
    private ImageView mMusicIcon;
    private TextView mMusicTitle;
    private TextView mMusicArtist;

    //private MusicListAdapter mMusicListAdapter = new MusicListAdapter();
    private MainActivity mActivity;
    private boolean isPause;

    private static final String TAG = "MusicListFragment";

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        mActivity = (MainActivity) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.music_list, container, false);
        setupViews(layout);
        return layout;
    }

    @Override
    public void onStart(){
        super.onStart();
        L.l(TAG,"onStart");
        allowBindService();
    }

    @Override
    public void onResume(){
        super.onResume();
        isPause = false;
    }

    @Override
    public void onPause(){
        isPause = true;
        super.onPause();
    }

    @Override
    public void onStop(){
        super.onStop();
        L.l(TAG,"onStop");
        allowUnbindService();
    }

    private void setupViews(View layout){
        mMusicListView = (ListView) layout.findViewById(R.id.lv_music_list);
        mMusicIcon = (ImageView) layout.findViewById(R.id.iv_play_icon);
        mMusicTitle = (TextView) layout.findViewById(R.id.tv_play_title);
        mMusicArtist = (TextView) layout.findViewById(R.id.tv_play_artist);

        //mMusicListView.setAdapter(mMusicListAdapter);
        mMusicListView.setOnItemClickListener(mMusicItemClickListener);
        //mMusicListView.setOnItemLongClickListener(mItemLongClickListener);

        mMusicIcon.setOnClickListener(this);
    }

    /*
    private AdapterView.OnItemLongClickListener mItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            final  int pos = position;
            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setTitle("Delete this item");
            builder.setMessage("sure to delete this item ?");
            builder.setPositiveButton("delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Music music = MusicUtils.sMusicList.remove(pos);
                    mMusicListAdapter.notifyDataSetChanged();
                    if (new File(music.getUri()).delete()){
                        scanSDCard();
                    }
                }
            });
            builder.setNegativeButton("cancle", null);
            builder.create().show();
            return true;
        }
    };

    */

    private AdapterView.OnItemClickListener mMusicItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //play(position);
            //----------------------------------------------------------------------------------------------------------------------
        }
    };

    /**
     * 发送广播，通知系统扫描指定的文件
     *
     */
    private void scanSDCard() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 判断SDK版本是不是4.4或者高于4.4
            String[] paths = new String[]{
                    Environment.getExternalStorageDirectory().toString()};
            MediaScannerConnection.scanFile(mActivity, paths, null, null);
        } else {
            Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED);
            intent.setClassName("com.android.providers.media",
                    "com.android.providers.media.MediaScannerReceiver");
            intent.setData(Uri.parse("file://"+ MusicUtils.getMusicDir()));
            mActivity.sendBroadcast(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    public void onPublish(int progress){

    }

    public void onChange(int postion){

    }
}
