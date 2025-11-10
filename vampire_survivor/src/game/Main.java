package game;


import javax.swing.SwingUtilities;
import game.screen.MainScreen;  //MainScreen class 가져옴

public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				//main 화면 표시
				new MainScreen();
			}
		});
	}

}
