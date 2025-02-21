/** 
 * Core data type interface for SILLY language values
 * @author Dave Reed (modified by Conler Simmons)
 * @version 2.1.0 [Feb 2025]
 */
public interface DataValue extends Comparable<DataValue> {
    // Value types supported by SILLY
    public static enum Type { NUMBER, BOOLEAN, LIST, CHAR }

    // Core interface methods
    public Object getValue();
    public DataValue.Type getType();
    public String toString();
}