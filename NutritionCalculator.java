import java.util.Scanner;
import java.util.InputMismatchException;

public class NutritionCalculator
{

    public enum Gender { MALE, FEMALE }
    public enum Goal { MAINTAIN, CUT, BULK }

    public enum ActivityLevel
    {
        SEDENTARY(1.2), LIGHT(1.375), MODERATE(1.55), ACTIVE(1.725), VERY_ACTIVE(1.9);
        private final double multiplier;
        ActivityLevel(double multiplier) { this.multiplier = multiplier; }
        public double getMultiplier() { return multiplier; }
    }

    public record Macronutrients(int proteinGrams, int carbsGrams, int fatsGrams)
    {

        @Override
            public String toString()
        {
                return "Protein: " + proteinGrams + "g\n" +
                        "Carbs: " + carbsGrams + "g\n" +
                        "Fats: " + fatsGrams + "g";
            }
        }

    public static double calculateBMR(double weightKg, double heightCm, int age, Gender gender)
    {
        return (gender == Gender.MALE)
                ? (10 * weightKg) + (6.25 * heightCm) - (5 * age) + 5
                : (10 * weightKg) + (6.25 * heightCm) - (5 * age) - 161;
    }

    public static double calculateTDEE(double bmr, ActivityLevel activityLevel)
    {
        return bmr * activityLevel.getMultiplier();
    }

    public static Macronutrients calculateMacros(double totalCalories, Goal goal)
    {
        double proteinPercent, carbPercent, fatPercent;
        switch (goal) {
            case CUT -> { proteinPercent = 0.40; carbPercent = 0.40; fatPercent = 0.20; }
            case BULK -> { proteinPercent = 0.30; carbPercent = 0.40; fatPercent = 0.30; }
            default -> { proteinPercent = 0.20; carbPercent = 0.50; fatPercent = 0.30; }
        }

        return new Macronutrients(
                (int) ((totalCalories * proteinPercent) / 4),
                (int) ((totalCalories * carbPercent) / 4),
                (int) ((totalCalories * fatPercent) / 9)
        );
    }

    public static void main(String[] args)
    {
        try (Scanner scanner = new Scanner(System.in))
        {
            System.out.print("Choose unit system (1 = Metric | 2 = US): ");
            boolean isMetric = scanner.nextInt() == 1;
            scanner.nextLine(); // consume newline

            System.out.print("Enter your name: ");
            String name = scanner.nextLine();

            double weightKg = isMetric
                    ? promptForDouble(scanner, "Enter your weight in kg: ")
                    : promptForDouble(scanner, "Enter your weight in pounds: ") / 2.2046;

            double heightCm = isMetric
                    ? promptForDouble(scanner, "Enter your height in cm: ")
                    : promptForDouble(scanner, "Enter your height in inches: ") * 2.54;

            int age = (int) promptForDouble(scanner, "Enter your age: ");

            Gender gender = null;
            while (gender == null) {
                System.out.print("Enter your gender (male/female): ");
                String input = scanner.next().trim().toLowerCase();
                if (input.equals("male")) gender = Gender.MALE;
                else if (input.equals("female")) gender = Gender.FEMALE;
                else System.out.println("Invalid input. Please enter 'male' or 'female'.");
            }

            ActivityLevel activityLevel = null;
            while (activityLevel == null)
            {
                System.out.println("""
                        Select your activity level:
                        1 = Sedentary       (little or no exercise)
                        2 = Light           (1–3 days/week)
                        3 = Moderate        (3–5 days/week)
                        4 = Active          (6–7 days/week)
                        5 = Very Active     (intense training or physical job)
                        """);
                int level = scanner.nextInt();
                switch (level) {
                    case 1 -> activityLevel = ActivityLevel.SEDENTARY;
                    case 2 -> activityLevel = ActivityLevel.LIGHT;
                    case 3 -> activityLevel = ActivityLevel.MODERATE;
                    case 4 -> activityLevel = ActivityLevel.ACTIVE;
                    case 5 -> activityLevel = ActivityLevel.VERY_ACTIVE;
                    default -> System.out.println("Invalid selection. Enter 1 to 5.");
                }
            }

            Goal goal = null;
            while (goal == null)
            {
                System.out.println("What is your goal? (1 = Maintain, 2 = Cut, 3 = Bulk): ");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1 -> goal = Goal.MAINTAIN;
                    case 2 -> goal = Goal.CUT;
                    case 3 -> goal = Goal.BULK;
                    default -> System.out.println("Invalid input. Enter 1, 2, or 3.");
                }
            }

            User user = new User(name, age, weightKg, heightCm, gender, activityLevel, goal);
            double bmr = user.calculateBMR();
            double maintenanceCalories = user.calculateTDEE();

            System.out.println("\n✅ " + user.name() + "'s Profile:");
            System.out.println(user);
            System.out.printf("\n📊 BMR: %.2f kcal/day\n", bmr);
            System.out.printf("🔥 Maintenance Calories (TDEE): %.2f kcal/day\n", maintenanceCalories);

            int[] calorieAdjustments = {250, 500, 1000};
            if (goal == Goal.CUT)
            {
                System.out.println("📉 Suggested calorie targets for weight loss:");
                for (int cal : calorieAdjustments)
                {
                    System.out.printf("Lose %.1f lb/week → %.2f kcal/day\n", cal / 500.0, maintenanceCalories - cal);
                }
            } else if (goal == Goal.BULK)
            {
                System.out.println("📈 Suggested calorie targets for weight gain:");
                for (int cal : calorieAdjustments)
                {
                    System.out.printf("Gain %.1f lb/week → %.2f kcal/day\n", cal / 500.0, maintenanceCalories + cal);
                }
            }

            double totalCalories = switch (goal)
            {
                case CUT -> maintenanceCalories - 500;
                case BULK -> maintenanceCalories + 500;
                default -> maintenanceCalories;
            };
            Macronutrients macros = calculateMacros(totalCalories, goal);
            System.out.printf("\n🎯 Macro Targets for Selected Goal (%.0f kcal):\n", totalCalories);
            System.out.println(macros);

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    private static double promptForDouble(Scanner scanner, String prompt)
    {
        while (true)
        {
            try
            {
                System.out.print(prompt);
                double value = scanner.nextDouble();
                if (value <= 0) throw new IllegalArgumentException("Value must be positive.");
                return value;
            } catch (InputMismatchException e)
            {
                System.out.println("❗ Please enter a valid number.");
                scanner.nextLine(); // clear buffer
            } catch (IllegalArgumentException e)
            {
                System.out.println("❗ " + e.getMessage());
            }
        }
    }
}
