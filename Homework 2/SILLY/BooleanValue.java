/** 
 * Boolean value implementation
 * @author Dave Reed (modified by Conler Simmons)
 * @version 2/7/24
 */
public class BooleanValue implements DataValue {
    // Field first
    protected boolean value;
    
    // Constructors grouped together
    /**
     * Constructs a default Boolean value (true).
     */
    public BooleanValue() {
        this(true);
    }
    
    /**
     * Constructs a Boolean value.
     *   @param val the value being stored
     */
    public BooleanValue(boolean val) {
        this.value = val;
    }
    
    // Core interface methods grouped together
    /**
     * Accesses the stored Boolean value.
     *   @return the Boolean value (as an Object)
     */
    @Override
    public Object getValue() {
        return (Boolean) this.value;
    }
    
    /**
     * Identifies the actual type of the value.
     *   @return Token.Type.BOOLEAN
     */
    @Override
    public DataValue.Type getType() {
        return DataValue.Type.BOOLEAN;
    }
    
    // toString last as utility method
    /**
     * Converts the Boolean value to a String.
     *   @return a String representation of the Boolean value
     */
    @Override
    public String toString() {
        return "" + this.value;
    }
    
    /**
     * Comparison method for BooleanValues.
     *   @param other the value being compared with
     *   @return negative if <, 0 if ==, positive if >
     */
    @Override
    public int compareTo(DataValue other) {
        return ((Boolean)this.getValue()).compareTo((Boolean)other.getValue());
    }
}