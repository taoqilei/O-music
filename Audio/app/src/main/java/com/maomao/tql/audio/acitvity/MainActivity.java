package com.maomao.tql.audio.acitvity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.view.View.OnClickListener;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.maomao.tql.audio.R;
import com.maomao.tql.audio.SearchFragment.MusicSearchFragment;
import com.maomao.tql.audio.fragment.MusicFragment;
import com.maomao.tql.audio.ListFragment.MusicListFragment;
import com.maomao.tql.audio.NetFragment.MusicNetFragment;
import com.maomao.tql.audio.fragment.SettingsFragment;

public class MainActivity extends FragmentActivity implements OnClickListener {

    //private LinearLayout mTabMusicPlay,mTabMusicList,mTabSettings,mTabMusicNet,mTabMusicSearch;
    private ImageButton mMusicPlayerImg,mMusicListImg,mSettingsImg,mMusicNetImg,mMusicSearchImg;
    private Fragment mFragMusic,mFragMusicList,mFragSettings,mFragMusicNet,mFragMusicSearch;

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
        mMusicPlayerImg = (ImageButton) findViewById(R.id.id_tab_music_player_img);
        mMusicListImg = (ImageButton) findViewById(R.id.id_tab_music_list_img);
        mMusicNetImg = (ImageButton) findViewById(R.id.id_tab_music_net_img);
        mMusicSearchImg = (ImageButton) findViewById(R.id.id_tab_music_search_img);
        mSettingsImg = (ImageButton) findViewById(R.id.id_tab_settings_img);
    }

    private void initEvents(){
        mMusicPlayerImg.setOnClickListener(this);
        mMusicListImg.setOnClickListener(this);
        mMusicNetImg.setOnClickListener(this);
        mMusicSearchImg.setOnClickListener(this);
        mSettingsImg.setOnClickListener(this);
    }

    private void resetImgs(){
        mMusicPlayerImg.setImageResource(R.mipmap.tab_music_normal);
        mMusicListImg.setImageResource(R.mipmap.tab_musiclist_normal);
        mMusicNetImg.setImageResource(R.mipmap.tab_music_net);
        mMusicSearchImg.setImageResource(R.mipmap.tab_music_search);
        mSettingsImg.setImageResource(R.mipmap.tab_settings_normal);
    }

    @Override
    public void onClick(View v){
        resetImgs();
        switch (v.getId()){
            case R.id.id_tab_music_player_img:
                selectTab(0);
                break;
            case R.id.id_tab_music_list_img:
                selectTab(1);
                break;
            case R.id.id_tab_music_net_img:
                selectTab(2);
                break;
            case R.id.id_tab_music_search_img:
                selectTab(3);
                break;
            case R.id.id_tab_settings_img:
                selectTab(4);
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
                mMusicNetImg.setImageResource(R.mipmap.tab_music_net);
                if (mFragMusicNet == null){
                    mFragMusicNet = new MusicNetFragment();
                    transaction.add(R.id.id_content,mFragMusicNet);
                }else{
                    transaction.show(mFragMusicNet);
                }
                break;

            case 3:
                mMusicSearchImg.setImageResource(R.mipmap.tab_music_search);
                if (mFragMusicSearch == null){
                    mFragMusicSearch = new MusicSearchFragment();
                    transaction.add(R.id.id_content,mFragMusicSearch);
                }else{
                    transaction.show(mFragMusicSearch);
                }
                break;


            case 4:
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
