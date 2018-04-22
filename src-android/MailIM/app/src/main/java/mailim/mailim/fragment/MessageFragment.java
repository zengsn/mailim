package mailim.mailim.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mailim.mailim.R;
import mailim.mailim.activity.ChatActivity;
import mailim.mailim.activity.MainActivity;
import mailim.mailim.entity.Friend;
import mailim.mailim.entity.Message;
import mailim.mailim.util.DateUtil;
import mailim.mailim.util.InputUtil;
import mailim.mailim.util.MyApplication;
import mailim.mailim.util.OutputUtil;


public class MessageFragment extends Fragment {
    private static ListView mListView;
    private static List<Message> messageList;
    private static MyAdapter adapter;
    public MessageFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message,container,false);
        mListView = (ListView) view.findViewById(R.id.lv_message);
        intiView();
        intiData();
        return view;
    }

    @Override
    public void onPause() {
        saveData();
        super.onPause();
    }


    public void update(){
        saveData();
        messageList.clear();
        adapter.notifyDataSetChanged();
        intiData();
        updateMessage();
        adapter.notifyDataSetChanged();
    }

    public static void clearRaw(String email){
        for(Message message:messageList){
            if(message.getEmail().equals(email)) {
                message.setRaw(0);
            }
        }
        MainActivity.updataNum();
    }

    public static int getMsgCount(){
//        intiData();
        int count = 0;
        if(messageList != null){
            for (Message message:messageList){
                if(message.isRaw())count++;
            }
        }
//        saveData();
        return count;
    }

    public static void addMessage(String time,String email,String last,boolean toTop){
        Message message;
//        intiData();
        boolean flag = true;
        Iterator<Message> iterator = messageList.iterator();
        while (iterator.hasNext()){
            message = iterator.next();
            if(email.equals(message.getEmail())){
                if(toTop){
                    iterator.remove();
                }
                message.setLast(last);
                flag = false;
            }
        }
        message = new Message(MyApplication.getInstance().getFriendUsername(email),email,last);
        message.setTime(time);
        if(flag || toTop)messageList.add(0,message);
//        saveData();
        adapter.notifyDataSetChanged();
    }

    private static void intiData(){
        InputUtil<Message> inputUtil = new InputUtil<Message>();
        List<Message> list = inputUtil.readListFromSdCard(MyApplication.getInstance().getLocalPath()+"message");
        if(list != null)messageList = list;
    }

    public static void updateMessage(){
        for(Message obj:messageList){
            Friend friend = MyApplication.getInstance().getFriend(obj.getEmail());
            if(null != friend.getUsername() && !"".equals(friend.getUsername()))obj.setUsername(friend.getUsername());
        }
    }

    private static void saveData(){
        OutputUtil<Message> outputUtil = new OutputUtil<Message>();
        outputUtil.writeListIntoSDcard(MyApplication.getInstance().getLocalPath()+"message",messageList);
    }

    private void intiView(){
        messageList = new ArrayList<Message>();
        adapter = new MyAdapter();
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new MyOnItemClickListener());
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if(messageList == null)return 0;
            else return messageList.size();
        }

        @Override
        public Object getItem(int position) {
            return messageList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(getActivity(),R.layout.list_item_message,null);
            TextView mTextView = (TextView) convertView.findViewById(R.id.tv_list_message);
            mTextView.setText(messageList.get(position).getUsername());
            TextView textView = (TextView) convertView.findViewById(R.id.tv_list_message_last);
            textView.setText(messageList.get(position).getLast());
            TextView tvTime = (TextView) convertView.findViewById(R.id.tv_list_message_time);
            tvTime.setText(DateUtil.getDateString(Long.valueOf(messageList.get(position).getTime())*1000));
            ImageView imageView = (ImageView) convertView.findViewById(R.id.list_item_message_image);
            MyApplication.getInstance().loadHead(messageList.get(position).getEmail());
            Picasso.with(getActivity())
                    .load(MyApplication.getInstance().getHeadFile(messageList.get(position).getEmail()))
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(imageView);
            return convertView;
        }
    }

    private class MyOnItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            String email = messageList.get(position).getEmail();
            intent.putExtra("email",email);
            startActivity(intent);
        }
    }
}
