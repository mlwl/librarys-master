package com.minji.librarys.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.minji.librarys.StringsFiled;
import com.minji.librarys.base.BaseFragment;
import com.minji.librarys.base.ContentPage;

import java.util.ArrayList;

/**
 * Created by user on 2016/8/4.
 */
public class FragmentSelectArea extends BaseFragment<String> {


    private int specificFloor;
    private int mFirst = 1;
    private int mSecond = 2;
    private int mThrid = 3;
    private int mFourth = 4;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            specificFloor = bundle.getInt(StringsFiled.SPECIFIC_FLOOR);
        }
    }

    @Override
    protected void onSubClassOnCreateView() {
        loadDataAndRefresh();
    }

    @Override
    protected View onCreateSuccessView() {
        return null;
    }

    @Override
    protected ContentPage.ResultState onLoad() {

        if (specificFloor == mFirst) {
            ArrayList<String> strings = new ArrayList<>();
            return chat(strings);
        } else if (specificFloor == mSecond) {
            ArrayList<String> strings = new ArrayList<>();
            return chat(strings);
        } else if (specificFloor == mThrid) {
            ArrayList<String> strings = new ArrayList<>();
            return chat(strings);
        } else if (specificFloor == mFourth) {
            ArrayList<String> strings = new ArrayList<>();
            return chat(strings);
        } else {
            return chat(null);
        }
    }
}
