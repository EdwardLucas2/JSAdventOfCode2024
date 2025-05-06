import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        char[][] input = ReadInputFile("input.txt");
        printArray(input);

        //int num_match = GetAllMatches(input);
        int num_valid_mas = GetNumberValidMAS(input);

        System.out.println(num_valid_mas);
    }

    //Given the input file, returns a 2d character array of all the characters
    public static char[][] ReadInputFile(String filename) {
        ArrayList<char[]> rows = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(new File(filename));

            while (scanner.hasNextLine()) {
                //Split string into character array
                String line = scanner.nextLine();
                //System.out.println("Scanner read line: " + line);
                rows.add(line.toCharArray());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return rows.toArray(new char[0][0]);
    }

    public static void printArray(char[][] input) {
        for (int y= 0; y < input.length; y++) {
            for (int x = 0; x < input[0].length; x++) {
                System.out.print(input[y][x]);
            }
            System.out.println();
        }
    }

    public static int GetAllMatches(char[][] input) {
        int total = 0;
        //Iterate over all rows and columns
        for(int y = 0; y < input.length; y++) {
            for(int x = 0; x < input[0].length; x++) {
                if(input[y][x] == 'X') {
                    total += GetNumberOfMatchesAtPos(input, x, y);
                }
            }
        }

        return total;
    }

    //Given the co-ordinates of an X, returns the number of matches that use this X
    public static int GetNumberOfMatchesAtPos(char[][] input, int xPos, int yPos) {
        //Check co-ords are in array and are valid
        if(input.length < yPos || input[0].length < xPos || xPos < 0 || yPos < 0) {
            System.err.println("Invalid input");
            return -1;
        }

        int count = 0;

        //Check the first letter is X
        if(input[yPos][xPos] != 'X') {
            //It's not, no match
            System.err.println("Invalid input, co-ordinates don't point to an 'X'");
            return 0;
        }

        //Check left to right XMAS, is it's position possible
        //System.out.println("Input[" + yPos + "].length: "+input[yPos].length + ", XPos = "+xPos);
        if(xPos + 4 <= input[yPos].length) {
            //System.out.println("Left to right possible, checking letters");
            //Position is possible, do the letters match
            if(input[yPos][xPos + 1] == 'M' && input[yPos][xPos + 2] == 'A' && input[yPos][xPos + 3] == 'S') {
                //System.out.println("Letters match!");
                count++;
            }
        }

        //Check right to left XMAS, is it possible
        if(xPos >= 3) {
            //Position is possible, do the letter match
            if(input[yPos][xPos - 1] == 'M' && input[yPos][xPos - 2] == 'A' && input[yPos][xPos - 3] == 'S') {
                count++;
            }
        }

        //Check up XMAS, is it possible
        if(yPos >= 3) {
            //Position is possible
            if(input[yPos-1][xPos] == 'M' && input[yPos-2][xPos] == 'A' && input[yPos-3][xPos] == 'S') {
                count++;
            }
        }

        //Check down XMAS, is it possible
        if(yPos + 4 <= input.length) {
            //Position is possible
            if(input[yPos+1][xPos] == 'M' && input[yPos+2][xPos] == 'A' && input[yPos+3][xPos] == 'S') {
                count++;
            }
        }

        //Check up to right XMAS, is it possible
        if(yPos >= 3 && xPos + 4 <= input[yPos].length) {
            //It is possible, check if letters match
            if(input[yPos-1][xPos+1] == 'M' && input[yPos-2][xPos+2] == 'A' && input[yPos-3][xPos+3] == 'S') {
                count++;
            }
        }

        //Check up to left XMAS
        if(yPos >= 3 && xPos >= 3) {
            //It is possible, check if letters match
            if(input[yPos-1][xPos-1] == 'M' && input[yPos-2][xPos-2] == 'A' && input[yPos-3][xPos-3] == 'S') {
                count++;
            }
        }

        //Check down to left XMAS
        if(yPos + 4 <= input.length && xPos >= 3) {
            //It is possible, check letters
            if(input[yPos+1][xPos-1] == 'M' && input[yPos+2][xPos-2] == 'A' && input[yPos+3][xPos-3] == 'S') {
                count++;
            }
        }

        //Check down to right XMAS
        if(yPos + 4 <= input.length && xPos + 4 <= input[yPos].length) {
            //It is possible, check letters
            if(input[yPos+1][xPos+1] == 'M' && input[yPos+2][xPos+2] == 'A' && input[yPos+3][xPos+3] == 'S') {
                count++;
            }
        }

        return count;
    }

    //For part 2 is X-MAS at given location (loc of A) a valid X-MAS
    public static boolean IsMASValid(char[][] input, int xPos, int yPos) {
        //Check the coords are for an A (and valid)
        if(input.length <= yPos || input[0].length <= xPos || yPos < 0 || xPos < 0) {
            System.err.println("Invalid input co-ordinates");
            return false;
        }
        if(input[yPos][xPos] != 'A') {
            System.err.println("Invalid input, co-ordinates don't point to an 'A', they point to: " + input[yPos][xPos]);
            return false;
        }

        //Check that the co-ordinates are valid for a possible x
        if(xPos + 1 >= input[yPos].length || yPos + 1 >= input[yPos].length || xPos < 1 || yPos < 1) {
            //Centre of X-MAS is on edge, not possible
            return false;
        }

        //There each arm of the X-MAS can be written either way
        //Check top-left to bottom-right arm
        //Check top-left char
        if(input[yPos-1][xPos-1] == 'S') {
            //Top left is S, bottom right should be M
            if(input[yPos+1][xPos+1] != 'M')
                return false;
        } else if(input[yPos-1][xPos-1] == 'M') {
            //Top left is M, bottom right should be S
            if(input[yPos+1][xPos+1] != 'S')
                return false;
        } else {
            //Top-left is invalid char
            return false;
        }
        //Check top-right to bottom-left arm
        //Check top-right char
        if(input[yPos-1][xPos+1] == 'S') {
            //Top-right is S, bottom-left should be M
            if(input[yPos+1][xPos-1] != 'M')
                return false;
        } else if(input[yPos-1][xPos+1] == 'M') {
            //Top-right is M, bottom-left should be S
            if(input[yPos+1][xPos-1] != 'S')
                return false;
        } else {
            //Top-right is invalid char
            return false;
        }

        return true;
    }

    public static int GetNumberValidMAS(char[][] input) {
        int count = 0;
        //Iterate over all characters in array not on edge
        for(int y = 1; y < input.length - 1; y++) {
            for(int x = 1; x < input[0].length - 1; x++) {
                if(input[y][x] == 'A') {
                    if(IsMASValid(input, x, y)) {
                        count++;
                    }
                }
            }
        }

        return count;
    }
}
