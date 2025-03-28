import java.util.ArrayList;
import java.util.List;


/**
 * Implementation of string functionality for the SILLY programming language.
 * Extends ListValue to treat strings as sequences of characters while maintaining
 * string-specific behavior. Provides methods for string creation, comparison,
 * and conversion operations.
 * 
 * @see DataValue for the core interface
 * @see CharValue for individual character handling
 * @version 2.7.0 [2024]
 * @author Conler Simmons
 */
public class StringValue extends ListValue {


    // ========== Constructors ==========
    
    /**
     * Initializes an empty string.
     * Creates a new string with no characters.
     */
    public StringValue() {
        super();
    }


    /**
     * Creates a new string with the specified content.
     * Converts the input string into a sequence of SILLY character values.
     * 
     * @param str the source string to initialize with
     */
    public StringValue(String str) {
        super(createCharValueList(str));
    }


    // ========== Helper Methods ==========
    
    /**
     * Utility method for string initialization.
     * Converts a Java String into SILLY's internal character representation.
     * 
     * @param str the string to convert
     * @return list of CharValue objects representing the string
     */
    private static ArrayList<DataValue> createCharValueList(String str) {
        ArrayList<DataValue> charValues = new ArrayList<>();
        
        for (char c : str.toCharArray()) {
            charValues.add(new CharValue(c));
        }
        
        return charValues;
    }


    // ========== Interface Implementations ==========
    
    /**
     * {@inheritDoc}
     * Always returns STRING type to ensure proper type checking.
     */
    @Override
    public DataValue.Type getType() {
        return DataValue.Type.STRING;
    }


    /**
     * {@inheritDoc}
     * Provides string representation without additional formatting.
     * Unlike ListValue, does not add spaces between characters.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        List<DataValue> value = (List<DataValue>) super.getValue();
        
        for (DataValue v : value) {
            result.append(v.toString());
        }
        
        return result.toString();
    }


    /**
     * {@inheritDoc}
     * Compares strings lexicographically based on their character sequences.
     * 
     * @param other the string to compare against
     * @return negative if less than, zero if equal, positive if greater than
     */
    @Override
    public int compareTo(DataValue other) {
        return this.toString().compareTo(other.toString());
    }

}
