import java.awt.*;

public class Circle 
{
    private double  x, y;
    private int radius;
    private Color color;
    private double circumfrence =2;
    double pi = 3.1416;
 
    public Circle (int size, Color shade, double CenterX, double CenterY)
    {
        radius = size;
        color = shade;
        x = CenterX;
        y = CenterY;
    }
    public void draw( Graphics page)
    {
        page.setColor( color);
        page.fillOval((int)(x-radius),(int)(y-radius),radius*2,radius*2);
    }
    public void setRadius (int size)
    {
        radius = size;
    }
    public void setColor( Color shade)
    {
        color = shade;
    }
    public void setX (double upperX)
    {
        x = upperX;
    }
    public void setY(double upperY)
    {
        y = upperY;
    }
    public int getRadius()
    {
        return radius;
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