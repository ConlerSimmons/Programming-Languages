/**
 * Derived class that represents an assignment statement in the SILLY language.
 *   @author Dave Reed Modified by Conler Simmons
 *   @version 1/20/25
 */
public class Assignment extends Statement {
    // Fields
    private Token vbl;
    private Expression expr;
    
    // Constructor
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
    
    // Core functionality
    @Override public void execute() throws Exception {
        if (!Interpreter.MEMORY.isDeclared(this.vbl)) {
            Interpreter.MEMORY.declareVariable(this.vbl);           
        } 
        DataValue value = this.expr.evaluate();
        Interpreter.MEMORY.storeValue(this.vbl, value);
    }
    
    // Object overrides
    @Override public String toString() {
        return this.vbl + " = " + this.expr;
    }
}