package game.screen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainScreen extends JFrame {
	
	public MainScreen() {
		// 화면 기본설정
		setTitle("Vam sur - Main Screen");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		// 레이아웃 설정
		setLayout(new BorderLayout());
		
		//시작 버튼 생성및 설정
		JButton startButton = new JButton("시작");
		//버튼 글자 폰트, 크기지정
		startButton.setFont(new Font("맑은 고딕", Font.BOLD, 30));
		//버튼 크기 설정
		startButton.setPreferredSize(new Dimension(200, 80));
		
		//버튼 동작 설정
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose(); // 현재 창 닫기
				System.out.println(" 게임 시작 !");
				//새 게임창 생성
				JFrame gameFrame = new JFrame("Vamsur - game");
				gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				gameFrame.setLocationRelativeTo(null);
				
				//Gamepanel 인스턴스 생성
				GamePanel gamePanel = new GamePanel();
				
				gameFrame.add(gamePanel);  
				gameFrame.pack();  //창 크기 조정
				
				gameFrame.setVisible(true);
				
				gamePanel.requestFocusInWindow();
				
				gamePanel.startGameLoop();
				
			}
		});
		
		
		//버튼을 담을 패널 생성
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 100)); //중앙정렬, 상단 여백 100
		buttonPanel.add(startButton);
		
		add(buttonPanel, BorderLayout.CENTER);
		
		setVisible(true);
		
		
	}
	
	

}
