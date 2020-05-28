package server;


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
    private ServerSocket socket ;
    private Socket clientsocket;
    private Thread thread;
    HashMap<String,String> hashMap = new HashMap<>();
    /**
     * Number of players
     */
    private final int playersNumber;
    ArrayList<String> playersnicks = new ArrayList<>();
    public Model.Board board;
    public Card card;
    public Server(int serverport,int playersNumber){
        this.serverport = serverport;
        this.playersNumber = playersNumber;
    }
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
     * Starts server
     */
    public  void runserver() throws IOException {
        card = new Card(1,1,1,"test");
        try {
            socket = new ServerSocket(serverport);
            System.out.println("Starting Splendor server on port: " + serverport);
            for(int i = 0;i<playersNumber;i++) {
                clientsocket = socket.accept();
                outputStream = new ObjectOutputStream(clientsocket.getOutputStream());
                inputStream = new ObjectInputStream(clientsocket.getInputStream());
                thread = new ClientHandler(outputStream,inputStream,clientsocket,playersNumber,this,card,hashMap);
                System.out.println(thread.getName());
                thread.start();
                }

            } catch (IOException e) {
                e.printStackTrace();
                closeConnection();
            }
        }
    public static void main(String[] args)  {
        try{
            Server server = new Server(defaultport,1);
            server.runserver();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
