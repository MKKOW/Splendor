package server;


import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

public class Server {
    /**
     * Default server port
     */
    private static final int defaultport = 2000;
    private int serverport;
    private String serverVersion = "1.0";
    /**
     * Data input
     */
    private ObjectInputStream inputStream;
    /**
     * Data output
     */
    private ObjectOutputStream outputStream;
    /**
     * Number of players
     */
    private int playersNumber;

    public Server(int serverport,int playersNumber){
        this.serverport = serverport;
        this.playersNumber = playersNumber;
    }

    /**
     * Receives message from client
     * @return message as String
     */
    public String getClientMessage() {
        String msg=null;
        try {
            msg = (String) inputStream.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            return msg;
        }
    }

    /**
     * Sends message to client
     * @param msg message to send
     */
    public void sendClientMessage(String msg) {
        try {
            outputStream.writeObject(msg);
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Part of protocol, sends hello to client and creates its clientID
     */
    public void sayHello() {
        JsonFactory factory = new JsonFactory();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            JsonGenerator generator = factory.createGenerator(stream, JsonEncoding.UTF8);
            generator.writeStartObject();
            generator.writeStringField("answer_type","server_hello");
            generator.writeStringField("set_client_id",UUID.randomUUID().toString());
            generator.writeNumberField("number_of_players",playersNumber);
            generator.writeStringField("server_version",serverVersion);
            generator.writeEndObject();
            generator.close();
            System.out.println(stream.toString());
            sendClientMessage(stream.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void clientHello(){
        // tymczasowe
        System.out.println("Client: " + getClientMessage());
    }

    /**
     * Starts server
     */
    public  void runserver() {
        try {
            ServerSocket socket = new ServerSocket(serverport);
            Socket clientsocket = socket.accept();
            System.out.println("Starting Splendor server on port: " + serverport);
            outputStream = new ObjectOutputStream(clientsocket.getOutputStream());
            inputStream = new ObjectInputStream(clientsocket.getInputStream());
            sayHello();
            clientHello();

            // for tests: create, send and receive object (card)
            Card karta1 = new Card(1, 1, 1, "test");
            System.out.println("Wysylam karte: " + karta1);
            outputStream.writeObject(karta1);
            karta1 = (Card) inputStream.readObject();
            System.out.println("Odeslana karta: " + karta1);

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    public static void main(String[] args)  {
        try{
            Server server = new Server(defaultport,3);
            server.runserver();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
