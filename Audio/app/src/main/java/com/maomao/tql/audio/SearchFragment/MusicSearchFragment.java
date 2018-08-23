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
    private EditText searchInfo;
    private ImageButton searchBtn;
    private ListView songBySearch;
    private SongSearchListViewAdapter myAdapter;
    private String searchstr=null;
    private GetListBySearchSong getListBySearchSong;
    private List<String> songidlist=new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.song_search, container, false);
        setView(view);

        myAdapter = new SongSearchListViewAdapter(App.sContext, searchSongList, new SongSearchListViewAdapter.Callback() {
            @Override
            public void downloadsongcallback(String songid) {
                //dialog(songid);
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String string=searchInfo.getText().toString();
//                searchstr= URLDecoder.decode(string ,"UTF-8");
                searchstr=searchInfo.getText().toString();
                if (!(searchstr ==null)){
                    recieveList(searchstr);

                }
            }
        });
        return view;
    }

    //设置view
    private void setView(View view){
        searchInfo= (EditText) view.findViewById(R.id.searchText);
        searchBtn= (ImageButton) view.findViewById(R.id.searchbtn);
        songBySearch= (ListView) view.findViewById(R.id.songbysearch);

        songBySearch.setAdapter(myAdapter);
    }

    //获得歌曲列表
    private void recieveList(String searchstr){
//        final List<SongBySearchBean> list=new ArrayList<>();
        getListBySearchSong =new GetListBySearchSong(searchstr, new GetListBySearchSong.Callback() {
            @Override
            public void getList() {
                searchSongList.clear();
                searchSongList.addAll(getListBySearchSong.songbysearchlist);
                Log.e("listmain", String.valueOf(searchSongList.size()));
                int i;
                songidlist.clear();
                for (i=0;i<searchSongList.size();i++){

                    songidlist.add(searchSongList.get(i).getSongid());
                }
                //myApplication.setSongidList(songidlist);
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

        songBySearch.setAdapter(myAdapter);
        songBySearch.setItemsCanFocus(false);
        listener= resetLickListener();
        songBySearch.setOnItemClickListener(listener);
*/
        songBySearch.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
    }
}
