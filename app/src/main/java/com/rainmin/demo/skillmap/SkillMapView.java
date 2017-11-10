package com.rainmin.demo.skillmap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.rainmin.demo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 正多边形技能展示图
 * Created by chenming on 2017/11/10
 */

public class SkillMapView extends View {

    private int mSkillCount;  //技能数量或单个正多边形的顶点数
    private int mRadius;  //最外侧正多边形的半径
    private int mIntervalCount;
    private float mAngle;  //夹角度数
    private List<List<PointF>> mPointArray;  //存储所有正多边形的顶点
    private Paint mLinePaint;
    private Paint mTextPaint;

    public SkillMapView(Context context) {
        this(context, null);
    }

    public SkillMapView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkillMapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        initSize();
        initPoints();
    }

    private void initPaint() {
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStrokeWidth(Utils.dp2pxF(getContext(), 2f));

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(Color.BLUE);
        mTextPaint.setTextSize(Utils.sp2pxF(getContext(), 15f));
    }

    private void initSize() {
        mIntervalCount = 4;
        mSkillCount = 5;
        mRadius = Utils.dp2px(getContext(),100);
        mAngle = (float) (2 * Math.PI / mSkillCount);
    }

    private void initPoints() {
        mPointArray = new ArrayList<>();
        float x;
        float y;
        for (int i=0; i<mIntervalCount; i++) {
            List<PointF> points = new ArrayList<>();
            for (int j=0; j<mSkillCount; j++) {
                float radius = mRadius * (mIntervalCount - i) / mIntervalCount;
                x = (float) (radius * Math.cos(j * mAngle - Math.PI / 2));
                y = (float) (radius * Math.sin(j * mAngle - Math.PI / 2));
                points.add(new PointF(x, y));
            }
            mPointArray.add(points);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int viewWidth = getWidth();
        int viewHeight = getHeight();
        canvas.translate(viewWidth / 2, viewHeight / 2);

        drawPolygons(canvas);
        drawText(canvas);
    }

    private void drawPolygons(Canvas canvas) {
        canvas.save();

        mLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        Path path = new Path();
        for (int i= 0; i<mIntervalCount; i++) {
            switch (i) {
                case 0:
                    mLinePaint.setColor(Color.parseColor("#D4F0F3"));
                    break;
                case 1:
                    mLinePaint.setColor(Color.parseColor("#99DCE2"));
                    break;
                case 2:
                    mLinePaint.setColor(Color.parseColor("#56C1C7"));
                    break;
                case 3:
                    mLinePaint.setColor(Color.parseColor("#278891"));
                    break;
            }
            for (int j=0; j<mSkillCount; j++) {
                float x = mPointArray.get(i).get(j).x;
                float y = mPointArray.get(i).get(j).y;
                if (j == 0) {
                    path.moveTo(x,y);
                } else {
                    path.lineTo(x,y);
                }
            }
            path.close();
            canvas.drawPath(path, mLinePaint);
            path.reset();
        }

        canvas.restore();
    }

    private void drawText(Canvas canvas) {
        canvas.save();

        List<PointF> textPoints = new ArrayList<>();
        for (int i=0; i<mSkillCount; i++) {
            float radius = mRadius + Utils.dp2pxF(getContext(), 15f);
            float x = (float) (radius * Math.cos(i * mAngle - Math.PI / 2));
            float y = (float) (radius * Math.sin(i * mAngle - Math.PI / 2));
            textPoints.add(new PointF(x,y));
        }
        Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        for (int i=0; i<mSkillCount; i++) {
            float x = textPoints.get(i).x;
            float y = textPoints.get(i).y - (metrics.ascent + metrics.descent) / 2;
            canvas.drawText(SkillBean.skillNames[i], x, y, mTextPaint);
        }

        canvas.restore();
    }
}
