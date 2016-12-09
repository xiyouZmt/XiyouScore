package com.zmt.boxin.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zmt.boxin.Module.ExamCourse;
import com.zmt.boxin.Module.PhysicalTestItem;
import com.zmt.boxin.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dangelo on 2016/7/31.
 */
public class PhysicalTestAdapter extends RecyclerView.Adapter {

    private List<PhysicalTestItem> list;

    public PhysicalTestAdapter(List<PhysicalTestItem> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        setScore(viewHolder, position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setScore(ViewHolder viewHolder, int position){
        viewHolder.examName.setText(list.get(position).getExamName());
        viewHolder.plusRank.setText(list.get(position).getPlusRank());
        viewHolder.score.setText("分数: " + list.get(position).getScore());
        viewHolder.actualScore.setText("成绩: " + list.get(position).getActualScore() + "(" + list.get(position).getExamUnit() + ")");
        viewHolder.rank.setText("等级: " + list.get(position).getRank());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.courseName)
        TextView examName;
        @BindView(R.id.property)
        TextView plusRank;
        @BindView(R.id.score)
        TextView score;
        @BindView(R.id.credit)
        TextView actualScore;
        @BindView(R.id.result)
        TextView rank;
        public ViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
