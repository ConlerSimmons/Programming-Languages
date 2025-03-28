import java.util.Scanner;
import java.io.File;

/**
 * Token stream processor for the SILLY language.
 * Provides lexical analysis and token extraction from input.
 * Features:
 * - Interactive and file-based input handling
 * - Token lookahead capability
 * - Robust error handling
 * - Efficient token parsing
 * - Support for all SILLY language tokens
 *
 * @see Token
 * @version 2.7.0 [2024]
 * @author Dave Reed (modified by Conler Simmons)
 */
public class TokenStream {
    // Fields first
    private Scanner input;
    private String pending;
    private Token nextToken;
    private String buffer;
    
    // Constructors grouped together
    public TokenStream() { 
        this.input = new Scanner(System.in);
        this.buffer = "";
    }
    
    public TokenStream(String filename) throws java.io.FileNotFoundException {
        this.input = new Scanner(new File(filename));
        this.buffer = "";
    }

    // Core stream functionality methods
    public boolean hasNext() { return hasMoreTokens(); }
    public Token next()      { return extractToken(); }
    public Token lookAhead() { return getNextToken(); }
    
    // Helper methods last
    private void processLine() {
        if (input.hasNextLine()) {
            pending = input.nextLine();
        }
        else {
            pending = null;
        }
    }
    
    private Token getNextToken() {
        if (nextToken == null) {
            updateBuffer();
            nextToken = parseNextToken();
        }
        return nextToken;
    }
    
    private Token extractToken() {
        Token token = getNextToken();
        nextToken = null;
        buffer = buffer.substring(token.toString().length()).strip();
        return token;
    }
    
    private boolean hasMoreTokens() {
        return (nextToken != null || !buffer.isEmpty() || input.hasNext());
    }
    
    private void updateBuffer() {
        if (buffer.isEmpty() && input.hasNext()) {
            buffer = input.next().strip();
        }
    }
    
    private Token parseNextToken() {
        int index = calculateTokenLength();
        return new Token(buffer.substring(0, index));
    }
    
    private int calculateTokenLength() {
        if (Token.delims.contains(buffer.substring(0,1))) {
            return 1;
        }
        
        int index = 1;
        char firstChar = buffer.charAt(0);
        
        if (firstChar == '"') {
            while (index < buffer.length() && buffer.charAt(index) != '"') {
                index++;
            }
            index++;
        }
        else if (firstChar == '\'') {
            index = 3;
        }
        else {
            while (index < buffer.length() && 
                   !Token.delims.contains(buffer.substring(index,index+1))) {
                index++;
            }
        }
        return index;
    }
}