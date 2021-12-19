
package util;

import java.awt.geom.Point2D;

public class Tile {
    
    public Point2D.Float topLeft ;
    
    public float width;
    public float height;
    
    public String type;
    
    public Tile(String t,float X, float Y, float w, float h){
        type = t;
        topLeft = new Point2D.Float(X,Y);
        width = w;
        height = h;
    }
}
