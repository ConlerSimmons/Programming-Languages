/**
 * Interface that defines the data types for the SILLY language.
 * 
 * @author Dave Reed
 * @version 1/20/25
 */
public interface DataValue extends Comparable<DataValue> {
    // ========== Type Definition ==========
    enum Type {
        NUMBER(1),    BOOLEAN(2),    LIST(3),
        CHAR(4),      STRING(5),     FUNCTION(6);
        
        private final int code;
        Type(int code)      { this.code = code; }
        public int getCode() { return code; }
    }

    // ========== Core Methods ==========
    Object getValue();
    Type getType();
    String toString();
}