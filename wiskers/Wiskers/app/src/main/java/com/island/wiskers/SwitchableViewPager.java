package com.island.wiskers;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Island on 01/08/15.
 */
public class SwitchableViewPager extends ViewPager
{
	public boolean Active=true;
	public SwitchableViewPager(Context context)
	{
		super(context);
	}
	public SwitchableViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		if(Active)
			return super.onInterceptTouchEvent(ev);
		else
			return false;
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		if(Active)
			return super.onTouchEvent(ev);
		else
			return false;
	}
}

