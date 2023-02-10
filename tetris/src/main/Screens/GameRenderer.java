package Screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.*;
import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Tetris.Tetris;
import Enums.BlockType;
import Enums.GameState;
import Game.Block;
import Game.Node;
import Game.Player;

public class GameRenderer extends JPanel{
    private BufferedImage Apple, Avocado, Border, Grape, Jsos, Melon, Banana, Strawberry, GameScreen;
    private GameState gameState;
    public JPanel menuScreen;
    public JPanel gameScreen;
    private int nodeWidth;
    private int nodeHeight;
    private int nodeSize;
    private Player[] players;

    public void render(GameState state, Player[] players){
        this.players = players;
        this.gameState = state;
        repaint();
    }

    public GameRenderer(int width, int height, int size){
        nodeWidth = width;
        nodeHeight = height;
        nodeSize = size;
        getAssets();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(GameScreen, 0, 0, getWidth(), getHeight(), null );
        drawFrame((Graphics)g);
    }

    public void registerKeys(Player player){
        addKeyListener(player.controls);
    }

    public void unregisterKeys(Player player){
        removeKeyListener(player.controls);
    }

    private void getAssets(){
        try {
            Apple = ImageIO.read(new File("assets/Apple.png"));
            Avocado = ImageIO.read(new File("assets/Avocado.png"));
            Banana = ImageIO.read(new File("assets/Banana.png"));
            Border = ImageIO.read(new File("assets/Border.png"));
            Grape = ImageIO.read(new File("assets/Grape.png"));
            Jsos = ImageIO.read(new File("assets/Jsos.png"));
            Melon = ImageIO.read(new File("assets/Melon.png"));
            Strawberry = ImageIO.read(new File("assets/Strawberry.png"));
            GameScreen = ImageIO.read(new File("assets/GameScreen.png"));
        }
        catch (IOException e) {}
    }

    private void drawFrame(Graphics g) {
        for(Player pl : players){
            if(pl==null) continue;
            switch(gameState){
                case Menu:
                    break;
                case InGame:
                case GameOver:
                    drawBlocks(g, pl);
                    drawNextBlock(g, pl);
                    drawStatus(g, pl);
                    drawPlayerLabel(g, pl);
                    break;
                case Paused:
                    drawBlocks(g, pl);
                    drawNextBlock(g, pl);
                    drawStatus(g, pl);
                    drawPlayerLabel(g, pl);
                    drawPauseScreen(g);
                    break;
            }
        }
    }

    private void drawBlocks(Graphics g, Player p) {
        //copy original
        Node[][] cells = new Node[nodeWidth][nodeHeight];
        for (int x = 0; x < nodeWidth; x++) {
            for(int y = 0; y < nodeHeight; y++){
                cells[x][y] = p.board[x][y];
            }
        }

        //add current block
        for (Point point : p.currentBlock.points) {
            int x = point.x + p.currentBlock.center.x;
            int y = point.y + p.currentBlock.center.y;
            cells[x][y] = new Node(p.currentBlock.type);
        }

        for (int x = 0; x < nodeWidth; x++) {
            for (int y = 0; y < nodeHeight; y++) {
                if(cells[x][y].isEmpty()){
                    drawEmpty(g, p.upperLeftCorner.x + x * nodeSize, p.upperLeftCorner.y + ((Tetris.BOARD_HEIGHT-1) - y) * nodeSize, Color.black);
                }
                else{
                    drawBlock(g, p.upperLeftCorner.x + x * nodeSize, p.upperLeftCorner.y + ((Tetris.BOARD_HEIGHT-1) - y) * nodeSize, cells[x][y].getType());
                }
            }
        }
        
        g.setColor(Color.white);
        g.drawRect(p.upperLeftCorner.x, p.upperLeftCorner.y, nodeWidth * nodeSize, nodeHeight * nodeSize);
    }
    
    private void drawStatus(Graphics g, Player pl) {
        g.setFont(new Font("Dialog", Font.PLAIN, 16));
        Point upperLeftCorner = new Point(pl.upperLeftCorner.x - (6 * nodeSize), pl.upperLeftCorner.y);
        g.setColor(Color.black);
        g.fillRect(upperLeftCorner.x, upperLeftCorner.y, 5 * nodeSize, 5 * 20);
        g.setColor(Color.white);
        g.drawRect(upperLeftCorner.x, upperLeftCorner.y, 5 * nodeSize, 5 * 20);
        g.drawString(String.format("Score: %1s", pl.score), upperLeftCorner.x + nodeSize/2, upperLeftCorner.y + nodeSize);
        g.drawString(String.format("Speed level: %1s", pl.getLevel()), upperLeftCorner.x + nodeSize/2, upperLeftCorner.y + 2*nodeSize);
        g.drawString(String.format("Filled lines: %1s", pl.lines), upperLeftCorner.x + nodeSize/2, upperLeftCorner.y + 3*nodeSize);
    }

