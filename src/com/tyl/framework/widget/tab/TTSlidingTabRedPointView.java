package com.tyl.framework.widget.tab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tyl.framework.log.L;
import com.tyl.framework.widget.adapter.TTFragmentPagerAdapter;


/**
 * 名称：FTSlidingTabView.java 
 * 描述：滑动的tab,tab不固定超出后可滑动.
 */
public class TTSlidingTabRedPointView extends LinearLayout {
	
	/** The context. */
	private Context context;
	
	private int left=0, top=0, right=0, bottom=0;
	
	/** The m tab selector. */
	private Runnable mTabSelector;
    
    /** The m listener. */
    private ViewPager.OnPageChangeListener mListener;
    
    /** The m max tab width. */
    public int mMaxTabWidth;
    
    /** The m selected tab index. */
    private int mSelectedTabIndex;
    
    /** tab的背景. */
    private int tabBackgroundResource = -1;
    
    /** tab的文字大小. */
	private int tabTextSize = 30;
	
	/** tab的文字颜色. */
	private int tabTextColor = Color.BLACK;
	
	/** tab的选中文字颜色. */
	private int tabSelectColor = Color.BLACK;
	
	/**tab字体样式*/
	private Typeface tabTextTypeface = Typeface.MONOSPACE;
	
	/** tab的线性布局. */
	private LinearLayout mTabLayout = null;
	
	/** tab的线性布局父. */
	private HorizontalScrollView mTabScrollView  = null;
	
	/** The m view pager. */
	private ViewPager mViewPager;
	
	/** tab的文字. */
	private List<String> tabItemTextList = null;
	
	/** tab的图标. */
	private List<Drawable> tabItemDrawableList = null;
	
	/** 内容的View. */
	private ArrayList<Fragment> pagerItemList = null;
	
	/** tab的列表. */
	private ArrayList<TTTabItemRedPointView> tabItemList = null;
	
	/** 内容区域的适配器. */
	private TTFragmentPagerAdapter mFragmentPagerAdapter = null;
    
    /** The m tab click listener. */
    private OnClickListener mTabClickListener = new OnClickListener() {
        public void onClick(View view) {
        	TTTabItemRedPointView tabView = (TTTabItemRedPointView)view;
            mViewPager.setCurrentItem(tabView.getIndex());
        }
    };

    /**
     * Instantiates a new ab sliding tab view.
     *
     * @param context the context
     */
    public TTSlidingTabRedPointView(Context context) {
        this(context, null);
    }

    /**
     * Instantiates a new ab sliding tab view.
     *
     * @param context the context
     * @param attrs the attrs
     */
    public TTSlidingTabRedPointView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        
        this.setOrientation(LinearLayout.VERTICAL);
		this.setBackgroundColor(Color.rgb(255, 255, 255));
		
		//滚动视图
		mTabScrollView  = new HorizontalScrollView(context);
		mTabScrollView.setHorizontalScrollBarEnabled(false);
		mTabScrollView.setSmoothScrollingEnabled(true);
		
		mTabLayout = new LinearLayout(context);
		mTabLayout.setOrientation(LinearLayout.HORIZONTAL);
		mTabLayout.setGravity(Gravity.CENTER);
		
		//mTabLayout是内容宽度
		mTabScrollView.addView(mTabLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
		
		this.addView(mTabScrollView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        
		//内容的View的适配
  		mViewPager = new ViewPager(context);
  		//手动创建的ViewPager,必须调用setId()方法设置一个id
  		mViewPager.setId(1985);
  		pagerItemList = new ArrayList<Fragment>();
  	    //定义Tab栏
  		tabItemList = new ArrayList<TTTabItemRedPointView>();
  		tabItemTextList = new ArrayList<String>();
  		tabItemDrawableList = new ArrayList<Drawable>();
  		
  	    //要求必须是FragmentActivity的实例
		if(!(this.context instanceof FragmentActivity)){
			L.e(TTSlidingTabRedPointView.class, "构造AbSlidingTabView的参数context,必须是FragmentActivity的实例。");
		}
  			
  		//FragmentManager mFragmentManager = ((FragmentActivity)this.context).getSupportFragmentManager();
		
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mViewPager.setOffscreenPageLimit(0);
  		
		this.addView(mViewPager,new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    
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
			if (mListener != null) {
	            mListener.onPageScrollStateChanged(arg0);
	        }
		}

		/* (non-Javadoc)
		 * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled(int, float, int)
		 */
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			if (mListener != null) {
	            mListener.onPageScrolled(arg0, arg1, arg2);
	        }
		}

		/* (non-Javadoc)
		 * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected(int)
		 */
		@Override
		public void onPageSelected(int arg0) {
			setCurrentItem(arg0);
	        if (mListener != null) {
	            mListener.onPageSelected(arg0);
	        }
		}
		
	}

