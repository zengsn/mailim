package mailim.mailim.fragment;


import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mailim.mailim.Account;
import mailim.mailim.entity.Email;
import mailim.mailim.adapter.EmailAdapter;
import mailim.mailim.R;

import static mailim.mailim.activity.MainActivity.mContext;


public class EmailFragment extends Fragment {
    public Context mContext;
    public ListView mListView;
    private Account account;
    public MyAdapter adapter;
    public List<Email> emails = new ArrayList<Email>();

    public EmailFragment(Context mContext){
        this.mContext = mContext;
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
            Toast.makeText(mContext,String.valueOf(emails.size()),Toast.LENGTH_SHORT).show();
        }
    }
    public void renovate(){
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_email,container,false);
        mListView = (ListView) view.findViewById(R.id.lv_email);
        adapter = new MyAdapter();
        mListView.setAdapter(adapter);

        return view;
    }

    private class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return emails.size();
        }

        @Override
        public Object getItem(int position) {
            return emails.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(mContext, R.layout.list_item_email,null);
            TextView mTextView = (TextView) convertView.findViewById(R.id.tv_list_email_subject);
            mTextView.setTextColor(Color.BLACK);
            mTextView.setText(emails.get(position).getSubject());
            return convertView;
        }
    }
}
