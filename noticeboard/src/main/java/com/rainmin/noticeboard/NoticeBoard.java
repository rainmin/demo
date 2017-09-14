package com.rainmin.noticeboard;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.List;

/**
 * 实现内容轮播功能的广告栏控件
 */

public class NoticeBoard extends LinearLayout {

    private Context mContext;
    private List<String> textArray;
    private OnClickListener clickListener;
    private ViewFlipper viewFlipper;
    private int textColor;
    private float textSize;
    private int flipInterval;

    public NoticeBoard(Context context) {
        super(context);
        this.mContext = context;
    }

    public NoticeBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NoticeBoard);
        textColor = typedArray.getColor(R.styleable.NoticeBoard_textColor, Color.DKGRAY);
        textSize = typedArray.getFloat(R.styleable.NoticeBoard_textSize, 14);
        flipInterval = typedArray.getInt(R.styleable.NoticeBoard_flipInterval, 3000);
        typedArray.recycle();
    }

    private void initBasicView() {
        setPadding(20,0,20,0);
        viewFlipper = new ViewFlipper(mContext);
        viewFlipper.setInAnimation(mContext, R.anim.slide_in_bottom);
        viewFlipper.setOutAnimation(mContext, R.anim.slide_out_top);
        viewFlipper.setFlipInterval(flipInterval);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(viewFlipper, layoutParams);
    }

    private void initNoticeBoard() {
        if (textArray == null || textArray.size() == 0) {
            return;
        }

        for(int i=0; i<textArray.size(); i++) {
            TextView textView = new TextView(mContext);
            textView.setPadding(0,20,0,20);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setMaxLines(1);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setTextColor(textColor);
            textView.setTextSize(textSize);
            textView.setText(textArray.get(i));
            textView.setOnClickListener(clickListener);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            layoutParams.gravity = Gravity.CENTER_VERTICAL;
            viewFlipper.addView(textView, layoutParams);
        }
    }

    /**
     * 设置需要轮播显示的文本内容
     * @param textArray 字符串数组
     * @return
     */
    public NoticeBoard setNotices(List<String> textArray) {
        if (textArray == null || textArray.size() == 0) {
            Log.e("NoticeBoard", "textArray is null");
        } else {
            this.textArray = textArray;
        }
        return this;
    }

    /**
     * 设置显示内容的字体大小
     * @param textSize 字体大小
     * @return
     */
    public NoticeBoard setTextSize(float textSize) {
        this.textSize = textSize;
        return this;
    }

    /**
     * 设置显示内容的字体颜色
     * @param textColor 字体颜色
     * @return
     */
    public NoticeBoard setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }

    /**
     * 设置每条内容的显示时间
     * @param flipInterval 时间值，单位为毫秒
     * @return
     */
    public NoticeBoard setFlipInterval(int flipInterval) {
        this.flipInterval = flipInterval;
        return this;
    }

    /**
     * 为每条内容设置点击事件响应，需在start方法前调用
     * @param listener
     * @return
     */
    public NoticeBoard setItemClickListener(OnClickListener listener) {
        this.clickListener = listener;
        return this;
    }

    /**
     * 开始广告栏的轮播，此方法应在设置完各种属性后才调用且必须调用
     */
    public void start() {
        initBasicView();
        initNoticeBoard();
        viewFlipper.startFlipping();
    }

    /**
     * 停止广告栏的轮播
     */
    public void stop() {
        viewFlipper.stopFlipping();
    }

    public void releaseResource() {
        if (viewFlipper != null) {
            viewFlipper.stopFlipping();
            viewFlipper.removeAllViews();
            viewFlipper = null;
        }
    }
}
