package application;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javafx.animation.FadeTransition;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

interface Battleship {
	public final static int SQUARE = 10;
	public final static int SHIPS = 5;
	public final static String GAMENAME = "BATTLESHIP";
	public static Random rand = new Random();
	public final static String VERSION = "3.1";
}

public class Board extends Parent implements Battleship {
	public int ships = SHIPS;
	public boolean isBot = false;
	private VBox cot = new VBox();
	
	public class Elements extends Rectangle {
		public int x, y; // Tọa độ của ô vuông
		private Board board;
		public Ship ship = null; // Khởi tạo object tàu
		public boolean isFire = false; // Biến kiểm tra tàu bị bắn chưa
		
		public Elements(int a, int b, Board board) {
			super(30, 30);	// Chiều dài ô vuông extends từ Rectangle
			this.x = a;
			this.y = b;
			this.board = board;
			setFill(Color.LIGHTBLUE);
			setStroke(Color.BLACK);
		}
		
		public boolean isHit() {
			isFire = true;
			
			if (ship != null) {
				ship.shoot();
				setFill(Color.RED);
				if (ship.isDead()) board.ships--;
				return true;
			}
			
			setFill(Color.DARKBLUE); // Bắn trượt
			return false;
		}
	}
	
	public Board(boolean Player, EventHandler<? super MouseEvent> handler) {
		this.isBot = Player;
		for (int y = 0; y < SQUARE; y++) {
			 HBox hang = new HBox(); // Sắp xếp trên 1 hàng
	            for (int x = 0; x < SQUARE; x++) {
	                Elements c = new Elements(x, y, this); // Tạo ô với tọa độ x, y vào Board người chơi này
                c.setOnMouseClicked(handler);
                hang.getChildren().add(c);
            }

            cot.getChildren().add(hang);
        }

        getChildren().add(cot);
	}
	
	public boolean CreateShip(Ship ship, int x, int y) {
		if (checkViTri(ship, x, y)) {
			int length = ship.type;
			
			if (ship.isVertical) {
				for (int i = y; i < y + length; i++) {
					Elements ovuong = getLocation(x, i);
					ovuong.ship = ship;
					if (!isBot) {
						ovuong.setFill(Color.GREEN);
						ovuong.setStroke(Color.WHITE);
						Fade(ovuong);
					}
				}
			}
			else {
				for (int i = x; i < x + length; i++) {
					Elements ovuong = getLocation(i, y);
					ovuong.ship = ship;
					if (!isBot) {
						ovuong.setFill(Color.GREEN);
						ovuong.setStroke(Color.WHITE);
						Fade(ovuong);
					}
				}
			}
			return true;
		}
		return false;
	}
	
    private boolean checkViTri(Ship ship, int x, int y) {
        int length = ship.type;

        if (ship.isVertical) {
            for (int i = y; i < y + length; i++) {
                if (!isPlacable(x, i))
                    return false;

                Elements ovuong = getLocation(x, i);
                if (ovuong.ship != null)
                    return false;

                
                // Không thể đặt tàu ở sát nhau
                for (Elements tau : checkXungQuanh(x, i)) {
                    if (!isPlacable(x, i))
                        return false;

                    if (tau.ship != null)
                        return false;
                }
            }
        }
        else {
            for (int i = x; i < x + length; i++) {
            	if (!isPlacable(i, y)) // Nếu không đủ độ dài
                    return false;

                Elements cell = getLocation(i, y);
                if (cell.ship != null) // Nếu có tàu ở đó
                    return false;

                for (Elements tau : checkXungQuanh(i, y)) {
                    if (!isPlacable(i, y))
                        return false;

                    if (tau.ship != null)
                        return false;
                }
            }
        }

        return true;
    }

    public Elements getLocation(int x, int y) {
    	return (Elements)((HBox)cot.getChildren().get(y)).getChildren().get(x); // Lấy 1 hàng từ biến "cột" sau đó convert sang HBox để lấy children, rồi lấy children của x ra được node convert qua Elements
    }

    private boolean isPlacable(double x, double y) {
    	return x >= 0 && x < SQUARE && y >= 0 && y < SQUARE;
    }
    
    private boolean kiemtraArray(Array2D a) {
    	return isPlacable(a.getX(), a.getY());
	}

    private Elements[] checkXungQuanh(int x, int y) {
        Array2D[] array = new Array2D[] {
                new Array2D(x - 1, y),
                new Array2D(x + 1, y),
                new Array2D(x, y - 1),
                new Array2D(x, y + 1),
                new Array2D(x+1, y+1),
                new Array2D(x-1, y-1),
                new Array2D(x-1, y+1),
                new Array2D(x+1, y-1)
        };

        List<Elements> list = new LinkedList<Elements>();

        // Check nếu ô đặt tàu không nằm sát tàu thì add vào list
        for (Array2D p : array) {
            if (kiemtraArray(p)) {
                list.add(getLocation((int)p.getX(), (int)p.getY()));
            }
        }

        return list.toArray(new Elements[0]);	// cast về array Elements
    }
    
    public static void Fade(Elements ovuong) {
		FadeTransition ft = new FadeTransition(Duration.millis(10), ovuong);
	    ft.setFromValue(0);
	    ft.setToValue(1);
	    ft.setAutoReverse(true);
	    ft.play();
    }
}