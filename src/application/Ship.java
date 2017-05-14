package application;

import javafx.scene.Parent;

public class Ship extends Parent {
    public int type;
    public boolean isVertical = true;

    private int dodai;

    public Ship(int a, boolean b) {
        this.type = a;
        this.isVertical = b;	// Chuột trái = đặt tàu d�?c, các nút còn lại đặt tàu ngang
        dodai = type;
    }

    public void shoot() {
        dodai--;
    }

    public boolean isDead() {
        return dodai <= 0;
    }
    
}