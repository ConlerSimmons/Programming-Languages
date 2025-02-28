import java.util.ArrayList;

/**
 * Expression class represents and evaluates expressions in the SILLY language.
 * Handles various types of expressions including:
 * - Simple values (numbers, booleans, strings, chars)
 * - Variable references
 * - List expressions
 * - Mathematical operations
 * - Boolean operations
 * - Sequence operations
 * 
 * @author Dave Reed (modified by Conler Simmons)
 * @version 2.7.0 [2024]
 * @see DataValue
 * @see Token
 */
public class Expression {
    /** The token representing this expression's operator or value */
    private Token tok;
    
    /** List of sub-expressions for compound expressions */
    private ArrayList<Expression> exprs;

    /**
     * Creates an expression from the token stream
     * @param input TokenStream containing expression tokens
     * @throws Exception if expression syntax is invalid
     */
    public Expression(TokenStream input) throws Exception {
        this.tok = input.next();
        if (isCompoundExpression()) {
            parseCompoundExpression(input);
        } else if (isListExpression()) {
            parseListExpression(input);
        } else {
            validateSimpleExpression();
        }
    }

    /**
     * Evaluates the expression and returns its value
     * @return DataValue representing the expression's value
     * @throws Exception if evaluation fails due to type mismatch or runtime error
     */
    public DataValue evaluate() throws Exception {
        if (exprs == null) {
            return evaluateSimpleExpression();
        } else if (tok.toString().equals("[")) {
            return evaluateListExpression();
        } else {
            return evaluateCompoundExpression();
        }
    }

    // Object overrides
    @Override
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

    // Helper methods
    /**
     * Checks if current token represents start of compound expression
     * @return true if expression starts with '('
     */
    private boolean isCompoundExpression() {
        return tok.toString().equals("(");
    }

    /**
     * Checks if current token represents start of list expression
     * @return true if expression starts with '['
     */
    private boolean isListExpression() {
        return tok.toString().equals("[");
    }

    /**
     * Parses a compound expression starting with '('
     * @param input TokenStream to parse from
     * @throws Exception if compound expression syntax is invalid
     */
    private void parseCompoundExpression(TokenStream input) throws Exception {
        if (
            input.lookAhead().getType() != Token.Type.IDENTIFIER &&
            input.lookAhead().getType() != Token.Type.MATH_FUNC &&
            input.lookAhead().getType() != Token.Type.BOOL_FUNC &&
            input.lookAhead().getType() != Token.Type.SEQ_FUNC
        ) {
            throw new Exception(
                "SYNTAX ERROR: Identifier or function expected in expression."
            );
        }
        this.tok = input.next();
        this.exprs = new ArrayList<Expression>();
        while (!input.lookAhead().toString().equals(")")) {
            this.exprs.add(new Expression(input));
        }
        input.next();
    }

    private void parseListExpression(TokenStream input) throws Exception {
        this.exprs = new ArrayList<Expression>();
        while (!input.lookAhead().toString().equals("]")) {
            this.exprs.add(new Expression(input));
        }
        input.next();
    }

    private void validateSimpleExpression() throws Exception {
        if (
            this.tok.getType() != Token.Type.IDENTIFIER &&
            this.tok.getType() != Token.Type.NUM_LITERAL &&
            this.tok.getType() != Token.Type.BOOL_LITERAL &&
            this.tok.getType() != Token.Type.CHAR_LITERAL &&
            this.tok.getType() != Token.Type.STR_LITERAL
        ) {
            throw new Exception(
                "SYNTAX ERROR: Unknown value (" + this.tok + ")."
            );
        }
    }

    private DataValue evaluateSimpleExpression() throws Exception {
        if (this.tok.getType() == Token.Type.IDENTIFIER) {
            if (!Interpreter.MEMORY.isDeclared(this.tok)) {
                throw new Exception(
                    "RUNTIME ERROR: variable " +
                    this.tok +
                    " is undeclared."
                );
            }
            return Interpreter.MEMORY.lookupValue(this.tok);
        } else if (this.tok.getType() == Token.Type.NUM_LITERAL) {
            return new NumberValue(Double.parseDouble(this.tok.toString()));
        } else if (this.tok.getType() == Token.Type.BOOL_LITERAL) {
            return new BooleanValue(Boolean.valueOf(this.tok.toString()));
        } else if (this.tok.getType() == Token.Type.CHAR_LITERAL) {
            return new CharValue(this.tok.toString().charAt(1));
        } else if (this.tok.getType() == Token.Type.STR_LITERAL) {
            String str = this.tok.toString(); // get the string
            return new StringValue(str.substring(1, str.length() - 1)); // remove quotes
        }
        throw new Exception("RUNTIME ERROR: Unknown simple expression format.");
    }

    private DataValue evaluateListExpression() throws Exception {
        ArrayList<DataValue> vals = new ArrayList<DataValue>();
        for (Expression e : this.exprs) {
            vals.add(e.evaluate());
        }
        return new ListValue(vals);
    }

