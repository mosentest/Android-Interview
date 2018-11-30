package moziqi.interviewdemo.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import moziqi.interviewdemo.R;

/**
 * Copyright (C), 2018-2018
 * FileName: RecyclerviewAdapter
 * Author: ziqimo
 * Date: 2018/11/30 下午10:07
 * Description: ${DESCRIPTION}
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder> {


    private Context context;
    private List<DataModel> data = new ArrayList<>();

    private RecyclerviewItemListener recyclerviewItemListener;

    public RecyclerviewAdapter(Context context, List<DataModel> data) {
        this.context = context;
        this.data.clear();
        this.data.addAll(data);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.title.setText(data.get(position).title);
        holder.url.setText(data.get(position).url);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerviewItemListener != null) {
                    recyclerviewItemListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public void setRecyclerviewItemListener(RecyclerviewItemListener recyclerviewItemListener) {
        this.recyclerviewItemListener = recyclerviewItemListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView url;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            url = itemView.findViewById(R.id.url);

        }
    }


}
