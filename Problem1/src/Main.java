import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();

        //Read list file into memory, time and space complexity O(n), n is list length
        ReadListsFromFile("Problem1/Problem1Input.csv", list1, list2);
        System.out.println("Read file, calculating total distance");
        int totalDistance = CalculateTotalDistance(list1, list2);
        System.out.println("Total distance: " + totalDistance);

        int totalSimScore  = CalculateSimilarityScore(list1, list2);
        System.out.println("Total similarity score: " + totalSimScore);
    }

    public static void ReadListsFromFile(String fp, List<Integer> list1, List<Integer> list2) {
        //Compile the regex pattern for getting the numbers
        Pattern pattern = Pattern.compile("[0-9]+");

        //Open the file
        File file = new File(fp);
        try {
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                //Match this line with the regex
                Matcher matcher = pattern.matcher(sc.nextLine());

                //Get the first match
                if(matcher.find()) {
                    list1.add(Integer.parseInt(matcher.group()));
                }

                //Get the second match
                if(matcher.find()) {
                    list2.add(Integer.parseInt(matcher.group()));
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found, path: " + fp);
            e.printStackTrace();
        }
    }

    public static int CalculateTotalDistance(List<Integer> list1, List<Integer> list2) {
        //Check that both lists are of the same length
        if(list1.size() != list2.size()) {
            System.err.println("Lists do not match length");
            return -1;
        }

        //Sort both lists O(n*logn) complexity, n is list length
        Collections.sort(list1);
        Collections.sort(list2);

        int totDis = 0;
        //Iterate over each pair of items in the lists
        for(int i = 0; i < list1.size(); i++) {
            totDis += Math.abs(list1.get(i) - list2.get(i));
        }

        return totDis;
    }

    //nlog(N) + O(N) complexity
    public static int CalculateSimilarityScore(List<Integer> list1, List<Integer> list2) {
        //Sort each list
        Collections.sort(list1);
        Collections.sort(list2);

        int simScore = 0;

        int leftPtr = 0, rightPtr = 0;
        int lastLeftNum = -1, lastNumOccur = 0;

        //O(N) complexity
        while(leftPtr < list1.size()) {
            int numOccur = 0;

            //Get the value of the left list
            int leftNum = list1.get(leftPtr);

            //Check if this left num is the same as the last one
            if(leftNum == lastLeftNum) {
                //It is
                numOccur = lastNumOccur;
            } else {
                //It's not, this left num is different (higher than the last one)
                //Update the last left num
                lastLeftNum = leftNum;

                //Increment right pointer up to values that are <= to left pointers values, checking for = numbers
                while (list2.get(rightPtr) <= leftNum && rightPtr < list2.size()-1) {
                    if(list2.get(rightPtr) == leftNum) {
                        numOccur++;
                    }
                    rightPtr++;
                }
            }

            //Calculate sim score
            int sim = leftNum * numOccur;
            simScore += sim;
            lastNumOccur = numOccur;

            //Debug message
            //System.out.println("Left num: " + leftNum +", Similarity score: " + sim);

            //Increment left ptr
            leftPtr++;
        }

        return simScore;
    }
}