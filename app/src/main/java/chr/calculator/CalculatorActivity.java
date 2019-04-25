package chr.calculator;

import android.content.res.Resources;
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

                // Set width as a height (Make circle)
                buttonPercent.getLayoutParams().height = buttonResult.getHeight();
                buttonPercent.getLayoutParams().width = buttonResult.getHeight();
                buttonPercent.requestLayout();

                buttonMultiplication.getLayoutParams().height = buttonResult.getHeight();
                buttonMultiplication.getLayoutParams().width = buttonResult.getHeight();
                buttonMultiplication.requestLayout();

                buttonDivision.getLayoutParams().height = buttonResult.getHeight();
                buttonDivision.getLayoutParams().width = buttonResult.getHeight();
                buttonDivision.requestLayout();

                buttonMinus.getLayoutParams().height = buttonResult.getHeight();
                buttonMinus.getLayoutParams().width = buttonResult.getHeight();
                buttonMinus.requestLayout();

                int width = buttonResult.getHeight() * 2 + (int) (16 * Resources.getSystem().getDisplayMetrics().density);

                buttonResult.getLayoutParams().width = width;
                buttonResult.requestLayout();
                
                buttonPlus.getLayoutParams().height = width;
                buttonPlus.getLayoutParams().width = buttonResult.getHeight();
                buttonPlus.requestLayout();
            }
        });


    }
}
