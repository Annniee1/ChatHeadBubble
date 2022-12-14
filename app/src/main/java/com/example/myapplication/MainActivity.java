package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private TextView tv1, tv2, tv3;
    private ImageView img1;

    public static final int Permission_code=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(MainActivity.this)) {
                Intent localIntent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION");
                localIntent.setData(Uri.parse("package:" + getPackageName()));
                localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(localIntent);
            }
        }
//        Button button=findViewById(R.id.preview);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv3);
        tv3 = findViewById(R.id.tv4);

        img1 = findViewById(R.id.img1);

        Spinner spinnerLanguages=findViewById(R.id.spinner_languages);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                spinnerLanguages.setVisibility(View.VISIBLE);
//                button.setVisibility(View.GONE);
//            }
//        });

        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerLanguages.setAdapter(adapter);

    }

    Date currentTime;

    @Override
    protected void onPause() {
        super.onPause();
        showchathead();
    }


    private void showchathead() {
        Intent intent=new Intent(MainActivity.this,ChatheadService.class);
        intent.putExtra("date",currentTime);
        startService(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Permission_code)
        {
            if(resultCode==RESULT_OK)
            {
                showchathead();
            }
        }
    }
}