package com.rainmin.demo.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.rainmin.demo.R;
import com.rainmin.demo.widget.SkillBean;
import com.rainmin.demo.widget.SkillMapView;

import java.util.Random;

public class SkillMapFragment extends Fragment {

    SkillMapView skillMapView;
    Button updateSkill;
    SkillBean skillBean;

    public SkillMapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_skillmap, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        skillMapView = (SkillMapView) view.findViewById(R.id.skill_map);
        updateSkill = (Button) view.findViewById(R.id.btn_update_skill);

        skillBean = new SkillBean(98, 80, 75,50, 69);
        skillMapView.setSkillBean(skillBean);
        updateSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                SkillBean bean = new SkillBean(random.nextInt(100) + 1, random.nextInt(100) + 1,
                        random.nextInt(100) + 1, random.nextInt(100) + 1, random.nextInt(100) + 1);
                skillMapView.updateSkillValue(bean);
            }
        });
    }
}
