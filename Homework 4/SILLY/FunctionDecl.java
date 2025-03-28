import java.util.ArrayList;

/**
 * Derived class that represents a function declaration in the SILLY language.
 * 
 * @author Conler Simmons
 * @version 3/10/25
 */
public class FunctionDecl extends Statement {

    // =============================== Fields ================================
    private final Token              functionIdentifier;
    private final ArrayList<Token>   formalParams;
    private final Compound           functionImplementation;


    // ========================== Public Interface ==========================
    public Token getName()                      { return this.functionIdentifier; }
    public ArrayList<Token> getParameters()     { return this.formalParams; }
    public Compound getBody()                  { return this.functionImplementation; }


    // =========================== Core Methods ============================
    @Override
    public void execute() throws Exception {
        validateDeclaration();
        registerFunction();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("func " + this.functionIdentifier + "(");
        for (int i = 0; i < formalParams.size(); i++) {
            if (i > 0) {
                result.append(" ");
            }
            result.append(formalParams.get(i));
        }
        result.append(") ");
        result.append(this.functionImplementation.toString());
        return result.toString();
    }


    // ========================== Helper Methods ===========================
    private void validateDeclaration() throws Exception {
        if (Interpreter.MEMORY.isDeclared(this.functionIdentifier)) {
            throw new Exception("RUNTIME ERROR: Cannot declare function - name exists as variable");
        }
        if (Interpreter.MEMORY.isFunctionDeclared(this.functionIdentifier.toString())) {
            throw new Exception("RUNTIME ERROR: Function already declared");
        }
    }

    private void registerFunction() {
        Interpreter.registerFunction(this.functionIdentifier.toString(), this);
        Interpreter.MEMORY.storeFunction(this.functionIdentifier.toString(), this);
        Interpreter.MEMORY.declareVariable(this.functionIdentifier);
        Interpreter.MEMORY.storeValue(this.functionIdentifier, new BooleanValue(true));
    }

    /**
     * Reads in a function declaration from the specified stream
     * 
     * @param input the stream to be read from
     */
    public FunctionDecl(TokenStream input) throws Exception {
        // code block that ensures the function is declared correctly, is an identifier,
        // has a body, and has parameters that are identifiers
        if (!input.next().toString().equals("func")) {
            throw new Exception("SYNTAX ERROR: Malnourished function declaration");
        }

        this.functionIdentifier = input.next();
        if (this.functionIdentifier.getType() != Token.Type.IDENTIFIER) {
            throw new Exception("SYNTAX ERROR: Function name must be an identifier");
        }

        if (!input.next().toString().equals("(")) {
            throw new Exception("SYNTAX ERROR: Expected '(' after function name");
        }

        this.formalParams = new ArrayList<Token>();
        while (!input.lookAhead().toString().equals(")")) {
            Token param = input.next();
            if (param.getType() != Token.Type.IDENTIFIER) {
                throw new Exception("SYNTAX ERROR: Function parameter must be an identifier");
            }
            this.formalParams.add(param);
        }

        input.next();
        this.functionImplementation = new Compound(input);
    }
}