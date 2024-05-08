import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
public class chatsocket  extends Thread{
    Socket socket;
    String owo_no;
    public chatsocket(Socket socket1,String owo_no1){
        socket = socket1;
        owo_no = owo_no1;
    }
    public void run(){
            try {
                while (true) {
                    getMessage();
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

    }
    public void getMessage() throws InterruptedException{
        try{
            if(socket.getInputStream().available()>0){
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Object receivedobject = ois.readObject();
                message messageGetted = (message)receivedobject;
                if(SeverListening.online_socker_list.containsKey(messageGetted.getAccepter())){
                    chatsocket socket_target = SeverListening.online_socker_list.get(messageGetted.getAccepter());
                    socket_target.sendMessage(1, messageGetted.getSender(), messageGetted.getMessage());
                }
                else{
                    sendMessage(2, "00000", "the target usr is not online");
                }
                //sendMessage(1,messageGetted.getAccepter(),messageGetted.getMessage());
        }
        }
        catch(Exception e){
            e.printStackTrace();
            sleep(4000);
        }
    }
    public void sendMessage(int type,String sender,String message){
        message messageSend = new message(type,sender,owo_no,message);
        try{
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(messageSend);
            System.out.println("message from "+sender+" to "+owo_no+" already send!");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
