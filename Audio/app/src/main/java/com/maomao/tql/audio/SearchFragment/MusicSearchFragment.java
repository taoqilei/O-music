package com.maomao.tql.audio.SearchFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import com.maomao.tql.audio.Adapter.SongSearchListViewAdapter;
import com.maomao.tql.audio.Bean.SongBySearchBean;
import com.maomao.tql.audio.InterAPItool.GetListBySearchSong;
import com.maomao.tql.audio.R;
import com.maomao.tql.audio.application.App;
import java.util.ArrayList;
import java.util.List;

public class MusicSearchFragment extends Fragment {

    private List<SongBySearchBean> searchSongList=new ArrayList<>();
    private EditText mETsearchInfo;
    private ImageButton mIBsearchBtn;
    private ListView mLVsongBySearch;
    private SongSearchListViewAdapter songSearchListViewAdapter;
    private String mStrSearch=null;
    private GetListBySearchSong getListBySearchSong;
    private List<String> mListSongid=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.song_search, container, false);
        setView(view);

        songSearchListViewAdapter = new SongSearchListViewAdapter(App.sContext, searchSongList, new SongSearchListViewAdapter.Callback() {
            @Override
            public void downloadsongcallback(String songid) {
                //dialog(songid);
            }
        });
        mIBsearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String string=mETsearchInfo.getText().toString();
//                mStrSearch= URLDecoder.decode(string ,"UTF-8");
                mStrSearch=mETsearchInfo.getText().toString();
                if (!(mStrSearch ==null)){
                    recieveList(mStrSearch);

                }
            }
        });
        return view;
    }

    //设置view
    private void setView(View view){
        mETsearchInfo= (EditText) view.findViewById(R.id.searchText);
        mIBsearchBtn= (ImageButton) view.findViewById(R.id.searchbtn);
        mLVsongBySearch= (ListView) view.findViewById(R.id.songbysearch);

        mLVsongBySearch.setAdapter(songSearchListViewAdapter);
    }

    //获得歌曲列表
    private void recieveList(String strSearch){
//        final List<SongBySearchBean> list=new ArrayList<>();
        getListBySearchSong =new GetListBySearchSong(strSearch, new GetListBySearchSong.Callback() {
            @Override
            public void getList() {
                searchSongList.clear();
                searchSongList.addAll(getListBySearchSong.songbysearchlist);
                Log.e("listmain", String.valueOf(searchSongList.size()));
                int i;
                mListSongid.clear();
                for (i=0;i<searchSongList.size();i++){

                    mListSongid.add(searchSongList.get(i).getSongid());
                }
                //myApplication.setSongidList(mListSongid);
                refreshListView();
            }

            @Override
            public void listnull() {
                Toast toast = Toast.makeText(App.sContext, "未搜索到歌曲",Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        getListBySearchSong.setDate();
    }

    //刷新ListView
    private void refreshListView() {
  /*      musicPlayerService=myApplication.musicPlayerService;
        if (musicPlayerService!=null){
            musicPlayerService.pesition=-1;
        }

        mLVsongBySearch.setAdapter(songSearchListViewAdapter);
        mLVsongBySearch.setItemsCanFocus(false);
        listener= resetLickListener();
        mLVsongBySearch.setOnItemClickListener(listener);
*/
        mLVsongBySearch.setAdapter(songSearchListViewAdapter);
        songSearchListViewAdapter.notifyDataSetChanged();
    }
}
