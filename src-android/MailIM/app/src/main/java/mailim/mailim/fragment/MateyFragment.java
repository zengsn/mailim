package mailim.mailim.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import mailim.mailim.R;


public class MateyFragment extends Fragment {
    private String context;
    private ListView mListView;
    private String[] names = {"第2项","第2项","第3项","第4项","第5项","第6项","第7项","第8项","第9项","第10项"};
    private int[] icons = {R.mipmap.ic_launcher,R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher};

    public MateyFragment(String context){
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_matey,container,false);
        mListView = (ListView) view.findViewById(R.id.lv_matey);
        MyBaseAdapter myBaseAdapter = new MyBaseAdapter();
        mListView.setAdapter(myBaseAdapter);

        return view;
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
            convertView = View.inflate(getActivity(),R.layout.list_item_matey,null);
            TextView mTextView = (TextView) convertView.findViewById(R.id.tv_list_matey);
            mTextView.setText(names[position]);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.list_item_matey_image);
            imageView.setBackgroundResource(icons[position]);
            return convertView;
        }
    }
}
