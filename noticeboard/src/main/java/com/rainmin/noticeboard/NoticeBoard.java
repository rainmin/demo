package com.rainmin.noticeboard;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.rainmin.noticeboard.R;

/**
 * Created by chenming on 2017/9/11
 */

public class NoticeBoard extends LinearLayout {

    private Context mContext;
    private String[] mTextArray;
    private OnClickListener clickListener;
    private ViewFlipper viewFlipper;
    private int textColor;
    private float textSize;
    private int flipInterval;

    public NoticeBoard(Context context) {
        super(context);
        this.mContext = context;
        initBasicView();
    }

    public NoticeBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NoticeBoard);
        textColor = typedArray.getColor(R.styleable.NoticeBoard_textColor, Color.BLACK);
        textSize = typedArray.getFloat(R.styleable.NoticeBoard_textSize, 12);
        flipInterval = typedArray.getInt(R.styleable.NoticeBoard_flipInterval, 3000);
        typedArray.recycle();

        initBasicView();
    }

    private void initBasicView() {
        //setPadding(20,20,20,20);
        viewFlipper = new ViewFlipper(mContext);
        viewFlipper.setInAnimation(mContext, R.anim.slide_in_bottom);
        viewFlipper.setOutAnimation(mContext, R.anim.slide_out_top);
        viewFlipper.setFlipInterval(flipInterval);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        addView(viewFlipper, layoutParams);
    }

    private void initNoticeBoard() {
        if (mTextArray == null || mTextArray.length == 0) {
            return;
        }

        for(int i=0; i<mTextArray.length; i++) {
            TextView textView = new TextView(mContext);
            textView.setTextColor(textColor);
            textView.setTextSize(textSize);
            textView.setText(mTextArray[i]);
            textView.setOnClickListener(clickListener);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = 30;
            viewFlipper.addView(textView, layoutParams);
        }
    }

    public NoticeBoard setInAnimation(@android.support.annotation.AnimRes int resourceID) {
        viewFlipper.setInAnimation(mContext, resourceID);
        return this;
    }

    public NoticeBoard setOutAnimation(@android.support.annotation.AnimRes int resourceID) {
        viewFlipper.setOutAnimation(mContext, resourceID);
        return this;
    }

    public NoticeBoard setNotices(String[] textArray) {
        if (textArray == null || textArray.length == 0) {
            Log.e("NoticeBoard", "textArray is null");
        } else {
            this.mTextArray = textArray;
        }
        return this;
    }

    public NoticeBoard setItemClickListener(OnClickListener listener) {
        this.clickListener = listener;
        return this;
    }

    public void start() {
        initNoticeBoard();
        viewFlipper.startFlipping();
    }

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
