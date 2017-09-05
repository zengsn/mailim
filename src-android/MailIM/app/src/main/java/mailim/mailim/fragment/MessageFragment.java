package mailim.mailim.fragment;

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

import mailim.mailim.R;
import mailim.mailim.activity.ChatActivity;


public class MessageFragment extends Fragment {
    private String context;
    private ListView mListView;
    private String[] names = {"第1项","第2项","第3项","第4项","第5项","第6项","第7项","第8项","第9项","第10项","第11项"};
    private int[] icons = {R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher};

    public MessageFragment(String context){
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message,container,false);
        mListView = (ListView) view.findViewById(R.id.lv_message);
        setView();
        return view;
    }

    private void setView(){
        MyBaseAdapter myBaseAdapter = new MyBaseAdapter();
        mListView.setAdapter(myBaseAdapter);
        mListView.setOnItemClickListener(new MyOnItemClickListener());
    }

    class MyBaseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int position) {
            return names[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(getActivity(),R.layout.list_item_message,null);
            TextView mTextView = (TextView) convertView.findViewById(R.id.tv_list_message);
            mTextView.setText(names[position]);
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
