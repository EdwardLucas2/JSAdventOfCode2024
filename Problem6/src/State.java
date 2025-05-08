//Stroes the given state, map, visited location, guard position and direction
public class State {
    public int[][] map;
    public int gx, gy;
    public int direction, step; //0: up, 1: left, 2: down, 3: right
    public boolean finished;

    public State(int[][] map, int gx, int gy, int direction) {
        this.map = map;
        this.gx = gx;
        this.gy = gy;
        this.direction = direction;
        this.finished = false;
        this.step = 0;
    }
}
