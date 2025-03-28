/**
 * Class that represents a character value.
 * 
 * @author Conler Simmons
 * @version 3/27/25
 */
public class CharValue implements DataValue {

    // ================================ Fields ================================
    private final Character charValue;   // renamed from value for clarity

    // ============================= Core Methods ============================
    @Override
    public Object getValue() { return this.charValue; }

    @Override
    public DataValue.Type getType() { return DataValue.Type.CHAR; }

    @Override
    public int compareTo(DataValue other) {
        return ((Character) this.getValue()).compareTo((Character) other.getValue());
    }

    @Override
    public String toString() { return "" + this.charValue; }

    // =========== Constructors ===========
    public CharValue() { this('_'); }  // default char is underscore

    public CharValue(char c) { this.charValue = c; }
}