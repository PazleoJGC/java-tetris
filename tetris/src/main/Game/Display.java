package Game;

import java.awt.Dimension;
import java.io.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Tetris.Tetris;
import Enums.GameState;
import Screens.GameRenderer;
import Screens.MenuRenderer;

public class Display{
    private Tetris gameMain;
    private Clip menuMusic, gameMusic, fallSound;
    public JPanel menuScreen, gameScreen;
    public GameRenderer gameRenderer;
    public MenuRenderer menuRenderer;
    private Player[] players;
    private GameState gameState;

    public void update(){
        gameRenderer.render(this.gameState, this.players);
    }

    public Display(Tetris gameMain){
        this.gameMain = gameMain;
        int windowWidth = Tetris.WINDOW_WIDTH;
        int windowHeight = Tetris.WINDOW_HEIGHT;

        GetAssets();
        JFrame frame = new JFrame("Tetris");  
        frame.setPreferredSize(new Dimension(windowWidth, windowHeight));

        menuRenderer = new MenuRenderer(windowWidth, windowHeight, Tetris.NODE_WIDTH);
        menuRenderer.setSize(new Dimension(windowWidth, windowHeight));

        gameRenderer = new GameRenderer(Tetris.BOARD_WIDTH, Tetris.BOARD_HEIGHT, Tetris.NODE_WIDTH);
        gameRenderer.setSize(new Dimension(windowWidth, windowHeight));

        menuScreen = new JPanel();
        menuScreen.setLayout(null);
        menuScreen.setPreferredSize(new Dimension(windowWidth, windowHeight));
        menuScreen.setSize(new Dimension(windowWidth, windowHeight));

        JButton b=new JButton("One Player");   
        b.setBounds(windowWidth/2 - 200, 340, 400, 80);
        b.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent) {
                Display.this.gameMain.startSinglePlayer();
            } 
        });
        menuScreen.add(b);

        b=new JButton("Two Players");   
        b.setBounds(windowWidth/2 - 200, 440, 400, 80);
        b.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent) {
                Display.this.gameMain.startMultiplayer();
            } 
        });
        menuScreen.add(b);
        menuScreen.add(menuRenderer);

        gameScreen = new JPanel();
        gameScreen.setLayout(null);
        gameScreen.add(gameRenderer);
        gameScreen.setSize(new Dimension(windowWidth, windowHeight));

        frame.add(gameScreen);
        frame.add(menuScreen);
        
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        gameScreen.setVisible(false);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.requestFocus();
    }

    public void playSound(){
        fallSound.stop();
        fallSound.setMicrosecondPosition(0);
        fallSound.start();
    }

    public void registerKeys(Player player){
        gameRenderer.addKeyListener(player.controls);
    }

    public void unregisterKeys(Player player){
        gameRenderer.removeKeyListener(player.controls);
    }

    private void GetAssets(){
        try {
            menuMusic = AudioSystem.getClip();
            menuMusic.open(AudioSystem.getAudioInputStream(new File("assets/MenuTheme.wav")));
            gameMusic = AudioSystem.getClip();
            gameMusic.open(AudioSystem.getAudioInputStream(new File("assets/wyver9_ThornsnRopes.wav")));
            fallSound = AudioSystem.getClip();
            fallSound.open(AudioSystem.getAudioInputStream(new File("assets/Fall.wav")));
        }
        catch (Exception e) {}
    }

    public void setState(GameState state, Player[] players){
        this.players = players;
        this.gameState = state;
        switch(state){
            case InGame:
            case Paused:
                menuScreen.setVisible(false);
                gameScreen.setVisible(true);
                break;
            case GameOver:
                endGame();
                break;
            default:
                menuScreen.setVisible(true);
                gameScreen.setVisible(false);
                break;
        }
        playMusic(state);
        gameRenderer.requestFocus();
    }

    private void endGame(){
        int winner = -1; 
        int playerCount = 0;
        int loserCount = 0;
        for(Player pl : players){
            if(pl==null) continue;
            playerCount++;
            if(!pl.gameLost){
                winner = pl.playerId;
            }
            else{
                loserCount++;
            }
        }
        if(playerCount == 1){
            JOptionPane.showMessageDialog(null,
            "Game finished!");
        }
        else if(loserCount>1){
            JOptionPane.showMessageDialog(null,
            "Game ended in a draw!");
        }
        else{
            JOptionPane.showMessageDialog(null,
            "Congratulations, player " + winner + " won the game!");
        }
    }

    private void playMusic(GameState state){
        switch(state){
            default:
                gameMusic.stop();
                menuMusic.setMicrosecondPosition(0);
                menuMusic.start();
                menuMusic.loop(Clip.LOOP_CONTINUOUSLY);
                break;
            case InGame:
                menuMusic.stop();
                gameMusic.setMicrosecondPosition(0);
                gameMusic.start();
                gameMusic.loop(Clip.LOOP_CONTINUOUSLY);
                break;
        }
    }
}
