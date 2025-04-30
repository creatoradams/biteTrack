package lib;
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

    /*
    Ethan's implementations
    Added here to assist the frontend
    also allows me to add additional values for file storage pointing
     */
    public String getFullNameNoSpace() {
        return firstName + lastName;
    }
    public String getFullName() {
        return firstName + " " + lastName;
    }
    public String getUsername() {
        return username;
    }
    // I would NEVER do this, however since there is not password hashing implemented there is no option for login
    public String getPassword() {
        return password;
    }
}
