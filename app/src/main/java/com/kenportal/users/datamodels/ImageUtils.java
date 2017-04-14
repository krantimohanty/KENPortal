package com.kenportal.users.datamodels;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by kranti on 11/26/2016.
 */
public class ImageUtils {

    Bitmap bitmap;
    ImageView imageView;

    public ImageUtils() {
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
