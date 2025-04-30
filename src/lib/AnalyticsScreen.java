package lib;

// Imports for charts and datasets from JFreeChart
import org.jfree.chart.*;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

// Imports for Swing and AWT components
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Displays an analytics dashboard with charts for meal data.
 */
public class AnalyticsScreen extends JPanel
{
    private final JFrame parentFrame;  // Reference to the parent window
    private final User user;           // Current logged-in user
    private final List<MealEntry> meals; // List of meals loaded from XML

    // Theme colors
    private final Color ORANGE = new Color(255, 102, 0);
    private final Color DARK_TEXT = new Color(0, 0, 0);
    private final Color LIGHT_GREY = new Color(245, 245, 245);
    private final Color WHITE = new Color(255, 255, 255);

    /**
     * Constructor initializes layout and charts.
     */
    public AnalyticsScreen(JFrame frame, User user)
    {
        this.parentFrame = frame;
        this.user = user;
        this.meals = MealXML.loadMealsFromXML(user.username());  // Load meal data from XML

        setLayout(new BorderLayout());              // Use border layout
        setBackground(LIGHT_GREY);                  // Set background color
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding

        // Header label for the screen
        JLabel header = new JLabel("\uD83D\uDCCA Analytics Dashboard", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 26));
        header.setForeground(DARK_TEXT);
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        add(header, BorderLayout.NORTH);  // Add to top

        // Panel to hold both charts side by side
        JPanel charts = new JPanel(new GridLayout(1, 2, 15, 0));
        charts.setBackground(LIGHT_GREY);
        charts.add(createBarChartPanel());   // Left: calories over time
        charts.add(createPieChartPanel());   // Right: macronutrient breakdown
        add(charts, BorderLayout.CENTER);

        // Back button to return to dashboard
        JButton backBtn = new JButton("\u2190 Back to Dashboard");
        backBtn.setFont(new Font("Arial", Font.BOLD, 14));
        backBtn.setBackground(ORANGE);
        backBtn.setForeground(WHITE);
        backBtn.setFocusPainted(false);
        backBtn.addActionListener(e ->
        {
            parentFrame.setContentPane(new DashboardScreen(parentFrame, user));
            parentFrame.revalidate(); // Refresh screen
        });

        // Bottom panel to hold navigation button
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setBackground(LIGHT_GREY);
        bottom.add(backBtn);
        add(bottom, BorderLayout.SOUTH);
    }

    /**
     * Creates and returns a JPanel containing a bar chart of daily calories.
     */
    private JPanel createBarChartPanel()
    {
        Map<String, Integer> caloriesPerDay = new TreeMap<>(); // Keeps dates sorted

        // Aggregate total calories for each day
        for (MealEntry m : meals) {
            caloriesPerDay.put(m.date, caloriesPerDay.getOrDefault(m.date, 0) + m.calories);
        }

        // Create dataset for bar chart
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> entry : caloriesPerDay.entrySet())
        {
            dataset.addValue(entry.getValue(), "Calories", entry.getKey());
        }

        // Generate bar chart
        JFreeChart chart = ChartFactory.createBarChart(
                "Calories per Day",     // Chart title
                "Date",                 // X-axis label
                "Calories",             // Y-axis label
                dataset,                // Data
                PlotOrientation.VERTICAL,
                false, true, false      // No legend, show tooltips
        );

        // Customize chart appearance
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinesVisible(false);
        plot.getRenderer().setSeriesPaint(0, ORANGE); // Bar color

        chart.getTitle().setFont(new Font("Arial", Font.BOLD, 18));
        chart.setBackgroundPaint(WHITE);

        return new ChartPanel(chart); // Wrap chart in panel
    }

    /**
     * Creates and returns a JPanel containing a pie chart of total macronutrients.
     */
    private JPanel createPieChartPanel()
    {
        // Accumulate total grams of each macro
        int totalProtein = 0, totalCarbs = 0, totalFats = 0;
        for (MealEntry m : meals) {
            totalProtein += m.protein;
            totalCarbs += m.carbs;
            totalFats += m.fats;
        }

        // Create dataset for pie chart
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Protein", totalProtein);
        dataset.setValue("Carbs", totalCarbs);
        dataset.setValue("Fats", totalFats);

        // Create pie chart
        JFreeChart pieChart = ChartFactory.createPieChart(
                "Macronutrient Breakdown", // Title
                dataset,
                true,  // Show legend
                true,  // Enable tooltips
                false  // Disable URLs
        );

        // Customize appearance of pie chart
        PiePlot plot = (PiePlot) pieChart.getPlot();
        plot.setSectionPaint("Protein", new Color(255, 102, 0));      // Orange
        plot.setSectionPaint("Carbs", new Color(100, 149, 237));      // Cornflower blue
        plot.setSectionPaint("Fats", new Color(60, 179, 113));        // Medium sea green
        plot.setLabelFont(new Font("Arial", Font.PLAIN, 14));
        plot.setLabelBackgroundPaint(WHITE);
        plot.setLabelOutlinePaint(Color.GRAY);
        plot.setLabelShadowPaint(null);
        plot.setSimpleLabels(true);                                   // Hide leader lines
        plot.setBackgroundPaint(LIGHT_GREY);
        plot.setInteriorGap(0.04);                                    // Tight layout
        plot.setExplodePercent("Fats", 0.03);                         // Slightly pop out fats
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
                "{0}: {1}g ({2})"  // Format: name, grams, percentage
        ));

        pieChart.setBackgroundPaint(WHITE);
        return new ChartPanel(pieChart);
    }
}
