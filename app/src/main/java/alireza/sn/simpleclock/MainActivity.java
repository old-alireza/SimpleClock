package alireza.sn.simpleclock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements ServiceConnection {
    private Button countdownBtn;
    private Button stopwatchBtn;

    private boolean showNotification;

    private MyService myService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this,MyService.class);
        bindService(intent ,this,BIND_AUTO_CREATE);

        findViews();
        configure();
    }

    private void configure() {

        countdownBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentCountdown fragmentCountdown = new FragmentCountdown();
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,fragmentCountdown).addToBackStack(null).commit();
            }
        });

        stopwatchBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentStopwatch fragmentStopwatch = new FragmentStopwatch(myService);
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,fragmentStopwatch).addToBackStack(null).commit();
            }
        });
    }

    private void findViews() {
        countdownBtn = findViewById(R.id.countdown_btn);
        stopwatchBtn = findViewById(R.id.stopwatch_btn);
    }

     BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showNotification = intent.getBooleanExtra("show",false);
            Log.e("TAG","get boolean"+showNotification);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,new IntentFilter("ok"));
        stopService(new Intent(this,MyService.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        if (showNotification){
            startService(new Intent(this,MyService.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (showNotification){
            startService(new Intent(this,MyService.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
}