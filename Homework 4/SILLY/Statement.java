/**
 * Abstract class for representing a statement in the SILLY language.
 * 
 * @author Dave Reed
 * @version 1/20/25, updated 3/27/25
 */
public abstract class Statement {

    /**
     * Abstract method to execute the statement's behavior.
     * Each derived class must implement their specific execution logic.
     * 
     * @throws Exception if execution fails due to syntax or runtime errors
     */
    public abstract void execute() throws Exception;

    /**
     * Abstract method to provide string representation of the statement.
     * Used for debugging and display purposes.
     * 
     * @return String representation of the statement
     */
    public abstract String toString();

    /**
     * Factory method to create appropriate Statement subclass.
     * Examines next token to determine statement type.
     * 
     * @param input TokenStream to read from
     * @return New Statement object of appropriate type
     * @throws Exception if statement syntax is invalid
     */
    public static Statement getStatement(TokenStream input) throws Exception {
        Token first = input.lookAhead();
        
        if (first.toString().equals("print"))     return new Print(input);
        if (first.toString().equals("if"))        return new If(input);
        if (first.toString().equals("while"))     return new While(input);
        if (first.toString().equals("{"))         return new Compound(input);
        if (first.toString().equals("repeat"))    return new Repeat(input);
        if (first.toString().equals("func"))      return new FunctionDecl(input);
        if (first.toString().equals("return"))    return new Return(input);
        
        if (first.getType() == Token.Type.IDENTIFIER) {
            return new Assignment(input);
        }
        
        throw new Exception("SYNTAX ERROR: Unknown statement type (" + first + ")");
    }
}