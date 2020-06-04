package server;


import Model.ClientBoard;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;

public class ClientHandler extends  Thread{
    /**
     * Data output
     */
    final ObjectOutputStream outputStream;
    /**
     * Data input
     */
    final ObjectInputStream inputStream;
    /**
     * Client's socket
     */
    final Socket socket;
    /**
     * Number of players in the game
     */
    final int playernumber;
    /**
     * // TODO: change to board
     */
    final ClientBoard board;
    /**
     * HashMap storing player's id with their nicknames
     */
    final HashMap<String,String> hashMap;
    /**
     * Server in the game
     */
    private final Server server;
    //private int turn;

    /**
     * ClientHandler constructor
     * @param outputStream data output
     * @param inputStream data input
     * @param socket client's socket
     * @param playernumber number of players in the game
     * @param server game server
     * @param board
     * @param hashMap map with nicks and ids
     */
    public ClientHandler(ObjectOutputStream outputStream, ObjectInputStream inputStream, Socket socket, int playernumber, Server server, ClientBoard board, HashMap<String,String> hashMap) {
        this.outputStream = outputStream;
        this.inputStream = inputStream;
        this.socket = socket;
        this.playernumber = playernumber;
        this.server = server;
        this.board = board;
        this.hashMap = hashMap;
    }
    /*
    public void sendClientMessage(String msg) {
        try {
            outputStream.writeObject(msg);
            outputStream.flush();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

     */

    /**
     * Sends to client server hello with player's id
     */
    public void sayHello() {
        try {
            String jsonString = new JSONObject()
                    .put("answer_type","server_hello")
                    .put("set_client_id", UUID.randomUUID().toString())
                    .put("number_of_players",server.playersnicks.size())
                    .put("connected_players",playernumber)
                    .put("nicknames",server.playersnicks.toString())
                    .toString();
            outputStream.writeObject(jsonString);
            outputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    /**
     * Verifies player's nickname and sends the result
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void checkClientnick() throws IOException, ClassNotFoundException {
        String input = (String) inputStream.readObject();
        JSONObject jsonObject = new JSONObject(input);
        //System.out.println(jsonObject.toString());
        while(server.playersnicks.contains(jsonObject.getString("set_nick"))){
            String response = new JSONObject()
                    .put("answer_type","nick_verification")
                    .put("result","invalid")
                    .put("number_of_players",server.playersnicks.size())
                    .put("connected_players",playernumber)
                    .put("nicknames",server.playersnicks.toString())
                    .put("message","Twój nick jest zły xd")
                    .toString();
            outputStream.writeObject(response);
            outputStream.flush();
            input = (String) inputStream.readObject();
            jsonObject = new JSONObject(input);
        }
        String response = new JSONObject()
                    .put("answer_type","nick_verification")
                    .put("result","ok")
                    .put("number_of_players",server.playersnicks.size())
                    .put("connected_players",playernumber)
                    .put("nicknames",server.playersnicks.toString())
                    .toString();
        outputStream.writeObject(response);
        outputStream.flush();
        //System.out.println(jsonObject.toString());
        server.playersnicks.add(jsonObject.getString("set_nick"));
        server.hashMap.put(jsonObject.getString("set_nick"),jsonObject.getString("client_id"));
        System.out.println(server.playersnicks.toString());
    }

    /**
     * Starts the game and sends to clients info about the board
     * @throws IOException
     */
    private void gameStart() throws IOException {

        ObjectMapper mapper=new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonBoard =mapper.writeValueAsString(board);
        String response = new JSONObject()
                    .put("answer_type","game_start")
                    .put("nicknames",server.playersnicks.toString())
                    .put("Board",jsonBoard)
                    .toString();


        outputStream.writeObject(response);
        outputStream.flush();
    }
    private void increment () {
        Server.turn = (Server.turn + 1)%playernumber;
    }

    private void play () {
        currentThread().setName(server.playersnicks.get(Server.turn));
        while(currentThread().getName().equals(String.valueOf(Server.turn))) {


        }
    }
    @Override
    public void run() {
        while (true) {
            try {
                sayHello();
                checkClientnick();
                synchronized (server){
                    if(server.playersnicks.size()==playernumber){
                        server.notifyAll();
                    }
                    else{
                        server.wait();
                    }
                }
                gameStart();
                //verifymove();


                Thread.sleep(100000000);
            }
            catch (InterruptedException | IOException | ClassNotFoundException e) {
                e.printStackTrace();

            }


        }

    }
}

