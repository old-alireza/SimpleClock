package alireza.sn.simpleclock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button countdownBtn;
    private Button stopwatchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                FragmentStopwatch fragmentStopwatch = new FragmentStopwatch();
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,fragmentStopwatch).addToBackStack(null).commit();
            }
        });
    }

    private void findViews() {
        countdownBtn = findViewById(R.id.countdown_btn);
        stopwatchBtn = findViewById(R.id.stopwatch_btn);
    }
}