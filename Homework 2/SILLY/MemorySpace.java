import java.util.*;

/** 
 * Memory management system
 * @author Dave Reed (modified by Conler Simmons)
 * @version 2.1.0 [2025]
 */
public class MemorySpace {
    private Stack<ScopeRec> runtimeStack;    // Scope stack

    /** Creates global scope */
    public MemorySpace() {
        this.runtimeStack = new Stack<>();
        this.runtimeStack.push(new ScopeRec(null));
    }
    
    // Scope management
    public void beginNestedScope()  { this.runtimeStack.push(new ScopeRec(this.runtimeStack.peek())); }
    public void endCurrentScope()   { this.runtimeStack.pop(); }
    
    // Variable operations
    public void declareVariable(Token variable) { 
        this.runtimeStack.peek().storeInScope(variable, null); 
    }
    
    public boolean isDeclared(Token variable) {
        return findScopeInStack(variable) != null;
    }
    
    public void storeValue(Token variable, DataValue val) {
        findScopeInStack(variable).storeInScope(variable, val);
    }
    
    public DataValue lookupValue(Token variable) {
        return findScopeInStack(variable).lookupInScope(variable);
    }

    // Helper method
    private ScopeRec findScopeInStack(Token variable) {
        ScopeRec current = this.runtimeStack.peek();
        while (current != null && !current.declaredInScope(variable)) {
            current = current.getParentScope();
        }
        return current;
    }
}