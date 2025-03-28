import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Token representation for the SILLY language.
 * Handles lexical analysis and token classification.
 * Supports multiple token types including:
 * - Keywords and Delimiters
 * - Mathematical and Boolean operators
 * - Sequence functions
 * - Literals (numbers, booleans, characters, strings)
 * - Identifiers
 *
 * @author Dave Reed (modified by Conler Simmons)
 * @version 2.7.0 [2024]
 * @see TokenStream
 */
public class Token {
    // Enum definitions first
    public static enum Type { UNKNOWN, DELIM, KEYWORD, IDENTIFIER, BOOL_FUNC, MATH_FUNC, SEQ_FUNC,  
    	                      NUM_LITERAL, BOOL_LITERAL, CHAR_LITERAL, STR_LITERAL }

    // Fields
    public static List<String> delims =    Arrays.asList( "{", "}", "(", ")", "[", "]"         );
    public static List<String> booleans =  Arrays.asList( "true", "false"                      );
    public static List<String> mathFuncs = Arrays.asList( "+", "*", "/"                        );  
    public static List<String> boolFuncs = Arrays.asList( "==", "!=", ">", ">=", "<", "<=", 
    		                                              "and", "or", "not"                   );
    public static List<String> seqFuncs =  Arrays.asList( "len", "get", "cat", "str"           ); 
    public static List<String> keywords =  Arrays.asList( "=", "print", "if", "else", "while", 
    		                                              "repeat", "func", "return"           ); 
    private String token;
    private Token.Type type;

    // Constructor
    public Token(String tok) {
        this.token = tok;
    }
    
    // Core functionality methods
    /**
     * Identifies what type of token it is.
     *   @return the token type (e.g., Token.Type.IDENTIFIER)
     */
    public Token.Type getType() {
    	if (Character.isDigit(this.token.charAt(0)) || 
    	    (this.token.charAt(0) == '-' && this.token.length() > 1 && Character.isDigit(this.token.charAt(1)))) {
            try {
            	Double.parseDouble(token);
            	return Token.Type.NUM_LITERAL;
            }
            catch (Exception e) {
                return Token.Type.UNKNOWN;
            }
        }
    	else if (Token.delims.contains(this.token)) {
            return Token.Type.DELIM;
        }
        else if (Token.keywords.contains(this.token)) {
            return Token.Type.KEYWORD;
        }
        else if (Token.boolFuncs.contains(this.token)) {
            return Token.Type.BOOL_FUNC;
        }
        else if (Token.mathFuncs.contains(this.token)) {
            return Token.Type.MATH_FUNC;
        }
        else if (Token.seqFuncs.contains(this.token)) {
            return Token.Type.SEQ_FUNC;
        }
        else if (Token.booleans.contains(this.token)) {
        	return Token.Type.BOOL_LITERAL;
        }     
        else if (Character.isLetter(this.token.charAt(0))) {
            for (int i = 1; i < this.token.length(); i++) {
                if (!Character.isLetterOrDigit(this.token.charAt(i))) {
                    return Token.Type.UNKNOWN;
                }
            }
            return Token.Type.IDENTIFIER;
        }
        else if (this.token.charAt(0) == '"') {
            if (this.token.length() == 1 || this.token.charAt(this.token.length()-1) != '"') {
                return Token.Type.UNKNOWN;
            }
            return Token.Type.STR_LITERAL;
        }
        else if (this.token.charAt(0) == '\'') {
            if (this.token.length() != 3 || this.token.charAt(2) != '\'') {
                return Token.Type.UNKNOWN;
            }
            return Token.Type.CHAR_LITERAL;
        }
        else {
            return Token.Type.UNKNOWN;
        }
    }
    
    /**
     * Determines when two tokens are identical.
     *   @param other the other token being compared
     *   @return whether the two tokens represent the same string value
     */
    public boolean equals(Object other) {
        return this.token.equals(((Token)other).token);
    }
   
    // toString last
    /**
     * Converts the token to its string representation.
     *   @return the string representation
     */
    public String toString() {
        return this.token;
    }
    
    /**
     * Generates a hash code for a Token (based on its String hash code).
     *   @return a hash code for the Token
     */
    public int hashCode() {
    	return this.token.hashCode();
    }
}