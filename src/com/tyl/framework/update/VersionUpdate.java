package com.tyl.framework.update;

import android.app.Activity;

import com.tyl.framework.TTApplication;
import com.tyl.framework.TTJson;
import com.tyl.framework.event.EventBus;
import com.tyl.framework.http.AsyncHttpClient;
import com.tyl.framework.http.AsyncHttpResponseHandler;
import com.tyl.framework.util.text.StringUtils;

public class VersionUpdate {

	/**
	 * 检查更新操作 ,发出事件 EventBus.getDefault().post(new VersionInfo(position, VersionStatus.failure));
	 * @param context  上下文
	 * @param url  请求地址
	 * @param position  请求处理标识
	 */
	public static void checkUpdate(final Activity context,String url,final int position){
		AsyncHttpClient client=new AsyncHttpClient();
		client.get(url, new AsyncHttpResponseHandler(){
			
			/**
			 * {"status":0,"data":{"versioncode":1,"versionname":"V2.1.2" ,
			 * ”downurl”:"http://www.firstte.com/apk/firstte_v2.1.2.apk",
			 * "message":["1.新增搜索功能","2.修复bug","3.操作更加流畅"]}
			 */
			@Override
			public void onSuccess(String json) {
				//Logger.e("VersionUpdate", content);
				
				if(!StringUtils.isEmpty(json)){
					if(TTJson.jsonToObjectIntFile(json, "status")==0){
						VersionInfo versionInfo=TTJson.jsonToObject(json, "data", VersionInfo.class);
						versionInfo.setPosition(position);
						versionInfo.setStatus(VersionStatus.success);
						TTApplication.getApplication().setVersionInfo(versionInfo);
						EventBus.getDefault().post(versionInfo);
						//Logger.i("VersionUpdate", versionInfo.toString());
					}
				}else{
					EventBus.getDefault().post(new VersionInfo(position, VersionStatus.failure));
				}	
				
			}

			@Override
			public void onFailure(Throwable error) {
				EventBus.getDefault().post(new VersionInfo(position, VersionStatus.failure));
			}
			
			
		});
	}
	
	
}
