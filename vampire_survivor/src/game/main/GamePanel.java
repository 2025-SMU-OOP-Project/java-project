package game.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// 패키지들 import
import game.entity.player.Player;
import game.entity.monster.Monster;
import game.state.GameState;
/**
 * 실제 게임이 진행되는 패널입니다.
 * JPanel을 상속받고, 키 입력을 처리하기 위해 KeyListener를 구현합니다.
 */
public class GamePanel extends JPanel {

    // --- 상수 정의 ---
    private final int SCREEN_WIDTH = 800;
    private final int SCREEN_HEIGHT = 600;
    private final int FPS = 60; // 초당 프레임 수

    // 클래스 가져오기
    public game.state.GameState gameState;
    public KeyHandler keyH = new KeyHandler(this);
    public Player player;
    
    
    // 몬스터 관리
    public List<Monster> monsters = new ArrayList<>();
    private Random rand = new Random();
    private int spawnTimer = 0;
    private final int SPAWN_INTERVAL = 60;

    private Image batImage, mummyImage, slimeImage;
    
    //  배경 관리 
    private Image backgroundImage;
    private int bgWidth, bgHeight;
    
    // 배경의 현재 위치 (카메라 오프셋)
    //private int backgroundX = 0;
    //private int backgroundY = 0;
    
    // 게임 루프
    private Timer gameTimer;
    
    

    /**
     * GamePanel 생성자
     */
    public GamePanel() {
    	// 1. 패널 설정
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true); // 깜빡임 방지
        this.setFocusable(true);
        
        // 2. 리스너 등록 (이제 KeyHandler가 처리)
        this.addKeyListener(keyH);

        // 3. 초기화
        loadImages(); // 배경 및 몬스터 이미지 로드
        player = new Player(this, keyH); // 플레이어 생성 (패널과 키핸들러 전달)
        gameState = GameState.RUNNING;   // 게임 시작 상태

