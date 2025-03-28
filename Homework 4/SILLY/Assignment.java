/**
 * Derived class that represents an assignment statement in the SILLY language.
 * 
 * @author Dave Reed & Conler Simmons
 * @version 1/20/25, updated 3/10/25
 */
public class Assignment extends Statement {

    // =============================== Fields ================================
    private final Token         targetVar;      // Variable being assigned to
    private final Expression    valueExpr;      // Expression to evaluate

    // =========================== Constructor ==============================
    /**
     * Creates an assignment statement by parsing input from the token stream.
     * Expects an identifier followed by '=' and an expression.
     *
     * @param input TokenStream to read from
     * @throws Exception if syntax is invalid
     */
    public Assignment(TokenStream input) throws Exception {
        this.targetVar = input.next();
        if (this.targetVar.getType() != Token.Type.IDENTIFIER) {
            throw new Exception("SYNTAX ERROR: Illegal lhs of assignment statement (" + this.targetVar + ")");
        }

        if (!input.next().toString().equals("=")) {
            throw new Exception("SYNTAX ERROR: Malformed assignment statement (expecting '=')");
        }

        this.valueExpr = new Expression(input);
    }

    // ========================== Core Methods =============================
    /**
     * Executes the assignment by evaluating the expression and storing the result.
     * Validates that assignment target is not a function name.
     *
     * @throws Exception if assignment is invalid or evaluation fails
     */
    @Override
    public void execute() throws Exception {
        validateAssignment();
        storeValue();
    }

    @Override
    public String toString() {
        return this.targetVar + " = " + this.valueExpr;
    }

    // ========================= Helper Methods ===========================
    private void validateAssignment() throws Exception {
        if (Interpreter.MEMORY.isFunctionDeclared(this.targetVar.toString())) {
            throw new Exception("RUNTIME ERROR: Cannot assign to '" + this.targetVar + "' - name exists as function");
        }
    }

    private void storeValue() throws Exception {
        if (!Interpreter.MEMORY.isDeclared(this.targetVar)) {
            Interpreter.MEMORY.declareVariable(this.targetVar);
        }
        Interpreter.MEMORY.storeValue(this.targetVar, this.valueExpr.evaluate());
    }
}