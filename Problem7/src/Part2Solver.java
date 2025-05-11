import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Part2Solver {
    public static long Solve(String s, boolean concatEnabled) throws InvalidParameterException {
        //Get the target number with regex
        Pattern numPat = Pattern.compile("\\d+");

        Stack<Integer> elements = new Stack<>();
        long target;

        Matcher numMat = numPat.matcher(s);

        if(numMat.find()) {
            target = Long.parseLong(numMat.group());
        } else {
            throw new InvalidParameterException("String is not valid: " + s);
        }

        while(numMat.find()) {
            elements.push(Integer.parseInt(numMat.group()));
        }

        if(Solvable(elements, target, concatEnabled)) {
            return target;
        } else {
            return 0;
        }
    }

    //Given a string of elements and a current value, return if it's possible to solve the equation
    public static boolean Solvable(Stack<Integer> elements, long curVal, boolean concatEnabled) {
        //System.out.println(elements.toString() + ". Current val: " + curVal);

        //Check for base-case
        if(elements.size() == 1) {
            return elements.pop() == curVal; //Base case, return if remaining element matches curVal (we can reach 0)
        } else if(curVal < 0) {
            return false;
        }

        //Check if divide is possible
        if(curVal % elements.peek() == 0) {
            //Division is possible, try it, clone the elements list, calculate
            Stack<Integer> newElements = (Stack<Integer>) elements.clone();
            //Calculate new cur val
            long newCurVal = curVal / newElements.pop();

            boolean result = Solvable(newElements, newCurVal, concatEnabled);
            if(result) {
                return true;
            }
        }
        //Divide not possible, or doesn't result in positive answer, try a -
        Stack<Integer> addElements = (Stack<Integer>) elements.clone();
        long newCurVal = curVal - addElements.pop();
        //Recurse
        boolean result = Solvable(addElements, newCurVal, concatEnabled);
        //If result found, return true
        if(result) {
            return true;
        }

        //- didn't work, is concat enabled
        if(concatEnabled) {
            //Concat is enabled, do the digits of the current element match the last digits of cur val
            Stack<Integer> ccElements = (Stack<Integer>) elements.clone();
            //Get current element
            int endEl = ccElements.pop();
            //Get number of digits of current element
            int numDigits = (int) Math.log10(endEl)+1;
            //Get the last numDigits of curVal
            int divisor = (int) Math.pow(10, numDigits);
            int lastDigits = (int) (curVal % divisor);
            //Do they match
            if(endEl == lastDigits) {
                //They do, what is the new cur val, with those last digits removed
                newCurVal = curVal / divisor;
                return Solvable(ccElements, newCurVal, true);
            } else {
                return false; //They don't, this won't work
            }
        } else {
            //Concat not enabled, return false
            return false;
        }
    }
}
