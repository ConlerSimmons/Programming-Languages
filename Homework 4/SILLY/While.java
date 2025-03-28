/**
 * Derived class that represents a while statement in the SILLY language.
 * 
 * @author Dave Reed & Conler Simmons
 * @version 1/20/25, updated 3/27/25
 */
public class While extends Statement {

    // ================================ Fields ================================
    private final Expression    loopCondition;
    private final Compound      loopBlock;


    // ============================= Constructor ==============================
    public While(TokenStream input) throws Exception {
        if (!input.next().toString().equals("while")) {
            throw new Exception("SYNTAX ERROR: Malformed while statement");
        }
        this.loopCondition = new Expression(input);
        this.loopBlock = new Compound(input);
    }


    // ============================ Main Operations ============================
    /**
     * Executes a while loop by repeatedly evaluating condition and executing body.
     * Continues until condition becomes false or return statement is encountered.
     * 
     * @throws Exception if condition evaluation or body execution fails
     */
    @Override
    public void execute() throws Exception {
        for (;;) {  // alternative to while(true)
            DataValue testResult = this.loopCondition.evaluate();
            validateBooleanCondition(testResult);
            
            if (!getBooleanValue(testResult)) { break; }
            
            executeLoopBody();
        }
    }

    @Override
    public String toString() {
        return String.format("while %s %s", this.loopCondition, this.loopBlock);
    }


    // ============================ Helper Methods ===========================
    /**
     * Validates that loop condition evaluates to a boolean.
     * 
     * @param result DataValue to validate
     * @throws Exception if result is not a boolean value
     */
    private void validateBooleanCondition(DataValue result) throws Exception {
        if (result.getType() != DataValue.Type.BOOLEAN) {
            throw new Exception("RUNTIME ERROR: Loop requires boolean condition");
        }
    }

    /**
     * Extracts boolean value from DataValue result.
     * 
     * @param result DataValue containing boolean
     * @return extracted boolean value
     */
    private boolean getBooleanValue(DataValue result) {
        return (Boolean) result.getValue();
    }

    /**
     * Executes a single iteration of the loop body.
     * Propagates any return exceptions from the body.
     * 
     * @throws Exception if body execution fails
     */
    private void executeLoopBody() throws Exception {
        try {
            this.loopBlock.execute();
        } catch (Return.ReturnException re) {
            throw re;
        }
    }
}