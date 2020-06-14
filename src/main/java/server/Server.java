package server;


import Model.ClientBoard;
import Model.Player;
import Model.ServerBoard;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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
    //private Thread thread;
    /**
     * HashMap storing player's id with their nicknames
     */
    HashMap<String,String> hashMap = new HashMap<>();
    /**
     * Number of players
     */
    public final int playersNumber;
    /**
     * List of players' nicknames
     */
    ArrayList<String> playersnicks = new ArrayList<>();
    /**
     * Instance of game's board
     */
    public Model.ServerBoard serverBoard;
    public Model.ClientBoard clientBoard;
    public int turn=0;
    HashMap<String, Player>  playerHashMap = new HashMap<>();
    final BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>();
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
            serverBoard = Controller.BoardMaker.generateRandomServerBoard(playersNumber);

            //ClientBoard.setInstanceFromServerBoard(serverBoard);
            for(int i = 0;i<playersNumber;i++) {
                clientsocket = socket.accept();
                outputStream = new ObjectOutputStream(clientsocket.getOutputStream());
                inputStream = new ObjectInputStream(clientsocket.getInputStream());
                Runnable clientHandler = new ClientHandler(outputStream,inputStream,clientsocket,this);
                new Thread(clientHandler).start();
                }

            } catch (IOException e) {
                e.printStackTrace();
                closeConnection();
            }
        }
    public static void main(String[] args)  {
        try{
            Server server = new Server(defaultport,Integer.parseInt(args[1]));
            server.runserver();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
