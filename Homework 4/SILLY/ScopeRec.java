import java.util.HashMap;
import java.util.Map;

/**
 * Class that represents a scope record in the program execution.
 * 
 * @author Dave Reed & Conler Simmons
 * @version 1/20/25, updated 3/10/25
 */
public class ScopeRec {
    private final Map<Token, DataValue> map;    // change back from symbolTable
    private final ScopeRec parentScope;         // change back from enclosingScope

    // Getters first
    public ScopeRec getParentScope() {
        return this.parentScope;
    }

    // Query methods next
    public boolean declaredInScope(Token variable) {  // change back from hasSymbol
        return this.map.containsKey(variable);
    }

    public DataValue lookupInScope(Token symbol) {
        return this.map.get(symbol);
    }

    // Mutator methods last
    public void storeInScope(Token variable, DataValue val) {  // change back from defineSymbol
        this.map.put(variable, val);
    }

    // Constructor at bottom
    public ScopeRec(ScopeRec parent) {
        this.map = new HashMap<Token, DataValue>();
        this.parentScope = parent;
    }
}