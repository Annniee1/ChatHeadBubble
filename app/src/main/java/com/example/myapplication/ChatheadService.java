package com.example.myapplication;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Bundle;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class ChatheadService extends Service {

    private WindowManager mWindowManager;

    int count=0;
    private View chathead;
    public ChatheadService(){

    }
    Context context;
    Intent intent1;
    String s1;
    private IBinder binder=new Binder();
    @Override
    public IBinder onBind(Intent intent) {
        intent1=intent;
        s1 = intent1.getStringExtra("date");
        return null;
    }

    Timer timer;
    TimerTask timerTask;
    Double time = 0.0;

    String ans;
    boolean timerStarted = false;

    @Override
    public void onCreate() {
        super.onCreate();
        chathead= LayoutInflater.from(this).inflate(R.layout.overlay_layout,null);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity= Gravity.TOP | Gravity.LEFT;
        params.x =0;
        params.y=100;
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(chathead, params);
        ImageView closeButtonCollapsed =  chathead.findViewById(R.id.image1);
        TextView value =  chathead.findViewById(R.id.value);
        TextView texttochange =  chathead.findViewById(R.id.texttochange);
        closeButtonCollapsed.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;
            private int lastAction;
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        initialX = params.x;
                        initialY = params.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        lastAction=event.getAction();
                        return true;
                    }

                    if(event.getAction() ==MotionEvent.ACTION_UP)
                    {
                        //remember the initial position.
                        if(lastAction==MotionEvent.ACTION_DOWN) {
                            closeButtonCollapsed.setBackgroundResource(R.drawable.visibility);
                            Button button = new Button(ChatheadService.this);
                            button.setText("X");
                            RelativeLayout layout = chathead.findViewById(R.id.collapse_view);
                            layout.addView(button);
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    stopSelf();
                                }
                            });
                        }
                            lastAction=event.getAction();
                            return true;
                        }

                        if(event.getAction()==MotionEvent.ACTION_MOVE)
                        {
//                            closeButtonCollapsed.setBackgroundResource(R.drawable.ic_baseline_circle_24);
                            params.x = initialX + (int) (event.getRawX() - initialTouchX);
                            params.y = initialY + (int) (event.getRawY() - initialTouchY);


                            //Update the layout with new X & Y coordinate
                            mWindowManager.updateViewLayout(chathead, params);
                            lastAction=event.getAction();
                            return true;
                        }

                return false;
            }
        });

        timer = new Timer();

        long duration=TimeUnit.MINUTES.toMillis(1);

        new CountDownTimer(duration, 1000){
            public void onTick(long l){

                String sduration=String.format(Locale.ENGLISH,"%02d : %02d",
                        TimeUnit.MILLISECONDS.toMinutes(l),
                        TimeUnit.MILLISECONDS.toSeconds(l),
                        -TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(l)+count));
                count+=1;
                if(count==10)
                {
                    closeButtonCollapsed.setBackgroundResource(R.drawable.angle);
                    texttochange.setText("Correct your orientation!");
                }
                if(count==20)
                {
                    closeButtonCollapsed.setBackgroundResource(R.drawable.blink);
                    texttochange.setText("You need to blink more!");
                }
                if(count==30)
                {
                    closeButtonCollapsed.setBackgroundResource(R.drawable.detox);
                    texttochange.setText("Move the mobile device away!");
                }
                 if(count<10)
                 {
                     value.setText("00 : 0" +count);
                 }
                 else
                 {
                     value.setText("00 : " +count);
                 }

            }
            public  void onFinish(){
                value.setText("FINISH!!");
            }
        }.start();

        closeButtonCollapsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        value.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return false;
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(chathead !=null)
        {

            mWindowManager.removeView(chathead);
        }
    }
    public String getTimestamp(long timestamp) {

        String time = "";

        long difference = System.currentTimeMillis()-timestamp;

        double days = Math.floor(difference / 1000 / 60 / 60 / 24);
        difference -= days * 1000 * 60 * 60 * 24;

        double hours = Math.floor(difference / 1000 / 60 / 60);
        difference -= hours * 1000 * 60 * 60;

        double minutes = Math.floor(difference / 1000 / 60);
        difference -= minutes * 1000 * 60;

        double seconds = Math.floor(difference / 1000);

        if ((int) days > 1) {
            time += String.valueOf((int)days) + " days ";
        }
        else if((int) days>0){
            time += String.valueOf((int)days) + " day ";
        }
        else if ((int) hours > 1) {
            time += String.valueOf((int)hours) + " hrs ";
        }
        else if ((int) hours > 0) {
            time += String.valueOf((int)hours) + " hr ";
        }
        else if ((int) minutes > 1) {
            time += String.valueOf((int)minutes) + " mins ";
        }
        else if ((int) minutes > 0) {
            time += String.valueOf((int)minutes) + " min ";
        }
        else if ((int) seconds >= 0) {
            time += String.valueOf((int)seconds) + " secs ";
        }
        time += "ago";

        return time;
    }
}
