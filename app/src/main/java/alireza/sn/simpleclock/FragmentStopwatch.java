package alireza.sn.simpleclock;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;

public class FragmentStopwatch extends Fragment {

    private static final DecimalFormat decimalFormat = new DecimalFormat("00");

    private Button startBtn;
    private Button stopBtn;
    private Button resetBtn;
    private TextView timerField;

    private MyService myService ;
    public FragmentStopwatch(MyService myService) {
        this.myService = myService;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stopwatch, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        init();
        Log.i("a","init fragment and states");
        myService.getView(startBtn , stopBtn , resetBtn ,timerField);
        Log.i("a","my service buttons update" );
        myService.countdown();
    }

    public void init() {
        int second = MyPref.getInstance(getContext()).getSecond();
        int minute = MyPref.getInstance(getContext()).getMinute();
        int hour = MyPref.getInstance(getContext()).getHour();
        timerField.setText(getContext().getString(R.string.stopwatch_field,
                decimalFormat.format(hour),
                decimalFormat.format(minute),
                decimalFormat.format(second)));

        startBtn.setEnabled(MyPref.getInstance(getContext()).getIsEnableStart());
        startBtn.setAlpha(MyPref.getInstance(getContext()).getAlphaStart());
        stopBtn.setEnabled(MyPref.getInstance(getContext()).getIsEnableStop());
        stopBtn.setAlpha(MyPref.getInstance(getContext()).getAlphaStop());
        resetBtn.setVisibility(MyPref.getInstance(getContext()).getVisibilityReset());

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myService.startPress();
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                myService.stopPress();
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myService.resetPress();
            }
        });
    }

    public  void initView(View view) {
        startBtn = view.findViewById(R.id.stopwatch_start);
        stopBtn = view.findViewById(R.id.stopwatch_stop);
        resetBtn = view.findViewById(R.id.stopwatch_reset);
        timerField = view.findViewById(R.id.timer_field);
    }
}
