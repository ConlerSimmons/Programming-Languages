/** 
 * CharValue class represents and manages character data in the SILLY language.
 * Provides storage and basic operations for single character values.
 * 
 * @version 2.1.0 [2025]
 */
public class CharValue implements DataValue {    
    /** The character value being stored */
    private final Character value;
    
    /** 
     * Creates a CharValue with default space character.
     * This constructor initializes the value to a single space.
     */
    public CharValue() { this(' '); }
    
    /** 
     * Creates a CharValue with the specified character.
     * 
     * @param c the character to store in this CharValue
     */
    public CharValue(char c) { this.value = c; }
    
    /** 
     * Retrieves the stored character value as an Object.
     * 
     * @return the Character object containing the stored value
     */
    @Override public Object getValue() { return this.value; }
    
    /** 
     * Identifies this value's type in the SILLY type system.
     * 
     * @return DataValue.Type.CHAR indicating this is a character value
     */
    @Override public DataValue.Type getType() { return DataValue.Type.CHAR; }
    
    /** 
     * Creates a string representation of the character value.
     * The character is surrounded by single quotes.
     * 
     * @return string form of the character, e.g., 'a'
     */
    @Override public String toString() { return "'" + this.value + "'"; }
    
    /** 
     * Compares this character value with another DataValue.
     * 
     * @param other the DataValue to compare with
     * @return negative if less than, 0 if equal, positive if greater than
     * @throws IllegalArgumentException if other is not a CHAR type
     */
    @Override
    public int compareTo(DataValue other) {
        if (other.getType() != DataValue.Type.CHAR) {
            throw new IllegalArgumentException("Type Error: Expected CHAR");
        }
        return this.value.compareTo((Character) other.getValue());
    }
}