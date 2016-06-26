import java.awt.*;
import java.lang.Math.*;
import java.util.ArrayList;

public class Mouse
{
    // x and y are the center cordinate postions for the mouse
    private double x;
    private double y;
    
    //x and y cordinates for the right and left wheels
    private double xRightWheel;
    private double yRightWheel;
    
    private double xLeftWheel;
    private double yLeftWheel;
    
    //other properties for the box
    private Color color;
    
    //Array that has the x/y positions for the points of the mouse
    private double[] xPoints;
    private double[] yPoints;
    
    
    //a list of walls the the box could come in collision with
    private ArrayList<Wall> Collision = new ArrayList<Wall>();
    public ArrayList<Sensor> Sensors = new ArrayList<Sensor>();
    
    //steps per revolution
    private int stepMode;
    
    //the direction of each wheel, if 1 then clock wise if -1 ccw for rev
    private int RightDirection;
    private int LeftDirection;
    
    //distance of wheel seperation in cm
    private double WheelDistance;
    
    //in cm
    private double WheelDiameter;
    
    //change in angle after step
    private double turnAngle;
    
    private double scale_ratio;
    
    //parameter n is the scaling parameter for use in a n x n maze
    public Mouse (int n, double CenterX, double CenterY, Color shade)
    {
        double mouse_ref =  780/(16*n+1);
        scale_ratio = mouse_ref/12;
        color = shade;
        x = CenterX;
        y = CenterY;

        xRightWheel = x + 62.5*scale_ratio;
        yRightWheel = y;
        
        xLeftWheel = x - 62.5*scale_ratio;
        yLeftWheel = y;
        
        RightDirection = 1;
        LeftDirection = 1;
       
        xPoints = new double[14];
        yPoints = new double[14];
        
        //build the corners of the box using center as main refrence
        //progresses around the box in clockwise order starting at top right
        
        //main box
        xPoints[0] = x;
        xPoints[1] = x + 37.5*scale_ratio;
        xPoints[2] = x + 37.5*scale_ratio;
        xPoints[3] = x;
        xPoints[4] = x - 37.5*scale_ratio;
        xPoints[5] = x - 37.5*scale_ratio;
        
        yPoints[0] = y - 85.6*scale_ratio;
        yPoints[1] = y - 67.5*scale_ratio;
        yPoints[2] = y + 35.3*scale_ratio;
        yPoints[3] = y + 53.3*scale_ratio;
        yPoints[4] = y + 35.3*scale_ratio;
        yPoints[5] = y - 67.5*scale_ratio;
        
        //right wheel
        xPoints[6] = x + 65*scale_ratio;
        xPoints[7] = x + 65*scale_ratio;
        xPoints[8] = x + 60*scale_ratio;
        xPoints[9] = x + 60*scale_ratio;
        
        yPoints[6] = y - 34.4*scale_ratio;
        yPoints[7] = y + 34.4*scale_ratio;
        yPoints[8] = y + 34.4*scale_ratio;
        yPoints[9] = y - 34.4*scale_ratio;
        
        //left wheel
        xPoints[10] = x - 65*scale_ratio;
        xPoints[11] = x - 65*scale_ratio;
        xPoints[12] = x - 60*scale_ratio;
        xPoints[13] = x - 60*scale_ratio;
        
        yPoints[10] = y - 34.4*scale_ratio;
        yPoints[11] = y + 34.4*scale_ratio;
        yPoints[12] = y + 34.4*scale_ratio;
        yPoints[13] = y - 34.4*scale_ratio;
        
        
        //set values
        stepMode = 1600;
    
        WheelDistance = 12.5*scale_ratio;
    
        WheelDiameter = 7*scale_ratio;
        
        turnAngle = (WheelDiameter*Math.PI)/(stepMode*WheelDistance);
        
        
    }
    public void setRD(int direction)
    {
        RightDirection = direction;
    }
    public void setLD(int direction)
    {
        LeftDirection = direction;
    }
    //add wall to collision list
    public void AddCollision(Wall Model)
    {
       Collision.add(Model);
    }
    public void setCollision(ArrayList<Wall> list)
    {
        for(int i = 0; i < list.size(); i++)
        {
            AddCollision(list.get(i));
        }
    }
     public void AddSensor(Sensor Model)
    {
       Sensors.add(Model);
       Model.FindTarget();
    }
    public void ClearCollision()
    {
        Collision.clear();
    }
    public void UpdateSensors()
    {
        for(int i = 0; i < Sensors.size(); i++)
        {
            Sensors.get(i).FindTarget();
        }
    }
    
