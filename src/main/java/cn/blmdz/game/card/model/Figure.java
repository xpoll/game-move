package cn.blmdz.game.card.model;

import cn.blmdz.game.card.enums.FigureAttributeEnum;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 角色
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Figure {

    /** 唯一ID */
    private Integer identifies;
    /** ID */
    private Integer id;
    /** 名称 */
    private String name;
    /** 属性 */
    private Map<FigureAttributeEnum, Integer> figureAttribute;
    /** 属性 */
    private Map<FigureAttributeEnum, Integer> figureAttributesOriginal;
    /** 默认技能-普攻 */
    private DefaultSkill defaultSkill;

    // 以下属性是非主动设置属性
    /** 存活 */
    private Boolean survive;
    /** 排序 */
    private Integer order;
    /** 位置 前 true 后 false */
    private Boolean at;
    /** 前方单位 */
    private List<Figure> frontFigureList;

    /**
     * 构建非主动设置属性
     */
    public static Figure buildDefaultFigure(DefaultFigure defaultFigure, Integer identifies) {
        Figure figure = builder().identifies(identifies).id(defaultFigure.getId()).name(defaultFigure.getName()).build();
        Map<FigureAttributeEnum, Integer> figureAttribute = defaultFigure.getFigureAttribute();
        figure.setFigureAttribute(Maps.newEnumMap(figureAttribute));
        figure.setFigureAttributesOriginal(Maps.newEnumMap(figureAttribute));
        return figure;
    }
}
