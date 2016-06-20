package com.rvitemtouch.sun.risingapplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

public class ZanActivity extends Activity {
    private RelativeLayout zan_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zan);
        zan_container = (RelativeLayout) findViewById(R.id.zan_container);
        zan_container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();

                final int action = MotionEventCompat.getActionMasked(event);
                switch (action) {
                    case MotionEvent.ACTION_DOWN://单点触摸动作
                        break;
                    case MotionEvent.ACTION_UP://单点触摸离开动作
                        ImageView imageView = new ImageView(ZanActivity.this);
                        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        final int width = imageView.getWidth();
                        final int height = imageView.getHeight();
                        param.setMargins((int) x - width / 2, (int) y - height / 2, 0, 0);
                        imageView.setLayoutParams(param);
                        imageView.setImageResource(R.drawable.zan_light_done);
                        zan_container.addView(imageView);
                        startAnim(x, y, imageView);
                        break;
                    case MotionEvent.ACTION_MOVE://触摸点移动动作
                        break;
                    case MotionEvent.ACTION_CANCEL://触摸动作取消
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN://多点触摸动作
                        break;
                    case MotionEvent.ACTION_POINTER_UP://多点离开动作
                        break;
                }
                return true;
            }
        });
    }

    private void startAnim(final float x, final float y, final ImageView imageView) {

        RotateAnimation rotateAnimation = createRandom();
        AnimationSet animSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        rotateAnimation.setDuration(600);//设置动画持续时间
        alphaAnimation.setDuration(1000);//设置动画持续时间
        alphaAnimation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        /** 常用方法 */
//        animation.setRepeatCount(int repeatCount);//设置重复次数
        //animation.setStartOffset(long startOffset);//执行前的等待时间
        animSet.addAnimation(rotateAnimation);
        animSet.addAnimation(alphaAnimation);
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.i("SHF", "x-->" + x + "y--->" + y);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (zan_container.getChildCount() > 0) {
                    //Attempt to read from field 'int android.view.View.mViewFlags' on a null
                    //加动画图片这边也在移除动画解决办法
                    new Handler().post(new Runnable() {
                        public void run() {
                            imageView.clearAnimation();
                            zan_container.removeView(imageView);
                        }
                    });
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        imageView.startAnimation(animSet);
    }

    /**
     * 随机角度
     *
     * @return
     */
    private RotateAnimation createRandom() {
        int i = new Random().nextInt(5);
        switch (i) {
            case 0:
                return new RotateAnimation(0f, 15f, Animation.RELATIVE_TO_SELF,
                        0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            case 1:
                return new RotateAnimation(0f, 30f, Animation.RELATIVE_TO_SELF,
                        0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            case 2:
                return new RotateAnimation(0f, 0f, Animation.RELATIVE_TO_SELF,
                        0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            case 3:
                return new RotateAnimation(0f, -15f, Animation.RELATIVE_TO_SELF,
                        0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            case 4:
                return new RotateAnimation(0f, -30f, Animation.RELATIVE_TO_SELF,
                        0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        }
        return new RotateAnimation(0f, 30f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    }
}
