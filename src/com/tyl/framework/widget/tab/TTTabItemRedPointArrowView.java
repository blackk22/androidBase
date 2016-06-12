package com.tyl.framework.widget.tab;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tyl.framework.R;
import com.tyl.framework.TTResource;
import com.tyl.framework.widget.util.TTViewUtil;


/**
 * 名称：FTTabItemView.java 
 * 描述：表示一个带下拉箭头与中间竖线的TAB
 */
public class TTTabItemRedPointArrowView extends LinearLayout {
	
	/** The m context. */
	private Context mContext;
	//当前的索引
    /** The m index. */
	private int mIndex;
    //包含的TextView
    /** The m text view. */
    private TextView mTextView;
    
    private ImageView mImageView,iv_xiala;
    
    private ImageView miv_line;
	
    /**
     * Instantiates a new ab tab item view.
     *
     * @param context the context
     */
    public TTTabItemRedPointArrowView(Context context) {
		this(context,null);
	}

	/**
	 * Instantiates a new ab tab item view.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public TTTabItemRedPointArrowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(LinearLayout.HORIZONTAL);
        this.mContext = context;
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        //View view=layoutInflater.inflate(R.layout.tab_item_arrow_layout,null);
        View view=layoutInflater.inflate(TTResource.getIdByName(context, "layout", "tab_item_arrow_layout"),null);
        
        mTextView = (TextView)view.findViewById(TTResource.getIdByName(context, "id", "tv_title"));
        iv_xiala=(ImageView)view.findViewById(TTResource.getIdByName(context, "id", "iv_xiala"));
        mImageView=(ImageView)view.findViewById(TTResource.getIdByName(context, "id", "iv_red_point"));
        miv_line=(ImageView)view.findViewById(TTResource.getIdByName(context, "id", "iv_line"));
        mImageView.setVisibility(View.GONE);
        
       // mTextView.setGravity(Gravity.CENTER);
       // mTextView.setFocusable(true);
       // mTextView.setPadding(10, 0, 10, 0);
//        mTextView.setBackgroundColor(0xff00ff00);
       // mTextView.setSingleLine();
        
        LinearLayout.LayoutParams layout_mm=new LayoutParams(0,LayoutParams.MATCH_PARENT,1);
        layout_mm.gravity=Gravity.CENTER;
        this.addView(view,layout_mm);
    }

    /**
     * Inits the.
     *
     * @param index the index
     * @param text the text
     */
    public void init(int index,String text) {
        mIndex = index;
        mTextView.setText(text);
    }


    /**
     * Gets the index.
     *
     * @return the index
     */
    public int getIndex() {
        return mIndex;
    }


	/**
	 * Gets the text view.
	 *
	 * @return the text view
	 */
	public TextView getTextView() {
		return mTextView;
	}

    /**
     * 描述：设置文字大小.
     *
     * @param tabTextSize the new tab text size
     */
	public void setTabTextSize(int tabTextSize) {
		TTViewUtil.setTextSize(mTextView, tabTextSize);
	}

	/**
	 * 描述：设置文字颜色.
	 *
	 * @param tabColor the new tab text color
	 */
	public void setTabTextColor(int tabColor) {
		mTextView.setTextColor(tabColor);
	}
	
	/**
	 * 描述：设置文字样式
	 * @param tabTextTypeface the new tab text typeface value
	 */
	public void setTabTextTypeface(Typeface tabTextTypeface){
		mTextView.setTypeface(tabTextTypeface);
	}
	
	/**
	 * 描述：设置文字图片结合.
	 *
	 * @param left the left
	 * @param top the top
	 * @param right the right
	 * @param bottom the bottom
	 */
	public void setTabCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
//		if(left!=null){
//			left.setBounds(0, 0, FTViewUtil.scale(mContext, left.getIntrinsicWidth()), FTViewUtil.scale(mContext, left.getIntrinsicHeight())); 
//		}
//		if(top!=null){
//		    top.setBounds(0, 0, FTViewUtil.scale(mContext, top.getIntrinsicWidth()), FTViewUtil.scale(mContext, top.getIntrinsicHeight())); 
//		}
//		if(right!=null){
//		    right.setBounds(0, 0, FTViewUtil.scale(mContext, right.getIntrinsicWidth()), FTViewUtil.scale(mContext, right.getIntrinsicHeight()));
//		}
//		if(bottom!=null){
//		    bottom.setBounds(0, 0, FTViewUtil.scale(mContext, bottom.getIntrinsicWidth()), FTViewUtil.scale(mContext, bottom.getIntrinsicHeight())); 
//		}
		
		if(left!=null){
			left.setBounds(0, 0,left.getIntrinsicWidth(), left.getIntrinsicHeight()); 
		}
		if(top!=null){
		    top.setBounds(0, 0, top.getIntrinsicWidth(), top.getIntrinsicHeight()); 
		}
		if(right!=null){
		    right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
		}
		if(bottom!=null){
		    bottom.setBounds(0, 0,  bottom.getIntrinsicWidth(),bottom.getIntrinsicHeight()); 
		}
		mTextView.setCompoundDrawables(left, top, right, bottom);
	}
	
	/**
	 * 描述：设置tab的背景选择.
	 *
	 * @param resid the new tab background resource
	 */
	public void setTabBackgroundResource(int resid) {
		this.setBackgroundResource(resid);
	}
	
	/**
	 * 描述：设置tab的背景选择.
	 *
	 * @param d the new tab background drawable
	 */
	public void setTabBackgroundDrawable(Drawable d) {
		this.setBackgroundDrawable(d);
	}
	
	
	/**
	 * 描述：设置tab的背景选择.
	 *
	 * @param resid the new tab background resource
	 */
	public void setTabTextBackgroundResource(int resid) {
		this.mTextView.setBackgroundResource(resid);
	}
	
	/**
	 * 描述：设置tab的背景选择.
	 *
	 * @param d the new tab background drawable
	 */
	public void setTabTextBackgroundDrawable(Drawable d) {
		this.mTextView.setBackgroundDrawable(d);
	}
	
	/**
	 * 设置红点是否显示，true显示、false 隐藏
	 * @param visiable
	 */
	public void setRedPoint(boolean visiable){
		if(visiable){
			mImageView.setVisibility(View.VISIBLE);
		}else{
			mImageView.setVisibility(View.GONE);
		}
		
	}
	
	/**
	 * 设置中间的线条是否显示，true显示、false 隐藏
	 * @param visiable
	 */
	public void setLine(boolean visiable){
		if(visiable){
			miv_line.setVisibility(View.VISIBLE);
		}else{
			miv_line.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 设置下拉图标
	 * @param draw
	 */
	public void setXialaImg(int draw){
		iv_xiala.setImageResource(draw);
	}
    
}