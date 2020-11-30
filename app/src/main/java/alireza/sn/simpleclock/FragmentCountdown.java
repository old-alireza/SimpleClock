package alireza.sn.simpleclock;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentCountdown extends Fragment {
    private TextView tvTimeFinished;

    private EditText edMinute;
    private EditText edSecond;

    private Button btnStartAndResume;
    private Button btnPauseAndClear;

    private int finalTimer;

    private CountDownTimer countDownTimer;

    // startMode == true --> the button is start  &  startMode == false --> the button is resume
    private boolean startMode = true;

    // pauseMode == true --> the button is pause % pauseMode == false --> the button is clear
    private boolean pauseMode = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_countdown, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        figure();
    }

    private void figure() {
        btnStartAndResume.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (startMode) {

                    if (labelIsEmpty())
                        return;

                    checkCorrectTime();
                    startCountDown();

                } else {
                    int minute = Integer.parseInt(edMinute.getText().toString());
                    int second = Integer.parseInt(edSecond.getText().toString());
                    finalTimer = second + (minute * 60);
                    countDownTimer.start();
                }
                changeState();
                activePauseBtn();

            }

            private void changeState() {

                btnStartAndResume.setText(getString(R.string.resume_btn));
                btnStartAndResume.setAlpha(.5f);
                btnStartAndResume.setEnabled(false);
                startMode = false;

                edMinute.setEnabled(false);
                edSecond.setEnabled(false);
            }
        });

        btnPauseAndClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (pauseMode)
                    pauseMethod();
                else
                    defaultState();
            }
        });
    }

    private void pauseMethod() {
        countDownTimer.cancel();
        activeResumeBtn();
        btnPauseAndClear.setText(getString(R.string.clear_btn));
        pauseMode = false;
    }

    private void activeResumeBtn() {
        btnStartAndResume.setEnabled(true);
        btnStartAndResume.setAlpha(1f);
    }

    private void activePauseBtn() {
        btnPauseAndClear.setEnabled(true);
        btnPauseAndClear.setAlpha(1f);
        pauseMode = true;
        btnPauseAndClear.setText(getString(R.string.pause_btn));
    }

    private void startCountDown() {
        tvTimeFinished.setVisibility(View.INVISIBLE);

        countDownTimer = new CountDownTimer((finalTimer + 1) * 1000, 1000) {

            int second = Integer.parseInt(edSecond.getText().toString());
            int minute = Integer.parseInt(edMinute.getText().toString());

            @Override
            public void onTick(long millisUntilFinished) {

                if (second == 0 && minute == 0) {
                    onFinish();
                    return;
                }

                second--;
                if (second < 0) {
                    second = 59;
                    minute--;
                }
                edMinute.setText(String.valueOf(minute));
                edSecond.setText(String.valueOf(second));

            }

            @Override
            public void onFinish() {
                tvTimeFinished.setVisibility(View.VISIBLE);
                defaultState();
            }
        };
        countDownTimer.start();
    }

    private void defaultState() {
        btnStartAndResume.setEnabled(true);
        btnStartAndResume.setAlpha(1f);
        btnStartAndResume.setText(getString(R.string.start_btn));
        btnPauseAndClear.setEnabled(false);
        btnPauseAndClear.setAlpha(.5f);
        btnPauseAndClear.setText(getString(R.string.pause_btn));

        startMode = true;
        pauseMode = true;

        edMinute.setEnabled(true);
        edSecond.setEnabled(true);
        edSecond.setText(null);
        edMinute.setText(null);

    }

    private void checkCorrectTime() {
        int second = Integer.parseInt(edSecond.getText().toString());
        int minute = Integer.parseInt(edMinute.getText().toString());

        if (second > 60) {
            second -= 60;
            minute++;
            if (minute == 100) {
                Toast.makeText(getActivity(), "incorrect time !", Toast.LENGTH_SHORT).show();
                return;
            }
            edMinute.setText(String.valueOf(minute));
            edSecond.setText(String.valueOf(second));
        }

        finalTimer = second + (minute * 60);
    }

    private boolean labelIsEmpty() {
        if (edMinute.getText().toString().isEmpty() || edSecond.getText().toString().isEmpty())
            return true;
        else
            return false;
    }

    private void findViews(View view) {
        tvTimeFinished = view.findViewById(R.id.tv_time_is_up);
        edMinute = view.findViewById(R.id.minutes_input);
        edSecond = view.findViewById(R.id.seconds_input);
        btnStartAndResume = view.findViewById(R.id.countdown_start_resume);
        btnPauseAndClear = view.findViewById(R.id.countdown_pause_clear);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (countDownTimer!=null)
        pauseMethod();

    }
}
