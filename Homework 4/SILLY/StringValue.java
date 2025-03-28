import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents a string value.
 * 
 * @author Conler Simmons
 * @version 2/07/25
 */
public class StringValue extends ListValue {

    // =========================== Interface Methods ===========================
    @Override
    public DataValue.Type getType()       { return DataValue.Type.STRING; }

    @Override
    public int compareTo(DataValue other) {
        return this.toString().compareTo(other.toString());
    }


    // ============================== Constructors ============================
    public StringValue()                  { super(); }
    public StringValue(String str)        { super(createCharValueList(str)); }


    // ============================= Core Methods ============================
    /**
     * Converts a string into a list of CharValue objects.
     * Each character in the string becomes a CharValue in the list.
     *
     * @param str String to convert
     * @return ArrayList of CharValues
     */
    private static ArrayList<DataValue> createCharValueList(String str) {
        ArrayList<DataValue> charValues = new ArrayList<>();
        for (char c : str.toCharArray()) {
            charValues.add(new CharValue(c));
        }
        return charValues;
    }

    /**
     * Converts the string value to its string representation.
     * Concatenates all characters without any delimiters.
     *
     * @return String representation of the value
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
}