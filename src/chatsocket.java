import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
public class chatsocket  extends Thread{
    Socket socket;
    String owo_no;
    boolean running;
    long LastHeartBeat;
    public chatsocket(Socket socket1,String owo_no1){
        socket = socket1;
        owo_no = owo_no1;
    }
    public void run(){
        running = true;
            try {
                LastHeartBeat = System.currentTimeMillis();
                while (running) {
                    getMessage();
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

    }
    public void getMessage() throws InterruptedException{
        try{
            if(System.currentTimeMillis()-LastHeartBeat>5000){
                SeverListening.online_socker_list.remove(owo_no);
                System.out.println("user "+owo_no+" offline,current online user is "+SeverListening.online_socker_list.size());
                stopRunning();
            }
            if(socket.getInputStream().available()>0){
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Object receivedobject = ois.readObject();
                message messageGetted = (message)receivedobject;
                messageDD(messageGetted);
        }
        }
        catch(Exception e){
            e.printStackTrace();
            sleep(4000);
        }
    }
    public void danBo(message messageGetted){
            if(SeverListening.online_socker_list.containsKey(messageGetted.getAccepter())){
                chatsocket socket_target = SeverListening.online_socker_list.get(messageGetted.getAccepter());
                socket_target.sendMessage(1, messageGetted.getSender(), messageGetted.getMessage());
                System.out.println("send message from "+messageGetted.getSender()+"to online user "+messageGetted.getAccepter());
            }
            else{
                sendMessage(2, "00000", "the target usr is not online");
                try {
                    SeverListening.sqls.sendmessageTOonlymysql(messageGetted);
                    System.out.println("send message from "+messageGetted.getSender()+" to offline user "+messageGetted.getAccepter()+" by sql ");
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            //sendMessage(1,messageGetted.getAccepter(),messageGetted.getMessage());
    }
    public void guanBo(message messageGetted){
        String sql = "select usr1 from friend where usr2=?";
        try {
        PreparedStatement prestatement = main.sqls.getCon().prepareStatement(sql);
        prestatement.setString(1, messageGetted.getSender());
        ResultSet rs = prestatement.executeQuery();
        while (rs.next()) {
            String usr1_owo = rs.getString("usr1");
            message messageToall = new message(messageGetted.getType(), messageGetted.getSender(), usr1_owo, messageGetted.getMessage());
            danBo(messageToall);
        }
        sql = "select usr2 from friend where usr1=?";
        prestatement = main.sqls.getCon().prepareStatement(sql);
        prestatement.setString(1, messageGetted.getSender());
        rs = prestatement.executeQuery();
        while (rs.next()) {
            String usr2_owo = rs.getString("usr2");
            message messageToall = new message(messageGetted.getType(), messageGetted.getSender(), usr2_owo, messageGetted.getMessage());
            danBo(messageToall);
        }
    }
    catch(Exception e){
        e.printStackTrace();
    }
}

    public void messageDD(message messageGetted) throws IOException{
        if(messageGetted.getType()==1){
            danBo(messageGetted);
        }
        else if (messageGetted.getType()==5){
            guanBo(messageGetted);
        }
        else if(messageGetted.getType()==3){
            LastHeartBeat = System.currentTimeMillis();
            sendMessage(4, "00000", "recivied heartBeat");
        }
        else if(messageGetted.getType()==6){
            String path = "./usersavatar/"+messageGetted.getSender()+".png";
            File outputfile = new File(path);
            if(!outputfile.exists()){
                outputfile.createNewFile();
            }
            byte[] imagesBytes = Base64.getDecoder().decode(messageGetted.getMessage());
            FileOutputStream fos = new FileOutputStream(path);
            ByteArrayInputStream bais = new ByteArrayInputStream(imagesBytes);
            
            byte[] buffer = new byte[999];
            int byteRead;
            while ((byteRead=bais.read(buffer))!=-1) {
                fos.write(buffer, 0, byteRead);
            }
            System.out.println("avatar already get from "+owo_no);

        }
        else if(messageGetted.getType()==9){
            String path = "./usersavatar/"+messageGetted.getMessage()+".png";
            File file = new File(path);
            if(file.exists()){
                sendAvatar(path,messageGetted.getMessage());
            }
            System.out.println("avatar for "+messageGetted.getMessage()+" is not exists");
        }

    }


    
    
    public void sendMessage(int type,String sender,String message){
        message messageSend = new message(type,sender,owo_no,message);
        try{
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(messageSend);
            if(type == 4){
                System.out.println("heartbeart callback already sended to "+owo_no);
            }
            if(type ==1 ){
                System.out.println("message from "+sender+" to "+owo_no+" already send!");

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void stopRunning(){
        running = false;
    }
    public void sendAvatar(String path,String belonger) throws IOException{
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        byte[] buffer = new byte[999];
        int byteRead;
        while ((byteRead=fis.read(buffer))!=-1) {
            baos.write(buffer, 0, byteRead);
        }
        byte[] imageBytes = baos.toByteArray();
        String base64image = Base64.getEncoder().encodeToString(imageBytes);
        sendMessage(6,belonger, base64image);
        System.out.println("avatar send successfully.");
    }
}
