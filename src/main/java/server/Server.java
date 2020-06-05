package server;


import Exceptions.AmbiguousNickException;
import Exceptions.InactivePlayersException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server {
    /**
     * Default server port
     */
    private static final int defaultport = 2000;
    /**
     * Chosen server port to run
     */
    private final int serverport;
    private final String serverVersion = "1.0";
    /**
     * Data input
     */
    private ObjectInputStream inputStream;
    /**
     * Data output
     */
    private ObjectOutputStream outputStream;
    /**
     * Server's socket
     */
    private ServerSocket socket ;
    /**
     * Client's socket
     */
    private Socket clientsocket;
    private Thread thread;
    /**
     * HashMap storing player's id with their nicknames
     */
    HashMap<String,String> hashMap = new HashMap<>();
    /**
     * Number of players
     */
    private final int playersNumber;
    /**
     * List of players' nicknames
     */
    ArrayList<String> playersnicks = new ArrayList<>();
    /**
     * Instance of game's board
     */
    public Model.ServerBoard board;
    public static int turn=0;

    /**
     * Server constructor
     * @param serverport server port to run
     * @param playersNumber number of players in the game
     */
    public Server(int serverport,int playersNumber){
        this.serverport = serverport;
        this.playersNumber = playersNumber;
    }

    /**
     * Closes clients-server connection
     * @throws IOException
     */
    public void closeConnection() throws IOException {
        try {
            outputStream.close();
        } finally {
            inputStream.close();
            socket.close();
            clientsocket.close();
        }
    }
    /**
     * Starts server and clients' threads
     */
    public  void runserver() throws IOException {
        try {
            socket = new ServerSocket(serverport);
            System.out.println("Starting Splendor server on port: " + serverport);
            board = Controller.BoardMaker.generateRandomServerBoard(playersNumber);
            for(int i = 0;i<playersNumber;i++) {
                clientsocket = socket.accept();
                outputStream = new ObjectOutputStream(clientsocket.getOutputStream());
                inputStream = new ObjectInputStream(clientsocket.getInputStream());
                thread = new ClientHandler(outputStream,inputStream,clientsocket,playersNumber,this,board,hashMap);
                System.out.println(thread.getName());
                thread.setName(String.valueOf(i));
                thread.start();
                }

            } catch (IOException | AmbiguousNickException e) {
                e.printStackTrace();
                closeConnection();
            }
        }
    public static void main(String[] args)  {
        try{
            Server server = new Server(defaultport,2);
            server.runserver();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
