package server;

import Exceptions.InactivePlayersException;
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

public class Client {
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

        // Verification from server
        while (jsonObject.getString("result").equals("invalid")) {
            System.out.println(jsonObject.getString("message"));
            chooseNick();
            jsonObject = sendAndGetNick(input,jsonObject);
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
    public void gameStart() throws IOException, ClassNotFoundException, InactivePlayersException {

        String input = (String) inputStream.readObject();
        JSONObject jsonObject = new JSONObject(input);
        nicknames = new ArrayList<>(Arrays.asList(jsonObject.getString("nicknames").replaceAll("(^\\[|\\]$)", "").split(", ")));
        System.out.println(nicknames.toString());
       // System.out.println(jsonObject.getString("Board"));
        Model.ServerBoard serverBoard = new ServerBoard();
        String boardString = jsonObject.getString("Board");
        System.out.println(boardString);

        JSONObject boardJSON = new JSONObject(boardString);
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
        }

        serverBoard.setNobles(new NoblesOnBoard(noblesArr));
        HashMap<String,Player> players = new HashMap<>();
        for(int i=0;i<nicknames.size();i++){
            Player player;
            if(i==0){
                 player = new Player(true,nicknames.get(i));
            }
            else {
                 player = new Player(false,nicknames.get(i));
            }
            players.put(player.getNick(),player);


        }
        serverBoard.setPlayers(players);
        serverBoard.setActivePlayer(nicknames.get(0));
        System.out.println(serverBoard.toString());

        ClientBoard.setInstanceFromServerBoard(serverBoard);

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
        scn.close();
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
        scn.close();
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
                while(sum!=4) {
                    sum = 0;
                    System.out.println("asfjsidf");
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
                        .put("request_type", "claim_card")
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
                    System.out.println("asfjsidf");
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
                        .put("request_type", "claim_card")
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
                System.out.println("cos");
                break;
        }
        scn.close();

    }

    private void playermove() throws IOException {
        boolean player = true;
        while(player){
        int move;
        System.out.println("Ruchy");
        Scanner scn = new Scanner(System.in);
        move = scn.nextInt();
            switch (move){
                case 1:{
                    buycard();
                    break;
                }
                case 2:{
                    reservecard();
                    break;
                }
                case 3:{
                    getgems();
                    break;
                }
                default:
                    System.out.println("cos");
                    break;
            }
            player = false;
        }

    }
    private void await() throws InterruptedException {

        System.out.println("koncze ruch");

    }
    /**
     * Starts client and connects to server
     */
    public void runClient() {
            try {
                InetAddress ip = InetAddress.getByName("localhost");
                Socket clientSocket = new Socket(ip, serverPort);
                System.out.println("Starting Splendor client...\nServer address: " + host + "\nServer port: " + serverPort);
                inputStream = new ObjectInputStream(clientSocket.getInputStream());
                outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                while (true) {
                    getserverHello();
                    sayHello();
                    gameStart();
                    System.out.println(ClientBoard.getInstance().toString());
                    System.out.println(board.getActivePlayer());

                    playermove();
                    await();
                    // for tests: receive, modify and send object (card)
                }
            } catch (IOException | ClassNotFoundException | InterruptedException | InactivePlayersException ex) {
                //System.out.println("Dziękujemy za grę ^^");
                ex.printStackTrace();
            }
        }

    public static void main(String[] args){
        try{
            Client client = new Client(defaultPort,host);
            client.runClient();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}

