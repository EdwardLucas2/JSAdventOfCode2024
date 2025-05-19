import java.util.HashMap;

public class Stone {
    public long value;
    public Stone next;

    public Stone(long value, Stone next) {
        this.value = value;
        this.next = next;
    }

    //Processes one blink for this stone, returns true if it split, false otherwise
    public boolean ProcessBlink() {
        //Calculate the number of digits
        long numDigits = (long) Math.floor(Math.log10(value)) + 1;

        if(value == 0)
            numDigits = 1;

        //System.out.println("Processing value: " + this.value + ", num digits: " + numDigits);

        if(value == 0) {
            this.value = 1;
            return false;
        } else if(numDigits % 2 == 0) {
            //Split, get the left and right half of the digits
            long div = (long) Math.pow(10, numDigits/2f);
            long rightVal = value % div;
            long leftVal = (value - rightVal)/div;

            //Create the new stone to go on the right of this one
            //Make sure it points at the next stone
            Stone newStone = new Stone(rightVal, this.next);

            //Point us at the new stone, and set our value to left
            this.next = newStone;
            this.value = leftVal;
            return true;
        } else {
            //We have an odd number of digits
            this.value = this.value * (long)2024;
            return false;
        }
    }

    public void toString(StringBuilder sb) {
        sb.append(value).append(", ");
        if(next != null) {
            next.toString(sb);
        }
    }
}
