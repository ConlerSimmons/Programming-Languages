/**
 * Class that represents a number value.
 *   @author Dave Reed
 *   @version 1/20/25
 */
public class NumberValue implements DataValue {

    private final Double numericValue;    // renamed from numValue

    /**
     * Constructs a default number value (0).
     */
    public NumberValue() {
        this(0);
    }

    /**
     * Constructs a number value.
     *   @param num the number being stored
     */
    public NumberValue(double num) {
        this.numericValue = (Double) num;
    }

    @Override
    public Object getValue() {
        return this.numericValue;
    }

    @Override
    public DataValue.Type getType() {
        return DataValue.Type.NUMBER;
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

    /**
     * Comparison method for NumberValues.
     *   @param other the value being compared with
     *   @return negative if <, 0 if ==, positive if >
     */
    public int compareTo(DataValue other) {
        return ((Double) this.getValue()).compareTo((Double) other.getValue());
    }
}