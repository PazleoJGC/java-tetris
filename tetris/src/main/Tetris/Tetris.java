package Tetris;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Enums.GameState;
import Game.Display;
import Game.Player;

public class Tetris{
    public static int WINDOW_WIDTH = 1200;
    public static int WINDOW_HEIGHT = 800;
    public static int NODE_WIDTH = 30;
    public static int BOARD_WIDTH = 10;
    public static int BOARD_HEIGHT = 20;
    public static Random random = new Random();
    private Display display;
    private GameState gameState = GameState.Menu;
    private int frameDelay = (1000/30);
    private Player player1;
    private Player player2;

    public Tetris() {
        display = new Display(this);
        display.setState(GameState.Menu, GetPlayers());
        gameLoop();
    }

    public static void main(String[] args) {
        new Tetris();
    }

    public void startSinglePlayer(){
        clearOldGame();
        gameState = GameState.InGame;
        player1 = new Player(1, new Point(WINDOW_WIDTH/2 - (BOARD_WIDTH/2 * NODE_WIDTH), 5*NODE_WIDTH));
        player1.startGame();
        display.setState(gameState, GetPlayers());
        display.registerKeys(player1);
    }

    public void startMultiplayer(){
        clearOldGame();
        gameState = GameState.InGame;
        player1 = new Player(1, new Point(7*NODE_WIDTH, 5*NODE_WIDTH));
        player2 = new Player(2, new Point((7+BOARD_WIDTH+10)*NODE_WIDTH, 5*NODE_WIDTH));
        player1.startGame();
        player2.startGame();
        display.setState(gameState, GetPlayers());
        display.registerKeys(player1);
        display.registerKeys(player2);
    }

    public Player[] GetPlayers(){
        return new Player[]{
            player1,
            player2
        };
    }

    private void gameLoop() {
        while (true) {
            tetrisLoop();
            display.update();
            try {
                Thread.sleep(frameDelay); //play at 30 fps
            }
            catch (Exception e) { }
        }
    }

    private void tetrisLoop(){
        boolean pauseRequest = false;
        List<Integer> playersLost = new ArrayList<Integer>();
        long timeNow = System.currentTimeMillis();

        for(Player pl : GetPlayers()){
            if(pl==null) continue;
            if(gameState == GameState.InGame){
                boolean nextMove = false;
                if (timeNow - pl.lastDropTime >= (long) ((((11 - pl.getLevel()) * 0.05) * 1000) + frameDelay)){
                    nextMove = true;
                    pl.lastDropTime = timeNow;
                }

                pl.update();
    
                if(nextMove){
                    pl.moveDown();
                }
                else{
                    pl.extraMoveDown();
                }
            }
            if (pl.getPause()){
                pauseRequest = true;
            }
            if(pl.gameLost){
                playersLost.add(pl.playerId);
            }
            if(pl.playSound){
                display.playSound();
                pl.playSound = false;
            }
        }

        if(!playersLost.isEmpty()){
            gameState = GameState.GameOver;
            display.setState(gameState, GetPlayers());
            gameState = GameState.Menu;
            display.setState(gameState, GetPlayers());
            clearOldGame();
        }
        if(pauseRequest){
            pauseGame();
        }
    }

    private void pauseGame(){
        if(gameState == GameState.InGame){
            gameState = GameState.Paused;
            display.setState(gameState, GetPlayers());
        }
        else if(gameState == GameState.Paused){
            gameState = GameState.InGame;
            display.setState(gameState, GetPlayers());
        }
    }

    private void clearOldGame(){
        if(player1 != null){
            display.unregisterKeys(player1);
            player1 = null;
        }
        if(player2 != null){
            display.unregisterKeys(player2);
            player2 = null;
        }
    }
}

