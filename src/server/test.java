package server;

import java.net.*;
import java.io.*;
class testobject implements Serializable {
    int value ;
    String id;
    public  testobject(int v, String s ){
        this.value=v;
        this.id=s;
    }
}
 class SimpleServer  {
    public static void main(String args[]) {
        int port = 2002;
        try {
            ServerSocket ss = new ServerSocket(port);
            Socket s = ss.accept();
            InputStream is = s.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(is);
            testobject to = (testobject)ois.readObject();
            if (to!=null){System.out.println(to.id);}
            System.out.println((String)ois.readObject());
            is.close();
            s.close();
            ss.close();
        }catch(Exception e){System.out.println(e);}
    }
}
class SimpleClient {
    public static void main(String args[]){
        try{
            Socket s = new Socket("localhost",2002);
            OutputStream os = s.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            testobject to = new testobject(1,"object from client");
            oos.writeObject(to);
            oos.writeObject(new String("another object from the client"));
            oos.close();
            os.close();
            s.close();
        }catch(Exception e){System.out.println(e);}
    }
}