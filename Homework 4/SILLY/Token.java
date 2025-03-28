import java.util.Arrays;
import java.util.List;

/**
 * Class that represents a token in the SILLY language.
 * 
 * @author Dave Reed
 * @version 1/20/25
 */
public class Token {

    // ================================ Constants ================================
    public static final List<String> delims        = Arrays.asList("{", "}", "(", ")", "[", "]");
    public static final List<String> booleans      = Arrays.asList("true", "false");
    public static final List<String> mathFuncs     = Arrays.asList("+", "*", "/");
    public static final List<String> boolFuncs     = Arrays.asList("==", "!=", ">", ">=", "<", "<=",
                                                                  "and", "or", "not");
    public static final List<String> seqFuncs      = Arrays.asList("len", "get", "cat", "str");
    public static final List<String> keywords      = Arrays.asList("=", "print", "if", "else", "while",
                                                                  "repeat", "func", "return");


    // ================================= Types ==================================
    public static enum Type {
        UNKNOWN,     DELIM,       KEYWORD,     IDENTIFIER, 
        BOOL_FUNC,   MATH_FUNC,   SEQ_FUNC,    NUM_LITERAL, 
        BOOL_LITERAL,             CHAR_LITERAL, STR_LITERAL
    }


    // =============================== Instance ================================
    private String strVal;

    /**
     * Constructs a token out of the given string.
     * 
     * @param str the string value of the token
     */
    public Token(String str) {
        this.strVal = str;
    }

    /**
     * Analyzes token string to determine its syntactic type.
     * Handles numbers, delimiters, keywords, identifiers, and literals.
     *
     * @return Type enum indicating token's syntactic category
     */
    public Token.Type getType() {
        if (Character.isDigit(this.strVal.charAt(0)) ||
                (this.strVal.charAt(0) == '-' && this.strVal.length() > 1
                        && Character.isDigit(this.strVal.charAt(1)))) {
            try {
                Double.parseDouble(strVal);
                return Token.Type.NUM_LITERAL;
            } catch (Exception e) {
                return Token.Type.UNKNOWN;
            }
        } else if (Token.delims.contains(this.strVal)) {
            return Token.Type.DELIM;
        } else if (Token.keywords.contains(this.strVal)) {
            return Token.Type.KEYWORD;
        } else if (Token.boolFuncs.contains(this.strVal)) {
            return Token.Type.BOOL_FUNC;
        } else if (Token.mathFuncs.contains(this.strVal)) {
            return Token.Type.MATH_FUNC;
        } else if (Token.seqFuncs.contains(this.strVal)) {
            return Token.Type.SEQ_FUNC;
        } else if (Token.booleans.contains(this.strVal)) {
            return Token.Type.BOOL_LITERAL;
        } else if (Character.isLetter(this.strVal.charAt(0))) {
            for (int i = 1; i < this.strVal.length(); i++) {
                if (!Character.isLetterOrDigit(this.strVal.charAt(i))) {
                    return Token.Type.UNKNOWN;
                }
            }
            return Token.Type.IDENTIFIER;
        } else if (this.strVal.charAt(0) == '"') {
            if (this.strVal.length() == 1 || this.strVal.charAt(this.strVal.length() - 1) != '"') {
                return Token.Type.UNKNOWN;
            }
            return Token.Type.STR_LITERAL;
        } else if (this.strVal.charAt(0) == '\'') {
            if (this.strVal.length() != 3 || this.strVal.charAt(2) != '\'') {
                return Token.Type.UNKNOWN;
            }
            return Token.Type.CHAR_LITERAL;
        } else {
            return Token.Type.UNKNOWN;
        }
    }

    /**
     * Compares token with another object for equality.
     * Two tokens are equal if they have the same string value.
     *
     * @param other Object to compare with
     * @return true if tokens are equal, false otherwise
     */
    public boolean equals(Object other) {
        return this.strVal.equals(((Token) other).strVal);
    }

    /**
     * Converts the token to its string representation.
     * 
     * @return the string representation
     */
    public String toString() {
        return this.strVal;
    }

    /**
     * Generates a hash code for a Token (based on its String hash code).
     * 
     * @return a hash code for the Token
     */
    public int hashCode() {
        return this.strVal.hashCode();
    }
}