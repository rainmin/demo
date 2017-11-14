package com.rainmin.demo.skillmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.rainmin.demo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 正多边形技能分布图
 * Created by chenming on 2017/11/10
 */

public class SkillMapView extends View {

    private int mSkillCount;  //技能数量或单个正多边形的顶点数
    private int mRadius;  //最外侧正多边形的半径
    private int mIntervalCount;  //层级数
    private float mAngle;  //夹角度数
    private List<List<PointF>> mPointArray;  //存储所有正多边形的顶点
    private Paint mLinePaint;
    private Paint mTextPaint;
    private SkillBean mSkillBean;

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

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mLinePaint.setStrokeWidth(Utils.dp2pxF(getContext(), 2f));

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(Color.BLUE);
        mTextPaint.setTextSize(Utils.sp2pxF(getContext(), 15f));
    }

    /**
     * 初始化层级数，技能数，半径大小
     */
    private void initSize() {
        mIntervalCount = 4;
        mSkillCount = SkillBean.skillNames.length;
        mRadius = Utils.dp2px(getContext(),100);
        mAngle = (float) (2 * Math.PI / mSkillCount);
    }

    /**
     * 计算各层级多边形的顶点坐标
     */
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

    /**
     * 初始化技能
     * @param skillBean
     */
    public void setSkillBean(SkillBean skillBean) {
        if (skillBean == null)
            return;
        mSkillBean = skillBean;
        invalidate();
    }

    /**
     * 更新技能值
     * @param skillBean
     */
    public void updateSkillValue(SkillBean skillBean) {
        if (mSkillBean == null)
            return;

        mSkillBean.setAttack(skillBean.getAttack());
        mSkillBean.setDefense(skillBean.getDefense());
        mSkillBean.setMagic(skillBean.getMagic());
        mSkillBean.setTreat(skillBean.getTreat());
        mSkillBean.setGold(skillBean.getGold());

        invalidate();
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
        drawOutline(canvas);
        drawSkillLine(canvas);
        drawText(canvas);
    }

    /**
     * 绘制各层级多边形并填充
     * @param canvas
     */
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

    /**
     * 绘制多边形轮廓线
     * @param canvas
     */
    private void drawOutline(Canvas canvas) {
        canvas.save();
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(Color.parseColor("#99DCE2"));

        Path path = new Path();
        for (int i=0; i<mSkillCount; i++) {
            float x = mPointArray.get(0).get(i).x;
            float y = mPointArray.get(0).get(i).y;
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        path.close();
        canvas.drawPath(path, mLinePaint);
        path.reset();

        for (int i=0; i<mSkillCount; i++) {
            float x = mPointArray.get(0).get(i).x;
            float y = mPointArray.get(0).get(i).y;
            canvas.drawLine(0, 0, x, y, mLinePaint);
        }

        canvas.restore();
    }

    /**
     * 绘制技能分布线
     * @param canvas
     */
    private void drawSkillLine(Canvas canvas) {
        if (mSkillBean == null)
            return;
        canvas.save();

        //计算各技能值的坐标
        int[] allValues = mSkillBean.getSkillValues();
        List<PointF> skillPoints = new ArrayList<>();
        for (int i=0; i<mSkillCount; i++) {
            float radius = mRadius * (allValues[i] / 100.0f);
            float x = (float) (radius * Math.cos(i * mAngle - Math.PI /2));
            float y = (float) (radius * Math.sin(i * mAngle - Math.PI / 2));
            skillPoints.add(new PointF(x, y));
        }

        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(Utils.dp2pxF(getContext(), 2));
        mLinePaint.setColor(Color.RED);
        Path path = new Path();
        for (int i=0; i<mSkillCount; i++) {
            float x = skillPoints.get(i).x;
            float y = skillPoints.get(i).y;
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        path.close();
        canvas.drawPath(path, mLinePaint);

        canvas.restore();
    }

    /**
     * 绘制描述文字
     * @param canvas
     */
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
