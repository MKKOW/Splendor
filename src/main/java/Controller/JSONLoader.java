package Controller;

import Model.Cost;
import Model.Cost.GemColor;
import Model.DevelopmentCard;
import Model.Noble;
import Model.Rules;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;

/**
 * Class for loading Model objects
 * from files in json format.
 *
 * Design pattern: Singleton
 */
public class JSONLoader {


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
    public static JSONLoader getInstance() {
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
        JSONArray jsonArrayCards = new JSONArray(Files.readString(Rules.cardsFilePath));
        DevelopmentCard[] developmentCards = new DevelopmentCard[Rules.numberOfDevelopmentCardsInFile];
        for (int i = 0; i < Rules.numberOfDevelopmentCardsInFile; i++) {
            JSONObject jsonObjectCard = jsonArrayCards.getJSONObject(i);
            GemColor discountColor = jsonObjectCard.getEnum(GemColor.class, "Color");
            DevelopmentCard.Level level = DevelopmentCard.Level.fromInt(jsonObjectCard.getInt("Level"));
            int prestige = jsonObjectCard.getInt("Prestige");
            Cost cost = parseCost(jsonObjectCard);
            developmentCards[i] = new DevelopmentCard(i + 1, cost, prestige, level, discountColor);
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
        JSONArray jsonArrayCards = new JSONArray(Files.readString(Rules.nobleFilePath));
        Noble[] nobles = new Noble[Rules.numberOfNoblesInFile];
        for (int i = 0; i < Rules.numberOfNoblesInFile; i++) {
            JSONObject jsonObjectNoble = jsonArrayCards.getJSONObject(i);
            Cost cost = parseCost(jsonObjectNoble);
            int prestige = jsonObjectNoble.getInt("Prestige");
            nobles[i] = new Noble(i + 1, cost, prestige);
        }
        return nobles;
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
