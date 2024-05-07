import java.beans.Expression;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
public class main {
   public static void main(String[] args) throws Exception {
      new SeverListening().start();
}
}
