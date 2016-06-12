package com.tyl.framework.camera;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.tyl.framework.exception.SDCardException;
import com.tyl.framework.util.bitmap.BitMapUtility;
import com.tyl.framework.util.sdcard.FTSDCard;

public class TakePhoto {
	
	/* 用来标识请求照相功能的activity */
	private static final int CAMERA_WITH_DATA = 3023;
	/* 用来标识请求裁剪图片后的activity */
	private static final int CAMERA_CROP_DATA = 3022;
	
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
	
	
	{
		if(FTSDCard.checkDeviceSDEnable()){
			File file=new File(FTSDCard.getExternalStoragePublicDCIMDirectory(),"mypic");
			if(!file.exists()){
				file.mkdirs();
			}
			save_photo_dir=file.getAbsolutePath();
		}
		
	}
	
	
	
	public TakePhoto() {
		//System.out.println("实例化");
	}




	public void setOnPhotoProcess(IPhotoProcess photoProcess){
		this.photoProcess=photoProcess;
	}
	
	

	
	public void startTakePhoto(Activity activity) throws SDCardException {
		if(!FTSDCard.checkDeviceSDEnable()){
			throw new SDCardException("你没有插入sdcard，请插入sdcard");
		}
		File file = new File(save_photo_dir, "ft_"+System.currentTimeMillis() + ".jpg");
		originalPhotoPath=file.getAbsolutePath();
		//System.out.println("拍照："+originalPhotoPath);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
		activity.startActivityForResult(intent, CAMERA_WITH_DATA);
		//return currentPhotoPath;
	}
	
	public void startPhotoZoom(Activity activity,Uri uri) {
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
	
	public void onActivityResult(Activity activity,int requestCode, int resultCode,Intent intent) {
		if(resultCode==Activity.RESULT_OK){
			if(requestCode==CAMERA_WITH_DATA){
				//进行裁剪
				//System.out.println("进行裁剪："+originalPhotoPath);
				if(originalPhotoPath==null||"".equals(originalPhotoPath)){
					return;
				}
				
				Uri uri=Uri.fromFile(new File(originalPhotoPath));
				if(isCrop){
					startPhotoZoom(activity,uri);
				}else{
					ZipBitmap(originalPhotoPath);
				}
				
			}else if(requestCode==CAMERA_CROP_DATA) {
				if(photoProcess!=null){
					//System.out.println("删除原图处理图像");
					new File(originalPhotoPath).delete();
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
//			//System.out.println("删除原图处理图像");
//			new File(originalPhotoPath).delete();
//			
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
