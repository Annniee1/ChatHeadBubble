package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static final int Permission_code=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         TextView hello=(TextView) findViewById(R.id.hello1);
        hello.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showchathead();
            }
        });
        showchathead();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent localIntent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION");
                localIntent.setData(Uri.parse("package:" + getPackageName()));
                localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(localIntent);
            }
        }
        else {
            showchathead();
        }

    }

    private void showchathead() {
        Toast.makeText(MainActivity.this,"Hello",Toast.LENGTH_LONG).show();
        startService(new Intent(MainActivity.this,ChatheadService.class));
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