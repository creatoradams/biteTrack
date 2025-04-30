package lib;
import javax.swing.*;


// Main class to start the application
public class BiteTrackApp
{
    public static void main(String[] args)
    {
        // should have made a main user, but the dependency is on a nutrition calculator instance. 
        //User testUser = new User("test", "test", "testUser", "5015550005", "tester", 25, 170, 50)
        // Initialize the main frame of the app
        SwingUtilities.invokeLater(() ->
        {
            // Create and set up the window (frame)
            JFrame frame = new JFrame("BiteTrack");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 600); // Set the window size

            // display logo
            try
            {
                frame.setContentPane(new displayLogo()); // show logo
            } catch (Exception ex)    // logo failed → fall back
            {
                ex.printStackTrace();
                frame.setContentPane(new LoginScreen(frame));
            }
            frame.setVisible(true);

            // Set the initial screen to LoginScreen
            frame.getContentPane().add(new LoginScreen(frame)); // Pass the frame to LoginScreen
            frame.setVisible(true);

            // after 2 seconds swap to login screen
            new Timer(2000, e ->
            {
                frame.setContentPane(new LoginScreen(frame));
                frame.revalidate();
            }).setRepeats(false);        // only fire once
        });
    }
}
