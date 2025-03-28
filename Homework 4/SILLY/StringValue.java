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
    private static ArrayList<DataValue> createCharValueList(String str) {
        ArrayList<DataValue> charValues = new ArrayList<>();
        for (char c : str.toCharArray()) {
            charValues.add(new CharValue(c));
        }
        return charValues;
    }

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