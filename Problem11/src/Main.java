import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Stone start = ParseFile("input.txt");

        StringBuilder sb = new StringBuilder();
        start.toString(sb);
        System.out.println(sb.toString());

        int numBlinks = 75;

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
}