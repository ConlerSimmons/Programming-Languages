/**
 * While statement implementation
 * @author Dave Reed (modified by Conler Simmons)
 * @version 2/7/24
 */
public class While extends Statement {
    // Core components
    private final Expression condition;
    private final Compound body;

    // Constructor
    public While(TokenStream input) throws Exception {
        validateKeyword(input);
        this.condition = new Expression(input);
        this.body = new Compound(input);
    }

    // Statement execution
    @Override 
    public void execute() throws Exception {
        while (evaluateCondition()) {
            body.execute();
        }
    }
    
    @Override 
    public String toString() {
        return String.format("while %s %s", condition, body);
    }

    // Helper methods  
    private void validateKeyword(TokenStream input) throws Exception {
        if (!input.next().toString().equals("while")) {
            throw new Exception("SYNTAX ERROR: Malformed while statement");
        }
    }

    private boolean evaluateCondition() throws Exception {
        DataValue result = condition.evaluate();
        validateBooleanType(result);
        return (Boolean) result.getValue();
    }

    private void validateBooleanType(DataValue value) throws Exception {
        if (value.getType() != DataValue.Type.BOOLEAN) {
            throw new Exception("RUNTIME ERROR: while statement requires Boolean test.");
        }
    }
}