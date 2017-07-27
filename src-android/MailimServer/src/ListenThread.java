
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @date   2017-7-26
 * @author Zhang zhanhong 
 */
public class ListenThread extends Thread{
    public static final int PORT = 8218;
    public ServerSocket serverSocket;
    ListenThread(){}
    public void run(){
        try {
            System.out.println("listenning...");
            listen();
        } catch (IOException ex) {
            Logger.getLogger(ListenThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void listen()throws IOException{
            serverSocket = new ServerSocket(PORT);
            while(!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                AnswerThread answerThread =  new AnswerThread(socket);
                answerThread.run();
            }
    }
    public void close(){
        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(ListenThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
