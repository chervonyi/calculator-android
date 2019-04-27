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


        String pattern = "memory_";
        TextView memorySlot;
        for (int i = 0; i < 4; i++) {
            memorySlot = findViewById(getResources().getIdentifier(pattern + i,
                    "id", getPackageName()));

            memorySlot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView slot = (TextView) v;

                    if (slot.getText().equals("+")) {
                        if (setMemory(v)) {
                            resultView.setText("");
                            inputView.setText("");
                        }
                    } else {
                        if (isLastEquals(inputView.getText().toString(),"+-*/(") || inputView.getText().length() == 0) {
                            appendInput(slot.getText().toString());
                            preCalculation();
                        }
                    }
                }
            });

            memorySlot.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    if (setMemory(v)) {
                        resultView.setText("");
                        inputView.setText("");
                    }

                    return true;
                }
            });
        }

    }

    private boolean setMemory(View view) {
        try {
            double result = calculator.calculate(inputView.getText().toString());
            TextView memorySlot = (TextView) view;
            memorySlot.setText(decimalFormat.format(result));
            return true;
        } catch (CalculatorSyntaxException e) {
            e.printStackTrace();
        }
        return false;
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
            resultView.setText("");
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

    private boolean isLastEquals(String stringToCheck, String set) {

        if (stringToCheck.length() == 0) {
            return false;
        }

        return set.contains(String.valueOf(stringToCheck.charAt(stringToCheck.length() - 1)));
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

        preCalculation();
    }

    private void appendInput(String str) {

        String input = inputView.getText().toString();

        if (input.length() == 0) {
            // One of these character cannot be the first one in the input string
            if ("+-*/%)".contains(str)) {
                return;
            }
        }

        // Check if pointer does not make error
        if (str.equals(".")) {
            // Put '0' before point to make input string like: "0."
            if (isLastEquals(input, "+-*/%(") || input.length() == 0) {
                input += "0";
            }

            else if (!isLastNumberWell() || isLastEquals(input, ")")) {
                return;
            }
        }

        if ("+-*/%".contains(str)) {
            if (isLastEquals(input, "+-*/%")) {
                input = input.substring(0, input.length() - 1);
            } else if (isLastEquals(input, ".")) {
                return;
            }
        }

        // Conditions for brackets
        if (str.equals("(")) {
            if (input.length() == 0) {
                inputView.setText(input + str);
                return;
            } else if (!isLastEquals(input, "+-*/%(")) {
                return;
            }
        } else if (str.equals(")")) {
            if (isLastEquals(input, "+-*/%.(") || count(input, ")") >= count(input,"(")) {
                return;
            }
        }

        inputView.setText(input + str);
    }

    private boolean checkSyntaxWith(String inputToCheck) {

        try {
            calculator.calculate(inputToCheck);
            return true;
        } catch (CalculatorSyntaxException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isLastNumberWell() {
        String input = inputView.getText().toString();

        String num = "";

        while (isLastEquals(input, "0123456789.")) {
            num = input.charAt(input.length() - 1) + num;
            input = input.substring(0, input.length() - 1);
        }

        return count(num, ".") < 1;
    }

    private int count(String stringToCheck, String symbol) {
        return stringToCheck.length() - stringToCheck.replaceAll(String.format("\\%s", symbol),"").length();
    }
}
