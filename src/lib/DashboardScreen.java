package lib;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * DashboardScreen provides the main user interface where users can view and update
 * their profile, log meals, view meal history, and open analytics.
 */
public class DashboardScreen extends JPanel {
    private final User user; // Current user
    private final JFrame parentFrame; // Reference to the main application frame

    // Theme colors for styling
    private final Color ORANGE = new Color(255, 102, 0);
    private final Color DARK_TEXT = new Color(0, 0, 0);
    private final Color LIGHT_GREY = new Color(245, 245, 245);
    private final Color WHITE = new Color(255, 255, 255);

    // Input fields for user profile
    private final JTextField weightField = new JTextField();
    private final JTextField heightField = new JTextField();
    private final JTextField ageField = new JTextField();

    // Dropdowns for activity level and goal selection
    private final JComboBox<NutritionCalculator.ActivityLevel> activityBox =
            new JComboBox<>(NutritionCalculator.ActivityLevel.values());
    private final JComboBox<NutritionCalculator.Goal> goalBox =
            new JComboBox<>(NutritionCalculator.Goal.values());

    // Labels to display TDEE and macro breakdown
    private final JLabel calorieLabel = new JLabel();
    private final JTextArea macroArea = new JTextArea();

    /**
     * Constructor sets up the layout and event handlers.
     */
    public DashboardScreen(JFrame frame, User user) {
        this.parentFrame = frame;
        this.user = user;

        setLayout(new BorderLayout());
        setBackground(LIGHT_GREY);

        // Greeting header
        JLabel header = new JLabel("Welcome, " + user.firstName() + "!", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 28));
        header.setForeground(DARK_TEXT);
        header.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(header, BorderLayout.NORTH);

        // Center panel includes form and stats side by side
        JPanel center = new JPanel(new BorderLayout());
        center.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        center.setBackground(LIGHT_GREY);
        center.add(profileForm(), BorderLayout.CENTER);         // Left side: profile form
        center.add(dashboardStats(), BorderLayout.EAST);        // Right side: TDEE/macros
        add(center, BorderLayout.CENTER);

        // Bottom action buttons panel
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 15));
        bottom.setBackground(LIGHT_GREY);

        JButton logMealBtn = styledButton("Log a Meal");
        JButton viewMealsBtn = styledButton("View Meal History");
        JButton analyticsBtn = styledButton("View Analytics");

        // Log meal button opens modal dialog
        logMealBtn.addActionListener(e -> {
            List<MealEntry> meals = MealXML.loadMealsFromXML(user.username());
            new LogMealDialog(parentFrame, user, meals).setVisible(true);
        });

        // View history button changes panel
        viewMealsBtn.addActionListener(e -> {
            parentFrame.setContentPane(new MealHistoryScreen(parentFrame, user));
            parentFrame.revalidate();
        });

        // View analytics button opens chart screen
        analyticsBtn.addActionListener(e -> {
            parentFrame.setContentPane(new AnalyticsScreen(parentFrame, user));
            parentFrame.revalidate();
        });

        // Add buttons to bottom
        bottom.add(logMealBtn);
        bottom.add(viewMealsBtn);
        bottom.add(analyticsBtn);
        add(bottom, BorderLayout.SOUTH);
    }

    /**
     * Creates the user profile input form panel.
     */
    private JPanel profileForm() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Populate fields with current user data
        weightField.setText(String.valueOf(user.weight()));
        heightField.setText(String.valueOf(user.heightCm()));
        ageField.setText(String.valueOf(user.age()));
        activityBox.setSelectedItem(user.activityLevel());
        goalBox.setSelectedItem(user.goal());

        // Add labeled input rows
        panel.add(labeledRow("Weight (lbs):", weightField));
        panel.add(labeledRow("Height (cm):", heightField));
        panel.add(labeledRow("Age:", ageField));
        panel.add(labeledRow("Activity Level:", activityBox));
        panel.add(labeledRow("Goal:", goalBox));

        // Add update button
        panel.add(Box.createVerticalStrut(10)); // Add space
        JButton updateButton = styledButton("Update Profile");
        updateButton.addActionListener(this::handleUpdate);
        panel.add(updateButton);

        return panel;
    }

    /**
     * Creates the TDEE and macronutrient display panel.
     */
    private JPanel dashboardStats() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Your Daily Target"));
        panel.setPreferredSize(new Dimension(280, 200));
        panel.setBackground(WHITE);

        // Calculate TDEE and macros
        double tdee = user.calculateTDEE();
        NutritionCalculator.Macronutrients macros = NutritionCalculator.calculateMacros(tdee, user.goal());

        // Display TDEE
        calorieLabel.setText("Calories: " + (int) tdee + " kcal");
        calorieLabel.setFont(new Font("Arial", Font.BOLD, 18));
        calorieLabel.setForeground(ORANGE);

        // Display macros
        macroArea.setText(macros.toString());
        macroArea.setEditable(false);
        macroArea.setBackground(WHITE);
        macroArea.setForeground(DARK_TEXT);
        macroArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        // Add components to panel
        panel.add(calorieLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(macroArea);
        return panel;
    }

    /**
     * Creates a horizontal row with a label and input component.
     */
    private JPanel labeledRow(String labelText, JComponent input) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.setBackground(WHITE);

        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(120, 30));
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setForeground(DARK_TEXT);

        input.setPreferredSize(new Dimension(200, 30));
        input.setFont(new Font("Arial", Font.PLAIN, 16));

        row.add(label);
        row.add(input);
        return row;
    }

    /**
     * Returns a styled JButton with consistent formatting.
     */
    private JButton styledButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Arial", Font.BOLD, 16));
        b.setBackground(ORANGE);
        b.setForeground(WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        return b;
    }

    /**
     * Handles the update profile action, validates input, recalculates TDEE/macros,
     * saves to XML, and refreshes the screen.
     */
    private void handleUpdate(ActionEvent e) {
        try {
            // Parse and validate form values
            double newWeight = Double.parseDouble(weightField.getText().trim());
            double newHeight = Double.parseDouble(heightField.getText().trim());
            int newAge = Integer.parseInt(ageField.getText().trim());
            var newActivity = (NutritionCalculator.ActivityLevel) activityBox.getSelectedItem();
            var newGoal = (NutritionCalculator.Goal) goalBox.getSelectedItem();

            // Create updated user instance
            User updatedUser = new User(
                    user.firstName(),
                    user.lastName(),
                    user.username(),
                    user.phone(),
                    user.password(),
                    newAge,
                    newWeight,
                    newHeight,
                    user.gender(),
                    newActivity,
                    newGoal
            );

            // Recalculate values and save
            double tdee = updatedUser.calculateTDEE();
            var macros = NutritionCalculator.calculateMacros(tdee, newGoal);
            Database.saveCalcMacrosToXML(updatedUser, tdee, newGoal, macros);

            // Notify and refresh dashboard
            JOptionPane.showMessageDialog(parentFrame, "Profile updated!");
            parentFrame.setContentPane(new DashboardScreen(parentFrame, updatedUser));
            parentFrame.revalidate();

        } catch (NumberFormatException ex) {
            // Show error if parsing fails
            JOptionPane.showMessageDialog(parentFrame, "Please enter valid numbers.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
