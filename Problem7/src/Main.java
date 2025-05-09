import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ArrayList<Equation> equations = ParseFile("test.txt", false);

        for (Equation equation : equations) {
            System.out.println(equation.ToString());
        }
    }

    //Given a file name, reads the file, and assumes each line is an equation, and tries to parse it
    //Returns a list of equation objects
    public static ArrayList<Equation> ParseFile(String fn, boolean LTR) {
        try {
            Scanner scanner = new Scanner(new File(fn));
            ArrayList<Equation> equations = new ArrayList<>();

            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Equation equation = new Equation(line, LTR);
                equations.add(equation);
            }

            return equations;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Recursive function that returns an equation with all the operators, so that it's cur val matches it's target value
    //Returns null if not possible
    public static Equation CompleteEquation(Equation eq) {
        //Base case, cur val matches or exceeds target val, or operators are full
        if(eq.curVal == eq.targetVal && eq.getOperators().size() == eq.elements.length - 1) {
            //System.out.println("Equation curVal is the same as the target equation, eq: " + eq.ToString());
            return eq; //Matches
        } else if(eq.curVal > eq.targetVal) {
            return null; //Exceeds (This assumes that all numbers in the equation are positive)
        } else if(eq.getOperators().size() >= eq.elements.length - 1) {
            return null; //Operators are already filled out
        }

        //Try adding a + operator, first clone the equation
        Equation newEq = new Equation(eq);
        newEq.AddOperator('+');
        newEq = CompleteEquation(newEq);

        if (newEq != null) {
            //We've done it, return
            return newEq;
        }

        //We haven't done it, add a * instead and return
        Equation timesEq = new Equation(eq);
        timesEq.AddOperator('*');
        return CompleteEquation(timesEq);
    }

    //This completes an equation recursively, but using right to left evaluation
    public static Equation CompleteEquationRTL(Equation eq) {
        //Base case check
        if(eq.curVal == eq.targetVal && eq.getOperators().size() == eq.elements.length - 1) {
            return eq; //Matches, and all operators added
        } else if(eq.curVal < eq.targetVal) {
            return null; //Current value smaller than target (RTL eval)
        } else if(eq.getOperators().size() >= eq.elements.length - 1) {
            return null; //Operators are already filled out
        }

        //Try adding a '*' operator, first check for remainder
        if(eq.curVal % eq.elements[eq.elements.length - eq.getOperators().size() - 1] == 0) {
            //No remainder, we can add a * operator
            Equation timesEq = new Equation(eq);
            timesEq.AddRTLOperator('*');
            //Recurse
            timesEq = CompleteEquationRTL(timesEq);
            if (timesEq != null)
                return timesEq; // Found solution
        }

        //Add a '+' operator
        Equation addEq = new Equation(eq);
        addEq.AddRTLOperator('+');
        return CompleteEquationRTL(addEq);
    }

    public static long GetSumOfValidEquations(ArrayList<Equation> equations) {
        //An equation is valid if it can possibly be true, calculate the sum of valid equations

        //Iterate over equations, summing valid ones
        long sum = 0;
        int count = 0;
        for(Equation equation : equations) {
            Equation compEq = CompleteEquation(equation);
            if(compEq != null) {
                sum = Math.addExact(sum, compEq.targetVal);
                count++;
                System.out.println(compEq.ToString());
            }
        }

        System.out.println("Sum of valid equations: " + sum + ", valid equations: " + count + "/" + equations.size());

        return sum;
    }
}