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

Side Notes: maybe I should have made it autocreate a filepath, but I dont know how to make it automatically set and managed rn

Another note: working in Cybersecurity, I would NEVER save passwords plaintext,
in reality I would create a separate database that would hash them and use that to move credentials
This is a working prototype
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
public class Database 
{
// XML document builder for saving a user list to an XML document for active storage
public static void saveUsersToXML(List<User> users) 
{
    /* saves users to a central aggregate list, and retrieval will be based on the email
    Degraded.
    I thought I could use a userlist and individual interchangable, turns out you cant.
     */
    String filePath = "users.xml";
    try {
        // black magic of document building
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element root = doc.createElement("Users");
        doc.appendChild(root);
        // saves user values for each value
        // processes each individual user and saves them to the aggregate file
        for (User user : users) {
            Element u = doc.createElement("User");
            root.appendChild(u);

            u.appendChild(createElement(doc, "FirstName", user.firstName()));
            u.appendChild(createElement(doc, "LastName", user.lastName()));
            u.appendChild(createElement(doc, "Username", user.username()));
            u.appendChild(createElement(doc, "Phone", user.phone()));
            // I would NOT do this if it werent just a prototype, you will not catch me saving passwords plaintext
            // in reality this would be a SQLite instance with stored hashes
            u.appendChild(createElement(doc, "Password", user.password()));

            u.appendChild(createElement(doc, "Age", String.valueOf(user.age())));
            u.appendChild(createElement(doc, "Weight", String.valueOf(user.weight())));
            u.appendChild(createElement(doc, "HeightCm", String.valueOf(user.height())));
            u.appendChild(createElement(doc, "Gender", user.gender().name()));
            u.appendChild(createElement(doc, "ActivityLevel", user.activityLevel().name()));
            u.appendChild(createElement(doc, "Goal", user.goal().name()));

        }
        saveDocumentToFile(doc, filePath);
        System.out.println("✅ Users saved to " + filePath);

    } catch (Exception e) 
    {
        e.printStackTrace();
    }
}
    public static void saveUserToXML(User user) 
    {
        /* saves users to a central aggregate list, and retrieval will be based on the email
        Updated version of saveUsersToXML
        This works with a single user, used for implementing user file saves when registering
        I thought I could use a list interchangably, or create a list with a single object for saving users.
        Turns out that makes things much harder
        so now we save a single user
        Kept old one as export futureproofing and artifact
         */

        String filePath = "users.xml";
        try 
        {
            // black magic of document building
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("Users");
            doc.appendChild(root);
            // saves user values for each value
            // processes each individual user and saves them to the aggregate file
                Element u = doc.createElement("User");
                root.appendChild(u);

                u.appendChild(createElement(doc, "FirstName", user.firstName()));
                u.appendChild(createElement(doc, "LastName", user.lastName()));
                u.appendChild(createElement(doc, "Username", user.username()));
                u.appendChild(createElement(doc, "Phone", user.phone()));
                // I would NOT do this if it werent just a prototype, you will not catch me saving passwords plaintext
                // in reality this would be a SQLite instance with stored hashes
                u.appendChild(createElement(doc, "Password", user.password()));

                u.appendChild(createElement(doc, "Age", String.valueOf(user.age())));
                u.appendChild(createElement(doc, "Weight", String.valueOf(user.weight())));
                u.appendChild(createElement(doc, "HeightCm", String.valueOf(user.height())));
                u.appendChild(createElement(doc, "Gender", user.gender().name()));
                u.appendChild(createElement(doc, "ActivityLevel", user.activityLevel().name()));
                u.appendChild(createElement(doc, "Goal", user.goal().name()));

            saveDocumentToFile(doc, filePath);
            System.out.println("✅ Users saved to " + filePath);

        } catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

// Function for saving our nutrition calculator record to an XML file
public static void saveCalcMacrosToXML(
        User user,
        double totalCalories,
        NutritionCalculator.Goal goal,
        NutritionCalculator.Macronutrients macros
) {
    // set file path based on users name
    String filePath = user.getFullNameNoSpace() + "Nutrition.xml";
    try {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element root = doc.createElement("UserMacronutrientCalculation");
        doc.appendChild(root);

        // Full User block
        Element u = doc.createElement("User");
        root.appendChild(u);
        u.appendChild(createElement(doc, "FirstName", user.firstName()));
        u.appendChild(createElement(doc, "LastName", user.lastName()));
        u.appendChild(createElement(doc, "Username", user.username()));

        u.appendChild(createElement(doc, "Age", String.valueOf(user.age())));
        u.appendChild(createElement(doc, "Weight", String.valueOf(user.weight())));
        u.appendChild(createElement(doc, "HeightCm", String.valueOf(user.height())));
        u.appendChild(createElement(doc, "Gender", user.gender().name()));
        u.appendChild(createElement(doc, "ActivityLevel", user.activityLevel().name()));
        u.appendChild(createElement(doc, "Goal", user.goal().name()));

        // Calculation params
        Element params = doc.createElement("CalculationParameters");
        root.appendChild(params);
        params.appendChild(createElement(doc, "TotalCalories", String.valueOf(totalCalories)));
        params.appendChild(createElement(doc, "GoalUsed", goal.name()));

        // Macronutrients
        Element m = doc.createElement("Macronutrients");
        root.appendChild(m);
        m.appendChild(createElement(doc, "ProteinGrams", String.valueOf(macros.proteinGrams())));
        m.appendChild(createElement(doc, "CarbsGrams",   String.valueOf(macros.carbsGrams())));
        m.appendChild(createElement(doc, "FatsGrams",    String.valueOf(macros.fatsGrams())));

        saveDocumentToFile(doc, filePath);
        System.out.println("✅ Calculation saved to " + filePath);

    } catch (Exception e) 
    {
        e.printStackTrace();
    }
}




    // creating elements for text nodes in XML
// this was a pain, mostly because its harder to create xml files from data than I expected.
// thanks Geeks for Geeks
    // but this is futureproofing so its easier for the machine to parse later
    private static Element createElement(Document doc, String name, String value) 
    {
        Element element = doc.createElement(name);
        element.appendChild(doc.createTextNode(value));
        return element;
    }
// saves our data to the file itself
    private static void saveDocumentToFile(Document doc, String filePath) throws Exception 
    {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(filePath));
        transformer.transform(source, result);
    }
// test main for pushing data to the files, prove it works and files exist in test cases
    public static void main(String[] args) 
    {
        // test userList, list of overall users on the app. very basic
        // tests pushing multiple users. Currently set this way so we can append the file list with additional users
        List<User> users = List.of(
                new User("Alice", "Microsoft", "alicemicrosoft", "5015555050", "123456",
                        30, 120, 160.5,
                        NutritionCalculator.Gender.FEMALE,
                        NutritionCalculator.ActivityLevel.LIGHT,
                        NutritionCalculator.Goal.CUT),

                new User("Bob", "Microsoft", "bobmicrosoft", "5015555050", "123456",
                        22, 185, 175.5,
                        NutritionCalculator.Gender.MALE,
                        NutritionCalculator.ActivityLevel.MODERATE,
                        NutritionCalculator.Goal.BULK)
        );
        // reworked the user save to only save one user, unsure if persistent and allows multiple or overwrites


        // saves the nutrition calculator recommendations
        // needed addition of the active user in the session to save the values
        // currently is free floating, we need to modify the nutrition calculator to account for current user

        // hello alice and bob from microsoft
        User alice = new User("Alice", "Microsoft", "alicemicrosoft", "5015555050", "123456",
                30, 120, 160.5,
                NutritionCalculator.Gender.FEMALE,
                NutritionCalculator.ActivityLevel.LIGHT,
                NutritionCalculator.Goal.CUT);
        User bob = new User("Bob", "Microsoft", "bobmicrosoft", "5015555050", "123456",
                22, 185, 175.5,
                NutritionCalculator.Gender.MALE,
                NutritionCalculator.ActivityLevel.MODERATE,
                NutritionCalculator.Goal.BULK);

        // reworked the user save to only save one user, unsure if persistent and allows multiple or overwrites

        Database.saveUserToXML(bob);

        // since we need the nutrition calculator to take a calorie count as an argument, have a placeholder
        double totalCalories = 1800.0;
        NutritionCalculator.Macronutrients aliceMacros =
                NutritionCalculator.calculateMacros(totalCalories, alice.goal());
        NutritionCalculator.Macronutrients bobMacros =
                NutritionCalculator.calculateMacros(totalCalories, bob.goal());

        // need to add a getter for the users name from user.java, that way we can create dynamic XML file names
        Database.saveCalcMacrosToXML(alice, totalCalories, alice.goal(), aliceMacros);
        Database.saveCalcMacrosToXML(bob, totalCalories, bob.goal(), bobMacros);
        // I honest to god thought XML's were easier than this
    }
}
