	package game.entity.player;

	import java.awt.*;
	import javax.swing.ImageIcon;
	import game.main.GamePanel;
	import game.main.KeyHandler;

	public class Player {

    GamePanel gp;
    KeyHandler keyH;

    // 월드 좌표 (게임 맵 상의 진짜 위치)
    public int worldX, worldY;
    public int speed;
    
    // 화면상에 고정될 좌표 (중앙)
    public final int screenX;
    public final int screenY;

    public int width = 40, height = 40;
    
    // 스탯
    private int maxHp = 100;
    private int currentHp = 100;
    private int exp = 0;
    private int level = 1;
    private int nextLevelExp = 50; //다음레벨까지 경험치
    
    //충돌 무적 쿨타임
    private boolean isInvincible = false;
    private int invincibleCounter = 0;
    private final int INVINCIBLE_TIME = 60;  //1초 무적
    
    
    private Image image;

    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        // 화면 중앙 좌표 계산 (고정값)
        // 플레이어 크기의 절반만큼 빼줘야 정확히 정중앙에 옴
        screenX = (gp.getScreenWidth() / 2) - (width / 2);
        screenY = (gp.getScreenHeight() / 2) - (height / 2);

        // 초기 월드 좌표 (맵의 정중앙 등에서 시작)
        worldX = 0; // 혹은 1000
        worldY = 0; // 혹은 1000
        speed = 4;

        loadImage();
    }

    private void loadImage() {
        image = new ImageIcon(getClass().getResource("/images/character_1.png")).getImage();
    }

    public void update() {
        // 키 입력에 따라 '월드 좌표'를 변경합니다.
        if (keyH.upPressed)    worldY -= speed;
        if (keyH.downPressed)  worldY += speed;
        if (keyH.leftPressed)  worldX -= speed;
        if (keyH.rightPressed) worldX += speed;
        
    

    
    	// 무적 후 해제 코드
    	if (isInvincible) {
    		invincibleCounter++;
    		if (invincibleCounter > INVINCIBLE_TIME) {
    			isInvincible = false;
    			invincibleCounter = 0;
    		}
    	}
    }
    	
    public void draw(Graphics g) {
        // ⭐ 그릴 때는 worldX, worldY가 아니라 '고정된 screenX, screenY'에 그립니다.
        if (image != null) {
            g.drawImage(image, screenX, screenY, width, height, null);
        }
        
        // (디버깅용: 내 실제 좌표 표시)
        // g.setColor(Color.WHITE);
        // g.drawString("World: " + worldX + "," + worldY, screenX, screenY - 10);
    }
    
    //충돌 감지 영역 반환
    public Rectangle getBounds() {
    	return new Rectangle(worldX, worldY, width, height);
    }
    
    //데미지 받는 메서드
    public void takeDamage(int damage) {
    	if(!isInvincible) {
    		currentHp -= damage;
    		isInvincible = true;
    		
    		if (currentHp <= 0 ) {
    			currentHp = 0;
    			
    			System.out.println("GAME OVER");
    		}
    	}
    }
    
    
    public void gainExp(int amount) {
    	exp += amount;
    	if (exp >= nextLevelExp) {
    		levelUp();
    	}
    }
    
    private void levelUp() {
    	level++;
    	exp -= nextLevelExp;
    	nextLevelExp = (int)(nextLevelExp * 1.2);
    	maxHp += 10;
    	currentHp = maxHp;
    }
    
    
    

    // Getter
    public int getCurrentHp() { return currentHp; }
    public int getMaxHp() { return maxHp; }
    public int getExp() { return exp; }
    public int getLevel() { return level; }
}
	
	