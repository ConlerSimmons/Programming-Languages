import java.util.List;
import java.util.ArrayList;

/**
 * List value implementation for the SILLY language.
 * Provides storage and operations for ordered collections of DataValue objects.
 * Supports basic list operations and maintains type safety through the DataValue interface.
 * Lists can contain mixed types of values (numbers, booleans, chars, strings, or other lists).
 * 
 * @see DataValue
 * @version 2.7.0 [2024]
 * @author Dave Reed (modified by Conler Simmons)
 */
public class ListValue implements DataValue {
    // Fields
    protected final List<DataValue> value;
    
    // Constructors
    public ListValue() { 
        this.value = new ArrayList<>(); 
    }
    
    public ListValue(ArrayList<DataValue> vals) {
        this();
        value.addAll(vals);
    }
    
    // Interface implementations
    @Override public Object getValue() { 
        return this.value; 
    }
    
    @Override public DataValue.Type getType() { 
        return DataValue.Type.LIST; 
    }
    
    @Override public int compareTo(DataValue other) { 
        return getValue().toString().compareTo(other.getValue().toString()); 
    }
    
    // String formatting methods
    @Override public String toString() {
        return formatListContents();
    }
    
    private String formatListContents() {
        StringBuilder sb = new StringBuilder("[");
        for (DataValue v : value) {
            sb.append(formatValue(v)).append(" ");
        }
        return sb.toString().trim() + "]";
    }
    
    private String formatValue(DataValue v) {
        if (v.getType() == DataValue.Type.STRING) return "\"" + v + "\"";
        if (v.getType() == DataValue.Type.CHAR) return "'" + v + "'";
        return v.toString();
    }
}