import java.util.HashSet;

public class Set {
    public int numEnds;
    public HashSet<Node> members;

    public Set(int numEnds) {
        this.numEnds = numEnds;
        members = new HashSet<>();
    }

    public void FillSet(int x, int y, int[][] map, HashSet<Node> visited) {
        //Have we already visited this node
        Node start = new Node(x, y, map[y][x]);
        visited.add(start);
        if(members.add(start)) {
            //Adding new node to set, is it an end
            if(map[y][x] == 9) {
                numEnds++;
                return; //Can't go any higher we're done
            }

            //We haven't already visited this node
            //Check if we can go up
            if(y < map.length - 1) {
                //We can, is the node above reachable
                if (map[y + 1][x] - map[y][x] == 1) {
                    //It is, recurse
                    FillSet(x, y + 1, map, visited);
                }
            }

            //Check if we can go down
            if(y > 0) {
                //We can, is down node reachable
                if (map[y - 1][x] - map[y][x] == 1) {
                    //It is, recurse
                    FillSet(x, y - 1, map, visited);
                }
            }

            //Check if we can go right
            if(x < map[y].length - 1) {
                //We can, check if right node is reachable
                if(map[y][x+1] - map[y][x] == 1) {
                    //It is, recurse
                    FillSet(x+1, y, map, visited);
                }
            }

            //Check if we can go left
            if(x > 0) {
                if(map[y][x-1] - map[y][x] == 1) {
                    FillSet(x - 1, y, map, visited);
                }
            }
        }
    }

    public int NumRoutes() {
        return numEnds;
    }

    //This is the worst function i have ever written - extremely slow
    //Only use for debugging
    public String toString(int[][] map) {
        StringBuilder sb = new StringBuilder();
        for(int y = 0; y < map.length; y++) {
            for(int x = 0; x < map[y].length; x++) {
                Node n = new Node(x, y, -1);

                if(members.contains(n)) {
                    for(Node m : members) {
                        if(m.x == x && m.y == y) {
                            sb.append(m.height);
                            break;
                        }
                    }
                } else {
                    sb.append('.');
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
