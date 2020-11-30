package alireza.sn.simpleclock;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.text.DecimalFormat;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class FragmentStopwatch extends Fragment {
    public MyService myService;

    private Button startBtn;
    private Button stopBtn;

    private TextView timerField;

    public FragmentStopwatch(MyService myService) {
      //  Log.e("TAG", String.valueOf(myService==null ? true : false));
        this.myService = myService;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stopwatch, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        myService.initViews(timerField,startBtn , stopBtn);
        init();
    }

    private void findViews(View view) {
        startBtn = view.findViewById(R.id.stopwatch_start);
        stopBtn = view.findViewById(R.id.stopwatch_stop);
        timerField = view.findViewById(R.id.timer_field);
    }


    private void init() {
        timerField.setText(getString(R.string.stopwatch_field, "00", "00", "00", "00"));
      //  countdown();

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myService.startPress();
                //startPress();
                Log.e("TAG","press start button");
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent("ok").putExtra("show",true));
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                myService.stopPress();
               // stopPress();
                Log.e("TAG","press stop button");
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent("ok").putExtra("show",false));
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        countDownTimer.cancel();
//        countDownTimer.onFinish();
    }

    @Override
    public void onPause() {
        super.onPause();
        //stopPress();

    }
}
