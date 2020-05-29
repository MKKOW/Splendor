package Controller;

import Model.Cost;
import Model.Cost.GemColor;
import Model.DevelopmentCard;
import Model.Noble;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Class for loading Model objects
 * from files in json format.
 *
 * Design pattern: Singleton
 */
public class JSONLoader {

    /**
     * Path to resource with cards information
     */
    private final Path cardsFilePath = Paths.get("src/main/resources/cards.json");

    /**
     * Path to resource with noble information
     */
    private final Path nobleFilePath = Paths.get("src/main/resources/nobles.json");

    /**
     * Total number of cards in the card file
     */
    private static final int numberOfCards = 90;

    /**
     * Total number of nobles in the noble file
     */
    private static final int numberOfNobles = 10;

    /**
     * Singleton instance
     */
    private static JSONLoader jsonLoaderInstance = null;

    /**
     * Private constructor
     */
    private JSONLoader() { }

    /**
     * Factory
     *
     * @return Controller.JSONLoader singleton instance
     */
    public static JSONLoader getJsonLoaderInstance() {
        if (jsonLoaderInstance == null) {
            jsonLoaderInstance = new JSONLoader();
        }
        return jsonLoaderInstance;
    }

    /**
     * Main for quick presentation
     *
     * @param args - not used
     */
    public static void main(String[] args) throws Exception {
        JSONLoader jsonLoader = new JSONLoader();
        for (DevelopmentCard developmentCard : jsonLoader.loadCards()) {
            System.out.println(developmentCard);
        }
        for (Noble noble : jsonLoader.loadNobles()) {
            System.out.println(noble);
        }
    }

    /**
     * Load cards from card resource file into array of DevelopmentCard objects
     *
     * @return Array of DevelopmentCard objects
     * @throws JSONException            - when there is a problem with json file format
     * @throws IllegalArgumentException - If any card or cost argument is wrong
     * @throws IOException              - when there was a problem with the file
     */
    public DevelopmentCard[] loadCards() throws JSONException, IOException, IllegalArgumentException {
        JSONArray jsonArrayCards = new JSONArray(Files.readString(cardsFilePath));
        DevelopmentCard[] developmentCards = new DevelopmentCard[numberOfCards];
        for (int i = 0; i < numberOfCards; i++) {
            JSONObject jsonObjectCard = jsonArrayCards.getJSONObject(i);
            GemColor discountColor = jsonObjectCard.getEnum(GemColor.class, "Color");
            DevelopmentCard.Level level = DevelopmentCard.Level.fromInt(jsonObjectCard.getInt("Level"));
            int prestige = jsonObjectCard.getInt("Prestige");
            Path imageFilePath = Paths.get(jsonObjectCard.getString("ImageFile"));
            Cost cost = parseCost(jsonObjectCard);
            developmentCards[i] = new DevelopmentCard(i + 1, cost, prestige, imageFilePath, level, discountColor);
        }
        return developmentCards;
    }

    /**
     * Load nobles from noble resource file into array of Noble objects
     *
     * @return Array of Noble objects
     * @throws JSONException            - when there is a problem with json file format
     * @throws IllegalArgumentException - when cost of a card is negative
     * @throws IOException              - when there was a problem with the file
     */
    public Noble[] loadNobles() throws JSONException, IllegalArgumentException, IOException {
        JSONArray jsonArrayCards = new JSONArray(Files.readString(nobleFilePath));
        Noble[] nobles = new Noble[numberOfNobles];
        for (int i = 0; i < numberOfNobles; i++) {
            JSONObject jsonObjectNoble = jsonArrayCards.getJSONObject(i);
            Path imageFilePath = Paths.get(jsonObjectNoble.getString("ImageFile"));
            Cost cost = parseCost(jsonObjectNoble);
            int prestige = jsonObjectNoble.getInt("Prestige");
            nobles[i] = new Noble(i + 1, cost, prestige, imageFilePath);
        }
        return nobles;
    }


    /**
     * Get path to resource with cards information
     * @return card information resource path
     */
    public Path getCardsFilePath() {
        return cardsFilePath;
    }

    /**
     * Get path to resource with cards information
     *
     * @return card information resource path
     */
    public Path getNobleFilePath() {
        return nobleFilePath;
    }

    private Cost parseCost(JSONObject jsonString) throws IllegalArgumentException {
        int white = jsonString.getInt("White");
        int green = jsonString.getInt("Green");
        int blue = jsonString.getInt("Blue");
        int black = jsonString.getInt("Black");
        int red = jsonString.getInt("Red");
        return new Cost(white, green, blue, black, red);
    }
}
