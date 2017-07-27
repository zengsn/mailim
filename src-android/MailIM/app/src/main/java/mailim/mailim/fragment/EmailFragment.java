package mailim.mailim.fragment;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mailim.mailim.Account;
import mailim.mailim.Email;
import mailim.mailim.Login;
import mailim.mailim.MyBaseAdapter;
import mailim.mailim.R;


public class EmailFragment extends Fragment {
    public Context mContext;
    public String string;
    private String context;
    public ListView mListView;
    private Account account;
    public MyBaseAdapter myBaseAdapter;
    public List<Email> emails = new ArrayList<Email>();
    public List<String> list = new ArrayList<String>();

    public EmailFragment(String context, Context mContext){
        this.context = context;
        this.mContext = mContext;
//        Login login = new Login(this);
//        login.execute();
        //Toast.makeText(mContext,login.string+":"+String.valueOf(login.count),Toast.LENGTH_SHORT).show();//        account = new Account();
        Account account = new Account(this);
        account.execute();
        //renovate();
        //Toast.makeText(mContext,account.getString(),Toast.LENGTH_SHORT).show();
        //account.send("smtp.163.com","zhangzhanhong218@163.com","1234567zzh");
    }

    public void show(){
        if(emails == null){
            Toast.makeText(mContext,"空",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(mContext,string,Toast.LENGTH_SHORT).show();
        }
    }
    public void renovate(){
        list.clear();
        for(int i=0; i<emails.size(); i++){
            list.add(emails.get(i).getSubject());
        }
        myBaseAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_email,container,false);
        mListView = (ListView) view.findViewById(R.id.lv_email);
        list.add("name");
        myBaseAdapter = new MyBaseAdapter(list);
        mListView.setAdapter(myBaseAdapter);

        return view;
    }
}
