package server;

import Exceptions.InactivePlayersException;
import Exceptions.NobleNotSelectedException;
import Exceptions.TooMuchCashException;
import Model.*;
import client.Card;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.management.PlatformLoggingMXBean;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.lang.Thread.sleep;

public class Client implements Runnable{
    /**
     * Default port to create client socket
     */
    private static final int defaultPort = 2000;
    /**
     * Default server address
     */
    private static String host = "127.0.0.1";
    /**
     * Default client version
     */
    // TODO: sposob przydzielania ?
    private final String clientVersion = "1.0";
    /**
     * Chosen server port
     */
    private final int serverPort;
    /**
     * Client unique UUID
     */
    private UUID clientID;
    /**
     * Data input
     */
    private ObjectInputStream inputStream;
    /**
     * Data output
     */
    private ObjectOutputStream outputStream;
    /**
     * List of nicknames
     */
    private ArrayList<String> nicknames;
    /**
     * Client's nick
     */
    private String nick = "";

    private ClientBoard board;

    private final HashMap<String, Player> players = new HashMap<>();
    private JSONObject currentResponse = null;
    private JSONObject currentBoard = null;

    /**
     * Gets client's nickname
     * @return nick
     */
    public String getNick () {
        return nick;
    }

    /**
     * Sets clientID from given String
     * @param clientID client ID as String
     */
    public void setClientID(String clientID) {
        this.clientID = UUID.fromString(clientID);
    }

    /**
     * Getter for client's ID
     * @return client ID
     */
    public String getStringID() {
        return clientID.toString();
    }

    /**
     * Client constructor
     * @param port server port
     * @param host server address
     */
    public Client(int port, String host){
        this.serverPort = port;
        Client.host = host;
    }
    private String chooseNick() {
        System.out.println("Podaj swój nick: ");
        Scanner scn = new Scanner(System.in);
        String tmp = scn.nextLine();
        String input;

        // Local verification on client
        while (!(tmp.length() >= 3 && tmp.length() <= 10)) {
            System.out.println("Nick niedozwolony");
            tmp = scn.nextLine();
        }
        this.nick = tmp;
        return tmp;
    }
    /**
     * Sends client hello to server and chooses player's nick
     */
    public void sayHello() throws IOException, ClassNotFoundException {
        String input="";
        JSONObject jsonObject=new JSONObject();
        chooseNick();
        System.out.println("Twój nick to: " + nick);
        //System.out.println(clientID);

        // Sending chosen nickname
        jsonObject = sendAndGetNick(input,jsonObject);
        currentResponse = jsonObject;
        // Verification from server
        while (jsonObject.getString("result").equals("invalid")) {
            System.out.println(jsonObject.getString("message"));
            chooseNick();
            jsonObject = sendAndGetNick(input,jsonObject);
            currentResponse = jsonObject;
        }
        if (jsonObject.getString("result").equals("ok"))
            System.out.println("dobre");
    }

    private JSONObject sendAndGetNick(String input, JSONObject jsonObject) throws IOException, ClassNotFoundException {
        String jsonString = new JSONObject()
                .put("request_type", "client_hello")
                .put("client_id", getStringID())
                .put("set_nick", getNick())
                .toString();
        outputStream.writeObject(jsonString);
        outputStream.flush();
        input = (String) inputStream.readObject();
        jsonObject = new JSONObject(input);
        currentResponse = jsonObject;
        return jsonObject;

    }
    /**
     * Gets and parses hello answer from server and sets given clientID
     */
    public void getserverHello()  {
        String input;
        String[] tmp;
        try{
            input = (String) inputStream.readObject();
            System.out.println(input);
            JSONObject jsonObject = new JSONObject(input);
            currentResponse = jsonObject;
            setClientID(jsonObject.getString("set_client_id"));
            System.out.println(clientID);
            //tmp = jsonObject.getString("nicknames").split(",");
            nicknames = new ArrayList<>(Arrays.asList(jsonObject.getString("nicknames").replaceAll("(^\\[|\\]$)", "").split(", ")));
            System.out.println(nicknames.toString());
        }
        catch (IOException | ClassNotFoundException e ){
            e.printStackTrace();
        }
    }

