
package util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import snakegame.Game;

public class Snake implements KeyListener{
    
    public int[] HeadPos;
    
    public int[] HeadDir = {1,0};
   
    public HashMap<Integer,int[]> keyInputs = new HashMap<>();
    
    public Game game ;
    
    public ArrayList<BodyPart> body = new ArrayList<>();
    
    public Snake( int tileX , int tileY, Game g){
        
        keyInputs.put(KeyEvent.VK_UP, new int[]{0,1});
        keyInputs.put(KeyEvent.VK_DOWN, new int[]{0,-1});
        keyInputs.put(KeyEvent.VK_LEFT, new int[]{-1,0});
        keyInputs.put(KeyEvent.VK_RIGHT, new int[]{1,0});
        
        HeadPos = new int[]{tileX, tileY};
        body.add(new BodyPart(HeadPos,HeadDir));
        
        game = g;
        
    }
    
    //draw snake on bufferedImage, updates the length and postion of the body arraylist
    public void Update(){}

    @Override
    public void keyTyped(KeyEvent ke) {
        int code = ke.getKeyCode();
        if (keyInputs.containsKey(code)){
            int[] keyDir = keyInputs.get(code);
            
            if (keyDir[0] != HeadDir[0] && keyDir[1] != HeadDir[1]){
                HeadDir = keyDir;
                Update();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent ke) {}

    @Override
    public void keyReleased(KeyEvent ke) {}
    
    public class BodyPart{
        int[] tilePos;
        
        int[] dir;
        
        public BodyPart(int[]pos, int[] inDir){
            tilePos = pos;
            dir = inDir;
        }
        
        public void setTilePos(int[] newPos){
            tilePos = newPos;
        }
        
        public void setDirection(int[] newDir){
            dir = newDir;
        }
    
    }
}


