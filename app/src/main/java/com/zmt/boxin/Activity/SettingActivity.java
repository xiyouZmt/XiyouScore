package com.zmt.boxin.Activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zmt.boxin.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.clean) RelativeLayout clean;
    @BindView(R.id.update) RelativeLayout update;
    @BindView(R.id.suggestion) RelativeLayout suggestion;
    @BindView(R.id.Boxin) RelativeLayout Boxin;
    @BindView(R.id.about) RelativeLayout about;
    @BindView(R.id.mail) TextView mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        title.setText(R.string.action_settings);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick({R.id.clean, R.id.update, R.id.suggestion, R.id.Boxin, R.id.about, R.id.mail})
    public void onClick(View v){
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.clean :
                break;
            case R.id.update :

                break;
            case R.id.suggestion :
                intent.setClass(SettingActivity.this, SuggestActivity.class);
                startActivity(intent);
                break;
            case R.id.Boxin :
                intent.setClass(this, BoxinActivity.class);
                startActivity(intent);
                break;
            case R.id.about :
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.xiyoumobile.com"));
                startActivity(intent);
                break;
            case R.id.mail :
                intent.setAction(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:zhumintao@xiyou3g.com"));
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
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
