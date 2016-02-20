package com.dmk.mediaplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.dmk.mediaplayer.adapter.PlayListAdapter;
import com.dmk.mediaplayer.adapter.PlayListVO;

import java.util.ArrayList;

public class PlayList extends AppCompatActivity {

    RecyclerView play_list;
    PlayListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);
        play_list=(RecyclerView) findViewById(R.id.playlist);
        LinearLayoutManager layoutManager=new LinearLayoutManager(PlayList.this);
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        play_list.setLayoutManager(layoutManager);
        Globals.list=new ArrayList<>();
        PlayListVO vo=new PlayListVO();
        vo.setSongName("Anna Enbathu");
        vo.setSongURL("http://www.yaash.co.in/demo/music/music/Anna%20Enbathu.mp3");
        Globals.list.add(vo);
        vo=new PlayListVO();
        vo.setSongName("Annai Mozhi");
        vo.setSongURL("http://www.yaash.co.in/demo/music/music/Annai%20Mozhi.mp3");
        Globals.list.add(vo);
        vo=new PlayListVO();
        vo.setSongName("Annavin");
        vo.setSongURL("http://www.yaash.co.in/demo/music/music/Annavin.mp3");
        Globals.list.add(vo);
        vo=new PlayListVO();
        vo.setSongName("Aringnar");
        vo.setSongURL("http://www.yaash.co.in/demo/music/music/Aringnar.mp3");
        Globals.list.add(vo);
        vo=new PlayListVO();
        vo.setSongName("Azhaikkindrar Anna");
        vo.setSongURL("http://www.yaash.co.in/demo/music/music/Azhaikkindrar%20Anna.mp3");
        Globals.list.add(vo);
        vo=new PlayListVO();
        vo.setSongName("DMK");
        vo.setSongURL("http://www.yaash.co.in/demo/music/music/DMK.mp3");
        Globals.list.add(vo);
        vo=new PlayListVO();
        vo.setSongName("Kopathai Maranduvidu");
        vo.setSongURL("http://www.yaash.co.in/demo/music/music/Kopathai%20Maranduvidu.mp3");
        Globals.list.add(vo);
        vo=new PlayListVO();
        vo.setSongName("Thennzgathin Thiruvilakkaam");
        vo.setSongURL("http://www.yaash.co.in/demo/music/music/Thennzgathin%20Thiruvilakkaam.mp3");
        Globals.list.add(vo);
        vo=new PlayListVO();
        vo.setSongName("Vazhga Dravida Naadu");
        vo.setSongURL("http://www.yaash.co.in/demo/music/music/Vazhga%20Dravida%20Naadu.mp3");
        Globals.list.add(vo);

        adapter=new PlayListAdapter(Globals.list,PlayList.this);
        play_list.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        return;
    }
}
