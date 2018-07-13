/*
 * Adapted from anorth at https://gist.github.com/anorth/9845602.
 * by cami7ord on Sept 20 - 2017.
 *
 * Adapted from cami7ord at https://gist.github.com/cami7ord/0ce6f36a28d36bf17d96284f2cf75ae9.
 * by Deathhit on May 31 - 2018.
 */
package tw.com.deathhit.mhw_note.utility.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.FrameLayout;

public class ZoomFrameLayout extends FrameLayout implements ScaleGestureDetector.OnScaleGestureListener {
    private static final float MIN_ZOOM = 1.0f;
    private static final float MAX_ZOOM = 4.0f;

    private enum Mode {
        NONE,
        DRAG,
        ZOOM
    }

    private Mode mode = Mode.NONE;
    private float scale = 1.0f;
    private float lastScaleFactor = 0f;

    // Where the finger first  touches the screen
    private float startX = 0f;
    private float startY = 0f;

    // How much to translate the canvas
    private float dx = 0f;
    private float dy = 0f;
    private float prevDx = 0f;
    private float prevDy = 0f;

    // Custom vars to handle double tap
    private boolean firstTouch = false;
    private long time = System.currentTimeMillis();
    private boolean restore = false;

    public ZoomFrameLayout(Context context) {
        super(context);
        init(context);
    }

    public ZoomFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ZoomFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {

        final ScaleGestureDetector scaleDetector = new ScaleGestureDetector(context, this);
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        if(firstTouch && (System.currentTimeMillis() - time) <= 300) {
                            //do stuff here for double tap
                            if(restore) {
                                scale = 1.0f;
                                restore = false;
                            } else {
                                scale *= 2.0f;
                                restore = true;
                            }
                            mode = Mode.ZOOM;
                            firstTouch = false;

                        } else {
                            if (scale > MIN_ZOOM) {
                                mode = Mode.DRAG;
                                startX = motionEvent.getX() - prevDx;
                                startY = motionEvent.getY() - prevDy;
                            }
                            firstTouch = true;
                            time = System.currentTimeMillis();
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mode == Mode.DRAG) {
                            dx = motionEvent.getX() - startX;
                            dy = motionEvent.getY() - startY;
                        }
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        mode = Mode.ZOOM;
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        mode = Mode.NONE;
                        break;
                    case MotionEvent.ACTION_UP:
                        mode = Mode.NONE;
                        prevDx = dx;
                        prevDy = dy;
                        break;
                }
                scaleDetector.onTouchEvent(motionEvent);

                if ((mode == Mode.DRAG && scale >= MIN_ZOOM) || mode == Mode.ZOOM) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    float maxDx = (child().getWidth() - (child().getWidth() / scale)) / 2 * scale;
                    float maxDy = (child().getHeight() - (child().getHeight() / scale))/ 2 * scale;
                    dx = Math.min(Math.max(dx, -maxDx), maxDx);
                    dy = Math.min(Math.max(dy, -maxDy), maxDy);

                    applyScaleAndTranslation();
                }

                return true;
            }
        });
    }

    // ScaleGestureDetector

    @Override
    public boolean onScaleBegin(ScaleGestureDetector scaleDetector) {
        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector scaleDetector) {
        float scaleFactor = scaleDetector.getScaleFactor();

        if (lastScaleFactor == 0 || (Math.signum(scaleFactor) == Math.signum(lastScaleFactor))) {
            scale *= scaleFactor;
            scale = Math.max(MIN_ZOOM, Math.min(scale, MAX_ZOOM));
            lastScaleFactor = scaleFactor;
        } else {
            lastScaleFactor = 0;
        }
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector scaleDetector) {

    }

    private void applyScaleAndTranslation() {
        child().setScaleX(scale);
        child().setScaleY(scale);
        child().setTranslationX(dx);
        child().setTranslationY(dy);
    }

    private View child() {
        return getChildAt(0);
    }
}
