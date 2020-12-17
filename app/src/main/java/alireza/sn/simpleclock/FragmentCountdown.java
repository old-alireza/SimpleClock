package alireza.sn.simpleclock;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentCountdown extends Fragment {
    private TextView tvTimeFinished;

    private NumberPicker minutePicker;
    private NumberPicker secondPicker;

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
        setValueOfNumberPicker();
        figure();
    }

    private void setValueOfNumberPicker() {
        secondPicker.setMinValue(0);
        secondPicker.setMaxValue(60);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(60);
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
                    int minute =minutePicker.getValue();
                    int second = secondPicker.getValue();

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

                minutePicker.setEnabled(false);
                secondPicker.setEnabled(false);
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

            int minute =minutePicker.getValue();
            int second = secondPicker.getValue();

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
               // minutePicker.setText(String.valueOf(minute));
                minutePicker.setValue(minute);
              //  secondPicker.setText(String.valueOf(second));
                secondPicker.setValue(second);
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

        minutePicker.setEnabled(true);
        secondPicker.setEnabled(true);
        //  secondPicker.setText(null);
       // minutePicker.setText(null);

    }

    private void checkCorrectTime() {
        int second = secondPicker.getValue();
        int minute = minutePicker.getValue();

        if (second > 60) {
            second -= 60;
            minute++;
            if (minute == 100) {
                Toast.makeText(getActivity(), "incorrect time !", Toast.LENGTH_SHORT).show();
                return;
            }
          //  minutePicker.setText(String.valueOf(minute));
            minutePicker.setValue(minute);
         //   secondPicker.setText(String.valueOf(second));
            secondPicker.setValue(second);
        }

        finalTimer = second + (minute * 60);
    }

    private boolean labelIsEmpty() {
        if (minutePicker.getValue()==0 && secondPicker.getValue() == 0)
            return true;
        else
            return false;
    }

    private void findViews(View view) {
        tvTimeFinished = view.findViewById(R.id.tv_time_is_up);
        minutePicker = view.findViewById(R.id.minutes_input);
        secondPicker = view.findViewById(R.id.seconds_input);
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
