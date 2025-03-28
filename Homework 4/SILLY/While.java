/**
 * Derived class that represents a while statement in the SILLY language.
 * 
 * @author Dave Reed & Conler Simmons
 * @version 1/20/25
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
    private void validateBooleanCondition(DataValue result) throws Exception {
        if (result.getType() != DataValue.Type.BOOLEAN) {
            throw new Exception("RUNTIME ERROR: Loop requires boolean condition");
        }
    }

    private boolean getBooleanValue(DataValue result) {
        return (Boolean) result.getValue();
    }

    private void executeLoopBody() throws Exception {
        try {
            this.loopBlock.execute();
        } catch (Return.ReturnException re) {
            throw re;
        }
    }
}