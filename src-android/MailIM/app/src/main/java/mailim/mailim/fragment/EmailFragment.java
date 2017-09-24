package mailim.mailim.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mailim.mailim.util.EmaiRecever;
import mailim.mailim.activity.EmailActivity;
import mailim.mailim.entity.Email;
import mailim.mailim.R;


public class EmailFragment extends Fragment {
    public Context mContext;
    public ListView mListView;
    private EmaiRecever emaiRecever;
    public MyAdapter adapter;
    public List<Email> emails = new ArrayList<Email>();

    public EmailFragment(Context mContext){
        this.mContext = mContext;
        //Toast.makeText(mContext,login.string+":"+String.valueOf(login.count),Toast.LENGTH_SHORT).show();//        emaiRecever = new EmaiRecever();
        emaiRecever = new EmaiRecever(this);
        emaiRecever.execute();
        //renovate();
        //Toast.makeText(mContext,emaiRecever.getString(),Toast.LENGTH_SHORT).show();
        //emaiRecever.send("smtp.163.com","zhangzhanhong218@163.com","1234567zzh");
    }

    public void updataEmail(List<Email> emails){
        this.emails = emails;
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_email,container,false);
        mListView = (ListView) view.findViewById(R.id.lv_email);
        adapter = new MyAdapter();
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new MyOnItemClickListener());

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
            TextView ev_fromaddr = (TextView)convertView.findViewById(R.id.tv_list_email_from);
            ev_fromaddr.setTextColor(Color.BLACK);
            ev_fromaddr.setText(emails.get(position).getFrom_address());

            TextView mTextView = (TextView) convertView.findViewById(R.id.tv_list_email_subject);
            mTextView.setTextColor(Color.BLACK);
            mTextView.setText(emails.get(position).getSubject());

            return convertView;
        }
    }

    private class MyOnItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getActivity(), EmailActivity.class);
            intent.putExtra("email",emails.get(position));
            startActivity(intent);
        }
    }
}
