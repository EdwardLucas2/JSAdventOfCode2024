import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Stone start = ParseFile("test.txt");

        StringBuilder sb = new StringBuilder();
        start.toString(sb);
        System.out.println(sb.toString());
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
}