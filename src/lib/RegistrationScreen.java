package lib;
import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;

public class RegistrationScreen extends JPanel
{
    // Create text fields for each input field
    private final JTextField firstNameField;
    private final JTextField lastNameField;
    private final JTextField userNameField;
    private final JTextField phoneField;
    private final JPasswordField passwordField;
    private final JPasswordField confirmPasswordField;
    private final JFrame parentFrame;
    private final JTextField ageField = new JTextField();
    private final JTextField weightField = new JTextField();
    private final JTextField heightField = new JTextField();
    private final JComboBox<NutritionCalculator.Gender> genderCombo =
            new JComboBox<>(NutritionCalculator.Gender.values());
    private final JComboBox<NutritionCalculator.ActivityLevel> activityLevelCombo =
            new JComboBox<>(NutritionCalculator.ActivityLevel.values());
    private final JComboBox<NutritionCalculator.Goal> goalCombo =
            new JComboBox<>(NutritionCalculator.Goal.values());
    public RegistrationScreen(JFrame frame)
    {
        this.parentFrame = frame;

        // Set layout for the panel
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 20));

        // Create a form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(formPanel, BorderLayout.CENTER);
        // First Name field
        formPanel.add(createFieldPanel("First Name", firstNameField = new JTextField()));

        // Last Name field
        formPanel.add(createFieldPanel("Last Name", lastNameField = new JTextField()));

        // Email field
        formPanel.add(createFieldPanel("Username", userNameField = new JTextField()));

        // Phone field
        formPanel.add(createFieldPanel("Phone", phoneField = new JTextField()));

        // Password field
        formPanel.add(createPasswordField("Password", passwordField = new JPasswordField()));

        // Confirm Password field
        formPanel.add(createPasswordField("Confirm Password", confirmPasswordField = new JPasswordField()));

        formPanel.add(createFieldPanel("Age", ageField));
        formPanel.add(createFieldPanel("Weight", weightField));
        formPanel.add(createFieldPanel("Height(in)", heightField));

        formPanel.add(createFieldPanel("Gender", genderCombo));
        formPanel.add(createFieldPanel("Activity", activityLevelCombo));
        formPanel.add(createFieldPanel("Goal", goalCombo));


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
    private JPanel createFieldPanel(String labelText, JComponent field)
    {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Arial", Font.PLAIN, 18));
        lbl.setPreferredSize(new Dimension(90, 30));   // fixed width so rows align

        // Give text components a consistent size
        if (field instanceof JTextField txt)
        {
            txt.setFont(new Font("Arial", Font.PLAIN, 18));
            txt.setPreferredSize(new Dimension(300, 40));
        } else if (field instanceof JPasswordField pwd)
        {
            pwd.setFont(new Font("Arial", Font.PLAIN, 18));
            pwd.setPreferredSize(new Dimension(300, 40));
        } else if (field instanceof JComboBox<?> combo)
        {
            combo.setFont(new Font("Arial", Font.PLAIN, 18));
            combo.setPreferredSize(new Dimension(300, 40));
        }


        row.add(lbl);
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
        System.out.println("_register reached");   //  ← temporary test line

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
        if (!isValidUsername(userNameField.getText()))
        {
            showMessage("Please enter a valid username.");
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

        // Build the user record from form widgets
        User newUser;
        try
        {
            newUser = buildUser();
        } catch (NumberFormatException ex)
        {
            showMessage("Age, weight, and height must be numeric.");
            return;
        }

        /* ----- TODO: persist newUser to file / DB here ----- */


        // If validation passes, show success message (for demo purposes)
        JOptionPane.showMessageDialog(parentFrame, "Registration successful!");

        // Navigate to the home/dashboard panel and pass the user
        parentFrame.setContentPane(new HomeScreen(newUser));   // you'll overload HomeScreen
        parentFrame.revalidate(); parentFrame.repaint();
    }

    // Show error message
    private void showMessage(String message)
    {
        JOptionPane.showMessageDialog(parentFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Validate username format using regex
    private boolean isValidUsername(String username)
    {
        // allow letters, numbers, underscores and dashes, 3–20 chars
        return username.matches("^[A-Za-z0-9_-]{3,20}$");
    }

    // Navigate to login screen
    private void navigateToLogin()
    {
        parentFrame.getContentPane().removeAll();
        parentFrame.getContentPane().add(new LoginScreen(parentFrame)); // You need to create LoginScreen class similarly.
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    //build a user record
    private User buildUser()
    {
        /**
         * Reads the form widgets, converts them to the correct units / enums,
         * and returns a populated User record.
         *
         * @throws NumberFormatException if the user typed non-numeric text
         *                               into age, weight, or height fields.
         */

        // Gather and trim plain text values
        String firstName = firstNameField.getText().trim();
        String lastName  = lastNameField.getText().trim();
        String username = userNameField.getText().trim();

        // Convert numeric strings to numbers
        int age = Integer.parseInt(ageField.getText().trim());
        double weight = Double.parseDouble(weightField.getText().trim());
        double heightCm = Double.parseDouble(heightField.getText().trim());

        // Read enum selections from combo boxes
        NutritionCalculator.Gender gender = (NutritionCalculator.Gender) genderCombo.getSelectedItem();
        NutritionCalculator.ActivityLevel activityLevel = (NutritionCalculator.ActivityLevel) activityLevelCombo.getSelectedItem();
        NutritionCalculator.Goal goal = (NutritionCalculator.Goal) goalCombo.getSelectedItem();

        // Build and return the immutable User record
        return new User(
                username,
                age,
                weight,
                heightCm,
                gender,
                activityLevel,
                goal
        );
    }
}
