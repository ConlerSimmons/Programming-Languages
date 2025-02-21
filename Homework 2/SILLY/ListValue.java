import java.util.List;
import java.util.ArrayList;

/** 
 * List container implementation
 * @author Dave Reed (modified by Conler Simmons)
 * @version 2.1.0 [Feb 2025]
 */
public class ListValue implements DataValue {
    private final List<DataValue> value;  // List storage

    // Constructors
    public ListValue() {
        this.value = new ArrayList<>();
    }
    
    public ListValue(ArrayList<DataValue> vals) {
        this();
        this.value.addAll(vals);
    }
    
    // Interface implementations
    @Override public Object getValue()         { return this.value; }
    @Override public DataValue.Type getType()  { return DataValue.Type.LIST; }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (DataValue v : this.value) {
            sb.append(v).append(" ");
        }
        return sb.toString().trim() + "]";
    }
    
    @Override
    public int compareTo(DataValue other) {
        return this.getValue().toString().compareTo(other.getValue().toString());
    }
}