package lib;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

public class RegistrationScreen extends JPanel
{
    // Create text fields for each input field
    private final JTextField firstNameField;
    private final JTextField lastNameField;
    private final JTextField emailField;
    private final JTextField phoneField;
    private final JPasswordField passwordField;
    private final JPasswordField confirmPasswordField;

    private final JFrame parentFrame;

    public RegistrationScreen(JFrame frame)
    {
        this.parentFrame = frame;

        // Set layout for the panel
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setAlignmentX(Component.CENTER_ALIGNMENT);

        // Create a form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // First Name field
        formPanel.add(createFieldPanel("First Name", firstNameField = new JTextField()));

        // Last Name field
        formPanel.add(createFieldPanel("Last Name", lastNameField = new JTextField()));

        // Email field
        formPanel.add(createFieldPanel("Email", emailField = new JTextField()));

        // Phone field
        formPanel.add(createFieldPanel("Phone", phoneField = new JTextField()));

        // Password field
        formPanel.add(createPasswordField("Password", passwordField = new JPasswordField()));

        // Confirm Password field
        formPanel.add(createPasswordField("Confirm Password", confirmPasswordField = new JPasswordField()));

        // Register button
        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.PLAIN, 20));
        registerButton.setPreferredSize(new Dimension(200, 50));
        registerButton.addActionListener(e -> _register());
        formPanel.add(registerButton);

        // Already have account link (Login)
        JButton loginButton = new JButton("Already have an account? Login here.");
        loginButton.setFont(new Font("Arial", Font.PLAIN, 18));
        loginButton.addActionListener(e -> navigateToLogin());
        formPanel.add(loginButton);

        // Add form panel to the main panel
        add(formPanel, BorderLayout.CENTER);
    }

    // Method to create text field with label
    private JPanel createFieldPanel(String labelText, JTextField field)
    {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        label.setPreferredSize(new Dimension(90, 40));

        field.setPreferredSize(new Dimension(300, 40));
        field.setFont(new Font("Arial", Font.PLAIN, 18));

        row.add(label);
        row.add(field);
        return row;
    }

    // Method to create password field with label
    private JPanel createPasswordField(String labelText, JPasswordField field)
    {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 18));
        label.setPreferredSize(new Dimension(90, 40));

        field.setFont(new Font("Arial", Font.PLAIN, 18));
        field.setPreferredSize(new Dimension(300, 40));

        row.add(label);
        row.add(field);
        return row;
    }

    // Registration logic
    private void _register()
    {
        // Validate fields and perform registration logic
        if (firstNameField.getText().isEmpty())
        {
            showMessage("Please enter your first name.");
            return;
        }
        if (lastNameField.getText().isEmpty())
        {
            showMessage("Please enter your last name.");
            return;
        }
        if (!isValidEmail(emailField.getText()))
        {
            showMessage("Please enter a valid email address.");
            return;
        }
        if (phoneField.getText().isEmpty())
        {
            showMessage("Please enter your phone number.");
            return;
        }
        if (passwordField.getPassword().length < 6)
        {
            showMessage("Password must be at least 6 characters.");
            return;
        }
        if (!String.valueOf(confirmPasswordField.getPassword()).equals(String.valueOf(passwordField.getPassword()))) {
            showMessage("Passwords do not match.");
            return;
        }

        // If validation passes, show success message (for demo purposes)
        JOptionPane.showMessageDialog(parentFrame, "Registration successful!");
        // Here, in a real app, you would send the data to your backend or API.
    }

    // Show error message
    private void showMessage(String message)
    {
        JOptionPane.showMessageDialog(parentFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Validate email format using regex
    private boolean isValidEmail(String email)
    {
        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.matches(emailPattern, email);
    }

    // Navigate to login screen
    private void navigateToLogin()
    {
        parentFrame.getContentPane().removeAll();
        parentFrame.getContentPane().add(new LoginScreen(parentFrame)); // You need to create LoginScreen class similarly.
        parentFrame.revalidate();
        parentFrame.repaint();
    }
}
