package com.tyl.framework.widget.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * © 2012 amsoft.cn
 * 名称：AbFragmentPagerAdapter.java 
 */
public class TTFragmentPagerAdapter extends FragmentPagerAdapter {

	/** The m fragment list. */
	private ArrayList<Fragment> mFragmentList = null;
	
	public void clreaFragmentList(){
		if(mFragmentList!=null){
			mFragmentList.clear();
		}
		this.notifyDataSetChanged();
		
	}

	/**
	 * Instantiates a new ab fragment pager adapter.
	 * @param mFragmentManager the m fragment manager
	 * @param fragmentList the fragment list
	 */
	public TTFragmentPagerAdapter(FragmentManager mFragmentManager,ArrayList<Fragment> fragmentList) {
		super(mFragmentManager);
		mFragmentList = fragmentList;
	}

	/**
	 * 描述：获取数量.
	 *
	 * @return the count
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return mFragmentList.size();
	}

	/**
	 * 描述：获取索引位置的Fragment.
	 *
	 * @param position the position
	 * @return the item
	 * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
	 */
	@Override
	public Fragment getItem(int position) {

		Fragment fragment = null;
		if (position < mFragmentList.size()){
			fragment = mFragmentList.get(position);
		}else{
			fragment = mFragmentList.get(0);
		}
		return fragment;

	}
}
