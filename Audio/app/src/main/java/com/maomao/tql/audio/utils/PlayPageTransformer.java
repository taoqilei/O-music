package com.maomao.tql.audio.utils;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.maomao.tql.audio.application.App;

public class PlayPageTransformer implements ViewPager.PageTransformer {
    @Override
    public void transformPage(View view, float position){
        if(position < -1) { // [-Infinity,-1) 左边看不见了
            view.setAlpha(0.0f);
        }else if(position <= 0) { // [-1,0]左边向中间 或 中间向左边
            view.setAlpha(1 + position);
            view.setTranslationX(App.sScreenWidth * (-position));
        }else if(position <= 1) { // (0,1] 右边向中间 或 中间向右边
            view.setAlpha(1 + position);
//			view.setTranslationX(mScreenWidth * -position);
        }else if(position > 1) { // (1,+Infinity] 右边看不见了
            view.setAlpha(1.0f);
        }
    }
}
