package com.rainmin.demo.widget;

/**
 * Created by chenming on 2017/11/10
 */

public class SkillBean {

    public static final String[] skillNames = {"攻击","防御","魔法","治疗","金币"};
    private int attack;
    private int defense;
    private int magic;
    private int treat;
    private int gold;

    public SkillBean(int attack, int defense, int magic, int treat, int gold) {
        this.attack = attack;
        this.defense = defense;
        this.magic = magic;
        this.treat = treat;
        this.gold = gold;
    }

    public int[] getSkillValues() {
        int[] allValues = {attack, defense, magic, treat, gold};
        return allValues;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getMagic() {
        return magic;
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }

    public int getTreat() {
        return treat;
    }

    public void setTreat(int treat) {
        this.treat = treat;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    @Override
    public String toString() {
        return "SkillBean{" +
                "attack=" + attack +
                ", defense=" + defense +
                ", magic=" + magic +
                ", treat=" + treat +
                ", gold=" + gold +
                '}';
    }
}
