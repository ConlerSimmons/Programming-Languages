import java.util.ArrayList;

/** 
 * Compound statement handler
 * @author Dave Reed (modified by Conler Simmons)
 * @version 2.1.0 [2025]
 */
public class Compound extends Statement {
    private ArrayList<Statement> stmts;   // Statement list

    /** Parses compound statement */
    public Compound(TokenStream input) throws Exception {
        validateOpen(input);
        this.stmts = parseStatements(input);
    }

    /** Executes all statements */
    @Override public void execute() throws Exception {
        Interpreter.MEMORY.beginNestedScope();
        for (Statement stmt : this.stmts) { stmt.execute(); }
        Interpreter.MEMORY.endCurrentScope();
    }

    @Override public String toString() {
        StringBuilder sb = new StringBuilder("{\n");
        for (Statement stmt : this.stmts) {
            sb.append("  ").append(stmt).append("\n");
        }
        return sb.append("}").toString();
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
}