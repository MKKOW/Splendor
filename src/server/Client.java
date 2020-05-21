package server;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.Socket;
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
    private String nick = "Robbie";
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
            generator.writeStringField("client_version",clientVersion);
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
        /*
        JsonFactory factory = new JsonFactory();
        JsonParser jParser;
        try {
            jParser = factory.createParser(json);
            while (jParser.nextToken() != JsonToken.END_OBJECT) {
                String fieldname = jParser.getCurrentName();
                if ("set_client_id".equals(fieldname)) {
                    jParser.nextToken();
                    setClientID(jParser.getText());
                }

                if ("age".equals(fieldname)) {
                    jParser.nextToken();
                    parsedAge = jParser.getIntValue();
                }

                if ("address".equals(fieldname)) {
                    jParser.nextToken();
                    while (jParser.nextToken() != JsonToken.END_ARRAY) {
                        addresses.add(jParser.getText());
                    }
                }
            }
            jParser.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
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
    public void runClient()  {
        try {
            Socket clientSocket = new Socket(host, serverPort);
            System.out.println("Starting Splendor client...\nServer address: " + host + "\nServer port: " + serverPort);
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            inputStream = new ObjectInputStream(clientSocket.getInputStream());
            connectToServer();
            // for tests: receive, modify and send object (card)
            Card card1 = (Card) inputStream.readObject();
            System.out.println("Moja karta: " + card1);
            card1.setBlue(0);
            card1.setGreen(0);
            card1.setRed(0);
            card1.setName("test2");
            System.out.println("Odsylam karte: " + card1);
            // close the streams and socket
            outputStream.writeObject(card1);
            outputStream.close();
            inputStream.close();
            clientSocket.close();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
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

