/**
 * Derived class that represents a return statement in the SILLY language.
 *
 * @author Owen McGrath
 * @version 3/10/25
 */
public class Return extends Statement {

    private Expression expr;

    // a token that represents the return value of a function
    public static final Token RETURN_VALUE_TOKEN = new Token("__return__");

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
        this.expr = new Expression(input);
    }

    /**
     * Executes the current return statement.
     * Throws a ReturnException to signal early termination of the function.
     */
    @Override
    public void execute() throws Exception {
        DataValue value = this.expr.evaluate();
        throw new ReturnException(value);
    }

    /**
     * Converts the current return statement into a String.
     *
     * @return the String representation of this statement
     */
    @Override
    public String toString() {
        return "return " + this.expr;
    }
}