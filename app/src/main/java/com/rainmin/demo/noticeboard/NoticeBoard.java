package com.rainmin.demo.noticeboard;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

/**
 * Created by chenming on 2017/9/11
 */

public class NoticeBoard extends LinearLayout {

    private Context mContext;
    private String[] mTextArray;
    private OnClickListener clickListener;
    private ViewFlipper viewFlipper;

    public NoticeBoard(Context context) {
        super(context);
        this.mContext = context;
        initBasicView();
    }

    public NoticeBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initBasicView();
    }

    private void initBasicView() {
        setBackgroundColor(Color.BLUE);
        viewFlipper = new ViewFlipper(mContext);
        viewFlipper.setInAnimation(mContext, android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(mContext, android.R.anim.slide_out_right);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(viewFlipper, layoutParams);
        viewFlipper.startFlipping();
    }

    public void initNoticeBoard(String[] textArray, OnClickListener listener) {
        this.clickListener = listener;
        setNotices(textArray);
    }

    private void setNotices(String[] textArray) {
        if (textArray == null || textArray.length == 0) {
            return;
        } else {
            this.mTextArray = textArray;
        }

        for(int i=0; i<mTextArray.length; i++) {
            TextView textView = new TextView(mContext);
            textView.setText(mTextArray[i]);
            textView.setOnClickListener(clickListener);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            viewFlipper.addView(textView, layoutParams);
        }
    }

    private void setTextClickListener(OnClickListener listener) {
        this.clickListener = listener;
    }

    public void releaseResource() {
        if (viewFlipper != null) {
            viewFlipper.stopFlipping();
            viewFlipper.removeAllViews();
            viewFlipper = null;
        }
    }
}
