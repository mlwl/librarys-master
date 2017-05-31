package com.minji.librarys.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class MyFrameLayout extends FrameLayout {

	private boolean mIsInterrupt;

	public MyFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public MyFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mIsInterrupt = false;
	}

	public MyFrameLayout(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (mIsInterrupt) {
			return true;
		} else {
			return super.onInterceptTouchEvent(ev);
		}
	}

	public void setIsInterruptTouch(boolean mIsInterrupt) {
		this.mIsInterrupt = mIsInterrupt;
	}
}
