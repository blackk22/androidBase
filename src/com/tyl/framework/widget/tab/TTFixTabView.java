package com.tyl.framework.widget.tab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.tyl.framework.log.L;
import com.tyl.framework.widget.adapter.TTFragmentPagerAdapter;



/**
 * 名称：FTFixTabView.java 
 * 描述tab固定屏幕内.
 */
public class TTFixTabView extends LinearLayout {
	
	/** The context. */
	private Context context;
	
	/** tab的线性布局. */
	private LinearLayout mTabLayout = null;
	
	/** The m view pager. */
	private ViewPager mViewPager;
	
	/** The m listener. */
	private ViewPager.OnPageChangeListener mListener;
	
	/** tab的列表. */
	private ArrayList<TTTabItemView> tabItemList = null;
	
	private int tabItemViewWith=LayoutParams.WRAP_CONTENT;
	
	/** 内容的View. */
	private ArrayList<Fragment> pagerItemList = null;
	
	/** tab的文字. */
	private List<String> tabItemTextList = null;
	
	/** tab的背景图标. */
	private List<Drawable> tabItemDrawableList = null;
	
	/** 当前选中编号. */
	private int mSelectedTabIndex = 0;
	
	/** 内容区域的适配器. */
	private TTFragmentPagerAdapter mFragmentPagerAdapter = null;

    
	/** tab的文字大小. */
	private int tabTextSize = 30;
	
	/** tab的文字颜色. */
	private int tabTextColor = Color.BLACK;
	
	/** tab的选中文字颜色. */
	private int tabSelectColor = Color.WHITE;
	
