public class Stone {
    public int value;
    public Stone next;

    public Stone(int value, Stone next) {
        this.value = value;
        this.next = next;
    }

    //Processes one blink for this stone, returns true if it split, false otherwise
    public boolean ProcessBlink() {
        //Calculate the number of digits
        int numDigits = (int) (Math.log10(value) + 1);

        //Should we split
        if(numDigits % 2 == 0) {
            //Split, get the left and right half of the digits
            int div = (int) Math.pow(10, numDigits/2);
            int rightVal = value % div;
            int leftVal = (value - rightVal)/div;

            //Create the new stone to go on the right of this one
            //Make sure it points at the next stone
            Stone newStone = new Stone(rightVal, this.next);

            //Point us at the new stone, and set our value to left
            this.next = newStone;
            this.value = leftVal;
            return true; //Return true (we split)
        } else {
            //We have an odd number of digits
            this.value *= 2024;
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
