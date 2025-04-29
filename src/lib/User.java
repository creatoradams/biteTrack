package lib;
public record User(String name, int age, double weight, double heightCm, NutritionCalculator.Gender gender,
                   NutritionCalculator.ActivityLevel activityLevel, NutritionCalculator.Goal goal)
{

    // send to
    public double calculateBMR() {
        return NutritionCalculator.calculateBMR(weight, heightCm, age, gender);
    }
    public double calculateTDEE() {
        return NutritionCalculator.calculateTDEE(calculateBMR(), activityLevel);
    }

    @SuppressWarnings("DefaultLocale")
    @Override
    //profile dump
    public String toString()
    {
        return "👤 User Profile\n" +
                "Name: " + name + "\n" +
                "Age: " + age + " years\n" +
                "Weight: " + String.format("%.2f", weight) + " lbs\n" +
                "Height: " + String.format("%.2f", heightCm) + " cm\n" +
                "Gender: " + gender + "\n" +
                "Activity Level: " + activityLevel + "\n" +
                "Goal: " + goal;
    }
}
