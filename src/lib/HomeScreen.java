package lib;

import javax.swing.*;
import java.awt.*;

class HomeScreen extends JPanel
{
    private final User user;
    public HomeScreen(User user)
    {
        this.user = user;

        // Set the layout for the panel
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // Create a label to greet user
        String greetText = (user == null) ? "Welcome to BiteTrack!" : "Nice to see you, " + user.name() + "!";
        JLabel greet = new JLabel(greetText, SwingConstants.CENTER);
        greet.setFont(new Font("Arial", Font.BOLD, 22));


        // macro summary panel
        assert user != null;
        double tdee = user.calculateTDEE();
        var macros  = NutritionCalculator.calculateMacros(tdee, user.goal());
        JPanel summaryPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Daily Macro Goals"));

        // labels for macro totals
        JLabel caloriesLabel = new JLabel("Calories: " + (int) tdee + " kcal", SwingConstants.CENTER);
        JLabel proteinLabel = new JLabel("Protein: " + macros.proteinGrams() + " g", SwingConstants.CENTER);
        JLabel carbsLabel = new JLabel("Carbs: " + macros.carbsGrams() + " g", SwingConstants.CENTER);
        JLabel fatsLabel = new JLabel("Fats: " + macros.fatsGrams() + " g", SwingConstants.CENTER);

        // Meal buttons
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonsPanel.setBorder(BorderFactory.createTitledBorder("Log a Meal"));


        add(buttonsPanel, BorderLayout.CENTER);

        // Add the label to the panel
        add(greet, BorderLayout.NORTH);
        summaryPanel.add(caloriesLabel);
        summaryPanel.add(proteinLabel);
        summaryPanel.add(carbsLabel);
        summaryPanel.add(fatsLabel);
        add(summaryPanel, BorderLayout.NORTH);
    }

}