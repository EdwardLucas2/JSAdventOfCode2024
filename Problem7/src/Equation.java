import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Equation {
    public final int[] elements;
    private ArrayList<Character> operators; //Element 0 is the operator between element index 0 and element at index 1, size will be elements.length - 1

    public long curVal;
    public long targetVal;

    public Equation(String s, boolean LTR) {
        //Parse a string into an equation object, first check if the string is valid
        //Compile validity regex pattern
        Pattern validPat = Pattern.compile("\\d+: (\\d+ )*\\d+");
        Matcher matcher = validPat.matcher(s);
        if(!matcher.matches()) {
            System.err.println("Error parsing equation string: " + s);
            this.elements = null;
            return;
        }

        //Patter is valid, compile regex pattern to fetch numbers from the string
        Pattern pat = Pattern.compile("\\d+");
        Matcher numMatch = pat.matcher(s);

        //Get the target val
        if (numMatch.find()) {
            if(LTR) {
                //If LTR, target is first num in string
                this.targetVal = Long.parseLong(numMatch.group());
            } else {
                //RTL, target is 0, current val is first num
                this.targetVal = 0;
                this.curVal = Long.parseLong(numMatch.group());
            }
        } else {
            System.err.println("Error parsing equation string: " + s);
            this.elements = null;
            return;
        }


        //Get all the next numbers (elements)
        ArrayList<Integer> elements = new ArrayList<>(); //Temp array to store them
        while(numMatch.find()) {
            elements.add(Integer.parseInt(numMatch.group()));
        }
        //Turn list into array
        int[] elementsArray = new int[elements.size()];
        for(int i = 0; i < elements.size(); i++) {
            elementsArray[i] = elements.get(i);
        }
        this.elements = elementsArray;
        if(LTR)
            this.curVal = this.elements[0];
        this.operators = new ArrayList<>();
    }

    public Equation(int[] elements, ArrayList<Character> operators, int targetVal) {
        this.elements = elements;
        this.operators = operators;
        this.targetVal = targetVal;
        this.curVal = elements[0];
    }

    public Equation(Equation equation) {
        this.elements = equation.elements;
        this.operators = new ArrayList<>();
        this.operators.addAll(equation.operators);
        this.targetVal = equation.targetVal;
        this.curVal = equation.curVal;
    }

    public ArrayList<Character> getOperators() {
        return operators;
    }

    public void AddOperator(char operator) {
        //Check size of operators
        if(operators.size() >= elements.length - 1) {
            System.err.println("Operator overflow, trying to add too many operators");
            return;
        }
        //Add op to list
        operators.add(operator);
        //Compute new cur val
        if(operator == '+') {
            curVal = Math.addExact(curVal, elements[operators.size()]);
        } else if(operator == '*') {
            curVal = Math.multiplyExact(curVal, elements[operators.size()]);
        } else {
            System.err.println("Trying to add invalid operator: " + operator);
            return;
        }
    }

    //Used for RTL evaluation, where the current value decreases
    public void AddRTLOperator(char operator) {

    }

    public String ToString() {
        StringBuilder result = new StringBuilder();
        result.append("Target: ").append(targetVal).append(" , Cur Value: ").append(curVal).append("\n");
        for (int i = 0; i < elements.length; i++) {
            // Append the element
            result.append(elements[i]);

            //Check if we need an operator (are we at the end)
            if(i >= elements.length - 1) {
                continue;
            }

            // Get the operator
            if (i >= operators.size()) {
                // No op
                result.append(" _ ");
            } else if (operators.get(i) == '+') {
                result.append(" + ");
            } else if (operators.get(i) == '*') {
                result.append(" * ");
            } else {
                System.err.println("Invalid operator int val: " + operators.get(i));
            }
        }
        return result.toString();
    }

    //This bit isn't ideal, when using RTL evaluation the operators in the array don't reference the same elements in LTR
    public String ToStringRTL() {
        StringBuilder result = new StringBuilder();
        result.append("Target: ").append(targetVal).append(" , Cur Value: ").append(curVal).append("\n");
        for (int i = 0; i < elements.length; i++) {
            // Append the element
            result.append(elements[i]);

            //3_3*4+5
            //Ops+,*
            //At element 0, opIdx is 2
            //Element len: 4, i: 0, op size: 2

            //At element 1, opIdx is 1
            //Element len: 4, i: 1, op size: 2

            //At element 2, opIdx is 0
            //Element len: 4, i: 2, op size: 2

            //At element 3: No op, at end

            //Get the operator index
            int opIdx = elements.length - 2 - i;

            //Check if we need/have an operator (are we at the end),
            if(i >= elements.length - 1) {
                continue;
            }

            // Get the operator
            if (opIdx >= operators.size()) {
                // No op
                result.append(" _ ");
            } else if (operators.get(opIdx) == '+') {
                result.append(" + ");
            } else if (operators.get(opIdx) == '*') {
                result.append(" * ");
            } else {
                System.err.println("Invalid operator int val: " + operators.get(opIdx));
            }
        }
        return result.toString();
    }
}
