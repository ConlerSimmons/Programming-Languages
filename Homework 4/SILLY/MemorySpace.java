import java.util.Stack;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that defines the memory space for the SILLY interpreter.
 *
 * @author Dave Reed & Conler Simmons
 * @version 1/20/25, updated 3/10/25
 */
public class MemorySpace {

    // ================================ Fields ================================
    private final Stack<ScopeRec>           scopeStack;
    private final Map<String, FunctionDecl>  functionTable;

    // ============================= Scope Methods ===========================
    /**
     * Constructs a memory space with a single (global) scope record.
     */
    public MemorySpace() {
        this.scopeStack = new Stack<ScopeRec>();
        this.beginFunctionScope();
        this.functionTable = new HashMap<String, FunctionDecl>();
    }

    public void beginNestedScope() {
        this.scopeStack.push(new ScopeRec(this.scopeStack.peek()));
    }

    public void beginFunctionScope() {
        this.scopeStack.push(new ScopeRec(null));
    }

    public void endCurrentScope() {
        this.scopeStack.pop();
    }

    // ============================ Memory Methods ===========================
    /**
     * Declares a variable (without storing an actual value).
     *
     * @param variable the variable to be declared
     */
    public void declareVariable(Token variable) {
        this.scopeStack.peek().storeInScope(variable, null);
    }

    /**
     * Determines if a variable is already declared.
     *
     * @param variable the variable to be found
     * @return true if it is declared and/or assigned; else, false
     */
    public boolean isDeclared(Token variable) {
        return (this.findScopeinStack(variable) != null);
    }

    /**
     * Stores a value in memory.
     *
     * @param variable the variable to store the value for
     * @param val      the value to be stored
     */
    public void storeValue(Token variable, DataValue val) {
        this.findScopeinStack(variable).storeInScope(variable, val);
    }

    /**
     * Determines the value associated with a variable in memory.
     *
     * @param variable the variable to look up
     * @return the value associated with that variable
     */
    public DataValue lookupValue(Token variable) {
        return this.findScopeinStack(variable).lookupInScope(variable);
    }

    // ========== Function Operations ==========
    /**
     * Stores a function declaration in the function map.
     * 
     * @param functionName the name of the function
     * @param functionDecl the function declaration object
     */
    public void storeFunction(String functionName, FunctionDecl functionDecl) {
        functionTable.put(functionName, functionDecl);
    }

    /**
     * Retrieves a function declaration from the function map.
     * 
     * @param functionName the name of the function to retrieve
     * @return the function declaration associated with the name, or null if not
     *         found
     */
    public FunctionDecl lookupFunction(String functionName) {
        return functionTable.get(functionName);
    }

    /**
     * Determines if a function is already declared.
     * 
     * @param functionName the name of the function to check
     * @return true if the function is declared; else, false
     */
    public boolean isFunctionDeclared(String functionName) {
        return functionTable.containsKey(functionName);
    }

    // ========== Private Helpers ==========
    /**
     * Locates the Scope in the stackSegment that contains the specified variable.
     *
     * @param variable the variable being searched for
     * @return the Scope containing that variable
     */
    private ScopeRec findScopeinStack(Token variable) {
        ScopeRec stepper = this.scopeStack.peek();
        while (stepper != null && !stepper.declaredInScope(variable)) {
            stepper = stepper.getParentScope();
        }
        return stepper;
    }
}