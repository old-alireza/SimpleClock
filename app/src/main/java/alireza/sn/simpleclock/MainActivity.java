package alireza.sn.simpleclock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.KeyEventDispatcher;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ServiceConnection{

    Intent serviceIntent;
    Intent stopIntent;

    MyService myService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initIntents();
        initService();
        configure();
    }

    private void initService() {
        startService(stopIntent);
        bindService(serviceIntent, this, BIND_AUTO_CREATE);
    }

    private void initIntents() {
        serviceIntent = new Intent(this, MyService.class);
        stopIntent = new Intent(this , MyService.class);
        stopIntent.setAction("stop_foreground");
        serviceIntent.setAction("stat_foreground");
    }

    private void configure() {
        findViewById(R.id.countdown_btn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentCountdown fragmentCountdown = new FragmentCountdown();
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragmentCountdown).addToBackStack(null).commit();
            }
        });

        findViewById(R.id.stopwatch_btn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentStopwatch fragmentStopwatch = new FragmentStopwatch(myService);
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragmentStopwatch).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MyService.MyBinder myBinder = (MyService.MyBinder) service;
        myService = myBinder.getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        myService = null;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (MyPref.getInstance(this).getStartService())
        startService(serviceIntent);
        else
            unbindService(this);
    }
}