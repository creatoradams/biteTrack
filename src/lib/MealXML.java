package lib;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * MealXML handles saving and loading MealEntry objects to and from XML files.
 * Each user has a dedicated XML file for their meal history.
 */
public class MealXML {

    /**
     * Saves a list of MealEntry objects to an XML file named by the user's username.
     *
     * @param username the username used to generate the XML filename
     * @param meals    the list of MealEntry objects to save
     */
    public static void saveMealsToXML(String username, List<MealEntry> meals) {
        try {
            // Create new XML document
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            // Create root <Meals> element
            Element root = doc.createElement("Meals");
            doc.appendChild(root);

            // Add each meal as a <Meal> element with child fields
            for (MealEntry m : meals) {
                Element meal = doc.createElement("Meal");
                root.appendChild(meal);

                appendChild(doc, meal, "Date", m.date);
                appendChild(doc, meal, "Name", m.mealName);
                appendChild(doc, meal, "Calories", String.valueOf(m.calories));
                appendChild(doc, meal, "Protein", String.valueOf(m.protein));
                appendChild(doc, meal, "Carbs", String.valueOf(m.carbs));
                appendChild(doc, meal, "Fats", String.valueOf(m.fats));
            }

            // Write XML content to file
            String filename = "meals_" + username + ".xml";
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // Pretty print

            transformer.transform(new DOMSource(doc), new StreamResult(new File(filename)));

        } catch (Exception e) {
            e.printStackTrace(); // Log errors to console
        }
    }

    /**
     * Loads a list of MealEntry objects from an XML file named by the user's username.
     *
     * @param username the username used to locate the XML file
     * @return list of MealEntry objects
     */
    public static List<MealEntry> loadMealsFromXML(String username) {
        List<MealEntry> meals = new ArrayList<>();
        String filename = "meals_" + username + ".xml";

        try {
            File file = new File(filename);
            if (!file.exists()) return meals; // Return empty list if file not found

            // Parse XML document
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            // Loop through each <Meal> element and extract its data
            NodeList list = doc.getElementsByTagName("Meal");
            for (int i = 0; i < list.getLength(); i++) {
                Element meal = (Element) list.item(i);
                String date = getValue(meal, "Date");
                String name = getValue(meal, "Name");
                int calories = Integer.parseInt(getValue(meal, "Calories"));
                int protein = Integer.parseInt(getValue(meal, "Protein"));
                int carbs = Integer.parseInt(getValue(meal, "Carbs"));
                int fats = Integer.parseInt(getValue(meal, "Fats"));

                // Add reconstructed MealEntry to the list
                meals.add(new MealEntry(date, name, calories, protein, carbs, fats));
            }

        } catch (Exception e) {
            e.printStackTrace(); // Log errors
        }

        return meals;
    }

    /**
     * Helper method to append a tag with text content to a parent element.
     */
    private static void appendChild(Document doc, Element parent, String tag, String value) {
        Element e = doc.createElement(tag);
        e.appendChild(doc.createTextNode(value));
        parent.appendChild(e);
    }

    /**
     * Helper method to retrieve the text content of a tag within a given element.
     */
    private static String getValue(Element element, String tagName) {
        NodeList nodes = element.getElementsByTagName(tagName);
        return nodes.getLength() > 0 ? nodes.item(0).getTextContent() : "";
    }
}
