package cn.blmdz.game.console;

public enum Instruction {
	CMD_0("\033[0m", "关闭所有属性 "),
	CMD_1("\033[1m", "设置高亮度"),
	CMD_4("\033[2m", "下划线"),
	CMD_5("\033[3m", "闪烁"),
	CMD_7("\033[4m", "反显"),
	CMD_8("\033[8m", "消隐"),
	/** 4+ 背景颜色 3+字体颜色 
	40: 黑                         30: 黑
	41: 红                         31: 红
	42: 绿                         32: 绿
	43: 黄                         33: 黄
	44: 蓝                         34: 蓝
	45: 紫                         35: 紫
	46: 深绿                       36: 深绿
	47: 白色                       37: 白色
	 */
	CMD__S_BACKGROUND_FONT("\033[%s;%sm", "背景和字体"),
	CMD__S_A("\033[%sA", "光标上移n行 "),
	CMD__S_B("\033[%sB", "光标下移n行 "),
	CMD__S_C("\033[%sC", "光标右移n行 "),
	CMD__S_D("\033[%sD", "光标左移n行 "),
	CMD__S_H("\033[%s;%sH", "设置光标位置(y,x)"),
	CMD_J("\033[2J", "清屏"),
	CMD_K("\033[K", "清除从光标到行尾的内容"),
	CMD_s("\033[s", "保存光标位置"),
	CMD_u("\033[u", "恢复光标位置"),
	CMD_l("\033[?25l", "隐藏光标"),
	CMD_h("\033[?25h", "显示光标"),
	
	;
	String value;
	String description;
	
	Instruction (String value, String description) {
		this.value = value;
		this.description = description;
	}
}