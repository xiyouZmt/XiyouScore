package com.zmt.boxin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zmt.boxin.Activity.NewsDetail;
import com.zmt.boxin.Module.News;
import com.zmt.boxin.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zmt on 2017/1/15.
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<News> list;
    private final int EMPTY_VIEW = 1;
    private final int PROGRESS_VIEW = 2;

    public NewsAdapter(List<News> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if(list.size() == 0){
            return EMPTY_VIEW;
        } else if(list.get(position) == null){
            return PROGRESS_VIEW;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType == PROGRESS_VIEW){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_item, parent, false);
            return new ProgressViewHolder(view);
        } else if(viewType == EMPTY_VIEW){
            return null;
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ViewHolder){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, NewsDetail.class);
                    intent.putExtra(NewsDetail.TAG, list.get(position));
                    context.startActivity(intent);
                }
            });
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.newsName.setText(list.get(position).getTitle());
        } else if(holder instanceof ProgressViewHolder){
            ProgressViewHolder progressViewHolder = (ProgressViewHolder) holder;
            progressViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.news_name) TextView newsName;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ProgressViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.progressBar)
        ProgressBar progressBar;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
