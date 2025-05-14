import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        MapData map = ParseMapFile("input.txt");
        PrintMap(map);

        HashSet<Coordinate> antinodes = FindAntinodes(map);

        System.out.println("Num unique antinodes: " + antinodes.size());
    }

    public static HashSet<Coordinate> FindAntinodes(MapData map) {
        //Init co-ord array for found anti-nodes
        HashSet<Coordinate> antinodes = new HashSet<>();

        for(char ant: map.AntennaLocations.keySet()) {
            //Iterate over every unique pair of antennas
            for(int i = 0; i < map.AntennaLocations.get(ant).size()-1; i++) {
                for(int j = i+1; j < map.AntennaLocations.get(ant).size(); j++) {
                    //Calculate the position difference between them
                    int[] antI = map.AntennaLocations.get(ant).get(i);
                    int[] antJ = map.AntennaLocations.get(ant).get(j);

                    //System.out.println("Finding anti nodes for antenna at (" + antI[0] + ", " + antI[1] + ") and at (" + antJ[0] + ", " + antJ[1] + ")");

                    //Calculate the difference between them, multiplied by 1.5
                    float[] dif = new float[]{(antI[0] - antJ[0])*1.5f, (antI[1] - antJ[1])*1.5f};

                    //Find the average position between the two nodes
                    float[] center = new float[]{(antI[0] + antJ[0])/2f, (antI[1] + antJ[1])/2f};

                    //System.out.println("Center calculated to be (" + center[0] + ", " + center[1] + ")");

                    //Find the two anti-nodes by adding the difference to the average position, and taking it away
                    Coordinate anti1 = new Coordinate(new int[]{Math.round(center[0] + dif[0]), Math.round(center[1] + dif[1])});
                    Coordinate anti2 = new Coordinate(new int[] {Math.round(center[0] - dif[0]), Math.round(center[1] - dif[1])});
                    // Are the anti-nodes within bounds of our map
                    if (anti1.data[0] >= 0 && anti1.data[1] >= 0 && anti1.data[0] < map.width && anti1.data[1] < map.height) {
                        // Anti 1 is within bounds
                        antinodes.add(anti1);
                        System.out.println("Antinode (" + anti1.data[0] + ", " + anti1.data[1] + ") in bounds");
                    }
                    if (anti2.data[0] >= 0 && anti2.data[1] >= 0 && anti2.data[0] < map.width && anti2.data[1] < map.height) {
                        // Anti 2 is within bounds
                        System.out.println("Antinode (" + anti2.data[0] + ", " + anti2.data[1] + ") in bounds");
                        antinodes.add(anti2);
                    }
                }
            }
        }



        return antinodes;
    }

    public static void PrintAntiNodes(HashSet<Coordinate> coords) {
        for(Coordinate coord : coords) {
            System.out.println("Antinode: (X: " + coord.data[0] + ", Y: " + coord.data[1] + ")");
        }
    }

    public static MapData ParseMapFile(String fileName) {
        try {
            Scanner scanner = new Scanner(new File(fileName));

            //Instantiate hashmap
            HashMap<Character, ArrayList<int[]>> AntennaLocations = new HashMap<Character, ArrayList<int[]>>();

            int row = 0;
            int width = -1;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if(width == -1)
                    width = line.length();

                //Iterate over characters
                for(int col = 0; col < line.length(); col++) {
                    char ch = line.charAt(col);

                    //Check if character is antenna
                    if(ch != '.') {
                        //It is, does this type exist in the map yet
                        if(AntennaLocations.containsKey(ch)) {
                            AntennaLocations.get(ch).add(new int[]{row, col});
                        } else {
                            //It doesn't, instantiate the array list, and put in the key
                            ArrayList<int[]> arr = new ArrayList<>();
                            arr.add(new int[]{row, col});
                            AntennaLocations.put(ch, arr);
                        }
                    }
                }

                row++;
            }

            scanner.close();
            return new MapData(AntennaLocations, width, row);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public record MapData(HashMap<Character, ArrayList<int[]>> AntennaLocations, int width, int height) {
        public MapData(HashMap<Character, ArrayList<int[]>> AntennaLocations, int width, int height) {
            this.AntennaLocations = AntennaLocations;
            this.width = width;
            this.height = height;
        }
    }

    public static void PrintMap(MapData map) {
        System.out.println("Map width: " + map.width + " height: " + map.height);
        //Iterate over keys
        for(char ch : map.AntennaLocations.keySet()) {
            ArrayList<int[]> arr = map.AntennaLocations.get(ch);
            System.out.print(ch + ": ");
            for(int[] ant : arr) {
                System.out.print("(" + ant[0] + "," + ant[1] + "), ");
            }
            System.out.println();
        }
    }
}