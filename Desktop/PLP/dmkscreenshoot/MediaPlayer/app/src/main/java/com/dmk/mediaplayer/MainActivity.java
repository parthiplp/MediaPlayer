package com.dmk.mediaplayer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class MainActivity extends Activity  implements MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener{
    private ImageView b1,b2,b3,b4;
    private ImageView iv;
    private MediaPlayer mediaPlayer;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();;
    private int forwardTime = 5000;
    private int backwardTime = 5000;
    private SeekBar seekbar;
    private TextView tx1,tx2,tx3;
    private boolean intialStage = true;
    private boolean playPause;
    LinearLayout controlerlayout;
    private final Handler mHandler = new Handler();
    private ScheduledFuture<?> mScheduleFuture; private final ScheduledExecutorService mExecutorService =
            Executors.newSingleThreadScheduledExecutor();
    public static int oneTimeOnly = 0;
    Utilities utils;
    int position;
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
        controlerlayout=(LinearLayout) findViewById(R.id.controllers);
        mediaPlayer = new MediaPlayer();
        seekbar=(SeekBar)findViewById(R.id.seekBar1);
        seekbar.setOnSeekBarChangeListener(this);
        seekbar.setEnabled(false);
        b2.setEnabled(false);
        loadgrid();
        mediaPlayer.setOnCompletionListener(MainActivity.this);

    }

    private void loadgrid() {


        if (!playPause) {
            b3.setImageResource(R.drawable.uamp_ic_pause_white_24dp);
            if (intialStage)

                playSong(position);
            else {
                if (!mediaPlayer.isPlaying())
                    mediaPlayer.start();
            }
            playPause = true;
        }

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!playPause) {
                    b3.setImageResource(R.drawable.uamp_ic_pause_white_24dp);
                    if (intialStage)

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

                    position = position + 1;
                    playSong(position);
                }else{
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
                    position = position - 1;
                    playSong(position);
                }else{
                    position = Globals.list.size()-1;
                    playSong(position);
                }

            }
        });
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            try {
                long totalDuration = mediaPlayer.getDuration();
                long currentDuration = mediaPlayer.getCurrentPosition();

                // Displaying Total Duration time
                tx2.setText(""+utils.milliSecondsToTimer(totalDuration));
                // Displaying time completed playing
                tx1.setText(""+utils.milliSecondsToTimer(currentDuration));

                // Updating progress bar
                int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
                //Log.d("Progress", ""+progress);
                seekbar.setProgress(progress);

                // Running this thread after 100 milliseconds
                mHandler.postDelayed(this, 100);
            }catch (Exception e){

            }

        }
    };



    @Override
    public void onCompletion(MediaPlayer mp) {
//
        if(position < (Globals.list.size() - 1)){
            position = position + 1;
            playSong(position);
        }else{
            position = 0;
            playSong(position);
        }
    }

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
               controlerlayout.setEnabled(true);
               final ProgressDialog progress=new ProgressDialog(MainActivity.this);
               progress.setMessage("Buffering...");
               progress.show();
               progress.setCanceledOnTouchOutside(false);
               progress.setCancelable(false);
//               progress.set
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

               Toast.makeText(MainActivity.this,"No Internet Connection Available",Toast.LENGTH_LONG).show();

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
        mediaPlayer.release();
    }
}