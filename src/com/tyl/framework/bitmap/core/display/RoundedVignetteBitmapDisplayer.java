/*******************************************************************************
 * Copyright 2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.tyl.framework.bitmap.core.display;

import android.graphics.*;

import com.tyl.framework.bitmap.RoundedVignetteDrawable;
import com.tyl.framework.bitmap.core.assist.LoadedFrom;
import com.tyl.framework.bitmap.core.imageaware.ImageAware;
import com.tyl.framework.bitmap.core.imageaware.ImageViewAware;

/**
 * Can display bitmap with rounded corners and vignette effect. This implementation works only with ImageViews wrapped
 * in ImageViewAware.
 * <br />
 * This implementation is inspired by
 * <a href="http://www.curious-creature.org/2012/12/11/android-recipe-1-image-with-rounded-corners/">
 * Romain Guy's article</a>. It rounds images using custom drawable drawing. Original bitmap isn't changed.
 * <br />
 * <br />
 * If this implementation doesn't meet your needs then consider
 * <a href="https://github.com/vinc3m1/RoundedImageView">this project</a> for usage.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.9.1
 */
public class RoundedVignetteBitmapDisplayer extends RoundedBitmapDisplayer {

	public RoundedVignetteBitmapDisplayer(int cornerRadiusPixels, int marginPixels) {
		super(cornerRadiusPixels, marginPixels);
	}

	@Override
	public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
		if (!(imageAware instanceof ImageViewAware)) {
			throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
		}

		imageAware.setImageDrawable(new RoundedVignetteDrawable(bitmap, cornerRadius, margin));
	}

	
}
