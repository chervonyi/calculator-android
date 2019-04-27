package chr.calculator;

import android.util.Log;

public class Calculator {

    private final char DELIMITER = '.';

    private String source;
    private double result;
    private double num;
    private Lexem operator;


    public double calculate(String source) throws CalculatorSyntaxException {

        if (source.length() == 0) {
            throw new CalculatorSyntaxException(CalculatorSyntaxException.EMPTY_INPUT);
        }

        reset();

        this.source = source;

        level_2();

        return result;
    }

    // Operations: + and -
    private void level_2() throws CalculatorSyntaxException  {

        level_3();

        double tmpResult = num != -1 ? num : 0;

        Lexem currentOperator;

        while (operator.value.equals("+") || operator.value.equals("-")) {
            currentOperator = operator;

            level_3();

            try {
                switch (currentOperator.value) {
                    case "+": tmpResult += num; break;
                    case "-": tmpResult -= num; break;
                    default: tmpResult = 0;
                }
            } catch (NumberFormatException e) {
                throw new CalculatorSyntaxException(CalculatorSyntaxException.BAD_SYNTAX);
            }
        }

        result = tmpResult;
    }

    // Operations: * and /
    private void level_3() throws CalculatorSyntaxException  {
        level_4();

        double tmpResult = num != -1 ? num : 0;

        Lexem currentOperator;

        while (operator.value.equals("*") || operator.value.equals("/")) {
            currentOperator = operator;

            level_4();

            try {
                switch (currentOperator.value) {
                    case "*": tmpResult *= num; break;
                    case "/": tmpResult /= num; break;
                    default: tmpResult = 0;
                }
            } catch (NumberFormatException e) {
                int a = 2;
                throw new CalculatorSyntaxException(CalculatorSyntaxException.BAD_SYNTAX);
            }
        }

        if (tmpResult != 0) {
            num = tmpResult;
        }
    }

    // Operations: %
    private void level_4() throws CalculatorSyntaxException  {
        level_5();

        double tmpResult = num != -1 ? num : 0;

        Lexem currentOperator;

        while (operator.value.equals("%")) {
            currentOperator = operator;

            level_5();

            try {
                switch (currentOperator.value) {
                    case "%": tmpResult = tmpResult * num / 100; break;
                    default: tmpResult = 0;
                }
            } catch (NumberFormatException e) {
                throw new CalculatorSyntaxException(CalculatorSyntaxException.BAD_SYNTAX);
            }
        }

        if (tmpResult != 0) {
            num = tmpResult;
        }
    }

    // Bottom level:
    private void level_5() throws CalculatorSyntaxException {

        if (source.length() == 0) {
            // TODO - Anyway, I can paste operator = new Lexem() to fix bug
            return;
        }

        Lexem currentLexem = nextLexem();

        if (currentLexem.value.equals("-")) {

            level_5();
            num = -num;

        } else if (currentLexem.type == Lexem.NUMBER) {

            num = parse(currentLexem);
            operator = nextOperator();

        } else if (currentLexem.value.equals("(")) {
            level_2();

            if (operator.value.equals(")")) {
                operator = nextOperator();
            } else {
                throw new CalculatorSyntaxException(CalculatorSyntaxException.BAD_SYNTAX);
            }

            num = result;
            result = 0;
        } else {
            throw new CalculatorSyntaxException(CalculatorSyntaxException.BAD_SYNTAX);
        }
    }

    private double parse(Lexem lexem) {
        return lexem.type == Lexem.NUMBER ? Double.parseDouble(lexem.value) : 0;
    }

    private Lexem nextOperator() throws CalculatorSyntaxException {
        if (source.length() > 0) {
           return nextLexem();
        }
        return new Lexem();
    }

    private Lexem nextLexem() throws CalculatorSyntaxException {

        // Read number
        if (Character.isDigit(source.charAt(0))) {
            boolean foundDelimiter = false;
            StringBuilder num = new StringBuilder();

            while (Character.isDigit(source.charAt(0)) ||
                source.charAt(0) == DELIMITER) {

                if (source.charAt(0) == DELIMITER) {
                    // Check if there are not two delimiters
                    if (foundDelimiter) {
                        throw new CalculatorSyntaxException(CalculatorSyntaxException.BAD_SYNTAX);
                    } else {
                        foundDelimiter = true;
                    }
                }

                num.append(source.charAt(0));
                popElement();

                if (source.length() == 0) { break; }
            }

            return new Lexem(num.toString(), Lexem.NUMBER);
        }

        // Read operators
        if ("+-*/%".contains(String.valueOf(source.charAt(0)))) {
            String operator = String.valueOf(source.charAt(0));
            popElement();

            // Check if it was a last character (Last character is an operator = Bad syntax)
            if (source.length() == 0) {
                throw new CalculatorSyntaxException(CalculatorSyntaxException.BAD_SYNTAX);
            }

            return new Lexem(operator, Lexem.OPERATOR);
        }

        // Read brackets
        if ("()".contains(String.valueOf(source.charAt(0)))) {
            String operator = String.valueOf(source.charAt(0));
            popElement();
            return new Lexem(operator, Lexem.BRACKET);
        }

        return new Lexem();
    }

    private void popElement() {
        source = source.substring(1);
    }


    private void reset() {
        source = "";
        result = 0;
        num = -1;
        operator = new Lexem();
    }

}
