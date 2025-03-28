import java.util.ArrayList;

/**
 * Derived class that represents a function declaration in the SILLY language.
 * 
 * @author Conler Simmons
 * @version 3/10/25
 */
public class FunctionDecl extends Statement {
    private final Token functionIdentifier;           // renamed from name
    private final ArrayList<Token> formalParams;      // renamed from parameters
    private final Compound functionImplementation;    // renamed from body

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

    /**
     * Gets the name of the function.
     *
     * @return the name token of the function
     */
    public Token getName() {
        return this.functionIdentifier;
    }

    /**
     * Gets the parameters of the function.
     *
     * @return the list of parameters
     */
    public ArrayList<Token> getParameters() {
        return this.formalParams;
    }

    /**
     * Gets the body of the function.
     *
     * @return the compound statement representing the function body
     */
    public Compound getBody() {
        return this.functionImplementation;
    }

    /**
     * Executes the current function declaration.
     */
    @Override
    public void execute() throws Exception {
        validateDeclaration();
        registerFunction();
    }

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
     * Converts the current function declaration into a String.
     * 
     * @return the String representation of this statement
     */
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
}