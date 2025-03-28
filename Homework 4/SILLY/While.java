/**
 * Derived class that represents a while statement in the SILLY language.
 * 
 * @author Dave Reed & Conler Simmons
 * @version 1/20/25
 */
public class While extends Statement {
    private final Expression loopCondition;     // renamed for clarity
    private final Compound loopBlock;         // renamed for clarity

    @Override
    public void execute() throws Exception {
        for (;;) {  // alternative to while(true)
            DataValue testResult = this.loopCondition.evaluate();
            validateBooleanCondition(testResult);
            
            if (!getBooleanValue(testResult)) { break; }
            
            executeLoopBody();
        }
    }

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

    /**
     * Converts the current while statement into a String.
     * 
     * @return the String representation of this statement
     */
    public String toString() {
        return String.format("while %s %s", this.loopCondition, this.loopBlock);
    }

    /**
     * Reads in a while statement from the specified stream
     * 
     * @param input the stream to be read from
     */
    public While(TokenStream input) throws Exception {
        if (!input.next().toString().equals("while")) {
            throw new Exception("SYNTAX ERROR: Malformed while statement");
        }
        this.loopCondition = new Expression(input);
        this.loopBlock = new Compound(input);
    }
}