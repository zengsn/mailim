package mailim.mailim.adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import static mailim.mailim.activity.MainActivity.mContext;

import java.util.List;

import mailim.mailim.R;

/**
 * Created by zzh on 2017/7/27.
 */

public class EmailAdapter extends BaseAdapter {
    public List<String> list;

    public EmailAdapter(List<String> list){
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
        convertView = View.inflate(mContext, R.layout.list_item_email,null);
        TextView mTextView = (TextView) convertView.findViewById(R.id.tv_list_email_subject);
        mTextView.setTextColor(Color.BLACK);
        mTextView.setText(list.get(position));
        return convertView;
    }
}