    private void drawPlayerLabel(Graphics g, Player pl){
        g.setFont(new Font("Dialog", Font.PLAIN, 32));
        Point upperLeftCorner = new Point(pl.upperLeftCorner.x, pl.upperLeftCorner.y - 4*nodeSize);
        g.setColor(Color.black);
        g.fillRect(upperLeftCorner.x, upperLeftCorner.y, nodeWidth * nodeSize, 3*nodeSize);
        g.setColor(Color.white);
        g.drawRect(upperLeftCorner.x, upperLeftCorner.y, nodeWidth * nodeSize, 3*nodeSize);
        g.drawString("Player " + pl.playerId, upperLeftCorner.x + 3 * nodeSize, upperLeftCorner.y + 2 * nodeSize);
    }

    private void drawPauseScreen(Graphics g) {
        g.setFont(new Font("Dialog", Font.PLAIN, 32));
        Color transparent = Color.black;
        transparent = new Color(transparent.getRed(), transparent.getGreen(), transparent.getBlue(), 150);
        g.setColor(transparent);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.black);
        g.fillRect(getWidth()/2 - 16 * nodeSize, getHeight()/2 - 4 * nodeSize, 32 * nodeSize, 8 * nodeSize);
        g.setColor(Color.white);
        g.drawString("GAME PAUSED", getWidth()/2 - 6 * nodeSize, getHeight()/2 + 16);
    }

    private void drawNextBlock(Graphics g, Player pl) {
        g.setFont(new Font("Dialog", Font.PLAIN, nodeSize/2));
        Block block = pl.nextBlock;
        Point upperLeftCorner = new Point(pl.upperLeftCorner.x - (6 * nodeSize), pl.upperLeftCorner.y + 5*nodeSize);
        Point blockCenter = new Point(upperLeftCorner.x + (2 * nodeSize), upperLeftCorner.y + (1 * nodeSize));
        g.setColor(Color.black);
        g.fillRect(upperLeftCorner.x, upperLeftCorner.y - nodeSize, (4+1) * nodeSize, nodeSize);


        g.fillRect(upperLeftCorner.x, upperLeftCorner.y, (4+1) * nodeSize, 4 * nodeSize);
        for (Point p : block.type.points) {
            switch(block.type){
                case I:
                    drawBlock(g, blockCenter.x + (p.x) * nodeSize + nodeSize/2, blockCenter.y - (p.y) * nodeSize + nodeSize/2, block.type);
                    break;
                case O:
                    drawBlock(g, blockCenter.x + (p.x) * nodeSize + nodeSize/2, blockCenter.y - (p.y) * nodeSize, block.type);
                    break;
                default:
                    drawBlock(g, blockCenter.x + (p.x) * nodeSize, blockCenter.y - (p.y) * nodeSize, block.type);
            }
        }
        g.setColor(Color.white);
        g.drawRect(upperLeftCorner.x, upperLeftCorner.y, (4+1) * nodeSize, 4 * nodeSize);
        g.drawRect(upperLeftCorner.x, upperLeftCorner.y - nodeSize, (4+1) * nodeSize, nodeSize);
        g.drawString("Next Block:", upperLeftCorner.x + nodeSize, upperLeftCorner.y - nodeSize/4);
    }

    private void drawEmpty(Graphics g, int x, int y, Color color) {
        g.setColor(color);
        g.fillRect(x, y, nodeSize, nodeSize);
        g.setColor(Color.gray);
        g.drawRect(x, y, nodeSize, nodeSize);
    }

    private void drawBlock(Graphics g, int x, int y, BlockType type){
        g.setColor(type.color);
        g.fillRect(x, y, nodeSize, nodeSize);
        g.setColor(Color.black);
        g.drawRect(x, y, nodeSize, nodeSize);
        g.drawImage(imgFromType(type), x, y, nodeSize, nodeSize, null);
        g.drawImage(Border, x, y, nodeSize, nodeSize, null);
    }

    private BufferedImage imgFromType(BlockType type){
        switch(type){
            case O:
                return Apple;
            case I:
                return Banana;
            case S:
                return Grape;
            case Z:
                return Melon;
            case L:
                return Strawberry;
            case J:
                return Jsos;
            case T:
                return Avocado;
        }
        return Border;
    }
}
