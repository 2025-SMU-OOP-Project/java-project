package game.entity.monster;

import java.awt.*;
import javax.swing.ImageIcon;

import game.entity.player.Player;

public class Monster {
    
    public int worldX, worldY; // 맵 상의 절대 좌표
    public int speed;
    public Image image;
    public int width = 30, height = 30;
    
    // 생성자
    public Monster(int x, int y, Image image) {
        this.worldX = x;
        this.worldY = y;
        this.image = image;
        this.speed = 1;
    }

    // 플레이어를 향해 이동하는 AI
    public void update(int playerWorldX, int playerWorldY) {
        if (worldX < playerWorldX) worldX += speed;
        if (worldX > playerWorldX) worldX -= speed;
        if (worldY < playerWorldY) worldY += speed;
        if (worldY > playerWorldY) worldY -= speed;
    }

    public void draw(Graphics g, Player player) {
        // 배경의 위치(backgroundX, Y)를 고려하여 화면상의 좌표 계산
        int screenX = worldX - player.worldX + player.screenX;
        int screenY = worldY - player.worldY + player.screenY;
        
        // 화면 안에 들어올 때만 그리기 (최적화)
        if (screenX > -width && screenX < 800 && screenY > -height && screenY < 600) {
            g.drawImage(image, screenX, screenY, width, height, null);
        }
    }
}
