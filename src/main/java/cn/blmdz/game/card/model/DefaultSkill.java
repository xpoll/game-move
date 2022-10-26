package cn.blmdz.game.card.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultSkill {
    /** ID */
    private Integer id;
    /** 名称 */
    private String name;
    /** 几段伤害 */
    private Integer times;
    /** 技能详情 */
    private List<DefaultSkillDetail> skillDetailList;
}
