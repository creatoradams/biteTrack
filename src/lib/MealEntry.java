package lib;

/**
 * MealEntry represents a single meal's nutritional data
 * including calories and macronutrients, along with the date and name.
 */
public class MealEntry
{
    public final String date;        // Date the meal was logged (MM-dd-yyyy)
    public final String mealName;    // Name of the meal
    public final int calories;       // Total calories in the meal
    public final int protein;        // Protein in grams
    public final int carbs;          // Carbohydrates in grams
    public final int fats;           // Fats in grams

    /**
     * Constructs a new MealEntry with all required nutritional details.
     */
    public MealEntry(String date, String mealName, int calories, int protein, int carbs, int fats)
    {
        this.date = date;
        this.mealName = mealName;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fats = fats;
    }

    /**
     * Converts the meal entry into a String array for table display.
     * Order matches table columns: Date, Meal Name, Calories, Protein, Carbs, Fats.
     */
    public String[] toTableRow()
    {
        return new String[]{
                date, mealName,
                String.valueOf(calories),
                String.valueOf(protein),
                String.valueOf(carbs),
                String.valueOf(fats)
        };
    }
}
