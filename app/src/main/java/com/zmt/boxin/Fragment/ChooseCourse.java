package com.zmt.boxin.Fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zmt.boxin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseCourse extends android.support.v4.app.Fragment {


    public ChooseCourse() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_course, container, false);
    }


}
