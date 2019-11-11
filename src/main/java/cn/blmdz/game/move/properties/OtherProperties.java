package cn.blmdz.game.move.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix="other")
public class OtherProperties {
    private Boolean devMode = Boolean.FALSE.booleanValue();
    private Boolean job = Boolean.FALSE.booleanValue();
}