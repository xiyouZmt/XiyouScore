package com.zmt.boxin.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.zmt.boxin.Application.App;
import com.zmt.boxin.R;
import com.zmt.boxin.RecyclerAdapter.RecyclerViewAdapter;
import com.zmt.boxin.Utils.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScoreActivity extends AppCompatActivity implements RecyclerViewAdapter.OnItemClickListener{

    private App app;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolBar)
    Toolbar toolbar;
    @BindView(R.id.termRecycler)
    RecyclerView termRecycler;
    RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        initViews();
    }

    public void initViews(){
        app = (App) getApplication();
        ButterKnife.bind(this);
        title.setText(R.string.score);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        adapter = new RecyclerViewAdapter(app.getUser().getTermList());
        adapter.addOnItemClickListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        termRecycler.setLayoutManager(manager);
        /**
         * item间距
         */
        termRecycler.addItemDecoration(new SpaceItemDecoration(
                ScoreActivity.this, SpaceItemDecoration.VERTICAL_LIST));
        termRecycler.setAdapter(adapter);
    }

    @Override
    public void OnItemClick(View view, int position) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_score, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
