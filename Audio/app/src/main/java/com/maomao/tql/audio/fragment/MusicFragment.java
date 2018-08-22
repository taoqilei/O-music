package com.maomao.tql.audio.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import android.view.Window;
import android.widget.Toast;

import com.maomao.tql.audio.R;
import com.maomao.tql.audio.UI.CDView;
import com.maomao.tql.audio.UI.LrcView;
import com.maomao.tql.audio.application.App;
import com.maomao.tql.audio.pojo.Music;
import com.maomao.tql.audio.utils.ImageTools;
import com.maomao.tql.audio.utils.MusicIconLoader;
import com.maomao.tql.audio.utils.MusicUtils;
import com.maomao.tql.audio.utils.PlayPageTransformer;


public class MusicFragment extends BaseFragment implements View.OnClickListener{

    private TextView mMusicTitle; //music title
    private ViewPager mViewPager;  //cd or lrc
    private CDView mCdView;        //cd
    private SeekBar mPlaySeekBar; //seekbar
    private ImageButton mStartPlayButton; //start or pause button
    private TextView mSingerTextView; //singer
    private LrcView mLrcViewOnFirstPage; //single lines lrc
    private LrcView mLrcViewOnSecondpage; // 7 lines lrc

    private ArrayList<View> mViewPagerContent = new ArrayList<View>(2);

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.music_play, container, false);
        setupViews(inflater, view,container);
        return view;
    }

    private void setupViews(LayoutInflater inflater,View view, ViewGroup container){
        mMusicTitle = (TextView) view.findViewById(R.id.tv_music_title);
        mViewPager = (ViewPager)view.findViewById(R.id.vp_play_container);
        mStartPlayButton = (ImageButton) view.findViewById(R.id.ib_play_start);
        mPlaySeekBar = (SeekBar) view.findViewById(R.id.sb_play_progress);

        //动态设置seekbar的margin
        mPlaySeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);

        initViewPagerContent(inflater, container);
        // 设置viewpager的切换动画
        mViewPager.setPageTransformer(true,new PlayPageTransformer());
        mViewPager.setOnPageChangeListener(mPageChangeListener);
        mViewPager.setAdapter(mPagerAdapter);

    }

    @Override
    public void onResume(){
        super.onResume();
        allowBindService();
    }

    @Override
    public void onPause(){
        allowUnbindService();
        super.onPause();
    }

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener(){
        @Override
        public void onPageSelected(int postion) {
            if (postion == 0){
                if (mPlayService.isPlaying()){
                    mCdView.start();
                }
            }else{
                mCdView.pause();
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2){

        }

        @Override
        public void onPageScrollStateChanged(int arg0){

        }
    };

    private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int progress = seekBar.getProgress();
            mPlayService.seek(progress);
            mLrcViewOnFirstPage.onDrag(progress);
            mLrcViewOnSecondpage.onDrag(progress);

        }
    };


    private PagerAdapter mPagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return mViewPagerContent.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        /**
         * 该方法是PagerAdapter的预加载方法，系统调用 当显示第一个界面时，
         * 第二个界面已经预加载，此时调用的就是该方法。
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewPagerContent.get(position));
            return mViewPagerContent.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }
    };


    //初始化viewpager的内容
    private void initViewPagerContent(LayoutInflater inflater, ViewGroup container){
        View cd = inflater.inflate(R.layout.play_page_item_cd,container,false);
        mCdView = (CDView) cd.findViewById(R.id.play_cdview);
        mLrcViewOnFirstPage = (LrcView) cd.findViewById(R.id.play_first_lrc);

        View lrcView = inflater.inflate(R.layout.play_page_item_lrc,container,false);
        mLrcViewOnSecondpage = (LrcView) lrcView.findViewById(R.id.play_first_lrc_2);

        mViewPagerContent.add(cd);
        mViewPagerContent.add(lrcView);
    }

    private void setBackground(int postion){
        //
    }

    //上一曲
    public void pre(View view){
        mPlayService.pre();
    }

    //播放 or 暂停
    public void play(View view){
        if (MusicUtils.sMusicList.isEmpty()){
            Toast.makeText(getContext(),"当前没有MP3文件",Toast.LENGTH_LONG).show();
            return;
        }
        if (mPlayService.isPlaying()){
            mPlayService.pause();
            mCdView.pause();
            mStartPlayButton.setImageResource(R.mipmap.player_btn_play_normal);
        }
    }

    //下一曲
    public void next(View view){
        mPlayService.next();
    }

    //播放时调用，主要设置显示当前播放音乐的信息
    private void onPlay(int position){
        Bitmap bmp = null;
        if (!MusicUtils.sMusicList.isEmpty()){
            Music music = MusicUtils.sMusicList.get(position);

            mMusicTitle.setText(music.getTitle());
            mSingerTextView.setText(music.getArtist());
            mPlaySeekBar.setMax(music.getLength());
            bmp = MusicIconLoader.getsInstance().load(music.getImage());
        }

        if (bmp == null)
            bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        mCdView.setImage(ImageTools.scaleBitmap(bmp,(int)(App.sScreenWidth * 0.8)));

        if (mPlayService.isPlaying()){
            mCdView.start();
            mStartPlayButton.setImageResource(R.mipmap.player_btn_play_normal);
        }else{
            mCdView.pause();
            mStartPlayButton.setImageResource(R.mipmap.player_btn_pause_normal);
        }
    }

    private void setLrc(int postion){
        if (MusicUtils.sMusicList.size() != 0){
            Music music = MusicUtils.sMusicList.get(postion);
            String lrcPath = MusicUtils.getLrcDir() + music.getTitle() + ".lrc";
            mLrcViewOnFirstPage.setLrcPath(lrcPath);
            mLrcViewOnSecondpage.setLrcPath(lrcPath);
        }
    }

    @Override
    public void onPublish(int progress){
        mPlaySeekBar.setProgress(progress);
        if (mLrcViewOnFirstPage.hasLrc())
            mLrcViewOnFirstPage.changeCurrent(progress);

        if (mLrcViewOnSecondpage.hasLrc())
            mLrcViewOnSecondpage.changeCurrent(progress);
    }


    @Override
    public void onChange(int position){
        setBackground(position);
        onPlay(position);
        setLrc(position);
    }



    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
    }

    @Override
    public void onStart(){
        super.onStart();
    }



    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public void onClick(View v) {

    }
}
