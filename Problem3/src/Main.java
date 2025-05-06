import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        ArrayList<String> instructs = GetEnabledInstructions("do()"+ReadInputFile("input.txt")+"don't()");

        int sum = 0;
        for (String instruct : instructs) {
            System.out.println("Instruction: " +instruct);
            int num = GetResult(instruct);
            if(num == 10000000) {
                System.err.println("Invalid return value from instruct: " + instruct);
                return;
            }
            sum += num;
        }

        System.out.println(sum);
    }

    public static String ReadInputFile(String fp) {
        try {
            File file = new File(fp);

            Scanner scanner = new Scanner(file);
            String output = "";
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                output += line;
            }

            return output;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Given a corrupted memory string, returns an array of strings, each being a valid mul(x, y) instruction
    public static ArrayList<String> GetValidInstructions(String input) {
        //Compile the regex pattern for finding the instructions
        Pattern pattern = Pattern.compile("mul\\([0-9]{1,3},[0-9]{1,3}\\)");
        Matcher matcher = pattern.matcher(input);

        ArrayList<String> validInstructions = new ArrayList<>();
        while (matcher.find()) {
            validInstructions.add(matcher.group());
        }
        return validInstructions;
    }

    //Given the string of a valid instruction, returns the result
    public static int GetResult(String instruction) {
        Pattern pat  = Pattern.compile("[0-9]{1,3}");
        Matcher matcher = pat.matcher(instruction);

        int num1, num2;
        //Get first number
        if (matcher.find()) {
            num1 = Integer.parseInt(matcher.group());
        } else {
            System.err.println("Invalid instruction: " + instruction);
            return 10000000; //Return number that's impossible to form with valid instruct
        }
        //Get second number
        if (matcher.find()) {
            num2 = Integer.parseInt(matcher.group());
        } else {
            System.err.println("Invalid instruction: " + instruction);
            return 10000000; //Return number that's impossible to form with valid instruct
        }

        return num1 * num2;
    }

    //Given a string input, returns only the valid instructions that are enabled
    public static ArrayList<String> GetEnabledInstructions(String input) {
        //Compile regex patterns for do and don't instructions
        Pattern doPat = Pattern.compile("do\\(\\)");
        Pattern dontPat = Pattern.compile("don't\\(\\)");

        //Get the character indexes for each element
        Matcher doMatch = doPat.matcher(input);
        Matcher dontMatch = dontPat.matcher(input);

        ArrayList<Integer> DoPos = new ArrayList<>(); //Assume ordered
        ArrayList<Integer> DontPos = new ArrayList<>(); //Assume ordered

        while (doMatch.find()) {
            DoPos.add(doMatch.start());
        }
        while (dontMatch.find()) {
            DontPos.add(dontMatch.start());
        }

        //SOrt the lists
        Collections.sort(DoPos);
        Collections.sort(DontPos);

        for(int i = 0; i < DoPos.size(); i++) {
            System.out.println("DoPos at idx " + i +": " +DoPos.get(i));
        }
        for(int i = 0; i < DontPos.size(); i++) {
            System.out.println("DontPos at idx " + i +": " +DontPos.get(i));
        }

        int beginIndex = 0, doIdx = 0, dontIdx = 0;
        boolean enabled = true;

        ArrayList<String> enabledInstructions = new ArrayList<>();

        //Iterate over each dostart and endstart
        while (doIdx < DoPos.size() || dontIdx < DontPos.size()) {
            //Get the position of the next end and start
            int nextDoPos, nextDontPos;
            if (doIdx == DoPos.size()) {
                nextDoPos = input.length();
            } else {
                nextDoPos = DoPos.get(doIdx);
            }

            if(dontIdx == DontPos.size()) {
                nextDontPos = input.length();
            } else {
                nextDontPos = DontPos.get(dontIdx);
            }

            System.out.println("DoIDx: " + doIdx + ", Cur DoPos: " + nextDoPos + ", DontIdx:" + dontIdx + ", Cur DontPos: " + nextDontPos + ", Enabled: " + enabled);

            //Is the next instruction a do or a don't
            if(nextDoPos < nextDontPos) {
                //Next instruction is a do, are we currently enabled
                if(enabled) {
                    //We are, we don't need to do anything
                    doIdx++;
                } else {
                    //We aren't, switch to enabled
                    enabled = true;
                    beginIndex = nextDoPos;
                    doIdx++;
                }
            } else {
                //Next instruction is a don't, are we still disabled
                if(enabled) {
                    //We are enabled, this don't marks the end of the enabled section
                    enabled = false;
                    //Get the enabled stretch
                    String enabledSection = input.substring(beginIndex, nextDontPos);
                    //Get the instructions in the enabled stretch
                    enabledInstructions.addAll(GetValidInstructions(enabledSection));
                    dontIdx++;
                } else {
                    //We are disabled (2+ don'ts in a row), do nothing
                    dontIdx++;
                }
            }
        }

        return enabledInstructions;
    }
}