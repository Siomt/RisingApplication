package com.rvitemtouch.sun.risingapplication.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.rvitemtouch.sun.risingapplication.R;

import java.util.Random;


/**
 * Created by sun on 2016/6/16.
 * 上升气泡动画效果
 */
public class RisingBubbleWidget extends RelativeLayout implements BaseLayoutInterface {
    private static final String TAG = "RisingBubbleWidget";
    private int mStarWidth;
    private int mStarHeight;
    // widget 的width
    private int mWidth;
    // widget 的height
    private int mHeight;
    private Drawable mStarDrawable;
    private Random mRandom = new Random();
    private static final int STAR_RIGHT_MARGIN = 57;
    private ObjectAnimator mEnterAnim;
    private ValueAnimator mBezierAnim;
    private AnimatorSet mStarAnimSet;
    private LayoutParams mParams;

    public RisingBubbleWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RisingBubbleWidget(Context context) {
        super(context);
        init();
    }

    public RisingBubbleWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 五星气泡
        mStarDrawable = getContext().getResources().getDrawable(R.drawable.icon_five_stars);
        // 获取图片的宽高
        mStarWidth = mStarDrawable.getIntrinsicWidth();
        mStarHeight = mStarDrawable.getIntrinsicHeight();

        mParams = new LayoutParams(mStarWidth, mStarHeight);
        mParams.addRule(ALIGN_PARENT_BOTTOM, TRUE);
        mParams.addRule(ALIGN_PARENT_RIGHT, TRUE);
        mParams.setMargins(0, 0, DensityUtility.dip2px(getContext(), STAR_RIGHT_MARGIN), 0);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                playRisingBubbleAnim();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    /**
     * 开始上升五星气泡动画
     */
    public void playRisingBubbleAnim() {
        ImageView starImageView = new ImageView(getContext());
        starImageView.setImageDrawable(mStarDrawable);
        starImageView.setLayoutParams(mParams);

        // 添加五星
        addView(starImageView);
        startRisingAnim(starImageView);
    }

    /**
     * 播放上升动画
     */
    private void startRisingAnim(final ImageView starImageView) {
        // 放大淡入动画
        PropertyValuesHolder pScaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0.5f, 1f);
        PropertyValuesHolder pScaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.5f, 1f);
        PropertyValuesHolder pAlpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0.5f, 1f);
        PropertyValuesHolder pRotation = PropertyValuesHolder.ofFloat(View.ROTATION, 0, getRandomDegree());
        mEnterAnim = ObjectAnimator.ofPropertyValuesHolder(starImageView, pScaleX, pScaleY, pAlpha, pRotation);
        mEnterAnim.setDuration(100);
        mEnterAnim.setInterpolator(new AccelerateInterpolator());

        //贝塞尔曲线动画
        BezierEvaluator bezierEvaluator = new BezierEvaluator();
        mBezierAnim = ValueAnimator.ofObject(bezierEvaluator,
                new PointF(mWidth - mStarWidth - DensityUtility.dip2px(getContext(), STAR_RIGHT_MARGIN), mHeight - mStarHeight),
                new PointF(2 * mWidth / 3 + mRandom.nextInt(mWidth / 3), (mHeight - mStarHeight) / 2));
        mBezierAnim.setDuration(2500);
        mBezierAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        mBezierAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //这里获取到贝塞尔曲线计算出来的x,y赋值给view,这样就能让五星随着曲线走
                PointF pointF = (PointF) valueAnimator.getAnimatedValue();
                starImageView.setX(pointF.x);
                starImageView.setY(pointF.y);
                //加上 alpha 动画
                starImageView.setAlpha(1 - valueAnimator.getAnimatedFraction());
            }
        });
        mBezierAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //因为不停的add,导致子view只增不减，所以在view动画结束后remove
                removeView(starImageView);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        mStarAnimSet = new AnimatorSet();
        mStarAnimSet.playSequentially(mEnterAnim, mBezierAnim);
        mStarAnimSet.start();
    }

    /**
     * 获取随机角度
     */
    private float getRandomDegree() {
        float degree;
        int randomInt = mRandom.nextInt(5);
        switch (randomInt) {
            case 0:
                degree = -15f;
                break;
            case 1:
                degree = -30f;
                break;
            case 2:
                degree = 0f;
                break;
            case 3:
                degree = 15f;
                break;
            case 4:
                degree = 30f;
                break;
            default:
                degree = 30f;
                break;
        }
        return degree;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    /**
     * 防止内存溢出、
     */
    @Override
    public void onDestroy() {
        if (mEnterAnim != null) {
            mEnterAnim.removeAllListeners();
            mEnterAnim.cancel();
        }
        if (mBezierAnim != null) {
            mBezierAnim.removeAllListeners();
            mBezierAnim.cancel();
        }
        if (mStarAnimSet != null) {
            mStarAnimSet.removeAllListeners();
            mStarAnimSet.cancel();
        }
    }

    @Override
    public void onStop() {

    }

    @Override
    public void onStart() {

    }
}
