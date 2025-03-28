/**
 * Derived class that represents a repeat statement in the SILLY language.
 * 
 * @author Owen McGrath
 * @version 2/10/25
 */
public class Repeat extends Statement {

    private Expression expr;
    private Compound body;

    /**
     * Reads in a repeat statement from the specified stream
     * 
     * @param input the stream to be read from
     */
    public Repeat(TokenStream input) throws Exception {
        if (!input.next().toString().equals("repeat")) {
            throw new Exception("SYNTAX ERROR: Malnourished repeat statement");
        }
        this.expr = new Expression(input);
        this.body = new Compound(input);
    }

    /**
     * Executes the current repeat statement.
     */
    @Override
    public void execute() throws Exception {
        DataValue eVal = this.expr.evaluate();
        if (eVal.getType() != DataValue.Type.NUMBER) {
            throw new Exception(
                    "RUNTIME ERROR: repeat statement requires a number.");
        }

        Double numVal = (Double) eVal.getValue();
        if (numVal % 1 != 0) {
            throw new Exception(
                    "RUNTIME ERROR: repeat statement requires an integer.");
        }

        int count = numVal.intValue();
        if (count < 0) {
            throw new Exception(
                    "RUNTIME ERROR: repeat statement requires a non-negative number.");
        }

        for (int i = 0; i < count; i++) {
            try {
                this.body.execute();
            } catch (Return.ReturnException re) {
                throw re;
            }
        }
    }

    /**
     * Converts the current repeat statement to a string.
     * 
     * @return the String representation of this statement
     */
    @Override
    public String toString() {
        return "repeat " + this.expr + " " + this.body;
    }
}
