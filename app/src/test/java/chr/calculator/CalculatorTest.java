package chr.calculator;

import org.junit.Test;

import static org.junit.Assert.*;

public class CalculatorTest {

    private Calculator calculator = new Calculator();

    @Test
    public void calculate() {

        String input = "(20+5-10)*2/3";

        double result;
        double expected = 100;

        try {
            result = calculator.calculate(input);

            assertEquals(expected, result, 1);

        } catch (CalculatorSyntaxException e) {
            e.printStackTrace();
        }
    }
}