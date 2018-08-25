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
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
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
import java.util.ArrayList;
import java.util.List;

import com.maomao.tql.audio.R;
import com.maomao.tql.audio.acitvity.MainActivity;
//import com.maomao.tql.audio.adapter.MusicListAdapter;
import com.maomao.tql.audio.fragment.BaseFragment;
import com.maomao.tql.audio.pojo.Music;
import com.maomao.tql.audio.utils.L;
import com.maomao.tql.audio.utils.MusicUtils;


public class MusicListFragment extends BaseFragment implements View.OnClickListener{

    private ViewPager viewPager;
    private int page = 0;
    private String[] title;

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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager.setCurrentItem(page);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(
                R.layout.fragment_tab, container, false);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
            viewPager.setOffscreenPageLimit(3);
        }

        final TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        //tabLayout.setTabTextColors(R.color.text_color, ThemeUtils.getThemeColorStateList(mContext, R.color.theme_color_primary).getDefaultColor());
        //tabLayout.setSelectedTabIndicatorColor(ThemeUtils.getThemeColorStateList(mContext, R.color.theme_color_primary).getDefaultColor());

        return rootView;
    }


    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new SongListFragment(), title[0]);
        adapter.addFragment(new ArtistListFragment(), title[1]);
        adapter.addFragment(new AlbumListFragment(), title[2]);
        adapter.addFragment(new FolderListFragment(), title[3]);
        viewPager.setAdapter(adapter);
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

    static class Adapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            if(mFragments.size() > position)
                return mFragments.get(position);

            return null;
        }
        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
            // don't super !
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
