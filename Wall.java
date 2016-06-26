import java.awt.*;
import java.lang.Math.*;

public class Wall
{
    // x and y are the center cordinate postions for the wall
    private double x;
    private double y;
    
    //other properties for the wall
    private double width;
    private double length;
    private Color color;
    
    //Array that has the four x/y positions for the corners
    private double[] xPoints;
    private double[] yPoints;
    
 
    public Wall (double horizontal, double vertical, double CenterX, double CenterY, Color shade)
    {
        width = horizontal;
        length = vertical;
        color = shade;
        x = CenterX;
        y = CenterY;
       
        xPoints = new double[4];
        yPoints = new double[4];
        
        //build the corners of the wall using center as main refrence
        //progresses around the wall in clockwise order starting at top right
      
        xPoints[0] = x + width/2;
        xPoints[1] = x + width/2;
        xPoints[2] = x - width/2;
        xPoints[3] = x - width/2;
        
        yPoints[0] = y - length/2;
        yPoints[1] = y + length/2;
        yPoints[2] = y + length/2;
        yPoints[3] = y - length/2;
        
    }
    
    //will say if an array of points lies inside the wall boundary
    //the x and y array must be the same length
    public boolean Boundary(double[] TestX, double[] TestY)
    {
        boolean test = false;
       
       for(int i = 0; i < TestX.length; i++)
       {
            test = Or(test, CheckPoint(TestX[i],TestY[i]));        
       }
        return test;
    }
    public boolean CheckPoint(double TestX, double TestY)
    {
        if( (xPoints[3] < TestX)&&(TestX < xPoints[0])&&(yPoints[0] < TestY)&&(TestY < yPoints[1]) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public boolean Or(boolean one, boolean two)
    {
        if(one||two)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
   
    public void draw( Graphics page)
    {
      
        int [] xCorners = new int[4];
        int [] yCorners = new int[4];
        
        for(int i = 0; i < 4; i++)
        {
            xCorners[i] = (int)xPoints[i];
            yCorners[i] = (int)yPoints[i];
        }
        page.setColor( color);
        page.fillPolygon( xCorners, yCorners, 4);
        
    }
    
            
    public void setColor( Color shade)
    {
        color = shade;
    }
    public Color getColor()
    {
        return color;
    }
    public double getX()
    {
        return x;
    }
    public double getY()
    {
        return y;
    }
}