	/** The m tab click listener. */
	private OnClickListener mTabClickListener = new OnClickListener() {
        public void onClick(View view) {
        	TTTabItemView tabView = (TTTabItemView)view;
            setCurrentItem(tabView.getIndex());
        }
    };
	
	
	/**
	 * Instantiates a new ab bottom tab view.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public TTFixTabView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//System.out.println("新建 FTFixTabView "+this);
		this.context = context;
		
		this.setOrientation(LinearLayout.VERTICAL);
		this.setBackgroundColor(Color.rgb(255, 255, 255));
		
		mTabLayout = new LinearLayout(context);
		mTabLayout.setOrientation(LinearLayout.HORIZONTAL);
		mTabLayout.setGravity(Gravity.CENTER);
		
		//内容的View的适配
		mViewPager = new ViewPager(context);
		//手动创建的ViewPager,必须调用setId()方法设置一个id
		mViewPager.setId(1985);
		pagerItemList = new ArrayList<Fragment>();
		
		LinearLayout.LayoutParams layout_ww=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		layout_ww.gravity=Gravity.CENTER_HORIZONTAL;
		
		this.addView(mTabLayout,layout_ww);
		this.addView(mViewPager,new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,0,1));
		
		
		//定义Tab栏
  		tabItemList = new ArrayList<TTTabItemView>();
  		tabItemTextList = new ArrayList<String>();
  		tabItemDrawableList = new ArrayList<Drawable>();
		//要求必须是FragmentActivity的实例
		if(!(this.context instanceof FragmentActivity)){
			L.e(TTBottomTabView.class, "构造AbSlidingTabView的参数context,必须是FragmentActivity的实例。");
		}
		
		FragmentManager mFragmentManager =((FragmentActivity)this.context).getSupportFragmentManager();
		mFragmentPagerAdapter = new TTFragmentPagerAdapter(mFragmentManager, pagerItemList);
		mViewPager.setAdapter(mFragmentPagerAdapter);
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mViewPager.setOffscreenPageLimit(3);
		
	}

	
	
	/**
	 * The listener interface for receiving myOnPageChange events.
	 * The class that is interested in processing a myOnPageChange
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addMyOnPageChangeListener<code> method. When
	 * the myOnPageChange event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see MyOnPageChangeEvent
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener{

		/* (non-Javadoc)
		 * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrollStateChanged(int)
		 */
		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		/* (non-Javadoc)
		 * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled(int, float, int)
		 */
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}

		/* (non-Javadoc)
		 * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected(int)
		 */
		@Override
		public void onPageSelected(int arg0) {
			setCurrentItem(arg0);
		}
		
	}
	
	/**
	 * 描述：设置显示哪一个.
	 *
	 * @param index the new current item
	 */
    public void setCurrentItem(int index) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        mSelectedTabIndex = index;
        final int tabCount = mTabLayout.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            final TTTabItemView child = (TTTabItemView)mTabLayout.getChildAt(i);
            final boolean isSelected = (i == index);
            child.setSelected(isSelected);
            if (isSelected) {
            	//设置选中情况 文字颜色
            	child.setTabTextColor(tabSelectColor);
            	//设置背景
            	if(tabItemDrawableList.size() >= tabCount*2){
            		child.setTabTextBackgroundDrawable(tabItemDrawableList.get(index*2+1));
             	}
            	mViewPager.setCurrentItem(index);
            }else{
            	//非选中情况
            	if(tabItemDrawableList.size() >= tabCount*2){
            		child.setTabTextBackgroundDrawable(tabItemDrawableList.get(i*2));
            	}
            	child.setTabTextColor(tabTextColor);
            }
        }
    }
    
    /**
     * 描述：设置一个外部的监听器.
     *
     * @param listener the new on page change listener
     */
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mListener = listener;
    }
    
	/* (non-Javadoc)
	 * @see android.widget.LinearLayout#onMeasure(int, int)
	 */
	@Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    	

	/**
	 * Gets the tab text size.
	 *
	 * @return the tab text size
	 */
	public int getTabTextSize() {
		return tabTextSize;
	}

	/**
	 * Sets the tab text size.
	 *
	 * @param tabTextSize the new tab text size
	 */
	public void setTabTextSize(int tabTextSize) {
		this.tabTextSize = tabTextSize;
	}
	
	/**
	 * 获得每个tab选项宽度方式
	 * @return
	 */
	public int getTabItemViewWith() {
		return tabItemViewWith;
	}

	/**
	 * 设置每个选项卡的宽度方式,此方法必须在addItemViews方法之前调用
	 * @param tabItemViewWith  宽度值 默认 为LayoutParams.WRAP_CONTENT; 0表示平分、 大于0就按设置大小。
	 */
	public void setTabItemViewWith(int tabItemViewWith) {
		this.tabItemViewWith = tabItemViewWith;
	}

	/**
	 * 描述：设置tab默认文字的颜色.
	 *
	 * @param tabColor the new tab text color
	 */
	public void setTabTextColor(int tabColor) {
		this.tabTextColor = tabColor;
	}

	/**
	 * 描述：设置选中字体的颜色.
	 *
	 * @param tabColor the new tab select color
	 */
	public void setTabSelectColor(int tabColor) {
		this.tabSelectColor = tabColor;
	}
    
	 /**
     * 描述：创造一个Tab.
     *
     * @param text the text
     * @param index the index
     */
    private void addTab(String text, int index) {
    	addTab(text,index,null);
    }
    
    /**
     * 描述：创造一个Tab.
     *
     * @param text the text
     * @param index the index
     * @param top the top
     */
    private void addTab(String text, int index,Drawable bg) {
   	
    	TTTabItemView tabView = new TTTabItemView(this.context);
    	//System.out.println("tabItemViewWith:"+tabItemViewWith);
    	if(tabItemViewWith==LayoutParams.WRAP_CONTENT){
    		tabView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    	}else if(tabItemViewWith==0){
    		tabView.setLayoutParams(new LinearLayout.LayoutParams(0,LayoutParams.WRAP_CONTENT, 1));
    	}else if(tabItemViewWith>0){
    		tabView.setLayoutParams(new LinearLayout.LayoutParams(tabItemViewWith,LayoutParams.WRAP_CONTENT));
    	}
    	
    	if(bg!=null){
        	tabView.setTabTextBackgroundDrawable(bg);
        }
    	tabView.setTabTextColor(tabTextColor);
    	tabView.setTabTextSize(tabTextSize);
        
        tabView.init(index,text);
        
        tabItemList.add(tabView);
        tabView.setOnClickListener(mTabClickListener);
        //添加tab也
        mTabLayout.addView(tabView);
    }
    
    /**
     * 描述：tab有变化刷新.
     */
    public void notifyTabDataSetChanged() {
        mTabLayout.removeAllViews();
        tabItemList.clear();
        
        final int count = mFragmentPagerAdapter.getCount();
        
        for (int i = 0; i < count; i++) {
        	if(tabItemDrawableList.size()>=count*2){
        		addTab(tabItemTextList.get(i), i,tabItemDrawableList.get(i*2));
        	}else if(tabItemDrawableList.size()>=count){
        		addTab(tabItemTextList.get(i), i,tabItemDrawableList.get(i));
        	}else{
        		addTab(tabItemTextList.get(i), i);
        	}
        }
        if (mSelectedTabIndex > count) {
            mSelectedTabIndex = count - 1;
        }
        setCurrentItem(mSelectedTabIndex);
        requestLayout();
    }
	
	

	/**
     * 描述：增加一组内容与tab.
     *
     * @param tabTexts the tab texts
     * @param fragments the fragments
     */
	public void addItemViews(List<String> tabTexts,List<Fragment> fragments){
		
		tabItemTextList.addAll(tabTexts);
		pagerItemList.addAll(fragments);
		
		mFragmentPagerAdapter.notifyDataSetChanged();
		notifyTabDataSetChanged();
	}
	
	/**
	 * 描述：增加一组内容与tab附带顶部图片.
	 *
	 * @param tabTexts the tab texts
	 * @param fragments the fragments
	 * @param drawables the drawables
	 */
	public void addItemViews(List<String> tabTexts,List<Fragment> fragments,List<Drawable> drawables){
		
		tabItemTextList.addAll(tabTexts);
		pagerItemList.addAll(fragments);
		tabItemDrawableList.addAll(drawables);
		mFragmentPagerAdapter.notifyDataSetChanged();
		notifyTabDataSetChanged();
	}
	
	
	/**
	 * 描述：增加一个内容与tab.
	 *
	 * @param tabText the tab text
	 * @param fragment the fragment
	 */
	public void addItemView(String tabText,Fragment fragment){
		tabItemTextList.add(tabText);
		pagerItemList.add(fragment);
		mFragmentPagerAdapter.notifyDataSetChanged();
		notifyTabDataSetChanged();
	}
	

	
	/**
	 * 描述：增加一个内容与tab.
	 *
	 * @param tabText the tab text
	 * @param fragment the fragment
	 * @param drawableNormal the drawable normal
	 * @param drawablePressed the drawable pressed
	 */
	public void addItemView(String tabText,Fragment fragment,Drawable drawableNormal,Drawable drawablePressed){
		tabItemTextList.add(tabText);
		pagerItemList.add(fragment);
		tabItemDrawableList.add(drawableNormal);
		tabItemDrawableList.add(drawablePressed);
		mFragmentPagerAdapter.notifyDataSetChanged();
		notifyTabDataSetChanged();
	}
	
	
	/**
	 * 描述：删除某一个.
	 *
	 * @param index the index
	 */
	public void removeItemView(int index){
		
		mTabLayout.removeViewAt(index);
		pagerItemList.remove(index);
		tabItemList.remove(index);
		tabItemDrawableList.remove(index);
		mFragmentPagerAdapter.notifyDataSetChanged();
		notifyTabDataSetChanged();
	}
	
	/**
	 * 描述：删除所有.
	 */
	public void removeAllItemViews(){
		mTabLayout.removeAllViews();
		pagerItemList.clear();
		tabItemList.clear();
		tabItemDrawableList.clear();
		mFragmentPagerAdapter.notifyDataSetChanged();
		notifyTabDataSetChanged();
	}
	
	/**
	 * 描述：获取这个View的ViewPager.
	 *
	 * @return the view pager
	 */
	public ViewPager getViewPager() {
		return mViewPager;
	}
	
	/**
	 * 设置tab导航的背景，默认无颜色
	 * @param resid
	 */
	public void setTabBackGround(int resid){
		mTabLayout.setBackgroundResource(resid);
	}
	
	/**
	 * 设置tab导航的背景，默认无颜色
	 * @param resid
	 */
	@SuppressWarnings("deprecation")
	public void setTabBackGround(Drawable drawable){
		mTabLayout.setBackgroundDrawable(drawable);
	}
	
	/**
	 * 设置tab导航的间距
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	public void setTabNavPadding(int left, int top, int right, int bottom) {
		mTabLayout.setPadding(left, top, right, bottom);
	}
	
	
	/**
	 * 描述：设置每个tab的边距.
	 *
	 * @param left the left
	 * @param top the top
	 * @param right the right
	 * @param bottom the bottom
	 */
	public void setTabPadding(int left, int top, int right, int bottom) {
		for(int i = 0;i<tabItemList.size();i++){
			tabItemList.get(i).setPadding(left, top, right, bottom);
		}
	}
	
	/**
	 * 描述：设置每个tab中文字内容的的边距.
	 *
	 * @param left the left
	 * @param top the top
	 * @param right the right
	 * @param bottom the bottom
	 */
	public void setTabTextPadding(int left, int top, int right, int bottom) {
		for(int i = 0;i<tabItemList.size();i++){
			tabItemList.get(i).getTextView().setPadding(left, top, right, bottom);
		}
	}
	
	/**
	 * 设置是否禁止所有tab选项卡滑动操作 
	 * @param flag  false 滑动、 true 不滑动
	 */
	public void setSlidingDisable(){
		mViewPager.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
	}
	

	/**
	 * 设置是否禁止滑动操作 ,只对某个tab选项有效
	 * @param flag  false 滑动、 true 不滑动 
	 * @param index  从索引0开始，第几个tab卡不能滑动
	 */
	public void setSlidingDisable(final int index){
		
		mViewPager.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				ViewPager vp=(ViewPager)v;
				if(vp.getCurrentItem()==index){
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * 设置是否禁止滑动操作 ,只对某个tab选项有效
	 * @param flag  false 滑动、 true 不滑动 
	 * @param index  从索引0开始，第几个tab卡不能滑动
	 */
	public void setSlidingDisable(final int[] indexs){
		
		mViewPager.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(indexs==null){
					return false;
				}
				String arrays=Arrays.toString(indexs);
				ViewPager vp=(ViewPager)v;
				if(arrays.indexOf(String.valueOf(vp.getCurrentItem()))>=0){
					return true;
				}
				return false;
			}
		});
	}
	
	
	public int framegetSize(){
		if(pagerItemList==null){
			return 0;
		}
		return pagerItemList.size();
	}

	public ArrayList<Fragment> getPagerItemList() {
		return pagerItemList;
	}
	
	
	public TTFragmentPagerAdapter getmFragmentPagerAdapter() {
		return mFragmentPagerAdapter;
	}

	public void clearFragmentPagerAdapter(){
		if(mFragmentPagerAdapter!=null){
			mFragmentPagerAdapter.clreaFragmentList();
		}
	}
	
}
