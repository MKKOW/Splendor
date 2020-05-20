package server;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int defaultport = 2000;
    private int serverport;
    public Server(int serverport){
        this.serverport = serverport;
    }
    public  void runserver() {
        try{
            ServerSocket socket = new ServerSocket(serverport);
            Socket clientsocket = socket.accept();
            Card karta1 = new Card(1, 1, 1, "test");
            while(true) {
                System.out.println(serverport);
                ObjectMapper mapper = new ObjectMapper();
                String jsonString = mapper.writeValueAsString(karta1);
                //System.out.println(jsonString);
                ObjectOutputStream outputStream = new ObjectOutputStream(clientsocket.getOutputStream());
                outputStream.writeObject(jsonString);
                ObjectInputStream inputStream = new ObjectInputStream(clientsocket.getInputStream());
                karta1 = (Card) inputStream.readObject();
                System.out.println(karta1.toString());
                System.out.println("send");
            }
        }
        catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }


    }
    public static void main(String[]args)  {
        try{
            Server server = new Server(defaultport);
            server.runserver();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
