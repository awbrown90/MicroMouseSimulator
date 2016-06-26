import java.awt.*;
import java.lang.Math.*;
import java.util.ArrayList;

public class Sensor
{
    // x and y are the center cordinate postions for the Sensor
    private double PositionX;
    private double PositionY;
    
    private double TargetX;
    private double TargetY;
    
    //other properties for the Sensor
    private ArrayList<Wall> Collision = new ArrayList<Wall>();
    private double distance;
    private double scope;
    private double angle;
    private Color color;
    
    //a sensor is defined by its x and y postion as well as the angle its at
    public Sensor (double x, double y, double theta)
    {
        PositionX = x;
        PositionY = y;
        angle = theta;
       
        scope = 1;
        color = Color.red;
        
        double SearchX = PositionX;
        double SearchY = PositionY;
        
        while(!SensorHit(SearchX,SearchY) && !OutofBounds(SearchX,SearchY) )
        {
            SearchX =  SearchX +  scope*Math.cos(angle);
            SearchY =  SearchY -  scope*Math.sin(angle);
        }
      
        TargetX = SearchX;
        TargetY = SearchY;
        
        distance = Math.sqrt( (TargetX-PositionX)*(TargetX-PositionX) + (TargetY-PositionY)*(TargetY-PositionY) );
        
    }
    public void setCollision(ArrayList<Wall> list)
    {
        for(int i = 0; i < list.size(); i++)
        {
            AddCollision(list.get(i));
        }
    }
    public void AddCollision(Wall Model)
    {
       Collision.add(Model);
    }
    public void ClearCollision()
    {
        Collision.clear();
    }
    public boolean SensorHit(double xCordinate, double yCordinate)
    {
       for(int i = 0; i < Collision.size() ; i++)
       { 
            if(Collision.get(i).CheckPoint( xCordinate, yCordinate))
            {
                return true;
            }
       }
       return false;
    }
    public boolean OutofBounds(double xCordinate, double yCordinate)
    {
        if(xCordinate < 0|| xCordinate > 780||yCordinate < 0|| yCordinate > 780)
        {
            return true;
        }
        else
        {
            return false;
        }
    } 
    public void FindTarget()
    {
        
        //create vector from center to midpoint
        //move sensor by step amount in vector direction
        
        double SearchX = PositionX;
        double SearchY = PositionY;
        //new addition to make it so the laser doesnt travel too far and freeze up the program
        //int dist_count = 0;
        
        while(!SensorHit(SearchX,SearchY) && !OutofBounds(SearchX,SearchY) )
        //while(!SensorHit(SearchX,SearchY) && (dist_count < 80) )
        {
            SearchX =  SearchX +  scope*Math.cos(angle);
            SearchY =  SearchY -  scope*Math.sin(angle);
            //dist_count++;
        }
      
        TargetX = SearchX;
        TargetY = SearchY;
        
        distance = Math.sqrt( (TargetX-PositionX)*(TargetX-PositionX) + (TargetY-PositionY)*(TargetY-PositionY) );
    }
    public void draw( Graphics page)
    {
        page.setColor(color);
        page.drawLine((int)PositionX,(int)PositionY,(int)TargetX,(int)TargetY);
        
        Circle target = new Circle(2,color,TargetX,TargetY);
        target.draw(page);
    }
    public void setX(double x)
    {
        PositionX = x;
    }
    public void setY(double y)
    {
        PositionY = y;
    }
    public double getX()
    {
        return PositionX;
    }
    public double getY()
    {
        return PositionY;
    }
    public void setAngle(double theta)
    {
        angle = theta;
    }
    public double getAngle()
    {
        return angle;
    }
    public double getDistance()
    {
        return distance;
    }
    
}