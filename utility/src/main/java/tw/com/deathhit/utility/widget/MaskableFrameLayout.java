/*Copyright 2015 Christophe Smet.
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
* Modifications copyright (C) 2018 Deathhit
*/
package tw.com.deathhit.utility.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import tw.com.deathhit.utility.R;

/**A frame layout which is used to mask its children.**/
public class MaskableFrameLayout extends FrameLayout {
    private static final int MODE_ADD = 0;
    private static final int MODE_CLEAR = 1;
    private static final int MODE_DARKEN = 2;
    private static final int MODE_DST = 3;
    private static final int MODE_DST_ATOP = 4;
    private static final int MODE_DST_IN = 5;
    private static final int MODE_DST_OUT = 6;
    private static final int MODE_DST_OVER = 7;
    private static final int MODE_LIGHTEN = 8;
    private static final int MODE_MULTIPLY = 9;
    private static final int MODE_OVERLAY = 10;
    private static final int MODE_SCREEN = 11;
    private static final int MODE_SRC = 12;
    private static final int MODE_SRC_ATOP = 13;
    private static final int MODE_SRC_IN = 14;
    private static final int MODE_SRC_OUT = 15;
    private static final int MODE_SRC_OVER = 16;
    private static final int MODE_XOR = 17;

    private Handler handler;

    private Drawable drawableMask;

    private Bitmap finalMask;

    private Paint paint;
    private PorterDuffXfermode porterDuffMode;

    public MaskableFrameLayout(Context context) {
        super(context);
    }

    public MaskableFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MaskableFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if(finalMask == null || paint == null)
            return;

        paint.setXfermode(porterDuffMode);

        canvas.drawBitmap(finalMask, 0.0f, 0.0f, paint);

        paint.setXfermode(null);
    }

    @Override
    public void invalidateDrawable(@Nullable Drawable drawable) {
        if(drawable == null)
            return;

        initMask(drawable);

        swapBitmapMask(makeBitmapMask(drawable));

        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        setSize(w, h);
    }

    @Override
    public void scheduleDrawable(@Nullable Drawable who,@Nullable Runnable what, long when) {
        if (who != null && what != null)
            handler.postAtTime(what, when);
    }

    @Override
    public void unscheduleDrawable(@Nullable Drawable who, @Nullable Runnable what) {
        if (who != null && what != null)
            handler.removeCallbacks(what);
    }

    private void init(AttributeSet attrs) {
        handler = new Handler();

        setDrawingCacheEnabled(true);
        setLayerType(LAYER_TYPE_SOFTWARE, null); //Only works for software layers

        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MaskableFrameLayout,
                0, 0);
        try {
            porterDuffMode = getModeFromInteger(typedArray.getInteger(R.styleable.MaskableFrameLayout_porterDuffMode, 0));

            drawableMask = initMask(typedArray.getDrawable(R.styleable.MaskableFrameLayout_drawableMask));

            paint = createPaint(typedArray.getBoolean(R.styleable.MaskableFrameLayout_antiAliasing, false));

        } finally {
            typedArray.recycle();
        }

        registerMeasure();
    }

    private Paint createPaint(boolean antiAliasing) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setAntiAlias(antiAliasing);
        paint.setXfermode(porterDuffMode);

        return paint;
    }

    private PorterDuffXfermode getModeFromInteger(int index) {
        PorterDuff.Mode mode;

        switch (index) {
            case MODE_ADD:
                mode = PorterDuff.Mode.ADD;
                break;
            case MODE_CLEAR:
                mode = PorterDuff.Mode.CLEAR;
                break;
            case MODE_DARKEN:
                mode = PorterDuff.Mode.DARKEN;
                break;
            case MODE_DST:
                mode = PorterDuff.Mode.DST;
                break;
            case MODE_DST_ATOP:
                mode = PorterDuff.Mode.DST_ATOP;
                break;
            case MODE_DST_IN:
                mode = PorterDuff.Mode.DST_IN;
                break;
            case MODE_DST_OUT:
                mode = PorterDuff.Mode.DST_OUT;
                break;
            case MODE_DST_OVER:
                mode = PorterDuff.Mode.DST_OVER;
                break;
            case MODE_LIGHTEN:
                mode = PorterDuff.Mode.LIGHTEN;
                break;
            case MODE_MULTIPLY:
                mode = PorterDuff.Mode.MULTIPLY;
                break;
            case MODE_OVERLAY:
                mode = PorterDuff.Mode.OVERLAY;
                break;
            case MODE_SCREEN:
                mode = PorterDuff.Mode.SCREEN;
                break;
            case MODE_SRC:
                mode = PorterDuff.Mode.SRC;
                break;
            case MODE_SRC_ATOP:
                mode = PorterDuff.Mode.SRC_ATOP;
                break;
            case MODE_SRC_IN:
                mode = PorterDuff.Mode.SRC_IN;
                break;
            case MODE_SRC_OUT:
                mode = PorterDuff.Mode.SRC_OUT;
                break;
            case MODE_SRC_OVER:
                mode = PorterDuff.Mode.SRC_OVER;
                break;
            case MODE_XOR:
                mode = PorterDuff.Mode.XOR;
                break;
            default:
                mode = PorterDuff.Mode.DST_IN;
        }

        return new PorterDuffXfermode(mode);
    }

    private Bitmap makeBitmapMask(@Nullable Drawable drawable) {
        int width = getMeasuredWidth(), height = getMeasuredHeight();

        if(drawable == null || width <= 0 || height <= 0)
            return null;

        Bitmap mask = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(mask);

        drawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
        drawable.draw(canvas);

        return mask;
    }

    private Drawable initMask(@Nullable Drawable input) {
        if(input == null)
            return null;

        drawableMask = input;

        if (drawableMask instanceof AnimationDrawable)
            drawableMask.setCallback(this);

        return input;
    }

    private void registerMeasure() {
        final ViewTreeObserver treeObserver = MaskableFrameLayout.this.getViewTreeObserver();

        if (treeObserver != null && treeObserver.isAlive()) {
            treeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ViewTreeObserver aliveObserver = treeObserver;

                    if (!aliveObserver.isAlive())
                        aliveObserver = MaskableFrameLayout.this.getViewTreeObserver();

                    if (aliveObserver != null)
                        aliveObserver.removeOnGlobalLayoutListener(this);

                    swapBitmapMask(makeBitmapMask(drawableMask));
                }
            });
        }
    }

    private void setSize(int width, int height) {
        if(width <= 0 || height <= 0 || drawableMask == null)
            return;

        swapBitmapMask(makeBitmapMask(drawableMask));
    }

    private void swapBitmapMask(@Nullable Bitmap newMask) {
        if(newMask == null)
            return;

        if (finalMask != null && !finalMask.isRecycled())
            finalMask.recycle();

        finalMask = newMask;
    }

    public void setMask(int drawableRes) {
        Resources res = getResources();

        if (res != null)
            setMask(res.getDrawable(drawableRes));

    }

    public void setMask(@Nullable Drawable input) {
        initMask(input);

        swapBitmapMask(makeBitmapMask(drawableMask));

        invalidate();
    }
}
