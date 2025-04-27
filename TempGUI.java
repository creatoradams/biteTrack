import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TempGUI extends JFrame
{

    private JTextField nameField, weightField, heightField, ageField;
    private JComboBox<String> unitBox, genderBox, activityBox, goalBox;
    private OutputPanel outputPanel;

    public void tempGUI()
    {
        setTitle("Calorie & Macro Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ========== TOP INPUT PANEL ==========
        JPanel inputPanel = new JPanel(new GridLayout(8, 2, 5, 5));

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Unit System:"));
        unitBox = new JComboBox<>(new String[]{"Metric (kg/cm)", "US (lb/in)"});
        inputPanel.add(unitBox);

        inputPanel.add(new JLabel("Weight:"));
        weightField = new JTextField();
        inputPanel.add(weightField);

        inputPanel.add(new JLabel("Height:"));
        heightField = new JTextField();
        inputPanel.add(heightField);

        inputPanel.add(new JLabel("Age:"));
        ageField = new JTextField();
        inputPanel.add(ageField);

        inputPanel.add(new JLabel("Gender:"));
        genderBox = new JComboBox<>(new String[]{"Male", "Female"});
        inputPanel.add(genderBox);

        inputPanel.add(new JLabel("Activity Level:"));
        activityBox = new JComboBox<>(new String[]{
                "Sedentary (1)", "Light (2)", "Moderate (3)", "Active (4)", "Very Active (5)"
        });
        inputPanel.add(activityBox);

        inputPanel.add(new JLabel("Goal:"));
        goalBox = new JComboBox<>(new String[]{"Maintain", "Cut", "Bulk"});
        inputPanel.add(goalBox);

        add(inputPanel, BorderLayout.NORTH);

        // ========== BOTTOM OUTPUT PANEL ==========
        outputPanel = new OutputPanel();
        outputPanel.setPreferredSize(new Dimension(400, 220));
        add(outputPanel, BorderLayout.CENTER);

        // ========== BUTTON ==========
        JButton calcButton = new JButton("Calculate");
        add(calcButton, BorderLayout.SOUTH);
        calcButton.addActionListener(new CalculateListener());

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ========== ACTION LISTENER ==========
    private class CalculateListener implements ActionListener
    {
        @SuppressWarnings("DefaultLocale")
        public void actionPerformed(ActionEvent e)
        {
            try
            {
                String name = nameField.getText().trim();
                double weight = Double.parseDouble(weightField.getText().trim());
                double height = Double.parseDouble(heightField.getText().trim());
                int age = Integer.parseInt(ageField.getText().trim());

                boolean isMetric = unitBox.getSelectedIndex() == 0;
                double weightKg = isMetric ? weight : weight / 2.2046;
                double heightCm = isMetric ? height : height * 2.54;

                NutritionCalculator.Gender gender = genderBox.getSelectedIndex() == 0
                        ? NutritionCalculator.Gender.MALE : NutritionCalculator.Gender.FEMALE;

                NutritionCalculator.ActivityLevel activity = switch (activityBox.getSelectedIndex())
                {
                    case 0 -> NutritionCalculator.ActivityLevel.SEDENTARY;
                    case 1 -> NutritionCalculator.ActivityLevel.LIGHT;
                    case 2 -> NutritionCalculator.ActivityLevel.MODERATE;
                    case 3 -> NutritionCalculator.ActivityLevel.ACTIVE;
                    case 4 -> NutritionCalculator.ActivityLevel.VERY_ACTIVE;
                    default -> throw new IllegalArgumentException("Invalid activity");
                };

                NutritionCalculator.Goal goal = NutritionCalculator.Goal.values()[goalBox.getSelectedIndex()];

                User user = new User(name, age, weightKg, heightCm, gender, activity, goal);
                double bmr = user.calculateBMR();
                double tdee = user.calculateTDEE();
                double adjusted = switch (goal) {
                    case CUT -> tdee - 500;
                    case BULK -> tdee + 500;
                    default -> tdee;
                };

                NutritionCalculator.Macronutrients macros =
                        NutritionCalculator.calculateMacros(adjusted, goal);

                outputPanel.setOutput(String.format("""
                        Name: %s
                        BMR: %.2f kcal/day
                        TDEE: %.2f kcal/day
                        Goal: %s (%.0f kcal)
                        Protein: %dg
                        Carbs: %dg
                        Fats: %dg
                        """,
                        name, bmr, tdee, goal, adjusted,
                        macros.proteinGrams(), macros.carbsGrams(), macros.fatsGrams()
                ));
                outputPanel.repaint();

            } catch (Exception ex)
            {
                JOptionPane.showMessageDialog(TempGUI.this,
                        "⚠ Error: " + ex.getMessage(),
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ========== CUSTOM OUTPUT PANEL ==========
    private static class OutputPanel extends JPanel
    {
        private String output = "";

        public void setOutput(String text)
        {
            this.output = text;
        }

        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            g.setFont(new Font("Consolas", Font.PLAIN, 14));
            int y = 20;
            for (String line : output.split("\n"))
            {
                g.drawString(line, 20, y);
                y += 20;
            }
        }
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(TempGUI::new);
    }
}