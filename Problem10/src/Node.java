import java.util.ArrayList;
import java.util.Objects;

public class Node {
    public int x, y, height;
    public ArrayList<Node> connectedNodes;

    public Node(int x, int y, int height) {
        this.x = x;
        this.y = y;
        this.height = height;
        connectedNodes = new ArrayList<>();
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Node that = (Node) obj;
        return this.x == that.x && this.y == that.y;
    }

    public String toString() {
        return "(" + x + ", " + y + ") h: " + height;
    }
}
