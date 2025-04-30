package lib;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RegistrationScreen extends JPanel
{
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
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 20));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(formPanel, BorderLayout.CENTER);

        formPanel.add(createFieldPanel("First Name", firstNameField = new JTextField()));
        formPanel.add(createFieldPanel("Last Name", lastNameField = new JTextField()));
        formPanel.add(createFieldPanel("Username", userNameField = new JTextField()));
        formPanel.add(createFieldPanel("Phone", phoneField = new JTextField()));
        formPanel.add(createPasswordField("Password", passwordField = new JPasswordField()));
        formPanel.add(createPasswordField("Confirm Password", confirmPasswordField = new JPasswordField()));

        formPanel.add(createFieldPanel("Age", ageField));
        formPanel.add(createFieldPanel("Weight", weightField));
        formPanel.add(createFieldPanel("Height(in)", heightField));
        formPanel.add(createFieldPanel("Gender", genderCombo));
        formPanel.add(createFieldPanel("Activity", activityLevelCombo));
        formPanel.add(createFieldPanel("Goal", goalCombo));

        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.PLAIN, 20));
        registerButton.setPreferredSize(new Dimension(200, 50));
        registerButton.addActionListener(e -> _register());
        formPanel.add(registerButton);

        JButton loginButton = new JButton("Already have an account? Login here.");
        loginButton.setFont(new Font("Arial", Font.PLAIN, 18));
        loginButton.addActionListener(e -> navigateToLogin());
        formPanel.add(loginButton);

        add(formPanel, BorderLayout.CENTER);
    }

    private JPanel createFieldPanel(String labelText, JComponent field)
    {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Arial", Font.PLAIN, 18));
        lbl.setPreferredSize(new Dimension(90, 30));

        field.setFont(new Font("Arial", Font.PLAIN, 18));
        field.setPreferredSize(new Dimension(300, 40));
        row.add(lbl);
        row.add(field);
        return row;
    }

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

    private void _register()
    {
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
        if (!String.valueOf(confirmPasswordField.getPassword()).equals(String.valueOf(passwordField.getPassword())))
        {
            showMessage("Passwords do not match.");
            return;
        }

        User newUser;
        try
        {
            newUser = buildUser();
        } catch (NumberFormatException ex)
        {
            showMessage("Age, weight, and height must be numeric.");
            return;
        }

        // Save to users.xml
        List<User> users = RetrieveXML.loadUserListFromXML("users.xml");
        users.add(newUser);
        Database.saveUsersToXML(users);

        JOptionPane.showMessageDialog(parentFrame, "Registration successful!");

        // Navigate to dashboard
        parentFrame.setContentPane(new DashboardScreen(parentFrame, newUser));
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    private void showMessage(String message)
    {
        JOptionPane.showMessageDialog(parentFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private boolean isValidUsername(String username) {
        return username.matches("^[A-Za-z0-9_-]{3,20}$");
    }

    private void navigateToLogin()
    {
        parentFrame.getContentPane().removeAll();
        parentFrame.getContentPane().add(new LoginScreen(parentFrame));
        parentFrame.revalidate();
        parentFrame.repaint();
    }

    private User buildUser()
    {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String username = userNameField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = String.valueOf(passwordField.getPassword());

        int age = Integer.parseInt(ageField.getText().trim());
        double weight = Double.parseDouble(weightField.getText().trim());
        double heightCm = Double.parseDouble(heightField.getText().trim());

        NutritionCalculator.Gender gender = (NutritionCalculator.Gender) genderCombo.getSelectedItem();
        NutritionCalculator.ActivityLevel activityLevel = (NutritionCalculator.ActivityLevel) activityLevelCombo.getSelectedItem();
        NutritionCalculator.Goal goal = (NutritionCalculator.Goal) goalCombo.getSelectedItem();

        return new User(
                firstName,
                lastName,
                username,
                phone,
                password,
                age,
                weight,
                heightCm,
                gender,
                activityLevel,
                goal
        );
    }
}
