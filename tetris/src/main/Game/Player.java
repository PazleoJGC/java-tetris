package Game;

import java.awt.Point;

import Tetris.Tetris;
import Enums.KeyName;

public class Player {
    public int playerId = 0;
    public boolean gameLost = false;
    public boolean requestPause = false;
    public boolean playSound = false;
    private int fallLength;
    public Block currentBlock;
    public Block nextBlock;
    public Node[][] board;
    public Point upperLeftCorner;
    public Controls controls;
    
    public long lastDropTime = System.currentTimeMillis() + 1000;
    public int lines = 0;
    public int score = 0;

    public Player(int id, Point upperLeftCorner){
        this.playerId = id;
        this.controls = new Controls(id);
        this.upperLeftCorner = upperLeftCorner;
    }

    public void startGame(){
        currentBlock = null;
        gameLost = false;
        board = new Node[Tetris.BOARD_WIDTH][Tetris.BOARD_HEIGHT];
        for (int x = 0; x < Tetris.BOARD_WIDTH; x++) {
            for(int y = 0; y < Tetris.BOARD_HEIGHT; y++){
                board[x][y] = new Node();
            }
        }
        nextBlock = new Block(Tetris.random);
        currentBlock = spawnBlock(currentBlock, new Block(Tetris.random), board);
    }

    public void update(){
        if (controls.getKeyDown(KeyName.Rotate)) {
            Block rot = currentBlock.rotate();
            rot.center = currentBlock.center;
            if (canMove(rot, 0, 0)) {
                currentBlock = rot;
            }
        }
        if (controls.getKeyDown(KeyName.Left)) {
            if (canMove(currentBlock, -1, 0)) {
                currentBlock.moveSideways(-1);
            }
        }
        if (controls.getKeyDown(KeyName.Right)) {
            if (canMove(currentBlock, 1, 0)) {
                currentBlock.moveSideways(1);
            }
        }
    }

    public boolean getPause(){
        return controls.getKeyDown(KeyName.Pause);
    }

    private boolean canMove(int dirX, int dirY){
        for (Point point : currentBlock.points) {
            int x = currentBlock.center.x + point.x + dirX;
            int y = currentBlock.center.y + point.y + dirY;

            if (x < 0 || x >= Tetris.BOARD_WIDTH || y >= Tetris.BOARD_HEIGHT || y < 0) {
                return false;
            }

            if (!board[x][y].isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private boolean canMove(Block fig, int dirX, int dirY){
        for (Point point : fig.points) {
            int x = fig.center.x + point.x + dirX;
            int y = fig.center.y + point.y + dirY;

            if (x < 0 || x >= Tetris.BOARD_WIDTH || y >= Tetris.BOARD_HEIGHT || y < 0) {
                return false;
            }

            if (!board[x][y].isEmpty()) {
                return false;
            }
        }

        return true;
    }

    public int getLevel() {
        int maxLvl = 10;
        int lvlStep = 10;
        if ((lines >= 1) && (lines <= (maxLvl-1)*lvlStep)) {
            return 1 + ((lines - 1) / lvlStep);
        } else if (lines > (maxLvl-1)*lvlStep) {
            return maxLvl;
        } else {
            return 1;
        }
    }

    public void moveDown(){
        if (!canMove(0, -1)) {
            if (fallLength == 0) {
                gameLost = true;
            } else {
                playSound = true;
                score += (fallLength * getLevel());
                fallLength = 0;
                lastDropTime += 500;
                currentBlock = spawnBlock(currentBlock, nextBlock, board);
                removeFullRows();
                if (!canMove(0, 0)){
                    gameLost = true;
                    return;
                }
                nextBlock = new Block(Tetris.random);
            }
        } else {
            currentBlock.moveBlock(0, -1);
            fallLength++;
        }
    }

    public void extraMoveDown(){
        if (controls.getKey(KeyName.Drop)) {
            moveDown();
        }
    }

    private void removeFullRows() {
        Node[][] tempBoard = new Node[Tetris.BOARD_WIDTH][Tetris.BOARD_HEIGHT];
        //create empty board
        for (int x = 0; x < Tetris.BOARD_WIDTH; x++) {
            for(int y = 0; y < Tetris.BOARD_HEIGHT; y++){
                tempBoard[x][y] = new Node();
            }
        }

        //copy original board, skipping full rows
        int full = 0;
        for (int y = 0; y < Tetris.BOARD_HEIGHT; y++) {
            if (isRowFull(y)) {
                full++;
            } else {
                for (int x = 0; x < Tetris.BOARD_WIDTH; x++) {
                    tempBoard[x][y-full] = board[x][y];
                }
            }
        }

        lines += full;
        board = tempBoard;
    }

    private boolean isRowFull(int y) {
        for (int x = 0; x < Tetris.BOARD_WIDTH; x++) {
            if (board[x][y].isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private Block spawnBlock(Block current, Block newFig, Node[][] board){
        if (current != null) {
            for (Point point : current.points) {
                int x = current.center.x + point.x;
                int y = current.center.y + point.y;
                board[x][y] = new Node(current.type);
            }
        }
        return newFig;
    }

}
