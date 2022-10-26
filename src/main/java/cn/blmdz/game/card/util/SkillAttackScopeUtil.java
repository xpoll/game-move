package cn.blmdz.game.card.util;

import cn.blmdz.game.card.enums.SkillAttackScopeEnum;
import cn.blmdz.game.card.model.Figure;
import cn.blmdz.game.card.model.LineUp;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * 阵容受
 */
public class SkillAttackScopeUtil {

    public static List<Figure> getFigure(LineUp lineUp, SkillAttackScopeEnum scope, Integer scopeValue) {
        switch (scope) {
            case ALL:
                return getAllFigure(lineUp);
            case FRONT:
                return getFrontRowFigure(lineUp);
            case FRONT_RELATIVE:
                return getFrontFigure(lineUp);
            case BACK:
                return getBackRowFigure(lineUp);
            case RANDOM:
                return getRandomFigure(lineUp, scopeValue);
        }
        return null;
    }

    /**
     * 前排挨打
     */
    private static List<Figure> getFrontRowFigure(LineUp lineUp) {
        List<Figure> result = lineUp.getFrontRow().all().stream().filter(Figure::getSurvive).collect(Collectors.toList());
        result.sort(Comparator.comparing(Figure::getOrder));
        return result;
    }

    /**
     * 前面挨打
     */
    private static List<Figure> getFrontFigure(LineUp lineUp) {
        List<Figure> figureList = getAllFigure(lineUp);
        List<Figure> front = figureList.stream().filter(item -> item.getAt()).collect(Collectors.toList());
        List<Figure> back = figureList.stream().filter(item -> !item.getAt()).collect(Collectors.toList());
        List<Figure> result = Lists.newArrayList(front);
        back.forEach(item -> {
            if (item.getFrontFigureList().stream().filter(Figure::getSurvive).count() <= 0) {
                result.add(item);
            }
        });
        result.sort(Comparator.comparing(Figure::getOrder));
        return result;
    }

    /**
     * 后排挨打
     */
    private static List<Figure> getBackRowFigure(LineUp lineUp) {
        List<Figure> result = lineUp.getBackRow().all().stream().filter(Figure::getSurvive).collect(Collectors.toList());
        result.sort(Comparator.comparing(Figure::getOrder));
        return result;
    }

    /**
     * 所有挨打
     */
    private static List<Figure> getAllFigure(LineUp lineUp) {
        List<Figure> figureList = Lists.newArrayList(lineUp.getFrontRow().all());
        figureList.addAll(lineUp.getBackRow().all());
        List<Figure> result = figureList.stream().filter(Figure::getSurvive).collect(Collectors.toList());
        result.sort(Comparator.comparing(Figure::getOrder));
        return result;
    }

    /**
     * 随机挨打
     * @return
     */
    private static List<Figure> getRandomFigure(LineUp lineUp, Integer num) {
        if (num <= 0) {
            return Lists.newArrayList();
        }
        List<Figure> figureList = getAllFigure(lineUp);
        if (num >= figureList.size()) {
            return figureList;
        }
        List<Figure> result = Lists.newArrayList();
        Set<Integer> setAll = Sets.newHashSet();
        for (int i = 0; i < num; i++) {
            Figure figure = null;
            while (Objects.isNull(figure) || !setAll.add(figure.getIdentifies())) {
                figure = figureList.get(ThreadLocalRandom.current().nextInt(figureList.size()));
            }
            result.add(figure);
        }
        result.sort(Comparator.comparing(Figure::getOrder));
        return result;
    }
}
