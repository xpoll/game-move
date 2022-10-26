package cn.blmdz.game.card.model;

import cn.blmdz.game.card.enums.SkillAttackScopeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultSkillDetail {
    /** 技能范围 */
    private SkillAttackScopeEnum scope;
    /** 范围值（随机x个） */
    private Integer scopeValue;
}
