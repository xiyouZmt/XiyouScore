package com.zmt.boxin.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zmt.boxin.Adapter.RunAdapter;
import com.zmt.boxin.R;
import com.zmt.boxin.Utils.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class Run extends android.support.v4.app.Fragment implements RunAdapter.OnItemClickListener {

    private View view;
    private List<String> list;
    @BindView(R.id.aRecyclerView) RecyclerView aRecyclerView;
    @BindView(R.id.bRecyclerView) RecyclerView bRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_run, container, false);
        initViews();
        return view;
    }

    public void initViews(){
        ButterKnife.bind(this,view);
        list = new ArrayList<>();
        RunAdapter adapter = new RunAdapter(list);
        adapter.addOnItemClickListener(this);
        /**
         * a
         */
        LinearLayoutManager aManager = new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false);
        aRecyclerView.setLayoutManager(aManager);
        aRecyclerView.addItemDecoration(new SpaceItemDecoration(
                getContext(), SpaceItemDecoration.VERTICAL_LIST));
        aRecyclerView.addOnScrollListener(new ScrollListener());
        /**
         * b
         */
        LinearLayoutManager bManager = new LinearLayoutManager(
                getContext(), LinearLayoutManager.VERTICAL, false);
        bRecyclerView.setLayoutManager(bManager);
        bRecyclerView.addItemDecoration(new SpaceItemDecoration(
                getContext(), SpaceItemDecoration.VERTICAL_LIST));
        bRecyclerView.addOnScrollListener(new ScrollListener());

        aRecyclerView.setAdapter(adapter);
        bRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Snackbar.make(view, "点击了第" + position + "项", Snackbar.LENGTH_SHORT)
                .setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    public class ScrollListener extends RecyclerView.OnScrollListener {

        public void onScrollStateChanged(RecyclerView recyclerView, int newState){
            super.onScrollStateChanged(recyclerView, newState);


        }

        public void onScrolled(RecyclerView recyclerView, int dx, int dy){
            super.onScrolled(recyclerView, dx, dy);


        }

    }

}