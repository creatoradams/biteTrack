public record User(String name, int age, double weightKg, double heightCm, NutritionCalculator.Gender gender,
                   NutritionCalculator.ActivityLevel activityLevel, NutritionCalculator.Goal goal) {

    public double calculateBMR() {
        return NutritionCalculator.calculateBMR(weightKg, heightCm, age, gender);
    }

    public double calculateTDEE() {
        return NutritionCalculator.calculateTDEE(calculateBMR(), activityLevel);
    }

    @SuppressWarnings("DefaultLocale")
    @Override
    public String toString() {
        return "👤 User Profile\n" +
                "Name: " + name + "\n" +
                "Age: " + age + " years\n" +
                "Weight: " + String.format("%.2f", weightKg) + " kg\n" +
                "Height: " + String.format("%.2f", heightCm) + " cm\n" +
                "Gender: " + gender + "\n" +
                "Activity Level: " + activityLevel + "\n" +
                "Goal: " + goal;
    }
}
