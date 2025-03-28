import java.util.ArrayList;

/**
 * Class that represents an expression in the SILLY language.
 * 
 * @author Dave Reed & Conler Simmons
 * @version 1/20/25, updated 3/27/25
 */
public class Expression {

    // =================================== Fields ===================================
    private Token tok;
    private ArrayList<Expression> exprs;


    // ================================ Core Methods ================================
    /**
     * Creates an expression by parsing from the token stream.
     * Handles parenthesized expressions, lists, and literals.
     *
     * @param input TokenStream to read from
     * @throws Exception if expression syntax is invalid
     */
    public Expression(TokenStream input) throws Exception {
        this.tok = input.next();
        
        if (this.tok.toString().equals("(")) {
            parseParenthesizedExpression(input);
        }
        else if (this.tok.toString().equals("[")) {
            parseListExpression(input);
        }
        else if (!isValidLiteral(this.tok)) {
            throw new Exception("SYNTAX ERROR: Unknown value (" + this.tok + ").");
        }
    }

    /**
     * Evaluates this expression to produce a DataValue.
     * Handles simple values, lists, and function calls.
     *
     * @return DataValue result of evaluation
     * @throws Exception if evaluation fails
     */
    public DataValue evaluate() throws Exception {
        if (this.exprs == null) {
            return evaluateSimpleExpression();
        }
        else if (this.tok.toString().equals("[")) {
            return evaluateListExpression();
        }
        else {
            return evaluateFunctionExpression();
        }
    }


    // ============================== Helper Methods ================================
    private void parseParenthesizedExpression(TokenStream input) throws Exception {
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
    }

    private void parseListExpression(TokenStream input) throws Exception {
        this.exprs = new ArrayList<Expression>();
        while (!input.lookAhead().toString().equals("]")) {
            this.exprs.add(new Expression(input));
        }
        input.next();
    }

    private boolean isValidLiteral(Token token) {
        return token.getType() == Token.Type.IDENTIFIER ||
               token.getType() == Token.Type.NUM_LITERAL ||
               token.getType() == Token.Type.BOOL_LITERAL ||
               token.getType() == Token.Type.CHAR_LITERAL ||
               token.getType() == Token.Type.STR_LITERAL;
    }

    private DataValue evaluateSimpleExpression() throws Exception {
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
        throw new Exception("RUNTIME ERROR: Unknown expression format.");
    }

    private DataValue evaluateListExpression() throws Exception {
        ArrayList<DataValue> vals = new ArrayList<DataValue>();
        for (Expression e : this.exprs) {
            vals.add(e.evaluate());
        }
        return new ListValue(vals);
    }

    private DataValue evaluateFunctionExpression() throws Exception {
        if (this.tok.getType() == Token.Type.MATH_FUNC) {
            return evaluateMathFunction();
        }
        else if (this.tok.getType() == Token.Type.BOOL_FUNC) {
            return evaluateBooleanFunction();
        }
        else if (this.tok.getType() == Token.Type.SEQ_FUNC) {
            return evaluateSequenceFunction();
        }
        else if (this.tok.getType() == Token.Type.IDENTIFIER) {
            return evaluateUserFunction();
        }
        throw new Exception("RUNTIME ERROR: Unknown expression format.");
    }

    /**
     * Evaluates math functions (+, *, /).
     * Validates all arguments are numbers.
     *
     * @return NumberValue containing result
     * @throws Exception if arity or types are invalid
     */
    private DataValue evaluateMathFunction() throws Exception {
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
    }

    /**
     * Evaluates boolean functions (and, or, not, comparisons).
     * Validates all arguments have correct types.
     *
     * @return BooleanValue containing result
     * @throws Exception if arity or types are invalid
     */
    private DataValue evaluateBooleanFunction() throws Exception {
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
    }

    private DataValue evaluateSequenceFunction() throws Exception {
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
        throw new Exception("RUNTIME ERROR: Unknown expression format.");
    }

    private DataValue evaluateUserFunction() throws Exception {
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

    // =========== Utility Methods ===========
    private String convertListToString(ArrayList<DataValue> list) {
        StringBuilder sb = new StringBuilder();
        for (DataValue val : list) {
            sb.append(val.toString());
        }
        return sb.toString();
    }

    public String toString() {
        if (this.exprs == null) {
            return this.tok.toString();
        }
        return this.tok.toString().equals("[") ? 
               formatListToString() : 
               formatFunctionToString();
    }

    private String formatListToString() {
        String message = "[";
        for (Expression e : this.exprs) {
            message += e + " ";
        }
        return message.trim() + "]";
    }

    private String formatFunctionToString() {
        String message = "(" + this.tok;
        for (Expression e : this.exprs) {
            message = message + " " + e;
        }
        return message + ")";
    }
}