package com.zmt.boxin.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zmt.boxin.R;

import java.util.List;

/**
 * Created by Dangelo on 2016/7/31.
 */
public class RunAdapter extends RecyclerView.Adapter<RunAdapter.ViewHolder> {

    private List<String> list;
    private OnItemClickListener clickListener;

    public RunAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.run_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.title.setText(position + "");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 30;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void addOnItemClickListener(OnItemClickListener listener){
        clickListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.content);
        }
    }

}
