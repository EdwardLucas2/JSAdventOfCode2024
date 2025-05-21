import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        char[][] map = ParseFile("input.txt");
        boolean discount = true;

        System.out.println("Input Map:\n" + MapToString(map));

        ArrayList<Region> regions = FindRegions(map, discount);

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

    public static ArrayList<Region> FindRegions(char[][] map, boolean discount) {
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
                    Region r;
                    if(discount) {
                        r = ProcessRegion2(map, x, y, adjacencyArray, visited);
                    } else {
                        r = ProcessRegion(map, x, y, adjacencyArray, visited);
                    }
                    regions.add(r);

                    System.out.println("Region at ("+x+", "+y+"), plant " + map[y][x] + ": " + r.toString(discount));
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


        boolean sameLft, sameRgt, sameUp, sameDown;


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

    public static Region ProcessRegion2(char[][] map, int startX, int startY, int[][] cornerCountArray, boolean[][] visited) {
        //Check that the start position hasn't been visited
        if(visited[startY][startX]) {
            System.err.println("Error calculating adjacency of position (" + startY + ", " + startX + "): Spot already visited");
            return null;
        }

        //Mark this plot visited
        visited[startY][startX] = true;

        //Look at neighbours, are they the same plant type or not, this assumes they aren't visited
        long regionArea = 1, regionCornerCount = 0;
        int cornerCount = CornerCount(startX, startY, map);
        char ch = map[startY][startX];

        boolean sameLft, sameRgt, sameUp, sameDown;

        //Check left
        if(startX > 0) {
            if (map[startY][startX - 1] == ch) {
                //Same plant type, recurse if not visited
                if (!visited[startY][startX - 1]) {
                    Region r = ProcessRegion2(map, startX - 1, startY, cornerCountArray, visited);
                    regionCornerCount += r.perimeter;
                    regionArea += r.area;
                }
            }
        }

        //Check right
        if(startX < map[0].length - 1) {
            if (map[startY][startX + 1] == ch) {
                if (!visited[startY][startX + 1]) {
                    //Same plant type, recurse
                    Region r = ProcessRegion2(map, startX + 1, startY, cornerCountArray, visited);
                    regionCornerCount += r.perimeter;
                    regionArea += r.area;
                }
            }
        }

        //Check up
        if(startY < map.length - 1) {
            if (map[startY + 1][startX] == ch) {
                //Same plant type, recurse
                if (!visited[startY + 1][startX]) {
                    Region r = ProcessRegion2(map, startX, startY + 1, cornerCountArray, visited);
                    regionCornerCount += r.perimeter;
                    regionArea += r.area;
                }
            }
        }

        //Check down
        if(startY > 0) {
            if (map[startY - 1][startX] == ch) {
                //Same plant type, recurse
                if (!visited[startY - 1][startX]) {
                    Region r = ProcessRegion2(map, startX, startY - 1, cornerCountArray, visited);
                    regionCornerCount += r.perimeter;
                    regionArea += r.area;
                }
            }
        }

        //Set adjacency array value
        cornerCountArray[startY][startX] = cornerCount;

        return new Region(regionArea, cornerCount + regionCornerCount);
    }

    public record Region(long area, long perimeter) {
        public String toString(boolean discount) {
            if(!discount) {
                return "Area: " + area + ", Perimeter: " + perimeter + ", Cost: " + Cost();
            } else {
                return "Area: " + area + ", Corner Count: " + perimeter + ", Cost: " + Cost();
            }
        }

        public long Cost() {
            return (long) area * (long) perimeter;
        }
    }

    public static int CornerCount(int x, int y, char[][] map) {
        int count = 0;
        char ch = map[y][x];

        // Check surrounding cells
        boolean top = matches(x, y + 1, map, ch);
        boolean left = matches(x - 1, y, map, ch);
        boolean right = matches(x + 1, y, map, ch);
        boolean bottom = matches(x, y - 1, map, ch);
        boolean topLeft = matches(x - 1, y + 1, map, ch);
        boolean topRight = matches(x + 1, y + 1, map, ch);
        boolean bottomLeft = matches(x - 1, y - 1, map, ch);
        boolean bottomRight = matches(x + 1, y - 1, map, ch);

        // Count outward corners
        if (!top && !right) count++; // Top-right corner
        if (!bottom && !right) count++; // Bottom-right corner
        if (!top && !left) count++; // Top-left corner
        if (!bottom && !left) count++; // Bottom-left corner

        // Count inward corners
        if (top && left && !topLeft) count++; // Inward top-left corner
        if (top && right && !topRight) count++; // Inward top-right corner
        if (bottom && left && !bottomLeft) count++; // Inward bottom-left corner
        if (bottom && right && !bottomRight) count++; // Inward bottom-right corner

        return count;
    }
    // Helper method to check if a neighbor matches
    private static boolean matches(int nx, int ny, char[][] map, char ch) {
        return nx >= 0 && ny >= 0 && ny < map.length && nx < map[ny].length && map[ny][nx] == ch;
    }
}

//How to calculate the number of straight fences - corner counting?
//What is a corner (90 degree corner) - one plot can have 4 corners max, 0 corners min
//A top-right corner exists where the plots to above, top-right, and right are off the map, or another plant type
//Count the total number of corners in region - perimeter is region corner count/2