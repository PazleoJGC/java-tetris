package Screens;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class MenuRenderer extends JPanel{
    private BufferedImage MenuScreen;
    
    public MenuRenderer(int width, int height, int tileSize){
        GetAssets();
    }

    private void GetAssets(){
        try {
            MenuScreen = ImageIO.read(new File("assets/MenuScreen.png"));
        }
        catch (IOException e) {}
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(MenuScreen, 0, 0, getWidth(), getHeight(), null );
    }
}
