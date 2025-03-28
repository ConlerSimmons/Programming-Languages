/** 
 * Conditional statement handler
 * @author Dave Reed (modified by Conler Simmons)
 * @version 2.1.0 [2025]
 */
public class If extends Statement {
    // Fields
    private Expression test;
    private Compound ifBody;
    private Compound elseBody;
    
    // Constructor
    public If(TokenStream input) throws Exception {
        validateIfKeyword(input);
        this.test = new Expression(input);
        this.ifBody = new Compound(input);
        validateElseKeyword(input);
        this.elseBody = new Compound(input);
    }
    
    // Core functionality
    @Override
    public void execute() throws Exception {
        validateTestExpression();
        executeAppropriateBody();
    }
    
    // Object overrides
    @Override
    public String toString() {
        return "if " + this.test + " " + this.ifBody + "\nelse " + this.elseBody;
    }
    
    // Helper methods
    private void validateIfKeyword(TokenStream input) throws Exception {
        if (!input.next().toString().equals("if")) {
            throw new Exception("SYNTAX ERROR: Malformed if statement");
        }
    }
    
    private void validateElseKeyword(TokenStream input) throws Exception {
        if (!input.next().toString().equals("else")) {
            throw new Exception("SYNTAX ERROR: Malformed if statement");
        }
    }
    
    private void validateTestExpression() throws Exception {
        DataValue test = this.test.evaluate();
        if (test.getType() != DataValue.Type.BOOLEAN) {
            throw new Exception("RUNTIME ERROR: If statement requires Boolean test.");
        }
    }
    
    private void executeAppropriateBody() throws Exception {
        if (((Boolean) test.evaluate().getValue())) {
            this.ifBody.execute();
        } else {
            this.elseBody.execute();
        }
    }
}