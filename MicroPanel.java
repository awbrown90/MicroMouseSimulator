import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.ArrayList;
import java.awt.event.MouseListener;

public class MicroPanel extends JPanel
{

    private final int WIDTH = 780, HEIGHT = 780;
    
    private double x,y,distance,angle;
    
    private Point point;
    private javax.swing.Timer timer;
    ArrayList<Wall> Corners = new ArrayList<Wall>();
    ArrayList<Wall> Walls = new ArrayList<Wall>();
    
    //used for maze editor
    ArrayList<Wall> RowGrid = new ArrayList<Wall>();
    ArrayList<Wall> ColumbGrid = new ArrayList<Wall>();
    
    ArrayList<Circle> Centers = new ArrayList<Circle>();
    Map<Integer,JLabel> Grid = new HashMap<Integer,JLabel>();
    private Mouse box1;
    private Sensor front;
    private Sensor right;
    private Sensor left;
    boolean sr;
    boolean sL;
    
    //variables for the right wall follower
     private final int DELAY = 30;
     boolean program_mode = false;
     int rightDistance;
     int leftDistance;
     
     int move_step;
     int rotate_step;
     int stage;
     
     //Acceleration values
     double final_speed = .75;  //rps
     double time_steps_constant = 1366.12; //sec/steps

     
     int right_range = 53;
     int right_margin = 5;
     int left_range =  53;
     int left_margin = 5;
     
     int maxRotate;
     boolean position_test;
     boolean turn_test;
     boolean s1;
     boolean s2;
     boolean s3;
     //end of variables
    
    //defines the row walls for the maze
    int n = 16; // maze dimensions n x n
    //scaling options for the mouse
    double mouse_ref =  780/(16*n+1);
    double scale_ratio = mouse_ref/12;
    //scaling options for maze
    // fundamental reference measurement based on variable n
    //780 is the side distance of the total avalable window
    double maze_ref = 780/(16*n+1);
    int corner_dim = (int)maze_ref; 
    int corner_ref = (int)Math.ceil(390/(16*n+1));
    int wall_dim = 15*corner_dim;
    int marker_dim = 16*corner_dim;
    int marker_ref = (int)Math.ceil(wall_dim/2+corner_dim);
    
    //center the window panel in the 780x780 display
    int window = n*wall_dim+(n+1)*corner_dim;
    int disp_gap = (780 - window)/2;
    
    private Stack<MazeCell> CellList = new Stack<MazeCell>();
    
    private int[] MazeRows;
    
    //the visiable row walls to the mouse
    private int[] MazeRowsValues = new int[n*(n+1)];
    
    private int[] MazeColumbs;
    
    private int[] MazeColumbsValues = new int[n*(n+1)];
    
    private int[][] Values = new int[n][n];
    
    //keep track of position in maze
    //direction = 0 is North
    //direction = 1 is East
    //direction = 2 is South
    //direction = 3 is West
    int direction = 0;
    // 1,5 is bottom left and 5,1 is top right
    int x_maze = 0;
    int y_maze = n-1;
    //
    int front_x;
    int front_y;
    int right_x;
    int right_y;
    int left_x;
    int left_y;
    
