/**
 * Class that represents a Boolean value.
 * 
 * @author Dave Reed
 * @version 1/20/25, updated 3/27/25
 */
public class BooleanValue implements DataValue {

    // ================================== Fields ==================================
    private final boolean logicalValue;


    // =============================== Core Methods ===============================
    @Override
    public Object getValue()         { return Boolean.valueOf(this.logicalValue); }
    
    @Override
    public DataValue.Type getType()  { return DataValue.Type.BOOLEAN; }

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