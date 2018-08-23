package com.maomao.tql.audio.NetFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maomao.tql.audio.Adapter.TabFragmentPagerAdapter;
import com.maomao.tql.audio.R;

import java.util.ArrayList;
import java.util.List;

public class MusicNetFragment extends Fragment implements View.OnClickListener {

    private TextView tv_item_new_music, tv_item_ranking_music;
    private ViewPager myViewPager;
    private List<Fragment> list;
    private TabFragmentPagerAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.music_net, container, false);

        InitView(view);

// 设置菜单栏的点击事件
        tv_item_new_music.setOnClickListener(this);
        tv_item_ranking_music.setOnClickListener(this);
        myViewPager.setOnPageChangeListener(new MyPagerChangeListener());

//把Fragment添加到List集合里面
        list = new ArrayList<>();
        list.add(new NewMusicFragment());
        list.add(new RankingMusicFragment());
        adapter = new TabFragmentPagerAdapter(getFragmentManager(), list);
        myViewPager.setAdapter(adapter);
        myViewPager.setCurrentItem(0);  //初始化显示第一个页面
        tv_item_new_music.setBackgroundColor(Color.RED);//被选中就为红色

        return view;
    }

    /**
     * 初始化控件
     */
    private void InitView(View view) {
        tv_item_new_music = (TextView) view.findViewById(R.id.tv_item_music_new);
        tv_item_ranking_music = (TextView) view.findViewById(R.id.tv_item_music_ranking);

        myViewPager = (ViewPager) view.findViewById(R.id.MusicNetViewPager);
    }


    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_item_music_new:
                myViewPager.setCurrentItem(0);
                tv_item_new_music.setBackgroundColor(Color.RED);
                tv_item_ranking_music.setBackgroundColor(Color.WHITE);
                break;
            case R.id.tv_item_music_ranking:
                myViewPager.setCurrentItem(1);
                tv_item_new_music.setBackgroundColor(Color.WHITE);
                tv_item_ranking_music.setBackgroundColor(Color.RED);
                break;

        }
    }

    /**
     * 设置一个ViewPager的侦听事件，当左右滑动ViewPager时菜单栏被选中状态跟着改变
     *
     */
    public class MyPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            switch (arg0) {
                case 0:
                    tv_item_new_music.setBackgroundColor(Color.RED);
                    tv_item_ranking_music.setBackgroundColor(Color.WHITE);
                    break;
                case 1:
                    tv_item_new_music.setBackgroundColor(Color.WHITE);
                    tv_item_ranking_music.setBackgroundColor(Color.RED);
                    break;

            }
        }
    }
}
