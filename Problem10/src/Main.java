import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int[][] map = ParseMapFile("input.txt");

        assert map != null;

        //System.out.println(MapArrayToString(map));

        ArrayList<Set> sets = ParseMap(map, false);

        //System.out.println(sets.get(0).toString(map));

        long routeCount = SumRoutes(sets);

        System.out.println("Route count: " + routeCount + " Num sets: " + sets.size());
    }

    public static int[][] ParseMapFile(String fileName) {
        try {
            Scanner scanner = new Scanner(new File(fileName));

            ArrayList<int[]> map = new ArrayList<>();

            int y = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                int[] row = new int[line.length()];

                for(int x = 0; x < line.length(); x++) {
                    int h = line.charAt(x) - '0';
                    row[x] = h;
                }

                map.add(row);
                y++;
            }

            scanner.close();

            // Convert ArrayList<int[]> to int[][]
            return map.toArray(new int[0][]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String MapArrayToString(int[][] map) {
        StringBuilder result = new StringBuilder();

        for(int y = 0; y < map.length; y++) {
            for(int x = 0; x < map[y].length; x++) {
                result.append("(" + x + ", " + y + "): " + map[y][x] + ", ");
            }
            result.append("\n");
        }

        return result.toString();
    }

    public static long SumRoutes(ArrayList<Set> sets) {
        long routes = 0;

        for (Set set : sets) {
            routes += set.NumRoutes();
        }

        return routes;
    }

    public static ArrayList<Set> ParseMap(int[][] nodes, boolean rating) {
        ArrayList<Set> sets = new ArrayList<>();
        HashSet<Node> visited = new HashSet<>();

        //Iterate over every node
        for(int y = 0; y < nodes.length; y++) {
            for(int x = 0; x < nodes[y].length; x++) {
                //Have we already visited this node
                Node node = new Node(x, y, nodes[y][x]);
                if(!visited.contains(node) && node.height == 0) {
                    if(rating) {

                    } else {
                        //We haven't, instantiate a new set
                        Set newSet = InstantiateSet(x, y, nodes, visited);
                        sets.add(newSet);
                    }
                }
            }
        }

        return sets;
    }

    public static Set InstantiateSet(int x, int y, int[][] map, HashSet<Node> visited) {
        //Instantiate the set
        Set set = new Set(0);
        set.FillSet(x, y, map, visited);
        return set;
    }

    public static Set InstantiateSetRating(int x, int y, int[][] map) {
        Set set = new Set(0);
        return set;
    }
}