/** 
 * Boolean value implementation
 * @author Dave Reed (modified by Conler Simmons)
 * @version 2.1.0 [Feb 2025]
 */
public class BooleanValue implements DataValue {
    protected boolean value;    // Stored boolean

    // Constructors
    public BooleanValue()           { this(true); }    
    public BooleanValue(boolean v)  { this.value = v; }

    // Interface implementations
    @Override public Object getValue()         { return this.value; }
    @Override public DataValue.Type getType()  { return DataValue.Type.BOOLEAN; }
    @Override public String toString()         { return String.valueOf(this.value); }
    
    @Override
    public int compareTo(DataValue other) {
        return ((Boolean)this.getValue()).compareTo((Boolean)other.getValue());
    }
}