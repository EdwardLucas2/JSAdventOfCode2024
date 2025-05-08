//Stroes the given state, map, visited location, guard position and direction
public class State {
    public int[][] map;
    public int gx, gy;
    public int direction, step; //0: up, 1: left, 2: down, 3: right
    public boolean exited;

    public State(int[][] map, int gx, int gy, int direction) {
        this.map = map;
        this.gx = gx;
        this.gy = gy;
        this.direction = direction;
        this.exited = false;
        this.step = 0;
    }

    //Clone init function
    public State(State state) {
        //Clone the array
        this.map = new int[state.map.length][state.map[0].length];
        for(int x = 0; x < state.map.length; x++) {
            for(int y = 0; y < state.map[0].length; y++) {
                map[x][y] = state.map[x][y];
            }
        }

        this.gx = state.gx;
        this.gy = state.gy;
        this.direction = state.direction;
        this.step = state.step;
        this.exited = state.exited;
    }

    //Used for debugging, returns the number of obstacles used
    public int numObstacles() {
        int obstacles = 0;
        for(int x = 0; x < map.length; x++) {
            for(int y = 0; y < map[0].length; y++) {
                if(map[x][y] == 2) {
                    obstacles++;
                }
            }
        }

        return obstacles;
    }
}
