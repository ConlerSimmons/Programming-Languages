/**
 * Derived class that represents a return statement in the SILLY language.
 *
 * @author Conler Simmons
 * @version 3/27/25
 */
public class Return extends Statement {
    // ================================ Fields ================================
    private final Expression returnExpr;
    public static final Token RETURN_VALUE_TOKEN = new Token("__return__");

    // ========== Exception Class ==========
    public static class ReturnException extends Exception {
        private final DataValue returnValue;

        public ReturnException(DataValue value) {
            super("RETURN");
            this.returnValue = value;
        }

        public DataValue getReturnValue() {
            return returnValue;
        }
    }

    /**
     * Reads in a return statement from the specified stream
     *
     * @param input the stream to be read from
     */
    public Return(TokenStream input) throws Exception {
        if (!input.next().toString().equals("return")) {
            throw new Exception("SYNTAX ERROR: Malformed return statement");
        }
        this.returnExpr = new Expression(input);
    }

    // ============================= Core Methods ============================
    @Override
    public void execute() throws Exception {
        throw new ReturnException(this.returnExpr.evaluate());
    }

    /**
     * Converts the current return statement into a String.
     *
     * @return the String representation of this statement
     */
    @Override
    public String toString() {
        return "return " + this.returnExpr;
    }
}