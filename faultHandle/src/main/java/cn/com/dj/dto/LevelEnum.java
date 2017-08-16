package cn.com.dj.dto;

/**
 * Created by dujiang02 on 17/8/16.
 */
public enum  LevelEnum {

    WARN(1, "报警"),FAULT(2, "故障");

    private int type;
    private String desc;

    LevelEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static LevelEnum findLevel(int type) {
        LevelEnum[] values = LevelEnum.values();
        for (LevelEnum levelEnum : values) {
            if (levelEnum.getType() == type) {
                return levelEnum;
            }
        }
        return WARN;
    }

}
