package com.tyl.framework.widget;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TTToast {

	private static Toast toast = null;
	public static int LENGTH_LONG = Toast.LENGTH_LONG;
	public static int LENGTH_SHORT = Toast.LENGTH_SHORT;

	/**
	 * 普通文本消息提示 显示在屏幕顶间
	 * 
	 * @param context
	 * @param text
	 * @param duration
	 */
	public static void showToastTop(Activity context, CharSequence text,int duration) {
		// 创建一个Toast提示消息
		toast = Toast.makeText(context, text, duration);
		// 设置Toast提示消息在屏幕上的位置
		toast.setGravity(Gravity.TOP, 0, 0);
		// 显示消息
		toast.show();
	}
	
	/**
	 * 普通文本消息提示 显示在屏幕中间
	 * 
	 * @param context
	 * @param text
	 * @param duration
	 */
	public static void showToastCenter(Activity context, CharSequence text,int duration) {
		// 创建一个Toast提示消息
		toast = Toast.makeText(context, text, duration);
		// 设置Toast提示消息在屏幕上的位置
		toast.setGravity(Gravity.CENTER, 0, 0);
		// 显示消息
		toast.show();
	}

	/**
	 * 普通文本消息提示 显示屏幕底部
	 * 
	 * @param context
	 * @param text
	 * @param duration
	 */
	public static void showToastBottom(Activity context, CharSequence text,
			int duration) {
		// 创建一个Toast提示消息
		toast = Toast.makeText(context, text, duration);
		// 设置Toast提示消息在屏幕上的位置
		toast.setGravity(Gravity.BOTTOM, 0, 0);
		// 显示消息
		toast.show();
	}

	/**
	 * 普通文本消息提示 长时间显示
	 * 
	 * @param context
	 * @param text
	 */
	public static void showLongToast(Activity context, CharSequence text) {
		// 创建一个Toast提示消息
		toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		// 显示消息
		toast.show();
	}
	
	/**
	 * 普通文本消息提示 短时间 显示
	 * 
	 * @param context
	 * @param text
	 */
	public static void showShortToast(Activity context, CharSequence text) {
		// 创建一个Toast提示消息
		toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		// 显示消息
		toast.show();
	}
	
	/**
	 * 带背景图像消息提示
	 * 
	 * @param context
	 * @param ImageResourceId
	 * @param text
	 * @param duration
	 */
	public static void showimageToastCenter(Activity context, int ImageResourceId,CharSequence text, int duration) {
		// 创建一个Toast提示消息
		toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		// 设置Toast提示消息在屏幕上的位置
		toast.setGravity(Gravity.CENTER, 0, 0);
		// 获取Toast提示消息里原有的View
		View toastView = toast.getView();
		// 创建一个ImageView
		ImageView img = new ImageView(context);
		img.setImageResource(ImageResourceId);
		// 创建一个LineLayout容器
		LinearLayout ll = new LinearLayout(context);
		// 向LinearLayout中添加ImageView和Toast原有的View
		ll.addView(img);
		ll.addView(toastView);
		// 将LineLayout容器设置为toast的View
		toast.setView(ll);
		// 显示消息
		toast.show();
	}
	
	/**
	 * 带标题和内容的对话框模式的提示
	 * @param context
	 * @param title
	 * @param content
	 * @param duration
	 */
	public static  void showDialogToast(Activity context,CharSequence title,CharSequence content, int duration){
		   
		showDialogToast(context,title,content,0xffffffff,duration);
		   
	}
	
	/**
	 * 带标题和内容的对话框模式的提示
	 * @param context
	 * @param title
	 * @param content
	 * @param duration
	 */
	public static  void showDialogToast(Activity context,CharSequence title,CharSequence content,int bgcolor, int duration){
		   
		   LinearLayout layout=new LinearLayout(context);
		   LayoutParams params1=new LayoutParams(200, LayoutParams.WRAP_CONTENT);
		   layout.setLayoutParams(params1);
		   layout.setBackgroundColor(bgcolor);//0xffffffff
		   layout.setOrientation(LinearLayout.VERTICAL);
		   
		   TextView tv_title=new TextView(context);
		   LinearLayout.LayoutParams params2=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		   tv_title.setLayoutParams(params2);
		   tv_title.setPadding(20, 10, 20, 10);
		   tv_title.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
		   tv_title.setTextColor(0xFF8CD2EC);
		   tv_title.setTextSize(20);
		   tv_title.setText(title);
		   
		   TextView tv_divice=new TextView(context);
		   LinearLayout.LayoutParams params3=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,2);
		   tv_divice.setLayoutParams(params3);
		   tv_divice.setBackgroundColor(0xFF8CD2EC);
		   
		   TextView tv_context=new TextView(context);
		   LinearLayout.LayoutParams params4=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT );
		   tv_context.setLayoutParams(params4);
		   tv_context.setPadding(15, 10, 15, 20);
		   tv_context.setGravity(Gravity.LEFT|Gravity.TOP);
		   tv_context.setTextColor(0xFF000000);
		   tv_context.setTextSize(18);
		   tv_context.setMaxEms(16);
		   tv_context.setText(content);
		   
		   layout.addView(tv_title);
		   layout.addView(tv_divice);
		   layout.addView(tv_context);

		   toast = new Toast(context);  
		   toast.setGravity(Gravity.CENTER,0,0);  
		   toast.setDuration(Toast.LENGTH_SHORT);  
		   toast.setView(layout);  
		   toast.show();  
		   
	}
	
	/**
	 * 带标题和内容的对话框模式的提示 长时间显示
	 * @param context
	 * @param title
	 * @param content
	 * @param duration
	 */
	public static  void showDialogToastLong(Activity context,CharSequence title,CharSequence content){
		showDialogToast(context,title,content,Toast.LENGTH_LONG);
	}
	
	/**
	 * 带标题和内容的对话框模式的提示 短时间显示
	 * @param context
	 * @param title
	 * @param content
	 * @param duration
	 */
	public static  void showDialogToastShort(Activity context,CharSequence title,CharSequence content){
		showDialogToast(context,title,content,Toast.LENGTH_SHORT);
	}
	
	/**
	 * 带标题和内容的对话框模式的提示 长时间显示
	 * @param context
	 * @param title
	 * @param content
	 * @param duration
	 */
	public static  void showDialogToastLong(Activity context,CharSequence title,CharSequence content,int bgcolor){
		showDialogToast(context,title,content,bgcolor,Toast.LENGTH_LONG);
	}
	
	/**
	 * 带标题和内容的对话框模式的提示 短时间显示
	 * @param context
	 * @param title
	 * @param content
	 * @param duration
	 */
	public static  void showDialogToastShort(Activity context,CharSequence title,CharSequence content,int bgcolor){
		showDialogToast(context,title,content,bgcolor,Toast.LENGTH_SHORT);
	}
	
	
}
