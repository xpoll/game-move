package cn.blmdz.game.card.model;

import cn.blmdz.game.card.enums.FigureAttributeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 默认角色
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultFigure {
    /** ID */
    private Integer id;
    /** 名称 */
    private String name;
    /** 属性 */
    private Map<FigureAttributeEnum, Integer> figureAttribute;
}
// 是否闪避
// 是否招架