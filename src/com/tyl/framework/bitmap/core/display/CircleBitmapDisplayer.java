package com.tyl.framework.bitmap.core.display;

import com.tyl.framework.bitmap.CircleDrawable;
import com.tyl.framework.bitmap.core.assist.LoadedFrom;
import com.tyl.framework.bitmap.core.imageaware.ImageAware;
import com.tyl.framework.bitmap.core.imageaware.ImageViewAware;

import android.graphics.Bitmap;



/**
 * Can display bitmap with Circle. This implementation works only with ImageViews wrapped
 * in ImageViewAware.
 * <br />
 * This implementation is inspired by
 * <a href="http://www.curious-creature.org/2012/12/11/android-recipe-1-image-with-rounded-corners/">
 * Romain Guy's article</a>. It rounds images using custom drawable drawing. Original bitmap isn't changed.
 * <br />
 * <br />
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.5.6
 */
public class CircleBitmapDisplayer  implements BitmapDisplayer{

	@Override
	public void display(Bitmap bitmap, ImageAware imageAware,
			LoadedFrom loadedFrom) {
		
		if (!(imageAware instanceof ImageViewAware)) {
            throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
        }

        imageAware.setImageDrawable(new CircleDrawable(bitmap));
	}

}
