package lib;

import javax.swing.*;
import java.awt.*;

class HomeScreen extends JPanel
{
    public HomeScreen()
    {
        // Set the layout for the panel
        setLayout(new BorderLayout());

        // Create a label to act as the text message
        JLabel label = new JLabel("Welcome to BiteTrack!", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 20));

        // Add the label to the panel
        add(label, BorderLayout.CENTER);
    }
}