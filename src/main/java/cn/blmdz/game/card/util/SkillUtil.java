package cn.blmdz.game.card.util;

import cn.blmdz.game.card.enums.SkillEnum;
import cn.blmdz.game.card.enums.SkillAttackScopeEnum;
import cn.blmdz.game.card.model.DefaultSkill;
import cn.blmdz.game.card.model.DefaultSkillDetail;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 技能实例
 */
public class SkillUtil {
    private static Map<SkillEnum, DefaultSkill> INSTALL = Maps.newConcurrentMap();

    static {
        SkillEnum skill = null;

        skill = SkillEnum.A;
        {
            INSTALL.put(skill, DefaultSkill.builder().id(skill.getCode()).name(skill.getDescription()).times(1)
                    .skillDetailList(
                            Lists.newArrayList(
                                    DefaultSkillDetail.builder().scope(SkillAttackScopeEnum.RANDOM).scopeValue(1).build()
                            )
                    ).build());
        }
        skill = SkillEnum.GAO_S;
        {
            INSTALL.put(skill, DefaultSkill.builder().id(skill.getCode()).name(skill.getDescription()).times(1)
                    .skillDetailList(
                            Lists.newArrayList(
                                    DefaultSkillDetail.builder().scope(SkillAttackScopeEnum.ALL).scopeValue(null).build()
                            )
                    ).build());
        }
    }

    public static DefaultSkill install(SkillEnum skillEnum) {
        return INSTALL.get(skillEnum);
    }
}
