import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println(SolveV2("input.txt", 75));
    }

    public static void SolveV1(String fn, int numBlinks) {
        Stone start = ParseFile(fn);

        StringBuilder sb = new StringBuilder();
        start.toString(sb);
        System.out.println(sb.toString());

        for(int i = 0; i < numBlinks; i++) {
            System.out.println("Step: " + i);
            Blink(start);
        }

        //Count number of stones
        int count = 0;
        Stone cur = start;
        while(cur != null) {
            count++;
            cur = cur.next;
        }
        System.out.println("Count: " + count);
    }

    public static Stone ParseFile(String fileName) {
        try {
            Scanner scanner = new Scanner(new File(fileName));

            if(!scanner.hasNextLine())
                return null;

            String[] stoneStrs = scanner.nextLine().split(" ");

            Stone start = new Stone(Integer.parseInt(stoneStrs[0]), null);
            Stone cur = start;

            for(int i = 1; i < stoneStrs.length; i++) {
                cur.next = new Stone(Integer.parseInt(stoneStrs[i]), null);
                cur = cur.next;
            }

            return start;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void Blink(Stone start) {
        Stone cur = start;

        while(cur != null) {
            boolean skipNext = cur.ProcessBlink();

            if(skipNext) {
                cur = cur.next.next;
            } else {
                cur = cur.next;
            }
        }
    }

    public static String StonesToString(Stone start) {
        StringBuilder sb = new StringBuilder();
        start.toString(sb);
        return sb.toString();
    }

    public static long SolveV2(String fileName, int numBlinks) {
        long[] stones = ParseFile2(fileName);

        long res = CalculateTotalNumStones(stones, numBlinks);

        return res;
    }

    public static long[] ParseFile2(String fileName) {
        try {
            Scanner scanner = new Scanner(new File(fileName));

            String[] stoneStrs = scanner.nextLine().split(" ");

            long[] stones = new long[stoneStrs.length];

            for(int i = 0; i < stoneStrs.length; i++) {
                stones[i] = Long.parseLong(stoneStrs[i]);
            }

            return stones;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static long CalculateTotalNumStones(long[] stones, int numBlinks) {
        //Init cache
        HashMap<Long, HashMap<Integer, Long>> cache = new HashMap<>();
        long totalNumStones = 0;

        for(int i = 0; i < stones.length; i++) {
            totalNumStones += ProcessBlinks(stones[i], numBlinks, cache);
        }

        return totalNumStones;
    }

    //Returns the number of stones a stone will return after a certain number of steps
    public static long ProcessBlinks(long value, int numSteps, HashMap<Long, HashMap<Integer, Long>> cache) {
        //Base case check
        if(numSteps == 0) {
            return 1;
        }

        //Not at base case - Query cache
        if(cache.containsKey(value)) {
            if(cache.get(value).containsKey(numSteps)) {
                return cache.get(value).get(numSteps);
            }
        }

        //Cache miss, prepare cache for this value
        if(!cache.containsKey(value)) {
            cache.put(value, new HashMap<>());
        }

        //Process this blink and recurse
        long numChildStones = 0;

        //Calculate the number of digits
        long numDigits = (long) Math.floor(Math.log10(value)) + 1;
        if(value == 0)
            numDigits = 1;

        if(value == 0) {
            long numChild = ProcessBlinks(1, numSteps - 1, cache);
            numChildStones += numChild;
        } else if(numDigits % 2 == 0) {
            //Split, get the left and right half of the digits
            long div = (long) Math.pow(10, numDigits/2f);
            long rightVal = value % div;
            long leftVal = (value - rightVal)/div;

            long leftNumChildStones = ProcessBlinks(leftVal, numSteps - 1, cache);
            long rightNumChildStones = ProcessBlinks(rightVal, numSteps - 1, cache);
            long totalNumChildStones = leftNumChildStones + rightNumChildStones;
            numChildStones += totalNumChildStones;
        } else {
            //We have an odd number of digits
            numChildStones += ProcessBlinks(value * (long)2024, numSteps - 1, cache);
        }

        //Add results to cache
        cache.get(value).put(numSteps, numChildStones);

        //Return result
        return numChildStones;
    }
}