import java.util.Arrays;
import java.util.Collections;

public class Coordinate {
    public final int[] data;

    public Coordinate(int[] data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Coordinate other)) return false;
        return Arrays.equals(this.data, other.data);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(data);
    }
}
