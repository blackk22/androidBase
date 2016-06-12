package com.tyl.framework;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tyl.framework.config.IConfig;
import com.tyl.framework.log.L;

public class TTFragment extends Fragment {
	private TTActivity ftActivity;
	private boolean isPrepared=false;

	/**
	 * 第一次onResume中的调用onUserVisible避免操作与onFirstUserVisible操作重复
	 */
	private boolean isFirstResume = true;

	private boolean isFirstVisible = true;
	private boolean isFirstInvisible = true;

	@Override
	public void onAttach(Activity activity) {
		L.s(this.getClass().getSimpleName() + "----onAttach---");
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		L.s(this.getClass().getSimpleName() + "----onCreate---");
		super.onCreate(savedInstanceState);
		ftActivity = (TTActivity) getActivity();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		L.s(this.getClass().getSimpleName() + "----onCreateView---");
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		L.s(this.getClass().getSimpleName() + "----onActivityCreated---");
		super.onActivityCreated(savedInstanceState);
		initPrepare();
	}

	@Override
	public void onStart() {
		L.s(this.getClass().getSimpleName() + "----onStart---");
		super.onStart();
	}

	@Override
	public void onResume() {
		L.s(this.getClass().getSimpleName() + "----onResume---");
		super.onResume();
		if (isFirstResume) {
			isFirstResume = false;
			return;
		}
		if (getUserVisibleHint()) {
			onUserVisible();
		}
	}

	@Override
	public void onPause() {
		L.s(this.getClass().getSimpleName() + "----onPause---");
		super.onPause();
		if (getUserVisibleHint()) {
			onUserInvisible();
		}
	}

	@Override
	public void onStop() {
		L.s(this.getClass().getSimpleName() + "----onStop---");
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		L.s(this.getClass().getSimpleName() + "----onDestroyView---");
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		L.s(this.getClass().getSimpleName() + "----onDestroy---");
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		L.s(this.getClass().getSimpleName() + "----onDetach---");
		super.onDetach();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		L.s(this.getClass().getSimpleName() + "----onHiddenChanged---"+hidden+"----");
		super.onHiddenChanged(hidden);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		L.s(this.getClass().getSimpleName() + "----setUserVisibleHint---"+isVisibleToUser+"-----");
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			if (isFirstVisible) {
				isFirstVisible = false;
				initPrepare();
			} else {
				onUserVisible();
			}
		} else {
			if (isFirstInvisible) {
				isFirstInvisible = false;
				onFirstUserInvisible();
			} else {
				onUserInvisible();
			}
		}
	}

	public synchronized void initPrepare() {
		if (isPrepared) {
			onFirstUserVisible();
		} else {
			isPrepared = true;
		}
	}

	/**
	 * 第一次fragment可见（进行初始化工作）
	 */
	public void onFirstUserVisible() {
		L.s(this.getClass().getSimpleName() + "----onFirstUserVisible---");
	}

	/**
	 * fragment可见（切换回来或者onResume）
	 */
	public void onUserVisible() {
		L.s(this.getClass().getSimpleName() + "----onUserVisible---");
	}

	/**
	 * 第一次fragment不可见（不建议在此处理事件）
	 */
	public void onFirstUserInvisible() {
		L.s(this.getClass().getSimpleName() + "----onFirstUserInvisible---");
	}

	/**
	 * fragment不可见（切换掉或者onPause）
	 */
	public void onUserInvisible() {
		L.s(this.getClass().getSimpleName() + "----onUserInvisible---");
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		L.s(this.getClass().getSimpleName() + "----onSaveInstanceState---");
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		L.s(this.getClass().getSimpleName() + "----onViewCreated---");
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		L.s(this.getClass().getSimpleName() + "----onViewStateRestored---");
		super.onViewStateRestored(savedInstanceState);
	}
	
	public TTActivity getFtActivity() {
		return ftActivity;
	}

	public boolean isPrepared() {
		return isPrepared;
	}

	public boolean isFirstResume() {
		return isFirstResume;
	}

	public boolean isFirstVisible() {
		return isFirstVisible;
	}

	public boolean isFirstInvisible() {
		return isFirstInvisible;
	}

	public TTApplication getFTApplication() {
		return TTApplication.getApplication();
	}

	/**
	 * 目标组件
	 * 
	 * @param cla
	 */
	public void startActivityFinish(Class<?> cla) {
		startActivity(new Intent(ftActivity, cla));
		ftActivity.finish();
	}

	/**
	 * 目标组件
	 * 
	 * @param cla
	 */
	public void startActivityFinish(Class<?> cla, Bundle bundle) {
		Intent intent = new Intent(ftActivity, cla);
		intent.putExtras(bundle);
		startActivity(intent);
		ftActivity.finish();
	}

	/**
	 * 目标组件
	 * 
	 * @param cla
	 */
	public void startActivity(Class<?> cla) {
		startActivity(new Intent(ftActivity, cla));
	}

	/**
	 * 目标组件
	 * 
	 * @param cla
	 */
	public void startActivity(Class<?> cla, Bundle bundle) {
		Intent intent = new Intent(ftActivity, cla);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	/**
	 * 关闭Activity
	 */
	public void finishActivity() {
		ftActivity.finish();
	}

	/**
	 * 订阅类注册
	 * 
	 * @param subscriber
	 */
	public void registerSubscriber(Object subscriber) {
		TTEventBus.register(subscriber);
	}

	/**
	 * 发布事件
	 * 
	 * @param event
	 */
	public void postEvent(Object event) {
		TTEventBus.post(event);
	}

	/**
	 * 注销订阅者
	 * 
	 * @param subscriber
	 */
	public void unregisterSubscriber(Object subscriber) {
		TTEventBus.unregister(subscriber);
	}

	/**
	 * 获得当前设置配置对象
	 * 
	 * @return
	 */
	public IConfig getCurrentConfig() {
		return getFTApplication().getCurrentConfig();
	}
}
