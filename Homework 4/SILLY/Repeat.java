/**
 * Derived class that represents a repeat statement in the SILLY language.
 * 
 * @author Conler Simmons
 * @version 2/10/25
 */
public class Repeat extends Statement {

    // =========================== Fields ===========================
    private final Expression    iterationExpr;
    private final Compound      loopBody;

    /**
     * Reads in a repeat statement from the specified stream
     * 
     * @param input the stream to be read from
     */
    public Repeat(TokenStream input) throws Exception {
        if (!input.next().toString().equals("repeat")) {
            throw new Exception("SYNTAX ERROR: Malnourished repeat statement");
        }
        this.iterationExpr = new Expression(input);
        this.loopBody = new Compound(input);
    }

    // ======================= Core Methods =======================
    @Override
    public void execute() throws Exception {
        int iterations = validateAndGetIterationCount();
        executeLoop(iterations);
    }

    // ====================== Helper Methods ======================
    private int validateAndGetIterationCount() throws Exception {
        DataValue countValue = this.iterationExpr.evaluate();
        validateNumericType(countValue);
        Double count = (Double) countValue.getValue();
        validateIntegerValue(count);
        return count.intValue();
    }

    private void executeLoop(int count) throws Exception {
        for (int i = 0; i < count; i++) {
            try {
                this.loopBody.execute();
            } catch (Return.ReturnException re) {
                throw re;
            }
        }
    }

    private void validateNumericType(DataValue value) throws Exception {
        if (value.getType() != DataValue.Type.NUMBER) {
            throw new Exception(
                    "RUNTIME ERROR: repeat statement requires a number.");
        }
    }

    private void validateIntegerValue(Double value) throws Exception {
        if (value % 1 != 0) {
            throw new Exception(
                    "RUNTIME ERROR: repeat statement requires an integer.");
        }
        if (value < 0) {
            throw new Exception(
                    "RUNTIME ERROR: repeat statement requires a non-negative number.");
        }
    }

    // ==================== String Generation ====================
    @Override
    public String toString() {
        return "repeat " + this.iterationExpr + " " + this.loopBody;
    }
}