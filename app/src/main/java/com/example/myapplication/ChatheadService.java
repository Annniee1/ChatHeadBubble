package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChatheadService extends Service {

    private WindowManager mWindowManager;

    private View chathead;
    public ChatheadService(){

    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

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
                            closeButtonCollapsed.setBackgroundResource(R.drawable.bg2);
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
                            closeButtonCollapsed.setBackgroundResource(R.drawable.ic_baseline_circle_24);
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
//            ImageView closeButtonCollapsed =  chathead.findViewById(R.id.image1);
//            closeButtonCollapsed.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                    Runtime.getRuntime().exit(0);
//                }
//            });
            mWindowManager.removeView(chathead);
        }
    }
}
