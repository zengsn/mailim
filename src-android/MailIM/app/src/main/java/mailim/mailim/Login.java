package mailim.mailim;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import mailim.mailim.fragment.EmailFragment;

/**
 * Created by zzh on 2017/7/26.
 */
public class Login extends AsyncTask<Void,String,Boolean> {
    public String string;
    public int count = 0;
    public Socket socket;
    public BufferedReader in;
    public PrintWriter ou;
    public EmailFragment fragment;
    public Login(EmailFragment fragment){
        this.fragment = fragment;
    }

    @Override
    protected void onProgressUpdate(String...valve){
        fragment.show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            socket = new Socket("192.168.218.101",8218);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ou = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            ou.println("aaa");
            fragment.string = in.readLine();
            publishProgress();
            //count = Integer.valueOf(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void test(){
    }
}
