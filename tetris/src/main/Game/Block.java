package Game;

import java.awt.Point;
import java.util.Random;

import Tetris.Tetris;
import Enums.BlockType;

public class Block {
    public Point center;
    public final Point points[];
    public final BlockType type;
    public final boolean isOriginalRotation;

    public Block(BlockType blockType, Point[] points, boolean original) {
        this.center = new Point(Tetris.BOARD_WIDTH/2, Tetris.BOARD_HEIGHT-1);
        isOriginalRotation = original;
        this.points = points;
        this.type = blockType;
    }

    public Block(Random rand) {
        BlockType pieceType = BlockType.values()[rand.nextInt(BlockType.values().length)];
        this.center = new Point(Tetris.BOARD_WIDTH/2, Tetris.BOARD_HEIGHT-1);
        this.isOriginalRotation = true;
        this.points = pieceType.points;
        this.type = pieceType;
    }

    void moveSideways(int direction){
        switch(direction){
            case -1:
            case 1:
                moveBlock(direction, 0);
                break;
        }
    }

    void moveBlock(int x, int y){
        center = new Point(center.x + x, center.y + y);
    }

    public Block rotate() {
        if (type.rotations == 0) {
            return this;
        } else if (type.rotations == 2) {
            if (isOriginalRotation) {
                return new Block(type, rotateRight(points), false);
            } else {
                return new Block(type, rotateLeft(points), true);
            }
        }
        return new Block(type, rotateRight(points), true);
    }

    private Point[] rotateLeft(Point points[]) {
        return rotate(points, 1, -1);
    }

    private Point[] rotateRight(Point points[]) {
        return rotate(points, -1, 1);
    }

    private Point[] rotate(Point points[], int x, int y) {
        Point rotated[] = new Point[4];

        for (int i = 0; i < 4; i++) {
            int temp = points[i].x;
            rotated[i] = new Point(x * points[i].y, y * temp);
        }

        return rotated;
    }
}