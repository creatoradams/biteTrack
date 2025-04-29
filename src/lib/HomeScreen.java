package lib;

import javax.swing.*;
import java.awt.*;

class HomeScreen extends JPanel
{
   // private final User user;

    public HomeScreen(User user)
    {
        //this.user = user;

        // Set the layout for the panel
        setLayout(new BorderLayout());

        // Create a label to act as the text message
        JLabel label = new JLabel("Welcome to BiteTrack!", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 20));

        // show nutrition summary
        double tdee = user.calculateTDEE();
        var macros  = NutritionCalculator.calculateMacros(tdee, user.goal());

        JTextArea info = new JTextArea(
                """
                Daily calories: %.0f kcal
                %s
                """.formatted(tdee, macros));
        info.setEditable(false);

        // Add the label to the panel
        add(label, BorderLayout.CENTER);
        add(info, BorderLayout.CENTER);
    }
}