package com.wx2.college.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RankType {
    DAILY(1, "日榜"),
    WEEKLY(2, "周榜"),
    MONTHLY(3, "月榜");

    private final Integer code;
    private final String desc;

    public static RankType getByDesc(String desc) {
        if (desc == null) {
            return null;
        }
        for (RankType type : values()) {
            if (type.desc.equals(desc)) {
                return type;
            }
        }
        return null;
    }
}
