package chr.calculator;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;

public class CalculatorActivity extends AppCompatActivity {

    // UI
    // Buttons:
    private TextView buttonPercent;
    private TextView buttonMultiplication;
    private TextView buttonPlus;
    private TextView buttonMinus;
    private TextView buttonDivision;
    private TextView buttonResult;

    // Expression views
    private TextView resultView;
    private TextView inputView;

    // Logic
    private Calculator calculator = new Calculator();
    private DecimalFormat decimalFormat = new DecimalFormat("#.###");

    // Times for logic of erase button
    private final int TIME_TO_ERASE_ONE_SYMBOL = 500;
    private final int MIN_TIME_TO_ERASE_ONE_SYMBOL = 100;
    private final int STEP_TIME = 70;

    private int currentTimeToErase;
    private boolean eraseIsPressed = false;

    private final int MAX_SYMBOLS = 30;

    // Timer
    private final Handler handler = new Handler();
    private Runnable mErasePress = new Runnable() {
        public void run() {

            // If erase button is still pressed - erase one symbol and call itself again
            if (eraseIsPressed) {

                eraseOneSymbol();

                // Reduce time to wait to erase one symbol
                if (currentTimeToErase > MIN_TIME_TO_ERASE_ONE_SYMBOL) {
                    currentTimeToErase -= STEP_TIME;
                }

                // Call itself again
                handler.postDelayed(mErasePress, currentTimeToErase);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        // Connect all UI components
        buttonPercent = findViewById(R.id.button_percent);
        buttonMultiplication = findViewById(R.id.button_multiplication);
        buttonPlus = findViewById(R.id.button_plus);
        buttonMinus = findViewById(R.id.button_subtraction);
        buttonDivision = findViewById(R.id.button_division);
        buttonResult = findViewById(R.id.button_result);
        resultView = findViewById(R.id.textViewResult);
        inputView = findViewById(R.id.textViewInput);

        composeButtonsView();

        setListeners();
    }

    /**
     * Set appropriate height and width to all operation buttons according to each other
     */
    private void composeButtonsView() {
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

    /**
     * Set logic on press for some buttons
     */
    private void setListeners() {
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
                        if (isLastEquals(inputView.getText().toString(),"+−×÷(") || inputView.getText().length() == 0) {
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

        findViewById(R.id.button_erase).setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    eraseIsPressed = true;
                    currentTimeToErase = TIME_TO_ERASE_ONE_SYMBOL;
                    handler.postDelayed(mErasePress, currentTimeToErase);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    eraseOneSymbol();
                    eraseIsPressed = false;
                }

                return true;
            }
        });
    }

