package moziqi.interviewdemo.recyclerview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;

import moziqi.interviewdemo.R;
import moziqi.interviewdemo.util.TouchUtils;
import moziqi.interviewdemo.webview.WebViewActivity;

public class RecyclerviewActivity extends AppCompatActivity {


    private TouchUtils touchUtils = new TouchUtils();

    private RecyclerView recyclerView;

    private RecyclerviewAdapter recyclerviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        recyclerView = findViewById(R.id.rv);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final ArrayList<DataModel> dataModels = new ArrayList<>();
        dataModels.add(create("新浪", "https://www.sina.com.cn"));
        dataModels.add(create("腾讯", "https://www.qq.com"));
        dataModels.add(create("微博", "https://weibo.com/login.php"));
        dataModels.add(create("今日头条", "https://www.toutiao.com/"));
        dataModels.add(create("java锁的概念", "https://www.cnblogs.com/doit8791/p/7776501.html"));
        recyclerviewAdapter = new RecyclerviewAdapter(this, dataModels);
        recyclerviewAdapter.setRecyclerviewItemListener(new RecyclerviewItemListener() {
            @Override
            public void onItemClick(int pos) {
                Intent intent = new Intent(RecyclerviewActivity.this, WebViewActivity.class);
                intent.putExtra("url", dataModels.get(pos).url);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(recyclerviewAdapter);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (recyclerviewAdapter.getItemCount() > 0) {
            RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(0);
            if (holder != null && holder instanceof RecyclerviewAdapter.InnerViewHolder) {
                RecyclerviewAdapter.InnerViewHolder viewHolder = (RecyclerviewAdapter.InnerViewHolder) holder;
                int[] location = new int[2];
                viewHolder.itemView.getLocationOnScreen(location);
                //使用坐标
                touchUtils.simulationDownTouch(viewHolder.itemView, location[0] / 2f, location[1] / 2f, 0);
            }
        }
    }

    private DataModel create(String title, String url) {
        DataModel dataModel = new DataModel();
        dataModel.title = title;
        dataModel.url = url;
        return dataModel;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
