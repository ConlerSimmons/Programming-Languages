import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents a list value.
 * 
 * @author Dave Reed
 * @version 1/20/25
 */
public class ListValue implements DataValue {
    // ================================ Fields ================================
    protected List<DataValue> value;

    // ============================= Core Methods ============================
    @Override
    public Object getValue()         { return this.value; }
    @Override
    public DataValue.Type getType()  { return DataValue.Type.LIST; }
    
    @Override
    public int compareTo(DataValue other) {
        return this.getValue().toString().compareTo(other.getValue().toString());
    }

    // ========== Constructors ==========
    public ListValue() {
        this.value = new ArrayList<>();
    }

    public ListValue(ArrayList<DataValue> vals) {
        this();
        this.value.addAll(vals);
    }

    // ========== Other Methods ==========
    @Override
    public String toString() {
        String message = "[";
        for (DataValue v : this.value) {
            if (v.getType() == DataValue.Type.STRING) {
                message += "\"" + v + "\" ";
            } else if (v.getType() == DataValue.Type.CHAR) {
                message += "'" + v + "' ";
            } else {
                message += v + " ";
            }
        }
        return message.trim() + "]";
    }
}