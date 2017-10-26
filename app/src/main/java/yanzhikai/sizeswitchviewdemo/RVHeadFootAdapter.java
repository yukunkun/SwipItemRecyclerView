package yanzhikai.sizeswitchviewdemo;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yukun on 17-9-12.
 */

public class RVHeadFootAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context mContext;
    List<String> mList;
    int foot=2;
    boolean isShowFooter;
    boolean isShowHeader=true;
    SwipeRefreshLayout mSwipeRefreshLayout;
    public RVHeadFootAdapter(Context context, List<String> list) {
        mContext = context;
        mList = list;
    }

    public void setFooterVisiable(boolean footerVisiable){
        isShowFooter=footerVisiable;
    }
    public void setHeaderVisiable(boolean footerVisiable){
        isShowHeader=footerVisiable;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View inflate = LayoutInflater.from(mContext).inflate(R.layout.rv_item, null);
        View footer = LayoutInflater.from(mContext).inflate(R.layout.rv_item, null);
        ((TextView)(footer.findViewById(R.id.tv_item))).setText("加载更多　loading");
        View header = LayoutInflater.from(mContext).inflate(R.layout.rv_item, null);
        ((TextView)(header.findViewById(R.id.tv_item))).setText("refresh");
        Log.i("ViewType",viewType+"");
        if(viewType==1){
            return new MyFotterHolder(footer);
        }else if(viewType==0){
            return new MyHeaderHolder(header);
        } else {
            return new MyHolder(inflate);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.i("TAG",position+"");
        if(holder instanceof MyHolder){
            if(position<=mList.size())
            ((MyHolder) holder).mTextView.setText(mList.get(position-1));

        }else if(holder instanceof MyHeaderHolder){
            if(isShowHeader){
                ((MyHeaderHolder) holder).mTextView.setVisibility(View.VISIBLE);
            }else {
                ((MyHeaderHolder) holder).mTextView.setVisibility(View.GONE);
            }
        }else if(holder instanceof MyHeaderHolder){
            if(isShowFooter){
                ((MyHeaderHolder) holder).mTextView.setVisibility(View.VISIBLE);
            }else {
                ((MyHeaderHolder) holder).mTextView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return 0;
        }else if(position==mList.size()+1){
            return 1;
        }else{
            return 2;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size()+2;
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView mTextView;
        public MyHolder(View itemView) {
            super(itemView);
            mTextView=(TextView) itemView.findViewById(R.id.tv_item);
        }
    }

    class MyHeaderHolder extends RecyclerView.ViewHolder{
        TextView mTextView;
        public MyHeaderHolder(View itemView) {
            super(itemView);
            mTextView=(TextView) itemView.findViewById(R.id.tv_item);
        }
    }
    class MyFotterHolder extends RecyclerView.ViewHolder{
        TextView mTextView;
        public MyFotterHolder(View itemView) {
            super(itemView);
            mTextView=(TextView) itemView.findViewById(R.id.tv_item);
        }
    }
}
