package com.dmk.mediaplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmk.mediaplayer.MainActivity;
import com.dmk.mediaplayer.R;

import java.util.List;

/**
 * Created by BSILIND\parthiban.m on 18/2/16.
 */
public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.ViewHolder> {

    List<PlayListVO> listVOs;
    Context c;
    public PlayListAdapter(List<PlayListVO> listVOs,Context con) {
        this.listVOs = listVOs;
        c=con;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_playlist_adapter,parent,false);
        ViewHolder vh=new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.name.setText(listVOs.get(position).getSongName());
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(c, MainActivity.class);
                intent.putExtra("Position",position);
                c.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listVOs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView name;
        ImageView play;
        View v;
        public ViewHolder(View v) {
            super(v);
            this.v=v;
            name=(TextView) v.findViewById(R.id.songname);

        }
    }
}
