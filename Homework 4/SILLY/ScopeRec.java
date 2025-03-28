import java.util.HashMap;
import java.util.Map;

/**
 * Class that represents a scope record in the program execution.
 * 
 * @author Dave Reed & Conler Simmons
 * @version 1/20/25, updated 3/10/25
 */
public class ScopeRec {

    // ==================== Fields ====================
    private final Map<Token, DataValue>    map;
    private final ScopeRec                 parentScope;


    // ==================== Constructor ====================
    public ScopeRec(ScopeRec parent) {
        this.map = new HashMap<Token, DataValue>();
        this.parentScope = parent;
    }


    // ==================== Access Methods ====================
    /**
     * Gets parent scope for variable lookup chain.
     * 
     * @return Parent scope or null if global scope
     */
    public ScopeRec getParentScope()                           { return this.parentScope; }

    /**
     * Checks if variable exists in current scope.
     * Does not check parent scopes.
     * 
     * @param variable Token identifying the variable
     * @return true if variable declared in this scope
     */
    public boolean declaredInScope(Token variable)             { return this.map.containsKey(variable); }

    /**
     * Retrieves variable value from current scope.
     * 
     * @param variable Token identifying the variable
     * @return Value associated with variable or null
     */
    public DataValue lookupInScope(Token variable)             { return this.map.get(variable); }

    public void storeInScope(Token variable, DataValue val)    { this.map.put(variable, val); }
}