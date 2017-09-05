package mailim.mailim.entity;

/**
 * Created by zzh on 2017/8/30.
 */
public class EmailServer {
    private String address;
    private String pop3Server;
    private int pop3Port;
    private String smtpServer;
    private int smtpPort;

    public EmailServer(String address){
        this.address = address;
        switch (address){
            case "qq.com":
                break;
            case "163.com":
                break;
            case "126.com":
                break;
            case "sina.com":
                break;
            case "139.com":
                break;
            default:
        }
    }
}
