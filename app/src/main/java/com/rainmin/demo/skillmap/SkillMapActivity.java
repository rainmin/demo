package com.rainmin.demo.skillmap;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rainmin.demo.BaseActivity;
import com.rainmin.demo.R;

import java.util.Random;

import butterknife.BindView;

/**
 * Created by chenming on 2017/11/10
 */

public class SkillMapActivity extends BaseActivity {

    @BindView(R.id.skill_map) SkillMapView skillMapView;
    @BindView(R.id.btn_update_skill) Button updateSkill;
    SkillBean skillBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initCustomizedView(R.layout.activity_skillmap, "技能展示图");
        skillBean = new SkillBean(98, 80, 75,50, 69);
        skillMapView.setSkillBean(skillBean);
        updateSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                skillBean.setAttack(random.nextInt(100) + 1);
                skillBean.setDefense(random.nextInt(100) + 1);
                skillBean.setMagic(random.nextInt(100) + 1);
                skillBean.setTreat(random.nextInt(100) + 1);
                skillBean.setGold(random.nextInt(100) + 1);
                skillMapView.updateSkillValue(skillBean);
            }
        });
    }
}