    //add grid numbers
    GridLayout experimentLayout = new GridLayout(n,n);
    //
    public MicroPanel()
    {
        //set up variables for right wall follower
        
         rightDistance = 1;
         leftDistance = -1;
         
         move_step = 0;
         stage = 1;
         
         direction = 0;
         int x_maze = 1;
         int y_maze = n-1;
         
         maxRotate = 0;
         position_test = true;
         
         //end variable set up
         
         setLayout(experimentLayout);
         
         //set up mouse/keyboard interaction
         DriveMouse listener = new DriveMouse();
         addKeyListener(listener);
         
         MazeEdit listener2 = new MazeEdit();
         addMouseListener(listener2);
         
         setFocusable(true);
        
        box1 = new Mouse(n, disp_gap+102*scale_ratio, (int)(disp_gap+n*corner_dim+(n-.5)*wall_dim), Color.black);
        
        front = new Sensor(box1.getX(),box1.getY()-85.6*scale_ratio,Math.PI/2);
        right = new Sensor(box1.getX()+37.5*scale_ratio,box1.getY()-52.5*scale_ratio,0);
        left = new Sensor(box1.getX()-37.5*scale_ratio,box1.getY()-52.5*scale_ratio,Math.PI);
        
        //create values for maze
        Value_Gen(n);
        //5x5
        if( n == 5)
        {
            int[] Create_Rows = 
            {1,1,1,1,1,
             1,0,0,1,0,
             1,0,0,0,0,
             0,0,1,0,0,
             0,0,1,1,0,
             1,1,1,1,1};
     
              int[] Create_Columbs = 
              {1,0,1,0,0,1,
               1,0,1,1,1,1,
               1,1,1,1,1,1,
               1,0,0,0,0,1,
               1,1,0,1,0,1};
               
               MazeRows = Create_Rows;
               MazeColumbs = Create_Columbs;
               CreateMemoryWalls();
            
         }
         //8x8
         else if( n == 8)
         {
             int[] Create_Rows = 
             {1,1,1,1,1,1,1,1,
              1,0,1,0,0,0,0,0,
              0,1,1,1,0,1,1,0,
              0,0,1,0,0,1,1,0,
              0,0,0,1,0,0,1,1,
              0,1,1,0,1,0,1,0,
              0,0,1,0,1,0,0,1,
              0,1,0,1,0,0,1,0,
              1,1,1,1,1,1,1,1};
     
              int[] Create_Columbs =  
              {1,0,0,0,0,0,1,0,1,
               1,0,0,0,1,1,0,1,1,
               1,0,1,0,0,0,0,0,1,
               1,1,1,0,1,1,0,0,1,
               1,1,1,1,0,0,0,0,1,
               1,1,0,0,1,0,1,0,1,
               1,0,0,1,0,1,1,0,1,
               1,1,0,0,0,0,0,0,1};
               
               MazeRows = Create_Rows;
               MazeColumbs = Create_Columbs;
               CreateMemoryWalls();
         }
         else if( n ==16 )
         {
             int[] Create_Rows = {
                1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
                0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,0,
                0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,0,
                1,0,0,0,0,1,1,1,0,0,1,1,1,1,0,0,
                0,1,0,0,0,0,1,1,0,1,0,1,1,1,0,0,
                0,1,1,0,0,0,1,0,1,0,1,1,1,0,0,0,
                0,1,1,1,0,0,0,1,1,1,0,1,1,1,0,0,
                0,0,1,1,1,0,0,1,0,0,1,1,1,0,0,0,
                0,0,1,1,1,1,1,0,0,0,1,1,1,1,1,1,
                0,0,0,1,1,0,0,1,1,0,0,1,0,1,0,0,
                0,1,1,1,0,0,0,1,1,1,1,0,0,1,0,0,
                0,1,1,0,0,0,1,1,1,1,1,1,1,1,0,0,
                0,1,0,0,0,0,0,1,1,1,1,1,1,1,1,0,
                1,0,0,0,0,0,1,1,1,1,1,1,1,1,0,0,
                0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,
                0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,0,
                1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1
                };
                int[] Create_Columbs = { 
                1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,
                1,1,1,0,1,0,0,0,0,0,0,0,0,0,0,0,1,
                1,0,0,1,1,1,0,0,0,1,0,0,0,0,0,1,1,
                1,0,1,1,1,1,0,0,0,0,1,0,0,0,0,1,1,
                1,0,0,1,1,1,0,0,0,0,1,0,0,0,1,1,1,
                1,0,0,0,1,1,1,0,0,1,1,0,0,0,1,1,1,
                1,1,0,0,0,1,0,0,1,0,1,0,0,0,1,1,1,
                1,1,0,0,0,0,1,1,0,1,0,0,0,0,1,1,1,
                1,1,1,0,0,0,1,1,0,1,0,0,0,0,0,0,1,
                1,1,0,0,0,1,1,0,0,0,1,0,0,0,0,1,1,
                1,0,0,0,1,1,1,0,0,0,0,0,1,0,0,1,1,
                1,0,0,1,1,1,1,0,0,0,0,0,0,0,0,0,1,
                1,0,1,1,1,1,1,0,0,0,0,0,0,0,0,1,1,
                1,0,0,1,1,1,0,0,0,0,0,0,0,0,0,1,1,
                1,1,1,0,1,0,0,0,0,0,0,0,0,0,0,0,1,
                1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1
                };
               MazeRows = Create_Rows;
               MazeColumbs = Create_Columbs;
               CreateMemoryWalls();
         }
         else
         {
             CreateMemoryWalls();
             MazeRows = MazeRowsValues;
             MazeColumbs = MazeColumbsValues;
         }
         
        
        //set up standard row/columb grids for maze editor
        int MazeCount = 0;
        for(int k = 0; k < MazeRows.length; k++)
        {
           RowGrid.add(MazeCount , new Wall(wall_dim, corner_dim, disp_gap + marker_ref+(k%n*marker_dim), disp_gap + corner_ref+(k/n*marker_dim), Color.black));
           MazeCount++;    
        }
        MazeCount = 0;
        for(int k = 0; k < MazeColumbs.length; k++)
        {
           ColumbGrid.add(MazeCount , new Wall(corner_dim, wall_dim, disp_gap + corner_ref+(k%(n+1)*marker_dim), disp_gap + marker_ref+(k/(n+1)*marker_dim), Color.black));
           MazeCount++;
        }
        
        
        int corner_count = 0;
        for(int k = 0; k < n+1; k++)
        {
            for(int j = 0; j < n+1; j++)
            {
                //Corners.add(k+(j*(n+1)), new Wall( corner_dim , corner_dim , disp_gap + corner_ref+(k*marker_dim), disp_gap + corner_ref+(j*marker_dim), Color.red));
                Corners.add(corner_count, new Wall( corner_dim , corner_dim , disp_gap + corner_ref+(k*marker_dim), disp_gap + corner_ref+(j*marker_dim), Color.red));
                corner_count++;
            }
        }
        
        //create visual centers for the box cells
        //Map<Integer,Circle> markers = new HashMap<Integer,Circle>();
        //for(int k = 0; k < n; k++)
        //{
        //    for(int j = 0; j < n; j++)
        //    {
        //        markers.put(k+(j*n), new Circle((int)(8*scale_ratio), Color.blue, disp_gap + marker_ref+(k*marker_dim), disp_gap + marker_ref+(j*marker_dim) ) );
        //    }
        //}
        
        //create wall pieces of the maze
        //Map<Integer,Wall> Walls = new HashMap<Integer,Wall>();
        
        //set up number grid
        //instanate the labels to fill the maze cells
        //Map<Integer,JLabel> Grid = new HashMap<Integer,JLabel>();
        for(int i = 0; i < (n*n); i++)
        {
            //Grid.put(i, new JButton(Integer.toString(i)));
            Grid.put(i,new JLabel(Integer.toString(Values[i%n][i/n])));
        }
        for(int x = 0; x < Grid.size(); x++)
        {
            Grid.get(x).setHorizontalAlignment(JLabel.CENTER);
            Grid.get(x).setVerticalAlignment(JLabel.CENTER);
            //put on panel
            add(Grid.get(x));
            //store button
            //buttons.add(Grid.get(x));    
        }
        //finish setting up number grid
        
        UpdateWalls();
        
        box1.AddSensor(front);
        box1.AddSensor(right);
        box1.AddSensor(left);
       

        timer = new javax.swing.Timer(DELAY, new RunProgram() );
        
        setPreferredSize (new Dimension(WIDTH,HEIGHT));
        setBackground (Color.white);
        timer.start();
       
    }
   public void paintComponent (Graphics page)
   {
       
       super.paintComponent(page);
       for(int x = 0; x < Walls.size() ; x++)
       {
            Walls.get(x).draw(page); 
       }
       for(int x = 0; x < Corners.size() ; x++)
       {
            Corners.get(x).draw(page);         
       }
            
       for(int x = 0; x < Centers.size() ; x++)
       {
            Centers.get(x).draw(page);         
       }
       
       box1.draw(page);
       
       //update number grid for cells
       for(int x = 0; x < Grid.size(); x++)
       {
            Grid.get(x).setText(Integer.toString(Values[x%n][x/n]));  
       }        
   }
   public void UpdateWalls()
   {
        Walls.clear();
        int MazeCount = 0;
        for(int k = 0; k < MazeRows.length; k++)
        {
            if(MazeRows[k] == 1)
            {
                if(MazeRowsValues[k] == 1)
                {
                    Walls.add(MazeCount , new Wall(wall_dim, corner_dim, disp_gap + marker_ref+(k%n*marker_dim), disp_gap + corner_ref+(k/n*marker_dim), Color.blue));
                }
                else
                {
                    Walls.add(MazeCount , new Wall(wall_dim, corner_dim, disp_gap + marker_ref+(k%n*marker_dim), disp_gap + corner_ref+(k/n*marker_dim), Color.black));
                }
                
                MazeCount++;
            }
        }
        for(int k = 0; k < MazeColumbs.length; k++)
        {
            if(MazeColumbs[k] == 1)
            {
                if(MazeColumbsValues[k] == 1)
                {
                    Walls.add(MazeCount , new Wall(corner_dim, wall_dim, disp_gap + corner_ref+(k%(n+1)*marker_dim), disp_gap + marker_ref+(k/(n+1)*marker_dim), Color.blue));
                }
                else
                {
                    Walls.add(MazeCount , new Wall(corner_dim, wall_dim, disp_gap + corner_ref+(k%(n+1)*marker_dim), disp_gap + marker_ref+(k/(n+1)*marker_dim), Color.black));
                }
                MazeCount++;
            }
        }
        box1.ClearCollision();
        front.ClearCollision();
        right.ClearCollision();
        left.ClearCollision();
        
        box1.setCollision(Corners);
        front.setCollision(Corners);
        right.setCollision(Corners);
        left.setCollision(Corners);
        
        for(int i = 0; i < Walls.size(); i++)
        {
            box1.AddCollision(Walls.get(i));
            front.AddCollision(Walls.get(i));
            right.AddCollision(Walls.get(i));
            left.AddCollision(Walls.get(i));
        }
        box1.UpdateSensors();
   }
   private class RunProgram implements ActionListener
   { 
         public void actionPerformed ( ActionEvent event)
         {
             if(program_mode)
             {
                //turn_test = false;
                //s1 = false;
                //s2 = true;
                //s3 = false;
                for(int s = 0; s < 10; s++)
                {
                        //RightWallFollower();
                        FloodFill();
                        if(s%5 == 0)
                        {
                             box1.UpdateSensors();
                        }
                        //RightWallFollower();
                        //FirstCell();
                }
                box1.UpdateSensors();
                //UpdateWalls();
                repaint(); 
            }
       }
   }
   public void FirstCell()
   {
       box1.setRD(1);
       box1.setLD(-1);
       //1037
       if(move_step < 900)
       {
            //double margin = right.getDistance() - left.getDistance();
            //System.out.println(margin);
            if(left.getDistance() > 54)
            {
                for(int s = 0; s < 5; s++)
                {
                    box1.stepRight();
                }
            }
            if(left.getDistance() < 52)
            {
                for(int s = 0; s < 5; s++)
                {
                    box1.stepLeft();
                }
            }
            for(int s = 0; s < 10; s++)
            {
                if(move_step < 900)
                {
                    box1.stepRight();
                    box1.stepLeft();
                }
                if(right.getDistance() > 100)
                {
                   move_step++;
                }
            }
           
        }
   }
   public void Step()
   {
       
         double right_dect = right.getDistance();
         sr = (right_dect < 60);
    
         double left_dect  = left.getDistance();
         sL = (left_dect < 60);
        
        if(sr)
        {
            SideSense_right();
        }
    
        else if(sL)
        {
            SideSense_left();
        }
        else
        {
            int n = 0;
            while( n < 5)
            {
                box1.stepRight();
                box1.stepLeft();
                n++;
            }
        }
    
    }
    void SideSense_right() // will check side sensors and make correction to drive path based on dly speed of steppers
    {
        //setDirection('front');
  
        //move forward 
        for(int i = 0; i < 5; i++)
        {
             box1.stepRight();
             box1.stepLeft();
        }
  
        //check value
        double val = right.getDistance();
  
        //if value is in a interval where the distance is too close
        //turn away from wall
        if( val < (right_range - right_margin)*scale_ratio )
        {
            box1.stepRight();   
        }
        //if value is in a interval where the distance is too far
        //turn towards the wall
        if( val > (right_range + right_margin)*scale_ratio )
        {
           box1.stepLeft();
        }
   }
   void SideSense_left() // will check side sensors and make correction to drive path based on dly speed of steppers
   {
      //setDirection('front');
  
       //move forward 
       for(int i = 0; i < 5; i++)
       {
            box1.stepRight();
            box1.stepLeft();
        }
  
        //check value
        double val = left.getDistance();
  
        //if value is in a interval where the distance is too far
        //turn towards the wall
        if( val > (left_range + left_margin)*scale_ratio )
        {
             box1.stepRight();
        }
        //if value is in a interval where the distance is too close
        //turn away from wall
        if( val < (left_range - left_margin)*scale_ratio )
        {
            box1.stepLeft();
        }
    }
   
