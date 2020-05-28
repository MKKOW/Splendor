package server;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.UUID;

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
    private String clientVersion = "1.0";
    private int serverPort;
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
        this.host = host;
    }

    /**
     * Receives message from server
     * @return message as String
     */
    public String getServerMessage() {
        String msg=null;
        try {
            msg = (String) inputStream.readObject();
            System.out.println("Server: " + msg);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            return msg;
        }
    }

    /**
     * Sends message to server
     * @param msg String message to send
     */
    public void sendServerMessage(String msg) {
        try {
            outputStream.writeObject(msg);
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Part of protocol, generates hello to server and chooses player's nick
     */
    public void sayHello() {
        JsonFactory factory = new JsonFactory();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            JsonGenerator generator = factory.createGenerator(stream, JsonEncoding.UTF8);
            generator.writeStartObject();
            generator.writeStringField("request_type","client_hello");
            generator.writeStringField("client_id",getStringID());
            generator.writeStringField("set_nick",getNick());
            //generator.writeStringField("client_version",clientVersion);
            generator.writeEndObject();
            generator.close();
            System.out.println(stream.toString());
            sendServerMessage(stream.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Part of protocol, parses hello answer from server and sets given clientID
     * @param json message from server
     */
    public void serverHello(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(json);
            setClientID(jsonNode.get("set_client_id").asText());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    private void setNick() throws IOException {
        System.out.println("Podaj swój nick: ");
        Scanner scn = new Scanner(System.in);
        String tmp = scn.nextLine();
        String input;
        while (!(tmp.length() >= 3 && tmp.length() <= 10)) {
            System.out.println("Nick niedozwolony");
            tmp = scn.nextLine();
        }
            outputStream.writeObject(tmp);
            outputStream.flush();
            input = inputStream.readUTF();
            while(input.contains("niepoprawny")){
                System.out.println("Nick zajęty,podaj nowy:  ");
                tmp = scn.nextLine();
                outputStream.writeObject(tmp);
                outputStream.flush();
                input = inputStream.readUTF();
            }
        System.out.println(tmp);
        this.nick = tmp;
    }

    /**
     * 4 steps connection to server: sending and receiving hello, verifying nick, starting game
     */
    public void connectToServer() {
        serverHello(getServerMessage());
        System.out.println("My UUID: " + getStringID());
        sayHello();
        //while(!verifyNick(getServerMessage())) sayHello();
        //startGame();
    }

    /**
     * Starts client
     */
    public void runClient() {
            String received;
            String tosend;
            Scanner scn;
            try {
            InetAddress ip = InetAddress.getByName("localhost");
                Socket clientSocket = new Socket(ip, serverPort);
                System.out.println("Starting Splendor client...\nServer address: " + host + "\nServer port: " + serverPort);
                inputStream = new ObjectInputStream(clientSocket.getInputStream());
                outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                while (true) {
                    received = (String) inputStream.readObject();
                    System.out.println(received);
                    serverHello(received);
                    System.out.println(clientID);
                    setNick();
                    System.out.println("Twój nick to: " +nick);

                    // for tests: receive, modify and send object (card)
                }
            } catch (IOException | ClassNotFoundException ex) {
                System.out.println("Dziękujemy za grę ^^");

            }

        }


    public static void main(String args[]){
        try{
            Client client = new Client(defaultPort,host);
            client.runClient();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}

