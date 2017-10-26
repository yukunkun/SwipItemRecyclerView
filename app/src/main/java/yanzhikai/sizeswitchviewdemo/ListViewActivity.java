package yanzhikai.sizeswitchviewdemo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListViewActivity extends AppCompatActivity {

    private RecyclerView mListView;
    List<String> mList=new ArrayList<>();
    private RVHeadFootAdapter mLvAdapter;
    private LinearLayoutManager mManager;
    private GridLayoutManager mGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        mListView = (RecyclerView) findViewById(R.id.recyclerview);
        mManager = new LinearLayoutManager(this);

        mGridLayoutManager = new GridLayoutManager(this,2);
        final GridLayoutManager.SpanSizeLookup lookup = mGridLayoutManager.getSpanSizeLookup();
        mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position==mList.size()+1 ? mGridLayoutManager.getSpanCount() : lookup.getSpanSize(position) ;
            }
        });
//        mListView.setLayoutManager(mManager);
        mListView.setLayoutManager(mGridLayoutManager);

        mLvAdapter = new RVHeadFootAdapter(this,mList);
        getInfo();
        mListView.setAdapter(mLvAdapter);
        setListener();
    }

    private void setListener() {
        mListView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.i("distance:","dx: "+dx+" dy:"+dy);

                int lastVisibleItemPosition = mGridLayoutManager.findLastVisibleItemPosition();
                if(lastVisibleItemPosition==mGridLayoutManager.getItemCount()-1){
                    mLvAdapter.setFooterVisiable(true);
                    mLvAdapter.notifyDataSetChanged();
                    mListView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getInfo();
                            mLvAdapter.setFooterVisiable(false);
                        }
                    },3000);
                }
            }
        });
    }

    private void getInfo() {
        for (int i = 0; i < 3; i++) {
            mList.add("香蕉");
            mList.add("苹果");
            mList.add("栗子");
        }
        mLvAdapter.notifyDataSetChanged();
    }
}
