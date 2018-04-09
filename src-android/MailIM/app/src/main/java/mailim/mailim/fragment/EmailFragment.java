package mailim.mailim.fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mailim.mailim.activity.MainActivity;
import mailim.mailim.entity.Chat;
import mailim.mailim.util.DateUtil;
import mailim.mailim.util.EmaiRecever;
import mailim.mailim.activity.EmailActivity;
import mailim.mailim.entity.Email;
import mailim.mailim.R;
import mailim.mailim.util.EmailUtil;
import mailim.mailim.util.MailMessageUtil;
import mailim.mailim.util.MyApplication;
import mailim.mailim.util.ToastUtil;


public class EmailFragment extends Fragment {
    public ListView mListView;
    public TextView TVgroovy;
    public TextView TVspecial;
    public MyAdapter adapter;
    public List<Email> emails = new ArrayList<>();
    public List<Email> allEmails = new ArrayList<>();
    public ProgressDialog waitingDialog=
            new ProgressDialog(MainActivity.mContext);
    private boolean isSpecial = false;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what){
                case 1:
                    ToastUtil.show(getActivity(),msg.obj.toString());
                    break;
                case 2:
                    updataEmail();
                    waitingDialog.cancel();
                    MyApplication.getInstance().intiFriendList();
                    MainActivity.f2.updateFriend();
                    break;
            }
        }
    };

    public EmailFragment(){
    }

    public boolean recevieEmail(){
        emails.clear();
        adapter.notifyDataSetChanged();

        final String email = MyApplication.getInstance().getMyUser().getEmail();
        final String pwd = MyApplication.getInstance().getMyUser().getEmailpwd();
        if(!EmailUtil.isEmail(email)){
            ToastUtil.show(getActivity(),"邮箱账号不正确");
            return false;
        }
        waitingDialog.setTitle("接收邮件");
        waitingDialog.setMessage("接收中...");
        waitingDialog.setIndeterminate(true);
        waitingDialog.setCancelable(true);
        waitingDialog.show();
        new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                EmaiRecever.recevie(email,pwd);
                android.os.Message msg = new android.os.Message();
                msg.what = 2;
                handler.sendMessage(msg);
                super.run();
            }
        }.start();
        return true;
    }

    public void updataEmail(){
        allEmails = new ArrayList<>(MyApplication.getInstance().getInboxEmail());
        allEmails.addAll(MyApplication.getInstance().getMailimEmail());
        emails.clear();
        emails.addAll(allEmails);
        filterEmail();
        adapter.notifyDataSetChanged();
    }

    public void filterEmail(){
        emails.clear();
        emails.addAll(allEmails);
        Iterator<Email> iterator = emails.iterator();
        while (iterator.hasNext()){
            Email email = iterator.next();
            if(email.getSubject().contains(MailMessageUtil.SUBJECT_MAILIM)){
                if(!isSpecial)iterator.remove();
            }
            else {
                if(isSpecial)iterator.remove();
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_email,container,false);
        mListView = (ListView) view.findViewById(R.id.lv_email);
        adapter = new MyAdapter();
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new MyOnItemClickListener());
        mListView.setOnItemLongClickListener(new MyItemLongClickListener());
        TVgroovy = (TextView)view.findViewById(R.id.email_groovy);
        TVspecial = (TextView)view.findViewById(R.id.email_special);
        TVgroovy.setSelected(true);
        TVspecial.setSelected(false);
        TVgroovy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TVspecial.setSelected(false);
                TVgroovy.setSelected(true);
                isSpecial = false;
                filterEmail();
            }
        });
        TVspecial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TVgroovy.setSelected(false);
                TVspecial.setSelected(true);
                isSpecial = true;
                filterEmail();
            }
        });
        recevieEmail();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
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

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(getActivity(), R.layout.list_item_email,null);
            TextView from_tv = (TextView)convertView.findViewById(R.id.tv_list_email_from);
            TextView time = (TextView)convertView.findViewById(R.id.email_list_item_time);

            time.setText(DateUtil.DateToString(emails.get(position).getSendDate()));
//            if(position>0){
//                if(time.getText().equals(DateUtil.DateToString(emails.get(position-1).getSendDate())))
//                    time.setVisibility(View.GONE);
//            }
            from_tv.setTextColor(Color.BLACK);
            String name = emails.get(position).getName();
            String email = emails.get(position).getEmailAddr();
            if(null == name || "".equals(name))name = MyApplication.getInstance().getFriendUsername(email);
            //ev_fromaddr.setText(name+"<"+ email +">");
            if("未命名".equals(name))from_tv.setText(email);
            else from_tv.setText(name);


            TextView mTextView = (TextView) convertView.findViewById(R.id.tv_list_email_subject);
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

    private class MyItemLongClickListener implements AdapterView.OnItemLongClickListener{

        @SuppressLint("ResourceType")
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            View view_add_friend = View.inflate(getActivity(),R.layout.layout_add_friend,null);
            final EditText ediTxt_email =(EditText) view_add_friend.findViewById(R.id.add_friend_email);
            final EditText ediTxt_name =(EditText) view_add_friend.findViewById(R.id.add_friend_name);
            ediTxt_email.setText(emails.get(position).getEmailAddr());
            ediTxt_name.setText(emails.get(position).getName());

            AlertDialog.Builder inputDialog =
                    new AlertDialog.Builder(getActivity());
            inputDialog.setTitle("添加好友").setView(view_add_friend);

//            AlertDialog inputDialog = new AlertDialog(getActivity(),0);

            inputDialog.setNegativeButton("取消",null);
            inputDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MyApplication.getInstance().addFriend(ediTxt_email.getText().toString(),ediTxt_name.getText().toString());
                    MyApplication.getInstance().updateFriendToEmail();
                    Log.e("dfa","test");
                }
            }).show();
            return true;
        }
    }
}
