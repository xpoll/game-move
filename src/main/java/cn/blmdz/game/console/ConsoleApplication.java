package cn.blmdz.game.console;

public class ConsoleApplication {
	public static CanvasHook canvasHook = null;
	public static ControllerHook controllerHook = null;
	public static KeyboardHook keyboardHook = null;
	static int i = 2;

	public static void main(String[] args) throws InterruptedException {
		
		CanvasHook.contents.add("你好啊");
		CanvasHook.contents.add(String.format(Instruction.CMD__S_BACKGROUND_FONT.value, 40, 31) + "你好啊");
		CanvasHook.contents.add(String.format(Instruction.CMD__S_BACKGROUND_FONT.value, 40, 31) + "你好啊" + String.format(Instruction.CMD__S_BACKGROUND_FONT.value, 41, 32) + "你好啊");
		CanvasHook.contents.add(String.format(Instruction.CMD__S_BACKGROUND_FONT.value, 42, 33) + "你　啊");
		CanvasHook.contents.add(String.format(Instruction.CMD__S_BACKGROUND_FONT.value, 43, 34) + "你好啊");
		CanvasHook.contents.add(String.format(Instruction.CMD__S_BACKGROUND_FONT.value, 44, 35) + "你好啊");
		CanvasHook.contents.add(String.format(Instruction.CMD__S_BACKGROUND_FONT.value, 45, 36) + "你好啊");
		CanvasHook.contents.add(String.format(Instruction.CMD__S_BACKGROUND_FONT.value, 46, 37) + "你好啊");
		CanvasHook.contents.add(String.format(Instruction.CMD__S_BACKGROUND_FONT.value, 47, 38) + "你好啊");

		CanvasHook.refreshCanvas();
		
		keyboardInit();
	}

	public static void controllerInit () {
		controllerHook = new ControllerHook();
		controllerHook.run();
	}
	
	public static void keyboardInit () {
		keyboardHook = new KeyboardHook() {
			@Override
			public void keyBoardCallback(int keyCode, int eventType) {
				if ((eventType == 1 && keyCode >=37 && keyCode <= 40)
						||
						(eventType == 0 && (keyCode == 32 || keyCode == 13))
						) {
					chooseOrientation(Orientation.conversion(keyCode));
				}
			}
		};
		keyboardHook.run();
	}
	public static void chooseOrientation (Orientation orientation) {
//		System.out.println(orientation.name());
		switch (orientation) {
		case UP: {
			if (i > 0) {
				String str = CanvasHook.contents.remove(i);
				i--;
				CanvasHook.contents.add(i, str);
			}
			break;
		}
		case DOWN: {
			if (i < (CanvasHook.contents.size() - 1)) {
				String str = CanvasHook.contents.remove(i);
				i++;
				CanvasHook.contents.add(i, str);
			}
			break;
		}
		default:
			break;
		}
		CanvasHook.refreshCanvas();
	}

}
