/** 
 * Assignment statement processor
 * @author Dave Reed (modified by Conler Simmons)
 * @version 2.1.0 [2025]
 */
public class Assignment extends Statement {
    private Token vbl;          // Variable being assigned
    private Expression expr;    // Value to assign
    
    /** Parses assignment statement */
    public Assignment(TokenStream input) throws Exception {
        this.vbl = input.next();
        validateIdentifier();
        validateEquals(input);
        this.expr = new Expression(input);
    }
    
    /** Executes assignment */
    @Override public void execute() throws Exception {
        if (!Interpreter.MEMORY.isDeclared(this.vbl)) {
            Interpreter.MEMORY.declareVariable(this.vbl);           
        } 
        Interpreter.MEMORY.storeValue(this.vbl, this.expr.evaluate());
    }
    
    @Override public String toString() { 
        return String.format("%s = %s", this.vbl, this.expr); 
    }

    // Helper methods
    private void validateIdentifier() throws Exception {
        if (this.vbl.getType() != Token.Type.IDENTIFIER) {
            throw new Exception("Syntax Error: Invalid assignment target");
        }
    }
    
    private void validateEquals(TokenStream input) throws Exception {
        if (!input.next().toString().equals("=")) {
            throw new Exception("Syntax Error: Expected '='");
        }
    }
}