    /**
     * Begins the actual game and sends info about the board
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void gameStart() throws IOException, ClassNotFoundException, InactivePlayersException, TooMuchCashException, NobleNotSelectedException {

        String input = (String) inputStream.readObject();
        JSONObject jsonObject = new JSONObject(input);
        currentResponse = jsonObject;
        nicknames = new ArrayList<>(Arrays.asList(jsonObject.getString("nicknames").replaceAll("(^\\[|\\]$)", "").split(", ")));
        System.out.println(nicknames.toString());
       // System.out.println(jsonObject.getString("Board"));
        getNewBoard(jsonObject,true);
    }

    public void getNewBoard(JSONObject jsonObject, boolean setup) throws TooMuchCashException, NobleNotSelectedException {
        Model.ServerBoard serverBoard = new ServerBoard();
        String boardString = jsonObject.getString("Board");
        System.out.println(boardString);

        JSONObject boardJSON = new JSONObject(boardString);
        currentBoard = boardJSON;
        serverBoard.setBankCash(new BankCash(boardJSON.getJSONObject("bankCash").getInt("white"),
                boardJSON.getJSONObject("bankCash").getInt("green"),
                boardJSON.getJSONObject("bankCash").getInt("blue"),
                boardJSON.getJSONObject("bankCash").getInt("black"),
                boardJSON.getJSONObject("bankCash").getInt("red"),
                boardJSON.getJSONObject("bankCash").getInt("yellow")));
        JSONArray nobles = boardJSON.getJSONObject("nobles").getJSONArray("nobles");
        Noble[] noblesArr = new Noble[nobles.length()];
        for(int i=0; i<nobles.length(); i++) {
            JSONObject jsonLord=nobles.getJSONObject(i);
            JSONObject costJSON=jsonLord.getJSONObject("cost");
            Model.Cost cost = new Model.Cost(costJSON.getInt("white"),
                    costJSON.getInt("green"),
                    costJSON.getInt("blue"),
                    costJSON.getInt("black"),
                    costJSON.getInt("red"));
            Model.Noble noble = new Model.Noble(jsonLord.getInt("id"),
                    cost,
                    jsonLord.getInt("prestige"));
            noblesArr[i] = noble;
            // serverBoard.setPlayers();
        }

        serverBoard.setNobles(new NoblesOnBoard(noblesArr));
        if(setup) {
            for (int i = 0; i < nicknames.size(); i++) {
                Player player;
                if (i == 0) {
                    player = new Player(true, nicknames.get(i));
                } else {
                    player = new Player(false, nicknames.get(i));
                }
                players.put(player.getNick(), player);
            }
            serverBoard.setPlayers(players);
            serverBoard.setActivePlayer(nicknames.get(0));
        }
        JSONObject playersJSON = boardJSON.getJSONObject("players");
        for(int i=0; i<nicknames.size(); i++) {
            //String nick = playersJSON.get(nicknames.get(i));
            JSONObject currentPlayer = playersJSON.getJSONObject(nicknames.get(i));
            Cash cash = new Cash(currentPlayer.getJSONObject("cash").getInt("white"),
                    currentPlayer.getJSONObject("cash").getInt("green"),
                    currentPlayer.getJSONObject("cash").getInt("blue"),
                    currentPlayer.getJSONObject("cash").getInt("black"),
                    currentPlayer.getJSONObject("cash").getInt("red"),
                    currentPlayer.getJSONObject("cash").getInt("yellow"));

        }
        serverBoard.setPlayers(players);

        CardsOnBoard cardsOnBoard = new CardsOnBoard();
        DevelopmentCard[][] cards = new DevelopmentCard[3][4];
        for (int i=0; i<3; i++) {
            JSONArray developmentJSON = boardJSON.getJSONObject("developmentCardsOnBoard").getJSONArray("level"+(i+1));
            for(int j=0; j<developmentJSON.length(); j++) {
                JSONObject cardJSON = developmentJSON.getJSONObject(j);
                JSONObject costJSON = cardJSON.getJSONObject("cost");
                Model.Cost cost = new Model.Cost(costJSON.getInt("white"),
                        costJSON.getInt("green"),
                        costJSON.getInt("blue"),
                        costJSON.getInt("black"),
                        costJSON.getInt("red"));
                String level = cardJSON.getString("level");
                JSONObject discountJSON = cardJSON.getJSONObject("discount");
                Model.Cost discount = new Model.Cost(discountJSON.getInt("white"),
                        discountJSON.getInt("green"),
                        discountJSON.getInt("blue"),
                        discountJSON.getInt("black"),
                        discountJSON.getInt("red"));
                Model.DevelopmentCard card = null;
                switch (level) {
                    case "One": {
                        card = new Model.DevelopmentCard(cardJSON.getInt("id"),
                                cost,
                                cardJSON.getInt("prestige"),
                                Model.DevelopmentCard.Level.One,
                                discount);
                        break;
                    }
                    case "Two": {
                        card = new Model.DevelopmentCard(cardJSON.getInt("id"),
                                cost,
                                cardJSON.getInt("prestige"),
                                Model.DevelopmentCard.Level.Two,
                                discount);
                        break;
                    }
                    case "Three": {
                        card = new Model.DevelopmentCard(cardJSON.getInt("id"),
                                cost,
                                cardJSON.getInt("prestige"),
                                Model.DevelopmentCard.Level.Three,
                                discount);
                        break;
                    }

                }
                cards[i][j]=card;
            }
        }
        cardsOnBoard.setLevel1(cards[0]);
        cardsOnBoard.setLevel2(cards[1]);
        cardsOnBoard.setLevel3(cards[2]);
        serverBoard.setDevelopmentCardsOnBoard(cardsOnBoard);
        ClientBoard.setInstanceFromServerBoard(serverBoard);
        System.out.println(ClientBoard.getInstance().toString());
    }

  /*  public void play () {
        String response;
        switch(move) {
            case 0: {
                response = new JSONObject()
                        .put("request_type","buy_card")
                        .put("client_id",getStringID())
                        .put("place",board.toString())
                        .toString();

                          "hand" || "table",
                        "card_id" : int,
                "noble_id" : int || null

            }
        }
        System.out.println("Koncze ture " + Server.turn);
        increment();
    }*/

