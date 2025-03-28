import java.util.Scanner;
import java.io.File;

/**
 * Class for reading SILLY language tokens from an input stream, either
 * standard input or a file.
 * @author Dave Reed
 * @version 1/20/25, updated 3/27/25
 */
public class TokenStream {

    // ================================== Fields ==================================
    private Scanner        input;
    private Token         nextToken;
    private String        buffer;


    // =============================== Constructors ==============================
    /**
     * Constructs a TokenStream for reading from standard input.
     * Initializes empty buffer and input scanner.
     */
    public TokenStream() {
        this.input = new Scanner(System.in);
        this.buffer = "";
    }
    
    /**
     * Constructs a TokenStream for reading from a file.
     * 
     * @param filename path to the source file
     * @throws java.io.FileNotFoundException if file cannot be opened
     */
    public TokenStream(String filename) throws java.io.FileNotFoundException {
        this.input = new Scanner(new File(filename));
        this.buffer = "";
    }


    // ============================= Token Processing ============================
    /**
     * Looks at next token without consuming it.
     * Handles string literals, char literals and delimiters.
     * 
     * @return Next token in stream
     */
    public Token lookAhead() {
        if (this.nextToken == null) {
            if (this.buffer.equals("") && this.input.hasNext()) {
                this.buffer = this.input.next().strip();
            }
            
            int index = 1;
            if (!Token.delims.contains(this.buffer.substring(0,1))) {
                if (this.buffer.charAt(0) == '"') {
                    while (index < this.buffer.length() && this.buffer.charAt(index) != '"') {
                        index++;
                    }
                    index++;
                } else if (this.buffer.charAt(0) == '\'') {
                    index = 3;
                } else {
                    while (index < this.buffer.length() && !Token.delims.contains(this.buffer.substring(index,index+1))) {
                        index++;
                    }
                }
            }
            this.nextToken = new Token(this.buffer.substring(0, index));
        }
        return this.nextToken;
    }

    /**
     * Reads and returns next token, removing it from stream.
     * 
     * @return Token that was read
     */
    public Token next() {
        Token safe = this.lookAhead();
        this.nextToken = null;
        this.buffer = this.buffer.substring(safe.toString().length()).strip();
        return safe;
    }
     

    // ============================== Stream State ==============================
    /**
     * Checks if more tokens are available.
     * 
     * @return true if more tokens can be read
     */
    public boolean hasNext() {
        return (this.nextToken != null || !this.buffer.equals("") || this.input.hasNext());
    }
}