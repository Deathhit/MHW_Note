/*
 * Copyright 2015 yqritc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Modifications copyright (C) 2018 Deathhit
 */

package tw.com.deathhit.utility.function;

import android.graphics.Matrix;

/**ScaleManager which provides scale matrix according to given width and height.*/
public final class ScaleManager {
    private static int viewWidth;
    private static int viewHeight;

    private static int contentWidth;
    private static int contentHeight;

    public static Matrix getScaleMatrix(int viewWidth, int viewHeight, int contentWidth, int contentHeight, ScaleType scaleType) {
        ScaleManager.viewWidth = viewWidth;
        ScaleManager.viewHeight = viewHeight;
        ScaleManager.contentWidth = contentWidth;
        ScaleManager.contentHeight = contentHeight;

        switch (scaleType) {
            case NONE:
                return getNoScale();

            case FIT_XY:
                return fitXY();
            case FIT_CENTER:
                return fitCenter();
            case FIT_START:
                return fitStart();
            case FIT_END:
                return fitEnd();

            case LEFT_TOP:
                return getOriginalScale(ScaleType.LEFT_TOP);
            case LEFT_CENTER:
                return getOriginalScale(ScaleType.LEFT_CENTER);
            case LEFT_BOTTOM:
                return getOriginalScale(ScaleType.LEFT_BOTTOM);
            case CENTER_TOP:
                return getOriginalScale(ScaleType.CENTER_TOP);
            case CENTER:
                return getOriginalScale(ScaleType.CENTER);
            case CENTER_BOTTOM:
                return getOriginalScale(ScaleType.CENTER_BOTTOM);
            case RIGHT_TOP:
                return getOriginalScale(ScaleType.RIGHT_TOP);
            case RIGHT_CENTER:
                return getOriginalScale(ScaleType.RIGHT_CENTER);
            case RIGHT_BOTTOM:
                return getOriginalScale(ScaleType.RIGHT_BOTTOM);

            case LEFT_TOP_CROP:
                return getCropScale(ScaleType.LEFT_TOP);
            case LEFT_CENTER_CROP:
                return getCropScale(ScaleType.LEFT_CENTER);
            case LEFT_BOTTOM_CROP:
                return getCropScale(ScaleType.LEFT_BOTTOM);
            case CENTER_TOP_CROP:
                return getCropScale(ScaleType.CENTER_TOP);
            case CENTER_CROP:
                return getCropScale(ScaleType.CENTER);
            case CENTER_BOTTOM_CROP:
                return getCropScale(ScaleType.CENTER_BOTTOM);
            case RIGHT_TOP_CROP:
                return getCropScale(ScaleType.RIGHT_TOP);
            case RIGHT_CENTER_CROP:
                return getCropScale(ScaleType.RIGHT_CENTER);
            case RIGHT_BOTTOM_CROP:
                return getCropScale(ScaleType.RIGHT_BOTTOM);

            case START_INSIDE:
                return startInside();
            case CENTER_INSIDE:
                return centerInside();
            case END_INSIDE:
                return endInside();

            default:
                return null;
        }
    }

    private static Matrix getMatrix(float sx, float sy, float px, float py) {
        Matrix matrix = new Matrix();

        matrix.setScale(sx, sy, px, py);

        return matrix;
    }

    private static Matrix getMatrix(float sx, float sy, ScaleType ScaleType) {
        switch (ScaleType) {
            case LEFT_TOP:
                return getMatrix(sx, sy, 0, 0);
            case LEFT_CENTER:
                return getMatrix(sx, sy, 0, viewHeight / 2f);
            case LEFT_BOTTOM:
                return getMatrix(sx, sy, 0, viewHeight);
            case CENTER_TOP:
                return getMatrix(sx, sy, viewWidth / 2f, 0);
            case CENTER:
                return getMatrix(sx, sy, viewWidth / 2f, viewHeight / 2f);
            case CENTER_BOTTOM:
                return getMatrix(sx, sy, viewWidth / 2f, viewHeight);
            case RIGHT_TOP:
                return getMatrix(sx, sy, viewWidth, 0);
            case RIGHT_CENTER:
                return getMatrix(sx, sy, viewWidth, viewHeight / 2f);
            case RIGHT_BOTTOM:
                return getMatrix(sx, sy, viewWidth, viewHeight);
            default:
                throw new IllegalArgumentException("Illegal ScaleType");
        }
    }

    private static Matrix getNoScale() {
        float sx = contentWidth / (float) viewWidth;
        float sy = contentHeight / (float) viewHeight;

        return getMatrix(sx, sy, ScaleType.LEFT_TOP);
    }

    private static Matrix getFitScale(ScaleType ScaleType) {
        float sx = (float) viewWidth / contentWidth;
        float sy = (float) viewHeight / contentHeight;

        float minScale = Math.min(sx, sy);

        sx = minScale / sx;
        sy = minScale / sy;

        return getMatrix(sx, sy, ScaleType);
    }

    private static Matrix fitXY() {
        return getMatrix(1, 1, ScaleType.LEFT_TOP);
    }

    private static Matrix fitStart() {
        return getFitScale(ScaleType.LEFT_TOP);
    }

    private static Matrix fitCenter() {
        return getFitScale(ScaleType.CENTER);
    }

    private static Matrix fitEnd() {
        return getFitScale(ScaleType.RIGHT_BOTTOM);
    }

    private static Matrix getOriginalScale(ScaleType ScaleType) {
        float sx = contentWidth / (float) viewWidth;
        float sy = contentHeight / (float) viewHeight;

        return getMatrix(sx, sy, ScaleType);
    }

    private static Matrix getCropScale(ScaleType ScaleType) {
        float sx = (float) viewWidth / contentWidth;
        float sy = (float) viewHeight / contentHeight;

        float maxScale = Math.max(sx, sy);

        sx = maxScale / sx;
        sy = maxScale / sy;

        return getMatrix(sx, sy, ScaleType);
    }

    private static Matrix startInside() {
        if (contentWidth <= viewWidth
                && contentHeight <= viewHeight)
            return getOriginalScale(ScaleType.LEFT_TOP);
        else
            return fitStart();
    }

    private static Matrix centerInside() {
        if (contentHeight <= viewWidth
                && contentHeight <= viewHeight)
            return getOriginalScale(ScaleType.CENTER);
        else
            return fitCenter();
    }

    private static Matrix endInside() {
        if (contentHeight <= viewWidth
                && contentHeight <= viewHeight)
            return getOriginalScale(ScaleType.RIGHT_BOTTOM);
        else
            return fitEnd();
    }

    public enum ScaleType {
        NONE,

        FIT_XY,
        FIT_START,
        FIT_CENTER,
        FIT_END,

        LEFT_TOP,
        LEFT_CENTER,
        LEFT_BOTTOM,
        CENTER_TOP,
        CENTER,
        CENTER_BOTTOM,
        RIGHT_TOP,
        RIGHT_CENTER,
        RIGHT_BOTTOM,

        LEFT_TOP_CROP,
        LEFT_CENTER_CROP,
        LEFT_BOTTOM_CROP,
        CENTER_TOP_CROP,
        CENTER_CROP,
        CENTER_BOTTOM_CROP,
        RIGHT_TOP_CROP,
        RIGHT_CENTER_CROP,
        RIGHT_BOTTOM_CROP,

        START_INSIDE,
        CENTER_INSIDE,
        END_INSIDE
    }
}
