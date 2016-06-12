package com.tyl.framework.widget;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/***
 * 通用的ViewHolder
 * 使用步骤
 * 实例化一个viewHolder  
 * ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,   R.layout.item_single_str, position);  
 * 通过getView获取控件  
 * TextView tv = viewHolder.getView(R.id.id_tv_title);  
 * 使用  
 * tv.setText(mDatas.get(position));  
 * @author LaoYing
 *
 */
public class TTViewHolder {
	
	private final SparseArray<View> mViews;
	private View mConvertView;

	private TTViewHolder(Context context, ViewGroup parent, int layoutId,int position)
	{
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,false);
		mConvertView.setTag(this);
		
		
	}

	/**
	 * 拿到一个ViewHolder对象
	 * @param context
	 * @param convertView
	 * @param parent
	 * @param layoutId
	 * @param position
	 * @return
	 */
	public static TTViewHolder get(Context context, View convertView,
			ViewGroup parent, int layoutId, int position){
	
		if (convertView == null)
		{
			return new TTViewHolder(context, parent, layoutId, position);
		}
		return (TTViewHolder) convertView.getTag();
	}


	/**
	 * 通过控件的Id获取对于的控件，如果没有则加入views
	 * @param viewId
	 * @return
	 */
	public <T extends View> T getView(int viewId)
	{
		
		View view = mViews.get(viewId);
		if (view == null)
		{
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T)view;
	}

	public View getConvertView()
	{
		return mConvertView;
	}

}
