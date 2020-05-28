import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import Exceptions.IllegalCostException;
import Model.Card;
import Model.Cost;
import Model.Noble;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import Model.Cost.GemColor;

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
    private final int numberOfCards = 90;

    /**
     * Total number of nobles in the noble file
     */
    private final int numberOfNobles = 10;

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
     * @return JSONLoader singleton instance
     */
    public static JSONLoader getJsonLoaderInstance() {
        if (jsonLoaderInstance == null) {
            jsonLoaderInstance =  new JSONLoader();
        }
        return jsonLoaderInstance;
    }


    /**
     *
     * Load cards from card resource file into array of Card objects
     * @return Array of Card objects
     * @throws JSONException - when there is a problem with json file format
     * @throws IllegalCostException - when cost of a card is negative
     * @throws IOException - when there was a problem with the file
     */
    public Card[] loadCards() throws JSONException, IllegalCostException, IOException {
        JSONArray jsonArrayCards = new JSONArray(Files.readString(cardsFilePath));
        Card[] cards = new Card[numberOfCards];
        for (int i = 0; i < numberOfCards; i++) {
            JSONObject jsonObjectCard = jsonArrayCards.getJSONObject(i);
            GemColor discountColor = jsonObjectCard.getEnum(GemColor.class, "Color");
            int level = jsonObjectCard.getInt("Level");
            int prestige = jsonObjectCard.getInt("Prestige");
            int white = jsonObjectCard.getInt("White");
            int green = jsonObjectCard.getInt("Green");
            int blue = jsonObjectCard.getInt("Blue");
            int black = jsonObjectCard.getInt("Black");
            int red = jsonObjectCard.getInt("Red");
            Path imageFilePath = Paths.get(jsonObjectCard.getString("ImageFile"));
            Cost cost = new Cost(white, green, blue, black, red);
            cards[i] = new Card(i+1,cost,discountColor,prestige,level,imageFilePath);
        }
        return cards;
    }

    /**
     * Load nobles from noble resource file into array of Noble objects
     * @return Array of Noble objects
     * @throws JSONException - when there is a problem with json file format
     * @throws IllegalCostException - when cost of a card is negative
     * @throws IOException - when there was a problem with the file
     */
    public Noble[] loadNobles() throws JSONException, IllegalCostException, IOException {
        JSONArray jsonArrayCards = new JSONArray(Files.readString(nobleFilePath));
        Noble[] nobles = new Noble[numberOfNobles];
        for (int i = 0; i < numberOfNobles; i++) {
            JSONObject jsonObjectNoble = jsonArrayCards.getJSONObject(i);
            int prestige = jsonObjectNoble.getInt("Prestige");
            int white = jsonObjectNoble.getInt("White");
            int green = jsonObjectNoble.getInt("Green");
            int blue = jsonObjectNoble.getInt("Blue");
            int black = jsonObjectNoble.getInt("Black");
            int red = jsonObjectNoble.getInt("Red");
            Path imageFilePath = Paths.get(jsonObjectNoble.getString("ImageFile"));
            Cost cost = new Cost(white, green, blue, black, red);
            nobles[i] = new Noble(i+1, cost, prestige, imageFilePath);
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
     * @return card information resource path
     */
    public Path getNobleFilePath() {
        return nobleFilePath;
    }

    /**
     * Main for quick presentation
     * @param args - not used
     */
    public static void main(String[] args) throws Exception{
        JSONLoader jsonLoader = new JSONLoader();
        for (Card card : jsonLoader.loadCards()) {
            System.out.println(card);
        }
        for (Noble noble : jsonLoader.loadNobles()) {
            System.out.println(noble);
        }
    }
}
