package com.zmt.boxin.Fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zmt.boxin.Activity.MessageActivity;
import com.zmt.boxin.Application.App;
import com.zmt.boxin.NetworkThread.SaveImage;
import com.zmt.boxin.NetworkThread.TermThread;
import com.zmt.boxin.R;
import com.zmt.boxin.Activity.ScoreActivity;
import com.zmt.boxin.Utils.CircleImageView;
import com.zmt.boxin.Utils.RequestUrl;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class Mine extends android.support.v4.app.Fragment {

    private App app;
    private View view;
    private String imagePath = "";
    private ProgressDialog progressDialog;
    @BindView(R.id.personal) RelativeLayout personal;
    @BindView(R.id.myImage) CircleImageView myImage;
    @BindView(R.id.student_name) TextView student_name;
    @BindView(R.id.school) TextView school;
    @BindView(R.id.colleague) TextView colleague;
    @BindView(R.id.sex) ImageView sex;
    @BindView(R.id.QR_code) ImageView QR_code;
    @BindView(R.id.run_note) RelativeLayout run_note;
    @BindView(R.id.score) RelativeLayout score;
    @BindView(R.id.coordinatorLayout)CoordinatorLayout coordinatorLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, container, false);
        initViews();
        return view;
    }

    public final Handler handler = new Handler(){
        public void handleMessage(Message msg){
            Intent intent = new Intent();
            switch (msg.obj.toString()){
                case "fail" :
                case "error" :
                    progressDialog.dismiss();
                    Snackbar.make(coordinatorLayout, "网络连接错误, 请检查网络连接", Snackbar.LENGTH_SHORT).show();
                    break;
                case "no evaluate" :
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "你还没有对本学期的课程进行评价, 请先评价", Toast.LENGTH_SHORT).show();
                    intent.setClass(getActivity(), com.zmt.boxin.Activity.WebView.class);
                    startActivity(intent);
                case "image is ok" :
                    Object object;
                    if((object = msg.getData().get("path")) != null){
                        imagePath = object.toString();
                        Bitmap bitmap = BitmapFactory.decodeFile(object.toString());
                        myImage.setImageBitmap(bitmap);
                    }
                    break;
                case "score is ok" :
                    Bundle bundle =  msg.getData();
                    if(bundle != null){
                        for (int i = 0; i < bundle.size(); i++) {
                            Object term;
                            if((term = bundle.get("year" + i)) != null){
                                String term2 = term + "学年第2学期学习成绩";
                                String term1 = term + "学年第1学期学习成绩";
                                app.getUser().getTermList().add(term2);
                                app.getUser().getTermList().add(term1);
                            }
                        }
                    }
                    intent.setClass(getActivity(), ScoreActivity.class);
                    startActivity(intent);
                    progressDialog.dismiss();
                    break;
            }
        }
    };

    @OnClick({R.id.personal, R.id.QR_code, R.id.run_note, R.id.score})
    public void onClick(View v){
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.personal :
                intent.setClass(getActivity(), MessageActivity.class);
                intent.putExtra("imagePath", imagePath);
                startActivity(intent);
                break;
            case R.id.QR_code :
                /**
                 * dialog
                 */
                break;
            case R.id.run_note :
                break;
            case R.id.score :
                if(app.getUser().getTermList().size() == 0 ||
                        app.getUser().getScoreList().size() == 0) {
                    progressDialog.show();
                    RequestUrl url = new RequestUrl(app.getUser().getName(), app.getUser().getNumber());
                    TermThread scoreThread = new TermThread(url.getScoreUrl(), handler, app);
                    Thread t = new Thread(scoreThread, "scoreThread");
                    t.start();
                } else {
                    intent.setClass(getActivity(), ScoreActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    public void initViews(){
        app = (App) getActivity().getApplication();
        ButterKnife.bind(this, view);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("正在加载...");
        progressDialog.setCancelable(false);
        student_name.setText(app.getUser().getName());
        colleague.setText(app.getUser().getColleague());
        if(app.getUser().getSex().equals("男")){
            sex.setBackgroundResource(R.mipmap.male);
        } else {
            sex.setBackgroundResource(R.mipmap.female);
        }
        /**
         * 获取头像
         */
        SaveImage saveImage = new SaveImage(app, handler);
        Thread t = new Thread(saveImage, "SaveImage");
        t.start();
    }

}
