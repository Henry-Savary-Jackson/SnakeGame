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
import java.util.Arrays;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import util.Snake;
import util.Tile;

public class Game extends JPanel{
    
    public int length;
    public boolean gameOver = false;
    
    public final float tileBorderPercent = 0.05f;
    
    public int tileWidth;
    public int tileHeight;
    
    public long ticks;
    
    public int time = 0;
    
    public int score = 0;
    
    public BufferedImage buffer;
    
    public Timer timer;
    
    public Snake snake;
    
    public JFrame frame;
    
    public JButton start = new JButton("Start");
    
    // Tile[width][height]
    public Tile[][] tiles = new Tile[20][20];
    
    public Game(int w, int h, JFrame f){
        super();
        setSize(w, h);
        
        frame = f;
        
        setPreferredSize(getSize());
        buffer = createBuffer();
        
        tileWidth = getWidth()/tiles.length;
        tileHeight = getHeight()/tiles[0].length;
        
        //createTiles();
        
        timer = new Timer(250,(event)->{
                    System.out.println(Arrays.toString(this.getKeyListeners()));
                     if(ticks % 4 == 0){
                         time ++;
                         updateJFrame();
                     }
                     if (!gameOver){
                         snake.canPress = true;
                         snake.Update(this);

                         ticks++;
                         System.out.println("timer tick" + String.valueOf(time));

                     }else {
                         gameOver();
                     } 
        });
        
        start.addActionListener((evt)->{
            if (evt.getSource() == start){
                time = 0;
                score = 0;
                ticks = 0;
                gameOver = false;
                createTiles();
                snake = new Snake(7,7,this);
                snake.Update(this);
                this.addKeyListener(snake);
                createFood();
                timer.start();
                start.setVisible(false);
                this.requestFocus();
            }
        });
        
        createStart("Start");
        start.setVisible(true);
        add(start);
        repaint();
    }
    
    public void createFood(){
        Random rand = new Random();
        int tX = rand.nextInt(tiles.length-2)+1;
        int tY = rand.nextInt(tiles[0].length-2)+1;
        Tile tile = tiles[tX][tY];
        
        while(!tile.type.equals("a")){
            tX = rand.nextInt(tiles.length-2)+1;
            tY = rand.nextInt(tiles[0].length-2)+1;
            tile = tiles[tX][tY];
        }
        tile.type = "f";
        drawTile(tile);
    }
    
    public void updateJFrame(){
        int x = 0;
        int y = this.getHeight();
        int width = frame.getWidth();
        int height = frame.getHeight() - y;
        frame.repaint(x,y,width,height);
    }
    
    public void gameOver() {
        gameOver = true;
        timer.stop();
        SwingUtilities.invokeLater(()->{
            this.removeKeyListener(snake);
        });
        createStart("Restart");
    }
    
    public void createStart(String r){
        start.setText(r);
        start.setVisible(true);
        start.setEnabled(true);
        start.requestFocus();
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
        repaint((int)tile.topLeft.x,(int)tile.topLeft.y, tile.width, tile.height);
    }
    
    @Override
    public void paint(Graphics g){
        super.paint(g);
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