    /**
     * 4 steps connection to server: sending and receiving hello, verifying nick, starting game
     */
    /*
    public void connectToServer() {
        serverHello(getServerMessage());
        System.out.println("My UUID: " + getStringID());
        sayHello();
        //while(!verifyNick(getServerMessage())) sayHello();
        //startGame();
    }
     */
    private void updateGame() throws IOException, ClassNotFoundException, TooMuchCashException, NobleNotSelectedException {
        String input = (String) inputStream.readObject();
        JSONObject jsonObject = new JSONObject(input);
        currentBoard = jsonObject;
        getNewBoard(jsonObject, false);
        ClientBoard.getInstance().setActivePlayer(jsonObject.getString("player"));
    }
    private void buycard() throws IOException {
        String place;
        int id;
        Scanner scn = new Scanner(System.in);
        System.out.println("opcje karty");
        place = scn.next();
        id = scn.nextInt();
        while(!(place.equals("hand")||place.equals("table"))||!(0<id&&id<90)) {
            System.out.println("Bład");
            place = scn.next();
            id = scn.nextInt();
        }
        String jsonString = new JSONObject()
                .put("request_type", "buy_card")
                .put("client_id", getStringID())
                .put("place", place)
                .put("card_id", id)
                .toString();
        System.out.println(jsonString);
        outputStream.writeObject(jsonString);
        outputStream.flush();

    }
    private void reservecard() throws IOException {
        int id;
        Scanner scn = new Scanner(System.in);
        System.out.println("opcje rezerwacja");
        id = scn.nextInt();
        while(!(0<id&&id<90)) {
            System.out.println("Bład");
            id = scn.nextInt();
        }
        String jsonString = new JSONObject()
                .put("request_type", "claim_card")
                .put("client_id", getStringID())
                .put("card_id", id)
                .toString();
        System.out.println(jsonString);
        outputStream.writeObject(jsonString);
        outputStream.flush();

    }
    private void getgems() throws IOException {
        int move;
        int[] gems = new int[5];
        int sum = 0;

        Scanner scn = new Scanner(System.in);
        System.out.println("opcje klejnoty");
        move = scn.nextInt();
        switch (move) {
            case 1: {
                while(sum!=2) {
                    sum = 0;
                    System.out.println("Weź 2 klejnoty z wybranego stosu");
                    for (int i = 0; i < gems.length; i++) {
                        gems[i] = scn.nextInt();
                        while (gems[i] != 2 && gems[i] != 0) {
                            System.out.println("Bład");
                            gems[i] = scn.nextInt();
                        }
                        sum = sum + gems[i];
                    }
                }
                String jsonString = new JSONObject()
                        .put("request_type", "get_gems")
                        .put("client_id", getStringID())
                        .put("white", gems[0])
                        .put("green", gems[1])
                        .put("blue", gems[2])
                        .put("black", gems[3])
                        .put("red", gems[4])
                        .toString();
                System.out.println(jsonString);
                outputStream.writeObject(jsonString);
                outputStream.flush();
                break;
            }



            case 2:{
                while(sum!=3) {
                    sum =0;
                    System.out.println("Weź 3 klejnoty z trzech róźnych stosów");
                    for (int i = 0; i < gems.length; i++) {
                        gems[i] = scn.nextInt();
                        while (gems[i] != 1 && gems[i] != 0) {
                            System.out.println("Bład");
                            gems[i] = scn.nextInt();
                        }
                        sum = sum + gems[i];
                    }
                }
                String jsonString = new JSONObject()
                        .put("request_type", "get_gems")
                        .put("client_id", getStringID())
                        .put("white", gems[0])
                        .put("green", gems[1])
                        .put("blue", gems[2])
                        .put("black", gems[3])
                        .put("red", gems[4])
                        .toString();
                System.out.println(jsonString);
                outputStream.writeObject(jsonString);
                outputStream.flush();
                break;

            }
            default:
                System.out.println("zły input");
                break;
        }


    }

