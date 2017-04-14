package com.kenportal.users.progress_dialog.custom_progress;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.animation.LinearInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;

import java.util.ArrayList;
import java.util.List;

import com.kenportal.users.R;

/**
 * Created by kranti on 11/06/2016.
 */
public class SquareSpinIndicator extends BaseIndicatorController {


    public void draw(Context context, Canvas canvas, Paint paint) {

        Bitmap myBitmap = BitmapFactory.decodeResource(
                context.getResources(),
                R.mipmap.ic_launcher);
        canvas.drawBitmap(myBitmap, null, new RectF(getWidth() / 5, getHeight() / 5, getWidth() * 4 / 5, getHeight() * 4 / 5), paint);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawRect(new RectF(getWidth() / 5, getHeight() / 5, getWidth() * 4 / 5, getHeight() * 4 / 5), paint);
    }


    @Override
    public List<Animator> createAnimation() {
        List<Animator> animators = new ArrayList<>();
        PropertyValuesHolder rotation5 = PropertyValuesHolder.ofFloat("rotationX", 0, 180, 180, 0, 0);
        PropertyValuesHolder rotation6 = PropertyValuesHolder.ofFloat("rotationY", 0, 0, 180, 180, 0);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(getTarget(), rotation6, rotation5);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(-1);
        animator.setDuration(2500);
        animator.start();
        animators.add(animator);
        return animators;
    }
}