    /**
     * Save value for resultView to appropriate memory slot
     * @param view clicked memory slot
     * @return true if value was saved successful; <br>
     *     false if it was not saved.
     */
    private boolean setMemory(View view) {
        try {
            double result = calculator.calculate(convertString(inputView.getText().toString()));
            TextView memorySlot = (TextView) view;
            memorySlot.setText(decimalFormat.format(result));
            return true;
        } catch (CalculatorSyntaxException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Listeners for num pad
     * @param view pressed number view
     */
    public void onClickNumber(View view) {
        String fullName = getResources().getResourceName(view.getId());
        String num = fullName.substring(fullName.lastIndexOf("_") + 1);

        appendInput(num);

        // Update resultView according to current input
        preCalculation();
    }

    /**
     * Remove the last character from inputView
     */
    private void eraseOneSymbol() {
        String input = inputView.getText().toString();

        if (input.length() > 0) {
            input = input.substring(0, input.length() - 1);
            inputView.setText(input);

            // Update resultView according to current input
            preCalculation();
        }
    }

    /**
     * Update resultView according to current expression from inputView
     */
    private void preCalculation() {
        try {
            double result = calculator.calculate(convertString(inputView.getText().toString()));
            resultView.setText(decimalFormat.format(result));

        } catch (CalculatorSyntaxException exp) {
            resultView.setText("");
        }
    }

    /**
     * Listener for "=" button
     * @param view pressed view
     */
    public void onClickCalculate(View view) {
        String input = inputView.getText().toString();

        if (input.length() == 0) {
            return;
        }

        try {
            double result = calculator.calculate(convertString(input));
            inputView.setText(decimalFormat.format(result));
            resultView.setText("");

        } catch (CalculatorSyntaxException e) {
            inputView.setText("");
            resultView.setText(e.toString());
        }
    }

    /**
     * Method to check if the last character equals to one of the listed in the given set
     * @param stringToCheck string which must be checked
     * @param set list of interested symbols
     * @return true if the last symbol equals to one of the listed in the given set;
     *  false if it was not found in the given list.
     */
    private boolean isLastEquals(String stringToCheck, String set) {
        if (stringToCheck.length() == 0) { return false; }

        return set.contains(String.valueOf(stringToCheck.charAt(stringToCheck.length() - 1)));
    }

    /**
     * Listener for all of operating buttons like: + - * / % . ( )
     * @param view pressed view
     */
    public void onClickOperatingButton(View view) {

        switch (view.getId()) {
            case R.id.button_division:          appendInput("÷"); break;
            case R.id.button_multiplication:    appendInput("×"); break;
            case R.id.button_percent:           appendInput("%"); break;
            case R.id.button_plus:              appendInput("+"); break;
            case R.id.button_point:             appendInput("."); break;
            case R.id.button_subtraction:       appendInput("−"); break;
            case R.id.buttonCloseBrackets:      appendInput(")"); break;
            case R.id.buttonOpenBrackets:       appendInput("("); break;
        }

        // Update resultView according to current input
        preCalculation();
    }

    /**
     * Method to append some string to inputView.
     * It contains some checks to decide - append string or do not append it.
     * @param str string to append
     */
    private void appendInput(String str) {

        String input = inputView.getText().toString();

        // Check on max symbols in inputView
        if (input.length() >= MAX_SYMBOLS) {
            return;
        }

        // Check on empty string of inputView
        if (input.length() == 0) {
            // One of these character cannot be the first one in the input string
            if ("+−×÷%)".contains(str)) {
                return;
            }
        }

        // Check if pointer does not make error
        if (str.equals(".")) {
            // Put '0' before point to make input string like: "0."
            if (isLastEquals(input, "+−×÷%(") || input.length() == 0) {
                input += "0";
            }

            else if (!isLastNumberWell() || isLastEquals(input, ")")) {
                return;
            }
        }

        // Check on main operation symbols
        if ("+−×÷%".contains(str)) {
            if (isLastEquals(input, "+−×÷%")) {
                input = input.substring(0, input.length() - 1);
            } else if (isLastEquals(input, ".")) {
                return;
            }
        }

        // Check on "0" symbol
        if (str.equals("0")) {
            if (input.length() == 0 || isLastEquals(input, "+−×÷%(")) {
                str += ".";
            }
        }

        // Conditions for brackets
        if (str.equals("(")) {
            if (input.length() == 0) {
                inputView.setText(input + str);
                return;
            } else if (!isLastEquals(input, "+−×÷%(")) {
                return;
            }
        } else if (str.equals(")")) {
            if (isLastEquals(input, "+−×÷%.(") || count(input, ")") >= count(input,"(")) {
                return;
            }
        }

        inputView.setText(input + str);
    }

    /**
     * Check if the last number (NOT only one symbol) is well (does not contain .)
     * @return <b>true</b> if the last number is ok; <br>
     *      <b>false</b> if it is not well.
     */
    private boolean isLastNumberWell() {
        return count(getLastNumber(inputView.getText().toString()), ".") < 1;
    }

    /**
     * Returns the whole number (Including delimiter) like: "242.2", "1984", "0.001"
     * @param input string to search for number
     * @return string with necessary number
     */
    private String getLastNumber(String input) {
        String num = "";

        while (isLastEquals(input, "0123456789.")) {
            num = input.charAt(input.length() - 1) + num;
            input = input.substring(0, input.length() - 1);
        }
        return num;
    }

    /**
     * Count the number of required symbol in given string
     * @param stringToCheck string to search
     * @param symbol required symbol
     * @return amount of the interested symbol in given string
     */
    private int count(String stringToCheck, String symbol) {
        return stringToCheck.length() -
                stringToCheck.replaceAll(String.format("\\%s", symbol),"").length();
    }

    /**
     * Convert given performed string to standard look.
     * Replace all special characters to usual analogues. (e.g. '×' -> '*')
     * @param str performed string from inputView
     * @return converted string which is ready to be sent into 'calculate' method. <br><br>
     * See {@link Calculator#calculate(String)}
     */
    private String convertString(String str) {
        str = str.replaceAll("\\×", "*");
        str = str.replaceAll("\\−", "-");
        str = str.replaceAll("\\÷", "/");
        return str;
    }
}
