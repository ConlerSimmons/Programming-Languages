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
    public ScopeRec getParentScope()                           { return this.parentScope; }
    public boolean declaredInScope(Token variable)             { return this.map.containsKey(variable); }
    public DataValue lookupInScope(Token variable)             { return this.map.get(variable); }
    public void storeInScope(Token variable, DataValue val)    { this.map.put(variable, val); }
}