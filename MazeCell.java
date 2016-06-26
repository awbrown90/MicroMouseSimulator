//contains the cell value, and position
public class MazeCell
{
    private int x_pos;
    private int y_pos;
    private int value;
    boolean top;
    boolean right;
    boolean bottom;
    boolean left;
    public MazeCell(int x, int y, int my_value)
    {
        x_pos = x;
        y_pos = y;
        value = my_value;
        top = false;
        right = false;
        bottom = false;
        left = false;
    }
    public MazeCell(int my_value)
    {
        value = my_value;
    }  
    public int getX()
    {
        return x_pos;
    }
    public int getY()
    {
        return y_pos;
    }
    public int getValue()
    {
        return value;
    }
    public void setTop()
    {
        top = true;
    }
    public void setRight()
    {
        right = true;
    }
    public void setBottom()
    {
        bottom = true;
    }
    public void setLeft()
    {
        left = true;
    }
    public void Reset()
    {
        top = false;
        right = false;
        bottom = false;
        left = false;
    }
}
