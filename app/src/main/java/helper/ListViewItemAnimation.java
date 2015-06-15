package helper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ListView;

import com.zari.matan.testapk.MainActivity;
import com.zari.matan.testapk.PageActivity;

/**
 * Created by Matan on 5/31/2015
 */
public class ListViewItemAnimation {

    Context context;
    View listItem;
    ListView mListView;
    SwipeCallback swipeCallback;

    public ListViewItemAnimation(Context context, View listItem, ListView listView, SwipeCallback SwipeCallback) {
        this.context = context;
        this.listItem = listItem;
        mListView = listView;
        this.swipeCallback = SwipeCallback;

    }


    BackgroundContainer mBackgroundContainer;
    boolean mSwiping = false;
    boolean mItemPressed = false;
    private static final int SWIPE_DURATION = 250;



    public void setBackgroundContainer(BackgroundContainer mBackgroundContainer) {
        this.mBackgroundContainer = mBackgroundContainer;


    }

    public View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        float mDownX;
        private int mSwipeSlop = -1;

        @Override
        public boolean onTouch(final View v, MotionEvent event) {
            listItem = v;
            if (mSwipeSlop < 0) {
                mSwipeSlop = ViewConfiguration.get(context).
                        getScaledTouchSlop();
            }
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (mItemPressed) {

                        return false;
                    }
                    mItemPressed = true;
                    mDownX = event.getX();

                    break;
                case MotionEvent.ACTION_CANCEL:
                    listItem.setAlpha(1);
                    listItem.setTranslationX(0);
                    mItemPressed = false;
                    break;
                case MotionEvent.ACTION_MOVE: {
                    float x = event.getX() + listItem.getTranslationX();
                    float deltaX = x - mDownX;
                    float deltaXAbs = Math.abs(deltaX);
                    if (!mSwiping) {
                        if (deltaXAbs > mSwipeSlop) {
                            mSwiping = true;
                            mListView.requestDisallowInterceptTouchEvent(true);
                            mBackgroundContainer.showBackground(listItem.getTop(), listItem.getHeight());
                        }
                    }
                    if (mSwiping) {
                        if (x < mDownX) {
                            listItem.setTranslationX((x - mDownX));
                            listItem.setAlpha(1 - deltaXAbs / listItem.getWidth());
                        } else
                            mSwiping = false;
                    }
                }
                break;
                case MotionEvent.ACTION_UP: {
                    // User let go - figure out whether to animate the view out, or back into place
                    if (mSwiping) {
                        float x = event.getX() + listItem.getTranslationX();
                        float deltaX = x - mDownX;
                        float deltaXAbs = Math.abs(deltaX);
                        float fractionCovered;
                        float endX;
                        float endAlpha;
                        final boolean openUrl;
                        if (deltaXAbs > listItem.getWidth() / 3) {
                            // Greater than a quarter of the width - animate it out
                            fractionCovered = deltaXAbs / listItem.getWidth();
                            endX = deltaX < 0 ? -listItem.getWidth() : listItem.getWidth();
                            endAlpha = 0;
                            openUrl = true;
                        } else {
                            // Not far enough - animate it back
                            fractionCovered = 1 - (deltaXAbs / listItem.getWidth());
                            endX = 0;
                            endAlpha = 1;
                            openUrl = false;
                        }
                        // Animate position and alpha of swiped item
                        // NOTE: This is a simplified version of swipe behavior, for the
                        // purposes of this demo about animation. A real version should use
                        // velocity (via the VelocityTracker class) to send the item off or
                        // back at an appropriate speed.
                        long duration = (int) ((1 - fractionCovered) * SWIPE_DURATION);
                        mListView.setEnabled(false);
                        listItem.animate().setDuration(duration).
                                alpha(endAlpha).translationX(endX).
                                withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Restore animated values
                                        listItem.setAlpha(1);
                                        listItem.setTranslationX(0);
                                        if (openUrl) {
                                            swipeCallback.onSwipe();
                                            mListView.setEnabled(true);
                                        } else {
                                            mBackgroundContainer.hideBackground();
                                            mSwiping = false;
                                            mListView.setEnabled(true);
                                        }
                                    }
                                });
                    }else{
                        v.performClick();
                    }
                }
                mItemPressed = false;
                break;
                default:
                    return false;
            }
            return true;
        }
    };

    public void setListItem(View listItem) {
        this.listItem = listItem;
    }


    public interface SwipeCallback {
        void onSwipe();
    }
}
