------------------------------------------------------------------------
License Agreement: The orginal source code was created by Aaron Brown for educational purposes.
Feel free to modify the program and share to explore more in depth on automous maze exploration, but for any redistrubtion 
include reference to orginal creator "Aaron Brown". 
for questions or other comments you can contact aaronwbrown@msn.com
------------------------------------------------------------------------

PROJECT TITLE: Micro Mouse Simulator
To simulate a robotic mouse that navigates to the center of a n x n maze (usually 16 x 16) 
without any prior knowledge of how the maze is laid out. The mouse always starts from a left
hand corner of the maze. The mouse uses senses to see and find walls as well as memory to store 
walls that it has found. This particular mouse uses a Flood Fill algorithm in order to find the 
center of the maze, the maze is designed so a wall follower will never be able to find the center.

VERSION or DATE: August 2012
HOW TO START THIS PROJECT: run the class Micro with void main()
AUTHORS: Aaron Brown
USER INSTRUCTIONS:

HOW TO START:
Once the application is loaded press space to start running the mouse enter "run mode". The grid numbers represent distance 
the center, at each step or grid cell these numbers are updated using the flood algorithm which also uses
all the walls (highlighted blue) that the mouse knows about to create the map. When the mouse finds a new
wall using its three distance sensors, the wall will turn blue and be save in its memory.

Its also possible to drive the mouse when not in "run mode" by using the up and left/right arrow keys.

HOW TO CUSTOMIZE:
The maze size (n x n) can be adjusted from 16 x 16 by changed the "int n" variable in the MicroPanel class

During a loaded appliaction, click directly on a wall in order to create or destroy it. After a desired Maze 
is made you can save it by pressing "p" during loaded appliaction and it will print out "Create_Rows", and 
"Create_Columbs" saved wall cordinates for the maze. Using the maze in the future can be done by copying
the printed row/columb arrays into the class MicroPanel for the apporpriate n size for both
Create_Rows and Create_Columbs.

