package com.zmt.boxin.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zmt.boxin.Activity.StartActivity;
import com.zmt.boxin.Application.App;
import com.zmt.boxin.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class Courses extends Fragment {

    @BindView(R.id.button) Button button;
    @BindView(R.id.title) TextView title;
    @BindViews({R.id.Monday_one_two, R.id.Tuesday_one_two, R.id.Wednesday_one_two, R.id.Thursday_one_two, R.id.Friday_one_two})
    TextView [] textViews1;
    @BindViews({R.id.Monday_three_four, R.id.Tuesday_three_four, R.id.Wednesday_three_four, R.id.Thursday_three_four, R.id.Friday_three_four})
    TextView [] textViews2;
    @BindViews({R.id.Monday_five_six, R.id.Tuesday_five_six, R.id.Wednesday_five_six, R.id.Thursday_five_six, R.id.Friday_five_six})
    TextView [] textViews3;
    @BindViews({R.id.Monday_seven_eight, R.id.Tuesday_seven_eight, R.id.Wednesday_seven_eight, R.id.Thursday_seven_eight, R.id.Friday_seven_eight})
    TextView [] textViews4;
    @BindViews({R.id.linear_11, R.id.linear_12, R.id.linear_13, R.id.linear_14, R.id.linear_15})
    LinearLayout [] linear1;
    @BindViews({R.id.linear_21, R.id.linear_22, R.id.linear_23, R.id.linear_24, R.id.linear_25})
    LinearLayout [] linear2;
    @BindViews({R.id.linear_31, R.id.linear_32, R.id.linear_33, R.id.linear_34, R.id.linear_35})
    LinearLayout [] linear3;
    @BindViews({R.id.linear_41, R.id.linear_42, R.id.linear_43, R.id.linear_44, R.id.linear_45})
    LinearLayout [] linear4;
    private View view;
    private String course;
    private String [] weeks = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    private int [] colors1 = {R.color.color_039be5, R.color.color_9d55b8, R.color.color_17abe3, R.color.color_ffc400, R.color.color_11de6e};
    private int [] colors2 = {R.color.color_00bb9c, R.color.color_03a9f4, R.color.color_eb8010, R.color.color_39b651, R.color.color_e89abe};
    private App app;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_courses, container, false);
        initViews();
        Document document = Jsoup.parse(course);
        title.setText(getTitle(document));
        setCourses(manageCourses(getCourses(document)));
        return view;
    }

    public void setCourses(List<Map<String, String>> list) {
        for (int i = 0; i < weeks.length; i++) {
            if(list.get(0).get(weeks[i]).length() != 1){
                textViews1[i].setText(list.get(0).get(weeks[i]));
                linear1[i].setBackgroundResource(colors1[i]);
            }
        }
        for (int i = 0; i < weeks.length; i++) {
            if(list.get(1).get(weeks[i]).length() != 1){
                textViews2[i].setText(list.get(1).get(weeks[i]));
                linear2[i].setBackgroundResource(colors2[i]);
            }
        }
        for (int i = 0; i < weeks.length; i++) {
            if(list.get(2).get(weeks[i]).length() != 1){
                textViews3[i].setText(list.get(2).get(weeks[i]));
                linear3[i].setBackgroundResource(colors1[4 - i]);
            }
        }
        for (int i = 0; i < weeks.length; i++) {
            if(list.get(3).get(weeks[i]).length() != 1){
                textViews4[i].setText(list.get(3).get(weeks[i]));
                linear4[i].setBackgroundResource(colors2[4 - i]);
            }
        }
    }

    public List<Map<String, String>> manageCourses(List<Map<String, String>> list) {
        for (int i = 0; i < list.size(); i++) {
            for (String week1 : weeks) {
                String week = list.get(i).get(week1);
                Log.e("week--->", "前" + week + "后");
                if (week.length() != 1) {
                    String name = week.substring(0, week.indexOf("周"));
                    String teacher = week.substring(week.lastIndexOf('}') + 2, week.lastIndexOf(' '));
                    String classroom = week.substring(week.lastIndexOf(' ') + 1, week.length());
                    Log.e("i--->", String.valueOf(i));
                    Log.e("week1--->", week1);
                    Log.e("week--->", name + "\n" + teacher + '\n' + classroom);
                    list.get(i).put(week1, name + "\n" + teacher + "\n@" + classroom);
                }
            }
        }
        return list;
    }

    public List<Map<String, String>> getCourses(Document document) {
        List<Map<String, String>> list = new ArrayList<>();
        Element element = document.getElementById("Table1");
        Elements elements = element.getElementsByTag("tr");
        /**
         * 从下标为2的节点开始， 每两个节点获取一次课表
         */
        for (int i = 2; i < elements.size(); i += 2) {
            /**
             * 得到每天对应节数的课表集合
             */
            Elements mElements = elements.get(i).getElementsByAttributeValue("align", "Center");
            Map<String, String> map = new HashMap<>();
            for (int j = 0; j < weeks.length; j++) {
                map.put(weeks[j], mElements.get(j).text());
                Log.e("weeks--->", mElements.get(j).text());
                Log.e("weeksLength--->", String.valueOf(mElements.get(j).text().length()));
            }
            list.add(map);
        }
        return list;
    }

    public String getTitle(Document document) {
        /**
         * 标题
         */
        StringBuilder title = new StringBuilder();
        Element title_Element = document.getElementsByAttributeValue("align", "center").get(0);
        for (Element element : title_Element.getAllElements()) {
            if (element.hasAttr("selected") && element.tag().toString().equals("option")) {
                title.append(element.text());
                continue;
            }
            if (element.hasAttr("id") && element.tag().toString().equals("span")) {
                title.append(element.text());
            }
        }
        return title.toString();
    }

    public void getPersonalMessage(Document document) {
        String number = document.getElementById("Label5").text();
        String name = document.getElementById("Label6").text();
        String colleague = document.getElementById("Label7").text();
        String major = document.getElementById("Label8").text();
        String classes = document.getElementById("Label9").text();
        app.getUser().setName(name);
        app.getUser().setNumber(number);
        app.getUser().setColleague(colleague);
        app.getUser().setMajor(major);
        app.getUser().setClasses(classes);
    }

    public void initViews() {
        ButterKnife.bind(this, view);
        app = (App)getActivity().getApplication();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StartActivity.class);
                startActivity(intent);
            }
        });
        Intent intent;
        if ((intent = getActivity().getIntent()) != null) {
            course = intent.getStringExtra("content");
        }
    }

}
