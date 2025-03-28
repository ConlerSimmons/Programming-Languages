/**
 * Derived class that represents a repeat statement in the SILLY language.
 * 
 * @author Conler Simmons
 * @version 3/27/25
 */
public class Repeat extends Statement {

    // =========================== Fields ===========================
    private final Expression    iterationExpr;
    private final Compound      loopBody;


    // ======================= Constructor ========================
    /**
     * Creates a repeat statement by parsing the number of iterations and body.
     * Expects "repeat" keyword followed by a numeric expression and compound statement.
     *
     * @param input TokenStream to read from
     * @throws Exception if repeat statement syntax is invalid
     */
    public Repeat(TokenStream input) throws Exception {
        if (!input.next().toString().equals("repeat")) {
            throw new Exception("SYNTAX ERROR: Malnourished repeat statement");
        }
        this.iterationExpr = new Expression(input);
        this.loopBody = new Compound(input);
    }


    // ======================= Core Methods =======================
    /**
     * Executes the repeat statement by evaluating iteration count and running body.
     * Validates count is a non-negative integer before executing.
     *
     * @throws Exception if evaluation fails or count is invalid
     */
    @Override
    public void execute() throws Exception {
        int iterations = validateAndGetIterationCount();
        executeLoop(iterations);
    }


    // ====================== Helper Methods ======================
    /**
     * Evaluates and validates the iteration expression.
     * Converts the result to an integer iteration count.
     *
     * @return Number of times to execute the loop
     * @throws Exception if count is not a valid non-negative integer
     */
    private int validateAndGetIterationCount() throws Exception {
        DataValue countValue = this.iterationExpr.evaluate();
        validateNumericType(countValue);
        Double count = (Double) countValue.getValue();
        validateIntegerValue(count);
        return count.intValue();
    }

    /**
     * Executes the loop body the specified number of times.
     * Propagates any return exceptions from the body.
     *
     * @param count Number of iterations to execute
     * @throws Exception if body execution fails
     */
    private void executeLoop(int count) throws Exception {
        for (int i = 0; i < count; i++) {
            try {
                this.loopBody.execute();
            } catch (Return.ReturnException re) {
                throw re;
            }
        }
    }

    /**
     * Validates that the iteration count is a numeric value.
     *
     * @param value DataValue to validate
     * @throws Exception if value is not numeric
     */
    private void validateNumericType(DataValue value) throws Exception {
        if (value.getType() != DataValue.Type.NUMBER) {
            throw new Exception(
                    "RUNTIME ERROR: repeat statement requires a number.");
        }
    }

    /**
     * Validates that a numeric value is a non-negative integer.
     *
     * @param value Value to validate
     * @throws Exception if value is not a non-negative integer
     */
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