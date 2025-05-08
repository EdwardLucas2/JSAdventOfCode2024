import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        State state = ReadInputFromFile("input.txt");
        PrintState(state);
        System.out.println("Running simulation...");
        int numVisited = RunSimulation(10000, state);
        System.out.println("Simulation finished. New Map:");
        PrintState(state);
        System.out.println("\n\nNum cells visited: " + numVisited);
    }

    //Print state to console - used for debugging
    public static void PrintState(State state) {
        for(int y = 0; y < state.map.length; y++) {
            for(int x = 0; x < state.map[0].length; x++) {
                if(y == state.gy && x == state.gx) {
                    //Print guard, get direction
                    if(state.direction == 0)
                        System.out.print("^");
                    else if (state.direction == 1)
                        System.out.print(">");
                    else if (state.direction == 2)
                        System.out.print("_");
                    else if(state.direction == 3)
                        System.out.print("<");
                    else
                        System.out.print("*");
                } else {
                    System.out.print(state.map[y][x]);
                }
            }
            System.out.println();
        }
        System.out.println("Step: " + state.step + ", finished: " + state.finished);
    }

    //Reads file, outputs state object
    public static State ReadInputFromFile(String filename) {
        try {
            //Read file into string list from disk
            List<String> fileContents = Files.readAllLines(Paths.get(filename));

            //Instantiate result array
            int[][] map = new int[fileContents.get(0).length()][fileContents.size()];
            int gx = -1, gy = -1;

            for(int y = 0; y < fileContents.size(); y++) {
                String line = fileContents.get(y);
                //Iterate over each character in the line
                for(int x = 0; x < line.length(); x++) {
                    int val = ParseFileChar(line.charAt(x));
                    //Check for guard
                    if(val == 9) {
                        //It is guard, set guard co-ords and guard's location to visited ground
                        gx=x;
                        gy=y;
                        map[x][y] = 1;
                    } else {
                        map[x][y] = val;
                    }
                }
            }

            return new State(map, gx, gy, 0);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //Assumes that the starting position of the guard in the input is up
    //'.': floor -> 0 (unvisited), 1 (visited)
    //'#': obstacle -> 2
    //'^': guard starting position (facing up) -> 9
    //Anything else: ERROR -> -1
    public static int ParseFileChar(char c) {
        if(c == '.') {
            return 0;
        } else if(c == '#') {
            return 2;
        } else if(c == '^') {
            return 9;
        } else {
            System.err.println("Parsing invalid character: " + c);
            return -1;
        }
    }

    //Returns the number of unique visited locations
    //Stops when max steps reached, or guard leaves map
    public static int RunSimulation(int maxSteps, State state) {
        while(state.step < maxSteps && !state.finished) {
            RunIteration(state);
        }

        //Finished simulation, count num visited locations
        int count = 0;
        for(int y = 0; y < state.map.length; y++) {
            for(int x = 0; x < state.map[0].length; x++) {
                if(state.map[x][y] == 1)
                    count++;
            }
        }
        return count;
    }

    public static void RunIteration(State state) {
        //Get the co-ordinates of what's in front of the guard
        int x = -1, y = -1;
        if(state.direction == 0) {
            //Facing up
            y = state.gy-1;
            x = state.gx;
        } else if(state.direction == 1) {
            //Facing right
            y = state.gy;
            x = state.gx+1;
        } else if(state.direction == 2) {
            //Facing down
            y = state.gy+1;
            x = state.gx;
        } else if(state.direction == 3) {
            //Facing left
            y = state.gy;
            x = state.gx-1;
        } else {
            System.err.println("Invalid direction: " + state.direction);
        }

        //Check if the next location is outside the map
        if(x < 0 || y < 0 || x >= state.map.length || y >= state.map[0].length) {
            //It is
            state.finished = true;
            return;
        }

        //Next location is inside the map, is the cell we are facing an obstacle or empty
        if(state.map[x][y] == 1 || state.map[x][y] == 0) {
            //Floor, move here
            //Set floor space to visited
            state.map[x][y] = 1;
            //Move player
            state.gy = y;
            state.gx = x;
            //Increment step count
            state.step++;
        } else if(state.map[x][y] == 2) {
            //Obstacle, turn right
            state.direction++;
            if(state.direction > 3) {
                state.direction = 0;
            }
            //Recurse
            RunIteration(state);
        } else {
            System.err.println("Invalid map cell: " + state.map[x][y]);
            return;
        }
    }
}