package com.minji.librarys.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.minji.librarys.FragmentFactory;
import com.minji.librarys.R;
import com.minji.librarys.widget.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;


public abstract class BasePagerAdapter extends FragmentStatePagerAdapter {

	// private List<Class<?>> mFragmentList = new ArrayList<Class<?>>();
	// private List<Bundle> mBundle = new ArrayList<Bundle>();

	private List<String> mTitle = new ArrayList<String>();
	private Context mContext;
	private PagerSlidingTabStrip mPagerTabs;

	public BasePagerAdapter(FragmentManager fm, Context context,
			PagerSlidingTabStrip mPagerTabs) {
		super(fm);
		this.mContext = context;
		this.mPagerTabs = mPagerTabs;
	}

	// public void setUpdate(Fragment fragment, String title, Bundle bundle) {
	// View v = LayoutInflater.from(mContext).inflate(
	// R.layout.base_viewpage_fragment_tab_item, null, false);
	// TextView textview = (TextView) v.findViewById(R.id.tab_title);
	// textview.setText(title);
	// mPagerTabs.addTab(v);
	// addFragment(fragment);
	// mBundle.add(bundle);
	// }

	public void setUpdate(String title) {
		View v = LayoutInflater.from(mContext).inflate(
				R.layout.base_viewpage_fragment_tab_item, null, false);
		TextView textview = (TextView) v.findViewById(R.id.tab_title);
		textview.setText(title);
		mPagerTabs.addTab(v);
		mTitle.add(title);
		this.notifyDataSetChanged();
	}

	// 调用此方法添加Fragment进入适配器数据源的集合中并刷新适配器
	// public void addFragment(Fragment fragment) {
	// mFragmentList.add(fragment.getClass());
	// this.notifyDataSetChanged();
	// }

	// @Override
	// public int getCount() {
	// return mFragmentList.size();
	// }

	@Override
	public int getCount() {
		return mTitle.size();
	}

	@Override
	public Fragment getItem(int position) {
		// return Fragment.instantiate(mContext, mFragmentList.get(position)
		// .getName(), mBundle.get(position));
		String differentiate = setDifferentiate();
		return FragmentFactory.create(position, differentiate);
	}

	public abstract String setDifferentiate();

	/*
	 * @see
	 * android.support.v4.view.PagerAdapter#getItemPosition(java.lang.Object)
	 * 如果使用的是FragmentStatePagerAdapter, 不需要保存Fragment及位置信息, 返回POSITION_NONE 不重写,
	 * 可能导致界面在切换回来时, 内容为空白
	 */
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
}
