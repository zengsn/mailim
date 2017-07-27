/**
 * @date   2017-7-26
 * @author Zhang zhanhong 
 */
public class MainClass {
    public static NewJFrame frame;
    public static void main(String[] args){
        frame = new NewJFrame();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        ListenThread listenThread = new ListenThread();
        listenThread.run();
    }
}
