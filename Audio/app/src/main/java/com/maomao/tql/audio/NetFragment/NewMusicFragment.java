package com.maomao.tql.audio.NetFragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.maomao.tql.audio.Bean.SongRankingBean;
import com.maomao.tql.audio.InterAPItool.Injection;
import com.maomao.tql.audio.R;
import com.maomao.tql.audio.application.App;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewMusicFragment extends Fragment {

    private int rankingnum;
    private SongRankingBean songRankingBean;
    private ImageView rankingimage;
    private TextView rankingname;
    private ListView rankingsonglist;
    private List<SongRankingBean.SongListBean> rankingsongbeanlist=new ArrayList<>();
    //private App myApplication;
    public MyAdapter myAdapter;
    private MyHandler myHandler;
    private String rankingimageUrl;
    private String rankingnamestr;
    private List<String> songidlist=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.song_ranking_list, null);

        //myApplication= (App) getApplication();

        //Intent intent=getIntent();
        //rankingnum=intent.getIntExtra("RankingType",1);
        rankingimage = (ImageView) view.findViewById(R.id.rankingimage);
        rankingname = (TextView) view.findViewById(R.id.rankingname);
        rankingsonglist = (ListView) view.findViewById(R.id.Ranking_song_list);

        myAdapter = new MyAdapter(App.sContext);
        rankingsonglist.setAdapter(myAdapter);

        myHandler = new MyHandler(App.sContext);
        songRankingBean=new SongRankingBean();
        net();

        //加载网络信息
        getRankingInfo(1);

        return view;
    }

    private void net(){
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
    }

    //得到网络list资源
    private void getRankingSongInfo() {
        Log.e("getRank", songRankingBean.getBillboard().getPic_s210());
        rankingimageUrl = songRankingBean.getBillboard().getPic_s210();
        rankingnamestr = songRankingBean.getBillboard().getName();
        int i;
        for (i = 0; i < songRankingBean.getSong_list().size() && i < 50; i++) {
            rankingsongbeanlist.add(songRankingBean.getSong_list().get(i));
        }
        int j;
        songidlist.clear();
        for (j=0;j<rankingsongbeanlist.size();j++){

            songidlist.add(rankingsongbeanlist.get(j).getSong_id());
        }
        //myApplication.setSongidList(songidlist);
    }

    //刷新ListView
    private void refreshListView() {
        myAdapter.notifyDataSetChanged();
        //listener = resetLickListener();
        //rankingsonglist.setOnItemClickListener(listener);
        //mypesition=-1;
    }

    public static String getUserAgent(Context context){
        WebView webView = new WebView(context);
        webView.layout(0, 0, 0, 0);
        WebSettings settings = webView.getSettings();
        String userAgent = settings.getUserAgentString();
        Log.d("User-Agent","User-Agent: "+ userAgent);
        return  userAgent;
    }

    private void getRankingInfo(int num){

        String ua = getUserAgent(App.sContext);

        Call<SongRankingBean> call = Injection.provideSongAPI()
                .getSongRanking("json", "", "webapp_music", "baidu.ting.billboard.billList", num, 50, 0);
        call.enqueue(new Callback<SongRankingBean>() {
            @Override
            public void onResponse(Call<SongRankingBean> call, Response<SongRankingBean> response) {

                SongRankingBean bean = response.body();
//                Log.e("bean", bean.getBillboard().getPic_s210());
                setRankingsongbeanlist(bean);
                Message message =new Message();
                message.what=SET_RANKINGBEAN;
                myHandler.sendMessage(message);

            }

            @Override
            public void onFailure(Call<SongRankingBean> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setRankingsongbeanlist(SongRankingBean bean) {
        songRankingBean = bean;
//        Log.e("songrankingBean", songRankingBean.getBillboard().getPic_s210());
    }


    private static final int SET_RANKINGBEAN = 123;

    public class MyHandler extends Handler {

        private Context context;

        public MyHandler(Context context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case SET_RANKINGBEAN:
                    getRankingSongInfo();
                    Picasso.with(context).load(songRankingBean.getBillboard().getPic_s210()).into(rankingimage);
//                    rankingimage.setImageBitmap(imageUtils.getImg(songRankingBean.getBillboard().getPic_s210()));
                    rankingname.setText(songRankingBean.getBillboard().getName()+" Top 50");
                    refreshListView();
                    break;
            }

        }
    }

    //LIST适配
    private class MyAdapter extends BaseAdapter {
        LayoutInflater inflater;

        private Context context;
        public MyAdapter(Context c) {
            this.inflater = LayoutInflater.from(c);
            context = c;
        }

        public int getCount() {
            return rankingsongbeanlist.size();
        }

        @Override
        public Object getItem(int position) {

            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.song_ranking_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.song_Image = (ImageView) convertView
                        .findViewById(R.id.song_image);
                viewHolder.song_name = (TextView) convertView
                        .findViewById(R.id.ranking_song_name);
                viewHolder.siner_name = (TextView) convertView
                        .findViewById(R.id.ranking_singer_name);
                viewHolder.download= (ImageButton) convertView
                        .findViewById(R.id.downloadbty);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
//            Log.e("urlpath",rankingsongbeanlist.get(position).getPic_small());
            Picasso.with(context).load(rankingsongbeanlist.get(position).getPic_small()).into(viewHolder.song_Image);
//            Log.e("urlpath",rankingsongbeanlist.get(position).getPic_small());
//            viewHolder.song_Image.setImageBitmap(imageUtils.getImg(rankingsongbeanlist.get(position).getPic_small()));
            viewHolder.song_name.setText(rankingsongbeanlist.get(position).getTitle());
            viewHolder.siner_name.setText(rankingsongbeanlist.get(position).getArtist_name());

            viewHolder.download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //dialog(position);
                }
            });

            return convertView;
        }

        class ViewHolder {
            ImageView song_Image;
            TextView song_name;
            TextView siner_name;
            ImageButton download;

        }
    }
}
