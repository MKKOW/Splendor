package server;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

public class ClientHandler extends  Thread{

        final ObjectOutputStream outputStream;
        final ObjectInputStream inputStream;
        final Socket socket;
        final int playernumber;
       private final Server server;

    public ClientHandler(ObjectOutputStream outputStream, ObjectInputStream inputStream, Socket socket, int playernumber, Server server) {
        this.outputStream = outputStream;
        this.inputStream = inputStream;
        this.socket = socket;
        this.playernumber = playernumber;
        this.server = server;

    }
    public void sendClientMessage(String msg) {
        try {
            outputStream.writeObject(msg);
            outputStream.flush();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    public void sayHello() {
        JsonFactory factory = new JsonFactory();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            JsonGenerator generator = factory.createGenerator(stream, JsonEncoding.UTF8);
            generator.writeStartObject();
            generator.writeStringField("answer_type","server_hello");
            generator.writeStringField("set_client_id", UUID.randomUUID().toString());
            generator.writeNumberField("number_of_players",server.playersnicks.size());
            //generator.writeStringField("server_version",serverVersion);
            generator.writeEndObject();
            generator.close();
            System.out.println(stream.toString());
            sendClientMessage(stream.toString());
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private void play() throws InterruptedException {
        while (true) {

            }
        }
    private void checknick() throws IOException, ClassNotFoundException {
        String nick = (String) inputStream.readObject();
        System.out.println(nick);

        while(server.playersnicks.contains(nick)){
            outputStream.writeUTF("Nick niepoprawny");
            outputStream.flush();
            nick = (String) inputStream.readObject();
        }
        outputStream.writeUTF("Nick poprawny");
        System.out.println("Nick poprawny");
        server.playersnicks.add(nick);
        outputStream.flush();
        System.out.println(server.playersnicks.toString());
    }

    @Override
    public void run() {
        String received;
        String tosend;
        while (true) {


            try {
                sayHello();
                checknick();
                play();
            }
            catch (InterruptedException | IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }


        }

    }
}

