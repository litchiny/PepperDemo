package cn.softbankrobotics.testapi.model;

public class AnimModel {
    String desc;
    int resId;

    public AnimModel(String desc, int resId) {
        this.desc = desc;
        this.resId = resId;
    }
    public AnimModel(int resId) {
        this("",resId);
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
