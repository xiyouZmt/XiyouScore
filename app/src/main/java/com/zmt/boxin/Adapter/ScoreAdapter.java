package com.zmt.boxin.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zmt.boxin.Module.ExamCourse;
import com.zmt.boxin.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dangelo on 2016/7/31.
 */
public class ScoreAdapter extends RecyclerView.Adapter {

    private List<ExamCourse> list;

    public ScoreAdapter(List<ExamCourse> list) {
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
        viewHolder.courseName.setText(list.get(position).getCourseName());
        viewHolder.property.setText(list.get(position).getCourseProperty());
        viewHolder.score.setText("正考成绩: " + list.get(position).getCourseScore());
        viewHolder.credit.setText("学分: " + list.get(position).getCourseCredit());
        viewHolder.examAgain.setVisibility(View.GONE);
        if(list.get(position).getCourseScore().equals("不及格")){
            viewHolder.result.setText("未通过");
            viewHolder.examAgain.setVisibility(View.VISIBLE);
            viewHolder.resit.setText(list.get(position).getResitScore());
            if(list.get(position).getRetakeScore().equals("不及格")){
                viewHolder.result.setText("重修未通过");
            } else {
                viewHolder.result.setText("重修通过");
            }
        } else if(list.get(position).getCourseScore().equals("及格")
                || list.get(position).getCourseScore().equals("合格")
                || list.get(position).getCourseScore().equals("中等")
                || list.get(position).getCourseScore().equals("良好")
                || list.get(position).getCourseScore().equals("优秀")) {
            viewHolder.result.setText("正考通过");
        } else if (Integer.parseInt(list.get(position).getCourseScore()) < 60) {
            viewHolder.result.setText("未通过");
            viewHolder.examAgain.setVisibility(View.VISIBLE);
            if(list.get(position).getResitScore().length() != 1){
                viewHolder.resit.setText("补考成绩: " + list.get(position).getResitScore());
                if(Integer.parseInt(list.get(position).getResitScore()) < 60){
                    viewHolder.state.setText("补考未通过");
                } else {
                    viewHolder.state.setText("补考通过");
                }
            }
            if(list.get(position).getRetakeScore().length() != 1){
                viewHolder.resit.setText("重修成绩: " + list.get(position).getRetakeScore());
                if(Integer.parseInt(list.get(position).getRetakeScore()) < 60){
                    viewHolder.state.setText("重修未通过");
                } else {
                    viewHolder.state.setText("重修通过");
                }
            }
        } else {
            viewHolder.result.setText("正考通过");
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.courseName)
        TextView courseName;
        @BindView(R.id.property)
        TextView property;
        @BindView(R.id.score)
        TextView score;
        @BindView(R.id.credit)
        TextView credit;
        @BindView(R.id.result)
        TextView result;
        @BindView(R.id.resit)
        TextView resit;
        @BindView(R.id.state)
        TextView state;
        @BindView(R.id.examAgain)
        RelativeLayout examAgain;
        public ViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
