package com.example.muze;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlaySong extends AppCompatActivity {
    @Override
    protected void onDestroy() {    //as soon as we go back song should stop playing
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateSeek.interrupt(); //interrupting the updated seek

    }

    TextView textView;
    ImageView play,previous,next;
    ArrayList<File> songs;  //to intent all the songs coming from the MediaCollectionActivity
    MediaPlayer mediaPlayer;
    String textContent;
    int position;
    SeekBar seekBar;
    Thread updateSeek; //using java thread using the seek bar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);


        textView = findViewById(R.id.textView);
        play = findViewById(R.id.play);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        seekBar = findViewById(R.id.seekBar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList) bundle.getParcelableArrayList("songList");
        textContent = intent.getStringExtra("currentSong");
        textView.setText(textContent);
        textView.setSelected(true);
        position = intent.getIntExtra("position", 0);
        Uri uri = Uri.parse(songs.get(position).toString()); //bringing actual location of the song by parse/ the song we wanna play
        mediaPlayer = MediaPlayer.create(this, uri);
        mediaPlayer.start();    //starting to play the song
        seekBar.setMax(mediaPlayer.getDuration()); //setting max of the seekBar to the duration of mediaPlayer

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());  //controlling seekBar by touching and doing forward or backward
            }
        });

        updateSeek = new Thread() {
            @Override
            public void run() { //exception handling
                int currentPosition = 0;
                try {
                    while(currentPosition<mediaPlayer.getDuration()){ //if seekBar position is less than media player's duration
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition); //setting seekBar progress to currentPosition
                        sleep(800); //sleeping for 800ms bcz we don't want to use much of the resources from the user
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        updateSeek.start();

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    play.setImageResource(R.drawable.play);//setting play button initially
                    mediaPlayer.pause(); //pausing the mediaPlayer
                }
                else{
                    play.setImageResource(R.drawable.pause);//setting drawable to pause
                    mediaPlayer.start();
                }
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop(); //stopping media player
                mediaPlayer.release(); //releasing media player
                if(position!=0){
                    position = position -1; //going to previous song
                }
                else{
                    position = songs.size() -1; //when position is zero then play the song listed at the last
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                play.setImageResource(R.drawable.pause); //setting to pause
                seekBar.setMax(mediaPlayer.getDuration());
                textContent = songs.get(position).getName().toString();
                textView.setText(textContent);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if (position != songs.size() - 1) { //if it's not the last song
                    position = position + 1; //play the next song listed
                } else {
                    position = 0; //if last song play the first song
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                play.setImageResource(R.drawable.pause);//setting to pause
                seekBar.setMax(mediaPlayer.getDuration());
                textContent = songs.get(position).getName().toString();
                textView.setText(textContent);  //updating the text view according to the song
            }


        });


    }
}