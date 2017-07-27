package mailim.mailim;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import static mailim.mailim.MainActivity.mContext;

import java.util.List;

/**
 * Created by zzh on 2017/7/27.
 */

public class MyBaseAdapter extends BaseAdapter {
    public List<String> list;
    public int[] icons = {R.mipmap.ic_launcher,R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,R.mipmap.ic_launcher};

    public MyBaseAdapter(List<String> list){
        this.list = list;
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
        convertView = View.inflate(mContext,R.layout.list_item_email,null);
        TextView mTextView = (TextView) convertView.findViewById(R.id.tv_list_email);
        mTextView.setTextColor(Color.BLACK);
        mTextView.setText(list.get(position));
        ImageView imageView = (ImageView) convertView.findViewById(R.id.list_item_email_image);
        imageView.setBackgroundResource(icons[position]);
        return convertView;
    }
}
