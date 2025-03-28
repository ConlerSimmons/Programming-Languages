/**
 * Derived class that represents an output statement in the SILLY language.
 * 
 * @author Dave Reed
 * @version 1/20/25, updated 3/27/25
 */
public class Print extends Statement {

    // =========================== Fields ===========================
    private final Expression displayExpr;


    // ======================== Constructor ========================
    public Print(TokenStream input) throws Exception {
        if (!input.next().toString().equals("print")) {
            throw new Exception("SYNTAX ERROR: Invalid print statement");
        }
        this.displayExpr = new Expression(input);
    }


    // ======================= Core Methods =======================
    @Override
    public void execute() throws Exception {
        System.out.println(displayExpr.evaluate().toString());
    }

    @Override
    public String toString() {
        return "print " + this.displayExpr;
    }
}