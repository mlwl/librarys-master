package com.minji.librarys.fragment;

import android.support.v4.view.ViewPager;

import com.minji.librarys.FragmentFactory;
import com.minji.librarys.base.BaseFragment;
import com.minji.librarys.base.BasePagerAdapter;
import com.minji.librarys.base.BaseViewPagerFragment;

/**
 * Created by user on 2016/8/29.
 */
public class FragmentStatementDetail extends BaseViewPagerFragment {
    @Override
    public void setupAdapter(BasePagerAdapter adapter) {
        adapter.setUpdate("预约入馆统计");
        adapter.setUpdate("占有率统计");
    }

    @Override
    protected void onSubClassOnCreateView() {
        mPagerTabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                // 加载网络
                BaseFragment baseFragment = FragmentFactory.fragments[position];
//                baseFragment.loadDataAndRefresh();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 为了在FragmentFactory中进行区分
     */
    @Override
    protected String setDifferentiate() {
        return "StatementDetail";
    }
}