   public void FloodFill()
   {
       //if stage is move
       if(stage == 1)
       {
           rightDistance = 1;
           leftDistance = -1;
           if(move_step < 1400)
           {
                box1.setRD(rightDistance);
                box1.setLD(leftDistance);
                Step();
                move_step+=5;     
            }
            else
            {
                move_step = 0;
                rotate_step = 0;
                if(direction == 0)
                {
                    y_maze--;
                    front_x = x_maze;
                    front_y = y_maze-1;
                    
                    right_x = x_maze+1;
                    right_y = y_maze;
                    
                    left_x = x_maze-1;
                    left_y = y_maze;
                }
                else if(direction == 1)
                {
                    x_maze++;
                    front_x = x_maze+1;
                    front_y = y_maze;
                    
                    right_x = x_maze;
                    right_y = y_maze+1;
                    
                    left_x = x_maze;
                    left_y = y_maze-1;
                }
                else if(direction == 2)
                {
                    y_maze++;
                    front_x = x_maze;
                    front_y = y_maze+1;
                    
                    right_x = x_maze-1;
                    right_y = y_maze;
                    
                    left_x = x_maze+1;
                    left_y = y_maze;
                }
                else if(direction == 3)
                {
                    x_maze--;
                    front_x = x_maze-1;
                    front_y = y_maze;
                    
                    right_x = x_maze;
                    right_y = y_maze-1;
                    
                    left_x = x_maze;
                    left_y = y_maze+1;
                }
                stage = 2;
            }
        }
        if(stage == 2)
        {
           if(Goal(x_maze,y_maze))
           {
              //do nothing your at the goal
           }
           else
           {
               if(position_test)
               {
                    int f_value;
                    int r_value;
                    int l_value;
                    if(front.getDistance() > 50*scale_ratio)
                    {
                        f_value = Values[front_x][front_y];
                    }
                    else
                    {
                        AddWall(false,true,false);
                        f_value = 1000;
                    }
                    if(right.getDistance() > 80*scale_ratio)
                    {
                        r_value = Values[right_x][right_y];
                    }
                    else
                    {
                        AddWall(false,false,true);
                        r_value = 1000;
                    }
                    if(left.getDistance() > 80*scale_ratio)
                    {
                        l_value = Values[left_x][left_y];
                    }
                    else
                    {
                        AddWall(true,false,false);
                        l_value = 1000;
                    }
                    FloodFillValues();
                    if(f_value==1000&&r_value==1000&&l_value==1000)
                    {
                        rightDistance = 1;
                        leftDistance = 1;
                        maxRotate = 1428;
                        Update_Direction(4);
                        //turn around
                    }
                    else
                    {
                        int value = Evaluation(l_value,f_value,r_value);
                        if(value == 1)
                        {
                            rightDistance = 1;
                            leftDistance =  1;
                            maxRotate = 714;
                            Update_Direction(value);
                            //go left
                        }
                        if(value == 2)
                        {
                             rightDistance = 1;
                             leftDistance = -1;
                             maxRotate = 0;
                             Update_Direction(value);
                             //go stright
                        }
                        if(value == 3)
                        {
                            rightDistance = -1;
                            leftDistance = -1;
                            maxRotate = 714;
                            Update_Direction(value);
                            //turn right
                        }
                    } 
                    position_test = false;
              } 
              if(rotate_step < maxRotate)
              {
                    box1.setRD(rightDistance);
                    box1.setLD(leftDistance);
                    for(int n = 0; n < 5; n++)
                    {
                        box1.stepRight();
                        box1.stepLeft();
                        rotate_step++;
                    }
               }
               else
               {
                    move_step = 0;
                    rotate_step = 0;
                    position_test = true;
                    stage = 1;
                    //run step by step or constantly
                    //program_mode = false;
                    UpdateWalls();
               }
           }
       }
   }
   //add wall to the mouse 
   public void AddWall(boolean left, boolean front, boolean right)
   {
       if(direction == 0)
       {
           if(left)
           {
               MazeColumbsValues[(n+1)*y_maze+x_maze] = 1;
           }
           if(front)
           {
               MazeRowsValues[n*y_maze+x_maze] = 1; 
           }
           if(right)
           {
               MazeColumbsValues[(n+1)*y_maze+x_maze+1] = 1;
           }
        }
        if(direction == 1)
        {
           if(left)
           {
               MazeRowsValues[n*y_maze+x_maze] = 1; 
           }
           if(front)
           {
               MazeColumbsValues[(n+1)*y_maze+x_maze+1] = 1;
           }
           if(right)
           {
               MazeRowsValues[n*(y_maze+1)+x_maze] = 1; 
           }
        }
        if(direction == 2)
        {
           if(left)
           {
               MazeColumbsValues[(n+1)*y_maze+x_maze+1] = 1;
           }
           if(front)
           {
               MazeRowsValues[n*(y_maze+1)+x_maze] = 1; 
           }
           if(right)
           {
               MazeColumbsValues[(n+1)*y_maze+x_maze] = 1; 
           }
        }
        if(direction == 3)
        {
            if(left)
            {
               MazeRowsValues[n*(y_maze+1)+x_maze] = 1; 
            }
            if(front)
            {
               MazeColumbsValues[(n+1)*y_maze+x_maze] = 1;
            }
            if(right)
            {
               MazeRowsValues[n*y_maze+x_maze] = 1;  
            }
        }             
   }
   public void Update_Direction(int value)
   {
       if(direction == 0)
       {
           if(value == 1)
           {
               direction = 3;
            }
            if(value == 3)
            {
                direction = 1;
            }
            if(value == 4)
            {
                direction = 2;
            }
            
        }
        else if(direction == 1)
        {
           if(value == 1)
           {
               direction = 0;
            }
            if(value == 3)
            {
                direction = 2;
            }
            if(value == 4)
            {
                direction = 3;
            }
        }
        else if(direction == 2)
        {
           if(value == 1)
           {
               direction = 1;
            }
            if(value == 3)
            {
                direction = 3;
            }
            if(value == 4)
            {
                direction = 0;
            }
        }
        else if(direction == 3)
        {
           if(value == 1)
           {
               direction = 2;
            }
            if(value == 3)
            {
                direction = 0;
            }
            if(value == 4)
            {
                direction = 1;
            }
        }
    }
               
           
   //feed in three values to evaluate based on three possible directions
   //return 1 to go left, 2 to go stright, and 3 to go right
   public int Evaluation(int left, int front, int right)
   {
       int priority = 1000;
       if(front < priority)
       {
           priority = front;
       }
       if(right < priority)
       {
           priority = right;
       }
       if(left < priority)
       {
           priority = left;
       }
       if(front  == priority)
       {
         //do nothing
       }
       else
       {
           front = 1000;
       }
       if(right == priority)
       {
           right++;
       }
       else
       {
           right = 1000;
       }
       if(left == priority)
       {
           left++;
       }
       else
       {
           left = 1000;
       }
       int descision = 1000;
       if(front < descision)
       {
           descision = front;
       }
       if(right < descision)
       {
           descision = right;
       }
       if(left < descision)
       {
           descision = left;
       }
       if(descision == left)
       {
           return 1;
       }
       else if(descision == front)
       {
           return 2;
       }
       else if(descision == right)
       {
           return 3;
       }
       else
       {
           return 1;
        }
   }
   public void RightWallFollower()
   {
       //if stage is move
       if(stage == 1)
       {
           rightDistance = 1;
           leftDistance = -1;
           if(move_step < 1400)
           {
                box1.setRD(rightDistance);
                box1.setLD(leftDistance);
                Step();
                move_step+=5;
                double front_dect = front.getDistance();
                if(front_dect < 150*scale_ratio)
                {
                    stage = 3;
                }      
            }
            else
            {
                move_step = 0;
                rotate_step = 0;
                stage = 2;
            }
        }
        if(stage == 2)
        {
            if(position_test)
            {
                if(right.getDistance() > 80*scale_ratio)
                {
                     rightDistance = -1;
                     leftDistance = -1;
                     maxRotate = 714;
                    //turn right
                    
                }
                else if(front.getDistance() > 50*scale_ratio)
                {
                     rightDistance = 1;
                     leftDistance = -1;
                     maxRotate = 0;
                    //go stright
                    
                }
                else if(left.getDistance() > 80*scale_ratio)
                {
                     rightDistance = 1;
                     leftDistance =  1;
                     maxRotate = 714;
                    //go left
                   
                }
                else
                {
                     rightDistance = 1;
                     leftDistance = 1;
                     maxRotate = 1428;
                    //turn around
                }
                position_test = false;
            }
            if(rotate_step < maxRotate)
            {
                box1.setRD(rightDistance);
                box1.setLD(leftDistance);
                for(int n = 0; n < 5; n++)
                {
                    box1.stepRight();
                    box1.stepLeft();
                    rotate_step++;
                }
            }
            else
            {
                move_step = 0;
                rotate_step = 0;
                position_test = true;
                stage = 1;
            }
        }
        if(stage == 3)
        {
            double front_dect = front.getDistance();
            box1.setRD(rightDistance);
            box1.setLD(leftDistance);
            if(front_dect > 10*scale_ratio)
            {
                Step();
                move_step+=5;
                front_dect = front.getDistance();   
            }
            else
            {
                move_step = 0;
                rotate_step = 0;
                stage = 2;
            }  
        }
        
    }
    void ConV(double centimeters)
    {
        double rev = centimeters/21.99;      //calculate number of revoulutions
        double max_steps = 1600*rev;         // calculate number of steps
        double current_steps = 0;
  
        box1.setRD(1);  // Set the stepper direction
        box1.setLD(-1); // Set the stepper direction
  
        while(current_steps < max_steps)
        {
            //move forward 
  
            box1.stepRight();
            box1.stepLeft();
            current_steps++;
            repaint();
        }
    }
   public boolean Goal(int x, int y)
   {
           if(n%2 == 0)
           {
                if( (x_maze == (n/2-1) && y_maze == (n/2-1)) || (x_maze == (n/2) && y_maze == (n/2-1))||(x_maze == (n/2-1) && y_maze == (n/2))||(x_maze == (n/2) && y_maze == (n/2)) )
                {
                    return true;
                }
           }
           else 
           {
               if(x_maze == (n-1)/2 && y_maze == (n-1)/2)
                {
                    return true;
                }     
           }
           return false;
    }
   public void FloodFillValues()
   {
       //set all maze cells to -1 representing void value
       for(int i = 0; i < (n*n); i++)
       {
           Values[i%n][i/n] = -1;
       }
       //if dim is even
       if(n%2 == 0)
       {
            int base_1 = n/2-1;
            int base_2 = n/2;
               
            CellList.add(new MazeCell(base_1,base_1,0));
            CellList.add(new MazeCell(base_2,base_1,0));
            CellList.add(new MazeCell(base_1,base_2,0));
            CellList.add(new MazeCell(base_2,base_2,0));
        }
        else
        {
            int base = (n-1)/2;
            CellList.add(new MazeCell(base,base,0));
        }
        while(!CellList.empty() )
        {
            MazeCell current_cell = CellList.pop();
            SetCell(current_cell.getX(), current_cell.getY(), current_cell.getValue());
        }
        
            
   }
   //set value in maze cell by x and y location 
   public void SetCell(int x, int y, int value)
   {
       int current_value = Values[x][y];
       if(  (current_value == -1)||(value < current_value) )
       {
           Values[x][y] = value;
            if(MazeRowsValues[x+(y*n)]==0 && (CellList.size() < 64))
            {
                CellList.add(new MazeCell(x,y-1,value+1));
            }
            if(MazeColumbsValues[(x+1)+(y*(n+1))]==0 && (CellList.size() < 64))
            {
                CellList.add(new MazeCell(x+1,y,value+1));
            }
            if(MazeRowsValues[x+((y+1)*n)]==0 && (CellList.size() < 64))
            {
                CellList.add(new MazeCell(x,y+1,value+1));
            }
            if(MazeColumbsValues[x+(y*(n+1))]==0 && (CellList.size() < 64))
            {
                CellList.add(new MazeCell(x-1,y,value+1));
            }
       }
   }
   void CreateMemoryWalls()
   {   
        //create visable rows
        for(int i = 0; i < n; i++)
        {
            MazeRowsValues[i] = 1;
        }
        for(int i = (n*n); i < (n*n)+n; i++)
        {
            MazeRowsValues[i] = 1;
        }
    
        //create visable columbs
        for(int i = 0; i < n*(n+1); i++)
        {
            //if( ((i%(n+1)) == 0) || ((i%n) == 0) )
            if( ((i%(n+1)) == 0) )
            {
                MazeColumbsValues[i] = 1;
                MazeColumbsValues[i+n] = 1;
            }
        }
    
        //create one additonal wall because mouse always starts on left bottom corner
        MazeColumbsValues[n*n] = 1;
   } 
   public void  Value_Gen(int dim)
   {
        //entered value must be at least three to generate array
        if(dim >= 3)
        {
            //if dim is even
           if(dim%2 == 0)
           {
               int base_1 = dim/2-1;
               int base_2 = dim/2;
               
               Values[base_1][base_1] = 0;
               Values[base_2][base_1] = 0;
               Values[base_1][base_2] = 0;
               Values[base_2][base_2] = 0;
               
               for(int i = 1; i<= base_1; i++)
               {
                   Values[base_2+i][base_1] = i;
                   Values[base_2+i][base_2] = i;
                   Values[base_1-i][base_1] = i;
                   Values[base_1-i][base_2] = i;
               }
               for(int i = 1; i <= base_1; i++)
               {
                   for(int j = 0; j < dim; j++)
                   {
                       Values[j][base_2+i] = Values[j][base_1]+i;
                       Values[j][base_1-i] = Values[j][base_1]+i;
                   }
               }
           }
           //else dim is odd
           else
           {
               int base = (dim-1)/2;
               Values[base][base] = 0;
               
               for(int i = 1; i <= base; i++)
               {
                   Values[base+i][base] = i;
                   Values[base-i][base] = i;
               }
               for(int i = 1; i <= base; i++)
               {
                   for(int j = 0; j < dim; j++)
                   {
                       Values[j][base+i] = Values[j][base]+i;
                       Values[j][base-i] = Values[j][base]+i;
                   }
               }
           }
        }
    }
    private class MazeEdit implements MouseListener
    {
        public void mouseClicked (MouseEvent event)
        {
            point = event.getPoint();
            for(int x = 0; x < RowGrid.size() ; x++)
            {
                if( RowGrid.get(x).CheckPoint(point.getX(),point.getY())  )
                {
                    if(MazeRows[x] == 0)
                    {
                        MazeRows[x] = 1;
                    }
                    else
                    {
                        MazeRows[x] = 0;
                    }
                }
            }
            for(int x = 0; x < ColumbGrid.size() ; x++)
            {
                if( ColumbGrid.get(x).CheckPoint(point.getX(),point.getY())  )
                {
                    if(MazeColumbs[x] == 0)
                    {
                        MazeColumbs[x] = 1;
                    }
                    else
                    {
                        MazeColumbs[x] = 0;
                    }
                }
            }
             UpdateWalls();
             repaint();
        }
     
