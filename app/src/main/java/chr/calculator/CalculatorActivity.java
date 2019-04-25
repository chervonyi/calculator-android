package chr.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class CalculatorActivity extends AppCompatActivity {

    // UI
    private TextView buttonPercent;
    private TextView buttonMultiplication;
    private TextView buttonPlus;
    private TextView buttonMinus;
    private TextView buttonDivision;
    private TextView buttonResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        buttonPercent = findViewById(R.id.button_percent);
        buttonMultiplication = findViewById(R.id.button_multiplication);
        buttonPlus = findViewById(R.id.button_plus);
        buttonMinus = findViewById(R.id.button_subtraction);
        buttonDivision = findViewById(R.id.button_division);
        buttonResult = findViewById(R.id.button_result);


        buttonPlus.post(new Runnable() {
            public void run() {

                /*
                Log.d("CHR_GAMES_TEST", "h: " + buttonPlus.getHeight());
                Log.d("CHR_GAMES_TEST", "w: " + buttonPlus.getWidth());
                Log.d("CHR_GAMES_TEST", "h layout:" + buttonPlus.getLayoutParams().height);
                Log.d("CHR_GAMES_TEST", "w layout:" + buttonPlus.getLayoutParams().width);

                buttonPlus.getLayoutParams().height = 100;
                buttonPlus.requestLayout();

                buttonPlus.getLayoutParams().width = 50;
                buttonPlus.requestLayout();

                Log.d("CHR_GAMES_TEST", "____");


                Log.d("CHR_GAMES_TEST", "h: " + buttonPlus.getHeight());
                Log.d("CHR_GAMES_TEST", "w: " + buttonPlus.getWidth());
                Log.d("CHR_GAMES_TEST", "h layout:" + buttonPlus.getLayoutParams().height);
                Log.d("CHR_GAMES_TEST", "w layout:" + buttonPlus.getLayoutParams().width);
                */



                // Set width as a height (Make circle)
                //Log.d("CHR_GAMES_TEST", "Set height for view from: " + buttonPercent.getLayoutParams().height + " to: " + buttonPercent.getWidth());
                buttonPercent.getLayoutParams().height = buttonResult.getHeight();
                buttonPercent.getLayoutParams().width = buttonResult.getHeight();
                buttonPercent.requestLayout();

                //Log.d("CHR_GAMES_TEST", "Set height for view from: " + buttonMultiplication.getLayoutParams().height + " to: " + buttonPercent.getWidth());
                buttonMultiplication.getLayoutParams().height = buttonResult.getHeight();
                buttonMultiplication.getLayoutParams().width = buttonResult.getHeight();
                buttonMultiplication.requestLayout();

                //Log.d("CHR_GAMES_TEST", "Set height for view from: " + buttonDivision.getLayoutParams().height + " to: " + buttonPercent.getWidth());
                buttonDivision.getLayoutParams().height = buttonResult.getHeight();
                buttonDivision.getLayoutParams().width = buttonResult.getHeight();
                buttonDivision.requestLayout();

                //Log.d("CHR_GAMES_TEST", "Set height for view from: " + buttonMinus.getLayoutParams().height + " to: " + buttonPercent.getWidth());
                buttonMinus.getLayoutParams().height = buttonResult.getHeight();
                buttonMinus.getLayoutParams().width = buttonResult.getHeight();
                buttonMinus.requestLayout();

                buttonPlus.getLayoutParams().height = buttonResult.getWidth();
                buttonPlus.getLayoutParams().width = buttonResult.getHeight();
                buttonPlus.requestLayout();


            }
        });


    }
}