    private void playermove() throws IOException, ClassNotFoundException {
        boolean player = true;
        while(player){
        int move;
        System.out.println("Ruchy");
        Scanner scn = new Scanner(System.in);
        move = scn.nextInt();
            switch (move){
                case 1:{
                    System.out.println("Karta");
                    buycard();
                    break;
                }
                case 2:{
                    System.out.println("Rezerwacja");
                    reservecard();
                    break;
                }
                case 3:{
                    System.out.println("Klejnoty");
                    getgems();
                    break;
                }
                default:
                    System.out.println("zły input");
                    break;
            }
            String input = (String) inputStream.readObject();
            JSONObject jsonObject = new JSONObject(input);
            currentResponse = jsonObject;
            if(jsonObject.getString("result").equals("ok")) {
                player = false;
                endTurn();
            }
            else {
                System.out.println(jsonObject.getString("message"));
            }
        }
        System.out.println();
    }
    public void endTurn() throws IOException, ClassNotFoundException {
        Scanner sc = new Scanner(System.in);
        int[] gems = new int[5];
        int cashSum=ClientBoard.getInstance().getActivePlayer().getCash().getYellow() +
                ClientBoard.getInstance().getActivePlayer().getCash().getBlack() +
                ClientBoard.getInstance().getActivePlayer().getCash().getBlue() +
                ClientBoard.getInstance().getActivePlayer().getCash().getGreen() +
                ClientBoard.getInstance().getActivePlayer().getCash().getRed() +
                ClientBoard.getInstance().getActivePlayer().getCash().getWhite();

        String input = (String) inputStream.readObject();
        JSONObject jsonObject = new JSONObject(input);
        System.out.println(input);
        String answer = jsonObject.getString("answer_type");
        String jsonString="";
        switch(answer) {
            case "discard_gems": {
                System.out.println(jsonObject.getString("message"));
                while(cashSum>10) {
                    for(int i=0; i<gems.length; i++) {
                        gems[i]=sc.nextInt();
                        cashSum-=gems[i];
                    }
                }
                jsonString = new JSONObject()
                        .put("request_type", "discard_gems")
                        .put("client_id", getStringID())
                        .put("white", gems[0])
                        .put("green", gems[1])
                        .put("blue", gems[2])
                        .put("black", gems[3])
                        .put("red", gems[4])
                        .toString();
                break;
            }
            case "select_noble": {
                System.out.println(jsonObject.getString("message"));
                String noble = sc.next();
                jsonString = new JSONObject()
                        .put("request_type", "select_noble")
                        .put("client_id", getStringID())
                        .put("noble_id", noble)
                        .toString();
                break;
            }
        }
        System.out.println(jsonString);
        outputStream.writeObject(jsonString);
        outputStream.flush();
    }
    private void await() throws IOException, ClassNotFoundException, TooMuchCashException, NobleNotSelectedException {
                /*String input ="";
                input = (String) inputStream.readObject();
                //input2 = (String) inputStream.readObject();
                System.out.println(input);
                JSONObject jsonObject = new JSONObject(input);*/
                updateGame();
                System.out.println(ClientBoard.getInstance().getActivePlayer());
                System.out.println("nowa tura");
    }
    public JSONObject takeRequest (JSONObject jsonObject) throws IOException, ClassNotFoundException {
        outputStream.writeObject(jsonObject.toString());
        outputStream.flush();
        String input = (String) inputStream.readObject();
        JSONObject jsonInput = new JSONObject(input);

        return jsonInput;
    }
    public JSONObject getResponse () {
        return currentResponse;
    }
    public JSONObject getCurrentBoard ()  {
        return currentBoard;
    }
    /**
     * Starts client and connects to server
     */
    @Override
     public void run() {
            try {
                InetAddress ip = InetAddress.getByName("localhost");
                Socket clientSocket = new Socket(ip, serverPort);
                System.out.println("Starting Splendor client...\nServer address: " + host + "\nServer port: " + serverPort);
                inputStream = new ObjectInputStream(clientSocket.getInputStream());
                outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                    getserverHello();
                    sayHello();
                    gameStart();
                    while (true) {
                        System.out.println(ClientBoard.getInstance().toString());
                        System.out.println(ClientBoard.getInstance().getActivePlayer());
                        if(ClientBoard.getInstance().getActivePlayer().getNick().equals(nick)) {
                            playermove();
                        }
                        await();


                    }

            } catch (IOException | ClassNotFoundException | InactivePlayersException | TooMuchCashException | NobleNotSelectedException ex) {
                //System.out.println("Dziękujemy za grę ^^");
                ex.printStackTrace();
            }
        }

    public static void main(String[] args){
        try{
            Client client = new Client(defaultPort,host);
            client.run();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

}

