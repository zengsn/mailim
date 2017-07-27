
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

/**
 * @date   2017-7-26
 * @author Zhang zhanhong 
 */
public class Account {
    public String user;
    public String password;
    public String host;
    public Message[] message;
    Account(){
        this.host = "pop.163.com";
        this.user = "zhangzhanhong218";
        this.password = "1234567zzh";
    }
    public int getMessageCount(){
        return message.length;
    }
    public Message getMessaage(int index){
        return message[index];
    }
    public void setUser(String host,String user,String pwd){
        this.host = host;
        this.user = user;
        this.password = pwd;
    }
    public boolean checkUser(String host,String user,String pwd){
        setUser(host,user,pwd);
	Properties props = new Properties();
	Session session = Session.getDefaultInstance(props, null);
	Store store;
        boolean check = false;
        try {
            store = session.getStore("pop3");
            store.connect(host, user, pwd);
            check = store.isConnected();
            store.close();
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
        return check;
    }
    public void getMessageFormServer(){
	Properties props = new Properties();
	Session session = Session.getDefaultInstance(props, null);
	Store store;
        try {
            store = session.getStore("pop3");
            store.connect(host, user, password);
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);
            message = folder.getMessages();
            System.out.println("Messages's length: "+message.length);
            folder.close();
            store.close();
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(Account.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
