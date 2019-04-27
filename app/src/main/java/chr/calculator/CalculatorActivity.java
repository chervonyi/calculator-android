package chr.calculator;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;

public class CalculatorActivity extends AppCompatActivity {

    // UI
    private TextView buttonPercent;
    private TextView buttonMultiplication;
    private TextView buttonPlus;
    private TextView buttonMinus;
    private TextView buttonDivision;
    private TextView buttonResult;

    private TextView resultView;
    private TextView inputView;

    private Calculator calculator = new Calculator();
    private DecimalFormat decimalFormat = new DecimalFormat("#.###");

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

        resultView = findViewById(R.id.textViewResult);
        inputView = findViewById(R.id.textViewInput);

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

    public void onClickNumber(View view) {
        String fullName = getResources().getResourceName(view.getId());
        String num = fullName.substring(fullName.lastIndexOf("_") + 1);

        appendInput(num);

        preCalculation();
    }

    public void onClickErase(View view) {

        String input = inputView.getText().toString();

        if (input.length() > 0) {
            input = input.substring(0, input.length() - 1);
            inputView.setText(input);

            preCalculation();
        }
    }

    private void preCalculation() {
        try {
            double result = calculator.calculate(inputView.getText().toString());

            resultView.setText(decimalFormat.format(result));

        } catch (CalculatorSyntaxException exp) {
            Log.d("CHR_GAMES_TEST", "EXCEPTION");
            resultView.setText(" ");
        }

    }

    public void onClickCalculate(View view) {
        try {
            double result = calculator.calculate(inputView.getText().toString());
            inputView.setText(decimalFormat.format(result));
            resultView.setText("");
        } catch (CalculatorSyntaxException e) {
            inputView.setText("");
            resultView.setText(R.string.bad_syntax);
        }
    }

    private boolean isLastOperator() {
        return "+-*/%".contains(String.valueOf(inputView.getText().charAt(inputView.getText().length() - 1)));
    }

    public void onClickOperatingButton(View view) {

        switch (view.getId()) {
            case R.id.button_division:          appendInput("/"); break;
            case R.id.button_multiplication:    appendInput("*"); break;
            case R.id.button_percent:           appendInput("%"); break;
            case R.id.button_plus:              appendInput("+"); break;
            case R.id.button_point:             appendInput("."); break;
            case R.id.button_subtraction:       appendInput("-"); break;
            case R.id.buttonCloseBrackets:      appendInput(")"); break;
            case R.id.buttonOpenBrackets:       appendInput("("); break;
        }

        //preCalculation();
    }

    private void appendInput(String str) {

        String input = inputView.getText().toString();

        if (input.length() == 0) {
            // Put '0' before point to make input string like: "0."
            if (str.equals(".")) {
                input = "0";
            }
            // One of these character cannot be the first one in the input string
            if ("+-*/%)".contains(str)) {
                return;
            }
        }

        // Replace last character (operator) with a new one
        else if (isLastOperator() && "+-*/%".contains(str)) {
            input = input.substring(0, input.length() - 1);
        }

        inputView.setText(input + str);
    }
}
