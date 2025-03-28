/**
 * Derived class that represents an if statement in the SILLY language.
 * 
 * @author Dave Reed & Conler Simmons
 * @version 1/20/25, updated 3/27/25
 */
public class If extends Statement {

    // ============================== Fields ===============================
    private final Expression condition;     // renamed from test
    private final Compound   thenBlock;     // renamed from ifBody
    private final Compound   elseBlock;     // renamed from elseBody

    /**
     * Reads in an if statement from the specified stream
     * 
     * @param input the stream to be read from
     */
    public If(TokenStream input) throws Exception {
        if (!input.next().toString().equals("if")) {
            throw new Exception("SYNTAX ERROR: Malformed if statement");
        }
        this.condition = new Expression(input);
        this.thenBlock = new Compound(input);

        if (!input.next().toString().equals("else")) {
            throw new Exception("SYNTAX ERROR: Malformed if statement");
        }

        this.elseBlock = new Compound(input);
    }

    // ========================== Execution Flow ==========================
    @Override
    public void execute() throws Exception {
        DataValue result = evaluateCondition();
        executeAppropriateBlock(result);
    }

    // ========================== Helper Methods =========================
    private DataValue evaluateCondition() throws Exception {
        DataValue result = this.condition.evaluate();
        validateBooleanType(result);
        return result;
    }

    private void validateBooleanType(DataValue value) throws Exception {
        if (value.getType() != DataValue.Type.BOOLEAN) {
            throw new Exception("RUNTIME ERROR: If statement requires Boolean condition");
        }
    }

    private void executeAppropriateBlock(DataValue result) throws Exception {
        try {
            if ((Boolean)result.getValue()) {
                this.thenBlock.execute();
            } else {
                this.elseBlock.execute();
            }
        } catch (Return.ReturnException re) {
            throw re;
        }
    }

    /**
     * Converts the current if statement into a String.
     * 
     * @return the String representation of this statement
     */
    public String toString() {
        return "if " + this.condition + " " + this.thenBlock + "\nelse " + this.elseBlock;
    }
}