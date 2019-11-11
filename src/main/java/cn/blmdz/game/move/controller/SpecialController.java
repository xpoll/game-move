package cn.blmdz.game.move.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class SpecialController {
	
	private static final String prefix = "/";
	private static final String sufix = ".html";

    @Autowired SimpMessagingTemplate template;
	
	@RequestMapping(value= {"/index", "/"}, method=RequestMethod.GET)
	public String indexView() {
		return prefix + "index" + sufix;
	}
}
