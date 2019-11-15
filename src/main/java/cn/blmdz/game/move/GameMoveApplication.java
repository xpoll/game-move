package cn.blmdz.game.move;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import cn.blmdz.game.move.properties.OtherProperties;

@EnableConfigurationProperties(OtherProperties.class)
@SpringBootApplication
public class GameMoveApplication extends WebMvcConfigurerAdapter {
	
	public static void main(String[] args) {
		SpringApplication.run(GameMoveApplication.class, args);
	}

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		// 用于html的后缀匹配
		configurer.setUseSuffixPatternMatch(false);
	}
}
