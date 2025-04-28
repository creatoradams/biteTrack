package lib;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

public class displayLogo extends JPanel
{
    public displayLogo() throws IOException
    {
        // logo dimensions
        int width = 100;
        int height = 100;

        // Set layout to null so we can control positioning directly
        setLayout(new BorderLayout());

        // Load the image
        ImageIcon icon = new ImageIcon("C:/JAVA/BiteTrackJava/BiteTrackJava/logo.jpg");
        Image img = icon.getImage();
        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        // Create and add a JLabel to hold the image
        JLabel logo = new JLabel(new ImageIcon(scaledImg));
        add(logo, BorderLayout.NORTH);
    }
}

