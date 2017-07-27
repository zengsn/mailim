
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;

/**
 * @date   2017-7-26
 * @author Zhang zhanhong 
 */
public class AnswerThread extends Thread{
    private final Socket socket;
    AnswerThread(Socket s){
        this.socket = s;
    }
    @Override
    public void run(){
        System.out.println("answer:"+socket.getInetAddress());
        test();
    }
    public void test(){
        String command;
        while(!"quit".equals( command = receive() ) ){
            String parameter[] = command.split(" ");
            switch(parameter[0]){
                case "check":
                    break;
                case "login":
                    break;
                case "getInboxCount":
                    break;
                case "getM":
                    break;
                case "":
                    break;
                default:
            }
        }
    }
    public void send(String s){
        // 定义一个PrintWriter对象写输出流
        PrintWriter out;
        try {
            out = new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    socket.getOutputStream())), true);
            out.println(s);
        } catch (IOException ex) {
            Logger.getLogger(AnswerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public String receive(){
        String s = "";
        try {
            BufferedReader in
                    = new BufferedReader(
                            new InputStreamReader(
                                    socket.getInputStream()));
             s = in.readLine();
        } catch (IOException ex) {
            Logger.getLogger(AnswerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        return s;
    }
}
