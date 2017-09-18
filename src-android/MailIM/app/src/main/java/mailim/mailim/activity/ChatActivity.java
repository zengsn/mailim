package mailim.mailim.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mailim.mailim.R;
import mailim.mailim.entity.Chat;

/**
 *聊天界面
 */
public class ChatActivity extends Activity implements View.OnClickListener{
    private List<Chat> list = new ArrayList<Chat>();
    private int myHead = R.mipmap.ic_launcher;
    private int friendHead = R.mipmap.ic_menu_message;
    private ListView myLV;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        inti();
    }

    /**
     *初始化
     */
    private void inti(){
        Button btnSend = (Button)findViewById(R.id.btn_right);
        Button btnleft = (Button)findViewById(R.id.btn_left);
        btnleft.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        myLV = (ListView)findViewById(R.id.lv_chat);
        adapter = new MyAdapter(this);
        myLV.setAdapter(adapter);
        myLV.setSelection(adapter.getCount()-1);
    }

    /**
     *添加消息
     */
    public void addMessage(Chat chat){
        list.add(chat);
        adapter.notifyDataSetChanged();
        myLV.setSelection(adapter.getCount()-1);
    }
    @Override
    public void onClick(View v) {
        EditText etMeg = (EditText)findViewById(R.id.et_meg);
        String text = etMeg.getText().toString();
        switch (v.getId()){
            case R.id.btn_left:
                Chat lm = new Chat(false,text);
                addMessage(lm);
                break;
            case R.id.btn_right:
                Chat rm = new Chat(true,text);
                addMessage(rm);
                break;
            default:
        }
    }

    /**
     * 消息适配器
     */
    class MyAdapter extends BaseAdapter{
        private Context context;

        public MyAdapter(Context context){
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //获取控件
            convertView = View.inflate(this.context,R.layout.list_item_chat,null);
            RelativeLayout left = (RelativeLayout)convertView.findViewById(R.id.left);
            RelativeLayout right = (RelativeLayout)convertView.findViewById(R.id.right);
            TextView textLeft = (TextView)convertView.findViewById(R.id.text_left);
            TextView textRight = (TextView)convertView.findViewById(R.id.text_right);
            ImageView imgLeft = (ImageView)convertView.findViewById(R.id.img_chat_left);
            ImageView imgRight = (ImageView)convertView.findViewById(R.id.img_chat_right);
            //获取消息数据
            Chat m = list.get(position);
            if(m.isMyself()){
                //发送的消息（在右边显示）
                textRight.setText(m.getText());
                right.setVisibility(View.VISIBLE);
                left.setVisibility(View.INVISIBLE);
            }else{
                //接收的消息（在左边显示）
                textLeft.setText(m.getText());
                left.setVisibility(View.VISIBLE);
                right.setVisibility(View.INVISIBLE);
            }
            //设置头像
            imgLeft.setImageResource(friendHead);
            imgRight.setImageResource(myHead);

            return convertView;
        }
    }
}