        // 4. 게임 루프 시작
        startGameLoop();
    }

    /**
     * 'images' 폴더에서 이미지 파일들을 로드합니다.
     */
    private void loadImages() {
        try {

            
            // 배경 이미지 로드 ('images/background.png')
            ImageIcon iconBg = new ImageIcon(getClass().getResource("/images/10background_1.png"));
            backgroundImage = iconBg.getImage();
            
            bgWidth = backgroundImage.getWidth(this);
            bgHeight = backgroundImage.getHeight(this);
            

            //  몬스터 이미지 로드 
            batImage = new ImageIcon(getClass().getResource("/images/monsters/bat.png")).getImage();
            mummyImage = new ImageIcon(getClass().getResource("/images/monsters/mummy.png")).getImage();
            slimeImage = new ImageIcon(getClass().getResource("/images/monsters/slime.png")).getImage();
            

        } catch (Exception e) {
            e.printStackTrace();
            // 이미지 로드 실패 시 콘솔에 에러 출력
            System.err.println("이미지 로드에 실패했습니다. 'images' 폴더를 확인하세요.");
        }
    }

    //game loop start
    public void startGameLoop() {
        int delay = 1000 / FPS; // 1초 / 60
        
        gameTimer = new Timer(1000 / FPS, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 1. 게임 상태 업데이트 (배경 위치 변경)
                update();
                repaint();
            }
        });
        gameTimer.start(); // 타이머 시작
    }

    /**
     * 게임 상태를 업데이트합니다. (주로 배경 위치 이동)
     */
    public void update() {
        // RUNNING 상태일 때만 움직임 계산
        if (gameState == GameState.RUNNING) {
            
            // 1. 플레이어 업데이트 (이동 로직은 Player 클래스 내부에 있음)
            player.update();

            // 2. 몬스터 스폰 및 이동
            updateSpawning();
            updateMonsters();
            
            for (int i = 0; i< monsters.size(); i++) {
            	Monster monster = monsters.get(i);
            	monster.update(player.worldX, player.worldY);
            	
            	if (player.getBounds().intersects(monster.getBounds())) {
            		player.takeDamage(10);
            	}
            }
            
        }
    }
    // 몬스터 스폰 타이머 업데이트
    
    private void updateSpawning() {
    	spawnTimer++;
    	if (spawnTimer >= SPAWN_INTERVAL) {
    		spawnMonster();
    		spawnTimer = 0;
    	}
    }
    // 몬스터 AI 업데이트
    
    private void updateMonsters() {
        // 몬스터는 플레이어 위치를 알아야 추적 가능하므로 플레이어 정보를 넘기거나 참조
        // 여기서는 각 몬스터에게 update() 명령만 내림
        for (Monster monster : monsters) {
            // 몬스터 AI (플레이어 쪽으로 이동)
            // 플레이어의 실제 월드 좌표를 전달해야 함
            monster.update(player.worldX, player.worldY);
        }
    }
    // 맵 가장자리에 몬스터 스폰
    private void spawnMonster() {
    	//스폰될 몬스터 랜덤선택
    	int monsterType = rand.nextInt(3);
    	Image selectedImage = null;
    	
    	
    	switch (monsterType) {
        case 0: selectedImage = batImage; break;
        case 1: selectedImage = mummyImage; break;
        default: selectedImage = slimeImage; break;
    	}
    	// 스폰 위치 계산 (플레이어 주변 랜덤 위치)
        // 플레이어 월드 좌표 기준으로 화면 밖 랜덤 위치 설정
        // (간단하게 플레이어 근처 -400 ~ +1200 범위 등으로 설정)
        int spawnX = player.worldX + rand.nextInt(1600) - 400; 
        int spawnY = player.worldY + rand.nextInt(1200) - 300;

        if (selectedImage != null) {
            monsters.add(new Monster(spawnX, spawnY, selectedImage));
        }
    } 	
    	
    	
    	
 // --- ⭐ PAINT: 화면 그리기 ---
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 1. 배경 그리기 (플레이어 중심 카메라)
        drawBackground(g);

        // 2. 몬스터 그리기
        for (Monster monster : monsters) {
            // 몬스터에게 "플레이어 정보"를 줘서 화면 어디에 그릴지 계산하게 함
            monster.draw(g, player); 
        }

        // 3. 플레이어 그리기 (항상 화면 중앙)
        player.draw(g);

        // 4. UI 그리기 (HP바 등)
        drawUI(g);

        // 5. 일시정지 화면 (반투명 오버레이)
        if (gameState == GameState.PAUSED) {
            drawPauseScreen(g);
        }
    }
    
    private void drawBackground(Graphics g) {
        if (backgroundImage != null) {
            // 플레이어가 움직인 만큼 배경을 반대로 이동 (-player.worldX)
            // 하지만 플레이어가 화면 중앙(player.screenX)에 있으므로 보정 필요
            // 배경은 무한 반복(타일링)되므로 나머지 연산(%) 사용

            int offsetX = -(player.worldX % bgWidth);
            int offsetY = -(player.worldY % bgHeight);

            // 화면 중앙 보정을 위해 플레이어의 screen 좌표값만큼 더해줄 수도 있으나
            // 보통 타일링 배경은 플레이어 이동량의 나머지값으로 처리하는 게 깔끔함

            // 음수 보정 (자연스러운 연결)
            if (offsetX > 0) offsetX -= bgWidth;
            if (offsetY > 0) offsetY -= bgHeight;

            // 화면 전체를 덮을 때까지 반복 그리기
            for (int x = offsetX - bgWidth; x < SCREEN_WIDTH + bgWidth; x += bgWidth) {
                for (int y = offsetY - bgHeight; y < SCREEN_HEIGHT + bgHeight; y += bgHeight) {
                    g.drawImage(backgroundImage, x, y, this);
                }
            }
        }
    }

    private void drawUI(Graphics g) {
        // HP바 예시
        g.setColor(Color.GRAY);
        g.fillRect(20, 20, 200, 20); // 배경
        g.setColor(Color.RED);
        // player.getMaxHp()가 0이면 에러나므로 체크
        if (player.getMaxHp() > 0) {
            int hpWidth = (int)(200 * ((double)player.getCurrentHp() / player.getMaxHp()));
            g.fillRect(20, 20, hpWidth, 20); // 현재 체력
        }
        g.setColor(Color.WHITE);
        g.drawRect(20, 20, 200, 20); // 테두리
        
        // 좌표 디버깅
        g.drawString("World: " + player.worldX + ", " + player.worldY, 20, 60);
    }
    
    private void drawPauseScreen(Graphics g) {
        g.setColor(new Color(0, 0, 0, 150)); // 반투명 검정
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        String text = "PAUSED";
        // 중앙 정렬 계산
        FontMetrics fm = g.getFontMetrics();
        int x = (SCREEN_WIDTH - fm.stringWidth(text)) / 2;
        int y = SCREEN_HEIGHT / 2;
        g.drawString(text, x, y);
    }

    // 다른 클래스에서 화면 크기 참조용
    public int getScreenWidth() { return SCREEN_WIDTH; }
    public int getScreenHeight() { return SCREEN_HEIGHT; }
}