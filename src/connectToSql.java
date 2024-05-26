import java.sql.*;
import java.io.File;
import java.io.IOException;

import com.mysql.cj.protocol.Resultset;
import com.mysql.cj.xdevapi.PreparableStatement;

public class connectToSql {
    private Connection con;
    private Statement statement;
    private String deiver;
    private String usr;
    private String passwrd;
    private String url;
    private PreparedStatement prestatement;
    public connectToSql(){
        deiver = "com.mysql.cj.jdbc.Driver";
        usr = "upupup";
        passwrd = "tianqi985";
        url = "jdbc:mysql://47.120.73.143:3306/owo";
        try{
            Class.forName(deiver);
            //System.out.println("driver loaded successfully");
        }
        catch(ClassNotFoundException e){
            System.out.println("driver loaded unsuccessfully");
            e.printStackTrace();
        }
        try{
            con = DriverManager.getConnection(url, usr, passwrd);
            if(!con.isClosed()){
                //System.out.println("mysql getConnection successfully");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        try{
            con = DriverManager.getConnection(url, usr, passwrd);
            statement= con.createStatement();
            //System.out.println("mysql createStatement successfully");
        }
        catch(Exception e){
            System.out.println("mysql createStatement unsuccessfully");
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(String sqls) throws SQLException{
        return statement.executeQuery(sqls);
    }
    public void executeUpdate(String sqls) throws SQLException{
        statement.executeUpdate(sqls);
    }
    public Connection getCon(){
        return con;
    }
    public void sendmessageTOonlymysql(message messageRecivied) throws SQLException{
        String sql = "insert into message values(?,?,?,?,?)";
        prestatement = con.prepareStatement(sql);
        prestatement.setString(1,messageRecivied.getSender());
        prestatement.setString(2, messageRecivied.getAccepter());
        prestatement.setInt(3,messageRecivied.getType());
        prestatement.setTimestamp(4, messageRecivied.getTime());
        prestatement.setString(5, messageRecivied.getMessage());
        prestatement.executeUpdate();
    }
}
