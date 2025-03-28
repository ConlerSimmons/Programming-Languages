/**
 * Interface that defines the data types for the SILLY language.
 * 
 * @author Dave Reed
 * @version 1/20/25, updated 3/27/25
 */
public interface DataValue extends Comparable<DataValue> {
    // ================================= Type System ================================
    enum Type {
        NUMBER(1),        BOOLEAN(2),        LIST(3),
        CHAR(4),          STRING(5),         FUNCTION(6);
        
        private final int code;
        
        Type(int code)           { this.code = code; }
        public int getCode()     { return code; }
    }

    // ============================== Core Operations ===============================
    
    /**
     * Retrieves the wrapped value of this data type.
     * 
     * @return The underlying value object
     */
    Object getValue();

    /**
     * Gets the SILLY type of this value.
     * 
     * @return One of the DataValue.Type enum values
     */
    Type getType();

    /**
     * Compares this value with another DataValue.
     * Used for implementing comparison operators.
     * 
     * @param other DataValue to compare with
     * @return negative if less, 0 if equal, positive if greater
     */
    int compareTo(DataValue other);

    String toString();
}