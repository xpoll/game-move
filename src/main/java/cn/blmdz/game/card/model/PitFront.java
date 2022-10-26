package cn.blmdz.game.card.model;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

/**
 * 前排
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PitFront {
    private Figure one;
    private Figure two;
    public List<Figure> all() {
        List<Figure> all = Lists.newArrayList();
        if (Objects.nonNull(one)) {
            all.add(one);
        }
        if (Objects.nonNull(two)) {
            all.add(two);
        }
        return all;
    }
}