package com.tanaysaxena.recyclerdatafetch;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        String[] permission={android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.INTERNET};
        //To check if all permission are granted
        if(!hasPermission(this,permission)){
            //To pop the get permissions  dialog
            ActivityCompat.requestPermissions(this,permission,121);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                finish();
            }
        },1000);
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 121:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(SplashActivity.this,MainActivity.class));
                            finish();
                        }
                    },1000);
                }
                else{
                    Toast.makeText(SplashActivity.this,"Please Grant Permission",Toast.LENGTH_SHORT).show();
                    finish();
                }
        }

    }
    public boolean hasPermission(Context context, String[] permission){
        boolean hasall=true;
        for(int i=0;i<permission.length;i++){
            int percons=context.checkCallingOrSelfPermission(permission[i]);
            if(percons!= PackageManager.PERMISSION_GRANTED){
                hasall=false;
            }
        }
        return hasall;
    }
}
