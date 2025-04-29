package lib;
import java.util.Scanner;
import java.util.InputMismatchException;

public class NutritionCalculator
{
    // enums used in USer record to store characteristics and nutrition needs
    public enum Gender { MALE, FEMALE } // enum used to help determine BMR
    public enum Goal { MAINTAIN, CUT, BULK } // represents fitness and nutrition goals
    public enum ActivityLevel // used to calculate TDEE
    {
        SEDENTARY(1.2), LIGHT(1.375), MODERATE(1.55), ACTIVE(1.725), VERY_ACTIVE(1.9);
        private final double multiplier;
        ActivityLevel(double multiplier) { this.multiplier = multiplier; }
        public double getMultiplier() { return multiplier; }
    }

    // used to store macros for a users diet plan
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

        // used to calculate BMR and called to create nutrition plans
    public static double calculateBMR(double weight, double height, int age, Gender gender)
    {
        /**
         * Calculates Basal Metabolic Rate.
         *
         * @param weight   body-weight in **pounds**
         * @param height   height in centimetres
         * @param age        age in whole years
         * @param gender     MALE or FEMALE
         * @return           BMR in kcal/day
         */
        return (gender == Gender.MALE)
                ? (10 * weight) + (6.25 * height) - (5 * age) + 5
                : (10 * weight) + (6.25 * height) - (5 * age) - 161;
    }

    // calculates the total number of calories burned in a day
    public static double calculateTDEE(double bmr, ActivityLevel activityLevel)
    {
        return bmr * activityLevel.getMultiplier();
    }

    // calculates macros breakdown based on intake
    public static Macronutrients calculateMacros(double totalCalories, Goal goal)
    {
        double proteinPercent, carbPercent, fatPercent;
        switch (goal) // picks which case based on goal
        {
            case CUT -> { proteinPercent = 0.40; carbPercent = 0.40; fatPercent = 0.20; }
            case BULK -> { proteinPercent = 0.30; carbPercent = 0.40; fatPercent = 0.30; }
            default -> { proteinPercent = 0.20; carbPercent = 0.50; fatPercent = 0.30; } //maintain
        }

        // return the calculated grams of each macro
        return new Macronutrients(
                (int) ((totalCalories * proteinPercent) / 4),
                (int) ((totalCalories * carbPercent) / 4),
                (int) ((totalCalories * fatPercent) / 9)
        );
    }

    // used to prompt user for input and validate it
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
