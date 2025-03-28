import java.util.HashMap;
import java.util.Scanner;

/**
 * Driver for the interactive SILLY Interpreter.
 * 
 * @author Dave Reed
 * @version 1/20/25
 */
public class Interpreter {

    public static MemorySpace MEMORY = new MemorySpace();
    private static HashMap<String, FunctionDecl> functions = new HashMap<>();

    /**
     * Registers a function declaration in the interpreter.
     * 
     * @param name     The name of the function
     * @param function The function declaration
     */
    public static void registerFunction(String name, FunctionDecl function) {
        functions.put(name, function);
    }

    /**
     * Gets a function declaration by name.
     * 
     * @param name The name of the function
     * @return The function declaration, or null if not found
     */
    public static FunctionDecl getFunction(String name) {
        return functions.get(name);
    }

    /**
     * Checks if a function exists with the given name.
     * 
     * @param name The name to check
     * @return true if a function with the given name exists, false otherwise
     */
    public static boolean functionExists(String name) {
        return functions.containsKey(name);
    }

    public static void main(String[] args) throws Exception {

        System.out.print(
                "Enter the program file name or hit RETURN for interactive: ");
        Scanner input = new Scanner(System.in);
        String response = input.nextLine().strip();

        TokenStream inStream = new TokenStream();
        if (!response.equals("")) {
            inStream = new TokenStream(response);
        } else {
        }

        while (response.equals("") || inStream.hasNext()) {
            System.out.print(">>> ");
            Statement stmt = Statement.getStatement(inStream);
            if (!response.equals("")) {
                System.out.println(stmt);
            }

            // as with other try-catch blocks dealing with return values, if a return
            // statement is reached, the current scope is closed and the return exception is
            // rethrown. see compond.java if this is unclear
            try {
                stmt.execute();
            } catch (Return.ReturnException re) {
                System.out.println("ERROR: Return statement outside of function");
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        input.close();
    }
}