        public void mousePressed (MouseEvent event) {}
        public void mouseReleased (MouseEvent event) {}
        public void mouseEntered (MouseEvent event) {}
        public void mouseExited (MouseEvent event) {}
        public void mouseDragged (MouseEvent event) {}
        public void mouseMoved (MouseEvent event) {}
        
       
    }
       
   private class DriveMouse implements KeyListener
    {   
        public void mousePressed(MouseEvent e)
        {
            if(MazeRows[56] == 0)
            {
                 MazeRows[56] = 1;
            }
            else
            {
                 MazeRows[56] = 0;
            }
            UpdateWalls();
            repaint();
        }
        public void keyTyped (KeyEvent e)
        {
        }
        public void keyPressed (KeyEvent e)
        { 
            if(e.getKeyCode()==KeyEvent.VK_DOWN)
            {
                box1.setRD(-1);
                box1.setLD(1);
                for(int s = 0; s < 20; s++)
                {
                    box1.stepRight();
                    box1.stepLeft();
                }
                box1.UpdateSensors();
                //System.out.println("L "+left.getDistance() );
                //System.out.println("R "+right.getDistance() );
                repaint();
            }
            if(e.getKeyCode()==KeyEvent.VK_UP)
            {
                box1.setRD(1);
                box1.setLD(-1);
                for(int s = 0; s < 20; s++)
                {
                    box1.stepRight();
                    box1.stepLeft();
                }
                box1.UpdateSensors();
                //System.out.println("L "+left.getDistance() );
                //System.out.println("R "+right.getDistance() );
                repaint();
            }
            if(e.getKeyCode()==KeyEvent.VK_RIGHT)
            {
                box1.setRD(-1);
                box1.setLD(-1);
                for(int s = 0; s < 8; s++)
                {
                    box1.stepRight();
                    box1.stepLeft();
                }
                box1.UpdateSensors();
                //System.out.println("L "+left.getDistance() );
                //System.out.println("R "+right.getDistance() );
                repaint();
            }
            if(e.getKeyCode()==KeyEvent.VK_LEFT)
            {
                box1.setRD(1);
                box1.setLD(1);
                for(int s = 0; s < 8; s++)
                {
                    box1.stepRight();
                    box1.stepLeft();
                }
                box1.UpdateSensors();
                //System.out.println("L "+left.getDistance() );
                //System.out.println("R "+right.getDistance() );
                repaint();
            }
            //move one maze cell
            if(e.getKeyCode()==KeyEvent.VK_S)
            {
                FloodFill();
            }
            if(e.getKeyCode()==KeyEvent.VK_SPACE)
            {
                //if(program_mode)
                //{
                //    program_mode = false;
                    
                //}
                //else
                //{
                //    move_step = 0;
                //    rotate_step = 0;
                //    stage = 1;
                //    program_mode = true;
                //}
                if(!program_mode)
                {
                    program_mode = true;
                }
            }
            if(e.getKeyCode()==KeyEvent.VK_P)
            {
                System.out.println("int[] Create_Rows = {");
                for(int i = 0; i < MazeRowsValues.length; i++)
                {
                    System.out.print(MazeRows[i]+",");
                    if((i+1)%n == 0)
                    {
                        System.out.println();
                    }
                }
                System.out.println("}");
                System.out.println("int[] Create_Columbs = { ");
                for(int i = 0; i < MazeColumbsValues.length; i++)
                {
                    System.out.print(MazeColumbs[i]+",");
                    if((i+1)%n == 0)
                    {
                        System.out.println();
                    }
                }
                System.out.println("}");
            }  
            if(e.getKeyCode()==KeyEvent.VK_C)
            {
                MazeRows = MazeRowsValues;
                MazeColumbs = MazeColumbsValues;
                UpdateWalls();
                repaint();
            }
            if(e.getKeyCode() ==KeyEvent.VK_U)
            {
                FloodFillValues();
                repaint();
            }
        }
        public void keyReleased (KeyEvent e)
        {
        }   
    }
  
}