package com.zmt.boxin.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zmt.boxin.Module.TrainCourses;
import com.zmt.boxin.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MintaoZhu on 2016/11/29.
 */
public class TrainAdapter extends RecyclerView.Adapter<TrainAdapter.ViewHolder>{

    private List<TrainCourses> list;

    public TrainAdapter(List<TrainCourses> list) {
        this.list = list;
    }

    @Override
    public TrainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.train_plan_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.courseName.setText(list.get(position).getCourseName());
        holder.property.setText(list.get(position).getCourseProperty());
        holder.examMethod.setText("考核方式: " + list.get(position).getExamMethod());
        holder.courseCredit.setText("学分: " + list.get(position).getCourseCredit());
        holder.weekHours.setText("周学时: " + list.get(position).getWeekHours());
        holder.courseTime.setText("起始结束周: " + list.get(position).getCourseTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.courseName) TextView courseName;
        @BindView(R.id.property) TextView property;
        @BindView(R.id.examMethod) TextView examMethod;
        @BindView(R.id.credit) TextView courseCredit;
        @BindView(R.id.weekHours) TextView weekHours;
        @BindView(R.id.courseTime) TextView courseTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}