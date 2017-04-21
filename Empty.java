//-------------------- Geometry interface -----------------------------
import java.awt.Point;
/**
 * Geometry interface defines opjects that have a location, width and
 *     height.
 * @author rdb
 * 03/26/14
 */
public class Empty implements Geometry 
{
    protected int empty = 666;
    //------------------ Empty() ---------------
    /**
     * Return the x value of the object's location.
     * @return int
     */
    public Empty()
    {
        
    }
    //------------------ getX() ---------------
    /**
     * Return the x value of the object's location.
     * @return int
     */
    public int getX()
    {
        return empty;
    }
    //------------------ getY() ---------------
    /**
     * Return the y value of the object's location.
     * @return int
     */
    public int getY()
    {
        return empty;
    }
    //------------------ getLocation() ---------------
    /**
     * Return the object's location as a Point.
     * @return Point
     */    
    public Point getLocation()
    {
        return new Point( empty, empty );
    }
    //------------------ getHeight() ---------------
    /**
     * Return the height of the object's bounding box.
     * @return int
     */    
    public int getHeight()
    {
        return empty;
    }
    //------------------ getWidth() ---------------
    /**
     * Return the height of the object's bounding box.
     * @return int
     */    
    public int getWidth()
    {
        return empty;
    }
}
