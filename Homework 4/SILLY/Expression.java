import java.util.ArrayList;

/**
 * Class that represents an expression in the SILLY language.
 * 
 * @author Dave Reed & Owen McGrath
 * @version 1/20/25, updated 3/10/25ƒ
 */
public class Expression {

    private Token tok;
    private ArrayList<Expression> exprs;

    /**
     * Creates an expression from the specified TokenStream.
     * 
     * @param input the TokenStream from which the program is read
     */
    public Expression(TokenStream input) throws Exception {
        this.tok = input.next();
        if (this.tok.toString().equals("(")) {
            if (input.lookAhead().getType() != Token.Type.IDENTIFIER &&
                    input.lookAhead().getType() != Token.Type.MATH_FUNC &&
                    input.lookAhead().getType() != Token.Type.BOOL_FUNC &&
                    input.lookAhead().getType() != Token.Type.SEQ_FUNC) {
                throw new Exception(
                        "SYNTAX ERROR: Identifier or function expected in expression.");
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
        } else if (this.tok.getType() != Token.Type.IDENTIFIER &&
                this.tok.getType() != Token.Type.NUM_LITERAL &&
                this.tok.getType() != Token.Type.BOOL_LITERAL &&
                this.tok.getType() != Token.Type.CHAR_LITERAL &&
                this.tok.getType() != Token.Type.STR_LITERAL) {
            throw new Exception(
                    "SYNTAX ERROR: Unknown value (" + this.tok + ").");
        }
    }

    /**
     * Evaluates the current expression.
     * 
     * @return the value represented by the expression
     */
    // this is just to get rid of those pesky "warnings" that java gives you it has
    // no effect on the code
    @SuppressWarnings("unchecked")
    public DataValue evaluate() throws Exception {
        if (this.exprs == null) {
            if (this.tok.getType() == Token.Type.IDENTIFIER) {
                if (!Interpreter.MEMORY.isDeclared(this.tok)) {
                    throw new Exception(
                            "RUNTIME ERROR: variable " +
                                    this.tok +
                                    " is undeclared.");
                }
                return Interpreter.MEMORY.lookupValue(this.tok);
            } else if (this.tok.getType() == Token.Type.NUM_LITERAL) {
                return new NumberValue(Double.parseDouble(this.tok.toString()));
            } else if (this.tok.getType() == Token.Type.BOOL_LITERAL) {
                return new BooleanValue(Boolean.valueOf(this.tok.toString()));
            } else if (this.tok.getType() == Token.Type.CHAR_LITERAL) {
                return new CharValue(this.tok.toString().charAt(1));
            } else if (this.tok.getType() == Token.Type.STR_LITERAL) {
                String str = this.tok.toString();
                return new StringValue(str.substring(1, str.length() - 1));
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
                    throw new Exception(
                            "RUNTIME ERROR: Incorrect arity in math expression.");
                }
                DataValue first = this.exprs.get(0).evaluate();
                if (first.getType() != DataValue.Type.NUMBER) {
                    throw new Exception(
                            "RUNTIME ERROR: Number value expected.");
                }
                Double returnVal = (Double) first.getValue();
                for (int i = 1; i < this.exprs.size(); i++) {
                    DataValue val = this.exprs.get(i).evaluate();
                    if (val.getType() != DataValue.Type.NUMBER) {
                        throw new Exception(
                                "RUNTIME ERROR: Number value expected.");
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
                                "RUNTIME ERROR: The `not` operator requires one expression.");
                    } else {
                        DataValue val = this.exprs.get(0).evaluate();
                        if (val.getType() != DataValue.Type.BOOLEAN) {
                            throw new Exception(
                                    "RUNTIME ERROR: Boolean value expected.");
                        } else {
                            return new BooleanValue(
                                    !((Boolean) val.getValue()));
                        }
                    }
                } else if (this.tok.toString().equals("and") ||
                        this.tok.toString().equals("or")) {
                    if (this.exprs.size() < 2) {
                        throw new Exception(
                                "RUNTIME ERROR: The number of arguments in an `and` or `or` expression must be greater than or equal to two.");
                    } else {
                        for (int i = 0; i < this.exprs.size(); i++) {
                            DataValue val = this.exprs.get(i).evaluate();
                            if (val.getType() != DataValue.Type.BOOLEAN) {
                                throw new Exception(
                                        "RUNTIME ERROR: Boolean value expected.");
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
                                "RUNTIME ERROR: Incorrect arity in comparison expression.");
                    }
                    for (int i = 0; i < this.exprs.size() - 1; i++) {
                        DataValue val1 = this.exprs.get(i).evaluate();
                        DataValue val2 = this.exprs.get(i + 1).evaluate();
                        if (val1.getType() != val2.getType()) {
                            throw new Exception(
                                    "RUNTIME ERROR: Type mismatch in comparison.");
                        }
                        if ((this.tok.toString().equals("==") &&
                                val1.compareTo(val2) != 0) ||
                                (this.tok.toString().equals("!=") &&
                                        val1.compareTo(val2) == 0)
                                ||
                                (this.tok.toString().equals("<") &&
                                        val1.compareTo(val2) >= 0)
                                ||
                                (this.tok.toString().equals(">") &&
                                        val1.compareTo(val2) <= 0)
                                ||
                                (this.tok.toString().equals("<=") &&
                                        val1.compareTo(val2) > 0)
                                ||
                                (this.tok.toString().equals(">=") &&
                                        val1.compareTo(val2) < 0)) {
                            return new BooleanValue(false);
                        }
                    }
                    return new BooleanValue(true);
                }
            } else if (this.tok.getType() == Token.Type.SEQ_FUNC) {
                if (this.exprs.size() == 0) {
                    throw new Exception(
                            "RUNTIME ERROR: Incorrect arity in sequence expression.");
                }

                if (this.tok.toString().equals("str")) {
                    if (this.exprs.size() != 1) {
                        throw new Exception(
                                "RUNTIME ERROR: Incorrect arity in str expression.");
                    }
                    DataValue val = this.exprs.get(0).evaluate();
                    return new StringValue(val.toString());
                }

                DataValue first = this.exprs.get(0).evaluate();
                if (first.getType() != DataValue.Type.LIST &&
                        first.getType() != DataValue.Type.STRING) {
                    throw new Exception(
                            "RUNTIME ERROR: List or String value expected.");
                }

                ArrayList<DataValue> list = new ArrayList<DataValue>();
                list.addAll((ArrayList<DataValue>) first.getValue());

                if (this.tok.toString().equals("len")) {
                    if (this.exprs.size() != 1) {
                        throw new Exception(
                                "RUNTIME ERROR: Incorrect arity in len expression.");
                    }
                    return new NumberValue(list.size());
                } else if (this.tok.toString().equals("get")) {
                    if (this.exprs.size() != 2) {
                        throw new Exception(
                                "RUNTIME ERROR: Incorrect arity in get expression.");
                    }
                    DataValue second = this.exprs.get(1).evaluate();
                    if (second.getType() != DataValue.Type.NUMBER) {
                        throw new Exception(
                                "RUNTIME ERROR: Number expected in get expression.");
                    }
                    double dub = (Double) second.getValue();
                    if (dub != Math.round(dub)) {
                        throw new Exception(
                                "RUNTIME ERROR: List index must be an integer.");
                    }
                    int index = (int) dub;
                    if (index < 0 || index >= list.size()) {
                        throw new Exception(
                                "RUNTIME ERROR: List index out of bounds.");
                    }
                    return list.get(index);
                } else if (this.tok.toString().equals("cat")) {
                    if (this.exprs.size() < 2) {
                        throw new Exception(
                                "RUNTIME ERROR: Incorrect arity in cat expression.");
                    }
                    boolean isString = (first.getType() == DataValue.Type.STRING);
                    for (int i = 1; i < this.exprs.size(); i++) {
                        DataValue val = this.exprs.get(i).evaluate();
                        if (val.getType() != DataValue.Type.LIST &&
                                val.getType() != DataValue.Type.STRING) {
                            throw new Exception(
                                    "RUNTIME ERROR: Type mismatch in cat expression.");
                        }
                        if ((val.getType() == DataValue.Type.STRING) != isString) {
                            throw new Exception(
                                    "RUNTIME ERROR: Type mismatch in cat expression.");
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
            } else if (this.tok.getType() == Token.Type.IDENTIFIER) {
                // check that ensures the idenitifier is a function and not a variable
                FunctionDecl function = Interpreter.getFunction(this.tok.toString());

                // this block performs checks to ensure that the function is declared, is a
                // function, and has the correct number of parameters
                if (function == null) {
                    throw new Exception("RUNTIME ERROR: Function '" + this.tok + "' is not declared");
                }

                ArrayList<Token> parameters = function.getParameters();

                if (parameters.size() != this.exprs.size()) {
                    throw new Exception("RUNTIME ERROR: Function '" + this.tok + "' expects " +
                            parameters.size() + " parameters, got " + this.exprs.size());
                }

                ArrayList<DataValue> evaluatedArgs = new ArrayList<>();
                DataValue returnValue = new BooleanValue(true);

                // code block that iterates over the arguments and evaluates them
                for (int i = 0; i < this.exprs.size(); i++) {
                    DataValue argValue = this.exprs.get(i).evaluate();
                    evaluatedArgs.add(argValue);
                }

                Interpreter.MEMORY.beginFunctionScope();

                // code block that iterats over the parameters and stores the evaluated
                // arguments
                for (int i = 0; i < parameters.size(); i++) {
                    Interpreter.MEMORY.declareVariable(parameters.get(i));
                    Interpreter.MEMORY.storeValue(parameters.get(i), evaluatedArgs.get(i));
                }

                // try-catch block that attempts to iterate over the statements in the compound
                // block if it is unable to do so, it will close the scope and rethrow the error
                // indicating that a return statement was reached. if no return statement is
                // reached a general purpose exception is thrown
                try {
                    function.getBody().execute();
                } catch (Return.ReturnException re) {
                    returnValue = re.getReturnValue();
                } catch (Exception e) {
                    Interpreter.MEMORY.endCurrentScope();
                    throw e;
                }

                Interpreter.MEMORY.endCurrentScope();
                return returnValue;
            }
        }
        throw new Exception("RUNTIME ERROR: Unknown expression format.");
    }

    /**
     * Converts the current expression into a String.
     * 
     * @return the String representation of this expression
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

    /**
     * Helper method to convert an ArrayList<DataValue> into a concatenated string.
     * 
     * @param list the list of DataValue objects
     * @return the concatenated string representation
     */
    private String convertListToString(ArrayList<DataValue> list) {
        StringBuilder sb = new StringBuilder();
        for (DataValue val : list) {
            sb.append(val.toString());
        }
        return sb.toString();
    }
}
