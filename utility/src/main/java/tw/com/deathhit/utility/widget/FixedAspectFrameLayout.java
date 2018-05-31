/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
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
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import tw.com.deathhit.utility.R;

public class FixedAspectFrameLayout extends FrameLayout {
    protected float layoutWidthRatio = -1;
    protected float layoutHeightRatio = -1;

    public FixedAspectFrameLayout(Context context) {
        super(context);
    }

    public FixedAspectFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FixedAspectFrameLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        if (layoutWidthRatio <= 0.0 || layoutHeightRatio <= 0.0)
            super.onMeasure(widthSpec, heightSpec);
        else {
            double localRatio= layoutWidthRatio/layoutHeightRatio;

            int lockedWidth=MeasureSpec.getSize(widthSpec);
            int lockedHeight=MeasureSpec.getSize(heightSpec);

            if (lockedWidth == 0 && lockedHeight == 0)
                throw new IllegalArgumentException(
                        "Both width and height cannot be zero -- watch out for scrollable containers");

            int hPadding=getPaddingLeft() + getPaddingRight();
            int vPadding=getPaddingTop() + getPaddingBottom();

            lockedWidth-=hPadding;
            lockedHeight-=vPadding;

            if (lockedHeight > 0 && (lockedWidth > lockedHeight * localRatio))
                lockedWidth=(int)(lockedHeight * localRatio + .5);
            else
                lockedHeight=(int)(lockedWidth / localRatio + .5);

            lockedWidth+=hPadding;
            lockedHeight+=vPadding;

            super.onMeasure(MeasureSpec.makeMeasureSpec(lockedWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(lockedHeight, MeasureSpec.EXACTLY));
        }
    }

    private void init(AttributeSet attrs){
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.FixedAspectFrameLayout,
                0, 0);

        try {
            layoutWidthRatio = typedArray.getFloat(R.styleable.FixedAspectFrameLayout_layoutWidthRatio, -1);
            layoutHeightRatio = typedArray.getFloat(R.styleable.FixedAspectFrameLayout_layoutHeightRatio, -1);
        } finally {
            typedArray.recycle();
        }
    }

    public void setAspectRatio(float layoutWidthRatio, float layoutHeightRatio) {
        setAspectRatioInLayout(layoutWidthRatio, layoutHeightRatio);

        if (this.layoutWidthRatio != layoutWidthRatio || this.layoutHeightRatio != layoutHeightRatio)
            requestLayout();

    }

    public void setAspectRatioInLayout(float layoutWidthRatio, float layoutHeightRatio) {
        if (layoutWidthRatio <= 0.0 || layoutHeightRatio <= 0.0)
            throw new IllegalArgumentException(
                    "aspect ratio must be positive");

        if (this.layoutWidthRatio != layoutWidthRatio || this.layoutHeightRatio != layoutHeightRatio) {
            this.layoutWidthRatio = layoutWidthRatio;
            this.layoutHeightRatio = layoutHeightRatio;
        }
    }
}
