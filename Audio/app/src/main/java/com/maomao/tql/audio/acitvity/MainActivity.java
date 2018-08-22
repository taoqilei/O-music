package com.maomao.tql.audio.acitvity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.maomao.tql.audio.R;
import com.maomao.tql.audio.fragment.MusicFragment;
import com.maomao.tql.audio.fragment.MusicListFragment;
import com.maomao.tql.audio.fragment.SettingsFragment;

public class MainActivity extends FragmentActivity implements OnClickListener {

    private LinearLayout mTabMusicPlay,mTabMusicList,mTabSettings;
    private ImageButton mMusicPlayerImg,mMusicListImg,mSettingsImg;
    private Fragment mFragMusic,mFragMusicList,mFragSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initViews();
        initEvents();
        selectTab(0);
    }

    private void initViews(){

        mTabMusicPlay = (LinearLayout)findViewById(R.id.id_tab_music_play);
        mTabMusicList = (LinearLayout)findViewById(R.id.id_tab_music_list);
        mTabSettings = (LinearLayout)findViewById(R.id.id_tab_settings);

        mMusicPlayerImg = (ImageButton) findViewById(R.id.id_tab_music_player_img);
        mMusicListImg = (ImageButton) findViewById(R.id.id_tab_music_list_img);
        mSettingsImg = (ImageButton) findViewById(R.id.id_tab_settings_img);
    }

    private void initEvents(){
        mTabMusicPlay.setOnClickListener(this);
        mTabMusicList.setOnClickListener(this);
        mTabSettings.setOnClickListener(this);
    }

    private void resetImgs(){
        mMusicPlayerImg.setImageResource(R.mipmap.tab_music_normal);
        mMusicListImg.setImageResource(R.mipmap.tab_musiclist_normal);
        mSettingsImg.setImageResource(R.mipmap.tab_settings_normal);
    }

    @Override
    public void onClick(View v){
        resetImgs();
        switch (v.getId()){
            case R.id.id_tab_music_play:
                selectTab(0);
                break;
            case R.id.id_tab_music_list:
                selectTab(1);
                break;
            case R.id.id_tab_settings:
                selectTab(2);
                break;
             default:
                    break;
        }
    }

    private void selectTab(int i){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragments(transaction);
        switch (i){
            case 0:
                mMusicPlayerImg.setImageResource(R.mipmap.tab_music_pressed);
                if(mFragMusic == null){
                    mFragMusic = new MusicFragment();
                    transaction.add(R.id.id_content,mFragMusic);
                }else{
                    transaction.show(mFragMusic);
                }
                break;

            case 1:
                mMusicListImg.setImageResource(R.mipmap.tab_musiclist_pressed);
                if (mFragMusicList == null){
                    mFragMusicList = new MusicListFragment();
                    transaction.add(R.id.id_content,mFragMusicList);
                }else{
                    transaction.show(mFragMusicList);
                }
                break;

            case 2:
                mSettingsImg.setImageResource(R.mipmap.tab_settings_pressed);
                if (mFragSettings == null){
                    mFragSettings = new SettingsFragment();
                    transaction.add(R.id.id_content,mFragSettings);
                }else{
                    transaction.show(mFragSettings);
                }
                break;

             default:
                    break;
        }
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction){
        if(mFragMusic != null){
            transaction.hide(mFragMusic);
        }

        if(mFragMusicList != null){
            transaction.hide(mFragMusicList);
        }

        if(mFragSettings != null){
            transaction.hide(mFragSettings);
        }
    }
}
