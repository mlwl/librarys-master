package com.minji.librarys.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.minji.librarys.R;
import com.minji.librarys.StringsFiled;
import com.minji.librarys.uitls.StringUtils;
import com.minji.librarys.widget.MyFrameLayout;

public abstract class BaseActivity extends FragmentActivity {

    private ImageView mBaseTitleImage;
    private ImageView mBaseBack;
    private ImageView mBaseSetting;
    private ImageView mBaseSelect;

    private TextView mBaseTitle;

    private MyFrameLayout mBaseContetn;
    public Bundle savedInstanceState;
    public String stringTitle;
    private FrameLayout mBaseLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);

        this.savedInstanceState = savedInstanceState;

        initView();

        setTitleText();

        onCreateContent();

    }

    private void setTitleText() {
        Intent intentTitle = getIntent();
        if (intentTitle != null) {
            stringTitle = intentTitle.getStringExtra(StringsFiled.ACTIVITY_TITLE);
            if (!StringUtils.isEmpty(stringTitle)) {
                mBaseTitle.setText(stringTitle);
            }
        }
    }

    public abstract void onCreateContent();

    private void initView() {
        mBaseTitleImage = (ImageView) findViewById(R.id.iv_title_image);
        mBaseSelect = (ImageView) findViewById(R.id.iv_title_select);
        mBaseSetting = (ImageView) findViewById(R.id.iv_title_setting);
        mBaseTitle = (TextView) findViewById(R.id.tv_base_title);
        mBaseContetn = (MyFrameLayout) findViewById(R.id.fl_base_content);

        mBaseBack = (ImageView) findViewById(R.id.iv_title_back);
        mBaseBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBaseLoading = (FrameLayout) findViewById(R.id.fl_loading);
    }

    public void setLoadingVisibility(int visibility) {
        mBaseLoading.setVisibility(visibility);
    }

    public void setIsInterruptTouch(boolean is) {
        mBaseContetn.setIsInterruptTouch(is);
    }

    public void setBackVisibility(int visibility) {
        mBaseBack.setVisibility(visibility);
    }

    public void setTitleImageVisibility(int visibility) {
        mBaseTitleImage.setVisibility(visibility);
    }


    public void setTitleVisibility(int visibility) {
        mBaseTitle.setVisibility(visibility);
    }

    public void setSettingVisibility(int visibility) {
        mBaseSetting.setVisibility(visibility);
    }

    public ImageView getSelectVisibility() {
        return mBaseSelect;
    }


    /**
     * 首先将一个xml布局打气压缩成一个View，在将该View添加到Framelayout中
     */
    public View setContent(int id) {
        View inflate = View.inflate(MyApplication.getContext(), id, null);
        mBaseContetn.addView(inflate);
        return inflate;
    }


}
