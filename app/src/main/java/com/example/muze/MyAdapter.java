package com.example.muze;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

// FirebaseRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View

public class MyAdapter extends FirebaseRecyclerAdapter<SongData, MyAdapter.MyViewHolder> {

    public MyAdapter(@NonNull FirebaseRecyclerOptions<SongData> options){
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull MyAdapter.MyViewHolder holder, int position, @NonNull @NotNull SongData model) {
        holder.songname.setText(model.getSongname());
        holder.songartist.setText(model.getSongartist());
        Glide.with(holder.img.getContext()).load(model.getImageurl()).into(holder.img);
    }


    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_music_row,parent,false);
        return new MyViewHolder(view);
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView songname, songartist;
        ImageView img;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            songname = (TextView) itemView.findViewById(R.id.songname_tv);
            songartist = (TextView) itemView.findViewById(R.id.songartist_tv);
            img = (ImageView) itemView.findViewById(R.id.imageurl_iv);
        }
    }
}
