package cn.blmdz.game.card.util;

import cn.blmdz.game.card.model.DefaultFigure;
import cn.blmdz.game.card.enums.FigureAttributeEnum;
import cn.blmdz.game.card.enums.FigureEnum;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 卡实例
 */
public class FigureUtil {
    private static Map<FigureEnum, DefaultFigure> INSTALL = Maps.newConcurrentMap();

    static {
        FigureEnum figure = null;

        figure = FigureEnum.GAO_YUE;
        {
            INSTALL.put(figure, DefaultFigure.builder().id(figure.getCode()).name(figure.getDescription())
                    .figureAttribute(
                            ImmutableMap
                                    .of(FigureAttributeEnum.ATTACK, 100)
                                    .of(FigureAttributeEnum.DEFENSE, 100)
                                    .of(FigureAttributeEnum.BLOOD, 100)
                                    .of(FigureAttributeEnum.SPEED, 100)
                                    .of(FigureAttributeEnum.DODGE, 100)
                                    .of(FigureAttributeEnum.PARRY, 100)
                                    .of(FigureAttributeEnum.HIT, 100)
                    ).build());
        }
        figure = FigureEnum.TIAN_MING;
        {
            INSTALL.put(figure, DefaultFigure.builder().id(figure.getCode()).name(figure.getDescription())
                    .figureAttribute(
                            ImmutableMap
                                    .of(FigureAttributeEnum.ATTACK, 100)
                                    .of(FigureAttributeEnum.DEFENSE, 100)
                                    .of(FigureAttributeEnum.BLOOD, 100)
                                    .of(FigureAttributeEnum.SPEED, 100)
                                    .of(FigureAttributeEnum.DODGE, 100)
                                    .of(FigureAttributeEnum.PARRY, 100)
                                    .of(FigureAttributeEnum.HIT, 100)
                    ).build());
        }
        figure = FigureEnum.SHAO_YU;
        {
            INSTALL.put(figure, DefaultFigure.builder().id(figure.getCode()).name(figure.getDescription())
                    .figureAttribute(
                            ImmutableMap
                                    .of(FigureAttributeEnum.ATTACK, 100)
                                    .of(FigureAttributeEnum.DEFENSE, 100)
                                    .of(FigureAttributeEnum.BLOOD, 100)
                                    .of(FigureAttributeEnum.SPEED, 100)
                                    .of(FigureAttributeEnum.DODGE, 100)
                                    .of(FigureAttributeEnum.PARRY, 100)
                                    .of(FigureAttributeEnum.HIT, 100)
                    ).build());
        }
        figure = FigureEnum.SHI_LAN;
        {
            INSTALL.put(figure, DefaultFigure.builder().id(figure.getCode()).name(figure.getDescription())
                    .figureAttribute(
                            ImmutableMap
                                    .of(FigureAttributeEnum.ATTACK, 100)
                                    .of(FigureAttributeEnum.DEFENSE, 100)
                                    .of(FigureAttributeEnum.BLOOD, 100)
                                    .of(FigureAttributeEnum.SPEED, 100)
                                    .of(FigureAttributeEnum.DODGE, 100)
                                    .of(FigureAttributeEnum.PARRY, 100)
                                    .of(FigureAttributeEnum.HIT, 100)
                    ).build());
        }
        figure = FigureEnum.FU_NIAN;
        {
            INSTALL.put(figure, DefaultFigure.builder().id(figure.getCode()).name(figure.getDescription())
                    .figureAttribute(
                            ImmutableMap
                                    .of(FigureAttributeEnum.ATTACK, 100)
                                    .of(FigureAttributeEnum.DEFENSE, 100)
                                    .of(FigureAttributeEnum.BLOOD, 100)
                                    .of(FigureAttributeEnum.SPEED, 100)
                                    .of(FigureAttributeEnum.DODGE, 100)
                                    .of(FigureAttributeEnum.PARRY, 100)
                                    .of(FigureAttributeEnum.HIT, 100)
                    ).build());
        }
        figure = FigureEnum.YAN_LU;
        {
            INSTALL.put(figure, DefaultFigure.builder().id(figure.getCode()).name(figure.getDescription())
                    .figureAttribute(
                            ImmutableMap
                                    .of(FigureAttributeEnum.ATTACK, 100)
                                    .of(FigureAttributeEnum.DEFENSE, 100)
                                    .of(FigureAttributeEnum.BLOOD, 100)
                                    .of(FigureAttributeEnum.SPEED, 100)
                                    .of(FigureAttributeEnum.DODGE, 100)
                                    .of(FigureAttributeEnum.PARRY, 100)
                                    .of(FigureAttributeEnum.HIT, 100)
                    ).build());
        }
        figure = FigureEnum.ZHANG_LIANG;
        {
            INSTALL.put(figure, DefaultFigure.builder().id(figure.getCode()).name(figure.getDescription())
                    .figureAttribute(
                            ImmutableMap
                                    .of(FigureAttributeEnum.ATTACK, 100)
                                    .of(FigureAttributeEnum.DEFENSE, 100)
                                    .of(FigureAttributeEnum.BLOOD, 100)
                                    .of(FigureAttributeEnum.SPEED, 100)
                                    .of(FigureAttributeEnum.DODGE, 100)
                                    .of(FigureAttributeEnum.PARRY, 100)
                                    .of(FigureAttributeEnum.HIT, 100)
                    ).build());
        }

    }

    public static DefaultFigure install(FigureEnum figureEnum) {
        return INSTALL.get(figureEnum);
    }
}
