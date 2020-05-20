package server;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    private static final int defaultport = 2000;
    private static String host = "127.0.0.1";
    private String msg = "";
    private int serverport;
    public Client(int serverport ,String host){
        this.serverport = serverport;
        this.host = host;

    }
    public void runclient()  {
        try {
            Socket clientsocket = new Socket(host, serverport);
            int i = 1;
            while(i<10) {
                System.out.println(host + serverport);
                ObjectMapper mapper = new ObjectMapper();
                ObjectInputStream inputStream = new ObjectInputStream(clientsocket.getInputStream());
                String tmp;
                tmp = (String) inputStream.readObject();
                System.out.println(tmp);
                Card karta1;
                karta1 = mapper.readValue(tmp,Card.class);
                karta1.setBlue(i);
                karta1.setGreen(i);
                karta1.setRed(i);
                System.out.println(karta1.toString());
                ObjectOutputStream outputStream = new ObjectOutputStream(clientsocket.getOutputStream());
                outputStream.writeObject(karta1);
                System.out.println(karta1.toString());
                i++;
            }
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }

    }
    public static void main(String args[]){
        try{
            Client client = new Client(defaultport,host);
            client.runclient();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}

