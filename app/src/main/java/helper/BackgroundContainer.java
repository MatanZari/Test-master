package helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.zari.matan.testapk.R;

/**
 * Created by Matan on 5/31/2015.
 */
public class BackgroundContainer extends FrameLayout {

    boolean mShowing = false;
    Drawable mShadowedBackground;
    int mOpenAreaTop, mOpenAreaBottom, mOpenAreaHeight;
    boolean mUpdateBounds = false;
    public String color;

    public BackgroundContainer(Context context) {
        super(context);
        init();


    }

    public BackgroundContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BackgroundContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mShadowedBackground = getResources().getDrawable(R.drawable.shadowed_background);

    }

    public void showBackground(int top, int bottom) {
        setWillNotDraw(false);
        mOpenAreaTop = top;
        mOpenAreaHeight = bottom;
        mShowing = true;
        mUpdateBounds = true;
    }

    public void hideBackground() {
        setWillNotDraw(true);
        mShowing = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mShowing) {


//            if (mUpdateBounds) {
//                mShadowedBackground.setBounds(0, 0, getWidth(), mOpenAreaHeight);
//
//            }
            //canvas.drawColor(R.color.colorAccent);
            canvas.save();
            canvas.translate(0, mOpenAreaTop);

            mShadowedBackground.draw(canvas);
            canvas.restore();
        }
    }

    public void setColor(String color) {
        this.color = color;
    }


}
