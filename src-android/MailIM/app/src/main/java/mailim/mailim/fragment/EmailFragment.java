package mailim.mailim.fragment;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;

import mailim.mailim.activity.MainActivity;
import mailim.mailim.activity.SettingsActivity;
import mailim.mailim.util.EmaiRecever;
import mailim.mailim.activity.EmailActivity;
import mailim.mailim.entity.Email;
import mailim.mailim.R;
import mailim.mailim.util.EmailUtil;
import mailim.mailim.util.MailMessageUtil;
import mailim.mailim.util.ToastUtil;


public class EmailFragment extends Fragment {
    public ListView mListView;
    public MyAdapter adapter;
    public List<Email> emails = new ArrayList<Email>();
    public List<Email> allEmails = new ArrayList<Email>();
    public ProgressDialog waitingDialog=
            new ProgressDialog(MainActivity.mContext);

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
                    MainActivity.app.intiFriendList();
                    break;
            }
        }
    };

    public EmailFragment(){
    }

    public boolean recevieEmail(){
        emails.clear();
        adapter.notifyDataSetChanged();

        final String email = MainActivity.app.getMyUser().getEmail();
        final String pwd = MainActivity.app.getMyUser().getEmailpwd();
        if(!EmailUtil.isEmail(email)){
            ToastUtil.show(getActivity(),"邮箱账号不正确");
            return false;
        }
        waitingDialog.setTitle("接收邮件");
        waitingDialog.setMessage("接收中...");
        waitingDialog.setIndeterminate(true);
        waitingDialog.setCancelable(false);
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
        allEmails = new ArrayList<>(MainActivity.app.getInboxEmail());
        allEmails.addAll(MainActivity.app.getMailimEmail());
        emails.clear();
        emails.addAll(allEmails);
        adapter.notifyDataSetChanged();
    }

    public void filter(){
        String[] item = new String[]{"普通邮件","mailim邮件"};
        final boolean[] isCheck = new boolean[item.length];
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("请选择")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        filter(isCheck);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setMultiChoiceItems(item, isCheck, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        isCheck[which] = isChecked;
                    }
                }).create();
        alertDialog.show();
    }

    public void filter(boolean isCheck[]){
        emails.clear();
        emails.addAll(allEmails);
        Iterator<Email> iterator = emails.iterator();
        while (iterator.hasNext()){
            Email email = iterator.next();
            if(email.getSubject().contains(MailMessageUtil.SUBJECT_MAILIM)){
                if(!isCheck[1])iterator.remove();
            }
            else {
                if(!isCheck[0])iterator.remove();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_email,container,false);
        mListView = (ListView) view.findViewById(R.id.lv_email);
        adapter = new MyAdapter();
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new MyOnItemClickListener());
        mListView.setOnItemLongClickListener(new MyItemLongClickListener());
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

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(getActivity(), R.layout.list_item_email,null);
            TextView ev_fromaddr = (TextView)convertView.findViewById(R.id.tv_list_email_from);
            ev_fromaddr.setTextColor(Color.BLACK);
            String name = emails.get(position).getName();
            String email = emails.get(position).getEmailAddr();
            if(null == name || "".equals(name))name = MainActivity.app.getFriendUsername(email);
            //ev_fromaddr.setText(name+"<"+ email +">");
            if("未命名".equals(name))ev_fromaddr.setText(email);
            else ev_fromaddr.setText(name);

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

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            final EditText editText = new EditText(getActivity());
            editText.setText(emails.get(position).getName());
            AlertDialog.Builder inputDialog =
                    new AlertDialog.Builder(getActivity());
            inputDialog.setTitle("添加好友").setView(editText);
            inputDialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ToastUtil.show(getActivity(),editText.getText().toString());
                            MainActivity.app.addFriend(emails.get(position).getEmailAddr(),editText.getText().toString());
                        }
                    }).show();
            return true;
        }
    }
}
