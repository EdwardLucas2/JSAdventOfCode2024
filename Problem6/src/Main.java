import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        State state = ReadInputFromFile("input.txt");

        GetNumLoopPositions(state, 100000);
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
        System.out.println("Step: " + state.step + ", finished: " + state.exited + ", Num obstacles: " + state.numObstacles());
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

    //Master function for part 1 - returns the number of unique cells will be visited by a guard
    public static int CalculateNumCellsVisited(State state) {
        PrintState(state);
        System.out.println("Running simulation...");
        int numVisited = RunSimulation(10000, state);
        System.out.println("Simulation finished. New Map:");
        PrintState(state);
        System.out.println("\n\nNum cells visited: " + numVisited);
        return numVisited;
    }

    //Master function for part 2 - returns the number of locations an obstacle can be placed where the guard will go into a loop
    public static int GetNumLoopPositions(State state, int maxSteps) {
        System.out.println("Running initial simulation to get guards unmodified path...");
        //Get all the positions the guard will go without adding any obstacles
        State startState = new State(state);
        RunSimulation(maxSteps, startState);

        System.out.println("Simulation finished. Guard path:");
        PrintState(startState);

        System.out.println("\n\nAdding obstacles and running simulations...");
        int count = 0;
        //Iterate over each position in the simulated start state
        for(int y = 0; y < state.map.length; y++) {
            for(int x = 0; x < state.map[0].length; x++) {
                if(y == state.gy && x == state.gx) {
                    //Do nothing, this is the guards initial position, can't spawn an obstacle here
                } else if(startState.map[x][y] == 1) {
                    System.out.println("Added obstacle, running loop detection simulation...");
                    //This location is visited by the guard, add an obstacle here and check for loop
                    State simState = new State(state); //Clone start state
                    simState.map[x][y] = 2; //Add obstacle
                    //Run simulation and check for loops
                    if(RunSimCheckForLoop(maxSteps, simState)) {
                        count++; //There's a loop here!
                        //System.out.println("Loop found in " + simState.step + " steps");
                    } else {
                        //System.out.println("No loop detected");
                        //PrintState(simState);
                    }
                }
            }
        }

        System.out.println("Number of loop positions: " + count);

        return count;
    }

    //Returns the number of unique visited locations
    //Stops when max steps reached, or guard leaves map
    public static int RunSimulation(int maxSteps, State state) {
        while(state.step < maxSteps && !state.exited) {
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

    //Returns true if the current state enters a loop within max steps, false otherwise
    public static boolean RunSimCheckForLoop(int maxSteps, State state) {
        //Initialise pos history
        HashMap<Integer, HashMap<Integer, HashSet<Integer>>> posHis = new HashMap<>();

        //Add initial position
        AddPosition(state, posHis);

        while (state.step < maxSteps && !state.exited) {
            if(RunIterationAndCheckForLoop(state, posHis)) {
                return true;
            }
        }

        //Either reached maxSteps or finished
        return false;
    }

    //Simulates a single step in the simulation
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
            state.exited = true;
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
            state.direction = (state.direction + 1) % 4;

            //Check that the guard isn't surrounded (obstacles to left, right, top, bottom
            if(state.map[state.gx+1][state.gy] == 2 && state.map[state.gx-1][state.gy] == 2 && state.map[state.gx][state.gy+1] == 2 && state.map[state.gx][state.gy-1] == 2) {
                //Surrounded
                System.out.println("Guard surrounded");
                state.exited = true;
                return;
            }
            //Recurse
            RunIteration(state);
        } else {
            System.err.println("Invalid map cell: " + state.map[x][y]);
            return;
        }
    }

    public static boolean RunIterationAndCheckForLoop(State state, HashMap<Integer, HashMap<Integer, HashSet<Integer>>> posHis) {
        //Run an iteration of the simulation
        RunIteration(state);

        //Check if the guard has left
        if(state.exited)
            return false; //It has, return false

        //Add this state, and check if this state has occurred before
        return AddPosition(state, posHis);
    }

    //Given a state, and a list of previous positions, are we in a loop?
    public static boolean InLoop(State state, HashMap<Integer, HashMap<Integer, HashSet<Integer>>> previousPositions) {
        //Fetch previous positions we have been in, in the current x axis
        HashMap<Integer, HashSet<Integer>> prevColumnPos = previousPositions.get(state.gx);
        //Check for null (not been in this column before
        if(prevColumnPos == null)
            return false; //Haven't been here before, not in loop
        //Fetch the previous directions we have been in
        HashSet<Integer> prevDirsAtPos = prevColumnPos.get(state.gy);
        //Check for null, have we been in this cell before
        if (prevDirsAtPos == null)
            return false; //Not been in this cell before
        //Check if we have pointed in the same dir in this cell before and return
        return prevDirsAtPos.contains(state.direction);
    }

    //Adds a state to position history, returns true if it's already been added, false if not
    public static boolean AddPosition(State state, HashMap<Integer, HashMap<Integer, HashSet<Integer>>> previousPositions) {
        //Fetch previous positions in the current x axis, init the row map for this col if needed
        HashMap<Integer, HashSet<Integer>> prevColumnPos = previousPositions.computeIfAbsent(state.gx, k -> new HashMap<>());

        //Fetch the previous directions we have been in this position
        HashSet<Integer> prevDirsAtPos = prevColumnPos.computeIfAbsent(state.gy, k -> new HashSet<>());

        //Add the current dir
        return !prevDirsAtPos.add(state.direction);
    }

    public static void PrintPositionHistory(HashMap<Integer, HashMap<Integer, HashSet<Integer>>> posHis) {
        for (int x : posHis.keySet()) {
            HashMap<Integer, HashSet<Integer>> column = posHis.get(x);
            for (int y : column.keySet()) {
                HashSet<Integer> directions = column.get(y);
                System.out.println("Position (" + x + ", " + y + ") visited with directions: " + directions);
            }
        }
    }
}