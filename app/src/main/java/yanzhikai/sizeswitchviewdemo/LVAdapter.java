package yanzhikai.sizeswitchviewdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yukun on 17-9-12.
 */

public class LVAdapter extends BaseAdapter{
    Context mContext;
    List<String> mList;

    public LVAdapter(Context context, List<String> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.rv_item, null);
        TextView textView = (TextView) convertView.findViewById(R.id.tv_item);
        textView.setText(mList.get(position));
        return convertView;
    }
}
