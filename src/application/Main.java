// Only run on Java 8
// Using Lambda Expressions

package application;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import application.Board.Elements;
import application.Menu.MenuButton;
import javafx.application.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Main extends Application implements Battleship {
	Board player1, player2;

	MediaPlayer mediaPlayer;

	private boolean isWon = false;
	private boolean createdship = false;

	private int sotau = SHIPS;
	private boolean turn = false;

	private Parent createContent() throws IOException {
		BorderPane root = new BorderPane();
		root.setPrefSize(800, 600);
		root.setPadding(new Insets(20)); // khoảng cách khung application

		String ship_sound = "sound/ship.mp3";
		Media ship_mp3 = new Media(new File(ship_sound).toURI().toString());
		mediaPlayer = new MediaPlayer(ship_mp3);
		if (!Menu.isMute) mediaPlayer.play();
		mediaPlayer.setVolume(0.5);
		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

		InputStream is = Files.newInputStream(Paths.get(("img/main.jpg")));
		Image img = new Image(is);
		is.close();

		ImageView imgView = new ImageView(img);
		imgView.setFitWidth(800);
		imgView.setFitHeight(600);
		root.getChildren().addAll(imgView);

		// Header
		Text name = new Text(GAMENAME);
		name.setFont(Font.font("Calibri", FontWeight.BOLD, 32));
		HBox top = new HBox(name);
		top.setAlignment(Pos.CENTER);
		root.setTop(top);

		// Player name
		Text playerI = new Text(Menu.playername);
		playerI.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
		Text playerII = new Text("Enemy");
		playerII.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
		HBox player = new HBox(290, playerI, playerII);
		player.setAlignment(Pos.CENTER);

		player1 = new Board(true, sukien -> {
			if (isWon)
				return;

			if (!createdship)
				return;

			Elements ovuong = (Elements) sukien.getSource();
			if (ovuong.isFire)
				return;

			turn = !ovuong.isHit();
			Board.Fade(ovuong);

			if (player1.ships == 0) {
				ShowMessage("CONGRATULATION!!! YOU HAVE WON THE GAME");
				isWon = true;
			}

			if (turn)
				AITurn();

		});

		player2 = new Board(false, sukien -> {
			if (isWon)
				return;

			if (createdship)
				return;

			Elements ovuong = (Elements) sukien.getSource();
			if (player2.CreateShip(new Ship(sotau, sukien.getButton() == MouseButton.PRIMARY), ovuong.x, ovuong.y)) {
				if (--sotau == 0) { // minus first to compare
					CreateShipForAI();
				}
			}
		});

		HBox Board = new HBox(50, player2, player1);
		Board.setAlignment(Pos.CENTER);
		VBox center = new VBox(player, Board);
		root.setCenter(center);

		// Close button
		MenuButton btnReturn = new MenuButton("RETURN TO MAIN MENU");
		btnReturn.setOnMouseClicked(sukien -> {
			Stage stage = (Stage) btnReturn.getScene().getWindow();
			stage.close();
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					// Your class that extends Application
					try {
						new Menu().start(new Stage());
						mediaPlayer.dispose();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		});
		btnReturn.setAlignment(Pos.CENTER);
		HBox close = new HBox(btnReturn);
		close.setAlignment(Pos.CENTER);
		root.setBottom(close);

		return root;
	}

	private void AITurn() {
		while (turn) {
			List<int[]> temp = new ArrayList<int[]>();
			int[] randInt = new int[2];
			do {
				randInt[0] = rand.nextInt(SQUARE - 1) + 1;
				randInt[1] = rand.nextInt(SQUARE - 1) + 1;
			} while (temp.contains(randInt));
			temp.add(randInt);

			Elements ovuong = player2.getLocation(randInt[0], randInt[1]);
			if (ovuong.isFire)
				continue;

			turn = ovuong.isHit();
			Board.Fade(ovuong);

			if (player2.ships == 0) {
				ShowMessage("I'M SORRY!!! YOU JUST LOST THE GAME");
				isWon = true;
			}
		}
	}

	private void CreateShipForAI() {

		// place random ships for player 1
		int type = 5;
		int x = 0, y = 0;
		while (type > 0) {
			int[] sotau = new int[SHIPS];
			for (int i : sotau) {
				x = tao_so_random(sotau, i);
				y = tao_so_random(sotau, i);
			}

			if (player1.CreateShip(new Ship(type, Math.random() < 0.5), x, y)) {
				type--;
			}
		}

		createdship = true;
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			Scene scene = new Scene(createContent());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Battleship");
			primaryStage.setResizable(false);

			InputStream icon = Files.newInputStream(Paths.get("img/icon.jpg"));
			Image iconimg = new Image(icon);
			icon.close();
			primaryStage.getIcons().add(iconimg);

			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static int tao_so_random(int[] a, int i) {
		int randInt;
		do {
			randInt = rand.nextInt(SQUARE); // No se random tu 1 toi 10
		} while (Arrays.asList(a).contains(randInt));

		a[i] = randInt;
		return randInt;
	}

	private void ShowMessage(String message) {
		// System.out.println(message);

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.setTitle("Battleship");
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		// Icon
		InputStream icon;
		try {
			icon = Files.newInputStream(Paths.get("img/icon.jpg"));
			Image iconimg = new Image(icon);
			icon.close();
			stage.getIcons().add(iconimg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		alert.showAndWait();
	}

	public static void closeMyApp(Button a) {
		try {
			Stage stage = (Stage) a.getScene().getWindow();
			stage.close();
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}
}