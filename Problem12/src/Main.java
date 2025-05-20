import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        char[][] map = ParseFile("input.txt");

        System.out.println("Input Map:\n" + MapToString(map));

        ArrayList<Region> regions = FindRegions(map);

        long cost = 0;

        for(Region region : regions) {
            cost += region.Cost();
        }

        System.out.println("Total cost: " + cost);
    }

    public static char[][] ParseFile(String fileName) {
        try {
            Scanner scanner = new Scanner(new File(fileName));

            ArrayList<char[]> ret = new ArrayList<>();

            while (scanner.hasNextLine()) {
                ret.add(scanner.nextLine().toCharArray());
            }

            //Convert array list ret to array and return
            return ret.toArray(new char[ret.size()][]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String MapToString(char[][] map) {
        StringBuilder sb = new StringBuilder();

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                sb.append(map[y][x]);
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    public static String IntArrToString(int[][] map, char delimiter) {
        StringBuilder sb = new StringBuilder();

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                sb.append(map[y][x]);
                sb.append(delimiter);
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    public static ArrayList<Region> FindRegions(char[][] map) {
        //Init adjacency arrays and visited array
        int[][] adjacencyArray = new int[map.length][map[0].length];
        boolean[][] visited = new boolean[map.length][map[0].length];
        ArrayList<Region> regions = new ArrayList<>();

        //Iterate over every element in the map
        for(int y = 0 ; y < map.length ; y++) {
            for(int x = 0 ; x < map[y].length ; x++) {
                //Has this element already been visited (when region finding)
                if(!visited[y][x]) {
                    //Element not already visited, find this elements region
                    regions.add(ProcessRegion(map, x, y, adjacencyArray, visited));
                }
            }
        }

        return regions;
    }

    public static Region ProcessRegion(char[][] map, int startX, int startY, int[][] adjacencyArray, boolean[][] visited) {
        //Check that the start position hasn't been visited
        if(visited[startY][startX]) {
            System.err.println("Error calculating adjacency of position (" + startY + ", " + startX + "): Spot already visited");
            return null;
        }

        //Mark this plot visited
        visited[startY][startX] = true;

        //Look at neighbours, are they the same plant type or not, this assumes they aren't visited
        int plotPerimeter = 0, regionArea = 1, regionPerimeter = 0;
        char ch = map[startY][startX];

        //Check left
        if(startX > 0) {
            if (map[startY][startX - 1] == ch) {
                //Same plant type, recurse if not visited
                if(!visited[startY][startX - 1]) {
                    Region r = ProcessRegion(map, startX - 1, startY, adjacencyArray, visited);
                    regionPerimeter += r.perimeter;
                    regionArea += r.area;
                }
            } else {
                //Different plant type
                plotPerimeter++;
            }
        } else {
            //On edge of plot, need fence
            plotPerimeter++;
        }

        //Check right
        if(startX < map[0].length - 1) {
            if (map[startY][startX + 1] == ch) {
                if(!visited[startY][startX + 1]) {
                    //Same plant type, recurse
                    Region r = ProcessRegion(map, startX + 1, startY, adjacencyArray, visited);
                    regionPerimeter += r.perimeter;
                    regionArea += r.area;
                }
            } else {
                //Different plant type
                plotPerimeter++;
            }
        } else {
            //On edge of plot, need fence
            plotPerimeter++;
        }

        //Check up
        if(startY < map.length - 1) {
            if (map[startY + 1][startX] == ch) {
                //Same plant type, recurse
                if (!visited[startY + 1][startX]) {
                    Region r = ProcessRegion(map, startX, startY + 1, adjacencyArray, visited);
                    regionPerimeter += r.perimeter;
                    regionArea += r.area;
                }
            } else {
                //Different plant type
                plotPerimeter++;
            }
        } else {
            //On edge of plot, need fence
            plotPerimeter++;
        }

        //Check down
        if(startY > 0) {
            if (map[startY - 1][startX] == ch) {
                //Same plant type, recurse
                if(!visited[startY - 1][startX]) {
                    Region r = ProcessRegion(map, startX, startY - 1, adjacencyArray, visited);
                    regionPerimeter += r.perimeter;
                    regionArea += r.area;
                }
            } else {
                //Different plant type, add adjacency
                plotPerimeter++;
            }
        } else {
            //On edge of plot, need fence
            plotPerimeter++;
        }

        //Set adjacency array value
        adjacencyArray[startY][startX] = plotPerimeter;

        return new Region(regionArea, plotPerimeter + regionPerimeter);
    }

    public record Region(int area, int perimeter) {
        @Override
        public String toString() {
            return "Area: " + area + ", Perimeter: " + perimeter;
        }

        public int Cost() {
            return area * perimeter;
        }
    }
}
