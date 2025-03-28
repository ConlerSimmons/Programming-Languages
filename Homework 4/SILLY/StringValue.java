import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents a string value.
 * 
 * @author Owen McGrath
 * @version 2/07/25
 */
public class StringValue extends ListValue {

    /**
     * Constructs a default string value.
     */
    public StringValue() {
        super();
    }

    /**
     * Constructs a string value.
     * 
     * @param str the string being stored
     */
    public StringValue(String str) {
        super(createCharValueList(str));
    }

    /**
     * Helper method to convert a string into a list of CharValue objects.
     * 
     * @param str the string being converted
     * @return a list of CharValue objects representing the characters in the string
     */
    private static ArrayList<DataValue> createCharValueList(String str) {
        ArrayList<DataValue> charValues = new ArrayList<>();
        for (char c : str.toCharArray()) {
            charValues.add(new CharValue(c));
        }
        return charValues;
    }

    /**
     * Accesses the stored string value.
     * 
     * @return the string value (as an Object)
     */
    @Override
    public DataValue.Type getType() {
        return DataValue.Type.STRING;
    }

    /**
     * Converts the string value to a String.
     * 
     * @return a String representation of the string value
     */
    @Override
    @SuppressWarnings("unchecked")
    public String toString() {
        StringBuilder result = new StringBuilder();
        List<DataValue> value = (List<DataValue>) super.getValue();
        for (DataValue v : value) {
            result.append(v.toString());
        }
        return result.toString();
    }

    /**
     * Comparison method for StringValues.
     * 
     * @param other the value being compared with
     */
    @Override
    public int compareTo(DataValue other) {
        return this.toString().compareTo(other.toString());
    }
}
