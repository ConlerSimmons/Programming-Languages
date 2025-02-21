import java.util.ArrayList;

/** 
 * Handles expression parsing and evaluation in the SILLY language.
 * Supports arithmetic operations (+, *, /), boolean logic (and, or, not),
 * comparisons (==, !=, <, >, <=, >=), and sequence operations (len, get, cat).
 * 
 * Expression Types:
 * - Simple: literals (numbers, booleans, chars) and identifiers
 * - Compound: function calls with arguments
 * - Lists: sequences of expressions
 * 
 * @author Dave Reed (modified by Conler Simmons)
 * @version 2.1.0 [2025]
 * @see DataValue
 * @see Token
 */
public class Expression {
    // Expression components
    private Token tok;                    // Token for simple expressions
    private ArrayList<Expression> exprs;  // List of sub-expressions

    // Error message constants
    private static final String ERR_SYNTAX_ID = 
        "Oops! I was expecting an identifier or function name here, but got something else.";
    private static final String ERR_UNKNOWN_VALUE = 
        "Hmm... I don't recognize this value: ";
    private static final String ERR_UNDECLARED_VAR = 
        "Hold up! The variable '%s' hasn't been declared yet.";
    private static final String ERR_MATH_ARITY = 
        "Hey! Math operations need at least two values to work with.";
    private static final String ERR_EXPECT_NUMBER = 
        "Wait a second... I need a number here!";
    private static final String ERR_NOT_ARITY = 
        "The 'not' operator is feeling lonely - it needs exactly one value to work with.";
    private static final String ERR_LOGICAL_ARITY = 
        "For 'and'/'or' to work, I need at least two values to compare.";
    private static final String ERR_EXPECT_BOOLEAN = 
        "I can only work with true/false values here!";
    private static final String ERR_COMPARISON_ARITY = 
        "I need something to compare here!";
    private static final String ERR_TYPE_MISMATCH = 
        "Uh-oh! Can't compare apples to oranges - types don't match.";
    private static final String ERR_SEQUENCE_ARITY = 
        "This sequence operation needs more values to work with.";
    private static final String ERR_EXPECT_LIST = 
        "I was expecting a list here, but got something else.";
    private static final String ERR_GET_ARITY = 
        "The 'get' operation needs exactly two things: a list and a position.";
    private static final String ERR_INDEX_TYPE = 
        "List positions must be numbers!";
    private static final String ERR_INDEX_INTEGER = 
        "List positions must be whole numbers - no decimals allowed!";
    private static final String ERR_INDEX_BOUNDS = 
        "Oops! That position is outside the list's boundaries.";
    private static final String ERR_CAT_ARITY = 
        "The 'cat' operation needs at least two lists to join.";
    private static final String ERR_CAT_TYPE = 
        "Sorry, I can only concatenate lists with other lists.";
    private static final String ERR_UNKNOWN_FORMAT = 
        "I'm not sure how to handle this expression format.";

    /**
     * Creates and validates an expression from the input stream.
     * Handles three types of expressions:
     * 1. Compound expressions starting with '(' followed by operator and arguments
     * 2. List expressions starting with '[' followed by elements
     * 3. Simple expressions (literals and identifiers)
     * 
     * @param input TokenStream containing expression tokens
     * @throws Exception with ERR_SYNTAX_ID if compound expression is malformed
     * @throws Exception with ERR_UNKNOWN_VALUE if token type is invalid
     */
    public Expression(TokenStream input) throws Exception {
        this.tok = input.next();
        if (this.tok.toString().equals("(")) {
            if (
                input.lookAhead().getType() != Token.Type.IDENTIFIER &&
                input.lookAhead().getType() != Token.Type.MATH_FUNC &&
                input.lookAhead().getType() != Token.Type.BOOL_FUNC &&
                input.lookAhead().getType() != Token.Type.SEQ_FUNC
            ) {
                throw new Exception(ERR_SYNTAX_ID);
            }
            this.tok = input.next();
            this.exprs = new ArrayList<Expression>();
            while (!input.lookAhead().toString().equals(")")) {
                this.exprs.add(new Expression(input));
            }
            input.next();
        } else if (this.tok.toString().equals("[")) {
            this.exprs = new ArrayList<Expression>();
            while (!input.lookAhead().toString().equals("]")) {
                this.exprs.add(new Expression(input));
            }
            input.next();
        } else if (
            this.tok.getType() != Token.Type.IDENTIFIER &&
            this.tok.getType() != Token.Type.NUM_LITERAL &&
            this.tok.getType() != Token.Type.BOOL_LITERAL &&
            this.tok.getType() != Token.Type.CHAR_LITERAL
        ) {
            throw new Exception(ERR_UNKNOWN_VALUE + this.tok + ").");
        }
    }

