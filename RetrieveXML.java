/*
Ethan Dykes
Basic XML data retriever for our working database replacement.
reads from the files stored from database.java

if I were to create an actual app, I would utilize a SQLite database
this would allow for secure storage of data, and instead of having to pull multiple files or multiple objects
from storage into memory, we would instead be able to pull what exactly we needed
this is why I do not have any form of batch storage or retrieval, as this is just a working prototype
 */
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
// for main test cases
import java.util.Scanner;

public class RetrieveXML {

    // Load a full user + macro calculation from an XML file
    // this is what retrieves our macro calculator and goal for the end user
    // since a macro goal is user defined, it makes sense to have it assigned to a user
    public static Result loadUserMacrosFromXML(String filePath) {
        Result result = null;

        // checks for given filepath
        try {
            File xmlFile = new File(filePath);
            if (!xmlFile.exists()) {
                System.out.println("❌ File not found: " + filePath);
                return null;
            }
            // basic building objects so we can read and parse the XML files
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            // Retrieve User
            Element userElement = (Element) doc.getElementsByTagName("User").item(0);
            User user = new User(
                    // assigns the user values to the object
                    getTagValue(userElement, "Name"),
                    Integer.parseInt(getTagValue(userElement, "Age")),
                    Double.parseDouble(getTagValue(userElement, "WeightKg")),
                    Double.parseDouble(getTagValue(userElement, "HeightCm")),
                    NutritionCalculator.Gender.valueOf(getTagValue(userElement, "Gender")),
                    NutritionCalculator.ActivityLevel.valueOf(getTagValue(userElement, "ActivityLevel")),
                    NutritionCalculator.Goal.valueOf(getTagValue(userElement, "Goal"))
            );

            // Retrieve user goal values
            Element paramsElement = (Element) doc.getElementsByTagName("CalculationParameters").item(0);
            double totalCalories = Double.parseDouble(getTagValue(paramsElement, "TotalCalories"));
            NutritionCalculator.Goal goal = NutritionCalculator.Goal.valueOf(getTagValue(paramsElement, "GoalUsed"));

            // Retrieve Macronutrient for the user
            Element macrosElement = (Element) doc.getElementsByTagName("Macronutrients").item(0);
            NutritionCalculator.Macronutrients macros = new NutritionCalculator.Macronutrients(
                    Integer.parseInt(getTagValue(macrosElement, "ProteinGrams")),
                    Integer.parseInt(getTagValue(macrosElement, "CarbsGrams")),
                    Integer.parseInt(getTagValue(macrosElement, "FatsGrams"))
            );

            result = new Result(user, totalCalories, goal, macros);
            System.out.println("✅ Data loaded successfully from " + filePath);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    // Load a list of Users from an XML file
    // this loads our overall user list
    public static List<User> loadUserListFromXML(String filePath) {
        List<User> users = new ArrayList<>();

        try {
            File xmlFile = new File(filePath);
            if (!xmlFile.exists()) {
                System.out.println("❌ File not found: " + filePath);
                return users;
            }

            // basic building objects so we can read and parse the XML files
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList userNodes = doc.getElementsByTagName("User");

            for (int i = 0; i < userNodes.getLength(); i++) {
                Node userNode = userNodes.item(i);
                if (userNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element userElement = (Element) userNode;

                    User user = new User(
                            getTagValue(userElement, "Name"),
                            Integer.parseInt(getTagValue(userElement, "Age")),
                            Double.parseDouble(getTagValue(userElement, "WeightKg")),
                            Double.parseDouble(getTagValue(userElement, "HeightCm")),
                            NutritionCalculator.Gender.valueOf(getTagValue(userElement, "Gender")),
                            NutritionCalculator.ActivityLevel.valueOf(getTagValue(userElement, "ActivityLevel")),
                            NutritionCalculator.Goal.valueOf(getTagValue(userElement, "Goal"))
                    );

                    users.add(user);
                }
            }

            System.out.println("✅ Loaded " + users.size() + " user(s) from " + filePath);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    // Debug print method for a list of users
    // returns all user data and goal values
    // in reality this would be attached to an encrypted database for user privacy
    public static void printUserList(List<User> users) {
        for (User user : users) {
            System.out.println("\n👤 User:");
            System.out.println("  Name: " + user.name());
            System.out.println("  Age: " + user.age());
            System.out.println("  WeightKg: " + user.weightKg());
            System.out.println("  HeightCm: " + user.heightCm());
            System.out.println("  Gender: " + user.gender());
            System.out.println("  ActivityLevel: " + user.activityLevel());
            System.out.println("  Goal: " + user.goal());
        }
    }

    // Helper to extract tag text content
    // something something this wouldnt work without this according to geeksforgeeks
    private static String getTagValue(Element element, String tagName) {
        NodeList list = element.getElementsByTagName(tagName);
        if (list.getLength() > 0) {
            return list.item(0).getTextContent();
        } else {
            return "N/A";
        }
    }

    // Result class to hold one User + calculation + macros
    // this is essentially loading our saved values to memory for the application
    public static class Result {
        public User user;
        public double totalCalories;
        public NutritionCalculator.Goal goal;
        public NutritionCalculator.Macronutrients macros;

        public Result(User user, double totalCalories, NutritionCalculator.Goal goal, NutritionCalculator.Macronutrients macros) {
            this.user = user;
            this.totalCalories = totalCalories;
            this.goal = goal;
            this.macros = macros;
        }

        // Debug print for result
        public void printToConsole() {
            System.out.println("\n👤 User:");
            System.out.println("  Name: " + user.name());
            System.out.println("  Age: " + user.age());
            System.out.println("  WeightKg: " + user.weightKg());
            System.out.println("  HeightCm: " + user.heightCm());
            System.out.println("  Gender: " + user.gender());
            System.out.println("  ActivityLevel: " + user.activityLevel());
            System.out.println("  Goal: " + user.goal());

            System.out.println("\n⚙️ Calculation Parameters:");
            System.out.println("  TotalCalories: " + totalCalories);
            System.out.println("  GoalUsed: " + goal);

            System.out.println("\n🍽️ Macronutrients:");
            System.out.println("  ProteinGrams: " + macros.proteinGrams());
            System.out.println("  CarbsGrams: " + macros.carbsGrams());
            System.out.println("  FatsGrams: " + macros.fatsGrams());
        }
    }



    public static void main(String[] args) {
        // test retrieval of stored files
        // demonstrates that it works, will use a different method to retrieve from inside the app
        // retrieve our XML data
        try (Scanner scanner = new Scanner(System.in))
        {
            // retrieve macros calculator with assigned user
            System.out.print("Enter the macros calculation filename for retrieval: ");
            String filePath = scanner.nextLine();
            RetrieveXML.Result result = RetrieveXML.loadUserMacrosFromXML(filePath);

            if (result != null) {
                result.printToConsole();
            }
            // retrieve our stored app userlist, only good for sign on systems as we probably wont be needing to retrieve a full user list
            System.out.print("Enter the userlist filename for retrieval: ");
            filePath = scanner.nextLine();
            List<User> users = RetrieveXML.loadUserListFromXML(filePath);
            RetrieveXML.printUserList(users);




        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
}
