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
        List<ArrayList<Integer>> reports = new ArrayList<>();

        ReadReportsFromFile("input.csv", reports);

        int numSafe = 0;

        for(ArrayList<Integer> rep : reports) {
            if(IsReportSafeWithDampenerBruteForce(rep)) {
                numSafe++;
            }
        }

        System.out.println("Number of safe reports: " + numSafe);
    }

    public static void ReadReportsFromFile(String fp, List<ArrayList<Integer>> reports) {
        //Compile the regex pattern for getting the numbers
        Pattern pattern = Pattern.compile("[0-9]+");

        //Open the file
        File file = new File(fp);
        try {
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                //Match this line with the regex
                Matcher matcher = pattern.matcher(sc.nextLine());
                ArrayList<Integer> level = new ArrayList<>();

                while (matcher.find()) {
                    level.add(Integer.parseInt(matcher.group()));
                }

                if(!level.isEmpty()) {
                    reports.add(level);
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found, path: " + fp);
            e.printStackTrace();
        }
    }

    public static boolean IsReportSafe(ArrayList<Integer> report) {
        //Check that the report is longer than 2
        if(report.size() < 2) {
            System.err.println("Report is invalid");
            return false;
        }

        boolean ascending;
        //Check first two reports to determine ascending/descending
        if(report.get(1) > report.get(0)) {
            ascending = true;
        } else {
            ascending = false;
        }

        //Print report to console
        /*
        for(int i = 0; i < report.size(); i++) {
            System.out.print(report.get(i) + ", ");
        }
        System.out.print("Ascending: " + ascending);
        */

        for(int i = 1; i < report.size(); i++) {
            //Calculate difference (always positive - takes into account ascending)
            int diff = 0;
            if(ascending) {
                diff = report.get(i) - report.get(i - 1);
            } else {
                diff = report.get(i - 1) - report.get(i);
            }

            if(diff < 1 || diff > 3) {
                //System.out.println(". Report is invalid");
                return false;
            }
        }

        //System.out.println(". Report is valid");
        return true;
    }

    //O(n * m) complexity, where n is the number of reports, and m is the average length of each report
    public static boolean IsReportSafeWithDampenerBruteForce(ArrayList<Integer> report) {
        //Check that the report is longer than 2
        if(report.size() < 2) {
            System.err.println("Report is invalid");
            return false;
        }

        //Check if it's safe without using the dampener
        if(IsReportSafe(report)) {
            return true;
        } else {
            //Not safe without problem dampener, instantiate list with each element removed
            for(int i = 0; i < report.size(); i++) {
                ArrayList<Integer> newReport = new ArrayList<>(report);
                newReport.remove(i);
                if(IsReportSafe(newReport)) {
                    return true;
                }
            }

            return false;
        }
    }
}