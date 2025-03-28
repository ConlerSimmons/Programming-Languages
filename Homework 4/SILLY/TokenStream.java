import java.util.Scanner;
import java.io.File;

/**
 * Class for reading SILLY language tokens from an input stream, either
 * standard input or a file.
 *   @author Dave Reed
 *   @version 1/20/25
 */
public class TokenStream {

    // ================================== Fields ==================================
    private Scanner        input;
    private Token         nextToken;
    private String        buffer;


    // =============================== Constructors ==============================
    public TokenStream() {
        this.input = new Scanner(System.in);
        this.buffer = "";
    }
    
    public TokenStream(String filename) throws java.io.FileNotFoundException {
        this.input = new Scanner(new File(filename));
        this.buffer = "";
    }


    // ============================= Token Processing ============================
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

    public Token next() {
        Token safe = this.lookAhead();
        this.nextToken = null;
        this.buffer = this.buffer.substring(safe.toString().length()).strip();
        return safe;
    }
     

    // ============================== Stream State ==============================
    public boolean hasNext() {
        return (this.nextToken != null || !this.buffer.equals("") || this.input.hasNext());
    }
}