    private DataValue evaluateCompoundExpression() throws Exception {
        if (this.tok.getType() == Token.Type.MATH_FUNC) {
            if (this.exprs.size() < 2) {
                throw new Exception(
                    "RUNTIME ERROR: Incorrect arity in math expression."
                );
            }
            DataValue first = this.exprs.get(0).evaluate();
            if (first.getType() != DataValue.Type.NUMBER) {
                throw new Exception(
                    "RUNTIME ERROR: Number value expected."
                );
            }
            Double returnVal = (Double) first.getValue();
            for (int i = 1; i < this.exprs.size(); i++) {
                DataValue val = this.exprs.get(i).evaluate();
                if (val.getType() != DataValue.Type.NUMBER) {
                    throw new Exception(
                        "RUNTIME ERROR: Number value expected."
                    );
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
                    throw new Exception(
                        "RUNTIME ERROR: The `not` operator requires one expression."
                    );
                } else {
                    DataValue val = this.exprs.get(0).evaluate();
                    if (val.getType() != DataValue.Type.BOOLEAN) {
                        throw new Exception(
                            "RUNTIME ERROR: Boolean value expected."
                        );
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
                    throw new Exception(
                        "RUNTIME ERROR: The number of arguments in an `and` or `or` expression must be greater than or equal to two."
                    );
                } else {
                    for (int i = 0; i < this.exprs.size(); i++) {
                        DataValue val = this.exprs.get(i).evaluate();
                        if (val.getType() != DataValue.Type.BOOLEAN) {
                            throw new Exception(
                                "RUNTIME ERROR: Boolean value expected."
                            );
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
                    throw new Exception(
                        "RUNTIME ERROR: Incorrect arity in comparison expression."
                    );
                }
                for (int i = 0; i < this.exprs.size() - 1; i++) {
                    DataValue val1 = this.exprs.get(i).evaluate();
                    DataValue val2 = this.exprs.get(i + 1).evaluate();
                    if (val1.getType() != val2.getType()) {
                        throw new Exception(
                            "RUNTIME ERROR: Type mismatch in comparison."
                        );
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
                throw new Exception(
                    "RUNTIME ERROR: Incorrect arity in sequence expression."
                );
            }

            if (this.tok.toString().equals("str")) {
                if (this.exprs.size() != 1) {
                    throw new Exception(
                        "RUNTIME ERROR: Incorrect arity in str expression."
                    );
                }
                DataValue val = this.exprs.get(0).evaluate();
                return new StringValue(val.toString());
            }

            DataValue first = this.exprs.get(0).evaluate();
            if (
                first.getType() != DataValue.Type.LIST &&
                first.getType() != DataValue.Type.STRING
            ) {
                throw new Exception(
                    "RUNTIME ERROR: List or String value expected."
                );
            }

            ArrayList<DataValue> list = (ArrayList<
                    DataValue
                >) first.getValue();

            if (this.tok.toString().equals("len")) {
                if (this.exprs.size() != 1) {
                    throw new Exception(
                        "RUNTIME ERROR: Incorrect arity in len expression."
                    );
                }
                return new NumberValue(list.size());
            } else if (this.tok.toString().equals("get")) {
                if (this.exprs.size() != 2) {
                    throw new Exception(
                        "RUNTIME ERROR: Incorrect arity in get expression."
                    );
                }
                DataValue second = this.exprs.get(1).evaluate();
                if (second.getType() != DataValue.Type.NUMBER) {
                    throw new Exception(
                        "RUNTIME ERROR: Number expected in get expression."
                    );
                }
                double dub = (Double) second.getValue();
                if (dub != Math.round(dub)) {
                    throw new Exception(
                        "RUNTIME ERROR: List index must be an integer."
                    );
                }
                int index = (int) dub;
                if (index < 0 || index >= list.size()) {
                    throw new Exception(
                        "RUNTIME ERROR: List index out of bounds."
                    );
                }
                return list.get(index);
            } else if (this.tok.toString().equals("cat")) {
                if (this.exprs.size() < 2) {
                    throw new Exception(
                        "RUNTIME ERROR: Incorrect arity in cat expression."
                    );
                }
                boolean isString =
                    (first.getType() == DataValue.Type.STRING);
                for (int i = 1; i < this.exprs.size(); i++) {
                    DataValue val = this.exprs.get(i).evaluate();
                    if (
                        val.getType() != DataValue.Type.LIST &&
                        val.getType() != DataValue.Type.STRING
                    ) {
                        throw new Exception(
                            "RUNTIME ERROR: Type mismatch in cat expression."
                        );
                    }
                    if (
                        (val.getType() == DataValue.Type.STRING) != isString
                    ) {
                        throw new Exception(
                            "RUNTIME ERROR: Type mismatch in cat expression."
                        );
                    }
                    list.addAll((ArrayList<DataValue>) val.getValue());
                }
                if (isString) {
                    String concatenatedString = convertListToString(list);
                    return new StringValue(concatenatedString);
                } else {
                    return new ListValue(list);
                }
            }
        }
        throw new Exception("RUNTIME ERROR: Unknown expression format.");
    }

    /**
     * Converts list of DataValues to concatenated string
     * @param list ArrayList of DataValues to concatenate
     * @return String representation of concatenated values
     */
    private String convertListToString(ArrayList<DataValue> list) {
        StringBuilder sb = new StringBuilder();
        for (DataValue val : list) {
            sb.append(val.toString());
        }
        return sb.toString();
    }
}