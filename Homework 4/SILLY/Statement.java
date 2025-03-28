/**
 * Abstract class for representing a statement in the SILLY language.
 * 
 * @author Dave Reed
 * @version 1/20/25
 */
public abstract class Statement {
    // ========== Core Methods ==========
    public abstract void execute() throws Exception;
    public abstract String toString();

    // ========== Statement Factory ==========
    public static Statement getStatement(TokenStream input) throws Exception {
        Token first = input.lookAhead();
        
        if (first.toString().equals("print"))  return new Print(input);
        if (first.toString().equals("if"))     return new If(input);
        if (first.toString().equals("while"))  return new While(input);
        if (first.toString().equals("{"))      return new Compound(input);
        if (first.toString().equals("repeat")) return new Repeat(input);
        if (first.toString().equals("func"))   return new FunctionDecl(input);
        if (first.toString().equals("return")) return new Return(input);
        if (first.getType() == Token.Type.IDENTIFIER) return new Assignment(input);
        
        throw new Exception("SYNTAX ERROR: Unknown statement type (" + first + ")");
    }
}