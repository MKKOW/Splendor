package server;


import Exceptions.*;
import Model.*;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.UUID;


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
     * Server board
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
    public String winner;
    public boolean gameOver = false;


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
            System.out.println(jsonString);

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
        //ServerBoard.getInstance().addPlayer(jsonObject.getString("set_nick")); TODO: Tak można było dodać playera :(
        System.out.println(server.playersnicks.toString());
        System.out.println(input);
        System.out.println(response);
    }

    /**
     * Starts the game and sends to clients info about the board
     * @throws IOException
     */
    private void gameStart() throws IOException, InactivePlayersException, TooMuchCashException, NobleNotSelectedException {

        ObjectMapper mapper=new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        server.serverBoard.setPlayers(server.playerHashMap);
        server.serverBoard.setActivePlayer(server.playersnicks.get(0));
        ClientBoard.setInstanceFromServerBoard(server.serverBoard);
        String jsonBoard = mapper.writeValueAsString(ClientBoard.getInstance());
        String response = new JSONObject()
                    .put("answer_type","game_start")
                    .put("nicknames",server.playersnicks.toString())
                    .put("Board",jsonBoard)
                    .toString();

        outputStream.writeObject(response);
        outputStream.flush();
        System.out.println(response);
    }

/*
    private void play () {
        currentThread().setName(server.playersnicks.get(Server.turn));
        while(currentThread().getName().equals(String.valueOf(Server.turn))) {

        }
    }
 */
    private void sendResponse(String answerType, String result, String message, int[] nobles) throws IOException {
        String response="";
        switch (answerType) {
            case "move_verification": {
                if(message==null) {
                    response = new JSONObject()
                            .put("answer_type", answerType)
                            .put("result", result)
                            .toString();
                }
                else {
                    response = new JSONObject()
                            .put("answer_type", answerType)
                            .put("result", result)
                            .put("message", message)
                            .toString();
                }
                break;
            }
            case "end_of_round" : {
                if(result.equals("select_noble")) {
                    response = new JSONObject()
                            .put("answer_type", answerType)
                            .put("result", result)
                            .put("nobles", nobles)
                            .toString();
                }
                else {
                    response = new JSONObject()
                            .put("answer_type", answerType)
                            .put("result", result)
                            .toString();
                }
                break;
            }

        }
        outputStream.writeObject(response);
        outputStream.flush();
        System.out.println(response);
    }

    private boolean verifyMove(JSONObject jsonObject) throws IOException{
        boolean ok = true;
        String request = jsonObject.getString("request_type");
        switch (request) {
            case "get_gems": {
                try {
                    ServerBoard.getInstance().giveCash(jsonObject.getInt("white"),
                            jsonObject.getInt("green"),
                            jsonObject.getInt("blue"),
                            jsonObject.getInt("black"),
                            jsonObject.getInt("red"));
                    ServerBoard.getInstance().getActivePlayer().incrementNumberOfMoves();
                    sendResponse("move_verification","ok", null, null);
                } catch (NotEnoughCashException | IllegalCashAmountException e) {
                    ok = false;
                    sendResponse("move_verification", "illegal", e.getMessage(), null);
                }
                break;
            }
            case "claim_card": {
                try {
                    ServerBoard.getInstance().claimCard(jsonObject.getInt("card_id"));
                    ServerBoard.getInstance().getActivePlayer().incrementNumberOfMoves();
                    sendResponse("move_verification", "ok", null, null);
                } catch (IllegalArgumentException | CardNotOnBoardException | TooManyClaimsException e) {
                    ok = false;
                    sendResponse("move_verification", "illegal", e.getMessage(), null);
                }
                break;
            }
            case "buy_card": {
                try {
                    String place = jsonObject.getString("place");
                    if(place.equals("hand"))
                        ServerBoard.getInstance().buyClaimedCard();
                    else
                        ServerBoard.getInstance().buyCard(jsonObject.getInt("card_id"));
                    ServerBoard.getInstance().getActivePlayer().incrementNumberOfMoves();
                    sendResponse("move_verification", "ok", null, null);
                } catch (NotEnoughCashException | NothingClaimedException | CardNotOnBoardException | IllegalArgumentException e) {
                    ok = false;
                    sendResponse("move_verification", "illegal", e.getMessage(), null);
                }
                break;
            }
            case "discard_gems": {
                try {
                    ServerBoard.getInstance().returnCash(
                            jsonObject.getInt("white"),
                            jsonObject.getInt("green"),
                            jsonObject.getInt("blue"),
                            jsonObject.getInt("black"),
                            jsonObject.getInt("red"));
                    if(ServerBoard.getInstance().getActivePlayer().isOverCashLimit())
                        sendResponse("end_of_round", "discard_gems", null, null);
                    else
                        sendResponse("end_of_round", "ok", null, null);
                } catch (NotEnoughCashException e) {
                    ok = false;
                    sendResponse("end_of_round", "discard_gems", null, null);
                }
                break;
            }
            case "select_noble": {
                try {
                    ServerBoard.getInstance().giveNoble(jsonObject.getInt("noble_id"));
                    sendResponse("end_of_round", "ok", null, null);
                } catch (NotEnoughDiscountException | CardNotOnBoardException e) {
                    ok = false;
                    sendResponse("end_of_round", "select_noble", null, ServerBoard.getInstance().getAvailableNoblesIDs());
                }
                break;
            }
        }
        System.out.println(request);
        return ok;
    }

    private void endOfTurn() throws IOException, ClassNotFoundException {
        String answer = "end_of_round";

        while (ServerBoard.getInstance().getActivePlayer().isOverCashLimit()) {
            sendResponse(answer, "discard_gems", null, null);
            verifyEndOfTurn();
        }

        while (ServerBoard.getInstance().getAvailableNoblesIDs().length != 0) {
            sendResponse(answer, "select_noble", null, ServerBoard.getInstance().getAvailableNoblesIDs());
            verifyEndOfTurn();
        }
        sendResponse(answer, "ok", null, null);
    }
    private void verifyEndOfTurn() throws IOException, ClassNotFoundException {
        String input = (String) inputStream.readObject();
        JSONObject jsonObject = new JSONObject(input);
        verifyMove(jsonObject);
    }
    private void turn() throws IOException, ClassNotFoundException {
        JSONObject jsonObject;
        do {
            String input = (String) inputStream.readObject();
            jsonObject = new JSONObject(input);
            System.out.println(input);
        } while(!verifyMove(jsonObject));
    }
    private synchronized void updategame() throws IOException, InterruptedException {
        ObjectMapper mapper=new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonBoard = mapper.writeValueAsString(ClientBoard.getInstance());

        String response = new JSONObject()
                .put("answer_type", "game_board")
                .put("next_player", server.playersnicks.get((server.turn + 1) % server.playersNumber))
                .put("player", ServerBoard.getInstance().getActivePlayer().getNick())
                .put("Board",jsonBoard)
                .toString();
        System.out.println(jsonBoard);
        server.blockingQueue.clear();
        for(int i =0;i<server.playersNumber;i++) {
            server.blockingQueue.put(response);
        }

    }

    private synchronized void sendgame() throws InterruptedException, IOException {
        String response = server.blockingQueue.take();
        outputStream.writeObject(response);
        outputStream.flush();
        System.out.println(response);
    }

    private synchronized void increment() throws IOException, InterruptedException, TooMuchCashException, NobleNotSelectedException {
        server.turn = (server.turn + 1)%server.playersNumber;
        System.out.println(server.turn);
        ServerBoard.getInstance().setActivePlayer(server.playersnicks.get(server.turn));
        System.out.println(ServerBoard.getInstance().getActivePlayer());
        updategame();
    }
    public void checkGameOver() {
        winner = ServerBoard.getInstance().checkWin();
        gameOver = winner != null;
    }
    public void gameOver() throws IOException {
        String response = new JSONObject()
                .put("answer_type", "game_over")
                .put("winner", winner)
                .toString();
        outputStream.writeObject(response);
        outputStream.flush();
        System.out.println(response);
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
                while (!gameOver){
                       // synchronized (server) {
                            if (server.serverBoard.getActivePlayer().getNick().equals(player.getNick())) {
                                turn();
                                endOfTurn();
                                //verifyEndOfTurn();
                                checkGameOver();
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
                gameOver();
            }
            catch (InterruptedException | IOException | ClassNotFoundException | InactivePlayersException e) {
                e.printStackTrace();
                break;

            } catch (TooMuchCashException | NobleNotSelectedException e) {
                e.printStackTrace();


            }

        }
    }
}


