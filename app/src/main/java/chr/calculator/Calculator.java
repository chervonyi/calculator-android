package chr.calculator;

public class Calculator {

    private final char DELIMITER = '.';

    // String of expression
    private String source;

    // Result of calculated part of expression
    private double result;

    // Current number
    private double num;

    // Current operator
    private Lexem operator;

    /**
     * This method use Recursive descent parser.
     *
     * @see <a href="https://en.wikipedia.org/wiki/Recursive_descent_parser">Wiki</a>
     * @see <a href="http://www.cs.fsu.edu/~lacher/courses/COP4020/fall14/assigns/proj2a.html">Description</a>
     *
     * @param source string of expression like: "2*(12/5.5)"
     * @return result of give expression
     *
     * @throws CalculatorSyntaxException exception with any kind of error like:
     * bad syntax of expression, empty given string, caught ArithmeticException etc.
     */
    public double calculate(String source) throws CalculatorSyntaxException {

        if (source.length() == 0) {
            throw new CalculatorSyntaxException(CalculatorSyntaxException.EMPTY_INPUT);
        }

        reset();
        this.source = source;

        // Start a descent by levels
        level_2();

        return result;
    }

    /**
     * The highest level of Recursive descent parser.
     * This method do calculation with lowest priority operations like: + and -.
     *
     * @throws CalculatorSyntaxException in the case of an error during calculation
     */
    private void level_2() throws CalculatorSyntaxException  {

        // Go to the lower level (Finding for operation with higher priority operation)
        level_3();

        double tmpResult = num != -1 ? num : 0;

        Lexem currentOperator;

        // Do calculation with appropriate operators (+ and -)
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

    /**
     * The lower level of Recursive descent parser.<br>
     * This method do calculation with higher priority operations like: * and /.
     *
     * @throws CalculatorSyntaxException in the case of an error during calculation
     */
    private void level_3() throws CalculatorSyntaxException  {

        // Go to the lower level (Finding for operation with higher priority operation)
        level_4();

        double tmpResult = num != -1 ? num : 0;

        Lexem currentOperator;

        // Do calculation with appropriate operators (* and /)
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
                throw new CalculatorSyntaxException(CalculatorSyntaxException.BAD_SYNTAX);
            }

            if (Double.isInfinite(tmpResult) || Double.isNaN(tmpResult)) {
                throw new CalculatorSyntaxException(CalculatorSyntaxException.BAD_EXPRESSION);
            }
        }

        if (tmpResult != 0) {
            num = tmpResult;
        }
    }

    /**
     * The lower level of Recursive descent parser.<br>
     * This method do calculation with higher priority operations like: %
     *
     * @throws CalculatorSyntaxException in the case of an error during calculation
     */
    private void level_4() throws CalculatorSyntaxException  {

        // Go to the lowest level (To read a number or an operator)
        level_5();

        double tmpResult = num != -1 ? num : 0;

        Lexem currentOperator;

        // Do calculation with appropriate operator - %
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

    /**
     * The lowest level of Recursive descent parser. <br>
     * This method does not do any calculation,
     * but it's reading any kind of Lexem (numbers or operators)
     * and then assign it into appropriate variables. <br><br>
     *
     * Also, in case of '(' operator, it calls {@link #level_2()} method
     * to do calculation in brackets.
     * @throws CalculatorSyntaxException in the case of an error during calculation
     */
    private void level_5() throws CalculatorSyntaxException {

        if (source.length() == 0) {
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

    /**
     * Convert Lexem to double
     * @param lexem required instance of Lexem class
     * @return number of double type
     */
    private double parse(Lexem lexem) {
        return lexem.type == Lexem.NUMBER ? Double.parseDouble(lexem.value) : 0;
    }

    /**
     * Read the next operator
     * @return instance of Lexem class
     *
     * @throws CalculatorSyntaxException in the case of an error during calculation
     */
    private Lexem nextOperator() throws CalculatorSyntaxException {
        if (source.length() > 0) {
           return nextLexem();
        }
        return new Lexem();
    }

    /**
     * Read the next Lexem
     * @return instance of Lexem class
     * @throws CalculatorSyntaxException in the case of an error during calculation
     */
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

            // Check if it was a last character (Last character is an bracket = Bad syntax)
            if (source.length() == 0 && operator.equals("(")) {
                throw new CalculatorSyntaxException(CalculatorSyntaxException.BAD_SYNTAX);
            }

            return new Lexem(operator, Lexem.BRACKET);
        }

        return new Lexem();
    }

    /**
     * Remove the last symbol from source
     */
    private void popElement() {
        source = source.substring(1);
    }

    /**
     * Reset all variables
     */
    private void reset() {
        source = "";
        result = 0;
        num = -1;
        operator = new Lexem();
    }
}
