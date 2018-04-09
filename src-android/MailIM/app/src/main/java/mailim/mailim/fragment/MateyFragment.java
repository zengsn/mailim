package mailim.mailim.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mailim.mailim.activity.ChatActivity;
import mailim.mailim.util.MyApplication;
import mailim.mailim.util.MyHttp;
import mailim.mailim.R;
import mailim.mailim.activity.MainActivity;
import mailim.mailim.entity.Friend;
import mailim.mailim.util.ToastUtil;


public class MateyFragment extends Fragment {
    private ListView mListView;
    private MyBaseAdapter myAdapter;
    private List<Friend> friendList;
    public MateyFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matey,container,false);
        mListView = (ListView) view.findViewById(R.id.lv_matey);

        friendList = MyApplication.getInstance().getFriendList();
        intiData();
        intiView();
        return view;
    }

    private void intiView(){
        myAdapter = new MyBaseAdapter();
        mListView.setAdapter(myAdapter);
        mListView.setOnItemClickListener(new MyOnItemClickListener());
    }

    private void intiData(){
        try {
            friendList = JSONObject.parseArray(JSONObject.toJSONString(MyApplication.getInstance().getFriendList()),Friend.class);
        }catch (JSONException e){
            if(MyApplication.getInstance().debugable){
                ToastUtil.show(getActivity(),e.getMessage());
            }
            e.printStackTrace();
        }
    }

    public void updateFriend(){
        intiData();
        checkFriend();
        myAdapter.notifyDataSetChanged();
    }
    public void checkFriend(){
        for (Friend obj:friendList){
            checkOnline(friendList.indexOf(obj));
            checkIsUser(friendList.indexOf(obj));
        }
    }

    private void checkOnline(final int index){
        String type = "online";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type",type);
        jsonObject.put("email",friendList.get(index).getEmail());
        MyHttp.post(jsonObject, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String str = new String(bytes);
                if("true".equals(str)){
                    friendList.get(index).setStatus(1);
                }
                else {
                    friendList.get(index).setStatus(0);
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }

    private void checkIsUser(final int index){
        String type = "checkUserByEmail";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type",type);
        jsonObject.put("email",friendList.get(index).getEmail());
        MyHttp.post(jsonObject, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String str = new String(bytes);
                if ("true".equals(str)) {
                    friendList.get(index).setStar(Friend.STAR_USER);
                    myAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
            }
        });
    }

    public void getFriend(){
        friendList.clear();
        myAdapter.notifyDataSetChanged();
        String type = "friend";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type",type);
        MyHttp.post(jsonObject, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String str = new String(bytes);
                if("true".equals(str)) {
                    ToastUtil.show(getActivity(),"获取成功！");
                }
                else if("false".equals(str)){
                    ToastUtil.show(getActivity(),"获取失败！");
                }
                else if("[]".equals(str)){
                    ToastUtil.show(getActivity(),"暂无好友！");
                }
                else{
                    try {
                        JSONObject res = JSONObject.parseObject(str);
                        if(res.getBoolean("json")) {
                            JSONObject object = JSONObject.parseObject(res.getString("data"));
                            List<JSONObject> list = new ArrayList<>();
                            for (Map.Entry<String, Object> entry : object.entrySet()) {
                                list.add(JSON.parseObject(entry.getValue().toString()));
                            }
                            friendList.clear();
                            Friend friend;
                            for (JSONObject obj : list) {
                                friend = new Friend();
                                if(MyApplication.getInstance().getMyUser().getUsername().equals(obj.getString("username"))) {
                                    friend.setUsername(obj.getString("friendname"));
                                }
                                else friend.setUsername(obj.getString("username"));
                                if ("true".equals(obj.getString("online"))) {
                                    friend.setStatus(1);
                                } else friend.setStatus(0);
                                friendList.add(friend);
                            }
                            myAdapter.notifyDataSetChanged();
                        }
                    }catch (NullPointerException e){
                        Log.e("空指针",e.getMessage());
                    }catch (Exception e){
                        Log.e("未知异常",e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    class MyBaseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return friendList.size();
        }

        @Override
        public Object getItem(int position) {
            return friendList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(getActivity(),R.layout.list_item_matey,null);
            TextView tvUsername = (TextView) convertView.findViewById(R.id.tv_list_matey);
            TextView tvEmail = (TextView) convertView.findViewById(R.id.tv_list_matey_email);
            tvUsername.setText(friendList.get(position).getUsername());
            tvEmail.setText(friendList.get(position).getEmail());
            TextView tvStatus = (TextView) convertView.findViewById(R.id.tv_list_matey_online);
            String str[] = {"离线","在线"};
            int status = friendList.get(position).getStatus();
            if(status == 0 || status == 1)tvStatus.setText(str[status]);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.list_item_matey_image);
            if(Friend.STAR_USER == friendList.get(position).getStar()){
                MyApplication.getInstance().loadHead(friendList.get(position).getEmail());
                Picasso.with(getActivity())
                        .load(MyApplication.getInstance().getHeadFile(friendList.get(position).getEmail()))
//                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(imageView);
            }
            else {
                imageView.setImageResource(R.mipmap.ic);
            }
            if(MyApplication.getInstance().getNewFriends().size()>0){
                LinearLayout ll = (LinearLayout)MainActivity.mContext.findViewById(R.id.matey_new_friend_lv);
                ll.setVisibility(View.VISIBLE);
            }
            else{
                LinearLayout ll = (LinearLayout)MainActivity.mContext.findViewById(R.id.matey_new_friend_lv);
                ll.setVisibility(View.GONE);
            }
            return convertView;
        }
    }

    private class MyOnItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String email = friendList.get(position).getEmail();
//            if(null != MainActivity.mContext){
//                MainActivity.mContext.getUser(username);
//            }
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            intent.putExtra("email",email);
//            intent.putExtra("friend",friendList.get(position));
            startActivity(intent);
        }
    }
}
