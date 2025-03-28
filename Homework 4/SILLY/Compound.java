import java.util.ArrayList;

public class Compound extends Statement {
    // Fields
    private ArrayList<Statement> stmts;
    
    // Constructor and initialization
    public Compound(TokenStream input) throws Exception {
        validateOpen(input);
        this.stmts = parseStatements(input);
    }
    
    // Core functionality
    @Override 
    public void execute() throws Exception {
        executeWithNewScope();
    }
    
    // Object overrides
    @Override 
    public String toString() {
        return formatCompoundStatement();
    }
    
    // Helper methods
    private void validateOpen(TokenStream input) throws Exception {
        if (!input.next().toString().equals("{")) {
            throw new Exception("Syntax Error: Expected '{'");
        }
    }
    
    private ArrayList<Statement> parseStatements(TokenStream input) throws Exception {
        ArrayList<Statement> statements = new ArrayList<>();
        while (!input.lookAhead().toString().equals("}")) {
            statements.add(Statement.getStatement(input));
        }
        input.next();
        return statements;
    }
    
    private void executeWithNewScope() throws Exception {
        Interpreter.MEMORY.beginNestedScope();
        executeAllStatements();
        Interpreter.MEMORY.endCurrentScope();
    }
    
    private void executeAllStatements() throws Exception {
        for (Statement stmt : this.stmts) {
            stmt.execute();
        }
    }
    
    private String formatCompoundStatement() {
        StringBuilder sb = new StringBuilder("{\n");
        for (Statement stmt : this.stmts) {
            sb.append("  ").append(stmt).append("\n");
        }
        return sb.append("}").toString();
    }
}