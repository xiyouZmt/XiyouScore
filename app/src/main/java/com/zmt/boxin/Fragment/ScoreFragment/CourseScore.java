package com.zmt.boxin.Fragment.ScoreFragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zmt.boxin.Activity.ScoreActivity;
import com.zmt.boxin.Adapter.PhysicalTestAdapter;
import com.zmt.boxin.Adapter.ScoreAdapter;
import com.zmt.boxin.Application.App;
import com.zmt.boxin.NetworkThread.PhysicalTestItemThread;
import com.zmt.boxin.NetworkThread.PhysicalTestThread;
import com.zmt.boxin.NetworkThread.ScoreThread;
import com.zmt.boxin.NetworkThread.TermThread;
import com.zmt.boxin.R;
import com.zmt.boxin.Utils.RequestUrl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CourseScore extends Fragment {

    public  static final String PHYSICAL_TEST_ITEM_POSITION = "physical test item position";
    private String TAG = "";
    @BindView(R.id.currentTerm)
    TextView currentTerm;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private App app;
    private View view;
    private ProgressDialog progressDialog;
    private RequestUrl url;
    private String [] tabName;
    private String year;
    private int term;
    public  static int position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_course_score, container, false);
        initViews();
        if(TAG.equals(tabName[0])){
            progressDialog.show();
            url = new RequestUrl(app.getUser().getName(), app.getUser().getNumber());
            TermThread termThread = new TermThread(url.getScoreUrl(), handler, app);
            Thread t = new Thread(termThread, "scoreThread");
            t.start();
        } else {
            url = new RequestUrl();
            PhysicalTestThread physicalTest = new PhysicalTestThread(app, url.getPhysicalTest(), 123 + "", handler);
            physicalTest.start();
        }
        return view;
    }

    public final Handler handler = new Handler(){
        public void handleMessage(Message msg){
            Intent intent = new Intent();
            switch (msg.obj.toString()){
                case "fail" :
                case "error" :
                    Snackbar.make(view, "网络连接错误, 请检查网络连接", Snackbar.LENGTH_SHORT).show();
                    break;
                case "no evaluate" :
                    Toast.makeText(getActivity(), "你还没有对本学期的课程进行评价, 请先评价", Toast.LENGTH_SHORT).show();
                    intent.setClass(getActivity(), com.zmt.boxin.Activity.WebView.class);
                    startActivity(intent);
                case "null" :
                    /**
                     * 暂无成绩(目测大一同学)
                     */
                    Snackbar.make(view, "同学, 你还没有考过试", Snackbar.LENGTH_SHORT).show();
                    break;
                case PhysicalTestThread.PHYSICAL_TEST_NULL :
                    /**
                     * 体测成绩暂无(目测又一大一同学)
                     */
                    Snackbar.make(view, "同学, 你还没有体测过", Snackbar.LENGTH_SHORT).show();
                    break;
                case PhysicalTestItemThread.PHYSICAL_TEST_ITEM_NULL :
                    /**
                     * 单项成绩暂无
                     */
                    Snackbar.make(view, "同学, 单项成绩暂无，请查看其它学期成绩", Snackbar.LENGTH_SHORT).show();
                    break;
                case PhysicalTestItemThread.PHYSICAL_TEST_ITEM_OK :
                    /**
                     * 显示体测成绩
                     */
                    PhysicalTestAdapter itemAdapter = new PhysicalTestAdapter(app.getUser().getPhysicalTestItem());
                    recyclerView.setAdapter(itemAdapter);

                    year = app.getUser().getPhysicalTest().get(position).getYear();
                    String totalScore = app.getUser().getPhysicalTest().get(position).getTotalScore();
                    currentTerm.setText(year + "年体测成绩: " + totalScore);
                    break;
                default :
                    /**
                     * 显示学习成绩
                     */
                    ScoreAdapter adapter = new ScoreAdapter(app.getUser().getScoreList());
                    recyclerView.setAdapter(adapter);
                    year = msg.obj.toString();
                    term = msg.what;
                    Bundle bundle = new Bundle();
                    bundle.putString(ScoreActivity.YEAR, year);
                    bundle.putInt(ScoreActivity.TERM, term);
                    app.getUser().setScoreYear(year);
                    app.getUser().setScoreTerm(term);
                    currentTerm.setText(year + "学年第" + term + "学期成绩");
                    break;
            }
            progressDialog.dismiss();
        }
    };

    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onMessageEvent(Bundle bundle) {
        String msg = "onEventMainThread收到了消息";
        Log.d("message", msg);
        if(TAG.equals(tabName[0])){
            year = bundle.getString(ScoreActivity.YEAR);
            term = bundle.getInt(ScoreActivity.TERM);
            url = new RequestUrl(app.getUser().getName(), app.getUser().getNumber());
            progressDialog.show();
            ScoreThread defaultScore = new ScoreThread(app, url.getScoreUrl(), year, term + "", handler);
            Thread t = new Thread(defaultScore, "ScoreThread");
            t.start();
        } else {
            String meaScoreId = bundle.getString("meaScoreId");
            position = bundle.getInt(PHYSICAL_TEST_ITEM_POSITION);
            if(meaScoreId != null && !meaScoreId.equals("")){
                url = new RequestUrl();
                PhysicalTestItemThread physicalTest = new PhysicalTestItemThread(app, url.getPhysicalTestItem(), meaScoreId, handler);
                physicalTest.start();
            }
        }
    }

    public void initViews(){
        ButterKnife.bind(this, view);
        app = (App) getActivity().getApplication();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("正在加载...");
        progressDialog.setCancelable(false);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        Bundle bundle;
        if((bundle = getArguments()) != null){
            TAG = bundle.getString(ScoreActivity.SCORE_TYPE);
        }
        tabName = getResources().getStringArray(R.array.score);
        position = ScoreThread.DEFAULT_YEAR_POSITION * 2 + 2 - ScoreThread.DEFAULT_TERM_POSITION;
    }
}
