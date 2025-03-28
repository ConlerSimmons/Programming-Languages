import java.util.ArrayList;

/**
 * Derived class that represents a compound statement in the SILLY language.
 *
 * @author Dave Reed & Conler Simmons
 * @version 1/20/25, updated 3/10/25
 */
public class Compound extends Statement {
    // =========== Fields ===========
    private final ArrayList<Statement> statements;    // renamed from stmts

    // =========== Constructor ===========
    public Compound(TokenStream input) throws Exception {
        validateOpenBrace(input);
        this.statements = new ArrayList<Statement>();
        parseStatements(input);
        validateCloseBrace(input);
    }

    // =========== Core Methods ===========
    @Override
    public void execute() throws Exception {
        Interpreter.MEMORY.beginNestedScope();
        try {
            executeStatements();
        } catch (Return.ReturnException re) {
            Interpreter.MEMORY.endCurrentScope();
            throw re;
        } catch (Exception e) {
            Interpreter.MEMORY.endCurrentScope();
            throw e;
        }
        Interpreter.MEMORY.endCurrentScope();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{\n");
        for (Statement stmt : this.statements) {
            sb.append("  ").append(stmt).append("\n");
        }
        return sb.append("}").toString();
    }

    // =========== Helper Methods ===========
    private void validateOpenBrace(TokenStream input) throws Exception {
        if (!input.next().toString().equals("{")) {
            throw new Exception("SYNTAX ERROR: Malformed compound statement");
        }
    }

    private void validateCloseBrace(TokenStream input) throws Exception {
        input.next();  // consume closing brace
    }

    private void parseStatements(TokenStream input) throws Exception {
        while (!input.lookAhead().toString().equals("}")) {
            this.statements.add(Statement.getStatement(input));
        }
    }

    private void executeStatements() throws Exception {
        for (Statement stmt : this.statements) {
            stmt.execute();
        }
    }
}