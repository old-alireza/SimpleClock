package alireza.sn.simpleclock;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.DecimalFormat;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class FragmentStopwatch extends Fragment {
    private Button startBtn;
    private Button stopBtn;

    private TextView timerField;

    private CountDownTimer countDownTimer;

    private DecimalFormat decimalFormat = new DecimalFormat("00");

    private float millis = 0;
    private int second = 0;
    private int minute = 0;
    private int hour = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stopwatch, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        init();
    }

    private void init() {
        timerField.setText(getString(R.string.stopwatch_field, "00", "00", "00", "00"));
        countdown();

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPress();
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                stopPress();

            }
        });
    }

    private void stopPress() {
        countDownTimer.cancel();
        activeStop(false);
        activeStart(true);
    }

    private void startPress() {
        countDownTimer.start();
        activeStop(true);
        activeStart(false);
    }

    private void activeStart(boolean b) {
        if (b) {
            startBtn.setEnabled(true);
            startBtn.setAlpha(1);
        } else {
            startBtn.setEnabled(false);
            startBtn.setAlpha(.5f);

        }
    }

    private void activeStop(boolean b) {
        if (b) {
            stopBtn.setEnabled(true);
            stopBtn.setAlpha(1);

        } else {
            stopBtn.setEnabled(false);
            stopBtn.setAlpha(.5f);

        }
    }

    private void countdown() {
        countDownTimer = new CountDownTimer(Integer.MAX_VALUE, 10) {

            @Override
            public void onTick(long millisUntilFinished) {
                millis += 1.8;

                if (millis > 99) {
                    second++;
                    millis = 0;
                } else if (second > 59) {
                    minute++;
                    second = 0;
                } else if (minute > 59) {
                    hour++;
                    minute = 0;
                }
                setTimer();
            }

            @Override
            public void onFinish() {

            }
        };

    }

    private void setTimer() {
        timerField.setText(getString(R.string.stopwatch_field, decimalFormat.format(hour), decimalFormat.format(minute), decimalFormat.format(second), decimalFormat.format(millis)));
    }

    private void findViews(View view) {
        startBtn = view.findViewById(R.id.stopwatch_start);
        stopBtn = view.findViewById(R.id.stopwatch_stop);
        timerField = view.findViewById(R.id.timer_field);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
        countDownTimer.onFinish();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopPress();

    }
}
