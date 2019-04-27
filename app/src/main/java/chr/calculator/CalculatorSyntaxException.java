package chr.calculator;

public class CalculatorSyntaxException extends Exception {

    public static final int EMPTY_INPUT = 10001;
    public static final int BAD_SYNTAX = 10002;

    private final int errorCode;

    public CalculatorSyntaxException(int code) {
        this.errorCode = code;
    }

    @Override
    public String toString() {

        switch (errorCode) {
            case EMPTY_INPUT:   return "Empty string";
            case BAD_SYNTAX:    return "Bad syntax";
        }

        return "Bad syntax";
    }
}
