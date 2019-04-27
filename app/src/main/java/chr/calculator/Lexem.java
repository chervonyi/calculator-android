package chr.calculator;

public class Lexem {

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
