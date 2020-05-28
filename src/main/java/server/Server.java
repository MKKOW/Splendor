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
import java.util.ArrayList;
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



    public ArrayList<String> playersnicks = new ArrayList<>();

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
            for(int i = 0;i<playersNumber;i++) {
                Socket clientsocket = socket.accept();
                System.out.println("Starting Splendor server on port: " + serverport);
                outputStream = new ObjectOutputStream(clientsocket.getOutputStream());
                inputStream = new ObjectInputStream(clientsocket.getInputStream());
                Thread thread = new ClientHandler(outputStream,inputStream,clientsocket,playersNumber,this);
                thread.start();
                }
            } catch (IOException e) {
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
