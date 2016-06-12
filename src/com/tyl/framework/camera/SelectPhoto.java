package com.tyl.framework.camera;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.tyl.framework.TTApplication;
import com.tyl.framework.cache.CacheDirectory;
import com.tyl.framework.exception.SDCardException;
import com.tyl.framework.log.Logger;
import com.tyl.framework.util.bitmap.BitMapUtility;

public class SelectPhoto {

	/* 用来标识请求照相功能的activity */
	private static final int CAMERA_select_DATA = 3021;
	/* 用来标识请求裁剪图片后的activity */
	private static final int CAMERA_CROP_DATA = 3020;
	
	/** The thumbnail width. */
	private int pWidth=116;
	
	/** The thumbnail height. */
	private int pHeight=116;
	
	private   String save_photo_dir;
	private   String  originalPhotoPath;
	private   String  currentPhotoPath;
	
	private int outputX=600;
	private int outputY=600;
	private boolean isCrop=true;
	
	private  IPhotoProcess photoProcess;
	
	//构造初始化
	{
		save_photo_dir=CacheDirectory.getPicDir(TTApplication.getApplication());
	}
	
	
	
	public SelectPhoto() {
		//System.out.println("实例化");
	}




	public void setOnPhotoProcess(IPhotoProcess photoProcess){
		this.photoProcess=photoProcess;
	}
	
	

	
	public void startSelectPhoto(Activity activity) throws SDCardException {
		
		Intent intent = null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            intent=new Intent(Intent.ACTION_OPEN_DOCUMENT, null);
        } else {
        	intent=new Intent(Intent.ACTION_GET_CONTENT, null);
        }
		intent.addCategory(Intent.CATEGORY_OPENABLE); 
		intent.setType("image/*");
		
		//Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		activity.startActivityForResult(Intent.createChooser(intent, "选择图片"), CAMERA_select_DATA);
		
	}
	
	public void startPhotoZoom(Activity activity,Uri uri) {
		Logger.d("SelectPhoto", "startPhotoZoom:"+uri);
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 设置裁剪
		intent.putExtra("crop", "true");
		intent.putExtra("scaleUpIfNeeded", true);
		intent.putExtra("noFaceDetection", true);
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
//		intent.putExtra("return-data", true);
		File file=new File(save_photo_dir,"ftc_"+System.currentTimeMillis() + ".jpg");
		currentPhotoPath=file.getAbsolutePath();
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		activity.startActivityForResult(intent, CAMERA_CROP_DATA);
	}
	
	@SuppressLint("NewApi")
	public void onActivityResult(Activity activity,int requestCode, int resultCode,Intent intent) {
		
		if(resultCode==Activity.RESULT_OK){
			Logger.d("SelectPhoto", "requestCode:"+requestCode);
			if(requestCode==CAMERA_select_DATA){
				Uri uri = intent.getData();
				Logger.d("SelectPhoto", "uri:"+uri);
				if(uri.getAuthority() == null || uri.getAuthority().trim().length() == 0){
					return;
				}
				
				if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT){
				    String wholeID = DocumentsContract.getDocumentId(uri);
				    String id = wholeID.split(":")[1];
				    String[] column = { MediaStore.Images.Media.DATA };
				    String sel = MediaStore.Images.Media._ID + "=?";
				    Cursor cursor = activity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column,
				            sel, new String[] { id }, null);
				    int columnIndex = cursor.getColumnIndex(column[0]);
				    if (cursor.moveToFirst()) {
				    	originalPhotoPath = cursor.getString(columnIndex);
				    }
				    cursor.close();
				}else{
				    String[] projection = { MediaStore.Images.Media.DATA };
				    Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);
				    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				    cursor.moveToFirst();
				    originalPhotoPath = cursor.getString(column_index);
				}

				/*
				Uri uri = intent.getData();
				Logger.d("SelectPhoto", "uri:"+uri);
				if(uri.getAuthority() == null || uri.getAuthority().trim().length() == 0){
					return;
				}
				String[] projection = { MediaStore.Images.Media.DATA };
				Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
				if(cursor==null){
					return;
				}
				int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				if(column_index<0){
					return;
				}
				Logger.d("SelectPhoto", "column_index:"+column_index);
				
				cursor.moveToFirst();
				originalPhotoPath = cursor.getString(column_index);
				try    
                {    
                    //4.0以上的版本会自动关闭 (4.0--14;; 4.0.3--15)    
                    if(Build.VERSION.SDK_INT < 14)    
                    {    
                        cursor.close();    
                    }    
                }catch(Exception e)    
                {    
                	Logger.d("SelectPhoto", "originalPhotoPath:"+e);
                }
                */
				
				Logger.d("SelectPhoto", "originalPhotoPath:"+originalPhotoPath);
				//进行裁剪
				//System.out.println("进行裁剪："+originalPhotoPath);
				if(originalPhotoPath==null||"".equals(originalPhotoPath)){
					return;
				}
				if(isCrop){
					Uri imageuri=Uri.fromFile(new File(originalPhotoPath));
					startPhotoZoom(activity,imageuri);
				}else{
					ZipBitmap(originalPhotoPath);
				}
			}else if(requestCode==CAMERA_CROP_DATA) {
				if(photoProcess!=null){
					Bitmap original=BitMapUtility.originalImgByCompression(currentPhotoPath);
					//Bitmap original = intent.getParcelableExtra("data");
					Bitmap thumbnail=BitMapUtility.scaleImg(original, pWidth, pHeight,false);
					photoProcess.Process(currentPhotoPath,original, thumbnail);
				}
			}
		}
	}
	
	/**
	 * 直接压缩图片，不去裁剪
	 * @param originalPhotoPath
	 */
	public void ZipBitmap(String originalPhotoPath){
		if(photoProcess!=null){
//			Bitmap originalImg = BitMapUtility.originalImg(originalPhotoPath); 
//			if(originalImg==null){
//				return;
//			}
//			currentPhotoPath=BitMapUtility.saveBmpToFile(save_photo_dir,"ftc_temp.png",originalImg);
//			if(TextUtils.isEmpty(currentPhotoPath)){
//				return;
//			}
			Bitmap original=BitMapUtility.originalImgByCompression(originalPhotoPath);
			//Bitmap original = intent.getParcelableExtra("data");
			if(original==null){
				return;
			}
			Bitmap thumbnail=BitMapUtility.scaleImg(original, pWidth, pHeight,false);
			photoProcess.Process(originalPhotoPath,original, thumbnail);
		}
	}
	
	
	public int getOutputX() {
		return outputX;
	}

	public void setOutputX(int outputX) {
		this.outputX = outputX;
	}

	public int getOutputY() {
		return outputY;
	}

	public void setOutputY(int outputY) {
		this.outputY = outputY;
	}
	
	public boolean isCrop() {
		return isCrop;
	}
	
	public void setCrop(boolean isCrop) {
		this.isCrop = isCrop;
	}

	public int getpWidth() {
		return pWidth;
	}

	public void setpWidth(int pWidth) {
		this.pWidth = pWidth;
	}

	public int getpHeight() {
		return pHeight;
	}

	public void setpHeight(int pHeight) {
		this.pHeight = pHeight;
	}
	
}
