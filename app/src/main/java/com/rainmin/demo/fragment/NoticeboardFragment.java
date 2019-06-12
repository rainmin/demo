package com.rainmin.demo.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rainmin.demo.R;
import com.rainmin.noticeboard.NoticeBoard;

import java.util.ArrayList;
import java.util.List;

public class NoticeboardFragment extends Fragment {

    public NoticeboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_noticeboard, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NoticeBoard noticeBoard = (NoticeBoard) view.findViewById(R.id.notice_board);
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
                        Toast.makeText(getActivity(), notice, Toast.LENGTH_SHORT).show();
                    }
                }).start();
    }
}
