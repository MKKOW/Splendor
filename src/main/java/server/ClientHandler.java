package server;


import Exceptions.InactivePlayersException;
import Model.ClientBoard;
import Model.Player;
import Model.ServerBoard;
import client.player;
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
import java.util.concurrent.BlockingQueue;

import static java.lang.Thread.sleep;

public class ClientHandler implements Runnable {
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
    /**
     * // TODO: change to board
     */
    private Player player;

    /**
     * HashMap storing player's id with their nicknames
     */
    /**
     * Server in the game
     */
    final Server server;
    //private int turn;
    private ClientBoard  clientBoard;



    /**
     * ClientHandler constructor
     * @param outputStream data output
     * @param inputStream data input
     * @param socket client's socket
     * @param server game server
     */
    public ClientHandler(ObjectOutputStream outputStream, ObjectInputStream inputStream, Socket socket, Server server) {
        this.outputStream = outputStream;
        this.inputStream = inputStream;
        this.socket = socket;
        this.server = server;

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
                    .put("connected_players",server.playersnicks)
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
                    .put("connected_players",server.playersNumber)
                    .put("nicknames",server.playersnicks.toString())
                    .put("message","Twój nick jest zły")
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
                    .put("connected_players",server.playersNumber)
                    .put("nicknames",server.playersnicks.toString())
                    .toString();
        outputStream.writeObject(response);
        outputStream.flush();
        //System.out.println(jsonObject.toString());
        player = new Player(false,jsonObject.getString("set_nick"));
        server.playersnicks.add(jsonObject.getString("set_nick"));
        server.playerHashMap.put(jsonObject.getString("set_nick"),player);
        server.hashMap.put(jsonObject.getString("set_nick"),jsonObject.getString("client_id"));
        System.out.println(server.playersnicks.toString());
    }

    /**
     * Starts the game and sends to clients info about the board
     * @throws IOException
     */
    private void gameStart() throws IOException, InactivePlayersException {

        ObjectMapper mapper=new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        server.board.setPlayers(server.playerHashMap);
        server.board.setActivePlayer(server.playersnicks.get(0));
        ClientBoard.setInstanceFromServerBoard(server.board);
        String jsonBoard = mapper.writeValueAsString(ClientBoard.getInstance());
        String response = new JSONObject()
                    .put("answer_type","game_start")
                    .put("nicknames",server.playersnicks.toString())
                    .put("Board",jsonBoard)
                    .toString();


        outputStream.writeObject(response);
        outputStream.flush();
    }

/*
    private void play () {
        currentThread().setName(server.playersnicks.get(Server.turn));
        while(currentThread().getName().equals(String.valueOf(Server.turn))) {


        }



    }
 */
    private void turn() throws IOException, ClassNotFoundException, InterruptedException {
        String input = (String) inputStream.readObject();
        JSONObject jsonObject = new JSONObject(input);
        String response;
        String request = jsonObject.getString("request_type");
        switch (request) {
            case "get_gems":
                 response = new JSONObject()
                        .put("answer_type","move_verification")
                        .put("result","ok")
                        .toString();
                outputStream.writeObject(response);
                outputStream.flush();
            case "claim_card":
                 response = new JSONObject()
                        .put("answer_type","move_verification")
                        .put("result","ok")
                        .toString();
                outputStream.writeObject(response);
                outputStream.flush();
            case "buy_card":
                response = new JSONObject()
                        .put("answer_type","move_verification")
                        .put("result","ok")
                        .toString();
                outputStream.writeObject(response);
                outputStream.flush();

        }


    }
    private synchronized void updategame() throws IOException, ClassNotFoundException, InterruptedException {
        String response = new JSONObject()
                .put("answer_type", "game_board")
                .put("next_player", server.playersnicks.get((server.turn + 1) % server.playersNumber))
                .put("player", ServerBoard.getInstance().getActivePlayer().getNick())
                .toString();
        server.blockingQueue.clear();
        for(int i =0;i<server.playersNumber;i++) {
            server.blockingQueue.put(response);
        }

    }
    private synchronized void sendgame() throws InterruptedException, IOException {

        String response = server.blockingQueue.take();
        outputStream.writeObject(response);
        outputStream.flush();


    }
    private synchronized void increment() throws IOException, ClassNotFoundException, InterruptedException {
        server.turn = (server.turn + 1)%server.playersNumber;
        System.out.println(server.turn);
        ServerBoard.getInstance().setActivePlayer(server.playersnicks.get(server.turn));
        System.out.println(ServerBoard.getInstance().getActivePlayer());
        updategame();
    }
    @Override
    public void run() {
        while (true) {
            try {
                sayHello();
                checkClientnick();
                synchronized (server){
                    if(server.playersnicks.size()==server.playersNumber){
                        server.notifyAll();
                    }
                    else{
                        server.wait();
                    }

                }
                gameStart();
                while (true){
                       // synchronized (server) {
                            if (server.board.getActivePlayer().getNick().equals(player.getNick())) {
                                turn();
                                increment();
                                //updategame();
                               // server.notifyAll();
                            //}
                           // else {
                              //  server.wait();
                           // }

                        }
                        System.out.println("sending data");
                        sendgame();
                }
            }
            catch (InterruptedException | IOException | ClassNotFoundException | InactivePlayersException e) {
                e.printStackTrace();
                break;

            }


        }

    }
}

