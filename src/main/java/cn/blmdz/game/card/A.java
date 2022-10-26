package cn.blmdz.game.card;

import cn.blmdz.game.card.enums.SkillAttackScopeEnum;
import cn.blmdz.game.card.model.Figure;
import cn.blmdz.game.card.model.LineUp;
import cn.blmdz.game.card.model.PitBack;
import cn.blmdz.game.card.model.PitFront;
import cn.blmdz.game.card.enums.FigureEnum;
import cn.blmdz.game.card.util.FigureUtil;
import cn.blmdz.game.card.util.SkillAttackScopeUtil;
import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.stream.Collectors;

public class A {
    public static void main(String[] args) {
        int i = 0;
        LineUp lineUp = LineUp.builder()
                .backRow(PitBack.builder()
                        .one(Figure.buildDefaultFigure(FigureUtil.install(FigureEnum.GAO_YUE), i++))
                        .two(Figure.buildDefaultFigure(FigureUtil.install(FigureEnum.TIAN_MING), i++))
                        .three(Figure.buildDefaultFigure(FigureUtil.install(FigureEnum.SHAO_YU), i++))
                        .build())
                .frontRow(PitFront.builder()
                        .one(Figure.buildDefaultFigure(FigureUtil.install(FigureEnum.SHI_LAN), i++))
                        .two(Figure.buildDefaultFigure(FigureUtil.install(FigureEnum.FU_NIAN), i++))
                        .build())
                .build().buildLineUp();
        for (SkillAttackScopeEnum scopeEnum : SkillAttackScopeEnum.values()) {
            System.out.println(scopeEnum.getDescription());
            List<Figure> allFigure = SkillAttackScopeUtil.getFigure(lineUp, scopeEnum, 2);
            System.out.println(JSON.toJSONString(allFigure.stream().map(Figure::getName).collect(Collectors.toList())));
        }
    }
}
