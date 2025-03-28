import java.util.ArrayList;

/**
 * Derived class that represents a function declaration in the SILLY language.
 * 
 * @author Owen McGrath
 * @version 3/10/25
 */
public class FunctionDecl extends Statement {
    private Token name;
    private ArrayList<Token> parameters;
    private Compound body;

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

        this.name = input.next();
        if (this.name.getType() != Token.Type.IDENTIFIER) {
            throw new Exception("SYNTAX ERROR: Function name must be an identifier");
        }

        if (!input.next().toString().equals("(")) {
            throw new Exception("SYNTAX ERROR: Expected '(' after function name");
        }

        this.parameters = new ArrayList<Token>();
        while (!input.lookAhead().toString().equals(")")) {
            Token param = input.next();
            if (param.getType() != Token.Type.IDENTIFIER) {
                throw new Exception("SYNTAX ERROR: Function parameter must be an identifier");
            }
            this.parameters.add(param);
        }

        input.next();
        this.body = new Compound(input);
    }

    /**
     * Gets the name of the function.
     *
     * @return the name token of the function
     */
    public Token getName() {
        return this.name;
    }

    /**
     * Gets the parameters of the function.
     *
     * @return the list of parameters
     */
    public ArrayList<Token> getParameters() {
        return this.parameters;
    }

    /**
     * Gets the body of the function.
     *
     * @return the compound statement representing the function body
     */
    public Compound getBody() {
        return this.body;
    }

    /**
     * Executes the current function declaration.
     */
    @Override
    public void execute() throws Exception {
        // a check is performed to see if there is a variable with this name already
        if (Interpreter.MEMORY.isDeclared(this.name)) {
            throw new Exception("RUNTIME ERROR: Cannot declare function '" + this.name
                    + "', a variable with this name already exists");
        }

        // a check is performed to see if there is a function with this name already
        if (Interpreter.MEMORY.isFunctionDeclared(this.name.toString())) {
            throw new Exception("RUNTIME ERROR: Function '" + this.name + "' is already declared");
        }

        Interpreter.registerFunction(this.name.toString(), this);
        Interpreter.MEMORY.storeFunction(this.name.toString(), this);
        Interpreter.MEMORY.declareVariable(this.name);
        Interpreter.MEMORY.storeValue(this.name, new BooleanValue(true));
    }

    /**
     * Converts the current function declaration into a String.
     * 
     * @return the String representation of this statement
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("func " + this.name + "(");
        for (int i = 0; i < parameters.size(); i++) {
            if (i > 0) {
                result.append(" ");
            }
            result.append(parameters.get(i));
        }
        result.append(") ");
        result.append(this.body.toString());
        return result.toString();
    }
}