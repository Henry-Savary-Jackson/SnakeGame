
package util;

import java.awt.geom.Point2D;

public class Tile {
    
    public Point2D.Float topLeft ;
    
    public int width;
    public int height;
    
    public String type;
    
    public Tile(String t,int X, int Y, int w, int h){
        type = t;
        topLeft = new Point2D.Float(X,Y);
        width = w;
        height = h;
    }
}
