/** 
 * Core data type interface for SILLY language values
 * @author Dave Reed (modified by Conler Simmons)
 * @version 2/7/24
 */
public interface DataValue extends Comparable<DataValue> {
    // Type enumeration
    enum Type {
        NUMBER("numeric"),
        BOOLEAN("boolean"),
        LIST("list"),
        CHAR("character"),
        STRING("string");
        
        private final String description;
        Type(String description) { this.description = description; }
        @Override public String toString() { return description; }
    }

    // Core methods
    Object getValue();
    Type getType();
    
    // Object overrides
    @Override String toString();
    @Override int compareTo(DataValue other);
}