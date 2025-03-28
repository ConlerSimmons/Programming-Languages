/**
 * Derived class that represents an assignment statement in the SILLY language.
 * 
 * @author Dave Reed & Owen McGrath
 * @version 1/20/25, updated 3/10/25
 */
public class Assignment extends Statement {
    private Token vbl;
    private Expression expr;

    /**
     * Reads in a assignment statement from the specified TokenStream.
     * 
     * @param input the stream to be read from
     */
    public Assignment(TokenStream input) throws Exception {
        this.vbl = input.next();
        if (this.vbl.getType() != Token.Type.IDENTIFIER) {
            throw new Exception("SYNTAX ERROR: Illegal lhs of assignment statement (" + this.vbl + ")");
        }

        if (!input.next().toString().equals("=")) {
            throw new Exception("SYNTAX ERROR: Malformed assignment statement (expecting '=')");
        }

        this.expr = new Expression(input);
    }

    /**
     * Executes the current assignment statement.
     */
    public void execute() throws Exception {
        // check if the variable is a function since variable and functions cannot have
        // the same name
        if (Interpreter.MEMORY.isFunctionDeclared(this.vbl.toString())) {
            throw new Exception("RUNTIME ERROR: Cannot assign to variable '" + this.vbl
                    + "', a function with this name already exists");
        }

        if (!Interpreter.MEMORY.isDeclared(this.vbl)) {
            Interpreter.MEMORY.declareVariable(this.vbl);
        }
        Interpreter.MEMORY.storeValue(this.vbl, this.expr.evaluate());
    }

    /**
     * Converts the current assignment statement into a String.
     * 
     * @return the String representation of this statement
     */
    public String toString() {
        return this.vbl + " = " + this.expr;
    }
}
