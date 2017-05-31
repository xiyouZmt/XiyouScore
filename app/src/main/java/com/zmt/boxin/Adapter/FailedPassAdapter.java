package com.zmt.boxin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zmt.boxin.R;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dangelo on 2016/8/3.
 */
public class FailedPassAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String, String>> list;

    public FailedPassAdapter(Context context, List<Map<String, String>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.score_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.courseName.setText(list.get(position).get("courseName"));
        viewHolder.property.setText(list.get(position).get("property"));
        viewHolder.score.setText("最高成绩值: " + list.get(position).get("score"));
        viewHolder.credit.setText("学分: " + list.get(position).get("credit"));
        return convertView;
    }

    static class ViewHolder{
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
        public ViewHolder(View itemView){
            ButterKnife.bind(this, itemView);
        }
    }

}
