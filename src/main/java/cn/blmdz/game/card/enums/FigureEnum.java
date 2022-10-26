package cn.blmdz.game.card.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 卡
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum FigureEnum {
    GAO_YUE(1, "高月"),
    TIAN_MING(2, "天明"),
    SHAO_YU(3, "少羽"),
    SHI_LAN(4, "石兰"),
    FU_NIAN(5, "伏念"),
    YAN_LU(6, "颜路"),
    ZHANG_LIANG(7, "张良"),
    ;
    private final int code;
    private final String description;
}
