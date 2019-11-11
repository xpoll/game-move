package cn.blmdz.game.move;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

		if (args == null) {
			System.out.println("args is null.");
		} else {
			System.out.println("args: " + args.length);
			System.out.println("args: " + Arrays.toString(args));
		}
		
		if (args == null || args.length != 6) {
			args = new String[]{
					"false",
					"3000",
					"6000",
					"http://127.0.0.1:8080",
					"C:/Program Files (x86)/Google/Chrome/Application/chrome.exe",
					"D:/game/chrome"
			};
		}
		final String abc[] = args;
		if (!Boolean.valueOf(abc[0])) return ;
		
		new Thread(new Runnable() {
			@Override
			public void run() {

				
				try {
					Thread.sleep(Long.parseLong(abc[1]));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				List<String> cmd = new ArrayList<String>();
				cmd.add(abc[4]);
				// cmd.add("--no-first-run");
				cmd.add("--start-maximized");// 窗口启动最大化
				cmd.add("--incognito");// 隐身模式打开
				cmd.add("--user-data-dir=" + abc[5]);
				cmd.add(abc[3]);
				ProcessBuilder process = new ProcessBuilder(cmd);
				try {
					process.start();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					Thread.sleep(Long.parseLong(abc[2]));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				try {
					// VM的Option 要加上 -Djava.awt.headless=false
					Robot r = new Robot();
					r.keyPress(122);
					r.keyRelease(122);
				} catch (AWTException e) {
					e.printStackTrace();
				} // 创建自动化工具对象
			}
		}).start();
		;
	}

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		// 用于html的后缀匹配
		configurer.setUseSuffixPatternMatch(false);
	}
}
