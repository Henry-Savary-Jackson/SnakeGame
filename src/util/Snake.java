
package util;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
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
        
        //sets the arrow inputs to use
        keyInputs.put(KeyEvent.VK_UP, new int[]{0,-1});
        keyInputs.put(KeyEvent.VK_DOWN, new int[]{0,1});
        keyInputs.put(KeyEvent.VK_LEFT, new int[]{-1,0});
        keyInputs.put(KeyEvent.VK_RIGHT, new int[]{1,0});
        
        //creates the snake's <body>
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
        //renders every element of the <body> arraylist
        for (int t = 0; t < body.size(); t++){
            //get the entry's current position
            int[] prevPos = body.get(t).tilePos;
            int[] prevDir = body.get(t).dir;
            //move it its current direction
            int[] newPos = new int[]{prevPos[0]+prevDir[0],prevPos[1]+ prevDir[1]};
            if(t == 0){
                //checks to see the head came into contact with food or a fatal tile type
                feed = "f".equals(g.tiles[newPos[0]][newPos[1]].type);
                if("e".equals(g.tiles[newPos[0]][newPos[1]].type) || "s".equals(g.tiles[newPos[0]][newPos[1]].type)){
                    game.gameOver = true;
                }
            }else{
                //once the new position has been chosen, change the <BodyPart>'s direction 
                //to the previous tile in order to keep the snake continuous
                body.get(t).dir = new int[]{body.get(t-1).tilePos[0]-newPos[0],body.get(t-1).tilePos[1]-newPos[1]};
                if (t == body.size()-1 ){
                    //clears the last tile postion
                    g.tiles[prevPos[0]][prevPos[1]].type = "a";
                    g.drawTile(g.tiles[prevPos[0]][prevPos[1]]);
                }
            }
            //draws the new tile position of the <BodyPart>
            g.tiles[newPos[0]][newPos[1]].type = "s";
            g.drawTile(g.tiles[newPos[0]][newPos[1]]);
            
            //updates the <BodyPart's> position
            body.get(t).tilePos = newPos;
        }
        if (feed){
            //updates the score if the snake fed
            feed = false;
            g.score ++;
            //updates the display of time and score
            g.updateJFrame();
            addLength(4);
            //create the next food to catch
            g.createFood();
        }
    }
    //appends the additional bodyparts <count> amount of times
    void addLength(int count){
        
        for (int i = 0; i <count ; i++){
            //Get the direction and tile postion of the <body>'s last <BodyPart>
            int[] lastDir = body.get(body.size()-1).dir;
            int[] lastPos = body.get(body.size()-1).tilePos;
            
            //moves in the inverted aforementioned direction 
            int[] backDir = invertDir(lastDir);
            int[] newBodyPos = new int[]{lastPos[0]+backDir[0], lastPos[1]+backDir[1]};

            //checks whether the tile position chosen is invalid
            if(!game.tiles[newBodyPos[0]][newBodyPos[1]].type.equals("a")){
                //uses <findNewValidPos> method to find a valid position around
                int[] solution = findNewValidPos(lastPos,backDir);

                if(solution != null){
                    //if a valid position has been found add a bodypart a that tile postion
                    //and set its direction towards the last body part of the <body>list
                    int[] prev = body.get(body.size()-1).tilePos;
                    int[] diff = new int[]{prev[0]-solution[0], prev[1] - solution[1]};
                    body.add(new BodyPart(solution,diff));
                }else {
                    // otherwise disallow any other bodyPart's to be added
                    break;
                }
            }else{ 
                //if the chosen postion was valid add it along with the direction of the previous <BodyPart>
                body.add(new BodyPart(newBodyPos,lastDir));
            }
        }
    }
    
    //useful for changing directions in <BodyPart>s
    public int[] invertDir(int[] dir){
        return new int[]{-dir[0],-dir[1]};
    }
    
    //finds a valid tile position,<pos>, around a tile position to add a <BodyPart>
    //using the current last <BodyPart>'s current invalid direction
    int[] findNewValidPos(int[] pos, int[] dir){
        int[] tryPos;
        //checks to see whether the direction is upwards-downwards
        if(dir[0] == 0){
            //attempt to find a valid position to its left
             tryPos = new int[]{pos[0] -1, pos[1]};
            if (!game.tiles[tryPos[0]][tryPos[1]].type.equals("a") ){
                //if that fails, attempt to find a valid position to its right
                tryPos = new int[]{pos[0] +1, pos[1]};
                if (!game.tiles[tryPos[0]][tryPos[1]].type.equals("a") ){
                    //if all fails, return a null solution
                    tryPos = null;
                }
            } 
        } else{
        //if the direction is moving leftwards-rightwards, do the same thing
        //but upwards-downards
            tryPos = new int[]{pos[0], pos[1] -1};
            //checks upwards
            if (!game.tiles[tryPos[0]][tryPos[1]].type.equals("a")){
                tryPos = new int[]{pos[0], pos[1] + 1};
                //checks downards
                if (!game.tiles[tryPos[0]][tryPos[1]].type.equals("a")){
                    tryPos = null;
                }
            } 
        }
        return tryPos;
    }

    //<keyTyped> doesn't seem to work with timer, but <keyPressed> does oddly enough
    @Override
    public void keyTyped(KeyEvent ke) {}

    @Override
    public void keyPressed(KeyEvent ke) {
        int code = ke.getKeyCode();
        //check to see if it's an arrow key and the player hasn't pressed already
        if (keyInputs.containsKey(code) && canPress){
            canPress = false;
            int[] keyDir = keyInputs.get(code);
            int[] headDir = body.get(0).dir;
            //checks if the direction of the chosen key is perpendicular to the player's
            //current direction
            if (keyDir[0] != headDir[0] && keyDir[1] != headDir[1]){
                body.get(0).dir = keyDir;
            }
        }}

    @Override
    public void keyReleased(KeyEvent ke) {}
    
    //useful class for defining a unit of the snake's body
    public class BodyPart{
        //its index in <tiles>
        int[] tilePos;
        
        //the direction it will move on the next tick
        int[] dir;
        
        public BodyPart(int[]pos, int[] inDir){
            tilePos = pos;
            dir = inDir;
        }
    }
}


