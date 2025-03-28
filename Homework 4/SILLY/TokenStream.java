import java.io.File;
import java.util.Scanner;

/**
 * Class for reading SILLY language tokens from an input stream, either
 * standard input or a file.
 *
 * @author Dave Reed
 * @version 1/20/25
 */
public class TokenStream {

    private Scanner input;
    private Token nextToken;
    private String buffer;

    /**
     * Constructs a TokenStream connected to System.in.
     */
    public TokenStream() {
        this.input = new Scanner(System.in);
        this.buffer = "";
    }

    /**
     * Constructs a TokenStream connected to a file.
     *
     * @param filename the file to read from
     */
    public TokenStream(String filename) throws java.io.FileNotFoundException {
        this.input = new Scanner(new File(filename));
        this.buffer = "";
    }

    /**
     * Returns the next token in the TokenStream (without removing it).
     *
     * @return the next token
     */
    public Token lookAhead() {
        if (this.nextToken == null) {
            if (this.buffer.equals("") && this.input.hasNext()) {
                this.buffer = this.input.next().strip();
            }

            if (this.buffer.isEmpty()) {
                return null;
            }

            int index = 1;
            if (
                index <= this.buffer.length() &&
                !Token.delims.contains(this.buffer.substring(0, 1))
            ) {
                if (this.buffer.charAt(0) == '"') {
                    while (
                        index < this.buffer.length() &&
                        this.buffer.charAt(index) != '"'
                    ) {
                        index++;
                    }
                    if (index < this.buffer.length()) {
                        index++;
                    }
                } else if (this.buffer.charAt(0) == '\'') {
                    // Ensure we don't go out of bounds for character literals
                    index = Math.min(3, this.buffer.length());
                } else {
                    while (
                        index < this.buffer.length() &&
                        !Token.delims.contains(
                            this.buffer.substring(index, index + 1)
                        )
                    ) {
                        index++;
                    }
                }
            }

            // Ensure index doesn't exceed buffer length
            index = Math.min(index, this.buffer.length());
            this.nextToken = new Token(this.buffer.substring(0, index));
        }
        return this.nextToken;
    }

    /**
     * Returns the next token in the TokenStream (and removes it).
     *
     * @return the next token
     */
    public Token next() {
        Token safe = this.lookAhead();
        this.nextToken = null;
        if (safe != null && !this.buffer.isEmpty()) {
            int tokenLength = safe.toString().length();
            if (tokenLength <= this.buffer.length()) {
                this.buffer = this.buffer.substring(tokenLength).strip();
            } else {
                this.buffer = "";
            }
        }
        return safe;
    }

    /**
     * Determines whether there are any more tokens to read.
     *
     * @return true if tokens remaining, else false
     */
    public boolean hasNext() {
        return (
            this.nextToken != null ||
            !this.buffer.equals("") ||
            this.input.hasNext()
        );
    }
}