    /**
     * Evaluates the expression and produces a result value.
     * The evaluation process depends on the expression type:
     * - Simple expressions: return literal value or variable lookup
     * - List expressions: evaluate all elements and create ListValue
     * - Compound expressions: evaluate operator with arguments
     * 
     * Mathematical Operations:
     * - Require numeric operands
     * - Support +, *, / operations
     * 
     * Boolean Operations:
     * - not: unary operation on boolean
     * - and/or: binary+ operations on booleans
     * - Comparisons: ==, !=, <, >, <=, >= between same types
     * 
     * Sequence Operations:
     * - len: list length
     * - get: list element access
     * - cat: list concatenation
     * 
     * @return DataValue containing the result of evaluation
     * @throws Exception for type mismatches, invalid operations, or runtime errors
     */
    public DataValue evaluate() throws Exception {
        if (this.exprs == null) {
            if (this.tok.getType() == Token.Type.IDENTIFIER) {
                if (!Interpreter.MEMORY.isDeclared(this.tok)) {
                    throw new Exception(
                        String.format(ERR_UNDECLARED_VAR, this.tok)
                    );
                }
                return Interpreter.MEMORY.lookupValue(this.tok);
            } else if (this.tok.getType() == Token.Type.NUM_LITERAL) {
                return new NumberValue(Double.parseDouble(this.tok.toString()));
            } else if (this.tok.getType() == Token.Type.BOOL_LITERAL) {
                return new BooleanValue(Boolean.valueOf(this.tok.toString()));
            } else if (this.tok.getType() == Token.Type.CHAR_LITERAL) {
                return new CharValue(this.tok.toString().charAt(1));
            }
        } else if (this.tok.toString().equals("[")) {
            ArrayList<DataValue> vals = new ArrayList<DataValue>();
            for (Expression e : this.exprs) {
                vals.add(e.evaluate());
            }
            return new ListValue(vals);
        } else {
            if (this.tok.getType() == Token.Type.MATH_FUNC) {
                if (this.exprs.size() < 2) {
                    throw new Exception(ERR_MATH_ARITY);
                }
                DataValue first = this.exprs.get(0).evaluate();
                if (first.getType() != DataValue.Type.NUMBER) {
                    throw new Exception(ERR_EXPECT_NUMBER);
                }
                Double returnVal = (Double) first.getValue();
                for (int i = 1; i < this.exprs.size(); i++) {
                    DataValue val = this.exprs.get(i).evaluate();
                    if (val.getType() != DataValue.Type.NUMBER) {
                        throw new Exception(ERR_EXPECT_NUMBER);
                    }
                    if (this.tok.toString().equals("+")) {
                        returnVal = returnVal + ((Double) val.getValue());
                    } else if (this.tok.toString().equals("*")) {
                        returnVal = returnVal * ((Double) val.getValue());
                    } else if (this.tok.toString().equals("/")) {
                        returnVal = returnVal / ((Double) val.getValue());
                    }
                }
                return new NumberValue(returnVal);
            } else if (this.tok.getType() == Token.Type.BOOL_FUNC) {
                if (this.tok.toString().equals("not")) {
                    if (this.exprs.size() != 1) {
                        throw new Exception(ERR_NOT_ARITY);
                    } else {
                        DataValue val = this.exprs.get(0).evaluate();
                        if (val.getType() != DataValue.Type.BOOLEAN) {
                            throw new Exception(ERR_EXPECT_BOOLEAN);
                        } else {
                            return new BooleanValue(
                                !((Boolean) val.getValue())
                            );
                        }
                    }
                } else if (
                    this.tok.toString().equals("and") ||
                    this.tok.toString().equals("or")
                ) {
                    if (this.exprs.size() < 2) {
                        throw new Exception(ERR_LOGICAL_ARITY);
                    } else {
                        for (int i = 0; i < this.exprs.size(); i++) {
                            DataValue val = this.exprs.get(i).evaluate();
                            if (val.getType() != DataValue.Type.BOOLEAN) {
                                throw new Exception(ERR_EXPECT_BOOLEAN);
                            }
                            if (this.tok.toString().equals("and")) {
                                if ((Boolean) val.getValue() == false) {
                                    return new BooleanValue(false);
                                }
                            } else if (this.tok.toString().equals("or")) {
                                if ((Boolean) val.getValue() == true) {
                                    return new BooleanValue(true);
                                }
                            }
                        }
                        if (this.tok.toString().equals("and")) {
                            return new BooleanValue(true);
                        } else {
                            return new BooleanValue(false);
                        }
                    }
                } else {
                    if (this.exprs.size() < 1) {
                        throw new Exception(ERR_COMPARISON_ARITY);
                    }
                    for (int i = 0; i < this.exprs.size() - 1; i++) {
                        DataValue val1 = this.exprs.get(i).evaluate();
                        DataValue val2 = this.exprs.get(i + 1).evaluate();
                        if (val1.getType() != val2.getType()) {
                            throw new Exception(ERR_TYPE_MISMATCH);
                        }
                        if (
                            (this.tok.toString().equals("==") &&
                                val1.compareTo(val2) != 0) ||
                            (this.tok.toString().equals("!=") &&
                                val1.compareTo(val2) == 0) ||
                            (this.tok.toString().equals("<") &&
                                val1.compareTo(val2) >= 0) ||
                            (this.tok.toString().equals(">") &&
                                val1.compareTo(val2) <= 0) ||
                            (this.tok.toString().equals("<=") &&
                                val1.compareTo(val2) > 0) ||
                            (this.tok.toString().equals(">=") &&
                                val1.compareTo(val2) < 0)
                        ) {
                            return new BooleanValue(false);
                        }
                    }
                    return new BooleanValue(true);
                }
            } else if (this.tok.getType() == Token.Type.SEQ_FUNC) {
                if (this.exprs.size() == 0) {
                    throw new Exception(ERR_SEQUENCE_ARITY);
                }
                DataValue first = this.exprs.get(0).evaluate();
                if (first.getType() != DataValue.Type.LIST) {
                    throw new Exception(ERR_EXPECT_LIST);
                }
                ArrayList<DataValue> list = (ArrayList<
                        DataValue
                    >) first.getValue();

                if (this.tok.toString().equals("len")) {
                    if (this.exprs.size() != 1) {
                        throw new Exception(ERR_SEQUENCE_ARITY);
                    }
                    return new NumberValue(list.size());
                } else if (this.tok.toString().equals("get")) {
                    if (this.exprs.size() != 2) {
                        throw new Exception(ERR_GET_ARITY);
                    }
                    DataValue second = this.exprs.get(1).evaluate();
                    if (second.getType() != DataValue.Type.NUMBER) {
                        throw new Exception(ERR_INDEX_TYPE);
                    }
                    double dub = (Double) second.getValue();
                    if (dub != Math.round(dub)) {
                        throw new Exception(ERR_INDEX_INTEGER);
                    }
                    int index = (int) dub;
                    if (index < 0 || index >= list.size()) {
                        throw new Exception(ERR_INDEX_BOUNDS);
                    }
                    return list.get(index);
                } else if (this.tok.toString().equals("cat")) {
                    if (this.exprs.size() < 2) {
                        throw new Exception(ERR_CAT_ARITY);
                    }
                    for (int i = 1; i < this.exprs.size(); i++) {
                        DataValue val = this.exprs.get(i).evaluate();
                        if (val.getType() != DataValue.Type.LIST) {
                            throw new Exception(ERR_CAT_TYPE);
                        }
                        list.addAll((ArrayList<DataValue>) val.getValue());
                    }
                    return new ListValue(list);
                }
            }
        }
        throw new Exception(ERR_UNKNOWN_FORMAT);
    }

    /**
     * Creates a string representation of the expression.
     * Format varies by expression type:
     * - Simple: token string
     * - List: "[elem1 elem2 ...]"
     * - Compound: "(operator arg1 arg2 ...)"
     * 
     * Examples:
     * - Number: "42"
     * - List: "[1 2 3]"
     * - Addition: "(+ 1 2)"
     * 
     * @return formatted string showing expression structure
     */
    public String toString() {
        if (this.exprs == null) {
            return this.tok.toString();
        } else if (this.tok.toString().equals("[")) {
            String message = "[";
            for (Expression e : this.exprs) {
                message += e + " ";
            }
            return message.trim() + "]";
        } else {
            String message = "(" + this.tok;
            for (Expression e : this.exprs) {
                message = message + " " + e;
            }
            return message + ")";
        }
    }
}