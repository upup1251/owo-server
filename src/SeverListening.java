import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SeverListening  extends Thread{
    private ServerSocket serverSocket;
    private int DuanKouHao = 40801;
    public static Map<String,chatsocket> online_socker_list = new HashMap<>();
    public static connectToSql sqls;
    SeverListening(){}
    public void run() {
        try{
            sqls = new connectToSql();
        serverSocket = new ServerSocket(DuanKouHao);
        }
        catch(Exception e){
            e.printStackTrace();
        }
       System.out.println("wait for connection...");
       while(true){
        newConnection();
       }
    }
    public void newConnection(){
       try{
           Socket socket = serverSocket.accept();
           //System.out.println("connection successfully");
           //接受用户发送的owo_no
            try{
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Object receivedobject = ois.readObject();
                message messageGetted = (message)receivedobject;
                chatsocket chatsocket = new chatsocket(socket,messageGetted.getMessage());
                online_socker_list.put(messageGetted.getSender(),chatsocket);
               System.out.println("getConnection from "+socket.getInetAddress()+" with owo_no:"+messageGetted.getMessage()+" current online user is "+online_socker_list.size());

                chatsocket.start();
            }
            catch(Exception e){
                e.printStackTrace();
            }
           //InputStream is = socket.getInputStream();
           //ByteArrayOutputStream baos = new ByteArrayOutputStream();
           //byte[] buffer = new byte[1024];
           //int len;
           //while ((len = is.read(buffer))!=-1) {
               //baos.write(buffer,0,len);
           //}
           //System.out.println(baos);
       }
       catch(Exception e){
        e.printStackTrace();
       }
    }
}

