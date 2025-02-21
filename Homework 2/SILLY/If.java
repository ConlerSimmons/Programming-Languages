/** 
 * Conditional statement handler
 * @author Dave Reed (modified by Conler Simmons)
 * @version 2.1.0 [2025]
 */
public class If extends Statement {
    private Expression test;    // Condition
    private Compound ifBody;    // True branch
    private Compound elseBody;  // False branch

    /** Parses if statement */
    public If(TokenStream input) throws Exception {
        validateKeyword(input, "if");
        this.test = new Expression(input);
        this.ifBody = new Compound(input);
        validateKeyword(input, "else");
        this.elseBody = new Compound(input);
    }

    /** Executes appropriate branch */
    @Override 
    public void execute() throws Exception {
        if (evaluateCondition()) {
            this.ifBody.execute();
        } else {
            this.elseBody.execute();
        }
    }

    @Override 
    public String toString() {
        return String.format("if %s %s\nelse %s", 
                           this.test, this.ifBody, this.elseBody);
    }

    // Helper methods
    private void validateKeyword(TokenStream input, String keyword) throws Exception {
        if (!input.next().toString().equals(keyword)) {
            throw new Exception("Syntax Error: Expected '" + keyword + "'");
        }
    }

    private boolean evaluateCondition() throws Exception {
        DataValue result = this.test.evaluate();
        if (result.getType() != DataValue.Type.BOOLEAN) {
            throw new Exception("Type Error: Condition must be boolean");
        }
        return (Boolean) result.getValue();
    }
}