package gui;

import model.Speech;
import error.Log;

public class LaunchChat {

	public static void main(String[] args) {
		new Log();
		new Speech();
		ChatWindow.addtheme();
		ChatWindow.startWindow();
	}
}
