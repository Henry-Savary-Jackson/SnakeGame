
package util;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import snakegame.Game;

public class Snake implements KeyListener{
    
    public boolean canPress = true;
    
    public int[] HeadPos;
    
    public int[] initDir = {1,0};
   
    public HashMap<Integer,int[]> keyInputs = new HashMap<>();
    
    public Game game ;
    
    public ArrayList<BodyPart> body = new ArrayList<>();
    
    public Snake( int tileX , int tileY, Game g){
        
        keyInputs.put(KeyEvent.VK_UP, new int[]{0,-1});
        keyInputs.put(KeyEvent.VK_DOWN, new int[]{0,1});
        keyInputs.put(KeyEvent.VK_LEFT, new int[]{-1,0});
        keyInputs.put(KeyEvent.VK_RIGHT, new int[]{1,0});
        
        HeadPos = new int[]{tileX, tileY};
        body.add(new BodyPart(HeadPos,initDir));
        body.add(new BodyPart(new int[]{tileX-1, tileY},initDir));
        body.add(new BodyPart(new int[]{tileX-2, tileY},initDir));
        body.add(new BodyPart(new int[]{tileX-3, tileY},initDir));
        
        game = g;
        
    }
    
    //draw snake on bufferedImage, updates the length and postion of the body arraylist
    public void Update(Game g){
        if (g == null){
            return;
        }    
        boolean feed = false;
        for (int t = 0; t < body.size(); t++){
            int[] prevPos = body.get(t).tilePos;
            int[] prevDir = body.get(t).dir;
            int[] newPos = new int[]{prevPos[0]+prevDir[0],prevPos[1]+ prevDir[1]};
            if(t == 0){
                feed = "f".equals(g.tiles[newPos[0]][newPos[1]].type);
                if("e".equals(g.tiles[newPos[0]][newPos[1]].type) || "s".equals(g.tiles[newPos[0]][newPos[1]].type)){
                    game.gameOver = true;
                }
            }else{
                body.get(t).dir = new int[]{body.get(t-1).tilePos[0]-newPos[0],body.get(t-1).tilePos[1]-newPos[1]};
                if (t == body.size()-1 ){
                    g.tiles[prevPos[0]][prevPos[1]].type = "a";
                    g.drawTile(g.tiles[prevPos[0]][prevPos[1]]);
                }
            }
            g.tiles[newPos[0]][newPos[1]].type = "s";
            g.drawTile(g.tiles[newPos[0]][newPos[1]]);
            
            body.get(t).tilePos = newPos;
        }
        if (feed){
            feed = false;
            g.score ++;
            g.updateJFrame();
            addLength(3);
            g.createFood();
        }
    }
    
    void addLength(int count){
        
        for (int i = 0; i <count ; i++){
            int[] lastDir = body.get(body.size()-1).dir;
            int[] lastPos = body.get(body.size()-1).tilePos;
            
            int[] backDir = invertDir(lastDir);
            
            int[] newBodyPos = new int[]{lastPos[0]+backDir[0], lastPos[1]+backDir[1]};

            if(!game.tiles[newBodyPos[0]][newBodyPos[1]].type.equals("a")){
                int[] solution = findNewValidPos(lastPos,backDir);

                if(solution != null){
                    int[] prev = body.get(body.size()-1).tilePos;
                    int[] diff = new int[]{prev[0]-solution[0], prev[1] - solution[1]};
                    body.add(new BodyPart(solution,diff));
                }else {
                    break;
                }
            }else{ 
                body.add(new BodyPart(newBodyPos,lastDir));
            }
        }
    }
    
    public int[] invertDir(int[] dir){
        return new int[]{-dir[0],-dir[1]};
    }
    
    int[] findNewValidPos(int[] pos, int[] dir){
        int[] tryPos;
        if(dir[0] == 0){
             tryPos = new int[]{pos[0] -1, pos[1]};
            if (!game.tiles[tryPos[0]][tryPos[1]].type.equals("a") ){
                tryPos = new int[]{pos[0] +1, pos[1]};
                if (!game.tiles[tryPos[0]][tryPos[1]].type.equals("a") ){
                    tryPos = null;
                }
            } 
        } else{
            tryPos = new int[]{pos[0], pos[1] -1};
            if (!game.tiles[tryPos[0]][tryPos[1]].type.equals("a")){
                tryPos = new int[]{pos[0], pos[1] + 1};
                if (!game.tiles[tryPos[0]][tryPos[1]].type.equals("a")){
                    tryPos = null;
                }
            } 
        }
        return tryPos;
    }

    @Override
    public void keyTyped(KeyEvent ke) {System.out.println("Alive");}

    @Override
    public void keyPressed(KeyEvent ke) {
        int code = ke.getKeyCode();
        if (keyInputs.containsKey(code) && canPress){
            canPress = false;
            int[] keyDir = keyInputs.get(code);
            int[] headDir = body.get(0).dir;
            if (keyDir[0] != headDir[0] && keyDir[1] != headDir[1]){
                body.get(0).dir = keyDir;
            }
        }}

    @Override
    public void keyReleased(KeyEvent ke) {}
    
    public class BodyPart{
        int[] tilePos;
        
        int[] dir;
        
        public BodyPart(int[]pos, int[] inDir){
            tilePos = pos;
            dir = inDir;
        }
    }
}


