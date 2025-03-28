import java.util.ArrayList;

/**
 * Derived class that represents a compound statement in the SILLY language.
 *
 * @author Dave Reed & Conler Simmons
 * @version 1/20/25, updated 3/10/25
 */
public class Compound extends Statement {

    // =========================== Fields ===========================
    private final ArrayList<Statement>    statements;


    // ======================= Constructor =========================
    /**
     * Parses and creates a compound statement from the input stream.
     * Validates opening brace, reads statements until closing brace.
     *
     * @param input TokenStream to read from
     * @throws Exception if compound statement syntax is invalid
     */
    public Compound(TokenStream input) throws Exception {
        validateOpenBrace(input);
        this.statements = new ArrayList<Statement>();
        parseStatements(input);
        validateCloseBrace(input);
    }


    // ====================== Core Methods ========================
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


    // ==================== Helper Methods =======================
    /**
     * Validates that the next token is an opening brace.
     *
     * @param input TokenStream to read from
     * @throws Exception if opening brace is missing
     */
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

    /**
     * Reads and executes all statements in the compound block.
     * Provides scope for variable declarations.
     *
     * @throws Exception if any statement execution fails
     */
    private void executeStatements() throws Exception {
        for (Statement stmt : this.statements) {
            stmt.execute();
        }
    }
}