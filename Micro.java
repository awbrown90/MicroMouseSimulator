import javax.swing.JFrame;

public class Micro
{
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("MOVING BALL");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.getContentPane().add(new MicroPanel());
        frame.pack();
        frame.setVisible(true);
    }
}