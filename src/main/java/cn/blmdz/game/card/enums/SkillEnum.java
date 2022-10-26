package cn.blmdz.game.card.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 基础属性
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum SkillEnum {
    A(1, "普攻"),
    GAO_S(1, "高山流水")
    ;
    private final int code;
    private final String description;
}
