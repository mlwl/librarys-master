package com.minji.librarys.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.minji.librarys.R;
import com.minji.librarys.widget.PagerSlidingTabStrip;


public abstract class BaseViewPagerFragment extends Fragment {

	public ViewPager mViewPager;
	public PagerSlidingTabStrip mPagerTabs;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_most, null);
		mPagerTabs = (PagerSlidingTabStrip) view
				.findViewById(R.id.pager_tabstrip);
		mViewPager = (ViewPager) view.findViewById(R.id.pager);
		// 获取适配器
		BasePagerAdapter basePagerAdapter = new BasePagerAdapter(
				getChildFragmentManager(), getActivity(), mPagerTabs) {
			@Override
			public String setDifferentiate() {
				return BaseViewPagerFragment.this.setDifferentiate();
			}
		};
		mViewPager.setAdapter(basePagerAdapter);
		// 添加标题和内容Fragment
		setupAdapter(basePagerAdapter);
		// 绑定
		mPagerTabs.setViewPager(mViewPager);

		onSubClassOnCreateView();

		return view;
	}

	/**
	 * 使用传入的适配器的setUpdate方法添加ViewPager中的各个Fragment
	 * 
	 * @param adapter
	 */
	public abstract void setupAdapter(BasePagerAdapter adapter);

	protected Bundle getBundle(String arg) {
		Bundle bundle = new Bundle();
		bundle.putString("key", arg);
		return bundle;
	}

	protected abstract void onSubClassOnCreateView();

	protected abstract String setDifferentiate();

}
