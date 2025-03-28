/**
 * Derived class that represents an assignment statement in the SILLY language.
 * 
 * @author Dave Reed & Conler Simmons
 * @version 1/20/25, updated 3/10/25
 */
public class Assignment extends Statement {
    // Fields at top with spacing
    private final Token        targetVar;    // renamed from vbl
    private final Expression  valueExpr;    // renamed from expr

    /**
     * Reads in a assignment statement from the specified TokenStream.
     * 
     * @param input the stream to be read from
     */
    public Assignment(TokenStream input) throws Exception {
        this.targetVar = input.next();
        if (this.targetVar.getType() != Token.Type.IDENTIFIER) {
            throw new Exception("SYNTAX ERROR: Illegal lhs of assignment statement (" + this.targetVar + ")");
        }

        if (!input.next().toString().equals("=")) {
            throw new Exception("SYNTAX ERROR: Malformed assignment statement (expecting '=')");
        }

        this.valueExpr = new Expression(input);
    }

    // Execute method first
    @Override
    public void execute() throws Exception {
        validateAssignment();
        storeValue();
    }

    private void validateAssignment() throws Exception {
        if (Interpreter.MEMORY.isFunctionDeclared(this.targetVar.toString())) {
            throw new Exception("RUNTIME ERROR: Cannot assign to '" + this.targetVar + "' - name exists as function");
        }
    }

    private void storeValue() throws Exception {
        if (!Interpreter.MEMORY.isDeclared(this.targetVar)) {
            Interpreter.MEMORY.declareVariable(this.targetVar);
        }
        Interpreter.MEMORY.storeValue(this.targetVar, this.valueExpr.evaluate());
    }

    /**
     * Converts the current assignment statement into a String.
     * 
     * @return the String representation of this statement
     */
    public String toString() {
        return this.targetVar + " = " + this.valueExpr;
    }
}