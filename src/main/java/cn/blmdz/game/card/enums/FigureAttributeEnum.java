package cn.blmdz.game.card.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 基础属性
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum FigureAttributeEnum {
    ATTACK("攻击"),
    DEFENSE("防御"),
    BLOOD("气血"),
    SPEED("速度"),
    DODGE("闪避"),
    PARRY("招架"),
    HIT("命中"),
    ;
    private final String description;
}
