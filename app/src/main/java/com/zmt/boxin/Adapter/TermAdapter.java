package com.zmt.boxin.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zmt.boxin.R;
import com.zmt.boxin.View.DropDownMenu;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dangelo on 2016/7/15.
 */
public class TermAdapter extends RecyclerView.Adapter<TermAdapter.MyViewHolder> {

    private List<String> list;
    private List<View> viewList;
    private OnItemClickListener clickListener;
//    private CardView cardView;

    public TermAdapter(List<String> list, List<View> viewList, OnItemClickListener clickListener) {
        this.list = list;
        this.viewList = viewList;
        this.clickListener = clickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Log.e("list.size--->", String.valueOf(list.size()));
        Log.e("list.String--->", list.get(position));
        List<String> titleList = new ArrayList<>();
        List<View> itemList = new ArrayList<>();
        titleList.add(list.get(position));
        itemList.add(viewList.get(position));
        holder.dropDownMenu.setDropDownMenu(titleList, itemList);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = holder.getLayoutPosition();
                clickListener.OnItemClick(v, n);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.dropDownMenu) DropDownMenu dropDownMenu;
        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener{
        void OnItemClick(View view, int position);
    }

    public void addOnItemClickListener(OnItemClickListener listener){
        clickListener = listener;
    }

}
