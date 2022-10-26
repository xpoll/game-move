package cn.blmdz.game.card.model;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 阵容
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LineUp {
    /** 前排 */
    private PitFront frontRow;
    /** 后排 */
    private PitBack backRow;

    /** 前后关系构建 */
    public LineUp buildLineUp() {
        int i = 1;
        setBackFigure(backRow.getOne(), i++);
        setBackFigure(backRow.getTwo(), i++);
        setBackFigure(backRow.getThree(), i++);

        setFrontFigure(frontRow.getOne(), i++, backRow.getOne(), backRow.getTwo());
        setFrontFigure(frontRow.getTwo(), i++, backRow.getTwo(), backRow.getThree());
        return this;
    }
    private void setBackFigure(Figure figure, Integer order) {
        if (Objects.nonNull(figure)) {
            figure.setAt(false);
            figure.setSurvive(true);
            figure.setOrder(order);
            figure.setFrontFigureList(Lists.newArrayList());
        }
    }
    private void setFrontFigure(Figure figure, Integer order, Figure ... backFigureArray) {
        if (Objects.nonNull(figure)) {
            figure.setAt(true);
            figure.setSurvive(true);
            figure.setOrder(order);
            for (Figure f : backFigureArray) {
                if (Objects.nonNull(f)) {
                    f.getFrontFigureList().add(figure);
                }
            }
        }
    }
}


