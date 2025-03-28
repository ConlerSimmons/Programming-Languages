/**
 * Derived class that represents an output statement in the SILLY language.
 * 
 * @author Dave Reed
 * @version 1/20/25
 */
public class Print extends Statement {
    private final Expression displayExpr;    // renamed from outputExpr

    @Override
    public void execute() throws Exception {
        System.out.println(displayExpr.evaluate().toString());
    }

    /**
     * Reads in a print statement from the specified TokenStream.
     * 
     * @param input the stream to be read from
     */
    public Print(TokenStream input) throws Exception {
        if (!input.next().toString().equals("print")) {
            throw new Exception("SYNTAX ERROR: Invalid print statement");
        }
        this.displayExpr = new Expression(input);
    }

    /**
     * Converts the current print statement into a String.
     * 
     * @return the String representation of this statement
     */
    public String toString() {
        return "print " + this.displayExpr;
    }
}