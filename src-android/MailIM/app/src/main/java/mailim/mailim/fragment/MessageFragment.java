package mailim.mailim.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.ArraySet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import mailim.mailim.R;
import mailim.mailim.activity.ChatActivity;
import mailim.mailim.entity.Message;
import mailim.mailim.entity.User;
import mailim.mailim.util.InputUtil;
import mailim.mailim.util.OutputUtil;
import mailim.mailim.util.ToastUtil;


public class MessageFragment extends Fragment {
    private ListView mListView;
    private List<Message> messageList;
    private MyAdapter adapter;
    private int[] icons = {R.mipmap.ic_launcher,R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher};

    public MessageFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message,container,false);
        mListView = (ListView) view.findViewById(R.id.lv_message);
        intiView();
        intiData();
        //test();
        return view;
    }

    @Override
    public void onDestroy() {
        saveData();
        super.onDestroy();
    }

    private void test(){
        Message message = new Message();
        message.setRaw(2);
        User user = new User();
        user.setRemarksName("zzh");
        user.setUsername("zh");
        message.setUser(user);
        messageList = new ArrayList<Message>();
        messageList.add(message);
        adapter.notifyDataSetChanged();
    }

    private void intiData(){
        InputUtil<Message> messageInputUtil = new InputUtil<Message>();
        messageList = messageInputUtil.readListFromSdCard("mailim/message.txt");
        if(messageList == null)
            ToastUtil.show(getActivity(),"读取失败");
        else ToastUtil.show(getActivity(),"读取成功");
    }

    private void saveData(){
        OutputUtil<Message> messageOutputUtil = new OutputUtil<Message>();
        if(messageOutputUtil.writeListIntoSDcard("mailim/message.txt",messageList))
            ToastUtil.show(getActivity(),"保存成功");
        else ToastUtil.show(getActivity(),"保存失败");
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

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(getActivity(),R.layout.list_item_message,null);
            TextView mTextView = (TextView) convertView.findViewById(R.id.tv_list_message);
            mTextView.setText(messageList.get(position).getUser().getRemarksName());
            ImageView imageView = (ImageView) convertView.findViewById(R.id.list_item_message_image);
            imageView.setBackgroundResource(icons[position]);
            return convertView;
        }
    }

    private class MyOnItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            startActivity(intent);
        }
    }
}
