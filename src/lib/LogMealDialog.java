package lib;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * LogMealDialog provides a modal window for users to enter
 * details about a meal and save it to their history.
 */
public class LogMealDialog extends JDialog
{
    // Input fields for meal data
    private final JTextField mealNameField = new JTextField();
    private final JTextField caloriesField = new JTextField();
    private final JTextField proteinField = new JTextField();
    private final JTextField carbsField = new JTextField();
    private final JTextField fatsField = new JTextField();

    // Current user and their meal list
    private final User user;
    private final List<MealEntry> mealList;

    // Theme colors for styling
    private final Color ORANGE = new Color(255, 102, 0);
    private final Color DARK_TEXT = new Color(0, 0, 0);
    private final Color LIGHT_GREY = new Color(245, 245, 245);
    private final Color WHITE = new Color(255, 255, 255);

    /**
     * Constructor sets up the form and dialog layout.
     */
    public LogMealDialog(JFrame parent, User user, List<MealEntry> mealList)
    {
        super(parent, "Log a Meal", true); // modal dialog
        this.user = user;
        this.mealList = mealList;

        // Layout and basic setup
        setLayout(new BorderLayout());
        setSize(400, 350);
        setLocationRelativeTo(parent); // center on parent
        getContentPane().setBackground(WHITE);

        // Panel that holds the form fields
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        form.setBackground(WHITE);

        // Add each labeled input field to the form
        form.add(labeledField("Meal Name", mealNameField));
        form.add(labeledField("Calories", caloriesField));
        form.add(labeledField("Protein (g)", proteinField));
        form.add(labeledField("Carbs (g)", carbsField));
        form.add(labeledField("Fats (g)", fatsField));

        // Create and style save button
        JButton saveBtn = new JButton("Save Meal");
        saveBtn.setBackground(ORANGE);
        saveBtn.setForeground(WHITE);
        saveBtn.setFont(new Font("Arial", Font.BOLD, 16));
        saveBtn.setFocusPainted(false);
        saveBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        saveBtn.addActionListener(e -> saveMeal()); // on click

        form.add(Box.createVerticalStrut(10)); // spacing
        form.add(saveBtn);

        // Add form to the dialog
        add(form, BorderLayout.CENTER);
    }

    /**
     * Utility method to create a row with a label and a text field.
     */
    private JPanel labeledField(String labelText, JTextField field)
    {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.setBackground(Color.WHITE);

        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(100, 30));
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(DARK_TEXT);

        field.setPreferredSize(new Dimension(200, 30));
        field.setFont(new Font("Arial", Font.PLAIN, 14));

        row.add(label);
        row.add(field);
        return row;
    }

    /**
     * Saves the meal to the user's meal list and XML file.
     */
    private void saveMeal()
    {
        try
        {
            // Get and validate input values
            String name = mealNameField.getText().trim();
            int calories = Integer.parseInt(caloriesField.getText().trim());
            int protein = Integer.parseInt(proteinField.getText().trim());
            int carbs = Integer.parseInt(carbsField.getText().trim());
            int fats = Integer.parseInt(fatsField.getText().trim());

            if (name.isEmpty())
            {
                JOptionPane.showMessageDialog(this, "Please enter a meal name.");
                return;
            }

            // Get today's date in MM-dd-yyyy format
            String date = new SimpleDateFormat("MM-dd-yyyy").format(new Date());

            // Create new MealEntry and save it
            MealEntry entry = new MealEntry(date, name, calories, protein, carbs, fats);
            mealList.add(entry); // add to memory
            MealXML.saveMealsToXML(user.username(), mealList); // save to file

            // Confirm and close
            JOptionPane.showMessageDialog(this, "Meal saved!");
            dispose(); // close dialog
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers.");
        }
    }
}
