package com.maomao.tql.audio.InterAPItool;

import com.maomao.tql.audio.Bean.SearchBean;
import com.maomao.tql.audio.Bean.SongBySearchBean;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GetListBySearchSong {

    public List<SongBySearchBean> songbysearchlist =new ArrayList<>();
    private String search;
    public SearchBean searchBean;
    public SongBySearchBean songBySearchBean;
    private FastJsonTools fastJsonTools;
    public Callback callback;


    public GetListBySearchSong(String searchstr, Callback call){
        this.search=searchstr;
        this.callback=call;
    }

    public interface Callback {
        public void getList();
        public void listnull();
    }

    public void setDate (){
        fastJsonTools=new FastJsonTools();
        try {
            search(search);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private static ExecutorService service= Executors.newFixedThreadPool(5);
    private void search(String searchstr) throws UnsupportedEncodingException {
//        Log.e("str",searchstr);
        String path="http://tingapi.ting.baidu.com/v1/restserver/ting?format=json&calback=&from=webapp_music&method=baidu.ting.search.catalogSug&query="+searchstr;
//        String path=new String(string.getBytes("UTF-8"),"iso-8859-1");
        NUtils.get(path , new NUtils.AbsCallback() {
            @Override
            public void response(String url, byte[] bytes) {
                super.response(url, bytes);
                String json = new String(bytes);
//                Log.e("json",json.toString());
                String Json=json.trim();
                SearchBean bean= fastJsonTools.createJsonBean(Json,SearchBean.class);
//                Log.e("bean",bean.toString());
                setSearchBean(bean);
            }
        });
    }

    private void setSearchBean (SearchBean bean){
        searchBean=new SearchBean();
        searchBean= bean;
//        Log.e("search",searchBean.toString());
        if (searchBean.getSong()!=null){
            getsong();
//            Log.e("searchbean", String.valueOf(searchBean.getSong().size()));
            callback.getList();
        }else{

            callback.listnull();
        }
    }

    public void getsong(){
        int i;
        for (i=0;i<searchBean.getSong().size();i++){
            songBySearchBean=new SongBySearchBean();
            songBySearchBean.setSongname(searchBean.getSong().get(i).getSongname());
            songBySearchBean.setSingername(searchBean.getSong().get(i).getArtistname());
            songBySearchBean.setSongid(searchBean.getSong().get(i).getSongid());
            songbysearchlist.add(songBySearchBean);
        }
    }
}
