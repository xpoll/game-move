package cn.blmdz.game.console;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.HHOOK;
import com.sun.jna.platform.win32.WinUser.LowLevelKeyboardProc;

public abstract class KeyboardHook implements Runnable {
	private HHOOK hhk;

	// 钩子回调函数
	private LowLevelKeyboardProc keyboardProc = new LowLevelKeyboardProc() {
		@Override
		public LRESULT callback(int nCode, WPARAM wParam, WinUser.KBDLLHOOKSTRUCT event) {
			if (nCode >= 0) {
				keyBoardCallback(event.vkCode, event.flags);
				// 按下ESC退出
				if (event.vkCode == 27) {
					KeyboardHook.this.setHookOff();
				}
			}
			return User32.INSTANCE.CallNextHookEx(hhk, nCode, wParam, new LPARAM());
			
		}
	};
	
	/**
	 * @param keyCode event.vkCode ： 键盘key值
	 * @param eventType event.flags : 0 按下 128 弹起
	 */
	public abstract void keyBoardCallback(int keyCode, int eventType);

	public void run() {
		setHookOn();
	}

	// 安装钩子
	public void setHookOn() {
		System.out.println("Hook On!");

		HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);
		hhk = User32.INSTANCE.SetWindowsHookEx(User32.WH_KEYBOARD_LL, keyboardProc, hMod, 0);

		int result;
		WinUser.MSG msg = new WinUser.MSG();
		while ((result = User32.INSTANCE.GetMessage(msg, null, 0, 0)) != 0) {
			if (result == -1) {
				setHookOff();
				System.out.println(2);
				break;
			} else {
				System.out.println(1);
				User32.INSTANCE.TranslateMessage(msg);
				User32.INSTANCE.DispatchMessage(msg);
			}
		}
	}

	// 移除钩子并退出
	public void setHookOff() {
		System.out.println("Hook Off!");
		User32.INSTANCE.UnhookWindowsHookEx(hhk);
		System.exit(0);
	}
}
