/*
Ethan Dykes
Class for basic implementation of Database implementation. For simplicity it outputs to XML files.
If opportune, will reimplement with a SQLite instance Database
For separate functions, one saves the user data to an XML for user reference between instances.
Will create a later class with gets data

if I were to create an actual app, I would utilize a SQLite database
this would allow for secure storage of data, and instead of having to pull multiple files or multiple objects
from storage into memory, we would instead be able to pull what exactly we needed
this is why I do not have any form of batch storage or retrieval, as this is just a working prototype
*/
package lib;
// WHAT IN THE DEPENDENCIES
import org.w3c.dom.Document;
import org.w3c.dom.Element;
// srs what is this
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.List;

// still need to implement a macros record, but should be easy since its already inside the Nutrition calculator
public class Database {
// XML document builder for saving a user list to an XML document for active storage
    public static void saveUsersToXML(List<User> users, String filePath) {
        // so far it saves users created to an aggregate list, we can split this to multiple files based on user name
        try {
            // document bulder BS, this is dark magic
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("Users");
            doc.appendChild(root);

            for (User user : users) {
                Element userElement = doc.createElement("User");
                root.appendChild(userElement);

                // saves our user values

                userElement.appendChild(createElement(doc, "Name", user.name()));
                userElement.appendChild(createElement(doc, "Age", String.valueOf(user.age())));
                userElement.appendChild(createElement(doc, "WeightKg", String.valueOf(user.weight())));
                userElement.appendChild(createElement(doc, "HeightCm", String.valueOf(user.heightCm())));
                userElement.appendChild(createElement(doc, "Gender", user.gender().name()));
                userElement.appendChild(createElement(doc, "ActivityLevel", user.activityLevel().name()));
                userElement.appendChild(createElement(doc, "Goal", user.goal().name()));
            }

            saveDocumentToFile(doc, filePath);
            // debug line
            //System.out.println("✅ User data saved to " + filePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
        TBH this was so complex I thought about saving the data to a CSV for .txt and just custom write
        the code parsing, would make it much more targeted
        but this might be easier to upgrade to a SQLite instance
        also makes it easier to pull the data from later
         */
    }
// Function for saving our nutrition calculator record to an XML file
public static void saveCalcMacrosToXML(User user, double totalCalories, NutritionCalculator.Goal goal, NutritionCalculator.Macronutrients macros, String filePath) {
    try {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        // Root element
        Element root = doc.createElement("UserMacronutrientCalculation");
        doc.appendChild(root);

        // User element
        Element userElement = doc.createElement("User");
        root.appendChild(userElement);

        userElement.appendChild(createElement(doc, "Name", user.name()));
        userElement.appendChild(createElement(doc, "Age", String.valueOf(user.age())));
        userElement.appendChild(createElement(doc, "WeightKg", String.valueOf(user.weight())));
        userElement.appendChild(createElement(doc, "HeightCm", String.valueOf(user.heightCm())));
        userElement.appendChild(createElement(doc, "Gender", user.gender().name()));
        userElement.appendChild(createElement(doc, "ActivityLevel", user.activityLevel().name()));
        userElement.appendChild(createElement(doc, "Goal", user.goal().name()));

        // Calculation Parameters element
        Element paramsElement = doc.createElement("CalculationParameters");
        root.appendChild(paramsElement);
        paramsElement.appendChild(createElement(doc, "TotalCalories", String.valueOf(totalCalories)));
        paramsElement.appendChild(createElement(doc, "GoalUsed", goal.name()));

        // Macronutrients element
        Element macrosElement = doc.createElement("Macronutrients");
        root.appendChild(macrosElement);

        macrosElement.appendChild(createElement(doc, "ProteinGrams", String.valueOf(macros.proteinGrams())));
        macrosElement.appendChild(createElement(doc, "CarbsGrams", String.valueOf(macros.carbsGrams())));
        macrosElement.appendChild(createElement(doc, "FatsGrams", String.valueOf(macros.fatsGrams())));

        saveDocumentToFile(doc, filePath);
        // debug line
        System.out.println("✅ Macronutrient calculation saved to " + filePath);

    } catch (Exception e) {
        e.printStackTrace();
    }
}


    // creating elements for text nodes in XML
// this was a pain, mostly because its harder to create xml files from data than I expected.
// thanks Geeks for Geeks
    // but this is futureproofing so its easier for the machine to parse later
    private static Element createElement(Document doc, String name, String value) {
        Element element = doc.createElement(name);
        element.appendChild(doc.createTextNode(value));
        return element;
    }
// saves our data to the file itself
    private static void saveDocumentToFile(Document doc, String filePath) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(filePath));
        transformer.transform(source, result);
    }
// test main for pushing data to the files, prove it works and files exist in test cases
    public static void main(String[] args) {
        // test userList, list of overall users on the app. very basic
        // tests pushing multiple users. Currently set this way so we can append the file list with additional users
        List<User> users = List.of(
                new User("Alice", 25, 60.0, 165.0,
                        NutritionCalculator.Gender.FEMALE,
                        NutritionCalculator.ActivityLevel.MODERATE,
                        NutritionCalculator.Goal.CUT),

                new User("Bob", 30, 75.0, 180.0,
                        NutritionCalculator.Gender.MALE,
                        NutritionCalculator.ActivityLevel.ACTIVE,
                        NutritionCalculator.Goal.BULK)
        );

        Database.saveUsersToXML(users, "testUsers.xml");

        // saves the nutrition calculator recommendations
        // needed addition of the active user in the session to save the values
        // currently is free floating, we need to modify the nutrition calculator to account for current user

        // hello alice and bob from microsoft
        User alice = new User("Alice", 25, 60.0, 165.0,
                NutritionCalculator.Gender.FEMALE,
                NutritionCalculator.ActivityLevel.MODERATE,
                NutritionCalculator.Goal.CUT);
        User bob = new User("Bob", 30, 80.0, 175.0,
                NutritionCalculator.Gender.MALE,
                NutritionCalculator.ActivityLevel.LIGHT,
                NutritionCalculator.Goal.BULK);

        // since we need the nutrition calculator to take a calorie count as an argument, have a placeholder
        double totalCalories = 1800.0;
        NutritionCalculator.Macronutrients aliceMacros =
                NutritionCalculator.calculateMacros(totalCalories, alice.goal());
        NutritionCalculator.Macronutrients bobMacros =
                NutritionCalculator.calculateMacros(totalCalories, bob.goal());

        // need to add a getter for the users name from user.java, that way we can create dynamic XML file names
        Database.saveCalcMacrosToXML(alice, totalCalories, alice.goal(), aliceMacros, alice.name()+"macroGoals.xml");
        Database.saveCalcMacrosToXML(bob, totalCalories, bob.goal(), bobMacros, bob.name()+"macroGoals.xml");
        // I honest to god though XML's were easier than this
    }
}
