package lib;
/*
public record User(String name, int age, double weight, double heightCm, NutritionCalculator.Gender gender,
                   NutritionCalculator.ActivityLevel activityLevel, NutritionCalculator.Goal goal)
{
    */
    public record User(String firstName, String lastName, String username, String phone, String password,
                       int age, double weight, double heightCm, NutritionCalculator.Gender gender,
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
                "First Name: " + firstName + "\n" +
                "Last Name: " + lastName + "\n" +
                "Username: " + username + "\n" +
                "Phone: " + phone + "\n" +
                "Age: " + age + " years\n" +
                "Weight: " + String.format("%.2f", weight) + " lbs\n" +
                "Height: " + String.format("%.2f", heightCm) + " cm\n" +
                "Gender: " + gender + "\n" +
                "Activity Level: " + activityLevel + "\n" +
                "Goal: " + goal;
    }
    // this is for debugging to make sure the password is stored, PLEASE DELETE WHEN DONE
    public String getPassword() {
        return password;
    }
    public String getFullNameNoSpace() {
        return firstName + lastName;
    }
    public String getFullName() {
        return firstName + " " + lastName;
    }
    public String getUsername() {
        return username;
    }
}
