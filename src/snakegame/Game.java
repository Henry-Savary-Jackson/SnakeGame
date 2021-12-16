/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakegame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.JLabel;
import javax.swing.Timer;
import util.Snake;
import util.Snake.BodyPart;
import util.Tile;

public class Game extends JLabel{
    
    public int length;
    public boolean gameOver = false;
    
    public final float tileBorderPercent = 0.05f;
    
    public int tileWidth;
    public int tileHeight;
    
    public BufferedImage buffer;
    
    // Tile[width][height]
    public Tile[][] tiles = new Tile[20][20];
    
    public Game(int w, int h){
        super();
        setSize(w, h);
        
        setPreferredSize(getSize());
        buffer = createBuffer();
        
        tileWidth = getWidth()/tiles.length;
        tileHeight = getHeight()/tiles[0].length;
        
        createTiles();
        
        Snake snake = new Snake(10,10,this);
        
        Timer timer = new Timer(2000,(evt)->{
            Random rand = new Random();
            int tX = rand.nextInt(tiles.length-2)+1;
            int tY = rand.nextInt(tiles[0].length-2)+1;
            
            Tile tile = tiles[tX][tY];
            
            tile.type = "f";
            
            drawTile(tile);
            
            repaint((int)tile.topLeft.x,(int)tile.topLeft.y, tile.width, tile.height);
            System.out.println("timer tick");
            
        });
        timer.start();

    }
    
    //initializes the tiles in the screen and draws them
    public void createTiles(){
        
        final int width = (int) (tileWidth * (1-(2*tileBorderPercent)));
        final int height = (int)(tileHeight * (1-(2*tileBorderPercent)));
        int incrX = (int) (tileWidth * tileBorderPercent);
        for (int c = 0; c < tiles.length; c++){
            int incrY =(int) (tileHeight * tileBorderPercent);
            if( c == 0 || c ==tiles.length -1){
                for (int r = 0; r< tiles[c].length; r ++){
                    tiles[c][r] = new Tile("e",incrX , incrY ,width,height);
                    drawTile(tiles[c][r]);
                    incrY += tileHeight;
                }
            } else{
                for (int r = 0; r< tiles[c].length; r ++){
                    String type = "a";
                    if (r ==0 || r == tiles[c].length-1) {
                        type = "e";
                    }
                    tiles[c][r] = new Tile(type,incrX , incrY ,width,height);
                    drawTile(tiles[c][r]);
                    incrY += tileHeight;
                }
            }
            incrX += tileWidth;
        }
        System.out.println("Tiles created");
    }
    
    public void drawTile(Tile tile){
        if (buffer == null){
            return;
        }
        Color clr = Color.WHITE; 
        switch(tile.type){
            case "s": clr = Color.GREEN;
            break;
            
            case "e": clr = Color.GRAY;
            break;
            
            case "f": clr = Color.RED;
            break;
            
            case "a": clr = Color.WHITE;
            break;
        }
        Graphics2D g2d = buffer.createGraphics();
        g2d.setColor(clr);
        g2d.fillRect((int)tile.topLeft.x, (int)tile.topLeft.y, tile.width, tile.height);
        g2d.dispose();
    }
    
    @Override
    public void paint(Graphics g){
        if (buffer != null){
            g.drawImage(buffer, 0, 0, this);
        }
    }
    
    public BufferedImage createBuffer(){
        BufferedImage buff = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = buff.createGraphics();
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, buff.getWidth(), buff.getHeight());
        g2.dispose();
        return buff;
    }
    
}
