package Game;

import Enums.BlockType;

public class Node {
    private BlockType blockType;

    public Node() {
        blockType = null;
    }

    public Node(BlockType type) {
        blockType = type;
    }

    public boolean isEmpty() {
        return blockType == null;
    }

    public BlockType getType() {
        return blockType;
    }
}
