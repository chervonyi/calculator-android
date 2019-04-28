package chr.calculator;

/**
 * Lexem class is used for describe some elementary part of expression (atom). <br>
 * Value of lexem may contain:
 *      * integer number
 *      * float number
 *      * operator
 * E-g.: "25.6", "1984", "0.097", "+", "(", "/"...
 */
public class Lexem {

    // Error codes
    public static final int NUMBER = 20001;
    public static final int OPERATOR = 20002;
    public static final int BRACKET = 20003;

    public final String value;
    public final int type;

    public Lexem() {
        value = "";
        type = -1;
    }

    public Lexem(String value, int code) {
        this.value = value;
        this.type = code;
    }
}
