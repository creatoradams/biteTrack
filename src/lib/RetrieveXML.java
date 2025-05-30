/*
Ethan Dykes
Basic XML data retriever for our working database replacement.
reads from the files stored from database.java

if I were to create an actual app, I would utilize a SQLite database
this would allow for secure storage of data, and instead of having to pull multiple files or multiple objects
from storage into memory, we would instead be able to pull what exactly we needed
this is why I do not have any form of batch storage or retrieval, as this is just a working prototype
 */
package lib;
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
    public static Result loadUserMacrosFromXML(String filePath)
    {
        Result result = null;
        try
        {
            File xmlFile = new File(filePath);
            if (!xmlFile.exists())
            {
                System.out.println("❌ File not found: " + filePath);
                return null;
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            Element ux = (Element) doc.getElementsByTagName("User").item(0);
            User user = new User(
                    getTagValue(ux, "FirstName"),
                    getTagValue(ux, "LastName"),
                    getTagValue(ux, "Username"),
                    getTagValue(ux, "Phone"),
                    getTagValue(ux, "Password"),          // debug only!
                    Integer.parseInt(getTagValue(ux, "Age")),
                    Double.parseDouble(getTagValue(ux, "Weight")),
                    Double.parseDouble(getTagValue(ux, "Height(in)")),
                    NutritionCalculator.Gender.valueOf(getTagValue(ux, "Gender")),
                    NutritionCalculator.ActivityLevel.valueOf(getTagValue(ux, "ActivityLevel")),
                    NutritionCalculator.Goal.valueOf(getTagValue(ux, "Goal"))
            );

            Element params = (Element) doc
                    .getElementsByTagName("CalculationParameters")
                    .item(0);
            double totalCalories = Double.parseDouble(getTagValue(params, "TotalCalories"));
            NutritionCalculator.Goal goal =
                    NutritionCalculator.Goal.valueOf(getTagValue(params, "GoalUsed"));

            Element mx = (Element) doc.getElementsByTagName("Macronutrients").item(0);
            NutritionCalculator.Macronutrients macros = new NutritionCalculator.Macronutrients(
                    Integer.parseInt(getTagValue(mx, "ProteinGrams")),
                    Integer.parseInt(getTagValue(mx, "CarbsGrams")),
                    Integer.parseInt(getTagValue(mx, "FatsGrams"))
            );

            result = new Result(user, totalCalories, goal, macros);
            System.out.println("✅ Data loaded successfully from " + filePath);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }


    // Load a list of Users from an XML file
    // this loads our overall user list
    public static ArrayList<User> loadUserListFromXML(String filename)
    {
        ArrayList<User> userList = new ArrayList<>();
        try
        {
            File file = new File(filename);
            if (!file.exists()) return userList;

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);

            NodeList userNodes = doc.getElementsByTagName("user");

            for (int i = 0; i < userNodes.getLength(); i++)
            {
                Element userElement = (Element) userNodes.item(i);

                String firstName = userElement.getElementsByTagName("firstName").item(0).getTextContent();
                String lastName = userElement.getElementsByTagName("lastName").item(0).getTextContent();
                String username = userElement.getElementsByTagName("username").item(0).getTextContent();
                String phone = userElement.getElementsByTagName("phone").item(0).getTextContent();
                String password = userElement.getElementsByTagName("password").item(0).getTextContent();

                int age = Integer.parseInt(userElement.getElementsByTagName("age").item(0).getTextContent());
                double weight = Double.parseDouble(userElement.getElementsByTagName("weight").item(0).getTextContent());
                double height = Double.parseDouble(userElement.getElementsByTagName("height(in)").item(0).getTextContent());

                NutritionCalculator.Gender gender = NutritionCalculator.Gender.valueOf(
                        userElement.getElementsByTagName("gender").item(0).getTextContent()
                );
                NutritionCalculator.ActivityLevel activityLevel = NutritionCalculator.ActivityLevel.valueOf(
                        userElement.getElementsByTagName("activityLevel").item(0).getTextContent()
                );
                NutritionCalculator.Goal goal = NutritionCalculator.Goal.valueOf(
                        userElement.getElementsByTagName("goal").item(0).getTextContent()
                );

                User user = new User(
                        firstName, lastName, username, phone, password,
                        age, weight, height, gender, activityLevel, goal
                );

                userList.add(user);
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return userList;
    }


    // Debug print method for a list of users
    // returns all user data and goal values
    // in reality this would be attached to an encrypted database for user privacy
    public static void printUserList(List<User> users)
    {
        for (User user : users)
        {
            System.out.println("\n👤 User:");
            System.out.println("  First Name: " + user.firstName());
            System.out.println("  Last Name: " + user.lastName());
            System.out.println("  Email: " + user.getUsername());
            System.out.println("  Phone: " + user.phone());
            System.out.println("  Age: " + user.age());
            System.out.println("  Weight: " + user.weight());
            System.out.println("  Height: " + user.height());
            System.out.println("  Gender: " + user.gender());
            System.out.println("  ActivityLevel: " + user.activityLevel());
            System.out.println("  Goal: " + user.goal());
        }
    }

    // Helper to extract tag text content
    // something something this wouldnt work without this according to geeksforgeeks
    private static String getTagValue(Element element, String tagName)
    {
        NodeList list = element.getElementsByTagName(tagName);
        if (list.getLength() > 0)
        {
            return list.item(0).getTextContent();
        } else {
            return "N/A";
        }
    }

    // Result class to hold one User + calculation + macros
    // this is essentially loading our saved values to memory for the application
    public static class Result
    {
        public User user;
        public double totalCalories;
        public NutritionCalculator.Goal goal;
        public NutritionCalculator.Macronutrients macros;

        public Result(User user, double totalCalories, NutritionCalculator.Goal goal, NutritionCalculator.Macronutrients macros)
        {
            this.user = user;
            this.totalCalories = totalCalories;
            this.goal = goal;
            this.macros = macros;
        }

        // Debug print for result
        public void printToConsole()
        {
            System.out.println("\n👤 User:");
            System.out.println("  First Name: " + user.firstName());
            System.out.println("  Last Name: " + user.lastName());
            System.out.println("  Email: " + user.getUsername());
            System.out.println("  Phone: " + user.phone());
            System.out.println("  Age: " + user.age());
            System.out.println("  Weight: " + user.weight());
            System.out.println("  Height: " + user.height());
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

    public static void main(String[] args)
    {
        // test retrieval of stored files
        // demonstrates that it works, will use a different method to retrieve from inside the app
        // retrieve our XML data
        try (Scanner scanner = new Scanner(System.in))
        {
            // retrieve macros calculator with assigned user
            System.out.print("Enter the macros calculation filename for retrieval: ");
            String filePath = scanner.nextLine();
            RetrieveXML.Result result = RetrieveXML.loadUserMacrosFromXML(filePath);

            if (result != null)
            {
                result.printToConsole();
            }
            // retrieve our stored app userlist, only good for sign on systems as we probably wont be needing to retrieve a full user list
            System.out.print("Enter the userlist filename for retrieval: ");
            filePath = scanner.nextLine();
            List<User> users = RetrieveXML.loadUserListFromXML(filePath);
            RetrieveXML.printUserList(users);




        } catch (Exception e)
        {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
}
