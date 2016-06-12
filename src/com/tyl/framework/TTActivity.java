package com.tyl.framework;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.tyl.framework.config.IConfig;
import com.tyl.framework.log.L;
import com.tyl.framework.widget.TTAppExitToast;

public class TTActivity extends  FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		L.d("framework",this.getClass().getSimpleName()+"----onCreate-----");
		super.onCreate(savedInstanceState);
		getFTApplication().getAppManager().addActivity(this);
	}

	@Override
	protected void onStart() {
		L.d("framework",this.getClass().getSimpleName()+"------onStart-----");
		super.onStart();
	}

	@Override
	protected void onRestart() {
		L.d("framework",this.getClass().getSimpleName()+"-------onRestart-----");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		L.d("framework",this.getClass().getSimpleName()+"-----onResume--------");
		super.onResume();
	}

	@Override
	protected void onPause() {
		L.d("framework",this.getClass().getSimpleName()+"------onPause------");
		super.onPause();
	}

	@Override
	protected void onStop() {
		L.d("framework",this.getClass().getSimpleName()+"-------onStop-------");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		L.d("framework",this.getClass().getSimpleName()+"------onDestroy-------");
		getFTApplication().getAppManager().removeActivity(this);
		super.onDestroy();
	}
	
	

	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		L.d("framework",this.getClass().getSimpleName()+"-----finalize------");
	}

	public TTApplication getFTApplication() {
		return TTApplication.getApplication();
	}
	
	/**
	  * 目标组件
	  * @param cla
	  */
	 public void startActivityFinish(Class<?> cla){
		 startActivity(new Intent(this, cla));
		 this.finish();
	 }
	 
	 /**
	  * 目标组件
	  * @param cla
	  */
	 public void startActivityFinish(Class<?> cla,Bundle bundle){
		 Intent intent=new Intent(this, cla);
		 intent.putExtras(bundle);
		 startActivity(intent);
		 this.finish();
	 }
	 
	 /**
	  * 目标组件
	  * @param cla
	  */
	 public void startActivity(Class<?> cla){
		 startActivity(new Intent(this, cla));
	 }
	 
	 /**
	  * 目标组件
	  * @param cla
	  */
	 public void startActivity(Class<?> cla,Bundle bundle){
		 Intent intent=new Intent(this, cla);
		 intent.putExtras(bundle);
		 startActivity(intent);
	 }
	 
	public void finishActivity() {
		this.finish();
	}
	
	
	/**
	 * 退出应用程序提示
	 * 
	 */
	public void exitAppToast(String msg) {
		TTAppExitToast.getInstance().exit(this,msg);
		
	}
	
	
	/**
	 * 退出应用程序提示
	 * 
	 */
	public void exitAppToast(int msgid) {
		TTAppExitToast.getInstance().exit(this,msgid);
		
	}

	/**
	 * 退出应用程序对话框	 * 
	 */
	public void exitAppDialog(){
		
	}
	
	/**
	 * 订阅类注册
	 * @param subscriber
	 */
	 public void registerSubscriber(Object subscriber) {
		 TTEventBus.register(subscriber);
	 }

	 /**
	  * 发布事件
	  * @param event
	  */
	 public void postEvent(Object event) {
		 TTEventBus.post(event);
	 }
	 
	 /**
	  * 注销订阅者
	  * @param subscriber
	  */
	 public void unregisterSubscriber(Object subscriber){
		 TTEventBus.unregister(subscriber);
	 }
	 
	 /**
	 * 获得当前设置配置对象
	 * @return
	 */
	public IConfig getCurrentConfig() {
		return getFTApplication().getCurrentConfig();
	}
}
