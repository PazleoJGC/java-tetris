package Enums;
import java.awt.Color;
import java.awt.Point;

public enum BlockType {
    I(2, new Point[]{new Point(-2, 0), new Point(-1, 0), new Point(0, 0),   new Point(1, 0)}, Color.yellow),
    J(4, new Point[]{new Point(-1, 0), new Point(0, 0),  new Point(1, 0),   new Point(1, -1)}, Color.gray),
    L(4, new Point[]{new Point(-1, 0), new Point(0, 0),  new Point(1, 0),   new Point(-1, -1)}, Color.orange),
    O(0, new Point[]{new Point(-1, 0), new Point(0, 0),  new Point(-1, -1), new Point(0, -1)}, Color.red),
    S(2, new Point[]{new Point(0, 0),  new Point(1, 0),  new Point(-1, -1), new Point(0, -1)}, Color.blue),
    T(4, new Point[]{new Point(-1, 0), new Point(0, 0),  new Point(1, 0),   new Point(0, -1)}, Color.cyan),
    Z(2, new Point[]{new Point(-1, 0), new Point(0, 0),  new Point(0, -1),  new Point(1, -1)}, Color.green);

    public final int rotations;
    public final Point points[];
    public final Color color;

    BlockType(int rotations, Point[] points, Color color) {
        this.rotations = rotations;
        this.points = points;
        this.color = color;
    }
}
