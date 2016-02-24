package com.dmk.mediaplayer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class MainActivity extends Activity  implements MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener,MediaPlayer.OnBufferingUpdateListener {
    private ImageView b1,b2,b3,b4;
    private MediaPlayer mediaPlayer;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();;
    private SeekBar seekbar;
    private TextView tx1,tx2,tx3;
    private boolean intialStage = true;
    private boolean playPause;
    private final Handler mHandler = new Handler();
    Utilities utils;
    int position;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i=getIntent();
        position=i.getExtras().getInt("Position");
        utils=new Utilities();
        b1 = (ImageView) findViewById(R.id.next);
        b2 = (ImageView) findViewById(R.id.prev);
        b3=(ImageView)findViewById(R.id.play_pause);
        b4=(ImageView)findViewById(R.id.background_image);
        tx1=(TextView)findViewById(R.id.startText);
        tx2=(TextView)findViewById(R.id.endText);
        mediaPlayer = new MediaPlayer();
        seekbar=(SeekBar)findViewById(R.id.seekBar1);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        seekbar.setOnSeekBarChangeListener(this);
        progress=new ProgressDialog(MainActivity.this);
        seekbar.setEnabled(false);
        b2.setEnabled(false);
        if (!playPause) {
            b3.setImageResource(R.drawable.uamp_ic_pause_white_24dp);
            if (intialStage)
               /* new Player()
                        .execute(Globals.list.get(position).getSongURL());*/
                playSong(position);
            else {
                if (!mediaPlayer.isPlaying())
                    mediaPlayer.start();
            }
            playPause = true;
        }

//        new Player().execute(Globals.list.get(position).getSongURL());
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!playPause) {
                    b3.setImageResource(R.drawable.uamp_ic_pause_white_24dp);
                    if (intialStage)
                      /* new Player()
                               .execute(Globals.list.get(position).getSongURL());*/
                        playSong(position);
                    else {
                        if (!mediaPlayer.isPlaying())
                            mediaPlayer.start();
                    }
                    playPause = true;
                } else {
                    b3.setImageResource(R.drawable.play_48dp);
                    if (mediaPlayer.isPlaying())
                        mediaPlayer.pause();
                    playPause = false;
                }

            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaPlayer.stop();
                if(position < (Globals.list.size() - 1)){
//                    playSong(position + 1);
//                    new Player().execute(Globals.list.get(position + 1).getSongURL());

                    position = position + 1;
                    playSong(position);
                }else{
                    // play first song
//                    new Player().execute(Globals.list.get(0).getSongURL());
                    position = 0;
                    playSong(position);
                }

            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                if(position >0){
//                    playSong(position + 1);
//                    new Player().execute(Globals.list.get(position - 1).getSongURL());
                    position = position - 1;
                    playSong(position);
                }else{
                    // play first song
//                    new Player().execute(Globals.list.get(Globals.list.size()-1).getSongURL());
                    position = Globals.list.size()-1;
                    playSong(position);
                }

            }
        });
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            try {
                if (mediaPlayer != null){
                    long totalDuration = mediaPlayer.getDuration();
                long currentDuration = mediaPlayer.getCurrentPosition();

                // Displaying Total Duration time
                tx2.setText("" + utils.milliSecondsToTimer(totalDuration));
                // Displaying time completed playing
                tx1.setText("" + utils.milliSecondsToTimer(currentDuration));

                // Updating progress bar
                int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
                //Log.d("Progress", ""+progress);
                seekbar.setProgress(progress);

                // Running this thread after 100 milliseconds
                mHandler.postDelayed(this, 100);
            }
            }catch (Exception e){

            }

        }
    };

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

    @Override
    public void onCompletion(MediaPlayer mp) {
    seekbar.setEnabled(false);
    b3.setImageResource(R.drawable.play_48dp);
        if(position < (Globals.list.size() - 1)){
//            new Player().execute(Globals.list.get(position + 1).getSongURL());
            position = position + 1;
            playSong(position);
        }else{
            // play first song
//            new Player().execute(Globals.list.get(0).getSongURL());
            position = 0;
            playSong(position);
        }
    }
//        onB/u
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mediaPlayer.getDuration();
        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        mediaPlayer.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }
    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    @Override
    public void onBackPressed() {
        mediaPlayer.stop();
        super.onBackPressed();
    }

    public void  playSong(int songIndex){
        // Play song
        try {
            if(Globals.isConnected(MainActivity.this))
            {
            progress.setMessage("Buffering...");
            progress.show();
            progress.setCanceledOnTouchOutside(false);
            mediaPlayer=new MediaPlayer();
            mediaPlayer.reset();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            Uri uri = Uri.parse(Globals.list.get(songIndex).getSongURL());
            mediaPlayer.setDataSource(MainActivity.this, uri);
//                if(intialStage)

            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                    progress.dismiss();
                    seekbar.setEnabled(true);
                    mediaPlayer.setOnCompletionListener(MainActivity.this);
//                    mediaPlayer.setOnBufferingUpdateListener(MainActivity.this);
                    b3.setImageResource(R.drawable.uamp_ic_pause_white_24dp);
                    finalTime = mediaPlayer.getDuration();
                    startTime = mediaPlayer.getCurrentPosition();

                    tx2.setText(String.format("%d : %d",
                                    TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                                    TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
                    );
                    tx1.setText(String.format("%d : %d ",
                                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
                    );

                    seekbar.setProgress((int) startTime);
                    myHandler.postDelayed(mUpdateTimeTask, 100);
                    b2.setEnabled(true);
                    b3.setEnabled(true);
                    intialStage = false;
                    seekbar.setEnabled(true);
                }
            });
            }else{

                Toast.makeText(MainActivity.this, "No Internet Connection Available", Toast.LENGTH_LONG).show();

                tx2.setText(String.format("%d : %d", "0", "00"));
                tx1.setText(String.format("%d : %d", "0", "00"));
            }
            // Updating progress bar
            updateProgressBar();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    @Override
    protected void onPause() {
        super.onPause();
        b3.setImageResource(R.drawable.play_48dp);
        if (mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        b3.setImageResource(R.drawable.uamp_ic_pause_white_24dp);
        if (!mediaPlayer.isPlaying())
            mediaPlayer.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        b3.setImageResource(R.drawable.play_48dp);
        if (mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekbar.setSecondaryProgress(percent);

        if(percent==100)
        {
            progress.dismiss();

        }else if(percent > seekbar.getProgress())
        {
            progress.dismiss();
        }else {
            progress.setMessage("Buffering...");
            progress.show();
            progress.setCanceledOnTouchOutside(false);
        }
    }
}