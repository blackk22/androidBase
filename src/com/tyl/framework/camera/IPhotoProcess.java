package com.tyl.framework.camera;

import android.graphics.Bitmap;

public interface IPhotoProcess {
	/**
	 *  * 图像处理操作
	 * @param original 压缩的原图路径
	 * @param thumbnail  缩略图路径
	 * @param originalpath 原图的路径
	 */
	public void Process(String originalpath,Bitmap original ,Bitmap thumbnail);
}
