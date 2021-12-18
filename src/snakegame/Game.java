
package snakegame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import util.Snake;
import util.Tile;

public class Game extends JPanel{
    
    public int length;
    public boolean gameOver = false;
    
    //constant for the width of the black border in each tile position
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
    //grid for each position  within the scene
    public Tile[][] tiles = new Tile[20][20];
    
    public Game(int w, int h, JFrame f){
        super();
        setSize(w, h);
        
        frame = f;
        
        setPreferredSize(getSize());
        
        buffer = createBuffer();
        
        tileWidth = getWidth()/tiles.length;
        tileHeight = getHeight()/tiles[0].length;
        
        createTiles();
        createStart("Start");
        
        add(start);
        
        timer = new Timer(250,(event)->{
            //updates to occur 4 times a second
                     if(ticks % 4 == 0){
                         time ++;
                         updateJFrame();
                     }
                     if (!gameOver){
                         snake.canPress = true;
                         snake.Update(this);

                         ticks++;

                     }else {
                         gameOver();
                     } 
                     
        });
        
        start.addActionListener((evt)->{
            if (evt.getSource() == start){
                //reset the whole scene and makes it playable
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
        
        
    }
    
    public void createFood(){
        Random rand = new Random();
        //chooses a random tile position to put the food inside the edges
        int tX = rand.nextInt(tiles.length-2)+1;
        int tY = rand.nextInt(tiles[0].length-2)+1;
        Tile tile = tiles[tX][tY];
        
        //if first chosen position is invalid, retry until it is
        while(!tile.type.equals("a")){
            tX = rand.nextInt(tiles.length-2)+1;
            tY = rand.nextInt(tiles[0].length-2)+1;
            tile = tiles[tX][tY];
        }
        //draws the food tile
        tile.type = "f";
        drawTile(tile);
    }
    
    //used to only update the statistics part of the screen in the botto,
    public void updateJFrame(){
        int x = 0;
        int y = this.getHeight();
        int width = frame.getWidth();
        int height = frame.getHeight() - y;
        frame.repaint(x,y,width,height);
    }
    
    public void gameOver() {
        //stops timer, disallows user input, then displays restart button
        gameOver = true;
        timer.stop();
        SwingUtilities.invokeLater(()->{
            this.removeKeyListener(snake);
        });
        createStart("Restart");
    }
    
    public void createStart(String r){
        //shows the start or restart button
        start.setText(r);
        start.setVisible(true);
        start.requestFocus();
    }
    
    //initializes the <tiles> in the screen and draws them
    public void createTiles(){
        
        //decides width, height, and x offset of every tile
        final int width = (int) (tileWidth * (1-(2*tileBorderPercent)));
        final int height = (int)(tileHeight * (1-(2*tileBorderPercent)));
        int incrX = (int) (tileWidth * tileBorderPercent);
        for (int c = 0; c < tiles.length; c++){
            //decides the y offset for every tile
            int incrY =(int) (tileHeight * tileBorderPercent);
            if( c == 0 || c ==tiles.length -1){
                //if the current x position is the first or last one, fill the entire column
                //with edge tiles
                for (int r = 0; r< tiles[c].length; r ++){
                    tiles[c][r] = new Tile("e",incrX , incrY ,width,height);
                    drawTile(tiles[c][r]);
                    incrY += tileHeight;
                }
            } else{
                //if not, simply make the first and last tiles in the column edge tiles,
                // and the rest air tiles
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
    
    //draws the colour of a tile based on its <type>
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
        //draws it on the <BufferedImage> object
        g2d.fillRect((int)tile.topLeft.x, (int)tile.topLeft.y, tile.width, tile.height);
        g2d.dispose();
        //updates only the space the drawn tile takes up on the screen
        repaint((int)tile.topLeft.x,(int)tile.topLeft.y, tile.width, tile.height);
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if (buffer != null){
            g.drawImage(buffer, 0, 0, this);
        }
        
    }
    
    //creates the black background to the <BufferedImage> when it is first created
    public BufferedImage createBuffer(){
        BufferedImage buff = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = buff.createGraphics();
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, buff.getWidth(), buff.getHeight());
        g2.dispose();
        return buff;
    }
    
}
