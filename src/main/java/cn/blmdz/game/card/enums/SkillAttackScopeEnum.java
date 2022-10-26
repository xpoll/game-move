package cn.blmdz.game.card.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 基础属性
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum SkillAttackScopeEnum {
    ALL("所有"),
    FRONT("前排-绝对"),
    FRONT_RELATIVE("前排-相对"),
    BACK("后排"),
    RANDOM("随机"),
//    FRONT_ONE, // 前排第一个
//    BACK_ONE, // 后排第一个
    ;
    private final String description;
}
