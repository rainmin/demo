package com.rainmin.demo.noticeboard;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rainmin.demo.BaseActivity;
import com.rainmin.demo.R;
import com.rainmin.noticeboard.NoticeBoard;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * show how to use NoticeBoard control
 */
public class NoticeboardActivity extends BaseActivity {

    @BindView(R.id.notice_board)
    NoticeBoard noticeBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initCustomizedView(R.layout.activity_noticeboard, "公告栏", true);
        // init noticeboard
        List<String> notices = new ArrayList<>(3);
        notices.add("网络安全周最强厂商巡礼：腾讯安全生态舰队全军出击");
        notices.add("高精地图对自动驾驶有多重要？和一般导航地图有何区别？");
        notices.add("15部委：2020年全国基本实现车用乙醇汽油全覆盖");
        noticeBoard.setNotices(notices)
                .setFlipInterval(2000)
                .setTextSize(20)
                .setItemClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String notice = ((TextView) v).getText().toString();
                        Toast.makeText(NoticeboardActivity.this, notice, Toast.LENGTH_SHORT).show();
                    }
                }).start();
    }

}
