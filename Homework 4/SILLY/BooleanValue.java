/**
 * Class that represents a Boolean value.
 * 
 * @author Dave Reed
 * @version 1/20/25
 */
public class BooleanValue implements DataValue {
    private final boolean logicalValue;   // renamed from value

    /**
     * Identifies the actual type of the value.
     * 
     * @return Token.Type.BOOLEAN
     */
    @Override
    public DataValue.Type getType() {
        return DataValue.Type.BOOLEAN;
    }

    /**
     * Accesses the stored Boolean value.
     * 
     * @return the Boolean value (as an Object)
     */
    @Override
    public Object getValue() {
        return Boolean.valueOf(this.logicalValue);
    }

    /**
     * Constructs a default Boolean value (true).
     */
    public BooleanValue() {
        this(true);
    }

    /**
     * Constructs a Boolean value.
     * 
     * @param val the value being stored
     */
    public BooleanValue(boolean val) {
        this.logicalValue = val;
    }

    /**
     * Converts the Boolean value to a String.
     * 
     * @return a String representation of the Boolean value
     */
    public String toString() {
        return "" + this.logicalValue;
    }

    /**
     * Comparison method for BooleanValues.
     * 
     * @param other the value being compared with
     * @return negative if <, 0 if ==, positive if >
     */
    public int compareTo(DataValue other) {
        return ((Boolean) this.getValue()).compareTo((Boolean) other.getValue());
    }
}