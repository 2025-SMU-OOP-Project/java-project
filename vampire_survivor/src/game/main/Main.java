package game.main;


import javax.swing.SwingUtilities;

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
