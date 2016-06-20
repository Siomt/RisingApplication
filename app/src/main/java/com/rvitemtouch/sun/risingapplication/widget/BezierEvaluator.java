package com.rvitemtouch.sun.risingapplication.widget;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * Created by sun on 2016/6/16.
 * 贝塞尔曲线效果
 */
public class BezierEvaluator implements TypeEvaluator<PointF> {

    @Override
    public PointF evaluate(float time, PointF startValue, PointF endValue) {
        PointF point1 = new PointF((startValue.x + endValue.x) / 2, (startValue.y + endValue.y) / 2);
        if (time < 0 || time > 1) {
            throw new IllegalArgumentException("time must between 0 and 1");
        }
        float timeLeft = 1.0f - time;
        PointF pointF = new PointF();
        pointF.x = timeLeft * timeLeft * startValue.x
                + 2 * time * timeLeft * point1.x
                + time * time * endValue.x;
        pointF.y = timeLeft * timeLeft * startValue.y
                + 2 * time * timeLeft * point1.y
                + time * time * endValue.y;
        return pointF;
    }
}
