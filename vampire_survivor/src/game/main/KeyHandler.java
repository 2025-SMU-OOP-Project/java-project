package game.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import game.state.GameState;


public class KeyHandler implements KeyListener {
	
	GamePanel gp;
	public boolean upPressed, downPressed, leftPressed, rightPressed;
	public KeyHandler(GamePanel gp) {
		this.gp = gp;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		// 일시정지 P키
		if (code == KeyEvent.VK_P) {
			if (gp.gameState == GameState.RUNNING) {
				gp.gameState = GameState.PAUSED;
				
			} else if (gp.gameState == GameState.PAUSED) {
				gp.gameState = GameState.RUNNING;
			}
		}
		//플레이어 이동 - RUNNING 상태일떄만
		if (gp.gameState == GameState.RUNNING) {
			if (code == KeyEvent.VK_W) upPressed = true;
			if (code == KeyEvent.VK_S) downPressed = true;
			if (code == KeyEvent.VK_A) leftPressed = true;
			if (code == KeyEvent.VK_D) rightPressed = true;
			
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_W) upPressed = false;
		if (code == KeyEvent.VK_S) downPressed = false;
		if (code == KeyEvent.VK_A) leftPressed = false;
		if (code == KeyEvent.VK_D) rightPressed = false;
	}
	@Override
	public void keyTyped(KeyEvent e) {}
}