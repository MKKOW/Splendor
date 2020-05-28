package server;

import org.json.JSONObject;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
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
    private final int serverPort;
    /**
     * Client unique UUID
     */
    private UUID clientID;
    /**
     * Input for client socket
     */
    private ObjectInputStream inputStream;
    /**
     * Output for client socket
     */
    private ObjectOutputStream outputStream;
    private ArrayList<String> nicknames;
    // Testowe
    private String nick = "";
    public String getNick () {
        return nick;
    }

    /**
     * Sets clientID from given String
     * @param clientID
     */
    public void setClientID(String clientID) {
        this.clientID = UUID.fromString(clientID);
    }
    public String getStringID() {
        return clientID.toString();
    }
    public Client(int port, String host){
        this.serverPort = port;
        Client.host = host;
    }
    /**
     * Part of protocol, generates hello to server and chooses player's nick
     */
    public void sayHello() throws IOException, ClassNotFoundException {
        System.out.println("Podaj swój nick: ");
        Scanner scn = new Scanner(System.in);
        String tmp = scn.nextLine();
        String input;
        while (!(tmp.length() >= 3 && tmp.length() <= 10)) {
            System.out.println("Nick niedozwolony");
            tmp = scn.nextLine();
        }
        while(nicknames.contains(tmp)){
            System.out.println("Nick zajęty,podaj nowy:  ");
            tmp = scn.nextLine();
        }
        System.out.println(tmp);
        this.nick = tmp;
        System.out.println("Twój nick to: " +nick);
        //System.out.println(clientID);
        String jsonString = new JSONObject()
                .put("request_type","client_hello")
                .put("client_id",getStringID())
                .put("set_nick",getNick())
                .toString();
        outputStream.writeObject(jsonString);
        outputStream.flush();
        input = (String) inputStream.readObject();
        JSONObject jsonObject = new JSONObject(input);
        while(jsonObject.getString("result").equals("invalid"))
        {
            sayHello();
        }
    }
    /**
     * Part of protocol, parses hello answer from server and sets given clientID

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
    public void gameStart() throws IOException, ClassNotFoundException {
        String input = (String) inputStream.readObject();
        JSONObject jsonObject = new JSONObject(input);
        nicknames = new ArrayList<>(Arrays.asList(jsonObject.getString("nicknames").replaceAll("(^\\[|\\]$)", "").split(", ")));
        System.out.println(nicknames.toString());
        System.out.println(jsonObject.getString("Board"));
    }

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
    /**
     * Starts client
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
                    // for tests: receive, modify and send object (card)
                }
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println("Dziękujemy za grę ^^");
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

