package com.example.bindservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.example.provideservice.IMyAidlInterface;

/*
 * 1. unbind会不会触发 onServiceDisconnected
 * 2. 杀死Service所在进程会不会回调 onServiceDisconnected
 * 3. unbind之后，使用服务的进程中，BinderProxy对象是否还是alive的？
 * 4. unbind只是减少bind计数？和binder的引用计数有关系么？
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "BearMainActivity";

    private View mBindBtn;

    IMyAidlInterface mService;
    private Intent mIntent = new Intent();
    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected: ");
            mService = IMyAidlInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected: ");

        }
    };
    private View mIsActiveBtn;
    private View mUnbindBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIntent.setPackage("com.example.provideservice");
        mIntent.setAction("com.bear.myservice");

        setContentView(R.layout.activity_main);
        mBindBtn = findViewById(R.id.bind);
        mBindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: bind");
                boolean bind = bindService(mIntent, mConnection, BIND_AUTO_CREATE);
                Log.i(TAG, "onClick: bindService="+bind);
            }
        });

        mUnbindBtn = findViewById(R.id.unbind);
        mUnbindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: unbind");
                unbindService(mConnection);
            }
        });


        mIsActiveBtn = findViewById(R.id.isActive);
        mIsActiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: mService " + (mService == null ? null : ("NonNull, active " + (mService.asBinder().isBinderAlive()))));
            }
        });


    }
}
