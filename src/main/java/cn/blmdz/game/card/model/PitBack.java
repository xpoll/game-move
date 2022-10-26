package cn.blmdz.game.card.model;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

/**
 * 后排
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PitBack {
    private Figure one;
    private Figure two;
    private Figure three;
    public List<Figure> all() {
        List<Figure> all = Lists.newArrayList();
        if (Objects.nonNull(one)) {
            all.add(one);
        }
        if (Objects.nonNull(two)) {
            all.add(two);
        }
        if (Objects.nonNull(three)) {
            all.add(three);
        }
        return all;
    }
}