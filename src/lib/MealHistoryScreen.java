package lib;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * MealHistoryScreen displays a table view of all meals logged by the user.
 */
public class MealHistoryScreen extends JPanel
{
    private final JFrame parentFrame;      // Reference to the parent window
    private final User user;               // The currently logged-in user
    private final List<MealEntry> mealList; // List of meal entries from XML

    // Define UI color constants
    private final Color ORANGE = new Color(255, 102, 0);
    private final Color DARK_TEXT = new Color(0, 0, 0);
    private final Color LIGHT_GREY = new Color(245, 245, 245);
    private final Color WHITE = new Color(255, 255, 255);

    /**
     * Constructor builds the meal history table and layout.
     */
    public MealHistoryScreen(JFrame frame, User user)
    {
        this.parentFrame = frame;
        this.user = user;
        this.mealList = MealXML.loadMealsFromXML(user.username()); // Load user's meals from XML

        // Set layout and background
        setLayout(new BorderLayout());
        setBackground(LIGHT_GREY);
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // Padding

        // Header label with user's name
        JLabel title = new JLabel("🍽️ Meal History for " + user.getFullName(), SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(DARK_TEXT);
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // Define table columns
        String[] columns = {"Date", "Meal", "Calories", "Protein", "Carbs", "Fats"};

        // Populate the table model with meal data
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        for (MealEntry m : mealList) {
            model.addRow(m.toTableRow());
        }

        // Create the table with custom styling
        JTable table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(24);
        table.setGridColor(LIGHT_GREY);
        table.setShowVerticalLines(false);
        table.setBackground(WHITE);
        table.setForeground(DARK_TEXT);

        // Add table to scroll pane and to center of layout
        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(WHITE);
        add(scroll, BorderLayout.CENTER);

        // Back button to return to dashboard
        JButton backBtn = new JButton("← Back to Dashboard");
        backBtn.setBackground(ORANGE);
        backBtn.setForeground(WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setFont(new Font("Arial", Font.BOLD, 14));
        backBtn.addActionListener(e -> {
            parentFrame.setContentPane(new DashboardScreen(parentFrame, user)); // Replace panel
            parentFrame.revalidate(); // Refresh layout
        });

        // Bottom panel for navigation button
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setBackground(LIGHT_GREY);
        bottom.add(backBtn);
        add(bottom, BorderLayout.SOUTH);
    }
}
