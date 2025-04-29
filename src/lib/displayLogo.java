package lib;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class displayLogo extends JPanel
{
    public displayLogo() throws IOException
    {
        // logo dimensions
        int width = 200;
        int height = 200;

        setLayout(new BorderLayout());

        // Load and scale the logo
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/resources/logo.jpg"), "Logo resource not found"));
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        // Create and add a JLabel to hold the image
        JLabel logo = new JLabel(new ImageIcon(scaledImg));
        add(logo, BorderLayout.NORTH);


    }
}

