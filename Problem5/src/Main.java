import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {

        String fn = "input.txt";

        HashMap<Integer, HashSet<Integer>> rules = ParseRules(GetRulesFromFile(fn));
        PrintRules(rules);

        ArrayList<ArrayList<Integer>> updates = ParseUpdates(GetUpdatesFromFile(fn));
        PrintUpdates(updates);

        int sum = 0;

        for(ArrayList<Integer> update: updates) {
            String output = "Update: " + update.toString() + " is ";

            if(IsUpdateValid(update, rules)) {
                int mid = GetMiddleNumberInUpdate(update);
                output += " valid, mid num: " + mid + ", not summing";
                //sum += mid;
            } else {
                output += " not valid, re-ordering";
                output += " Previous ordering: " + update.toString();
                ArrayList<Integer> reorder = OrderUpdateCorrectly_v2(update, rules);
                int reorderMid = GetMiddleNumberInUpdate(reorder);
                output += " reorder: " + reorder.toString() + ", mid num: " + reorderMid;
                sum += reorderMid;
            }

            System.out.println(output);
        }

        System.out.println("Sum of mid nums of in-valid updates: " + sum);
    }

    //Print the rules to console - used for debugging
    public static void PrintRules(HashMap<Integer, HashSet<Integer>> rules) {
        for(Integer key : rules.keySet()) {
            HashSet<Integer> set = rules.get(key);

            String output = "";
            for(Integer value : set) {
                output += value + ", ";
            }

            System.out.println(key + " | " + output);
        }
    }

    //Print the updates to the console - used for debugging
    public static void PrintUpdates(ArrayList<ArrayList<Integer>> updates) {
        for(int i = 0; i < updates.size(); i++) {
            String output = "";
            for(int x : updates.get(i)) {
                output += x + ", ";
            }
            System.out.println(output);
        }
    }

    //Given the file path, get each rule as a string, return all rules in the file as a list
    public static ArrayList<String> GetRulesFromFile(String fileName) {
        //Init ret list
        ArrayList<String> ruleStrs = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(new File(fileName));

            Pattern pat = Pattern.compile("\\d+\\|\\d+");

            //Iterate over each line
            while(scanner.hasNextLine()) {
                //Check if line matches
                String line = scanner.nextLine();

                Matcher mat = pat.matcher(line);
                if(mat.find()) {
                    //Matches
                    ruleStrs.add(line);
                } else {
                    break;
                }
            }

            return ruleStrs;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Given a rules list string, return a hashmap of the parse rules
    //Map<page to be printed, list of pages that must be printed beforehand>
    public static HashMap<Integer, HashSet<Integer>> ParseRules(ArrayList<String> ruleStrs) {
        //Init the hashmap
        HashMap<Integer, HashSet<Integer>> rules = new HashMap<>();

        //Regex for fetching numbers
        Pattern numPat = Pattern.compile("\\d+");

        //Iterate over the rule strings
        for(String ruleStr : ruleStrs) {
            //Match string on pattern
            Matcher matcher = numPat.matcher(ruleStr);

            int page = -1, prevPage = -1;

            //Get first num - page to be printed
            if(matcher.find()){
                page = Integer.parseInt(matcher.group());
            }

            //Get second num - page to be printed before
            if(matcher.find()) {
                prevPage = Integer.parseInt(matcher.group());
            }

            //If both numbers exist, add to rules set
            if(page >= 0 || prevPage >= 0) {
                //Check if the page already exists in hashmap
                if(rules.containsKey(page)) {
                    //It does, add the prev page to the set
                    rules.get(page).add(prevPage);
                } else {
                    //Page doesn't exist in hashmap, init it
                    HashSet<Integer> prevPages = new HashSet<>();
                    prevPages.add(prevPage);
                    rules.put(page, prevPages);
                }
            }
        }

        return rules;
    }

    //Given the file, returns all the update strings in the file
    //Assumes every update has 2 or more pages
    public static ArrayList<String> GetUpdatesFromFile(String fileName) {
        //Init ret list
        ArrayList<String> updateStrs = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(new File(fileName));

            Pattern pat = Pattern.compile("(\\d+,)+(\\d+)");

            //Iterate over each line
            while(scanner.hasNextLine()) {
                //Check if line matches
                String line = scanner.nextLine();

                Matcher mat = pat.matcher(line);
                if(mat.find()) {
                    //Matches
                    updateStrs.add(line);
                }
            }

            return updateStrs;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Given an array of strings, each element being the string for an update line, parses them into a list of lists of ints
    public static ArrayList<ArrayList<Integer>> ParseUpdates(ArrayList<String> updateStrs) {
        ArrayList<ArrayList<Integer>> updates = new ArrayList<>();
        Pattern numPat = Pattern.compile("\\d+");
        for(String updateStr : updateStrs) {
            Matcher mat = numPat.matcher(updateStr);

            //Init array
            ArrayList<Integer> update = new ArrayList<>();
            while(mat.find()) {
                update.add(Integer.parseInt(mat.group()));
            }

            updates.add(update);
        }
        return updates;
    }

    public static int GetMiddleNumberInUpdate(ArrayList<Integer> update) {
        //Check that the length is odd
        if(update.size() % 2 == 0) {
            System.err.println("Error calculating middle number of update length: " + update.size());
            return -1;
        }

        return  update.get((update.size() - 1) / 2);
    }
    //Given an update and the rules, determines if the update is valid
    //Average Time Complexity O(r*u), u being update size, r being the average rule length for numbers in the update
    public static boolean IsUpdateValid(ArrayList<Integer> update, HashMap<Integer, HashSet<Integer>> rules) {
        //System.out.println("IsUpdateValid called for update: " + update.toString());

        //Create an 'before set' that contains all the pages that have already been printed
        HashSet<Integer> beforePages = new HashSet<>();
        //Add the first page to this set (Nothing is printed before it, so it doesn't need to be checked)
        beforePages.add(update.get(0));

        //Iterate forward over update, from second-first page to last (first page doesn't need to be checked as nothing is printed before it)
        for(int pg = 1; pg < update.size(); pg++) {
            //Get the rule for this page
            HashSet<Integer> rule = rules.get(update.get(pg));

            //Check if this page has no rules
            if(rule == null) {
                //System.out.println("No rules found for page: " + update.get(pg));
                beforePages.add(update.get(pg));
                continue;
            }

            //System.out.println("rules for page " + update.get(pg) + ": " + rule.toString());

            //Does a page that has already been printed (in beforePages) contained in the rules
            //O(r) average time complexity, r being average rule set size or the beforePageSize, depending on which is smaller
            if(!Collections.disjoint(rule, beforePages)) {
                //Rule is violated, this page needs to be printed before one that has already been printed
                //System.out.println("Update invalid");
                return false;
            }

            //Add this page to the set
            beforePages.add(update.get(pg));
        }

        //System.out.println("Update valid");
        return true;
    }

    //Given an update, order the numbers in the update correctly so that it conforms to the rules
    public static ArrayList<Integer> OrderUpdateCorrectly(ArrayList<Integer> update, HashMap<Integer, HashSet<Integer>> rules) {
        //Given a list of numbers and the ordering rules, how do we order them properly

        //Keep track of the pages we have already printed and their indexes: Map<Index, Page>
        HashMap<Integer, Integer> beforePages = new HashMap<>();

        //Add the first page in the update to the map
        beforePages.put(update.get(0), 0);

        //Iterate over each index in the update, from start to end (ignoring the first index)
        for(int idx = 1; idx < update.size(); idx++) {
            //Get the page
            int pg = update.get(idx);

            //Get the rules for this page
            HashSet<Integer> rule = rules.get(pg);

            //Check if there are any rules
            if(rule == null) {
                //No rules, update before page and skip checks
                beforePages.put(idx, pg);
                continue;
            }

            boolean moved = false;
            //Check if this page should be printed before any one we have already printed
            //Iterate over each page we have printed so far
            for(int prevPgIdx = 0; prevPgIdx < beforePages.size(); prevPgIdx++) {
                //Check if this previously printed page should be printed after our current page
                if(rule.contains(beforePages.get(prevPgIdx))) {
                    //This page needs to be printed after our current page, move it to after our current page
                    //Get the page we are moving's page number
                    int pgToMv = beforePages.get(prevPgIdx);

                    System.out.print("Re-odering list " + update.toString());
                    System.out.print(" Checking page " + update.get(idx) + " at index " + idx + ". Page " + pgToMv + " at index " + prevPgIdx + " conflicts, moving it to index " + (idx+1));
                    //Insert it into a new position just after our current page
                    update.add(idx+1, pgToMv);
                    //Remove it from the update list
                    update.remove(prevPgIdx);
                    System.out.println(" New list: " + update.toString());

                    moved = true;
                    break;
                }
            }

            if(moved) {
                //Restart iteration
                beforePages.clear();
                idx = 1;
            } else {
                beforePages.put(idx, pg);
            }
        }

        return update;
    }

    public static ArrayList<Integer> OrderUpdateCorrectly_v2(ArrayList<Integer> update, HashMap<Integer, HashSet<Integer>> rules) {
        //Create a list for the ordered update
        ArrayList<Integer> orderUpdate = new ArrayList<>();

        //Go through each number in the update
        for(int idx = 0; idx < update.size(); idx++) {
            //Get the page number
            int pg = update.get(idx);
            //Get the rules for this page
            HashSet<Integer> rule = rules.get(pg);

            //Check if there are any rules
            if(rule == null) {
                //No rules, add this page to end
                orderUpdate.add(pg);
                continue;
            }

            //Find a valid position for this page, iterate over the ordered pages added so far
            int insertIdx = orderUpdate.size();
            for(int i = 0; i < orderUpdate.size(); i++) {
                //Check if this page is in the rules of the page we are adding
                if(rule.contains(orderUpdate.get(i))) {
                    //It is, set insert index
                    insertIdx = i;
                    break;
                }
            }

            orderUpdate.add(insertIdx, pg);
        }

        return orderUpdate;
    }
}