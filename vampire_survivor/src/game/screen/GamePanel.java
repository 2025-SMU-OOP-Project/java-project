package game.screen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * 실제 게임이 진행되는 패널입니다.
 * JPanel을 상속받고, 키 입력을 처리하기 위해 KeyListener를 구현합니다.
 */
public class GamePanel extends JPanel implements KeyListener {

    // --- 상수 정의 ---
    private final int SCREEN_WIDTH = 800;
    private final int SCREEN_HEIGHT = 600;
    private final int PLAYER_WIDTH = 40;
    private final int PLAYER_HEIGHT = 40;
    private final int PLAYER_SPEED = 5; // 배경이 움직이는 속도
    private final int FPS = 60; // 초당 프레임 수

    // --- 이미지 및 위치 변수 ---
    private Image playerImage;
    private Image backgroundImage;
    private int bgWidth, bgHeight; // 배경 이미지의 크기

    // 배경의 현재 위치 (카메라 오프셋)
    private int backgroundX = 0;
    private int backgroundY = 0;
    
    // 플레이어는 항상 화면 중앙에 고정
    private final int PLAYER_SCREEN_X = (SCREEN_WIDTH / 2) - (PLAYER_WIDTH / 2);
    private final int PLAYER_SCREEN_Y = (SCREEN_HEIGHT / 2) - (PLAYER_HEIGHT / 2);

    // --- 입력 및 게임 루프 ---
    private boolean upPressed, downPressed, leftPressed, rightPressed;
    private Timer gameTimer;

    /**
     * GamePanel 생성자
     */
    public GamePanel() {
        // 1. 이미지 로드
        loadImages();
        
        // 2. 패널 기본 설정
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(Color.BLACK); // 이미지가 로드되지 않았을 때의 기본 배경색
        
        // 3. 키 입력을 받을 수 있도록 설정
        setFocusable(true); // 이 패널이 키 입력을 받을 수 있도록 포커스를 설정
        addKeyListener(this); // KeyListener 등록
    }

    /**
     * 'res' 폴더에서 이미지 파일들을 로드합니다.
     */
    private void loadImages() {
        try {
            // 플레이어 이미지 로드 ('images/player.png')
            ImageIcon iconPlayer = new ImageIcon(getClass().getResource("/images/character_1.png"));
            playerImage = iconPlayer.getImage();
            
            // 배경 이미지 로드 ('images/background.png')
            ImageIcon iconBg = new ImageIcon(getClass().getResource("/images/10background_1.png"));
            backgroundImage = iconBg.getImage();
            
            // 배경 이미지의 원본 크기를 저장 (타일링에 필요)
            bgWidth = backgroundImage.getWidth(this);
            bgHeight = backgroundImage.getHeight(this);

        } catch (Exception e) {
            e.printStackTrace();
            // 이미지 로드 실패 시 콘솔에 에러 출력
            System.err.println("이미지 로드에 실패했습니다. 'images' 폴더를 확인하세요.");
        }
    }

    //game loop start
    public void startGameLoop() {
        int delay = 1000 / FPS; // 1초 / 60
        
        gameTimer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 1. 게임 상태 업데이트 (배경 위치 변경)
                updateGame();
                // 2. 화면 다시 그리기 (paintComponent 호출)
                // repaint()는 updateGame 내부에서 호출
            }
        });
        gameTimer.start(); // 타이머 시작
    }

    /**
     * 게임 상태를 업데이트합니다. (주로 배경 위치 이동)
     */
    private void updateGame() {
        // 키 입력에 따라 '배경'을 '반대' 방향으로 이동
        
        // 플레이어가 위로 -> 배경을 아래로
        if (upPressed) backgroundY += PLAYER_SPEED;
        //플레이어가 아래로 -> 배경을 위로
        if (downPressed) backgroundY -= PLAYER_SPEED;
        // 플레이어가 왼쪽으로 -> 배경을 오른쪽으로
        if (leftPressed) backgroundX += PLAYER_SPEED;
        //플레이어가 오른쪽으로 -> 배경을 왼쪽으로
        if (rightPressed) backgroundX -= PLAYER_SPEED;
        
        // 화면을 다시 그리기
        repaint(); 
    }

    /**
     *  화면을 그리는 메서드 (Swing에 의해 자동으로 호출됨)
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // 부모 클래스(JPanel)의 그리기 메서드 호출

        // --- 1. 배경 그리기 (타일링) ---
        if (backgroundImage != null && bgWidth > 0 && bgHeight > 0) {
            // 그리기 시작할 기준 오프셋 계산 (현재 위치 % 이미지 크기)
            int offsetX = backgroundX % bgWidth;
            int offsetY = backgroundY % bgHeight;

            // 오프셋이 양수일 때, 빈 공간이 생기지 않도록 한 칸 뒤부터 그리기
            if (offsetX > 0) offsetX -= bgWidth;
            if (offsetY > 0) offsetY -= bgHeight;

            // 화면을 모두 덮을 때까지 배경 이미지를 반복해서 그리기
            for (int x = offsetX; x < SCREEN_WIDTH; x += bgWidth) {
                for (int y = offsetY; y < SCREEN_HEIGHT; y += bgHeight) {
                    g.drawImage(backgroundImage, x, y, this);
                }
            }
        } else {
            // 이미지 로드 실패 시 검은색 배경
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        }
        
        // --- 2. 플레이어 그리기 ---
        // 플레이어는 항상 배경 위에, 화면 중앙에 그립니다.
        if (playerImage != null) {
            g.drawImage(playerImage, PLAYER_SCREEN_X, PLAYER_SCREEN_Y, PLAYER_WIDTH, PLAYER_HEIGHT, this);
        }
        
        // (선택 사항) 디버그용 텍스트
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("BG X: " + backgroundX + ", BG Y: " + backgroundY, 10, 20);
    }

    // --- KeyListener 구현 메서드 ---

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) upPressed = true;
        if (key == KeyEvent.VK_S) downPressed = true;
        if (key == KeyEvent.VK_A) leftPressed = true;
        if (key == KeyEvent.VK_D) rightPressed = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) upPressed = false;
        if (key == KeyEvent.VK_S) downPressed = false;
        if (key == KeyEvent.VK_A) leftPressed = false;
        if (key == KeyEvent.VK_D) rightPressed = false;
    }

    // 사용하지 않음
    @Override
    public void keyTyped(KeyEvent e) {}
}