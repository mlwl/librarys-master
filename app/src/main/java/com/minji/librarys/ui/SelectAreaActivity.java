package com.minji.librarys.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.minji.librarys.R;
import com.minji.librarys.StringsFiled;
import com.minji.librarys.base.BaseActivity;
import com.minji.librarys.fragment.FragmentSelectArea;

import java.util.ArrayList;
import java.util.List;

public class SelectAreaActivity extends BaseActivity implements View.OnClickListener {

    private TextView mFirstFloor;
    private TextView mSecondFloor;
    private TextView mThridFloor;
    private TextView mFourthFloor;
    private List<TextView> mTextViews;

    private int mSelectTab = 1;// 一开始就为1是因为第一个选项卡就是第一个
    private FragmentSelectArea mFirstFragment;
    private FragmentSelectArea mSecondFragment;
    private FragmentSelectArea mThridFragment;
    private FragmentSelectArea mFourthFragment;
    /**
     * 当前界面显示的Fragment
     */
    private FragmentSelectArea mContentFragment;
    /**
     * 当跳转Fragment时需要隐藏的上一个Fragment (其实就是点击Tab时的Fragment)
     */
    private FragmentSelectArea mFromFragment;

    @Override
    public void onCreateContent() {

        // 对标题栏进行设置
        setBackVisibility(View.VISIBLE);
        setSettingVisibility(View.VISIBLE);
        setTitleVisibility(View.VISIBLE);
        setTitleImageVisibility(View.GONE);

        View content = setContent(R.layout.activity_select_area);

        initView(content);

        amendClickStyle(1);// 一开就设置第一个Tab的变形

        showFragmentWhenFirstOrSecond();
    }

    /*
    *   此方法中对第一次进入界面直接展示一个Fragment
    *   当activity销毁时且数据保存了，那么则从FragmentManger中取出一存储的Fragment
    * */
    private void showFragmentWhenFirstOrSecond() {
        if (savedInstanceState == null) {// 第一次进入该界面，需要通过add来进行展示Fragment
            System.out.println("第一次进入该界面，需要通过add来进行展示Fragment");
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            Bundle bundle = new Bundle();
            bundle.putInt(StringsFiled.SPECIFIC_FLOOR, 1);


            mFirstFragment = new FragmentSelectArea();
            mFirstFragment.setArguments(bundle);

            fragmentTransaction.add(R.id.fl_select_area_show_home, mFirstFragment, "first").commit();

            mContentFragment = mFirstFragment;// 第一次添加Fragment就该说明此时显示的Fragment是哪个
            mFromFragment = mFirstFragment;
        } else {
            System.out.println("第二次+++++++++++++++++++++++++++++");
            mFirstFragment = (FragmentSelectArea) getSupportFragmentManager().findFragmentByTag("first");
            mSecondFragment = (FragmentSelectArea) getSupportFragmentManager().findFragmentByTag("second");
            mThridFragment = (FragmentSelectArea) getSupportFragmentManager().findFragmentByTag("thrid");
            mFourthFragment = (FragmentSelectArea) getSupportFragmentManager().findFragmentByTag("fourth");

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            if (mFirstFragment != null) {
                fragmentTransaction.show(mFirstFragment);
                mContentFragment = mFirstFragment;// 说明此时显示的Fragment是哪个
                mFromFragment = mFirstFragment;
            }
            if (mSecondFragment != null) {
                fragmentTransaction.hide(mSecondFragment);
            }
            if (mThridFragment != null) {
                fragmentTransaction.hide(mThridFragment);
            }
            if (mFourthFragment != null) {
                fragmentTransaction.hide(mFourthFragment);
            }
            fragmentTransaction.commit();
        }
    }

    private void initView(View content) {
        mTextViews = new ArrayList<>();

        mFirstFloor = (TextView) content.findViewById(R.id.tv_select_area_first);
        mFirstFloor.setOnClickListener(this);
        mTextViews.add(mFirstFloor);
        mSecondFloor = (TextView) content.findViewById(R.id.tv_select_area_second);
        mSecondFloor.setOnClickListener(this);
        mTextViews.add(mSecondFloor);
        mThridFloor = (TextView) content.findViewById(R.id.tv_select_area_thrid);
        mThridFloor.setOnClickListener(this);
        mTextViews.add(mThridFloor);
        mFourthFloor = (TextView) content.findViewById(R.id.tv_select_area_fourth);
        mFourthFloor.setOnClickListener(this);
        mTextViews.add(mFourthFloor);



    }

    private int middle = 0;
    private FragmentSelectArea mTo = null;
    private String mTag = "";

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_select_area_first:
                setSelectTabValue(1, "first", mFirstFragment);
                break;
            case R.id.tv_select_area_second:
                setSelectTabValue(2, "second", mSecondFragment);
                break;
            case R.id.tv_select_area_thrid:
                setSelectTabValue(3, "thrid", mThridFragment);
                break;
            case R.id.tv_select_area_fourth:
                setSelectTabValue(4, "fourth", mFourthFragment);
                break;
        }
        if (middle != 0) {// 判断是否有按Tab
            if (mSelectTab != middle) {// 判断所按是否已经被按了
                amendClickStyle(middle);
                startSelectAreaContent(middle, mTo, mTag);
                mSelectTab = middle;
            }
        }
    }

    /*设置add跳转fragment时的 tag 和 toFragment*/
    private void setSelectTabValue(int middle, String tag, FragmentSelectArea fragmentSelectArea) {
        this.middle = middle;
        if (fragmentSelectArea == null) {
            fragmentSelectArea = new FragmentSelectArea();
        }
        mTo = fragmentSelectArea;
        mTag = tag;
    }

    /**
     * click 为被点击的选项卡
     */
    private void amendClickStyle(int click) {
        for (int i = 1; i < mTextViews.size() + 1; i++) {
            TextView view = mTextViews.get(i - 1);
            if (i == click) {
                view.setTextColor(ContextCompat.getColor(SelectAreaActivity.this, R.color.thin_green));
                view.setBackgroundResource(R.drawable.tabs_background_press);
            } else {
                view.setTextColor(ContextCompat.getColor(SelectAreaActivity.this, R.color.white));
                view.setBackgroundResource(R.drawable.tabs_background_default);
            }
        }
    }

    // 开启具体楼层的Fragment
    private void startSelectAreaContent(int selectFloor, FragmentSelectArea to, String tag) {

        if (mContentFragment != to) {
            mContentFragment = to;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            if (!to.isAdded()) {    // 先判断是否被add过
                Bundle bundle = new Bundle();
                bundle.putInt(StringsFiled.SPECIFIC_FLOOR, selectFloor);

                to.setArguments(bundle);

                transaction.hide(mFromFragment).add(R.id.fl_select_area_show_home, to, tag).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(mFromFragment).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
            mFromFragment = to;
        }
    }
}
