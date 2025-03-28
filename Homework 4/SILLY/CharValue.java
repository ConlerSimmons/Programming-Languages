/** 
 * Character value implementation for the SILLY language.
 * Provides storage and operations for single character values.
 * Implements the DataValue interface for type safety and comparison operations.
 *
 * @author Dave Reed (modified by Conler Simmons)
 * @version 2.7.0 [2024]
 * @see DataValue
 */
public class CharValue implements DataValue {    
    // Field
    private final Character value;
    
    // Constructors
    public CharValue() { 
        this(' '); 
    }
    
    public CharValue(char c) { 
        this.value = c; 
    }
    
    // Interface implementations
    @Override public Object getValue() { 
        return this.value; 
    }
    
    @Override public DataValue.Type getType() { 
        return DataValue.Type.CHAR; 
    }
    
    @Override public String toString() { 
        return "'" + this.value + "'"; 
    }
    
    @Override
    public int compareTo(DataValue other) {
        if (other.getType() != DataValue.Type.CHAR) {
            throw new IllegalArgumentException("Type Error: Expected CHAR");
        }
        return this.value.compareTo((Character) other.getValue());
    }
}