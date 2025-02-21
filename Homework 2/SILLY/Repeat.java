/**
 * Loop Control Structure for the SILLY language.
 * Implements iteration functionality by repeating a block of code a specified number of times.
 * The repeat count must be a non-negative integer value.
 * 
 * @author  Conler Y. Simmons
 * @version 2.0.1 [Feb 2025]
 */
public class Repeat extends Statement {
    
    /** Expression that evaluates to the number of iterations */
    private final Expression condition;
    
    /** Block of statements to be executed in each iteration */
    private final Compound body;
    
    /** 
     * Constructs a repeat statement by parsing the input stream.
     * Expected format: repeat <count_expression> <statement_block>
     * 
     * @param input TokenStream containing the repeat statement tokens
     * @throws Exception if syntax is invalid or missing required components
     */
    public Repeat(TokenStream input) throws Exception {
        ensureKeyword(input);
        this.condition = new Expression(input);
        this.body = new Compound(input);
    }
    
    /**
     * Executes the repeat statement by running the body multiple times.
     * First evaluates the condition to get iteration count, then executes
     * the body that many times.
     * 
     * @throws Exception if condition doesn't evaluate to valid integer or
     *         if body execution fails
     */
    @Override
    public void execute() throws Exception {
        int iterations = validateAndGetCount();
        runLoop(iterations);
    }
    
    /**
     * Verifies the presence of the 'repeat' keyword at the start.
     * 
     * @param input TokenStream to check for the keyword
     * @throws Exception if 'repeat' keyword is missing
     */
    private void ensureKeyword(TokenStream input) throws Exception {
        if (!"repeat".equals(input.next().toString())) {
            throw new Exception("Oops! I was expecting the 'repeat' keyword here.");
        }
    }
    
    /**
     * Evaluates and validates the repeat count expression.
     * The count must be a non-negative integer value.
     * 
     * @return validated integer count for loop iterations
     * @throws Exception if count is not a number, is negative, or not an integer
     */
    private int validateAndGetCount() throws Exception {
        DataValue value = this.condition.evaluate();
        
        if (value.getType() != DataValue.Type.NUMBER) {
            throw new Exception(
                "Hold up! The repeat count needs to be a number, but I got something else."
            );
        }
        
        Double count = (Double) value.getValue();
        if (count % 1 != 0 || count < 0) {
            throw new Exception(
                "Hey! The repeat count must be a whole number greater than or equal to zero."
            );
        }
        
        return count.intValue();
    }
    
    /**
     * Executes the loop body the specified number of times.
     * Each iteration runs all statements in the body compound.
     * 
     * @param count number of times to execute the loop body
     * @throws Exception if any iteration fails during execution
     */
    private void runLoop(int count) throws Exception {
        for (int i = 0; i < count; i++) {
            this.body.execute();
        }
    }
    
    /**
     * Creates a string representation of the repeat statement.
     * Format: "repeat <condition> <body>"
     * 
     * @return formatted string showing the repeat structure
     */
    @Override
    public String toString() {
        return String.format("repeat %s %s", this.condition, this.body);
    }
}