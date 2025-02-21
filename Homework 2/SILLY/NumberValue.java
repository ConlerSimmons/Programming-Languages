/** 
 * Numeric value handler
 * @author Dave Reed (modified by Conler Simmons)
 * @version 2.1.0 [2025]
 */
public class NumberValue implements DataValue {
    private final Double value;    // Stored number
    
    // Constructors
    public NumberValue()           { this(0.0); }
    public NumberValue(double num) { this.value = num; }
    
    // Interface implementations
    @Override public Object getValue()        { return this.value; }
    @Override public DataValue.Type getType() { return DataValue.Type.NUMBER; }
    @Override public String toString()        { return this.value.toString(); }
    
    @Override
    public int compareTo(DataValue other) {
        if (other.getType() != DataValue.Type.NUMBER) {
            throw new IllegalArgumentException("Type Error: Expected NUMBER");
        }
        return this.value.compareTo((Double) other.getValue());
    }
}