    // tests to see if the mouse can move with out colliding into a wall
    public boolean CanMove(double[] xCordinates, double[] yCordinates)
    {
       boolean test = true;
       
       for(int i = 0; i < Collision.size() ; i++)
       {
            test = And(test, ! Collision.get(i).Boundary( xCordinates, yCordinates));        
       }
       return test;
       
    }
    
    //digital logic AND operation
    public boolean And(boolean one, boolean two)
    {
        if(one&&two)
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
   
    //distance from point of interest to refrence point
    public double Distance(double xPoint, double yPoint, double  xRef, double yRef)
    {
        return Math.sqrt( (xRef-xPoint)*(xRef-xPoint) + (yRef-yPoint)*(yRef-yPoint) );
    }
    //step the right wheel
    public void stepRight()
    {
        //make temp list to store corner point
        double[] CornerX = new double[14];
        double[] CornerY = new double[14];
        
        for(int i = 0; i < 14; i++)
        {
            //angle is the current angle from the wheel center to the point
            
            double angle =  getAngle(xPoints[i],yPoints[i],xLeftWheel,yLeftWheel);
            
            //add or subtract turnAngle to current angle based on direction mode
            CornerX[i] = (Distance(xPoints[i],yPoints[i],xLeftWheel,yLeftWheel)*Math.cos( (angle+RightDirection * turnAngle) ))+xLeftWheel;
            CornerY[i] = yLeftWheel-(Distance(xPoints[i],yPoints[i],xLeftWheel,yLeftWheel)*Math.sin( (angle+RightDirection * turnAngle) ));
        }
        if(CanMove(CornerX,CornerY))
        {
            xPoints = CornerX;
            yPoints = CornerY;
            
            double angle =  getAngle(xRightWheel,yRightWheel,xLeftWheel,yLeftWheel);
            double xRightTemp = xRightWheel;
            double yRightTemp = yRightWheel;
            
            xRightWheel = (Distance(xRightTemp,yRightTemp,xLeftWheel,yLeftWheel)*Math.cos( (angle+RightDirection * turnAngle) ))+xLeftWheel;
            yRightWheel = yLeftWheel-(Distance(xRightTemp,yRightTemp,xLeftWheel,yLeftWheel)*Math.sin( (angle+RightDirection * turnAngle) ));
            
            angle = getAngle(x,y,xLeftWheel,yLeftWheel);
            double xTemp = x;
            double yTemp = y;
            
            x = (Distance(xTemp,yTemp,xLeftWheel,yLeftWheel)*Math.cos( (angle+RightDirection * turnAngle) ))+xLeftWheel;
            y = yLeftWheel-(Distance(xTemp,yTemp,xLeftWheel,yLeftWheel)*Math.sin( (angle+RightDirection * turnAngle) ));
            
            //update each sensor
            for(int i = 0; i < Sensors.size(); i++)
            {
                
                double xPoint = Sensors.get(i).getX();
                double yPoint = Sensors.get(i).getY();
                double theta = Sensors.get(i).getAngle();
                
                angle =  getAngle(xPoint,yPoint,xLeftWheel,yLeftWheel);
                
                Sensors.get(i).setX ( (Distance(xPoint,yPoint,xLeftWheel,yLeftWheel)*Math.cos( (angle+RightDirection * turnAngle) ))+xLeftWheel);
                Sensors.get(i).setY( yLeftWheel-(Distance(xPoint,yPoint,xLeftWheel,yLeftWheel)*Math.sin( (angle+RightDirection * turnAngle) )) );
                Sensors.get(i).setAngle(theta+RightDirection * turnAngle);
            }
        }
    }
    

    //step the left wheel
    public void stepLeft()
    {
        //make temp list to store corner point
        double[] CornerX = new double[14];
        double[] CornerY = new double[14];
        
        for(int i = 0; i < 14; i++)
        {
            //angle is the current angle from the wheel center to the point
            
            double angle =  getAngle(xPoints[i],yPoints[i],xRightWheel,yRightWheel);
            
            //add or subtract turnAngle to current angle based on direction mode
            CornerX[i] = (Distance(xPoints[i],yPoints[i],xRightWheel,yRightWheel)*Math.cos( (angle+LeftDirection * turnAngle) ))+xRightWheel;
            CornerY[i] = yRightWheel-(Distance(xPoints[i],yPoints[i],xRightWheel,yRightWheel)*Math.sin( (angle+LeftDirection * turnAngle) ));
        }
        if(CanMove(CornerX,CornerY))
        {
            xPoints = CornerX;
            yPoints = CornerY;
            
            double angle =  getAngle(xLeftWheel,yLeftWheel,xRightWheel,yRightWheel);
            double xLeftTemp = xLeftWheel;
            double yLeftTemp = yLeftWheel;
            
            xLeftWheel = (Distance(xLeftTemp,yLeftTemp,xRightWheel,yRightWheel)*Math.cos( (angle+LeftDirection * turnAngle) ))+xRightWheel;
            yLeftWheel = yRightWheel-(Distance(xLeftTemp,yLeftTemp,xRightWheel,yRightWheel)*Math.sin( (angle+LeftDirection * turnAngle) ));
            
            angle = getAngle(x,y,xRightWheel,yRightWheel);
            double xTemp = x;
            double yTemp = y;
            
            x = (Distance(xTemp,yTemp,xRightWheel,yRightWheel)*Math.cos( (angle+LeftDirection * turnAngle) ))+xRightWheel;
            y = yRightWheel-(Distance(xTemp,yTemp,xRightWheel,yRightWheel)*Math.sin( (angle+LeftDirection * turnAngle) ));
            
            //update each sensor
            for(int i = 0; i < Sensors.size(); i++)
            {
                
                double xPoint = Sensors.get(i).getX();
                double yPoint = Sensors.get(i).getY();
                double theta = Sensors.get(i).getAngle();
                
                angle =  getAngle(xPoint,yPoint,xRightWheel,yRightWheel);
                
                Sensors.get(i).setX ( (Distance(xPoint,yPoint,xRightWheel,yRightWheel)*Math.cos( (angle+LeftDirection * turnAngle) ))+xRightWheel);
                Sensors.get(i).setY( yRightWheel-(Distance(xPoint,yPoint,xRightWheel,yRightWheel)*Math.sin( (angle+LeftDirection * turnAngle) )) );
                Sensors.get(i).setAngle(theta+LeftDirection * turnAngle);
            }
        }
    }
    
    
    //get the mid point value from two points, either x or y
    public double MidPoint(double point1, double point2)
    {
        //find absoulte value of distance between points
        double difference = Math.abs(point1 - point2);
        
        //add half the difference to smaller point
        if(point1 < point2)
        {
            return point1+(difference/2);
        }
        if(point1 > point2)
        {
            return point2+(difference/2);
        }
        else
        {
            return point1;
        }
    } 
    
    //return the angle between the entered point and the reference point in radians
    public double getAngle(double TestX, double TestY, double xRef, double yRef)
    {
        if(TestX > xRef)
        {
            if(TestY < yRef)
            {
                //quadrant one
                return (Math.atan((yRef-TestY)/ (TestX-xRef) ) );
            }
            if(TestY > yRef)
            {
                 //quadrant four
                 return (2*Math.PI)-(Math.atan((TestY-yRef)/(TestX-xRef) ) );
            }
            else
            {
                //angle is zero if y's have same value
                return 0;
            }
        }
        if(TestX < xRef)
        {
            if(TestY < yRef)
            {
                //quadrant two
                return (Math.PI)-(Math.atan((yRef-TestY)/ (xRef-TestX) ) );
            }
            if(TestY > yRef)
            {
                //quadrant three
                return (Math.PI)+(Math.atan((TestY-yRef)/(xRef-TestX)) ) ;
            }
            else
            {
                //angle is pi if y's have same value
                return Math.PI;
            }
        }
        //the x value is the same
        else
        {
             if(TestY < yRef)
             {
                 return  Math.PI/2;
             }
             if(TestY > yRef)
             {
                 return -Math.PI/2;
             }
             //invalid input angle can not be determined return null
             else
             {
                 return 0;
             }
        }
            
       
    }
    public void draw( Graphics page)
    {
        int [] xCorners = new int[6];
        int [] yCorners = new int[6];
        page.setColor( color);
        
        //draw main
        for(int i = 0; i < 6; i++)
        {
            xCorners[i] = (int)xPoints[i];
            yCorners[i] = (int)yPoints[i];
        }
        page.drawPolygon( xCorners, yCorners, 6);
        
        //draw right tire
        for(int i = 0; i < 4; i++)
        {
            xCorners[i] = (int)xPoints[i+6];
            yCorners[i] = (int)yPoints[i+6];
        }
        page.drawPolygon( xCorners, yCorners, 4);
        
        //draw left tire
        for(int i = 0; i < 4; i++)
        {
            xCorners[i] = (int)xPoints[i+10];
            yCorners[i] = (int)yPoints[i+10];
        }
        page.drawPolygon( xCorners, yCorners, 4);
        
        for(int i = 0; i < Sensors.size(); i++)
        {
            Sensors.get(i).draw(page);
        }
        
        Circle RightWheel = new Circle((int)(8*scale_ratio),Color.green,xRightWheel,yRightWheel);
        RightWheel.draw(page);
        
        Circle LeftWheel = new Circle((int)(8*scale_ratio),Color.green,xLeftWheel,yLeftWheel);
        LeftWheel.draw(page);
        
        Circle center = new Circle((int)(8*scale_ratio),Color.black,x,y);
        center.draw(page);
        
        //for testing purposes
        //System.out.println("this is the x component "+x);
        //System.out.println("this is the y component "+y);
        
    }
    public void setColor( Color shade)
    {
        color = shade;
    }
    public void setCenter(double CenterX, double CenterY)
    {
         //build the corners of the box using center as main refrence
        //progresses around the box in clockwise order starting at top right
        
        //find the difference between new and old center for x and y
        double diffX = CenterX - x;
        double diffY = CenterY - y;
        
        double[] CornerX = new double[14];
        double[] CornerY = new double[14];
      
        //apply the difference to corner points
        for(int i = 0; i < 14; i++)
        {
            CornerX[i] = xPoints[i] + diffX;
            CornerY[i] = yPoints[i] + diffY;
        }
        
        if(CanMove(CornerX,CornerY))
        {
            //save new center point
            xPoints = CornerX;
            yPoints = CornerY;
            
            x = CenterX;
            y = CenterY;
            
            //update each sensor
            for(int i = 0; i < Sensors.size(); i++)
            {
                double SensorX = Sensors.get(i).getX();
                double SensorY = Sensors.get(i).getY();
                
                Sensors.get(i).setX(SensorX + diffX);
                Sensors.get(i).setY(SensorY + diffY);
    
            }
        }
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
