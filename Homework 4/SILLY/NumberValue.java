/**
 * Class that represents a number value.
 *   @author Dave Reed
 *   @version 1/20/25, updated 3/27/25
 */
public class NumberValue implements DataValue {

    // ======================== Fields ========================
    private final Double numericValue;    // renamed from numValue


    // ======================== Constructors ========================
    public NumberValue()        { this(0); }
    public NumberValue(double num) {
        this.numericValue = (Double) num;
    }


    // ======================== Interface Methods ========================
    @Override
    public Object getValue()          { return this.numericValue; }
    
    @Override
    public DataValue.Type getType()   { return DataValue.Type.NUMBER; }
    
    @Override
    public int compareTo(DataValue other) {
        return ((Double) this.getValue()).compareTo((Double) other.getValue());
    }

    @Override
    public String toString() {
        if (isInteger(this.numericValue)) {
            return String.valueOf(this.numericValue.intValue());
        }
        return String.valueOf(this.numericValue);
    }

    private boolean isInteger(double num) {
        return num == Math.floor(num);
    }
}