import java.util.Stack;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that defines the memory space for the SILLY interpreter.
 *
 * @author Dave Reed & Conler Simmons
 * @version 1/20/25, updated 3/27/25
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

    /**
     * Creates a new nested scope with access to parent scope.
     * Used for compound statements.
     */
    public void beginNestedScope() {
        this.scopeStack.push(new ScopeRec(this.scopeStack.peek()));
    }

    /**
     * Creates a new function scope with no parent access.
     * Used for function bodies.
     */
    public void beginFunctionScope() {
        this.scopeStack.push(new ScopeRec(null));
    }

    /**
     * Handles the end of current scope by removing it from stack.
     * All variables declared in this scope become inaccessible.
     */
    public void endCurrentScope() {
        this.scopeStack.pop();
    }

    // ============================ Memory Methods ===========================
    /**
     * Declares a variable in current scope.
     * Initially stores null value.
     *
     * @param variable Token representing variable name
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
     * Stores a value for a variable in its declaring scope.
     *
     * @param variable Token representing variable name
     * @param val Value to store
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
     * Searches scope stack for variable's declaring scope.
     * Follows scope chain from current to global.
     * 
     * @param variable Token to search for
     * @return ScopeRec containing variable or null if not found
     */
    private ScopeRec findScopeinStack(Token variable) {
        ScopeRec stepper = this.scopeStack.peek();
        while (stepper != null && !stepper.declaredInScope(variable)) {
            stepper = stepper.getParentScope();
        }
        return stepper;
    }
}