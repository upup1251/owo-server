import java.io.Serializable;

public class message implements Serializable{
    private int type;
    //type == 0 :用户连接到服务器时将把自己的owo_no发送给服务器用于创建chatSocket对象
    //1:message
    //2:accepter不在线
    private String sender;
    private String accepter;
    private String message;
    message(){
        type = -1;
        sender = accepter = message = null;
    };
    message(int type1,String sender1,String accepter1,String message1){
        type = type1;
        sender = sender1;
        accepter = accepter1;
        message = message1;
    }
    public int getType(){
        return type;
    }
    public String getSender(){
        return sender;
    }
    public String getAccepter(){
        return accepter;
    }
    public String getMessage(){
        return message;
    }
}