    /* (non-Javadoc)
     * @see android.widget.LinearLayout#onMeasure(int, int)
     */
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;
        mTabScrollView.setFillViewport(lockedExpanded);

        final int childCount = mTabLayout.getChildCount();
        if (childCount > 1 && (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST)) {
            if (childCount > 2) {
                mMaxTabWidth = (int)(MeasureSpec.getSize(widthMeasureSpec) * 0.4f);
            } else {
                mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
            }
        } else {
            mMaxTabWidth = -1;
        }

        final int oldWidth = getMeasuredWidth();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int newWidth = getMeasuredWidth();

        if (lockedExpanded && oldWidth != newWidth) {
            // Recenter the tab display if we're at a new (scrollable) size.
            setCurrentItem(mSelectedTabIndex);
        }
    }

    /**
     * Animate to tab.
     *
     * @param position the position
     */
    private void animateToTab(final int position) {
        final View tabView = mTabLayout.getChildAt(position);
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
        mTabSelector = new Runnable() {
            public void run() {
                final int scrollPos = tabView.getLeft() - (getWidth() - tabView.getWidth()) / 2;
                mTabScrollView.smoothScrollTo(scrollPos, 0);
                mTabSelector = null;
            }
        };
        post(mTabSelector);
    }

    /* (non-Javadoc)
     * @see android.view.View#onAttachedToWindow()
     */
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mTabSelector != null) {
            // Re-post the selector we saved
            post(mTabSelector);
        }
    }

    /* (non-Javadoc)
     * @see android.view.View#onDetachedFromWindow()
     */
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
    }
    
    public void setFragmentManager(FragmentManager mFragmentManager){
    	mFragmentPagerAdapter = new TTFragmentPagerAdapter(mFragmentManager, pagerItemList);
		mViewPager.setAdapter(mFragmentPagerAdapter);
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
    private void addTab(String text, int index,Drawable topdDrawable) {
   	
    	TTTabItemRedPointView tabView = new TTTabItemRedPointView(this.context);
        if(tabBackgroundResource!=-1){
        	tabView.setTabBackgroundResource(tabBackgroundResource);
        }
        if(topdDrawable!=null){
        	tabView.setTabCompoundDrawables(null, topdDrawable, null, null);
        }
    	tabView.setTabTextColor(tabTextColor);
    	tabView.setTabTextSize(tabTextSize);
    	tabView.setTabTextTypeface(tabTextTypeface);
        
        tabView.init(index,text);
        
        tabItemList.add(tabView);
        
        tabView.setOnClickListener(mTabClickListener);
        LayoutParams layoutParams=new LayoutParams(0,LayoutParams.MATCH_PARENT,1);
        layoutParams.leftMargin=left;
        layoutParams.topMargin=top;
        layoutParams.rightMargin=right;
        layoutParams.bottomMargin=bottom;
        mTabLayout.addView(tabView,layoutParams);
    }

    /**
     * 描述：tab有变化刷新.
     */
    public void notifyTabDataSetChanged() {
        mTabLayout.removeAllViews();
        tabItemList.clear();
        final int count = mFragmentPagerAdapter.getCount();
        L.e("FTSlidingTabView"," mFragmentPagerAdapter.getCount():"+count);
        for (int i = 0; i < count; i++) {
        	if(tabItemDrawableList.size()>0){
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
     * 描述：设置显示哪一个.
     *
     * @param item the new current item
     */
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        mSelectedTabIndex = item;
        final int tabCount = mTabLayout.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            final TTTabItemRedPointView child = (TTTabItemRedPointView)mTabLayout.getChildAt(i);
            final boolean isSelected = (i == item);
            child.setSelected(isSelected);
            if (isSelected) {
            	child.setTabTextColor(tabSelectColor);
                animateToTab(item);
                mViewPager.setCurrentItem(item);
            }else{
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

	/**
	 * 描述：设置tab文字的颜色.
	 *
	 * @param tabColor the new tab text color
	 */
	public void setTabTextColor(int tabColor) {
		this.tabTextColor = tabColor;
	}

	/**
	 * 描述：设置选中的颜色.
	 *
	 * @param tabColor the new tab select color
	 */
	public void setTabSelectColor(int tabColor) {
		this.tabSelectColor = tabColor;
	}

	/**
	 * 描述：设置文字大小.
	 *
	 * @param tabTextSize the new tab text size
	 */
	public void setTabTextSize(int tabTextSize) {
		this.tabTextSize = tabTextSize;
	}
	
	/**
	 * 描述：设置文字样式
	 * @param tabTextTypeface the new tab text typeface value
	 */
	public void setTabTextTypeface(Typeface tabTextTypeface){
		this.tabTextTypeface = tabTextTypeface;
	}
	
	/**
	 * 描述：设置单个tab的背景选择器.
	 *
	 * @param resid the new tab background resource
	 */
	public void setTabBackgroundResource(int resid) {
    	tabBackgroundResource = resid;
    }
	
	/**
	 * 描述：设置Tab的背景.
	 *
	 * @param resid the new tab layout background resource
	 */
	public void setTabLayoutBackgroundResource(int resid) {
		this.mTabLayout.setBackgroundResource(resid);
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
	 * 描述：增加一组内容与tab.
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
	 * @param drawable the drawable
	 */
	public void addItemView(String tabText,Fragment fragment,Drawable drawable){
		tabItemTextList.add(tabText);
		pagerItemList.add(fragment);
		tabItemDrawableList.add(drawable);
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
		tabItemTextList.remove(index); 
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
		tabItemTextList.clear(); 
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
	 * 设置每个tab的边距.
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	public void setTabPadding(int left, int top, int right, int bottom){
    	this.left=0;
    	this.top=top;
    	this.right=right;
    	this.bottom=bottom;
    }
	
	
	/**
	 * 描述：设置每个tab中文本的边距.
	 *
	 * @param left the left
	 * @param top the top
	 * @param right the right
	 * @param bottom the bottom
	 */
	public void setTabTextPadding(int left, int top, int right, int bottom) {
		for(int i = 0;i<tabItemList.size();i++){
			TextView tv = tabItemList.get(i).getTextView();
			tv.setPadding(left, top, right, bottom);
		}
	}
	
	/**
	 * 描述：设置tab导航条的边距.
	 *
	 * @param left the left
	 * @param top the top
	 * @param right the right
	 * @param bottom the bottom
	 */
	public void setTabNavPadding(int left, int top, int right, int bottom) {
		mTabLayout.setPadding(left, top, right, bottom);
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

	/**
	 * 得到当前选中页Index
	 * @return
	 */
	public int getmSelectedTabIndex() {
		return mSelectedTabIndex;
	}
	
	/**
	 * 设置某tab有红点
	 * @param i
	 */
	public void setRedPointEnable(int i){
		if(tabItemList!=null&&i<tabItemList.size()){
			tabItemList.get(i).setRedPoint(true);
		}
	}
	
	/**
	 * 设置某tab没有红点
	 * @param i
	 */
	public void setRedPointDisable(int i){
		if(tabItemList!=null&&i<tabItemList.size()){
			tabItemList.get(i).setRedPoint(false);
		}
	}
		
    
}
